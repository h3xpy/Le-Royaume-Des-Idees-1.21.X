package com.royaumedesidees.grace;

import com.royaumedesidees.RoyaumeDesIdees;
import com.royaumedesidees.registre.ModPiecesJointes;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Les deux Voies et leurs effets, dans le Royaume seulement :
 * <ul>
 *   <li>Raison : +15 % de vitesse de minage (calcul, outils), mais moitié moins de Grâce ({@link Grace#ajouter}) ;</li>
 *   <li>Cœur : −15 % de vitesse de minage (lent à fabriquer), mais le Livre des Confessions ;</li>
 *   <li>la Voie s'affiche après le pseudo dans la liste des joueurs (TAB), tant que le joueur est dans le Royaume.</li>
 * </ul>
 * Choisir une Voie l'annonce à tout le serveur. En changer coûte 50 de Grâce ({@link Grace#PRIX_CHANGER_VOIE}).
 */
public final class Voies {
    private static final ResourceLocation MODIFICATEUR = RoyaumeDesIdees.id("voie");
    private static final double EFFET_MINAGE = 0.15;
    /** Dernier état connu (Voie et présence dans le Royaume), pour ne rafraîchir la liste TAB qu'au changement. */
    private static final Map<UUID, String> ETATS = new HashMap<>();

    private Voies() {
    }

    /** Donne une Voie au joueur et l'annonce au serveur. */
    public static void choisir(ServerPlayer joueur, Voie voie) {
        joueur.setData(ModPiecesJointes.VOIE, voie);
        joueur.server.getPlayerList().broadcastSystemMessage(Component.translatable("message.royaumedesidees.voie.annonce",
                joueur.getDisplayName(), Component.translatable("voie.royaumedesidees." + voie.getSerializedName())), false);
        appliquer(joueur);
    }

    public static void tick(PlayerTickEvent.Post evenement) {
        if (evenement.getEntity() instanceof ServerPlayer joueur && joueur.tickCount % 20 == 0) {
            appliquer(joueur);
        }
    }

    private static void appliquer(ServerPlayer joueur) {
        Voie voie = Grace.voie(joueur);
        boolean royaume = Grace.dansRoyaume(joueur);
        AttributeInstance minage = joueur.getAttribute(Attributes.BLOCK_BREAK_SPEED);
        if (minage != null) {
            double effet = !royaume ? 0 : voie == Voie.RAISON ? EFFET_MINAGE : voie == Voie.COEUR ? -EFFET_MINAGE : 0;
            AttributeModifier actuel = minage.getModifier(MODIFICATEUR);
            if (effet == 0 && actuel != null) {
                minage.removeModifier(MODIFICATEUR);
            } else if (effet != 0 && (actuel == null || actuel.amount() != effet)) {
                minage.addOrUpdateTransientModifier(new AttributeModifier(MODIFICATEUR, effet, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
            }
        }
        String etat = voie.getSerializedName() + "|" + royaume;
        if (!etat.equals(ETATS.put(joueur.getUUID(), etat))) {
            joueur.refreshTabListName();
        }
    }

    /** Le pseudo dans la liste TAB, suivi de la Voie (seulement dans le Royaume). */
    public static void nomTab(PlayerEvent.TabListNameFormat evenement) {
        if (!(evenement.getEntity() instanceof ServerPlayer joueur) || !Grace.dansRoyaume(joueur) || Grace.voie(joueur) == Voie.AUCUNE) {
            return;
        }
        Voie voie = Grace.voie(joueur);
        evenement.setDisplayName(Component.empty().append(joueur.getName())
                .append(Component.literal(" [").withStyle(ChatFormatting.GRAY))
                .append(Component.translatable("voie.royaumedesidees." + voie.getSerializedName() + ".court")
                        .withStyle(voie == Voie.RAISON ? ChatFormatting.AQUA : ChatFormatting.RED))
                .append(Component.literal("]").withStyle(ChatFormatting.GRAY)));
    }
}
