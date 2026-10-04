package com.royaumedesidees.donnees;

import com.royaumedesidees.RoyaumeDesIdees;
import com.royaumedesidees.registre.ModItems;
import com.royaumedesidees.registre.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class ModTagsItems extends ItemTagsProvider {
    public ModTagsItems(PackOutput sortie, CompletableFuture<HolderLookup.Provider> registres,
                        CompletableFuture<TagsProvider.TagLookup<Block>> tagsBlocs, ExistingFileHelper fichiers) {
        super(sortie, registres, tagsBlocs, RoyaumeDesIdees.MODID, fichiers);
    }

    @Override
    protected void addTags(HolderLookup.Provider registres) {
        tag(ModTags.LIE_AU_ROYAUME).add(ModItems.LANTERNE_DIOGENE.get());
        // Exceptions à « Royaume seulement » : leur effet marche aussi dans l'Overworld.
        tag(ModTags.ACTIF_HORS_ROYAUME).add(ModItems.PUPITRE_AMBROISE.get());
        copy(BlockTags.LEAVES, ItemTags.LEAVES);
        copy(BlockTags.LOGS_THAT_BURN, ItemTags.LOGS_THAT_BURN);
        copy(BlockTags.PLANKS, ItemTags.PLANKS);
    }
}
