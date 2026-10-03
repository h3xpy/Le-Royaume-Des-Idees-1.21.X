package com.royaumedesidees.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

/** Un item dont l'infobulle affiche une ligne en italique (clé de traduction : nom de l'item + « .citation »). */
public class ItemAvecCitation extends Item {
    public ItemAvecCitation(Properties proprietes) {
        super(proprietes);
    }

    @Override
    public void appendHoverText(ItemStack pile, TooltipContext contexte, List<Component> lignes, TooltipFlag options) {
        lignes.add(Component.translatable(getDescriptionId() + ".citation").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
    }
}
