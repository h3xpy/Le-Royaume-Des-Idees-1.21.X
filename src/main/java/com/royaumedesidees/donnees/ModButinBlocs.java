package com.royaumedesidees.donnees;

import com.royaumedesidees.registre.ModBlocs;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.Set;

/** Ce que lâchent les blocs. Le portail et les chaînes n'ont pas de table : ils ne lâchent rien. */
public class ModButinBlocs extends net.minecraft.data.loot.BlockLootSubProvider {
    public ModButinBlocs(HolderLookup.Provider registres) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registres);
    }

    @Override
    protected void generate() {
        dropSelf(ModBlocs.PIERRE_OMBRE.get());
        dropSelf(ModBlocs.PIERRE_OMBRE_TAILLEE.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocs.BLOCS.getEntries().stream().<Block>map(DeferredHolder::get).toList();
    }
}
