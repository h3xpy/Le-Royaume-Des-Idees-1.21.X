package com.royaumedesidees.mixin;

import com.royaumedesidees.bloc.PupitreAmbroiseBloc;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Grille de craft (établi et inventaire) : le Pupitre d'Ambroise ne sort que pour qui a appris son silence. */
@Mixin(CraftingMenu.class)
public abstract class CraftingMenuMixin {
    @Inject(method = "slotChangedCraftingGrid", at = @At("TAIL"))
    private static void royaumedesidees$pupitre(AbstractContainerMenu menu, Level niveau, Player joueur, CraftingContainer grille,
                                                ResultContainer resultat, RecipeHolder<CraftingRecipe> recette, CallbackInfo info) {
        if (joueur instanceof ServerPlayer serveur) {
            PupitreAmbroiseBloc.verifierCraft(menu, serveur, resultat);
        }
    }
}
