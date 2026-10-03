package com.royaumedesidees.structures;

import com.royaumedesidees.monde.GenerateurRoyaume;
import com.royaumedesidees.monde.ReliefRoyaume;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

/**
 * Pose les blocs d'une structure, en refusant tout ce qui sort de sa boîte englobante.
 * Les blocs sont posés sans mettre à jour leurs voisins, pour ne rien déclencher hors de la boîte.
 */
public final class Pose {
    private static final int DRAPEAUX = Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE;

    private final ServerLevel niveau;
    private final BoundingBox boite;
    private final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
    /** Blocs dont la forme dépend des voisins (clôtures, murets, escaliers, vitres), raccordés à la fin. */
    private final java.util.List<BlockPos> araccorder = new java.util.ArrayList<>();

    Pose(ServerLevel niveau, BoundingBox boite) {
        this.niveau = niveau;
        this.boite = boite;
    }

    /** Le niveau, pour les blocs qui ont besoin d'une entité de bloc (panneaux). */
    public ServerLevel niveau() {
        return niveau;
    }

    public void poser(int x, int y, int z, BlockState etat) {
        if (boite.isInside(x, y, z)) {
            niveau.setBlock(pos.set(x, y, z), etat, DRAPEAUX);
        }
    }

    public void poser(int x, int y, int z, Block bloc) {
        poser(x, y, z, bloc.defaultBlockState());
    }

    /** Pose un bloc qui se raccordera à ses voisins une fois toute la structure posée (voir {@link #terminer()}). */
    public void poserRaccorde(int x, int y, int z, BlockState etat) {
        if (boite.isInside(x, y, z)) {
            poser(x, y, z, etat);
            araccorder.add(new BlockPos(x, y, z));
        }
    }

    public void poserRaccorde(int x, int y, int z, Block bloc) {
        poserRaccorde(x, y, z, bloc.defaultBlockState());
    }

    /** Ce qu'il y a actuellement en (x, y, z). */
    public BlockState lire(int x, int y, int z) {
        return niveau.getBlockState(pos.set(x, y, z));
    }

    public boolean dedans(int x, int y, int z) {
        return boite.isInside(x, y, z);
    }

    /**
     * Raccorde les blocs posés avec {@link #poserRaccorde} : chaque clôture, muret, escalier ou vitre prend la forme
     * que lui donnent ses voisins, comme s'il avait été posé à la main.
     */
    void terminer() {
        for (BlockPos ici : araccorder) {
            BlockState etat = niveau.getBlockState(ici);
            BlockState raccorde = Block.updateFromNeighbourShapes(etat, niveau, ici);
            if (raccorde != etat && !raccorde.isAir()) {
                niveau.setBlock(ici, raccorde, DRAPEAUX);
            }
        }
        araccorder.clear();
    }

    /** Remplit le pavé entre deux coins (inclus). */
    public void remplir(int x1, int y1, int z1, int x2, int y2, int z2, BlockState etat) {
        for (int x = Math.min(x1, x2); x <= Math.max(x1, x2); x++) {
            for (int y = Math.min(y1, y2); y <= Math.max(y1, y2); y++) {
                for (int z = Math.min(z1, z2); z <= Math.max(z1, z2); z++) {
                    poser(x, y, z, etat);
                }
            }
        }
    }

    public void remplir(int x1, int y1, int z1, int x2, int y2, int z2, Block bloc) {
        remplir(x1, y1, z1, x2, y2, z2, bloc.defaultBlockState());
    }

    /**
     * Remet toute la boîte dans l'état où la génération l'a créée (relief, Caverne, eau), avant de reposer
     * une nouvelle version. La végétation (herbes, arbres) n'est pas recréée.
     */
    void restaurerTerrain() {
        for (int x = boite.minX(); x <= boite.maxX(); x++) {
            for (int z = boite.minZ(); z <= boite.maxZ(); z++) {
                ReliefRoyaume.Colonne colonne = ReliefRoyaume.colonne(x, z);
                int pente = colonne.ile() ? ReliefRoyaume.pente(colonne, x, z) : 0;
                for (int y = boite.minY(); y <= boite.maxY(); y++) {
                    BlockState attendu = colonne.ile() && y >= colonne.fond()
                            ? GenerateurRoyaume.bloc(colonne, pente, x, y, z)
                            : Blocks.AIR.defaultBlockState();
                    if (niveau.getBlockState(pos.set(x, y, z)) != attendu) {
                        niveau.setBlock(pos, attendu, DRAPEAUX);
                    }
                }
            }
        }
    }
}
