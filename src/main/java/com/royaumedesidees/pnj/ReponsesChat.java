package com.royaumedesidees.pnj;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.ServerChatEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * Réponses données dans le chat à une question d'un PNJ (défi d'Adéodat, additions de Pascal). Tant qu'un joueur doit
 * répondre, son prochain message n'est pas envoyé à tout le serveur : il est rendu au PNJ, et le joueur le revoit en
 * gris. Sans réponse dans le délai, le PNJ est prévenu.
 */
public final class ReponsesChat {
    private record Attente(long finTick, Consumer<String> reponse, Runnable delaiDepasse) {
    }

    private static final Map<UUID, Attente> ATTENTES = new HashMap<>();
    private static long tickServeur;

    private ReponsesChat() {
    }

    /** Attend la prochaine ligne de chat du joueur, pendant {@code ticks} ticks. Remplace une attente en cours. */
    public static void attendre(ServerPlayer joueur, int ticks, Consumer<String> reponse, Runnable delaiDepasse) {
        ATTENTES.put(joueur.getUUID(), new Attente(tickServeur + ticks, reponse, delaiDepasse));
    }

    public static boolean enAttente(ServerPlayer joueur) {
        return ATTENTES.containsKey(joueur.getUUID());
    }

    public static void annuler(ServerPlayer joueur) {
        ATTENTES.remove(joueur.getUUID());
    }

    public static void chat(ServerChatEvent evenement) {
        ServerPlayer joueur = evenement.getPlayer();
        Attente attente = ATTENTES.remove(joueur.getUUID());
        if (attente == null) {
            return;
        }
        evenement.setCanceled(true);
        String texte = evenement.getRawText().trim();
        joueur.sendSystemMessage(Component.literal("> " + texte).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        joueur.server.execute(() -> attente.reponse().accept(texte));
    }

    public static void tick(ServerTickEvent.Post evenement) {
        tickServeur++;
        Iterator<Attente> iterateur = ATTENTES.values().iterator();
        while (iterateur.hasNext()) {
            Attente attente = iterateur.next();
            if (tickServeur >= attente.finTick()) {
                iterateur.remove();
                attente.delaiDepasse().run();
            }
        }
    }
}
