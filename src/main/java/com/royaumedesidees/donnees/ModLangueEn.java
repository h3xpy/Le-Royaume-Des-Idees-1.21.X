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
        add("message.royaumedesidees.confession.repetee", "*Slap!* You already confessed that. Augustine wrote thirteen books without repeating himself once: find another sin, or go and think it over.");
        add("message.royaumedesidees.confession.hors_royaume", "Here your conscience is silent: Guilt and confession only exist in the Kingdom.");
        // v0.3 : Grâce et Voies
        add("message.royaumedesidees.grace.gain", "+%1$s Grace (%2$s)");
        add("message.royaumedesidees.grace.don", "Grace is a gift, not a wage.");
        add("message.royaumedesidees.grace.rien", "No Grace this time (%1$s). It cannot be earned.");
        add("message.royaumedesidees.grace.raison.confession", "confession");
        add("message.royaumedesidees.grace.raison.frappe", "struck without striking back");
        add("message.royaumedesidees.grace.raison.commande", "command");
        add("commande.royaumedesidees.grace.valeur", "%1$s: %2$s Grace, %3$s.");
        add("voie.royaumedesidees.aucune", "no Way");
        add("voie.royaumedesidees.raison", "Way of Reason");
        add("voie.royaumedesidees.coeur", "Way of the Heart");
        // v0.3 : PNJ
        add("entity.royaumedesidees.augustin_jeune", "Augustine");
        add("entity.royaumedesidees.adeodat", "Adeodatus");
        add("entity.royaumedesidees.ambroise", "Ambrose");
        add("entity.royaumedesidees.monique", "Monica");
        add("entity.royaumedesidees.pascal", "Blaise Pascal");
        add("pnj.royaumedesidees.augustin_jeune.bonjour", "Hey! You look far too well-behaved. There's a cure for that: pears, over there.");
        add("pnj.royaumedesidees.augustin_jeune.coup", "*bursts out laughing* You hit the way you steal: half-heartedly!");
        add("pnj.royaumedesidees.adeodat.bonjour", "Hello. My father says I was born of his sin. I prefer mathematics.");
        add("pnj.royaumedesidees.adeodat.coup", "*sighs* Hitting a genius does not make you smarter. I checked.");
        add("pnj.royaumedesidees.ambroise.bonjour", "*Ambrose looks up from his book, then down again, without a word.*");
        add("pnj.royaumedesidees.ambroise.coup", "*Ambrose turns a page, without a word.*");
        add("pnj.royaumedesidees.monique.bonjour", "*Monica wipes her tears.* My son… Have you seen my son?");
        add("pnj.royaumedesidees.monique.coup", "*Monica weeps louder.*");
        add("pnj.royaumedesidees.pascal.bonjour", "The heart has its reasons which reason does not know. My father's accounts, however, have none.");
        add("pnj.royaumedesidees.pascal.coup", "Thank you. *He tightens his belt.*");
        add("message.royaumedesidees.pascal.merci", "Pascal thanks %1$s for hitting him.");
        add("sous_titre.royaumedesidees.augustin_rire", "Augustine laughs");
        add("sous_titre.royaumedesidees.ambroise_page", "A page turns");
        add("sous_titre.royaumedesidees.pascal_merci", "Pascal says thank you");
        add("message.royaumedesidees.confessionnal.mode_emploi", "To confess, type /confesse followed by your sin.");
        add("message.royaumedesidees.etal.achat", "You honestly buy %1$s pears. They look less tasty.");
        add("message.royaumedesidees.etal.ecriteau", "\"One emerald in the box, %1$s pears for you. Or you could just steal them...\"");
        add("panneau.royaumedesidees.verger.lucius", "Lucius' Orchard");
        add("panneau.royaumedesidees.verger.severe", "Severus' Orchard");
        add("panneau.royaumedesidees.verger.verecundus", "Verecundus' Orchard");
        add("panneau.royaumedesidees.vigne.1", "Vineyard of");
        add("panneau.royaumedesidees.vigne.2", "Patricius");
        add("panneau.royaumedesidees.porcherie.1", "Pigsty");
        add("panneau.royaumedesidees.porcherie.2", "This is where");
        add("panneau.royaumedesidees.porcherie.3", "stolen pears");
        add("panneau.royaumedesidees.porcherie.4", "end up.");
        add("panneau.royaumedesidees.direction.vergers.1", "To the orchards");
        add("panneau.royaumedesidees.direction.vergers.2", "and vineyard");
        add("panneau.royaumedesidees.direction.figuier.1", "To the garden");
        add("panneau.royaumedesidees.direction.figuier.2", "fig tree");
        add("panneau.royaumedesidees.confessionnal.1", "Inquietum est");
        add("panneau.royaumedesidees.confessionnal.2", "cor nostrum,");
        add("panneau.royaumedesidees.confessionnal.3", "donec requiescat");
        add("panneau.royaumedesidees.confessionnal.4", "in te.");
        add("livre.royaumedesidees.platoniciens.page1", "Books of the Platonists\n\nTranslated from Greek into Latin by Marius Victorinus.\n\nAugustine read them in Milan, in 386, in this very house.");
        add("livre.royaumedesidees.platoniciens.page2", "He found in them that in the beginning was the Word, and the Word was God.\n\nHe did not find that the Word was made flesh.\n\n(Confessions, VII, 9)");
        add("livre.royaumedesidees.platoniciens.page3", "Remember the Cave: what you took for the world was only its shadow.\n\nPlato shows the light; he does not tell the way.");
        add("livre.royaumedesidees.romains.page1", "Epistle to the Romans, XIII, 13-14\n\nLet us walk honestly, as in the day; not in rioting and drunkenness, not in chambering and wantonness,");
        add("livre.royaumedesidees.romains.page2", "not in strife and envying. But put ye on the Lord Jesus Christ, and make not provision for the flesh, to fulfil the lusts thereof.");
        add("livre.royaumedesidees.romains.page3", "Augustine read this passage in silence, here, on this bench.\n\n\"As if a light of peace had been poured into my heart, all the darkness of doubt fled away.\"\n\n(Confessions, VIII, 12)");
        add("panneau.royaumedesidees.verger.defense", "No");
        add("panneau.royaumedesidees.verger.voler", "stealing!");
        add("message.royaumedesidees.figuier.voix", "A child's voice from the house next door: \"Take up and read; take up and read.\"");
        add("sous_titre.royaumedesidees.sanglots", "Someone sobs");

        add(ModTags.LIE_AU_ROYAUME, "Bound to the Kingdom");
    }
}
