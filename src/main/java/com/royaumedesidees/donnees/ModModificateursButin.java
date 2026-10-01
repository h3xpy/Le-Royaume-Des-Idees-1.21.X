package com.royaumedesidees.donnees;

import com.royaumedesidees.RoyaumeDesIdees;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.AddTableLootModifier;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/** Ajoute le livre Tolle, Lege aux coffres des villages de l'Overworld. */
public class ModModificateursButin extends GlobalLootModifierProvider {
    private static final List<ResourceKey<LootTable>> COFFRES_DE_VILLAGE = List.of(
            BuiltInLootTables.VILLAGE_WEAPONSMITH, BuiltInLootTables.VILLAGE_TOOLSMITH, BuiltInLootTables.VILLAGE_ARMORER,
            BuiltInLootTables.VILLAGE_CARTOGRAPHER, BuiltInLootTables.VILLAGE_MASON, BuiltInLootTables.VILLAGE_SHEPHERD,
            BuiltInLootTables.VILLAGE_BUTCHER, BuiltInLootTables.VILLAGE_FLETCHER, BuiltInLootTables.VILLAGE_FISHER,
            BuiltInLootTables.VILLAGE_TANNERY, BuiltInLootTables.VILLAGE_TEMPLE, BuiltInLootTables.VILLAGE_DESERT_HOUSE,
            BuiltInLootTables.VILLAGE_PLAINS_HOUSE, BuiltInLootTables.VILLAGE_TAIGA_HOUSE, BuiltInLootTables.VILLAGE_SNOWY_HOUSE,
            BuiltInLootTables.VILLAGE_SAVANNA_HOUSE);

    public ModModificateursButin(PackOutput sortie, CompletableFuture<HolderLookup.Provider> registres) {
        super(sortie, registres, RoyaumeDesIdees.MODID);
    }

    @Override
    protected void start() {
        LootItemCondition.Builder[] coffres = COFFRES_DE_VILLAGE.stream()
                .map(table -> LootTableIdCondition.builder(table.location()))
                .toArray(LootItemCondition.Builder[]::new);
        add("tolle_lege_villages", new AddTableLootModifier(
                new LootItemCondition[]{AnyOfCondition.anyOf(coffres).build()}, ModButinCoffres.TOLLE_LEGE_VILLAGES));
    }
}
