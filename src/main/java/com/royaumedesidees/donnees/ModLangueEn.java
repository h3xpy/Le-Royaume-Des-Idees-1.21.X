package com.royaumedesidees.donnees;

import com.royaumedesidees.RoyaumeDesIdees;
import com.royaumedesidees.registre.ModBlocs;
import com.royaumedesidees.registre.ModItems;
import com.royaumedesidees.registre.ModTags;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

/** Langue de secours. Mêmes clés que {@link ModLangueFr}. */
public class ModLangueEn extends LanguageProvider {
    public ModLangueEn(PackOutput sortie) {
        super(sortie, RoyaumeDesIdees.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add("itemGroup." + RoyaumeDesIdees.MODID, "The Kingdom of Ideas");

        add(ModItems.TOLLE_LEGE.get(), "Tolle, Lege");
        add(ModItems.TOLLE_LEGE.get().getDescriptionId() + ".citation", "\"Take up and read; take up and read.\" (Confessions, VIII)");
        add(ModItems.LANTERNE_DIOGENE.get(), "Diogenes' Lantern");

        add(ModBlocs.PIERRE_OMBRE.get(), "Shadow Stone");
        add(ModBlocs.PIERRE_OMBRE_TAILLEE.get(), "Chiseled Shadow Stone");
        add(ModBlocs.PORTAIL_ROYAUME.get(), "Kingdom Portal");
        add(ModBlocs.CHAINE_CAVERNE.get(), "Cave Chain");

        add("biome." + RoyaumeDesIdees.MODID + ".caverne_platon", "Plato's Cave");
        add("biome." + RoyaumeDesIdees.MODID + ".jardin_milan", "Garden of Milan");
        add("biome." + RoyaumeDesIdees.MODID + ".port_royal", "Port-Royal");
        add("biome." + RoyaumeDesIdees.MODID + ".puy_de_dome", "Puy de Dôme");
        add("biome." + RoyaumeDesIdees.MODID + ".hippone", "Hippo");

        add(ModTags.LIE_AU_ROYAUME, "Bound to the Kingdom");
    }
}
