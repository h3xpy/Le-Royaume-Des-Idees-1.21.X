package com.royaumedesidees.pnj;

import com.royaumedesidees.grace.Grace;
import com.royaumedesidees.grace.Voie;
import com.royaumedesidees.grace.Voies;
import com.royaumedesidees.registre.ModPiecesJointes;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * La quête des impôts de Rouen, qui donne la Voie de la Raison. Le père de Pascal, Étienne, répartit les impôts en
 * Normandie et passe ses nuits sur des additions en livres, sols et deniers (20 sols font une livre, 12 deniers un
 * sol) ; c'est pour lui que Pascal, à 19 ans, inventa la Pascaline. Trois sommes, à donner dans le chat : la première
 * est celle des comptes posés sur le lutrin de sa cellule.
 * <p>
 * Donnée {@code quete_impots} : 0 pas commencée, 1 à 3 la somme en cours, 4 finie.
 */
public final class QueteImpots {
    public static final int AUCUNE = 0;
    public static final int FINIE = 4;
    private static final int SOMMES = 3;
    private static final int DELAI_REPONSE = 90 * 20;
    private static final Pattern NOMBRE = Pattern.compile("\\d+");

    /** Une somme en livres, sols et deniers. */
    private record Montant(int livres, int sols, int deniers) {
        int enDeniers() {
            return (livres * 20 + sols) * 12 + deniers;
        }

        static Montant de(int deniers) {
            return new Montant(deniers / 240, (deniers / 12) % 20, deniers % 12);
        }

        Component texte() {
            return Component.translatable("message.royaumedesidees.impots.montant", livres, sols, deniers);
        }
    }

    private QueteImpots() {
    }

    public static int etat(ServerPlayer joueur) {
        return joueur.getData(ModPiecesJointes.QUETE_IMPOTS);
    }

    /** Les trois montants à additionner pour la somme numéro {@code n} (1 à 3), toujours les mêmes pour un joueur. */
    private static List<Montant> somme(ServerPlayer joueur, int n) {
        List<Montant> montants = new ArrayList<>();
        if (n == 1) {
            // Les comptes de la cellule : 3 l 7 s 4 d + 12 l 15 s 9 d + 2 l 19 s 11 d.
            montants.add(new Montant(3, 7, 4));
            montants.add(new Montant(12, 15, 9));
            montants.add(new Montant(2, 19, 11));
            return montants;
        }
        RandomSource hasard = RandomSource.create(joueur.getUUID().getLeastSignificantBits() * 31 + n);
        for (int i = 0; i < 3; i++) {
            montants.add(new Montant(1 + hasard.nextInt(25), hasard.nextInt(20), hasard.nextInt(12)));
        }
        return montants;
    }

    /** Clic droit sur Pascal. */
    public static void parlerPascal(Pascal pascal, ServerPlayer joueur) {
        if (ReponsesChat.enAttente(joueur)) {
            pascal.parler(joueur, "impots.attends");
            return;
        }
        int etat = etat(joueur);
        if (etat >= FINIE) {
            if (Grace.voie(joueur) == Voie.RAISON) {
                pascal.parler(joueur, "raison");
            } else if (Grace.depenser(joueur, Grace.PRIX_CHANGER_VOIE)) {
                pascal.parler(joueur, "retour_raison");
                Voies.choisir(joueur, Voie.RAISON);
            } else {
                pascal.parler(joueur, "pas_assez_de_grace", Grace.PRIX_CHANGER_VOIE);
            }
            return;
        }
        if (etat == AUCUNE) {
            pascal.parler(joueur, "impots.debut");
            joueur.setData(ModPiecesJointes.QUETE_IMPOTS, 1);
            etat = 1;
        }
        poser(pascal, joueur, etat);
    }

    private static void poser(Pascal pascal, ServerPlayer joueur, int n) {
        List<Montant> montants = somme(joueur, n);
        pascal.parlerPendant(joueur, DELAI_REPONSE, "impots.somme", n, SOMMES, montants.get(0).texte(), montants.get(1).texte(), montants.get(2).texte(),
                DELAI_REPONSE / 20);
        int total = montants.stream().mapToInt(Montant::enDeniers).sum();
        ReponsesChat.attendre(joueur, DELAI_REPONSE, reponse -> corriger(pascal, joueur, n, total, reponse),
                () -> pascal.parler(joueur, "impots.temps"));
    }

    private static void corriger(Pascal pascal, ServerPlayer joueur, int n, int total, String reponse) {
        List<Integer> nombres = new ArrayList<>();
        Matcher m = NOMBRE.matcher(reponse);
        while (m.find() && nombres.size() < 4) {
            nombres.add(Integer.parseInt(m.group()));
        }
        Montant juste = Montant.de(total);
        boolean bon = nombres.size() == 3 && nombres.get(0) == juste.livres() && nombres.get(1) == juste.sols()
                && nombres.get(2) == juste.deniers();
        if (!bon) {
            pascal.parler(joueur, nombres.size() == 3 ? "impots.faux" : "impots.format");
            return;
        }
        joueur.serverLevel().playSound(null, pascal, SoundEvents.VILLAGER_WORK_CARTOGRAPHER, SoundSource.NEUTRAL, 1.0F, 1.0F);
        if (n < SOMMES) {
            joueur.setData(ModPiecesJointes.QUETE_IMPOTS, n + 1);
            pascal.parler(joueur, "impots.juste");
            poser(pascal, joueur, n + 1);
            return;
        }
        joueur.setData(ModPiecesJointes.QUETE_IMPOTS, FINIE);
        pascal.parler(joueur, "impots.fin");
        Grace.ajouter(joueur, 20, "quete");
        if (Grace.voie(joueur) == Voie.COEUR && !Grace.depenser(joueur, Grace.PRIX_CHANGER_VOIE)) {
            pascal.parler(joueur, "pas_assez_de_grace", Grace.PRIX_CHANGER_VOIE);
            return;
        }
        Voies.choisir(joueur, Voie.RAISON);
        joueur.sendSystemMessage(Component.translatable("message.royaumedesidees.impots.raison").withStyle(ChatFormatting.GOLD, ChatFormatting.ITALIC));
    }

    /** L'étape à afficher dans le journal ({@code /quete}). */
    static String etapeJournal(ServerPlayer joueur) {
        int etat = etat(joueur);
        return etat == AUCUNE ? "aucune" : etat >= FINIE ? "finie" : "en_cours";
    }
}
