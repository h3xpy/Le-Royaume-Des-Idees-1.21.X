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

        add("commande." + RoyaumeDesIdees.MODID + ".structures.titre", "Structures du Royaume :");
        add("commande." + RoyaumeDesIdees.MODID + ".structures.ligne", "- %1$s : posée v%2$s, attendue v%3$s, vers %4$s (%5$s)");
        add("commande." + RoyaumeDesIdees.MODID + ".structures.jamais", "pas encore posée");
        add("commande." + RoyaumeDesIdees.MODID + ".structures.a_jour", "à jour");
        add("commande." + RoyaumeDesIdees.MODID + ".structures.a_reposer", "sera reposée quand un joueur passera à proximité");
        add("commande." + RoyaumeDesIdees.MODID + ".structures.reposee", "Structure %1$s reposée (version %2$s).");
        add("commande." + RoyaumeDesIdees.MODID + ".structures.inconnue", "Structure inconnue : %1$s");
        add("commande." + RoyaumeDesIdees.MODID + ".structures.sans_royaume", "La dimension du Royaume n'est pas chargée.");

        add("message.royaumedesidees.portail.incomplet", "Le livre reste muet : il faut un cadre de bibliothèques de 4 sur 5, vide au milieu, avec une lanterne contre chaque coin.");
        add("message.royaumedesidees.portail.allume", "« Prends, lis ; prends, lis. » Le portail s'ouvre.");
        add("message.royaumedesidees.portail.dans_royaume", "Ici, on ne lit plus : on cherche la sortie.");
        add("sous_titre.royaumedesidees.portail_chant", "Une voix d'enfant chante");

        add(ModTags.LIE_AU_ROYAUME, "Lié au Royaume");
    }
}
