package com.royaumedesidees.jardin;

import com.royaumedesidees.grace.Grace;
import com.royaumedesidees.registre.ModEffets;
import com.royaumedesidees.registre.ModPiecesJointes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

/**
 * La Culpabilité : un niveau de 0 à 5 par joueur, sauvegardé avec lui et conservé à la mort. Elle n'existe que dans
 * le Royaume : ailleurs, elle est en pause (ni effet, ni nuage, ni sanglots) et reprend intacte au retour. Effets
 * cumulés :
 * <ol>
 *   <li>lenteur légère (portée par l'effet {@link CulpabiliteEffet}) ;</li>
 *   <li>un nuage de pluie personnel au-dessus de la tête, visible par tous ;</li>
 *   <li>Monique pleure plus fort (voir {@link com.royaumedesidees.pnj.Monique}) ;</li>
 *   <li>une musique dramatique, côté client ({@link com.royaumedesidees.client.MusiqueRoyaume}) ;</li>
 *   <li>« [pseudo] croule sous la culpabilité. » annoncé à tout le serveur en atteignant ce niveau.</li>
 * </ol>
 */
public final class Culpabilite {
    public static final int MAXIMUM = 5;

    private Culpabilite() {
    }

    public static int niveau(Player joueur) {
        return joueur.getData(ModPiecesJointes.CULPABILITE);
    }

    /** Un vol de poire : message de honte à tout le serveur, puis +1 de Culpabilité. Hors du Royaume, ça ne compte pas. */
    public static void volerPoire(ServerPlayer joueur) {
        if (!Grace.dansRoyaume(joueur)) {
            return;
        }
        joueur.server.getPlayerList().broadcastSystemMessage(
                Component.translatable("message.royaumedesidees.culpabilite.vol_poire", joueur.getDisplayName()), false);
        changer(joueur, 1);
        // Augustin jeune accourt (s'il est près) et le vol compte pour la quête de conversion.
        com.royaumedesidees.pnj.QueteConversion.poireVolee(joueur);
    }

    /** Ajoute (ou retire, si {@code ecart} est négatif) des niveaux, entre 0 et 5, et met l'effet à jour. */
    public static void changer(ServerPlayer joueur, int ecart) {
        int avant = niveau(joueur);
        int apres = Math.max(0, Math.min(MAXIMUM, avant + ecart));
        joueur.setData(ModPiecesJointes.CULPABILITE, apres);
        appliquerEffet(joueur, Grace.dansRoyaume(joueur) ? apres : 0);
        if (apres == MAXIMUM && avant < MAXIMUM) {
            joueur.server.getPlayerList().broadcastSystemMessage(
                    Component.translatable("message.royaumedesidees.culpabilite.ecrase", joueur.getDisplayName()), false);
        }
    }

    /** En quittant le Royaume, l'effet disparaît (il reviendra au retour). */
    public static void changementDimension(net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent evenement) {
        if (evenement.getEntity() instanceof ServerPlayer joueur && !Grace.dansRoyaume(joueur)) {
            appliquerEffet(joueur, 0);
        }
    }

    private static void appliquerEffet(ServerPlayer joueur, int niveau) {
        if (niveau <= 0) {
            joueur.removeEffect(ModEffets.CULPABILITE);
            return;
        }
        MobEffectInstance actuel = joueur.getEffect(ModEffets.CULPABILITE);
        if (actuel == null || actuel.getAmplifier() != niveau - 1 || !actuel.isInfiniteDuration()) {
            joueur.removeEffect(ModEffets.CULPABILITE);
            joueur.addEffect(new MobEffectInstance(ModEffets.CULPABILITE, MobEffectInstance.INFINITE_DURATION, niveau - 1, false, false, true));
        }
    }

    /**
     * Toutes les demi-secondes : remet l'effet s'il a disparu (mort, commande /effect), puis joue les effets
     * du niveau (nuage de pluie). Les pleurs sont ceux de Monique, qui suit le joueur.
     */
    public static void tick(PlayerTickEvent.Post evenement) {
        if (!(evenement.getEntity() instanceof ServerPlayer joueur) || joueur.tickCount % 10 != 0 || joueur.isSpectator()) {
            return;
        }
        int niveau = niveau(joueur);
        if (niveau <= 0) {
            return;
        }
        if (!Grace.dansRoyaume(joueur)) {
            // En pause hors du Royaume : l'effet est retiré, le niveau reste en mémoire.
            appliquerEffet(joueur, 0);
            return;
        }
        appliquerEffet(joueur, niveau);
        ServerLevel monde = joueur.serverLevel();
        if (niveau >= 2) {
            // Nuage de pluie personnel, au-dessus de la tête, qui goutte.
            double y = joueur.getY() + joueur.getBbHeight() + 0.9;
            monde.sendParticles(ParticleTypes.CLOUD, joueur.getX(), y, joueur.getZ(), 4, 0.35, 0.08, 0.35, 0.0);
            monde.sendParticles(ParticleTypes.FALLING_WATER, joueur.getX(), y - 0.2, joueur.getZ(), 3, 0.3, 0.0, 0.3, 0.0);
        }
    }
}
