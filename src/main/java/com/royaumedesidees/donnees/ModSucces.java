package com.royaumedesidees.donnees;

import com.royaumedesidees.RoyaumeDesIdees;
import com.royaumedesidees.caverne.SortieCaverne;
import com.royaumedesidees.registre.ModItems;
import com.royaumedesidees.registre.ModMonde;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.ChangeDimensionTrigger;
import net.minecraft.advancements.critereon.ImpossibleTrigger;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.function.Consumer;

/** Succès du Royaume, dans leur propre onglet. */
public class ModSucces implements AdvancementProvider.AdvancementGenerator {
    @Override
    public void generate(HolderLookup.Provider registres, Consumer<AdvancementHolder> sortie, ExistingFileHelper fichiers) {
        AdvancementHolder racine = Advancement.Builder.advancement()
                .display(ModItems.TOLLE_LEGE.get(),
                        Component.translatable("advancements.royaumedesidees.racine.title"),
                        Component.translatable("advancements.royaumedesidees.racine.description"),
                        RoyaumeDesIdees.id("textures/block/pierre_ombre_taillee.png"),
                        AdvancementType.TASK, true, false, false)
                .addCriterion("entree", ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(ModMonde.ROYAUME))
                .save(sortie, RoyaumeDesIdees.id("royaume/racine").toString());

        // Accordé par le code (SortieCaverne), d'où le critère « impossible ».
        Advancement.Builder.advancement()
                .parent(racine)
                .display(ModItems.LANTERNE_DIOGENE.get(),
                        Component.translatable("advancements.royaumedesidees.allegorie_vecue.title"),
                        Component.translatable("advancements.royaumedesidees.allegorie_vecue.description"),
                        null, AdvancementType.GOAL, true, true, false)
                .addCriterion("sortie", CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance()))
                .save(sortie, SortieCaverne.SUCCES.toString());
    }
}
