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
        add("pnj.royaumedesidees.monique.coup", "*Monica weeps louder.*");
        add("message.royaumedesidees.monique.arrive", "§dSaint Monica§r hurries to you, in tears: you are the greatest sinner in the Kingdom.");
        add("message.royaumedesidees.monique.revient", "§dMonica§r comes back to pray for you: no one here has a heavier soul.");
        add("message.royaumedesidees.monique.quitte", "§dMonica§r leaves you for %1$s, who needs her more than you.");
        add("message.royaumedesidees.monique.rentre", "§dMonica§r dries her tears and goes back to pray in the villa garden.");
        add("pnj.royaumedesidees.monique.pas_mon_fils", "It is not for you that I weep today. But I will pray for you too.");
        add("pnj.royaumedesidees.monique.priere", "*Monica prays in silence.* As long as no one sins, I can pray in peace.");
        add("panneau.royaumedesidees.direction.bibliotheque.1", "To Ambrose's");
        add("panneau.royaumedesidees.direction.bibliotheque.2", "library");
        add("pnj.royaumedesidees.monique.bonjour", "*Monica weeps.* So many tears… The son of so many tears cannot perish.");
        add("pnj.royaumedesidees.monique.apaisee", "*Monica smiles, at peace.* My prayers are answered. I can go in peace.");
        add("pnj.royaumedesidees.monique.nourrie", "*Monica eats in silence, then blesses you.*");
        add("pnj.royaumedesidees.monique.repue", "Thank you, but I have already eaten today. Keep it for the poor.");
        add("message.royaumedesidees.grace.raison.monique", "Monica fed");
        add("pnj.royaumedesidees.pascal.bonjour", "The heart has its reasons which reason does not know. My father's accounts, however, have none.");
        add("pnj.royaumedesidees.pascal.coup", "Thank you. *He tightens his belt.*");
        add("message.royaumedesidees.pascal.merci", "Pascal thanks %1$s for hitting him.");
        // v0.3 : Bibliothèque d'Ambroise, cellule de Pascal
        add("block.royaumedesidees.pupitre_ambroise", "Ambrose's Lectern");
        add("tooltip.royaumedesidees.actif_partout", "Also works outside the Kingdom");
        // v0.3 : quête de conversion, Adéodat, récompenses
        add("item.royaumedesidees.livre_confessions", "Book of Confessions");
        add("item.royaumedesidees.livre_confessions.citation", "\"You have made us for yourself, and our heart is restless until it rests in you.\" Right-click: heals nearby players.");
        add("item.royaumedesidees.sceau_conversion", "Seal of Conversion");
        add("item.royaumedesidees.sceau_conversion.citation", "First of the four Seals. The Altar of the City of God awaits it.");
        add("message.royaumedesidees.livre_confessions.souvenir", "Outside the Kingdom, the book is silent.");
        add("message.royaumedesidees.livre_confessions.muet", "This book speaks only to converted hearts.");
        add("message.royaumedesidees.livre_confessions.lu", "You read a page of the Confessions: %1$s soul(s) soothed.");
        add("message.royaumedesidees.quete.poires", "Conversion quest, 1/4: \"The pears of Thagaste\". Steal 3 pears with Augustine nearby.");
        add("message.royaumedesidees.quete.silence", "Conversion quest, 2/4: \"Ambrose's silence\". Stay 60 seconds near his lectern, at the library, without saying a word.");
        add("message.royaumedesidees.quete.silence_compte", "Silence… %1$s s left.");
        add("message.royaumedesidees.quete.silence_rompu", "You tried to speak: the silence starts over.");
        add("message.royaumedesidees.quete.figuier", "Ambrose finally looks up and points to the fig tree garden. Quest 3/4: \"Take up and read\". Weep under the fig tree (crouch 5 seconds beneath it).");
        add("message.royaumedesidees.quete.romains", "Go back to Alypius's bench, where you left the Apostle's book, and read it.");
        add("message.royaumedesidees.quete.bapteme", "\"As if a light of peace had been poured into my heart…\" Quest 4/4: \"The baptism\". Go and find Ambrose.");
        add("message.royaumedesidees.bapteme.annonce", "Ambrose baptises %1$s, as he did Augustine at Easter 387. The Kingdom has one more convert.");
        add("message.royaumedesidees.voie.annonce", "%1$s has chosen the %2$s.");
        add("message.royaumedesidees.grace.raison.adeodat", "Adeodatus's challenge");
        add("message.royaumedesidees.grace.raison.quete", "quest");
        add("message.royaumedesidees.adeodat.humiliation", "Adeodatus, aged 15, calculated faster than %1$s.");
        add("message.royaumedesidees.monique.consolee", "§dMonica§r weeps with joy: \"The son of so many tears cannot perish.\" She goes back to the garden, at peace.");
        add("pnj.royaumedesidees.augustin_jeune.quete.debut", "You look like a pear thief. Come on, let's take %1$s: not because we're hungry, just for the pleasure of it.");
        add("pnj.royaumedesidees.augustin_jeune.quete.poires", "%1$s more pear(s)! See Lucius's orchard? Nobody's watching.");
        add("pnj.royaumedesidees.augustin_jeune.quete.porcs", "Ha! We won't even eat them: we'll throw them to the pigs. Well… They say at the library a bishop reads without moving his lips. Go and see, I don't believe it.");
        add("pnj.royaumedesidees.augustin_jeune.quete.silence", "So, that bishop who reads without speaking? Go to the library, and be quiet for once.");
        add("pnj.royaumedesidees.augustin_jeune.quete.figuier", "The fig tree in the garden… I never go there. People cry too much there.");
        add("pnj.royaumedesidees.augustin_jeune.quete.bapteme", "You look different. Go and see Ambrose, before I change your mind.");
        add("pnj.royaumedesidees.augustin_jeune.quete.finie", "You, converted? Me too, one day. \"Grant me chastity, but not yet.\"");
        add("pnj.royaumedesidees.augustin_jeune.vol", "*runs up laughing* Well done! %1$s more and I'll tell you a secret.");
        add("pnj.royaumedesidees.augustin_jeune.vol_encore", "*runs up laughing* Another one? You have good taste… in theft.");
        add("pnj.royaumedesidees.ambroise.bapteme", "*Ambrose puts down his book, pours water on your head, and finally speaks.* I baptise you.");
        add("pnj.royaumedesidees.ambroise.silence", "*Ambrose puts a finger to his lips, and goes on reading.*");
        add("pnj.royaumedesidees.ambroise.pas_assez_de_grace", "*Ambrose shakes his head: changing your Way costs %1$s Grace.*");
        add("pnj.royaumedesidees.adeodat.defi", "What is %1$s? You have %2$s seconds. Answer in the chat.");
        add("pnj.royaumedesidees.adeodat.reussi", "*nods* Not bad, for a grown-up.");
        add("pnj.royaumedesidees.adeodat.rate", "It was %1$s. I had it before you finished reading the question.");
        add("pnj.royaumedesidees.adeodat.repos", "One challenge at a time. Come back in %1$s seconds, even geniuses breathe.");
        add("pnj.royaumedesidees.adeodat.attends", "I'm waiting for your answer. In the chat.");
        add("message.royaumedesidees.silence.chut", "Hush. Ambrose is reading.");
        add("panneau.royaumedesidees.bibliotheque.1", "Silentium.");
        add("panneau.royaumedesidees.bibliotheque.2", "Here Ambrose");
        add("panneau.royaumedesidees.bibliotheque.3", "reads in silence.");
        add("panneau.royaumedesidees.port_royal.1", "Port-Royal");
        add("panneau.royaumedesidees.port_royal.2", "Abbey under");
        add("panneau.royaumedesidees.port_royal.3", "construction");
        add("livre.royaumedesidees.hexaemeron.page1", "Hexaemeron\n\nThe six days of Creation, preached by Ambrose in Milan.\n\nAugustine came to hear him, first for his fine words, then for what he said.");
        add("livre.royaumedesidees.hexaemeron.page2", "\"When he read, his eyes ran over the pages and his heart sought out the meaning, but his voice and tongue were at rest.\"\n\n(Confessions, VI, 3)");
        add("livre.royaumedesidees.comptes.page1", "Election of Rouen\nTaille of 1641\n\n   3 livres 7 sols 4 deniers\n+ 12 livres 15 sols 9 deniers\n+  2 livres 19 sols 11 deniers\n= ?\n\n20 sols make a livre,\n12 deniers make a sol.");
        add("livre.royaumedesidees.comptes.page2", "My father spends his nights on these.\n\nOne would need a machine to do such sums.\n\nB. P., aged 19");
        add("sous_titre.royaumedesidees.augustin_rire", "Augustine laughs");
        add("sous_titre.royaumedesidees.ambroise_page", "A page turns");
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
        add("sous_titre.royaumedesidees.sanglots", "Monica sobs");

        add(ModTags.LIE_AU_ROYAUME, "Bound to the Kingdom");
    }
}
