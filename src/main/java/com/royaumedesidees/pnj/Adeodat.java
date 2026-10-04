package com.royaumedesidees.pnj;

import com.royaumedesidees.grace.Grace;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Adéodat, le fils d'Augustin : à quinze ans, son intelligence effrayait son père (Confessions, IX, 6). Il vit au
 * tablinum de la villa, parmi les livres. Cliqué, il lance un défi de calcul mental (une addition ou une
 * multiplication), à résoudre dans le chat en 15 secondes : réussi, +2 de Grâce ; raté, tout le serveur l'apprend.
 * Un défi par minute et par joueur. Frappé, il soupire.
 */
public class Adeodat extends PnjRoyaume {
    private static final int DELAI_REPONSE = 15 * 20;
    private static final int DELAI_DEFIS = 60 * 20;
    private final Map<UUID, Long> derniersDefis = new HashMap<>();

    public Adeodat(EntityType<? extends Adeodat> type, Level niveau) {
        super(type, niveau, ChatFormatting.AQUA);
    }

    @Override
    protected void parleAvec(ServerPlayer joueur) {
        long maintenant = level().getGameTime();
        Long dernier = derniersDefis.get(joueur.getUUID());
        if (ReponsesChat.enAttente(joueur)) {
            parler(joueur, "attends");
            return;
        }
        if (dernier != null && maintenant - dernier < DELAI_DEFIS) {
            parler(joueur, "repos", (DELAI_DEFIS - (maintenant - dernier) + 19) / 20);
            return;
        }
        derniersDefis.put(joueur.getUUID(), maintenant);
        int a;
        int b;
        int resultat;
        String operation;
        if (getRandom().nextBoolean()) {
            a = 12 + getRandom().nextInt(87);
            b = 12 + getRandom().nextInt(87);
            resultat = a + b;
            operation = a + " + " + b;
        } else {
            a = 12 + getRandom().nextInt(38);
            b = 3 + getRandom().nextInt(7);
            resultat = a * b;
            operation = a + " × " + b;
        }
        parler(joueur, "defi", operation, DELAI_REPONSE / 20);
        ReponsesChat.attendre(joueur, DELAI_REPONSE, reponse -> corriger(joueur, reponse, resultat),
                () -> rate(joueur, resultat));
    }

    private void corriger(ServerPlayer joueur, String reponse, int resultat) {
        String chiffres = reponse.replaceAll("[^0-9-]", "");
        if (!chiffres.isEmpty() && chiffres.length() < 10 && Integer.parseInt(chiffres) == resultat) {
            parler(joueur, "reussi");
            level().playSound(null, this, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.NEUTRAL, 0.8F, 1.2F);
            Grace.ajouter(joueur, 2, "adeodat");
        } else {
            rate(joueur, resultat);
        }
    }

    private void rate(ServerPlayer joueur, int resultat) {
        parler(joueur, "rate", resultat);
        joueur.server.getPlayerList().broadcastSystemMessage(
                Component.translatable("message.royaumedesidees.adeodat.humiliation", joueur.getDisplayName()), false);
    }

    @Override
    protected void reagirCoup(ServerPlayer joueur) {
        parler(joueur, "coup");
    }
}
