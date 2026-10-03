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

        add("entity.royaumedesidees.ombre", "Shadow");
        add("message.royaumedesidees.ombre.intouchable", "It is only a shadow: your blow goes straight through.");
        add("message.royaumedesidees.caverne.enchaine", "You are chained facing the wall, like Plato's prisoners. Break your chains.");
        add("message.royaumedesidees.caverne.lumiere", "Your eyes, used to the shadows, burn in the daylight.");
        add("message.royaumedesidees.caverne.lanterne", "By the path, an old lantern was waiting for you. \"I am looking for a man,\" said Diogenes.");
        add("item.royaumedesidees.lanterne_diogene.effet", "When held: reveals Shadows within 8 blocks.");
        add("item.royaumedesidees.lanterne_diogene.citation", "\"I am looking for a man.\" (Diogenes of Sinope)");
        add("tooltip.royaumedesidees.souvenir", "Keepsake of the Kingdom");
        add("tooltip.royaumedesidees.souvenir.detail", "No effect outside the Kingdom.");
        add("advancements.royaumedesidees.racine.title", "The Kingdom of Ideas");
        add("advancements.royaumedesidees.racine.description", "Take up, read, and step through the bookshelf portal.");
        add("advancements.royaumedesidees.allegorie_vecue.title", "Allegory Lived");
        add("advancements.royaumedesidees.allegorie_vecue.description", "Leave Plato's Cave and finally see the light (and be dazzled by it).");

        add("message.royaumedesidees.ombre.statue", "It was only a wooden statue. Diogenes is still looking for a man.");
        add("message.royaumedesidees.caverne.tenebres", "Back in the Cave, your sun-filled eyes see nothing but darkness.");

        add("item.royaumedesidees.poire", "Pear");
        add("item.royaumedesidees.poire.citation", "Honestly bought. Not as good as the other kind: Augustine noticed it too.");
        add("item.royaumedesidees.poire_volee", "Stolen Pear");
        add("item.royaumedesidees.poire_volee.citation", "\"It was not the pear I loved, but the theft.\" (after the Confessions, II)");
        add("block.royaumedesidees.feuilles_poirier", "Pear Leaves");
        add("block.royaumedesidees.bois_figuier", "Fig Log");
        add("block.royaumedesidees.planches_figuier", "Fig Planks");
        add("block.royaumedesidees.feuilles_figuier", "Fig Leaves");
        add("block.royaumedesidees.confessionnal", "Confessional");
        add("block.royaumedesidees.etal_verger", "Orchard Stall");
        add("effect.royaumedesidees.culpabilite", "Guilt");
        add("message.royaumedesidees.culpabilite.vol_poire", "%1$s stole a pear. Shame.");
        add("message.royaumedesidees.culpabilite.ecrase", "%1$s is crushed by guilt.");
        add("message.royaumedesidees.confession.publique", "%1$s confesses: \"%2$s\"");
        add("message.royaumedesidees.confession.allege", "Your conscience feels lighter. Guilt left: %1$s.");
        add("message.royaumedesidees.confession.rien", "Augustine wrote thirteen books of them; you really have nothing?");
        add("message.royaumedesidees.confession.loin", "You need a Confessional to confess. There is one near the Altar, in the centre of the island.");
        add("message.royaumedesidees.confession.trop_tot", "Easy: wait %1$s more seconds before your next confession.");
        add("message.royaumedesidees.confession.trop_long", "Your confession is too long (%1$s characters at most). Augustine needed thirteen books.");
        add("message.royaumedesidees.confessionnal.mode_emploi", "To confess, type /confesse followed by your sin.");
        add("message.royaumedesidees.etal.achat", "You honestly buy %1$s pears. They look less tasty.");
        add("message.royaumedesidees.etal.ecriteau", "\"One emerald in the box, %1$s pears for you. Or you could just steal them...\"");
        add("panneau.royaumedesidees.verger.lucius", "Lucius' Orchard");
        add("panneau.royaumedesidees.verger.severe", "Severus' Orchard");
        add("panneau.royaumedesidees.verger.verecundus", "Verecundus' Orchard");
        add("panneau.royaumedesidees.verger.defense", "No");
        add("panneau.royaumedesidees.verger.voler", "stealing!");
        add("message.royaumedesidees.figuier.voix", "A child's voice from the house next door: \"Take up and read; take up and read.\"");
        add("sous_titre.royaumedesidees.sanglots", "Someone sobs");

        add(ModTags.LIE_AU_ROYAUME, "Bound to the Kingdom");
    }
}
