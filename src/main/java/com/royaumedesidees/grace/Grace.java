package com.royaumedesidees.grace;

import com.royaumedesidees.registre.ModMonde;
import com.royaumedesidees.registre.ModPiecesJointes;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * La Grâce : une jauge de 0 à 100 par joueur, sauvegardée avec lui et conservée à la mort. Comme tous les systèmes de
 * la v0.3, elle n'existe que dans le Royaume : hors de la dimension, on n'en gagne pas et la jauge est cachée.
 * <p>
 * La blague d'Augustin : la grâce ne se mérite pas. Chaque gain a 10 % de chance d'afficher « La grâce est un don, pas
 * un salaire », puis de doubler ou de ne rien donner, à pile ou face.
 * <p>
 * Les autres systèmes passent par {@link #ajouter} et {@link #depenser} : confession, Monique, Adéodat, quêtes, et plus
 * tard le Totem de la Grâce et l'Autel (v1.0).
 */
public final class Grace {
    public static final int MAXIMUM = 100;
    /** Coût d'un changement de Voie. */
    public static final int PRIX_CHANGER_VOIE = 50;
    /** Délai sans riposte (10 s) après un coup reçu d'un joueur, pour gagner de la Grâce « à la Pascal ». */
    private static final int DELAI_SANS_RIPOSTE = 200;
    private static final int FRAPPES_PAR_JOUR = 5;
    private static final String CLE = "message.royaumedesidees.grace.";

    /** Joueurs frappés par un autre joueur, et l'heure (en ticks) du dernier coup reçu. */
    private static final Map<UUID, Long> FRAPPES_EN_ATTENTE = new HashMap<>();

    private Grace() {
    }

    public static boolean dansRoyaume(Player joueur) {
        return joueur.level().dimension().equals(ModMonde.ROYAUME);
    }

    public static int valeur(Player joueur) {
        return joueur.getData(ModPiecesJointes.GRACE);
    }

    public static Voie voie(Player joueur) {
        return joueur.getData(ModPiecesJointes.VOIE);
    }

    /**
     * Fait gagner de la Grâce, dans le Royaume seulement. La Voie de la Raison n'en gagne que la moitié (arrondie au
     * dessus). Renvoie ce qui a vraiment été gagné (0 hors du Royaume, ou si la blague a tout repris).
     *
     * @param raison clé de traduction courte de la cause, sous {@code message.royaumedesidees.grace.raison.}
     */
    public static int ajouter(ServerPlayer joueur, int base, String raison) {
        if (base <= 0 || !dansRoyaume(joueur)) {
            return 0;
        }
        int gain = voie(joueur) == Voie.RAISON ? (base + 1) / 2 : base;
        Component cause = Component.translatable(CLE + "raison." + raison);
        if (joueur.getRandom().nextInt(10) == 0) {
            joueur.sendSystemMessage(Component.translatable(CLE + "don").withStyle(ChatFormatting.GOLD, ChatFormatting.ITALIC));
            if (joueur.getRandom().nextBoolean()) {
                gain *= 2;
            } else {
                joueur.displayClientMessage(Component.translatable(CLE + "rien", cause).withStyle(ChatFormatting.GRAY), true);
                return 0;
            }
        }
        int avant = valeur(joueur);
        definir(joueur, avant + gain);
        int gagne = valeur(joueur) - avant;
        joueur.displayClientMessage(Component.translatable(CLE + "gain", gagne, cause).withStyle(ChatFormatting.GOLD), true);
        if (gagne > 0) {
            com.royaumedesidees.pnj.Astuces.montrer(joueur, com.royaumedesidees.pnj.Astuces.GRACE, "grace");
        }
        return gagne;
    }

    /** Dépense de la Grâce si le joueur en a assez ; renvoie faux sinon, sans rien retirer. */
    public static boolean depenser(ServerPlayer joueur, int cout) {
        if (valeur(joueur) < cout) {
            return false;
        }
        definir(joueur, valeur(joueur) - cout);
        return true;
    }

    /** Fixe la Grâce (entre 0 et 100) et prévient le client. */
    public static void definir(ServerPlayer joueur, int valeur) {
        joueur.setData(ModPiecesJointes.GRACE, Math.max(0, Math.min(MAXIMUM, valeur)));
        synchroniser(joueur);
    }

    public static void synchroniser(ServerPlayer joueur) {
        PacketDistributor.sendToPlayer(joueur, new GracePaquet(valeur(joueur)));
    }

    /** Numéro du jour de jeu (celui de l'Overworld, qui rythme tout le serveur). */
    public static long jour(ServerPlayer joueur) {
        return joueur.server.overworld().getDayTime() / 24000L;
    }

    public static CompteursJour compteurs(ServerPlayer joueur) {
        return joueur.getData(ModPiecesJointes.COMPTEURS_JOUR).pour(jour(joueur));
    }

    public static void noterCompteurs(ServerPlayer joueur, CompteursJour compteurs) {
        joueur.setData(ModPiecesJointes.COMPTEURS_JOUR, compteurs);
    }

    // ------------------------------------------------------------------ Événements

    public static void connexion(PlayerEvent.PlayerLoggedInEvent evenement) {
        if (evenement.getEntity() instanceof ServerPlayer joueur) {
            synchroniser(joueur);
        }
    }

    public static void changementDimension(PlayerEvent.PlayerChangedDimensionEvent evenement) {
        if (evenement.getEntity() instanceof ServerPlayer joueur) {
            synchroniser(joueur);
            FRAPPES_EN_ATTENTE.remove(joueur.getUUID());
        }
    }

    public static void reapparition(PlayerEvent.PlayerRespawnEvent evenement) {
        if (evenement.getEntity() instanceof ServerPlayer joueur) {
            synchroniser(joueur);
        }
    }

    /**
     * Se faire frapper sans riposter, comme Pascal : un coup reçu d'un autre joueur, dans le Royaume, ouvre un délai
     * de 10 s. Un coup rendu pendant ce délai l'annule. Un coup porté par le joueur en attente l'annule aussi.
     */
    public static void degats(LivingDamageEvent.Post evenement) {
        if (evenement.getSource().getEntity() instanceof ServerPlayer attaquant) {
            FRAPPES_EN_ATTENTE.remove(attaquant.getUUID());
        }
        if (evenement.getEntity() instanceof ServerPlayer victime && dansRoyaume(victime)
                && evenement.getSource().getEntity() instanceof Player attaquant && attaquant != victime) {
            FRAPPES_EN_ATTENTE.put(victime.getUUID(), victime.serverLevel().getGameTime());
        }
    }

    public static void attaque(AttackEntityEvent evenement) {
        FRAPPES_EN_ATTENTE.remove(evenement.getEntity().getUUID());
    }

    public static void tick(PlayerTickEvent.Post evenement) {
        if (!(evenement.getEntity() instanceof ServerPlayer joueur) || joueur.tickCount % 20 != 0) {
            return;
        }
        Long coup = FRAPPES_EN_ATTENTE.get(joueur.getUUID());
        if (coup == null || joueur.serverLevel().getGameTime() - coup < DELAI_SANS_RIPOSTE) {
            return;
        }
        FRAPPES_EN_ATTENTE.remove(joueur.getUUID());
        CompteursJour compteurs = compteurs(joueur);
        if (compteurs.frappes() >= FRAPPES_PAR_JOUR) {
            return;
        }
        noterCompteurs(joueur, new CompteursJour(compteurs.jour(), compteurs.monique(), compteurs.frappes() + 1));
        ajouter(joueur, 1, "frappe");
    }
}
