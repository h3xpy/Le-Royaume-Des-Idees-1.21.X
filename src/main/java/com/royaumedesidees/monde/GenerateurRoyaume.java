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

        for (int lx = 0; lx < 16; lx++) {
            for (int lz = 0; lz < 16; lz++) {
                int x = minX + lx;
                int z = minZ + lz;
                ReliefRoyaume.Colonne colonne = ReliefRoyaume.colonne(x, z);
                if (!colonne.ile()) {
                    continue;
                }
                int haut = Math.max(colonne.surface(), colonne.sousLEau() ? ReliefRoyaume.NIVEAU_MER : colonne.surface());
                for (int y = colonne.fond(); y <= haut; y++) {
                    BlockState etat = bloc(colonne, x, y, z);
                    chunk.setBlockState(pos.set(lx, y, lz), etat, false);
                    fondOcean.update(lx, y, lz, etat);
                    surfaceMonde.update(lx, y, lz, etat);
                }
            }
        }
        VegetationRoyaume.decorer(chunk, fondOcean, surfaceMonde);
        return CompletableFuture.completedFuture(chunk);
    }

    /** Le bloc à la hauteur y d'une colonne de l'île (entre son fond et la surface de l'eau). */
    public static BlockState bloc(ReliefRoyaume.Colonne colonne, int x, int y, int z) {
        int surface = colonne.surface();
        if (y > surface) {
            return y <= ReliefRoyaume.NIVEAU_MER ? EAU : AIR;
        }
        int profondeur = surface - y;
        BlockState roche = y < Y_ARDOISE ? Blocks.DEEPSLATE.defaultBlockState() : Blocks.STONE.defaultBlockState();
        if (profondeur > 4) {
            return roche;
        }
        int hasard = hachage(x, y, z);

        // Falaises du bord : roche nue.
        if (colonne.rebord() > 4) {
            return profondeur == 0 && hasard % 5 == 0 ? Blocks.ANDESITE.defaultBlockState() : roche;
        }

        return switch (colonne.zoneSol()) {
            case JARDIN_MILAN -> solHerbeux(profondeur, roche);
            case PORT_ROYAL -> {
                // Fond des mares et leurs berges : boue, puis argile.
                if (surface <= ReliefRoyaume.NIVEAU_MER) {
                    yield profondeur == 0 ? Blocks.MUD.defaultBlockState() : profondeur < 3 ? Blocks.CLAY.defaultBlockState() : roche;
                }
                if (surface == ReliefRoyaume.NIVEAU_MER + 1 && profondeur == 0) {
                    yield Blocks.MUD.defaultBlockState();
                }
                yield solHerbeux(profondeur, roche);
            }
            case PUY_DE_DOME -> {
                if (colonne.affleurement() && profondeur < 2) {
                    // Roche volcanique qui perce l'herbe sur le haut du dôme.
                    yield (hasard % 3 == 0 ? Blocks.ANDESITE : Blocks.TUFF).defaultBlockState();
                }
                if (profondeur == 0 && surface >= ReliefRoyaume.NEIGE_Y) {
                    // Herbe sous une fine couche de neige (la neige est posée par VegetationRoyaume).
                    yield Blocks.GRASS_BLOCK.defaultBlockState().setValue(SnowyDirtBlock.SNOWY, true);
                }
                yield solHerbeux(profondeur, roche);
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
                if (colonne.affleurement() && profondeur < 2) {
                    yield (hasard % 3 == 0 ? Blocks.ANDESITE : Blocks.STONE).defaultBlockState();
                }
                if (profondeur == 0) {
                    // Garrigue : herbe sèche, terre nue et podzol.
                    int tirage = hasard % 100;
                    if (tirage < 14) {
                        yield Blocks.COARSE_DIRT.defaultBlockState();
                    }
                    if (tirage < 20) {
                        yield Blocks.PODZOL.defaultBlockState();
                    }
                }
                yield solHerbeux(profondeur, roche);
            }
        };
    }

    private static BlockState solHerbeux(int profondeur, BlockState roche) {
        if (profondeur == 0) {
            return Blocks.GRASS_BLOCK.defaultBlockState();
        }
        return profondeur < 4 ? Blocks.DIRT.defaultBlockState() : roche;
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
        BlockState[] etats = new BlockState[niveau.getHeight()];
        for (int i = 0; i < etats.length; i++) {
            int y = niveau.getMinBuildHeight() + i;
            boolean dansIle = colonne.ile() && y >= colonne.fond();
            etats[i] = dansIle ? bloc(colonne, x, y, z) : AIR;
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
