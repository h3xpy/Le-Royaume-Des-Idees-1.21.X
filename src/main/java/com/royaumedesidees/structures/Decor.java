package com.royaumedesidees.structures;

import com.royaumedesidees.monde.ReliefRoyaume;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.network.Filterable;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.WrittenBookContent;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignText;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Half;

import java.util.ArrayList;
import java.util.List;

/** Petits éléments de décor communs aux structures : escaliers, plantes, bougies, panneaux, livres, arbres. */
final class Decor {
    private Decor() {
    }

    /** Hauteur du sol généré en (x, z). */
    static int sol(int x, int z) {
        return ReliefRoyaume.colonne(x, z).surface();
    }

    static int solMax(int x1, int z1, int x2, int z2) {
        int max = Integer.MIN_VALUE;
        for (int x = x1; x <= x2; x++) {
            for (int z = z1; z <= z2; z++) {
                max = Math.max(max, sol(x, z));
            }
        }
        return max;
    }

    static int solMin(int x1, int z1, int x2, int z2) {
        int min = Integer.MAX_VALUE;
        for (int x = x1; x <= x2; x++) {
            for (int z = z1; z <= z2; z++) {
                min = Math.min(min, sol(x, z));
            }
        }
        return min;
    }

    /** Hasard déterministe dans [0, 1), qui ne dépend que de la position et d'une graine. */
    static double hasard(int x, int z, int graine) {
        long h = x * 341873128712L + z * 132897987541L + graine * 2654435761L;
        h ^= h >>> 29;
        h *= 0xBF58476D1CE4E5B9L;
        h ^= h >>> 32;
        return (h >>> 11) * 0x1.0p-53;
    }

    static double hasard(int x, int y, int z, int graine) {
        return hasard(x * 31 + y, z * 17 - y * 7, graine);
    }

    static BlockState escalier(Block bloc, Direction dos) {
        return bloc.defaultBlockState().setValue(StairBlock.FACING, dos).setValue(StairBlock.HALF, Half.BOTTOM);
    }

    static BlockState escalierRenverse(Block bloc, Direction dos) {
        return bloc.defaultBlockState().setValue(StairBlock.FACING, dos).setValue(StairBlock.HALF, Half.TOP);
    }

    static BlockState feuilles(Block bloc) {
        return bloc.defaultBlockState().setValue(LeavesBlock.PERSISTENT, true);
    }

    static BlockState bougies(Block bloc, int nombre) {
        return bloc.defaultBlockState().setValue(CandleBlock.CANDLES, nombre).setValue(CandleBlock.LIT, true);
    }

    static BlockState lanterne(boolean suspendue) {
        return Blocks.LANTERN.defaultBlockState().setValue(LanternBlock.HANGING, suspendue);
    }

    /** Fleur haute (rosier, pivoine, lilas…), ses deux moitiés. */
    static void plante2(Pose pose, int x, int y, int z, Block plante) {
        pose.poser(x, y, z, plante.defaultBlockState().setValue(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER));
        pose.poser(x, y + 1, z, plante.defaultBlockState().setValue(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER));
    }

    /** Petit lampadaire : un poteau de muret surmonté de bougies allumées. */
    static void candelabre(Pose pose, int x, int y, int z) {
        pose.poser(x, y, z, Blocks.POLISHED_DEEPSLATE_WALL);
        pose.poser(x, y + 1, z, bougies(Blocks.WHITE_CANDLE, 3));
    }

    /** Lignes de panneau tirées des clés de traduction (4 au plus). */
    private static SignText texte(String... cles) {
        SignText texte = new SignText();
        for (int i = 0; i < Math.min(4, cles.length); i++) {
            texte = texte.setMessage(i, Component.translatable(cles[i]));
        }
        return texte;
    }

    /** Panneau sur pied ; {@code rotation} de 0 (texte tourné vers le sud) à 15, dans le sens horaire. */
    static void panneau(Pose pose, Block bloc, int x, int y, int z, int rotation, String... cles) {
        if (!pose.dedans(x, y, z)) {
            return;
        }
        pose.poser(x, y, z, bloc.defaultBlockState().setValue(StandingSignBlock.ROTATION, rotation));
        if (pose.niveau().getBlockEntity(new BlockPos(x, y, z)) instanceof SignBlockEntity panneau) {
            panneau.setText(texte(cles), true);
        }
    }

    /** Panneau accroché au bloc situé derrière lui ; {@code face} est la direction vers laquelle on le lit. */
    static void panneauMural(Pose pose, Block bloc, int x, int y, int z, Direction face, String... cles) {
        if (!pose.dedans(x, y, z)) {
            return;
        }
        pose.poser(x, y, z, bloc.defaultBlockState().setValue(WallSignBlock.FACING, face));
        if (pose.niveau().getBlockEntity(new BlockPos(x, y, z)) instanceof SignBlockEntity panneau) {
            panneau.setText(texte(cles), true);
        }
    }

    /** Livre écrit dont les pages sont des clés de traduction : chacun le lit dans sa langue. */
    static ItemStack livre(String titre, String auteur, String clePages, int pages) {
        List<Filterable<Component>> contenu = new ArrayList<>();
        for (int i = 1; i <= pages; i++) {
            contenu.add(Filterable.passThrough(Component.translatable(clePages + i)));
        }
        ItemStack livre = new ItemStack(Items.WRITTEN_BOOK);
        livre.set(DataComponents.WRITTEN_BOOK_CONTENT, new WrittenBookContent(Filterable.passThrough(titre), auteur, 0, contenu, true));
        return livre;
    }

    /** Lutrin tourné vers {@code face}, avec un livre ouvert dessus. */
    static void lutrin(Pose pose, int x, int y, int z, Direction face, ItemStack livre) {
        if (!pose.dedans(x, y, z)) {
            return;
        }
        pose.poser(x, y, z, Blocks.LECTERN.defaultBlockState().setValue(LecternBlock.FACING, face).setValue(LecternBlock.HAS_BOOK, true));
        if (pose.niveau().getBlockEntity(new BlockPos(x, y, z)) instanceof LecternBlockEntity lutrin) {
            lutrin.setBook(livre);
        }
    }

    /** Cyprès de Toscane : tronc court et colonne étroite, fuselée, de feuillage sombre. */
    static void cypres(Pose pose, int x, int y, int z, int hauteur) {
        BlockState feuillage = feuilles(Blocks.SPRUCE_LEAVES);
        pose.remplir(x, y, z, x, y + 1, z, Blocks.SPRUCE_LOG.defaultBlockState());
        for (int dy = 1; dy <= hauteur; dy++) {
            double t = dy / (double) hauteur;
            // Large au tiers inférieur, pointu au sommet.
            double rayon = t < 0.3 ? 1.0 : (t < 0.8 ? 0.95 : 0.4);
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    boolean coin = dx != 0 && dz != 0;
                    if (dx == 0 && dz == 0) {
                        pose.poser(x, y + dy, z, dy < hauteur - 1 ? Blocks.SPRUCE_LOG.defaultBlockState() : feuillage);
                    } else if (!coin && rayon >= 0.9 && hasard(x + dx, y + dy, z + dz, 77) > 0.12) {
                        pose.poser(x + dx, y + dy, z + dz, feuillage);
                    }
                }
            }
        }
        pose.poser(x, y + hauteur + 1, z, feuillage);
    }

    /** Fait apparaître des animaux qui ne disparaissent pas (cochons de la porcherie, par exemple). */
    static void animaux(Pose pose, EntityType<? extends Mob> type, int nombre, int x, int y, int z) {
        if (!pose.dedans(x, y, z)) {
            return;
        }
        for (int i = 0; i < nombre; i++) {
            Mob animal = type.create(pose.niveau());
            if (animal == null) {
                return;
            }
            animal.moveTo(x + 0.5 + (i % 2) * 1.5, y, z + 0.5 + (i / 2) * 1.5, i * 90.0F, 0.0F);
            net.neoforged.neoforge.event.EventHooks.finalizeMobSpawn(animal, pose.niveau(),
                    pose.niveau().getCurrentDifficultyAt(animal.blockPosition()), MobSpawnType.STRUCTURE, null);
            animal.setPersistenceRequired();
            pose.niveau().addFreshEntity(animal);
        }
    }

    /**
     * Volée de marches qui descend depuis une porte jusqu'au sol, dans la direction {@code vers}. La première marche
     * est juste devant le seuil, dont le dessus est à {@code yPalier}. Les marches sont posées sur un massif de pierre.
     */
    static void descente(Pose pose, int x0, int z0, Direction vers, int yPalier, int demiLargeur, Block marche, Block massif) {
        Direction cote = vers.getClockWise();
        for (int k = 1; k <= 24; k++) {
            int y = yPalier - k;
            boolean fini = true;
            for (int l = -demiLargeur; l <= demiLargeur; l++) {
                int x = x0 + vers.getStepX() * k + cote.getStepX() * l;
                int z = z0 + vers.getStepZ() * k + cote.getStepZ() * l;
                int s = sol(x, z);
                if (s >= y) {
                    continue;
                }
                fini = false;
                pose.remplir(x, s, z, x, y - 1, z, massif.defaultBlockState());
                pose.poserRaccorde(x, y, z, escalier(marche, vers.getOpposite()));
            }
            if (fini) {
                return;
            }
        }
    }
}
