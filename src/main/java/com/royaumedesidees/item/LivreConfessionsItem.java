package com.royaumedesidees.item;

import com.royaumedesidees.grace.Grace;
import com.royaumedesidees.grace.Voie;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Le Livre des Confessions, outil de la Voie du Cœur : un clic droit soigne de 4 cœurs tous les joueurs à moins de
 * 6 blocs (soi compris), une fois par minute. Il marche aussi hors du Royaume (tag {@code actif_hors_royaume}), mais
 * pour les convertis seulement : le livre ne parle qu'aux cœurs qui ont suivi la Voie d'Augustin.
 */
public class LivreConfessionsItem extends ItemAvecCitation {
    private static final double RAYON = 6.0;
    private static final float SOIN = 8.0F;
    private static final int RECHARGE = 60 * 20;

    public LivreConfessionsItem(Properties proprietes) {
        super(proprietes);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level niveau, Player joueur, InteractionHand main) {
        ItemStack livre = joueur.getItemInHand(main);
        if (!(joueur instanceof ServerPlayer serveur) || !(niveau instanceof ServerLevel monde)) {
            return InteractionResultHolder.sidedSuccess(livre, niveau.isClientSide);
        }
        if (Grace.voie(serveur) != Voie.COEUR) {
            serveur.displayClientMessage(Component.translatable("message.royaumedesidees.livre_confessions.muet")
                    .withStyle(ChatFormatting.GRAY), true);
            return InteractionResultHolder.fail(livre);
        }
        int soignes = 0;
        for (ServerPlayer autre : monde.players()) {
            if (autre.distanceToSqr(serveur) <= RAYON * RAYON && !autre.isSpectator()) {
                autre.heal(SOIN);
                monde.sendParticles(ParticleTypes.HEART, autre.getX(), autre.getEyeY() + 0.3, autre.getZ(), 4, 0.4, 0.3, 0.4, 0.0);
                soignes++;
            }
        }
        monde.playSound(null, serveur.blockPosition(), SoundEvents.BOOK_PAGE_TURN, SoundSource.PLAYERS, 1.0F, 1.0F);
        monde.playSound(null, serveur.blockPosition(), SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 1.0F, 1.2F);
        serveur.displayClientMessage(Component.translatable("message.royaumedesidees.livre_confessions.lu", soignes)
                .withStyle(ChatFormatting.GOLD), true);
        serveur.getCooldowns().addCooldown(this, RECHARGE);
        return InteractionResultHolder.consume(livre);
    }
}
