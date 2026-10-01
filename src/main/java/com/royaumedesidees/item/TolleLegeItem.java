package com.royaumedesidees.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

/**
 * Le livre « Tolle, Lege ». Pour l'instant il n'affiche que sa citation ;
 * l'allumage du portail est ajouté à l'étape 7.
 */
public class TolleLegeItem extends Item {
    public TolleLegeItem(Properties proprietes) {
        super(proprietes);
    }

    @Override
    public void appendHoverText(ItemStack pile, TooltipContext contexte, List<Component> lignes, TooltipFlag options) {
        lignes.add(Component.translatable(getDescriptionId() + ".citation").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
    }
}
