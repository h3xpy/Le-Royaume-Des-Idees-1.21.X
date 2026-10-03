package com.royaumedesidees.item;

import com.royaumedesidees.caverne.SouvenirRoyaume;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

/**
 * La Lanterne de Diogène. Tenue en main dans le Royaume, elle révèle les Ombres à moins de 8 blocs : elles
 * prennent leur vraie forme et peuvent être frappées (le calcul est fait par {@link com.royaumedesidees.entite.Ombre}).
 * Liée au Royaume : ailleurs, ce n'est qu'un souvenir.
 */
public class LanterneDiogeneItem extends Item {
    public LanterneDiogeneItem(Properties proprietes) {
        super(proprietes);
    }

    @Override
    public void appendHoverText(ItemStack pile, TooltipContext contexte, List<Component> lignes, TooltipFlag options) {
        boolean active = SouvenirRoyaume.actif(contexte.level());
        lignes.add(Component.translatable(getDescriptionId() + ".effet")
                .withStyle(active ? ChatFormatting.GOLD : ChatFormatting.DARK_GRAY));
        lignes.add(Component.translatable(getDescriptionId() + ".citation").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
    }
}
