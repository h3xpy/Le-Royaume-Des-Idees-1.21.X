package com.royaumedesidees.donnees;

import com.royaumedesidees.RoyaumeDesIdees;
import com.royaumedesidees.bloc.PortailRoyaumeBloc;
import com.royaumedesidees.registre.ModBlocs;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModEtatsBlocs extends BlockStateProvider {
    public ModEtatsBlocs(PackOutput sortie, ExistingFileHelper fichiers) {
        super(sortie, RoyaumeDesIdees.MODID, fichiers);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlockWithItem(ModBlocs.PIERRE_OMBRE.get(), cubeAll(ModBlocs.PIERRE_OMBRE.get()));
        simpleBlockWithItem(ModBlocs.PIERRE_OMBRE_TAILLEE.get(), cubeAll(ModBlocs.PIERRE_OMBRE_TAILLEE.get()));

        // Les chaînes laissent voir à travers leurs maillons.
        simpleBlockWithItem(ModBlocs.CHAINE_CAVERNE.get(),
                models().cubeAll("chaine_caverne", blockTexture(ModBlocs.CHAINE_CAVERNE.get())).renderType("cutout"));

        // Le portail reprend la forme du portail du Nether, avec notre texture animée.
        ResourceLocation texture = blockTexture(ModBlocs.PORTAIL_ROYAUME.get());
        ModelFile nordSud = models().withExistingParent("portail_royaume_ns", mcLoc("block/nether_portal_ns"))
                .texture("particle", texture).texture("portal", texture).renderType("translucent");
        ModelFile estOuest = models().withExistingParent("portail_royaume_ew", mcLoc("block/nether_portal_ew"))
                .texture("particle", texture).texture("portal", texture).renderType("translucent");
        getVariantBuilder(ModBlocs.PORTAIL_ROYAUME.get()).forAllStates(etat -> ConfiguredModel.builder()
                .modelFile(etat.getValue(PortailRoyaumeBloc.AXE) == Direction.Axis.Z ? estOuest : nordSud)
                .build());
    }
}
