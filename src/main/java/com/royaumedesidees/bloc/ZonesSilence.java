package com.royaumedesidees.bloc;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.ServerChatEvent;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Les zones de silence des Pupitres d'Ambroise : une sphère de 8 blocs autour de chaque pupitre chargé.
 * <ul>
 *   <li>On ne peut pas y écrire dans le chat (« Chut. Ambroise lit. »). Les commandes, elles, marchent, et une réponse
 *       attendue par un PNJ passe quand même (elle est traitée avant, voir {@link com.royaumedesidees.pnj.ReponsesChat}).</li>
 *   <li>Les monstres n'y repèrent pas les joueurs, et oublient ceux qu'ils poursuivaient s'ils y entrent.</li>
 * </ul>
 * Plus tard, les PNJ non plus n'y entendront rien : on pourra y voler sans qu'Augustin accoure, mais la Culpabilité
 * tombera quand même.
 */
public final class ZonesSilence {
    public static final double RAYON = 8.0;
    private static final Map<ResourceKey<Level>, Set<BlockPos>> PUPITRES = new HashMap<>();

    private ZonesSilence() {
    }

    static void ajouter(Level niveau, BlockPos pos) {
        PUPITRES.computeIfAbsent(niveau.dimension(), cle -> new HashSet<>()).add(pos.immutable());
    }

    static void retirer(Level niveau, BlockPos pos) {
        Set<BlockPos> pupitres = PUPITRES.get(niveau.dimension());
        if (pupitres != null) {
            pupitres.remove(pos);
        }
    }

    /** Vrai si l'entité est dans la zone de silence d'un Pupitre d'Ambroise. */
    public static boolean dansZone(Entity entite) {
        Set<BlockPos> pupitres = PUPITRES.get(entite.level().dimension());
        if (pupitres == null) {
            return false;
        }
        for (BlockPos pos : pupitres) {
            if (entite.distanceToSqr(Vec3.atCenterOf(pos)) <= RAYON * RAYON) {
                return true;
            }
        }
        return false;
    }

    public static void chat(ServerChatEvent evenement) {
        ServerPlayer joueur = evenement.getPlayer();
        if (dansZone(joueur)) {
            evenement.setCanceled(true);
            joueur.displayClientMessage(Component.translatable("message.royaumedesidees.silence.chut")
                    .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC), true);
        }
    }

    public static void cible(LivingChangeTargetEvent evenement) {
        if (evenement.getEntity() instanceof Enemy && evenement.getNewAboutToBeSetTarget() instanceof Player joueur
                && dansZone(joueur)) {
            evenement.setCanceled(true);
        }
    }

    /** Une fois par seconde, les monstres proches d'un pupitre lâchent les joueurs réfugiés dans le silence. */
    public static void tick(LevelTickEvent.Post evenement) {
        if (!(evenement.getLevel() instanceof ServerLevel niveau) || niveau.getGameTime() % 20 != 0) {
            return;
        }
        Set<BlockPos> pupitres = PUPITRES.get(niveau.dimension());
        if (pupitres == null || pupitres.isEmpty()) {
            return;
        }
        for (BlockPos pos : Set.copyOf(pupitres)) {
            for (Mob mob : niveau.getEntitiesOfClass(Mob.class, new AABB(pos).inflate(32), m -> m instanceof Enemy)) {
                if (mob.getTarget() instanceof Player joueur && dansZone(joueur)) {
                    mob.setTarget(null);
                }
            }
        }
    }
}
