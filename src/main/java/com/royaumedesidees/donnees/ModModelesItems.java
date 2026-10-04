package com.royaumedesidees.donnees;

import com.royaumedesidees.RoyaumeDesIdees;
import com.royaumedesidees.registre.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

/** Modèles des items « plats ». Les items de blocs sont faits par {@link ModEtatsBlocs}. */
public class ModModelesItems extends ItemModelProvider {
    public ModModelesItems(PackOutput sortie, ExistingFileHelper fichiers) {
        super(sortie, RoyaumeDesIdees.MODID, fichiers);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.LIVRE_CONFESSIONS.get());
        basicItem(ModItems.SCEAU_CONVERSION.get());
        basicItem(ModItems.TOLLE_LEGE.get());
        basicItem(ModItems.LANTERNE_DIOGENE.get());
        basicItem(ModItems.POIRE.get());
        basicItem(ModItems.POIRE_VOLEE.get());
        // Bloc de deux de haut : une icône plate en inventaire, comme les portes.
        basicItem(ModItems.CONFESSIONNAL.get());
    }
}
