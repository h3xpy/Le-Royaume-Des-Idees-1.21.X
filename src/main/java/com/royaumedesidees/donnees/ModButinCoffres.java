package com.royaumedesidees.donnees;

import com.royaumedesidees.RoyaumeDesIdees;
import com.royaumedesidees.registre.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.function.BiConsumer;

/** Tables de butin ajoutées aux coffres vanilla par {@link ModModificateursButin}. */
public class ModButinCoffres implements LootTableSubProvider {
    /** Un Tolle, Lege dans environ 15 % des coffres de village. */
    public static final ResourceKey<LootTable> TOLLE_LEGE_VILLAGES =
            ResourceKey.create(Registries.LOOT_TABLE, RoyaumeDesIdees.id("injection/tolle_lege_villages"));

    public ModButinCoffres(HolderLookup.Provider registres) {
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> sortie) {
        sortie.accept(TOLLE_LEGE_VILLAGES, LootTable.lootTable().withPool(LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .when(LootItemRandomChanceCondition.randomChance(0.15F))
                .add(LootItem.lootTableItem(ModItems.TOLLE_LEGE.get()))));
    }
}
