package com.royaumedesidees.structures;

import com.royaumedesidees.RoyaumeDesIdees;
import com.royaumedesidees.registre.ModMonde;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

/**
 * Système de pose des structures (« StructurePlacer » de la spec). Une fois par seconde, dans le Royaume :
 * si un joueur est à moins de 96 blocs d'une structure absente ou dont la version a augmenté, elle est posée
 * (ou remplacée) et sa version est enregistrée. Ainsi, une structure ajoutée dans une version future du mod
 * apparaît aussi dans un monde créé avant.
 */
public final class PoseurStructures {
    public static final int DISTANCE = 96;

    private PoseurStructures() {
    }

    public static void tick(LevelTickEvent.Post evenement) {
        if (!(evenement.getLevel() instanceof ServerLevel niveau) || !niveau.dimension().equals(ModMonde.ROYAUME)
                || niveau.getGameTime() % 20 != 0 || niveau.players().isEmpty()) {
            return;
        }
        DonneesStructures donnees = DonneesStructures.de(niveau);
        for (StructureRoyaume structure : StructuresRoyaume.TOUTES) {
            int posee = donnees.version(structure.id());
            if (posee >= structure.version()) {
                continue;
            }
            for (ServerPlayer joueur : niveau.players()) {
                if (proche(structure.boite(), joueur.blockPosition())) {
                    poser(niveau, structure, posee > 0);
                    break;
                }
            }
        }
    }

    /**
     * Pose la structure maintenant. Si {@code remplacer}, la boîte est d'abord remise dans son état d'origine,
     * pour qu'il ne reste rien de l'ancienne version.
     */
    public static void poser(ServerLevel niveau, StructureRoyaume structure, boolean remplacer) {
        BoundingBox boite = structure.boite();
        // Charge (et génère si besoin) tous les chunks de la boîte avant d'y toucher.
        for (int cx = boite.minX() >> 4; cx <= boite.maxX() >> 4; cx++) {
            for (int cz = boite.minZ() >> 4; cz <= boite.maxZ() >> 4; cz++) {
                niveau.getChunk(cx, cz);
            }
        }
        Pose pose = new Pose(niveau, boite);
        if (remplacer) {
            pose.restaurerTerrain();
        }
        structure.constructeur().accept(pose);
        pose.terminer();
        DonneesStructures.de(niveau).noter(structure.id(), structure.version());
        RoyaumeDesIdees.LOGGER.info("Structure {} posée (version {})", structure.id(), structure.version());
    }

    private static boolean proche(BoundingBox boite, BlockPos pos) {
        long dx = Math.max(Math.max(boite.minX() - pos.getX(), pos.getX() - boite.maxX()), 0);
        long dy = Math.max(Math.max(boite.minY() - pos.getY(), pos.getY() - boite.maxY()), 0);
        long dz = Math.max(Math.max(boite.minZ() - pos.getZ(), pos.getZ() - boite.maxZ()), 0);
        return dx * dx + dy * dy + dz * dz <= (long) DISTANCE * DISTANCE;
    }
}
