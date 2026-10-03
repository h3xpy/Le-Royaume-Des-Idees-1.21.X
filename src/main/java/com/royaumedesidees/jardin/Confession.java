package com.royaumedesidees.jardin;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.royaumedesidees.registre.ModBlocs;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * La commande {@code /confesse <ton péché>}, ouverte à tous les joueurs. Il faut se tenir à 5 blocs ou moins d'un
 * Confessionnal ; le texte part à tout le serveur, et la Culpabilité baisse d'un niveau. Une confession toutes les
 * 10 secondes au plus, et jamais deux fois de suite le même péché : qui se répète reçoit une claque.
 */
public final class Confession {
    public static final int DISTANCE = 5;
    public static final int LONGUEUR_MAX = 100;
    private static final int DELAI_TICKS = 200;
    private static final String CLE = "message.royaumedesidees.confession.";

    /** Dernière confession de chaque joueur (heure du jeu, en ticks). */
    private static final Map<UUID, Long> DERNIERE = new HashMap<>();
    /** Texte de la dernière confession acceptée de chaque joueur, normalisé (minuscules, espaces simples). */
    private static final Map<UUID, String> DERNIER_TEXTE = new HashMap<>();

    private Confession() {
    }

    public static void enregistrer(RegisterCommandsEvent evenement) {
        evenement.getDispatcher().register(Commands.literal("confesse")
                .then(Commands.argument("peche", StringArgumentType.greedyString())
                        .executes(Confession::confesser)));
    }

    private static int confesser(CommandContext<CommandSourceStack> contexte) throws CommandSyntaxException {
        ServerPlayer joueur = contexte.getSource().getPlayerOrException();
        String peche = StringArgumentType.getString(contexte, "peche").trim();
        if (peche.length() > LONGUEUR_MAX) {
            contexte.getSource().sendFailure(Component.translatable(CLE + "trop_long", LONGUEUR_MAX));
            return 0;
        }
        if (!presDUnConfessionnal(joueur)) {
            contexte.getSource().sendFailure(Component.translatable(CLE + "loin"));
            return 0;
        }
        String normalise = normaliser(peche);
        if (normalise.equals(DERNIER_TEXTE.get(joueur.getUUID()))) {
            claque(joueur);
            return 0;
        }
        long maintenant = joueur.serverLevel().getGameTime();
        Long derniere = DERNIERE.get(joueur.getUUID());
        if (derniere != null && maintenant - derniere < DELAI_TICKS) {
            long secondes = (DELAI_TICKS - (maintenant - derniere) + 19) / 20;
            contexte.getSource().sendFailure(Component.translatable(CLE + "trop_tot", secondes));
            return 0;
        }
        DERNIERE.put(joueur.getUUID(), maintenant);
        DERNIER_TEXTE.put(joueur.getUUID(), normalise);

        joueur.server.getPlayerList().broadcastSystemMessage(
                Component.translatable(CLE + "publique", joueur.getDisplayName(), peche), false);
        ServerLevel monde = joueur.serverLevel();
        monde.playSound(null, joueur.blockPosition(), SoundEvents.BELL_BLOCK, SoundSource.PLAYERS, 1.0F, 1.0F);
        if (Culpabilite.niveau(joueur) == 0) {
            joueur.sendSystemMessage(Component.translatable(CLE + "rien"));
            return 1;
        }
        Culpabilite.changer(joueur, -1);
        // Dans le Royaume seulement : se confesser rapporte de la Grâce.
        com.royaumedesidees.grace.Grace.ajouter(joueur, 5, "confession");
        monde.sendParticles(ParticleTypes.END_ROD, joueur.getX(), joueur.getY() + 1.0, joueur.getZ(), 12, 0.4, 0.6, 0.4, 0.02);
        joueur.sendSystemMessage(Component.translatable(CLE + "allege", Culpabilite.niveau(joueur)));
        return 1;
    }

    private static String normaliser(String texte) {
        return texte.toLowerCase(java.util.Locale.ROOT).replaceAll("[\\s\\p{Punct}]+", " ").trim();
    }

    /**
     * Le même péché deux fois de suite : une claque. Un demi-cœur de dégâts (jamais mortel), un recul, le bruit
     * d'un coup, et une remarque. La Culpabilité ne baisse pas.
     */
    private static void claque(ServerPlayer joueur) {
        ServerLevel monde = joueur.serverLevel();
        if (joueur.getHealth() > 2.0F) {
            joueur.hurt(monde.damageSources().generic(), 1.0F);
        }
        // Le recul part à l'opposé du regard : on donne la direction du regard à knockback, qui pousse en sens inverse.
        double angle = joueur.getYRot() * Math.PI / 180.0;
        joueur.knockback(0.6, -Math.sin(angle), Math.cos(angle));
        joueur.hurtMarked = true;
        monde.playSound(null, joueur.blockPosition(), SoundEvents.PLAYER_ATTACK_STRONG, SoundSource.PLAYERS, 1.0F, 1.2F);
        monde.sendParticles(ParticleTypes.CRIT, joueur.getX(), joueur.getEyeY(), joueur.getZ(), 8, 0.3, 0.2, 0.3, 0.1);
        joueur.sendSystemMessage(Component.translatable(CLE + "repetee"));
    }

    private static boolean presDUnConfessionnal(ServerPlayer joueur) {
        BlockPos centre = joueur.blockPosition();
        for (BlockPos pos : BlockPos.betweenClosed(centre.offset(-DISTANCE, -DISTANCE, -DISTANCE), centre.offset(DISTANCE, DISTANCE, DISTANCE))) {
            if (joueur.level().getBlockState(pos).is(ModBlocs.CONFESSIONNAL.get())) {
                return true;
            }
        }
        return false;
    }
}
