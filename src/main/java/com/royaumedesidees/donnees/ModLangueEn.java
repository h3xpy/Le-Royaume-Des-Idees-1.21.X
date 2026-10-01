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

        add("commande." + RoyaumeDesIdees.MODID + ".structures.titre", "Kingdom structures:");
        add("commande." + RoyaumeDesIdees.MODID + ".structures.ligne", "- %1$s: placed v%2$s, expected v%3$s, near %4$s (%5$s)");
        add("commande." + RoyaumeDesIdees.MODID + ".structures.jamais", "not placed yet");
        add("commande." + RoyaumeDesIdees.MODID + ".structures.a_jour", "up to date");
        add("commande." + RoyaumeDesIdees.MODID + ".structures.a_reposer", "will be placed again when a player comes near");
        add("commande." + RoyaumeDesIdees.MODID + ".structures.reposee", "Structure %1$s placed again (version %2$s).");
        add("commande." + RoyaumeDesIdees.MODID + ".structures.inconnue", "Unknown structure: %1$s");
        add("commande." + RoyaumeDesIdees.MODID + ".structures.sans_royaume", "The Kingdom dimension is not loaded.");

        add("message.royaumedesidees.portail.incomplet", "The book stays silent: you need a 4 by 5 bookshelf frame, empty inside, with a lantern against each corner.");
        add("message.royaumedesidees.portail.allume", "\"Take up and read; take up and read.\" The portal opens.");
        add("message.royaumedesidees.portail.dans_royaume", "No more reading here: look for the way out.");
        add("sous_titre.royaumedesidees.portail_chant", "A child's voice sings");

        add(ModTags.LIE_AU_ROYAUME, "Bound to the Kingdom");
    }
}
