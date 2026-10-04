package com.royaumedesidees.pnj;

import com.royaumedesidees.bloc.ZonesSilence;
import com.royaumedesidees.grace.Grace;
import com.royaumedesidees.grace.Voie;
import com.royaumedesidees.registre.ModItems;
import com.royaumedesidees.registre.ModPiecesJointes;
import com.royaumedesidees.structures.StructuresJardin;
import com.royaumedesidees.structures.StructuresPnj;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * La quête de conversion, qui donne la Voie du Cœur, en quatre étapes tirées des Confessions :
 * <ol>
 *   <li><b>Les poires de Thagaste</b> (livre II) : voler 3 poires avec Augustin jeune à côté ;</li>
 *   <li><b>Le silence d'Ambroise</b> (VI, 3) : rester 60 s dans la zone de silence de son pupitre sans rien écrire ;</li>
 *   <li><b>Prends, lis</b> (VIII, 12) : pleurer sous le figuier (s'accroupir 5 s dessous) jusqu'à entendre la voix
 *       d'enfant venue de la maison voisine, puis lire l'Épître aux Romains sur le lutrin de l'exèdre d'Alypius ;</li>
 *   <li><b>Le baptême</b> (IX, 6) : Ambroise baptise le joueur, qui reçoit la Voie du Cœur, le Livre des Confessions,
 *       le Sceau de la Conversion et 20 de Grâce.</li>
 * </ol>
 * L'avancement est gardé dans la donnée de joueur {@code quete_conversion} ; tout se passe dans le Royaume.
 */
public final class QueteConversion {
    public static final int AUCUNE = 0;
    /** Étape 1 ; les unités comptent les poires volées (100, 101, 102). */
    public static final int POIRES = 100;
    public static final int SILENCE = 200;
    public static final int FIGUIER = 300;
    public static final int ROMAINS = 310;
    public static final int BAPTEME = 400;
    public static final int FINIE = 500;

    private static final int POIRES_A_VOLER = 3;
    private static final int DUREE_SILENCE = 60 * 20;
    private static final int DUREE_LARMES = 5 * 20;
    private static final String CLE = "message.royaumedesidees.quete.";

    private static final Map<UUID, Integer> SILENCE_TENU = new HashMap<>();
    private static final Map<UUID, Integer> LARMES = new HashMap<>();
    /** Sous le figuier : jusqu'à 9 blocs du tronc (toute la couronne). */
    private static final double RAYON_FIGUIER = 9.0;

    private QueteConversion() {
    }

    public static int etat(Player joueur) {
        return joueur.getData(ModPiecesJointes.QUETE_CONVERSION);
    }

    private static void passer(ServerPlayer joueur, int etat, String message) {
        joueur.setData(ModPiecesJointes.QUETE_CONVERSION, etat);
        joueur.sendSystemMessage(Component.translatable(CLE + message).withStyle(ChatFormatting.GOLD, ChatFormatting.ITALIC));
    }

    // ------------------------------------------------------------------ Étape 1 : les poires

    /** Augustin propose la quête (ou rappelle où on en est) quand on lui parle. */
    public static void parlerAugustin(AugustinJeune augustin, ServerPlayer joueur) {
        int etat = etat(joueur);
        if (etat == AUCUNE) {
            augustin.parler(joueur, "quete.debut", POIRES_A_VOLER);
            passer(joueur, POIRES, "poires");
        } else if (etat < SILENCE) {
            augustin.parler(joueur, "quete.poires", POIRES_A_VOLER - (etat - POIRES));
        } else if (etat < FIGUIER) {
            augustin.parler(joueur, "quete.silence");
        } else if (etat < BAPTEME) {
            augustin.parler(joueur, "quete.figuier");
        } else if (etat < FINIE) {
            augustin.parler(joueur, "quete.bapteme");
        } else {
            augustin.parler(joueur, "quete.finie");
        }
    }

    /**
     * Un vol de poire dans le Royaume : Augustin, s'il est à moins de 32 blocs et que le voleur n'est pas dans le
     * silence d'Ambroise (où personne n'entend rien), accourt et en redemande. Le vol compte pour la quête.
     */
    public static void poireVolee(ServerPlayer joueur) {
        if (ZonesSilence.dansZone(joueur)) {
            return;
        }
        AugustinJeune augustin = joueur.serverLevel().getEntitiesOfClass(AugustinJeune.class,
                new AABB(joueur.blockPosition()).inflate(32)).stream().findFirst().orElse(null);
        if (augustin == null) {
            return;
        }
        augustin.accourir(joueur);
        int etat = etat(joueur);
        if (etat == AUCUNE) {
            etat = POIRES;
            passer(joueur, POIRES, "poires");
        }
        if (etat >= POIRES && etat < SILENCE) {
            int volees = etat - POIRES + 1;
            if (volees >= POIRES_A_VOLER) {
                augustin.parler(joueur, "quete.porcs");
                passer(joueur, SILENCE, "silence");
            } else {
                joueur.setData(ModPiecesJointes.QUETE_CONVERSION, POIRES + volees);
                augustin.parler(joueur, "vol", POIRES_A_VOLER - volees);
            }
        } else {
            augustin.parler(joueur, "vol_encore");
        }
    }

    // ------------------------------------------------------------------ Étapes 2 et 3 : le silence, le figuier

    /** Une ligne de chat tentée dans le silence d'Ambroise : le compteur repart de zéro. */
    public static void chatTente(ServerPlayer joueur) {
        if (SILENCE_TENU.remove(joueur.getUUID()) != null && etat(joueur) == SILENCE) {
            joueur.displayClientMessage(Component.translatable(CLE + "silence_rompu").withStyle(ChatFormatting.GRAY), true);
        }
    }

    public static void tick(PlayerTickEvent.Post evenement) {
        if (!(evenement.getEntity() instanceof ServerPlayer joueur) || !Grace.dansRoyaume(joueur) || joueur.isSpectator()) {
            return;
        }
        int etat = etat(joueur);
        if (etat == SILENCE) {
            if (ZonesSilence.dansZone(joueur)) {
                int tenu = SILENCE_TENU.merge(joueur.getUUID(), 1, Integer::sum);
                if (tenu % 200 == 0 && tenu < DUREE_SILENCE) {
                    joueur.displayClientMessage(Component.translatable(CLE + "silence_compte", (DUREE_SILENCE - tenu) / 20)
                            .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC), true);
                }
                if (tenu >= DUREE_SILENCE) {
                    SILENCE_TENU.remove(joueur.getUUID());
                    passer(joueur, FIGUIER, "figuier");
                }
            } else {
                SILENCE_TENU.remove(joueur.getUUID());
            }
        }
        BlockPos figuier = StructuresJardin.POS_FIGUIER;
        double dx = joueur.getX() - (figuier.getX() + 0.5);
        double dz = joueur.getZ() - (figuier.getZ() + 0.5);
        boolean sousLeFiguier = dx * dx + dz * dz <= RAYON_FIGUIER * RAYON_FIGUIER && Math.abs(joueur.getY() - figuier.getY()) <= 4;
        if (sousLeFiguier && joueur.isShiftKeyDown() && etat != FIGUIER && etat < ROMAINS && joueur.tickCount % 40 == 0) {
            // Pleurer sous le figuier trop tôt : le figuier le dit, pour qu'on sache que c'est le bon endroit.
            joueur.displayClientMessage(Component.translatable(CLE + (etat == AUCUNE ? "figuier_inconnu" : "figuier_trop_tot"))
                    .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC), true);
        }
        if (etat == FIGUIER) {
            if (sousLeFiguier && joueur.isShiftKeyDown()) {
                int larmes = LARMES.merge(joueur.getUUID(), 1, Integer::sum);
                if (larmes % 20 == 0 && larmes < DUREE_LARMES) {
                    joueur.displayClientMessage(Component.translatable(CLE + "larmes", larmes / 20, DUREE_LARMES / 20)
                            .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC), true);
                }
                if (larmes % 10 == 0) {
                    joueur.serverLevel().sendParticles(ParticleTypes.FALLING_WATER, joueur.getX(), joueur.getEyeY() - 0.2,
                            joueur.getZ(), 3, 0.2, 0.05, 0.2, 0.0);
                }
                if (larmes >= DUREE_LARMES) {
                    LARMES.remove(joueur.getUUID());
                    voixDEnfant(joueur);
                    passer(joueur, ROMAINS, "romains");
                }
            } else {
                LARMES.remove(joueur.getUUID());
            }
        }
    }

    /** « Prends, lis ; prends, lis » : une voix d'enfant, venue de la fenêtre de la maison voisine. */
    private static void voixDEnfant(ServerPlayer joueur) {
        joueur.setData(ModPiecesJointes.FIGUIER_ENTENDU, true);
        joueur.sendSystemMessage(Component.translatable("message.royaumedesidees.figuier.voix").withStyle(ChatFormatting.ITALIC));
        ServerLevel monde = joueur.serverLevel();
        BlockPos fenetre = StructuresJardin.POS_VOIX_ENFANT;
        float[] notes = {1.19F, 1.0F, 1.19F, 1.0F};
        int[] temps = {0, 8, 24, 32, 56, 64, 80, 88};
        for (int i = 0; i < temps.length; i++) {
            float note = notes[i % 4];
            Echeancier.plusTard(temps[i], () -> monde.playSound(null, fenetre, SoundEvents.NOTE_BLOCK_FLUTE.value(),
                    SoundSource.NEUTRAL, 1.5F, note));
        }
    }

    /** Lire l'Épître aux Romains sur le lutrin de l'exèdre d'Alypius, une fois la voix entendue. */
    public static void lutrin(PlayerInteractEvent.RightClickBlock evenement) {
        if (!(evenement.getEntity() instanceof ServerPlayer joueur) || etat(joueur) != ROMAINS
                || !evenement.getPos().equals(StructuresJardin.POS_LUTRIN_FIGUIER)) {
            return;
        }
        joueur.serverLevel().sendParticles(ParticleTypes.END_ROD, joueur.getX(), joueur.getEyeY(), joueur.getZ(), 20, 0.5, 0.5, 0.5, 0.02);
        passer(joueur, BAPTEME, "bapteme");
    }

    // ------------------------------------------------------------------ Étape 4 : le baptême

    /** Ambroise baptise le joueur s'il est prêt. Renvoie faux si ce n'est pas le moment (Ambroise répond alors par un geste). */
    public static boolean bapteme(Ambroise ambroise, ServerPlayer joueur) {
        int etat = etat(joueur);
        if (etat >= FINIE && Grace.voie(joueur) == Voie.RAISON) {
            // Déjà baptisé, passé à la Raison : revenir au Cœur coûte 50 de Grâce.
            if (Grace.depenser(joueur, Grace.PRIX_CHANGER_VOIE)) {
                ambroise.parler(joueur, "retour_coeur");
                com.royaumedesidees.grace.Voies.choisir(joueur, Voie.COEUR);
            } else {
                ambroise.parler(joueur, "pas_assez_de_grace", Grace.PRIX_CHANGER_VOIE);
            }
            return true;
        }
        if (etat == SILENCE && ZonesSilence.dansZone(joueur)) {
            ambroise.parler(joueur, "silence");
            return true;
        }
        if (etat != BAPTEME) {
            return false;
        }
        if (Grace.voie(joueur) == Voie.RAISON && !Grace.depenser(joueur, Grace.PRIX_CHANGER_VOIE)) {
            ambroise.parler(joueur, "pas_assez_de_grace", Grace.PRIX_CHANGER_VOIE);
            return true;
        }
        ServerLevel monde = joueur.serverLevel();
        joueur.setData(ModPiecesJointes.QUETE_CONVERSION, FINIE);
        ambroise.parler(joueur, "bapteme");
        monde.sendParticles(ParticleTypes.SPLASH, joueur.getX(), joueur.getEyeY() + 0.5, joueur.getZ(), 40, 0.3, 0.2, 0.3, 0.1);
        monde.sendParticles(ParticleTypes.END_ROD, joueur.getX(), joueur.getY() + 1.0, joueur.getZ(), 30, 0.5, 0.8, 0.5, 0.03);
        for (int i = 0; i < 3; i++) {
            Echeancier.plusTard(i * 25, () -> monde.playSound(null, StructuresPnj.POS_PUPITRE, SoundEvents.BELL_BLOCK, SoundSource.BLOCKS, 1.5F, 1.0F));
        }
        joueur.server.getPlayerList().broadcastSystemMessage(
                Component.translatable("message.royaumedesidees.bapteme.annonce", joueur.getDisplayName()).withStyle(ChatFormatting.GOLD), false);
        com.royaumedesidees.grace.Voies.choisir(joueur, Voie.COEUR);
        donner(joueur, new ItemStack(ModItems.LIVRE_CONFESSIONS.get()));
        donner(joueur, new ItemStack(ModItems.SCEAU_CONVERSION.get()));
        Grace.ajouter(joueur, 20, "quete");
        return true;
    }

    private static void donner(ServerPlayer joueur, ItemStack objet) {
        if (!joueur.getInventory().add(objet)) {
            joueur.drop(objet, false);
        }
    }
}
