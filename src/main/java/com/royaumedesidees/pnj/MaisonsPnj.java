package com.royaumedesidees.pnj;

import com.royaumedesidees.RoyaumeDesIdees;
import com.royaumedesidees.registre.ModEntites;
import com.royaumedesidees.registre.ModMonde;
import com.royaumedesidees.structures.DonneesStructures;
import com.royaumedesidees.structures.StructuresJardin;
import com.royaumedesidees.structures.StructuresPnj;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import java.util.List;
import java.util.function.Supplier;

/**
 * Où vit chaque PNJ, et le retour chez lui s'il manque. Toutes les 5 secondes, pour chaque maison dont la structure est
 * posée et dont les entités sont chargées (un joueur est dans le coin) : s'il n'y a aucun PNJ de ce type à moins de
 * 48 blocs, un nouveau apparaît chez lui. C'est ainsi que les PNJ sont placés la première fois, et qu'ils reviennent
 * après un /kill.
 */
public final class MaisonsPnj {
    private record Maison(Supplier<? extends EntityType<? extends PnjRoyaume>> type, String structure, BlockPos pos, int rayon) {
    }

    private static final List<Maison> MAISONS = List.of(
            new Maison(ModEntites.AUGUSTIN_JEUNE, "villa_augustin", StructuresJardin.POS_ATRIUM, 7),
            new Maison(ModEntites.ADEODAT, "villa_augustin", StructuresJardin.POS_TABLINUM, 3),
            new Maison(ModEntites.AMBROISE, "bibliotheque_ambroise", StructuresPnj.POS_AMBROISE, 2),
            new Maison(ModEntites.PASCAL, "port_royal", StructuresPnj.POS_PASCAL, 2));

    private MaisonsPnj() {
    }

    public static void tick(LevelTickEvent.Post evenement) {
        if (!(evenement.getLevel() instanceof ServerLevel niveau) || !niveau.dimension().equals(ModMonde.ROYAUME)
                || niveau.getGameTime() % 100 != 0) {
            return;
        }
        DonneesStructures structures = DonneesStructures.de(niveau);
        for (Maison maison : MAISONS) {
            if (structures.version(maison.structure()) <= 0 || !niveau.isPositionEntityTicking(maison.pos())) {
                continue;
            }
            EntityType<? extends PnjRoyaume> type = maison.type().get();
            if (!niveau.getEntities(type, new AABB(maison.pos()).inflate(48), pnj -> true).isEmpty()) {
                continue;
            }
            PnjRoyaume pnj = type.create(niveau);
            if (pnj == null) {
                continue;
            }
            BlockPos pos = maison.pos();
            pnj.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 180.0F, 0.0F);
            pnj.setMaison(pos, maison.rayon());
            niveau.addFreshEntity(pnj);
            RoyaumeDesIdees.LOGGER.info("PNJ {} placé chez lui en {}", pnj.id(), pos.toShortString());
        }
    }
}
