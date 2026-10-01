package com.royaumedesidees.monde;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowyDirtBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Générateur de chunks du Royaume. Il ne lit jamais la seed du monde : chaque bloc dépend
 * uniquement de ses coordonnées, via {@link ReliefRoyaume}. Pas de grottes, pas de décoration,
 * pas de structures vanilla : les structures sont posées par le mod, pas par la génération.
 */
public class GenerateurRoyaume extends ChunkGenerator {
    public static final MapCodec<GenerateurRoyaume> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BiomeSource.CODEC.fieldOf("biome_source").forGetter(generateur -> generateur.biomeSource)
    ).apply(instance, GenerateurRoyaume::new));

    public static final int HAUTEUR_MONDE = 384;
    /** En dessous, la roche devient de l'ardoise des abîmes. */
    private static final int Y_ARDOISE = 24;

    private static final BlockState AIR = Blocks.AIR.defaultBlockState();
    private static final BlockState EAU = Blocks.WATER.defaultBlockState();

    public GenerateurRoyaume(BiomeSource sourceBiomes) {
        super(sourceBiomes);
    }

    @Override
    protected MapCodec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Blender melange, RandomState aleatoire, StructureManager structures, ChunkAccess chunk) {
        Heightmap fondOcean = chunk.getOrCreateHeightmapUnprimed(Heightmap.Types.OCEAN_FLOOR_WG);
        Heightmap surfaceMonde = chunk.getOrCreateHeightmapUnprimed(Heightmap.Types.WORLD_SURFACE_WG);
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        int minX = chunk.getPos().getMinBlockX();
        int minZ = chunk.getPos().getMinBlockZ();

        // Colonnes du chunk plus une bordure d'un bloc, pour connaître la pente de chaque colonne.
        ReliefRoyaume.Colonne[][] grille = new ReliefRoyaume.Colonne[18][18];
        for (int i = 0; i < 18; i++) {
            for (int j = 0; j < 18; j++) {
                grille[i][j] = ReliefRoyaume.colonne(minX + i - 1, minZ + j - 1);
            }
        }

        for (int lx = 0; lx < 16; lx++) {
            for (int lz = 0; lz < 16; lz++) {
                ReliefRoyaume.Colonne colonne = grille[lx + 1][lz + 1];
                if (!colonne.ile()) {
                    continue;
                }
                int pente = ReliefRoyaume.pente(colonne, grille[lx + 2][lz + 1], grille[lx][lz + 1], grille[lx + 1][lz + 2], grille[lx + 1][lz]);
                int x = minX + lx;
                int z = minZ + lz;
                int haut = Math.max(colonne.surface(), colonne.sousLEau() ? ReliefRoyaume.NIVEAU_MER : colonne.surface());
                for (int y = colonne.fond(); y <= haut; y++) {
                    BlockState etat = bloc(colonne, pente, x, y, z);
                    chunk.setBlockState(pos.set(lx, y, lz), etat, false);
                    fondOcean.update(lx, y, lz, etat);
                    surfaceMonde.update(lx, y, lz, etat);
                }
            }
        }
        VegetationRoyaume.decorer(chunk, fondOcean, surfaceMonde);
        return CompletableFuture.completedFuture(chunk);
    }

    /** Pente à partir de laquelle la terre ne tient plus : la roche du biome affleure. */
    private static final int PENTE_ROCHEUSE = 3;

    /**
     * Le bloc à la hauteur y d'une colonne de l'île (entre son fond et la surface de l'eau).
     *
     * <p>Chaque biome a son propre sol, choisi d'après le vrai lieu :
     * <ul>
     *   <li>Jardin de Milan : terre de jardin, roche grise dans les pentes ;</li>
     *   <li>Port-Royal (vallée marécageuse du Rhodon) : boue et argile ;</li>
     *   <li>Puy de Dôme : dôme de trachyte clair (la « domite »), petits puys en scories sombres ;</li>
     *   <li>Hippone (côte d'Afrique du Nord) : terre rouge méditerranéenne sur du calcaire.</li>
     * </ul>
     */
    public static BlockState bloc(ReliefRoyaume.Colonne colonne, int pente, int x, int y, int z) {
        int surface = colonne.surface();
        if (y > surface) {
            return y <= ReliefRoyaume.NIVEAU_MER ? EAU : AIR;
        }
        int profondeur = surface - y;
        BlockState roche = y < Y_ARDOISE ? Blocks.DEEPSLATE.defaultBlockState() : Blocks.STONE.defaultBlockState();
        int hasard = hachage(x, y, z);

        // Falaises du bord : toute la paroi en roche claire, comme des falaises de craie au-dessus des nuages.
        if (colonne.rebord() > 4) {
            return rochePale(hasard);
        }
        // Dessous de l'île, visible depuis le vide : roche claire, presque nacrée.
        if (profondeur > 4 && y - colonne.fond() < 6) {
            return rochePale(hasard);
        }
        boolean raide = pente >= PENTE_ROCHEUSE;
        // Petite marche (1 ou 2 blocs) : on verrait le flanc de terre du bloc d'herbe. On y met un bloc
        // dont le flanc est de la même couleur que le dessus (mousse, ou grès clair à Hippone).
        boolean bordDeMarche = !raide && profondeur < Math.max(pente, 1) && pente >= 1;

        return switch (colonne.zoneSol()) {
            case JARDIN_MILAN -> {
                if (profondeur > 3) {
                    yield roche;
                }
                if (raide) {
                    yield (hasard % 3 == 0 ? Blocks.ANDESITE : Blocks.STONE).defaultBlockState();
                }
                if (bordDeMarche) {
                    yield Blocks.MOSS_BLOCK.defaultBlockState();
                }
                yield profondeur == 0 ? Blocks.GRASS_BLOCK.defaultBlockState() : Blocks.DIRT.defaultBlockState();
            }
            case PORT_ROYAL -> {
                if (profondeur > 4) {
                    yield roche;
                }
                if (surface < ReliefRoyaume.NIVEAU_MER) {
                    // Fond des flaques et des étangs.
                    yield profondeur < 2 ? Blocks.MUD.defaultBlockState() : Blocks.CLAY.defaultBlockState();
                }
                if (surface <= ReliefRoyaume.NIVEAU_MER + 2) {
                    // Fond du marais : herbe détrempée, mousse et un peu de boue à nu, sur de la boue et de l'argile.
                    if (profondeur == 0) {
                        int tirage = hasard % 100;
                        yield (tirage < 18 ? Blocks.MUD : tirage < 30 || bordDeMarche ? Blocks.MOSS_BLOCK : Blocks.GRASS_BLOCK).defaultBlockState();
                    }
                    if (bordDeMarche) {
                        yield Blocks.MOSS_BLOCK.defaultBlockState();
                    }
                    yield profondeur < 3 ? Blocks.MUD.defaultBlockState() : Blocks.CLAY.defaultBlockState();
                }
                if (raide) {
                    yield profondeur < 3 ? Blocks.CLAY.defaultBlockState() : roche;
                }
                if (bordDeMarche) {
                    yield Blocks.MOSS_BLOCK.defaultBlockState();
                }
                // Coteaux : un peu de terre sur de l'argile.
                yield profondeur == 0 ? Blocks.GRASS_BLOCK.defaultBlockState() : profondeur == 1 ? Blocks.DIRT.defaultBlockState() : Blocks.CLAY.defaultBlockState();
            }
            case PUY_DE_DOME -> {
                if (profondeur > 4) {
                    yield roche;
                }
                // Petits puys : cônes de scories, basalte et tuf, pouzzolane rouge dans les pentes.
                if (colonne.petitPuy()) {
                    if (raide || profondeur > 0) {
                        yield (hasard % 4 == 0 ? Blocks.RED_TERRACOTTA : hasard % 4 == 1 ? Blocks.BASALT : Blocks.TUFF).defaultBlockState();
                    }
                    yield (bordDeMarche ? Blocks.MOSS_BLOCK : Blocks.GRASS_BLOCK).defaultBlockState();
                }
                // Dôme : trachyte clair sous une herbe rase.
                if (colonne.altitudeDome() > 0.05) {
                    BlockState domite = (hasard % 3 == 0 ? Blocks.DIORITE : Blocks.ANDESITE).defaultBlockState();
                    if (profondeur > 0 || raide || colonne.affleurement()) {
                        yield domite;
                    }
                    if (bordDeMarche) {
                        yield Blocks.MOSS_BLOCK.defaultBlockState();
                    }
                    if (surface >= ReliefRoyaume.NEIGE_Y) {
                        // Herbe sous une fine couche de neige (la neige est posée par VegetationRoyaume).
                        yield Blocks.GRASS_BLOCK.defaultBlockState().setValue(SnowyDirtBlock.SNOWY, true);
                    }
                    yield Blocks.GRASS_BLOCK.defaultBlockState();
                }
                // Plaine : prairie sur une mince couche de terre, puis tuf volcanique.
                if (raide) {
                    yield Blocks.TUFF.defaultBlockState();
                }
                if (bordDeMarche) {
                    yield Blocks.MOSS_BLOCK.defaultBlockState();
                }
                yield profondeur == 0 ? Blocks.GRASS_BLOCK.defaultBlockState() : profondeur == 1 ? Blocks.DIRT.defaultBlockState() : Blocks.TUFF.defaultBlockState();
            }
            case HIPPONE -> {
                if (surface <= ReliefRoyaume.NIVEAU_MER - 6) {
                    // Fond de la mer : sable, avec du gravier par endroits.
                    yield profondeur < 3 ? (hasard % 4 == 0 ? Blocks.GRAVEL : Blocks.SAND).defaultBlockState() : Blocks.SANDSTONE.defaultBlockState();
                }
                if (surface <= ReliefRoyaume.NIVEAU_MER + 2) {
                    // Plage, seulement au bord de l'eau.
                    yield profondeur < 3 ? Blocks.SAND.defaultBlockState() : Blocks.SANDSTONE.defaultBlockState();
                }
                if (profondeur > 7) {
                    yield roche;
                }
                // Rochers de calcaire gris qui percent la garrigue.
                if (colonne.affleurement() && profondeur < 2) {
                    yield (hasard % 3 == 0 ? Blocks.ANDESITE : Blocks.STONE).defaultBlockState();
                }
                // Pentes et marches de 2 blocs : calcaire clair. Marches d'un bloc : mousse, de la teinte olive de la garrigue.
                if (raide || (bordDeMarche && pente >= 2)) {
                    yield Blocks.SANDSTONE.defaultBlockState();
                }
                if (bordDeMarche) {
                    yield Blocks.MOSS_BLOCK.defaultBlockState();
                }
                if (profondeur == 0) {
                    // Garrigue : surtout de l'herbe sèche, quelques plaques de terre nue et de cailloux.
                    int tirage = hasard % 100;
                    if (tirage < 6) {
                        yield Blocks.COARSE_DIRT.defaultBlockState();
                    }
                    if (tirage < 9) {
                        yield Blocks.GRAVEL.defaultBlockState();
                    }
                    yield Blocks.GRASS_BLOCK.defaultBlockState();
                }
                // Terre rouge (terra rossa) puis calcaire.
                yield profondeur < 3 ? Blocks.TERRACOTTA.defaultBlockState() : Blocks.SANDSTONE.defaultBlockState();
            }
        };
    }

    /** Calcite, diorite et un peu de pierre : la roche blanche du bord et du dessous de l'île. */
    private static BlockState rochePale(int hasard) {
        int tirage = hasard % 10;
        return (tirage < 5 ? Blocks.CALCITE : tirage < 8 ? Blocks.DIORITE : Blocks.STONE).defaultBlockState();
    }

    /** Nombre pseudo-aléatoire fixe pour une position : sert à varier les blocs sans dépendre de la seed. */
    private static int hachage(int x, int y, int z) {
        int h = x * 73856093 ^ y * 19349663 ^ z * 83492791;
        h ^= h >>> 13;
        h *= 0x5bd1e995;
        h ^= h >>> 15;
        return h & Integer.MAX_VALUE;
    }

    @Override
    public int getBaseHeight(int x, int z, Heightmap.Types type, LevelHeightAccessor niveau, RandomState aleatoire) {
        ReliefRoyaume.Colonne colonne = ReliefRoyaume.colonne(x, z);
        if (!colonne.ile()) {
            return niveau.getMinBuildHeight();
        }
        if (colonne.sousLEau() && type.isOpaque().test(EAU)) {
            return ReliefRoyaume.NIVEAU_MER + 1;
        }
        return colonne.surface() + 1;
    }

    @Override
    public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor niveau, RandomState aleatoire) {
        ReliefRoyaume.Colonne colonne = ReliefRoyaume.colonne(x, z);
        int pente = colonne.ile() ? ReliefRoyaume.pente(colonne, x, z) : 0;
        BlockState[] etats = new BlockState[niveau.getHeight()];
        for (int i = 0; i < etats.length; i++) {
            int y = niveau.getMinBuildHeight() + i;
            boolean dansIle = colonne.ile() && y >= colonne.fond();
            etats[i] = dansIle ? bloc(colonne, pente, x, y, z) : AIR;
        }
        return new NoiseColumn(niveau.getMinBuildHeight(), etats);
    }

    @Override
    public void addDebugScreenInfo(List<String> lignes, RandomState aleatoire, BlockPos pos) {
        ReliefRoyaume.Colonne colonne = ReliefRoyaume.colonne(pos.getX(), pos.getZ());
        lignes.add("Royaume : " + colonne.zone() + (colonne.ile() ? ", sol à y=" + colonne.surface() : ", hors de l'île"));
    }

    // --- Tout ce que la génération vanilla ajouterait est désactivé. ---

    @Override
    public void applyCarvers(WorldGenRegion niveau, long graine, RandomState aleatoire, BiomeManager biomes,
                             StructureManager structures, ChunkAccess chunk, GenerationStep.Carving etape) {
    }

    @Override
    public void buildSurface(WorldGenRegion niveau, StructureManager structures, RandomState aleatoire, ChunkAccess chunk) {
    }

    @Override
    public void applyBiomeDecoration(WorldGenLevel niveau, ChunkAccess chunk, StructureManager structures) {
    }

    @Override
    public void createStructures(RegistryAccess registres, ChunkGeneratorStructureState etat, StructureManager structures,
                                 ChunkAccess chunk, StructureTemplateManager gabarits) {
    }

    @Override
    public void spawnOriginalMobs(WorldGenRegion niveau) {
    }

    @Override
    public int getGenDepth() {
        return HAUTEUR_MONDE;
    }

    @Override
    public int getSeaLevel() {
        return ReliefRoyaume.NIVEAU_MER;
    }

    @Override
    public int getMinY() {
        return 0;
    }
}
