package com.royaumedesidees.donnees;

import com.royaumedesidees.RoyaumeDesIdees;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * Lancé par {@code ./gradlew runData} : écrit modèles, blockstates, langues, tags, recettes
 * et loot tables dans {@code src/generated/resources}.
 */
public final class GenerateurDonnees {
    public static void generer(GatherDataEvent evenement) {
        DataGenerator generateur = evenement.getGenerator();
        PackOutput sortie = generateur.getPackOutput();
        ExistingFileHelper fichiers = evenement.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> registres = evenement.getLookupProvider();

        boolean client = evenement.includeClient();
        generateur.addProvider(client, new ModEtatsBlocs(sortie, fichiers));
        generateur.addProvider(client, new ModModelesItems(sortie, fichiers));
        generateur.addProvider(client, new ModLangueFr(sortie));
        generateur.addProvider(client, new ModLangueEn(sortie));

        boolean serveur = evenement.includeServer();
        // Dimension du Royaume. Les tags et recettes suivants voient ainsi aussi ses entrées.
        DatapackBuiltinEntriesProvider dimension = generateur.addProvider(serveur,
                new DatapackBuiltinEntriesProvider(sortie, registres, ModDimension.registres(), Set.of(RoyaumeDesIdees.MODID)));
        registres = dimension.getRegistryProvider();
        ModTagsBlocs tagsBlocs = generateur.addProvider(serveur, new ModTagsBlocs(sortie, registres, fichiers));
        generateur.addProvider(serveur, new ModTagsItems(sortie, registres, tagsBlocs.contentsGetter(), fichiers));
        generateur.addProvider(serveur, new ModRecettes(sortie, registres));
        generateur.addProvider(serveur, new LootTableProvider(sortie, Set.of(),
                List.of(new LootTableProvider.SubProviderEntry(ModButinBlocs::new, LootContextParamSets.BLOCK)), registres));
    }

    private GenerateurDonnees() {
    }
}
