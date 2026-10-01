package com.royaumedesidees.donnees;

import com.royaumedesidees.RoyaumeDesIdees;
import com.royaumedesidees.registre.ModBlocs;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class ModTagsBlocs extends BlockTagsProvider {
    public ModTagsBlocs(PackOutput sortie, CompletableFuture<HolderLookup.Provider> registres, ExistingFileHelper fichiers) {
        super(sortie, registres, RoyaumeDesIdees.MODID, fichiers);
    }

    @Override
    protected void addTags(HolderLookup.Provider registres) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocs.PIERRE_OMBRE.get(), ModBlocs.PIERRE_OMBRE_TAILLEE.get());
        // Le portail ne doit ni se casser ni être déplacé, comme celui du Nether.
        tag(BlockTags.PORTALS).add(ModBlocs.PORTAIL_ROYAUME.get());
        tag(BlockTags.DRAGON_IMMUNE).add(ModBlocs.PORTAIL_ROYAUME.get());
        tag(BlockTags.WITHER_IMMUNE).add(ModBlocs.PORTAIL_ROYAUME.get());
    }
}
