package com.royaumedesidees.pnj;

import com.royaumedesidees.bloc.ZonesSilence;
import com.royaumedesidees.grace.Grace;
import com.royaumedesidees.registre.ModPiecesJointes;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Tout doit pouvoir se découvrir en jeu. Les astuces s'affichent une seule fois par joueur (donnée {@code astuces}),
 * au moment où la mécanique apparaît : première entrée dans le Royaume, première Culpabilité, première Grâce. Et
 * chaque fois qu'on entre dans la zone de silence d'Ambroise, un rappel s'affiche au-dessus de la barre d'objets.
 */
public final class Astuces {
    public static final int ROYAUME = 1;
    public static final int CULPABILITE = 2;
    public static final int GRACE = 4;
    private static final String CLE = "message.royaumedesidees.astuce.";
    private static final Set<UUID> DANS_LE_SILENCE = new HashSet<>();

    private Astuces() {
    }

    /** Affiche l'astuce si le joueur ne l'a encore jamais vue. */
    public static void montrer(ServerPlayer joueur, int astuce, String cle) {
        int vues = joueur.getData(ModPiecesJointes.ASTUCES);
        if ((vues & astuce) != 0) {
            return;
        }
        joueur.setData(ModPiecesJointes.ASTUCES, vues | astuce);
        joueur.sendSystemMessage(Component.translatable(CLE + cle).withStyle(ChatFormatting.AQUA));
    }

    public static void tick(PlayerTickEvent.Post evenement) {
        if (!(evenement.getEntity() instanceof ServerPlayer joueur) || joueur.tickCount % 10 != 0 || joueur.isSpectator()) {
            return;
        }
        if (!Grace.dansRoyaume(joueur)) {
            DANS_LE_SILENCE.remove(joueur.getUUID());
            return;
        }
        if (joueur.tickCount % 40 == 0) {
            montrer(joueur, ROYAUME, "royaume");
        }
        boolean dedans = ZonesSilence.dansZone(joueur);
        if (dedans && DANS_LE_SILENCE.add(joueur.getUUID())) {
            joueur.displayClientMessage(Component.translatable(CLE + "silence").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC), true);
        } else if (!dedans) {
            DANS_LE_SILENCE.remove(joueur.getUUID());
        }
    }
}
