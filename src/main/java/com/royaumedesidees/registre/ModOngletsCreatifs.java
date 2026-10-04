package com.royaumedesidees.registre;

import com.royaumedesidees.RoyaumeDesIdees;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * L'onglet créatif du mod. Un objet abandonné en est retiré, mais reste enregistré.
 */
public final class ModOngletsCreatifs {
    public static final DeferredRegister<CreativeModeTab> ONGLETS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, RoyaumeDesIdees.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ROYAUME = ONGLETS.register("royaume", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + RoyaumeDesIdees.MODID))
            .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
            .icon(() -> ModItems.TOLLE_LEGE.get().getDefaultInstance())
            .displayItems((parametres, sortie) -> {
                sortie.accept(ModItems.TOLLE_LEGE.get());
                sortie.accept(ModItems.LANTERNE_DIOGENE.get());
                sortie.accept(ModItems.PIERRE_OMBRE.get());
                sortie.accept(ModItems.PIERRE_OMBRE_TAILLEE.get());
                sortie.accept(ModItems.CHAINE_CAVERNE.get());
                sortie.accept(ModItems.POIRE_VOLEE.get());
                sortie.accept(ModItems.POIRE.get());
                sortie.accept(ModItems.FEUILLES_POIRIER.get());
                sortie.accept(ModItems.BOIS_FIGUIER.get());
                sortie.accept(ModItems.PLANCHES_FIGUIER.get());
                sortie.accept(ModItems.FEUILLES_FIGUIER.get());
                sortie.accept(ModItems.CONFESSIONNAL.get());
                sortie.accept(ModItems.ETAL_VERGER.get());
                sortie.accept(ModItems.PUPITRE_AMBROISE.get());
            })
            .build());

    private ModOngletsCreatifs() {
    }
}
