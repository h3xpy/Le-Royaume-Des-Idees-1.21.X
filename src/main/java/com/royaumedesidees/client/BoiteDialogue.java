package com.royaumedesidees.client;

import com.royaumedesidees.RoyaumeDesIdees;
import com.royaumedesidees.pnj.ParolePaquet;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.util.FormattedCharSequence;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

/**
 * La boîte de dialogue des PNJ, en haut au centre de l'écran : le nom du PNJ en couleur, sa réplique dessous, puis
 * elle s'efface. Les répliques arrivées pendant qu'une autre est affichée attendent leur tour (au moins 3 s chacune),
 * ce qui garde le chat pour les joueurs et les annonces. Une question (somme de Pascal, défi d'Adéodat) reste
 * affichée tant qu'on peut y répondre.
 */
public final class BoiteDialogue {
    private static final int LARGEUR_MAX = 300;
    private static final int MARGE = 6;
    private static final long MINIMUM_MS = 3000;
    private static final int FILE_MAX = 6;

    private static final Deque<ParolePaquet> FILE = new ArrayDeque<>();
    private static ParolePaquet actuelle;
    private static long debut;
    private static long duree;

    private BoiteDialogue() {
    }

    public static void enregistrer(RegisterGuiLayersEvent evenement) {
        evenement.registerAbove(VanillaGuiLayers.BOSS_OVERLAY, RoyaumeDesIdees.id("dialogue"), BoiteDialogue::dessiner);
    }

    /** Durée d'affichage : celle demandée, sinon de 4 à 14 s selon la longueur. */
    private static long dureeDe(ParolePaquet parole) {
        if (parole.ticks() > 0) {
            return parole.ticks() * 50L;
        }
        int lettres = parole.texte().getString().length();
        return Math.max(4000, Math.min(14000, 2500 + lettres * 60L));
    }

    private static void avancer(long maintenant) {
        for (ParolePaquet recue = ParolePaquet.prendre(); recue != null; recue = ParolePaquet.prendre()) {
            if (FILE.size() >= FILE_MAX) {
                FILE.poll();
            }
            FILE.add(recue);
        }
        if (actuelle != null) {
            long ecoule = maintenant - debut;
            if (ecoule >= duree || (!FILE.isEmpty() && ecoule >= MINIMUM_MS)) {
                actuelle = null;
            }
        }
        if (actuelle == null && !FILE.isEmpty()) {
            actuelle = FILE.poll();
            debut = maintenant;
            duree = dureeDe(actuelle);
        }
    }

    private static void dessiner(GuiGraphics graphiques, DeltaTracker temps) {
        Minecraft jeu = Minecraft.getInstance();
        long maintenant = Util.getMillis();
        avancer(maintenant);
        if (actuelle == null || jeu.options.hideGui || jeu.player == null) {
            return;
        }
        long ecoule = maintenant - debut;
        long reste = FILE.isEmpty() ? duree - ecoule : Math.max(0, MINIMUM_MS - ecoule) + 300;
        float opacite = Math.min(1.0F, Math.min(ecoule / 200.0F, reste / 400.0F));
        int alpha = (int) (opacite * 255);
        if (alpha < 8) {
            return;
        }
        Font police = jeu.font;
        int largeurMax = Math.min(LARGEUR_MAX, graphiques.guiWidth() - 32);
        List<FormattedCharSequence> lignes = police.split(actuelle.texte(), largeurMax - 2 * MARGE);
        int largeur = police.width(actuelle.nom());
        for (FormattedCharSequence ligne : lignes) {
            largeur = Math.max(largeur, police.width(ligne));
        }
        largeur += 2 * MARGE;
        int hauteur = MARGE + 11 + lignes.size() * 10 + MARGE - 2;
        int x = (graphiques.guiWidth() - largeur) / 2;
        int y = 8;

        TextColor couleurNom = actuelle.nom().getStyle().getColor();
        int accent = couleurNom == null ? 0xFFFFFF : couleurNom.getValue();
        graphiques.fill(x, y, x + largeur, y + hauteur, ((int) (alpha * 0.72F) << 24) | 0x101018);
        graphiques.fill(x, y, x + 2, y + hauteur, (alpha << 24) | accent);
        graphiques.drawString(police, actuelle.nom(), x + MARGE, y + MARGE, (alpha << 24) | 0xFFFFFF);
        for (int i = 0; i < lignes.size(); i++) {
            graphiques.drawString(police, lignes.get(i), x + MARGE, y + MARGE + 11 + i * 10, (alpha << 24) | 0xE8E8E8);
        }
        if (!FILE.isEmpty()) {
            // D'autres répliques attendent : un petit « ▼ » en bas à droite.
            Component suite = Component.literal("▼").withStyle(ChatFormatting.GRAY);
            graphiques.drawString(police, suite, x + largeur - MARGE - police.width(suite), y + hauteur - 9, (alpha << 24) | 0xAAAAAA);
        }
    }
}
