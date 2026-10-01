package com.royaumedesidees.dev;

import com.royaumedesidees.RoyaumeDesIdees;
import com.royaumedesidees.structures.StructuresRoyaume;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.client.gui.screens.GenericMessageScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
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
        RoyaumeDesIdees.LOGGER.info("[visite] {} : dimension {}, position {}", nom,
                jeu.level.dimension().location(), jeu.player.blockPosition().toShortString());
        Screenshot.grab(jeu.gameDirectory, "visite_" + nom + ".png", jeu.getMainRenderTarget(), message -> { });
        if (etape + 1 < ETAPES.size()) {
            commencer(jeu, etape + 1);
        } else {
            etape++;
            // Quitte le monde comme « Sauvegarder et quitter », pour que tout soit écrit sur le disque.
            RoyaumeDesIdees.LOGGER.info("[visite] terminee");
            jeu.level.disconnect();
            jeu.disconnect(new GenericMessageScreen(Component.literal("Visite terminée")));
        }
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
