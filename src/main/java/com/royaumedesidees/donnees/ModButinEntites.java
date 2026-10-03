package com.royaumedesidees.donnees;

import com.royaumedesidees.registre.ModEntites;
import com.royaumedesidees.registre.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.stream.Stream;

/** Butin des entités du mod. */
public class ModButinEntites extends EntityLootSubProvider {
    public ModButinEntites(HolderLookup.Provider registres) {
        super(FeatureFlags.REGISTRY.allFlags(), registres);
    }

    @Override
    public void generate() {
        // Une Ombre tuée à la Lanterne laisse 1 ou 2 Pierres d'Ombre (une de plus au maximum avec Butin).
        add(ModEntites.OMBRE.get(), LootTable.lootTable().withPool(LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(ModItems.PIERRE_OMBRE.get())
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                        .apply(EnchantedCountIncreaseFunction.lootingMultiplier(registries, UniformGenerator.between(0, 1))))));
    }

    @Override
    protected Stream<EntityType<?>> getKnownEntityTypes() {
        return ModEntites.ENTITES.getEntries().stream().map(entree -> entree.get());
    }
}
