package com.royaumedesidees.client;

import com.royaumedesidees.caverne.SouvenirRoyaume;
import com.royaumedesidees.registre.ModTags;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

/**
 * Hors du Royaume, les objets liés au Royaume affichent « Souvenir du Royaume » en gris, juste sous leur nom. Les objets
 * de la liste « actifs hors du Royaume » disent, eux, qu'ils marchent partout.
 */
public final class InfobulleSouvenir {
    private InfobulleSouvenir() {
    }

    public static void infobulle(ItemTooltipEvent evenement) {
        if (evenement.getItemStack().is(ModTags.ACTIF_HORS_ROYAUME)) {
            evenement.getToolTip().add(Math.min(1, evenement.getToolTip().size()),
                    Component.translatable("tooltip.royaumedesidees.actif_partout").withStyle(ChatFormatting.DARK_GREEN));
            return;
        }
        if (!evenement.getItemStack().is(ModTags.LIE_AU_ROYAUME) || SouvenirRoyaume.actif(Minecraft.getInstance().level)) {
            return;
        }
        int position = Math.min(1, evenement.getToolTip().size());
        evenement.getToolTip().add(position, Component.translatable("tooltip.royaumedesidees.souvenir").withStyle(ChatFormatting.GRAY));
        evenement.getToolTip().add(position + 1, Component.translatable("tooltip.royaumedesidees.souvenir.detail").withStyle(ChatFormatting.DARK_GRAY));
    }
}
