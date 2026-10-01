package com.royaumedesidees.donnees;

import com.royaumedesidees.RoyaumeDesIdees;
import com.royaumedesidees.registre.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.concurrent.CompletableFuture;

public class ModRecettes extends RecipeProvider {
    public ModRecettes(PackOutput sortie, CompletableFuture<HolderLookup.Provider> registres) {
        super(sortie, registres);
    }

    @Override
    protected void buildRecipes(RecipeOutput sortie) {
        // 4 Pierres d'Ombre en carré donnent 4 Pierres taillées, comme les briques de pierre.
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModItems.PIERRE_OMBRE_TAILLEE.get(), 4)
                .pattern("##")
                .pattern("##")
                .define('#', ModItems.PIERRE_OMBRE.get())
                .unlockedBy(getHasName(ModItems.PIERRE_OMBRE.get()), has(ModItems.PIERRE_OMBRE.get()))
                .save(sortie);

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(ModItems.PIERRE_OMBRE.get()), RecipeCategory.BUILDING_BLOCKS, ModItems.PIERRE_OMBRE_TAILLEE.get())
                .unlockedBy(getHasName(ModItems.PIERRE_OMBRE.get()), has(ModItems.PIERRE_OMBRE.get()))
                .save(sortie, RoyaumeDesIdees.id(getConversionRecipeName(ModItems.PIERRE_OMBRE_TAILLEE.get(), ModItems.PIERRE_OMBRE.get()) + "_tailleur"));
    }
}
