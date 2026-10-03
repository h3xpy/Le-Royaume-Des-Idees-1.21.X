package com.royaumedesidees.donnees;

import com.royaumedesidees.RoyaumeDesIdees;
import com.royaumedesidees.bloc.ConfessionnalBloc;
import com.royaumedesidees.bloc.FeuillesPoirierBloc;
import com.royaumedesidees.bloc.PortailRoyaumeBloc;
import com.royaumedesidees.registre.ModBlocs;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
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

        jardin();
    }

    /** Blocs du Jardin de Milan (v0.2). Les feuilles ont leurs propres couleurs : pas de teinte de biome. */
    private void jardin() {
        ModelFile poirier = models().cubeAll("feuilles_poirier", modLoc("block/feuilles_poirier")).renderType("cutout_mipped");
        ModelFile poirierPoires = models().cubeAll("feuilles_poirier_poires", modLoc("block/feuilles_poirier_poires")).renderType("cutout_mipped");
        getVariantBuilder(ModBlocs.FEUILLES_POIRIER.get()).forAllStates(etat -> ConfiguredModel.builder()
                .modelFile(etat.getValue(FeuillesPoirierBloc.POIRES) ? poirierPoires : poirier).build());
        simpleBlockItem(ModBlocs.FEUILLES_POIRIER.get(), poirierPoires);

        logBlock(ModBlocs.BOIS_FIGUIER.get());
        simpleBlockItem(ModBlocs.BOIS_FIGUIER.get(), models().getExistingFile(modLoc("block/bois_figuier")));
        simpleBlockWithItem(ModBlocs.PLANCHES_FIGUIER.get(), cubeAll(ModBlocs.PLANCHES_FIGUIER.get()));
        simpleBlockWithItem(ModBlocs.FEUILLES_FIGUIER.get(),
                models().cubeAll("feuilles_figuier", modLoc("block/feuilles_figuier")).renderType("cutout_mipped"));

        // Confessionnal : deux moitiés, la grille sur la face avant, tournée selon l'orientation.
        ModelFile bas = models().orientable("confessionnal_bas", modLoc("block/confessionnal_cote"),
                modLoc("block/confessionnal_bas_face"), modLoc("block/confessionnal_dessus"));
        ModelFile haut = models().orientable("confessionnal_haut", modLoc("block/confessionnal_cote"),
                modLoc("block/confessionnal_haut_face"), modLoc("block/confessionnal_dessus"));
        getVariantBuilder(ModBlocs.CONFESSIONNAL.get()).forAllStates(etat -> ConfiguredModel.builder()
                .modelFile(etat.getValue(ConfessionnalBloc.MOITIE) == DoubleBlockHalf.UPPER ? haut : bas)
                .rotationY(((int) etat.getValue(ConfessionnalBloc.FACING).toYRot() + 180) % 360)
                .build());

        ModelFile etal = models().orientable("etal_verger", modLoc("block/etal_verger_cote"),
                modLoc("block/etal_verger_face"), modLoc("block/etal_verger_dessus"));
        horizontalBlock(ModBlocs.ETAL_VERGER.get(), etal);
        simpleBlockItem(ModBlocs.ETAL_VERGER.get(), etal);
    }
}
