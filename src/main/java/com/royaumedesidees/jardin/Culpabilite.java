package com.royaumedesidees.jardin;

import com.royaumedesidees.registre.ModEffets;
import com.royaumedesidees.registre.ModPiecesJointes;
import com.royaumedesidees.registre.ModSons;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

/**
 * La Culpabilité : un niveau de 0 à 5 par joueur, sauvegardé avec lui et conservé à la mort. Elle suit le joueur
 * partout, Overworld compris. Effets cumulés :
 * <ol>
 *   <li>lenteur légère (portée par l'effet {@link CulpabiliteEffet}) ;</li>
 *   <li>un nuage de pluie personnel au-dessus de la tête, visible par tous ;</li>
 *   <li>des sanglots lointains (Monique, qui pleurera elle-même en v0.3) ;</li>
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

    /** Un vol de poire : message de honte à tout le serveur, puis +1 de Culpabilité. */
    public static void volerPoire(ServerPlayer joueur) {
        joueur.server.getPlayerList().broadcastSystemMessage(
                Component.translatable("message.royaumedesidees.culpabilite.vol_poire", joueur.getDisplayName()), false);
        changer(joueur, 1);
    }

    /** Ajoute (ou retire, si {@code ecart} est négatif) des niveaux, entre 0 et 5, et met l'effet à jour. */
    public static void changer(ServerPlayer joueur, int ecart) {
        int avant = niveau(joueur);
        int apres = Math.max(0, Math.min(MAXIMUM, avant + ecart));
        joueur.setData(ModPiecesJointes.CULPABILITE, apres);
        appliquerEffet(joueur, apres);
        if (apres == MAXIMUM && avant < MAXIMUM) {
            joueur.server.getPlayerList().broadcastSystemMessage(
                    Component.translatable("message.royaumedesidees.culpabilite.ecrase", joueur.getDisplayName()), false);
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
     * du niveau (nuage de pluie, sanglots).
     */
    public static void tick(PlayerTickEvent.Post evenement) {
        if (!(evenement.getEntity() instanceof ServerPlayer joueur) || joueur.tickCount % 10 != 0 || joueur.isSpectator()) {
            return;
        }
        int niveau = niveau(joueur);
        if (niveau <= 0) {
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
        if (niveau >= 3 && joueur.getRandom().nextInt(16) == 0) {
            // Des sanglots de temps en temps (environ toutes les 8 s), audibles par ceux qui sont autour.
            monde.playSound(null, joueur.getX(), joueur.getY(), joueur.getZ(), ModSons.SANGLOTS.get(), SoundSource.PLAYERS, 0.6F, 1.0F);
        }
    }
}
