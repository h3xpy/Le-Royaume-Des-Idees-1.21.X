package com.royaumedesidees.dev;

import com.royaumedesidees.RoyaumeDesIdees;
import com.royaumedesidees.client.MusiqueRoyaume;
import com.royaumedesidees.monde.CaverneRoyaume;
import com.royaumedesidees.monde.ReliefRoyaume;
import com.royaumedesidees.registre.ModBlocs;
import com.royaumedesidees.registre.ModItems;
import com.royaumedesidees.structures.StructuresRoyaume;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.client.gui.screens.GenericMessageScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.common.NeoForge;

import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

/**
 * Visite automatique réservée au développement, côté client : active seulement avec
 * {@code -Droyaumedesidees.visite=true}, à n'utiliser que sur une copie du monde de test.
 * <ol>
 *   <li>voyage aller : le joueur (en créatif) entre dans le portail de test construit par {@link VerificationDev} ;</li>
 *   <li>voyage retour : il entre dans le portail de Pierre d'Ombre ;</li>
 *   <li>puis, en spectateur et en plein jour, il visite quelques points de vue.</li>
 * </ol>
 * Chaque étape note dans le journal la dimension et la position du joueur, et prend une capture
 * ({@code run/screenshots/visite_*.png}). À la fin, le joueur quitte le monde, ce qui le sauvegarde.
 */
public final class VisiteDev {
    private record Etape(String nom, Supplier<List<String>> commandes) {
    }

    private static final List<Etape> ETAPES = List.of(
            new Etape("aller", () -> {
                BlockPos portail = VerificationDev.portailTest;
                return portail == null ? List.of() : List.of("gamemode creative", String.format(Locale.ROOT,
                        "execute in minecraft:overworld run tp @s %.1f %d %.1f 0 0", portail.getX() + 0.5, portail.getY(), portail.getZ() + 0.5));
            }),
            // Une Ombre figée (sans IA, pour qu'elle reste à portée) devant le joueur enchaîné : sans Lanterne, le coup est refusé.
            new Etape("sans_lanterne", () -> List.of("summon royaumedesidees:ombre ~ ~ ~-3 {NoAI:1b}",
                    "damage @e[type=royaumedesidees:ombre,limit=1,sort=nearest] 4 minecraft:player_attack by @s")),
            // Avec la Lanterne en main, l'Ombre se révèle (capture) puis le coup porte.
            new Etape("avec_lanterne", () -> List.of("item replace entity @s weapon.mainhand with royaumedesidees:lanterne_diogene")),
            new Etape("frappe", () -> List.of(
                    "damage @e[type=royaumedesidees:ombre,limit=1,sort=nearest] 4 minecraft:player_attack by @s",
                    "data get entity @e[type=royaumedesidees:ombre,limit=1,sort=nearest] Health")),
            // Remontée par le tunnel, puis sortie à l'air libre : aveuglement, succès et Lanterne.
            new Etape("dans_le_tunnel", () -> {
                int z = 80;
                return List.of("item replace entity @s weapon.mainhand with minecraft:air",
                        "clear @s royaumedesidees:lanterne_diogene",
                        String.format(Locale.ROOT, "execute in royaumedesidees:royaume run tp @s %d %d %d 0 0",
                                Math.round(CaverneRoyaume.axeTunnel(z)), CaverneRoyaume.solTunnel(z) + 1, z));
            }),
            new Etape("lumiere", () -> List.of(String.format(Locale.ROOT, "execute in royaumedesidees:royaume run tp @s 0 %d 128 0 0",
                    ReliefRoyaume.colonne(0, 128).surface() + 1))),
            new Etape("retour", () -> List.of(String.format(Locale.ROOT,
                    "execute in royaumedesidees:royaume run tp @s %.1f %d %.1f 0 0", StructuresRoyaume.PORTAIL_X + 1.5,
                    StructuresRoyaume.PORTAIL_Y + 1, StructuresRoyaume.PORTAIL_Z + 0.5))),
            vue("jardin", 0, 135, 0, 135f, 12f, true),
            vue("port_royal", 220, 104, -190, 225f, 18f, false),
            vue("puy", 40, 150, 40, 315f, 2f, false),
            vue("hippone", -140, 118, 140, 45f, 14f, false),
            vue("bord", 742, 128, 270, 90f, 6f, false),
            vue("caverne", 0, 52, 15, 180f, 5f, false),
            vue("rampe", 0, 52, -15, 0f, 5f, false),
            vue("portail", 7, 100, 142, 180f, 10f, false),
            vue("tunnel", 0, 62, 50, 0f, -12f, false));

    /** Ticks d'attente avant de commencer, puis à chaque étape (voyage ou chargement des chunks). */
    private static final int ATTENTE_DEPART = 100;
    private static final int ATTENTE_ETAPE = 160;

    private static int compteur;
    private static int etape = -1;

    private static Etape vue(String nom, int x, int y, int z, float orientation, float inclinaison, boolean spectateur) {
        String tp = String.format(Locale.ROOT, "execute in royaumedesidees:royaume run tp @s %d %d %d %.1f %.1f",
                x, y, z, orientation, inclinaison);
        return new Etape(nom, () -> spectateur ? List.of("gamemode spectator", tp) : List.of(tp));
    }

    public static void activerSiDemande() {
        if (Boolean.getBoolean(RoyaumeDesIdees.MODID + ".visite")) {
            NeoForge.EVENT_BUS.addListener(VisiteDev::tick);
        }
    }

    private static void tick(ClientTickEvent.Post evenement) {
        Minecraft jeu = Minecraft.getInstance();
        if (jeu.player == null || jeu.getConnection() == null || etape >= ETAPES.size()) {
            return;
        }
        compteur++;
        if (etape == -1) {
            if (compteur < ATTENTE_DEPART) {
                return;
            }
            jeu.options.hideGui = true;
            jeu.getConnection().sendCommand("gamerule doDaylightCycle false");
            jeu.getConnection().sendCommand("time set 6000");
            jeu.getConnection().sendCommand("weather clear");
            jeu.getConnection().sendCommand("royaume structures");
            commencer(jeu, 0);
            return;
        }
        if (compteur < ATTENTE_ETAPE) {
            return;
        }
        String nom = ETAPES.get(etape).nom();
        RoyaumeDesIdees.LOGGER.info("[visite] {} : dimension {}, position {}, musique du Royaume en cours : {}", nom,
                jeu.level.dimension().location(), jeu.player.blockPosition().toShortString(), MusiqueRoyaume.joue());
        RoyaumeDesIdees.LOGGER.info("[visite] {} : chaînes autour {}, aveuglé {}, Lanterne dans l'inventaire {}, infobulle « Souvenir » {}",
                nom, chainesAutour(jeu), jeu.player.hasEffect(MobEffects.BLINDNESS),
                jeu.player.getInventory().hasAnyMatching(pile -> pile.is(ModItems.LANTERNE_DIOGENE.get())), infobulleSouvenir(jeu));
        Screenshot.grab(jeu.gameDirectory, "visite_" + nom + ".png", jeu.getMainRenderTarget(), message -> { });
        if (etape + 1 < ETAPES.size()) {
            commencer(jeu, etape + 1);
        } else {
            etape++;
            // Quitte le monde comme « Sauvegarder et quitter » (le jeu attend la fin de la sauvegarde), puis ferme le
            // jeu : la fenêtre ne reste pas ouverte sur un écran qui pourrait faire croire à un blocage.
            RoyaumeDesIdees.LOGGER.info("[visite] terminee");
            jeu.level.disconnect();
            jeu.disconnect(new GenericMessageScreen(Component.literal("Visite automatique terminée : le jeu se ferme")));
            RoyaumeDesIdees.LOGGER.info("[visite] monde sauvegardé, fermeture du jeu");
            jeu.stop();
        }
    }

    private static int chainesAutour(Minecraft jeu) {
        int nombre = 0;
        for (BlockPos pos : BlockPos.betweenClosed(jeu.player.blockPosition().offset(-1, 0, -1), jeu.player.blockPosition().offset(1, 2, 1))) {
            if (jeu.level.getBlockState(pos).is(ModBlocs.CHAINE_CAVERNE.get())) {
                nombre++;
            }
        }
        return nombre;
    }

    /** Vrai si l'infobulle de la Lanterne contient la ligne « Souvenir du Royaume ». */
    private static boolean infobulleSouvenir(Minecraft jeu) {
        ItemStack lanterne = new ItemStack(ModItems.LANTERNE_DIOGENE.get());
        return lanterne.getTooltipLines(Item.TooltipContext.of(jeu.level), jeu.player, TooltipFlag.NORMAL).stream()
                .anyMatch(ligne -> ligne.getContents() instanceof TranslatableContents traduit
                        && traduit.getKey().equals("tooltip.royaumedesidees.souvenir"));
    }

    private static void commencer(Minecraft jeu, int numero) {
        for (String commande : ETAPES.get(numero).commandes().get()) {
            jeu.getConnection().sendCommand(commande);
        }
        etape = numero;
        compteur = 0;
    }

    private VisiteDev() {
    }
}
