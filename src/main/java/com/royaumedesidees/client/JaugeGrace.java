package com.royaumedesidees.client;

import com.royaumedesidees.RoyaumeDesIdees;
import com.royaumedesidees.grace.Grace;
import com.royaumedesidees.grace.GracePaquet;
import com.royaumedesidees.registre.ModMonde;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

/**
 * La jauge de Grâce, à droite de la barre d'objets : une auréole, une barre dorée et le nombre. Elle ne s'affiche que
 * dans le Royaume, comme tous les systèmes de la v0.3. La barre se remplit en douceur, et un « +N » s'affiche un
 * instant au-dessus quand la Grâce monte.
 */
public final class JaugeGrace {
    private static final ResourceLocation ICONE = RoyaumeDesIdees.id("textures/gui/grace.png");
    private static final int LARGEUR_BARRE = 50;

    private static float affichee = -1;
    private static int connue = -1;
    private static int dernierGain;
    private static long finGain;

    private JaugeGrace() {
    }

    public static void enregistrer(RegisterGuiLayersEvent evenement) {
        evenement.registerAbove(VanillaGuiLayers.HOTBAR, RoyaumeDesIdees.id("grace"), JaugeGrace::dessiner);
    }

    /** Vrai si la jauge est affichée en ce moment (pour la visite de développement). */
    public static boolean visible() {
        Minecraft jeu = Minecraft.getInstance();
        return jeu.player != null && jeu.level != null && !jeu.options.hideGui && !jeu.player.isSpectator()
                && jeu.level.dimension().equals(ModMonde.ROYAUME) && GracePaquet.recue() >= 0;
    }

    private static void dessiner(GuiGraphics graphiques, DeltaTracker temps) {
        Minecraft jeu = Minecraft.getInstance();
        if (!visible()) {
            return;
        }
        int valeur = GracePaquet.recue();
        long maintenant = jeu.level.getGameTime();
        if (connue >= 0 && valeur > connue) {
            dernierGain = valeur - connue;
            finGain = maintenant + 40;
        }
        connue = valeur;
        affichee = affichee < 0 ? valeur : affichee + (valeur - affichee) * 0.15F;

        int x = graphiques.guiWidth() / 2 + 96;
        int y = graphiques.guiHeight() - 19;
        graphiques.blit(ICONE, x, y, 0, 0, 9, 9, 9, 9);

        int bx = x + 11;
        int by = y + 2;
        // Cadre sombre, fond, remplissage doré avec un reflet clair sur le dessus.
        graphiques.fill(bx - 1, by - 1, bx + LARGEUR_BARRE + 1, by + 6, 0xFF2A2214);
        graphiques.fill(bx, by, bx + LARGEUR_BARRE, by + 5, 0xFF5A4A2A);
        int plein = Math.round(LARGEUR_BARRE * affichee / Grace.MAXIMUM);
        if (plein > 0) {
            graphiques.fill(bx, by, bx + plein, by + 5, 0xFFE0B23A);
            graphiques.fill(bx, by, bx + plein, by + 1, 0xFFFFE89A);
            graphiques.fill(bx, by + 4, bx + plein, by + 5, 0xFFB8860B);
        }
        graphiques.drawString(jeu.font, Integer.toString(valeur), bx + LARGEUR_BARRE + 4, y + 1, 0xFFE8C34A, true);
        if (maintenant < finGain) {
            graphiques.drawString(jeu.font, Component.literal("+" + dernierGain), bx + LARGEUR_BARRE / 2 - 4, y - 10, 0xFFFFE89A, true);
        }
    }
}
