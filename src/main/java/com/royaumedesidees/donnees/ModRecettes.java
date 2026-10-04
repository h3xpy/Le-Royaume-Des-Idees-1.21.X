package com.royaumedesidees.donnees;

import com.royaumedesidees.RoyaumeDesIdees;
import com.royaumedesidees.registre.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.concurrent.CompletableFuture;

public class ModRecettes extends RecipeProvider {
    public ModRecettes(PackOutput sortie, CompletableFuture<HolderLookup.Provider> registres) {
        super(sortie, registres);
    }

    @Override
    protected void buildRecipes(RecipeOutput sortie) {
        // Bois de figuier : 1 bûche donne 4 planches, comme les bois vanilla.
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ModItems.PLANCHES_FIGUIER.get(), 4)
                .requires(ModItems.BOIS_FIGUIER.get())
                .unlockedBy(getHasName(ModItems.BOIS_FIGUIER.get()), has(ModItems.BOIS_FIGUIER.get()))
                .save(sortie);

        // Confessionnal : 6 planches, une cloche pour l'absolution, un panneau pour la grille.
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.CONFESSIONNAL.get())
                .pattern("PSP")
                .pattern("PBP")
                .pattern("P P")
                .define('P', ItemTags.PLANKS)
                .define('S', ItemTags.SIGNS)
                .define('B', Items.BELL)
                .unlockedBy(getHasName(Items.BELL), has(Items.BELL))
                .save(sortie);

        // Pupitre d'Ambroise : un lutrin, un livre, et de la Pierre d'Ombre (lire en silence, c'est lire dans l'ombre).
        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ModItems.PUPITRE_AMBROISE.get())
                .requires(Items.LECTERN)
                .requires(Items.BOOK)
                .requires(ModItems.PIERRE_OMBRE.get())
                .unlockedBy(getHasName(ModItems.PIERRE_OMBRE.get()), has(ModItems.PIERRE_OMBRE.get()))
                .save(sortie);

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
