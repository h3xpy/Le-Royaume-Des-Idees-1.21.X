package com.royaumedesidees.donnees;

import com.royaumedesidees.RoyaumeDesIdees;
import com.royaumedesidees.registre.ModBlocs;
import com.royaumedesidees.registre.ModItems;
import com.royaumedesidees.registre.ModTags;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

/** Langue principale du mod. Toute clé ajoutée ici doit aussi l'être dans {@link ModLangueEn}. */
public class ModLangueFr extends LanguageProvider {
    public ModLangueFr(PackOutput sortie) {
        super(sortie, RoyaumeDesIdees.MODID, "fr_fr");
    }

    @Override
    protected void addTranslations() {
        add("itemGroup." + RoyaumeDesIdees.MODID, "Le Royaume des Idées");

        add(ModItems.TOLLE_LEGE.get(), "Tolle, Lege");
        add(ModItems.TOLLE_LEGE.get().getDescriptionId() + ".citation", "« Prends, lis ; prends, lis. » (Confessions, VIII)");
        add(ModItems.LANTERNE_DIOGENE.get(), "Lanterne de Diogène");

        add(ModBlocs.PIERRE_OMBRE.get(), "Pierre d'Ombre");
        add(ModBlocs.PIERRE_OMBRE_TAILLEE.get(), "Pierre d'Ombre taillée");
        add(ModBlocs.PORTAIL_ROYAUME.get(), "Portail du Royaume");
        add(ModBlocs.CHAINE_CAVERNE.get(), "Chaîne de la Caverne");

        add("biome." + RoyaumeDesIdees.MODID + ".caverne_platon", "Caverne de Platon");
        add("biome." + RoyaumeDesIdees.MODID + ".jardin_milan", "Jardin de Milan");
        add("biome." + RoyaumeDesIdees.MODID + ".port_royal", "Port-Royal");
        add("biome." + RoyaumeDesIdees.MODID + ".puy_de_dome", "Puy de Dôme");
        add("biome." + RoyaumeDesIdees.MODID + ".hippone", "Hippone");

        add(ModTags.LIE_AU_ROYAUME, "Lié au Royaume");
    }
}
