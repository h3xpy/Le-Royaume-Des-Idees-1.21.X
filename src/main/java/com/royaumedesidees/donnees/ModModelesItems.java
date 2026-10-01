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
        basicItem(ModItems.TOLLE_LEGE.get());
        basicItem(ModItems.LANTERNE_DIOGENE.get());
    }
}
