package com.royaumedesidees.item;

import com.royaumedesidees.portail.CadrePortail;
import com.royaumedesidees.registre.ModMonde;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import java.util.List;
import java.util.Optional;

/**
 * Le livre « Tolle, Lege ». Un clic droit sur un cadre de bibliothèques complet, avec une lanterne à chaque coin,
 * allume le portail du Royaume. Le livre n'est pas consommé.
 */
public class TolleLegeItem extends Item {
    private static final String MESSAGE = "message.royaumedesidees.portail.";

    public TolleLegeItem(Properties proprietes) {
        super(proprietes);
    }

    @Override
    public InteractionResult useOn(UseOnContext contexte) {
        Level niveau = contexte.getLevel();
        if (!niveau.getBlockState(contexte.getClickedPos()).is(Blocks.BOOKSHELF)) {
            return InteractionResult.PASS;
        }
        if (niveau.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        Player joueur = contexte.getPlayer();
        if (niveau.dimension().equals(ModMonde.ROYAUME)) {
            message(joueur, "dans_royaume");
            return InteractionResult.CONSUME;
        }
        Optional<CadrePortail> cadre = CadrePortail.trouverPourAllumage(niveau, contexte.getClickedPos());
        if (cadre.isEmpty()) {
            message(joueur, "incomplet");
            return InteractionResult.CONSUME;
        }
        cadre.get().allumer(niveau);
        niveau.playSound(null, contexte.getClickedPos(), SoundEvents.BOOK_PAGE_TURN, SoundSource.BLOCKS, 1.0F, 1.0F);
        niveau.playSound(null, contexte.getClickedPos(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 1.0F, 0.8F);
        message(joueur, "allume");
        return InteractionResult.CONSUME;
    }

    private static void message(Player joueur, String cle) {
        if (joueur != null) {
            joueur.displayClientMessage(Component.translatable(MESSAGE + cle), true);
        }
    }

    @Override
    public void appendHoverText(ItemStack pile, TooltipContext contexte, List<Component> lignes, TooltipFlag options) {
        lignes.add(Component.translatable(getDescriptionId() + ".citation").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
    }
}
