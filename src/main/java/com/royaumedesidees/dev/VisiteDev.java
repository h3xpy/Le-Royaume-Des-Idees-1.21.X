package com.royaumedesidees.dev;

import com.royaumedesidees.RoyaumeDesIdees;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.client.gui.screens.GenericMessageScreen;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.common.NeoForge;

/**
 * Visite automatique réservée au développement, côté client : active seulement avec
 * {@code -Droyaumedesidees.visite=true}. Une fois dans le monde, le joueur passe en spectateur, se
 * téléporte en quelques points de vue du Royaume en plein jour et prend une capture d'écran à chacun
 * ({@code run/screenshots/visite_*.png}). À n'utiliser que sur une copie du monde de test.
 */
public final class VisiteDev {
    /** Points de vue : nom, x, y, z, orientation horizontale (0 = sud, 90 = ouest), inclinaison. */
    private static final Object[][] POINTS_DE_VUE = {
            {"jardin", 0, 135, 0, 135f, 12f},
            {"port_royal", 220, 104, -190, 225f, 18f},
            {"puy", 40, 150, 40, 315f, 2f},
            {"hippone", -140, 118, 140, 45f, 14f},
            {"bord", 742, 128, 270, 90f, 6f},
            {"caverne", 0, 52, 15, 180f, 5f},
            {"rampe", 0, 52, -15, 0f, 5f},
            {"portail", 7, 100, 142, 180f, 10f},
            {"tunnel", 0, 62, 50, 0f, -12f},
    };
    /** Ticks d'attente avant d'entrer dans le monde, puis entre deux points de vue (chargement des chunks). */
    private static final int ATTENTE_DEPART = 100;
    private static final int ATTENTE_POINT = 160;

    private static int compteur;
    private static int etape = -1;

    public static void activerSiDemande() {
        if (Boolean.getBoolean(RoyaumeDesIdees.MODID + ".visite")) {
            NeoForge.EVENT_BUS.addListener(VisiteDev::tick);
        }
    }

    private static void tick(ClientTickEvent.Post evenement) {
        Minecraft jeu = Minecraft.getInstance();
        if (jeu.player == null || jeu.getConnection() == null || etape >= POINTS_DE_VUE.length) {
            return;
        }
        compteur++;
        if (etape == -1) {
            if (compteur < ATTENTE_DEPART) {
                return;
            }
            jeu.options.hideGui = true;
            jeu.getConnection().sendCommand("gamemode spectator");
            jeu.getConnection().sendCommand("gamerule doDaylightCycle false");
            jeu.getConnection().sendCommand("time set 6000");
            jeu.getConnection().sendCommand("weather clear");
            jeu.getConnection().sendCommand("royaume structures");
            allerA(jeu, 0);
            return;
        }
        if (compteur < ATTENTE_POINT) {
            return;
        }
        String nom = "visite_" + POINTS_DE_VUE[etape][0] + ".png";
        Screenshot.grab(jeu.gameDirectory, nom, jeu.getMainRenderTarget(), message -> { });
        RoyaumeDesIdees.LOGGER.info("[visite] capture {}", nom);
        if (etape + 1 < POINTS_DE_VUE.length) {
            allerA(jeu, etape + 1);
        } else {
            etape++;
            // Quitte le monde comme « Sauvegarder et quitter », pour que tout soit écrit sur le disque.
            RoyaumeDesIdees.LOGGER.info("[visite] terminee");
            jeu.level.disconnect();
            jeu.disconnect(new GenericMessageScreen(Component.literal("Visite terminée")));
        }
    }

    private static void allerA(Minecraft jeu, int numero) {
        Object[] point = POINTS_DE_VUE[numero];
        jeu.getConnection().sendCommand(String.format(java.util.Locale.ROOT,
                "execute in royaumedesidees:royaume run tp @s %d %d %d %.1f %.1f",
                point[1], point[2], point[3], point[4], point[5]));
        etape = numero;
        compteur = 0;
    }

    private VisiteDev() {
    }
}
