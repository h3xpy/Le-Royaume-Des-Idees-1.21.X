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

        add("entity.royaumedesidees.ombre", "Ombre");
        add("message.royaumedesidees.ombre.intouchable", "Ce n'est qu'une ombre : ton coup la traverse.");
        add("message.royaumedesidees.caverne.enchaine", "Tu es enchaîné face au mur, comme les prisonniers de Platon. Brise tes chaînes.");
        add("message.royaumedesidees.caverne.lumiere", "Tes yeux, habitués aux ombres, brûlent à la lumière du jour.");
        add("message.royaumedesidees.caverne.lanterne", "Au bord du chemin, une vieille lanterne t'attendait. « Je cherche un homme », disait Diogène.");
        add("item.royaumedesidees.lanterne_diogene.effet", "Tenue en main : révèle les Ombres à moins de 8 blocs.");
        add("item.royaumedesidees.lanterne_diogene.citation", "« Je cherche un homme. » (Diogène de Sinope)");
        add("tooltip.royaumedesidees.souvenir", "Souvenir du Royaume");
        add("tooltip.royaumedesidees.souvenir.detail", "Sans effet hors du Royaume.");
        add("advancements.royaumedesidees.racine.title", "Le Royaume des Idées");
        add("advancements.royaumedesidees.racine.description", "Prendre, lire, et passer le portail de bibliothèques.");
        add("advancements.royaumedesidees.allegorie_vecue.title", "Allégorie vécue");
        add("advancements.royaumedesidees.allegorie_vecue.description", "Sortir de la Caverne de Platon et voir enfin la lumière (et en être ébloui).");

        add("message.royaumedesidees.ombre.statue", "Ce n'était qu'une statue de bois. Diogène cherche toujours un homme.");
        add("message.royaumedesidees.caverne.tenebres", "Revenu dans la Caverne, tes yeux pleins de soleil ne voient plus que des ténèbres.");

        add("item.royaumedesidees.poire", "Poire");
        add("item.royaumedesidees.poire.citation", "Achetée honnêtement. Moins bonne que l'autre : Augustin l'avait bien remarqué.");
        add("item.royaumedesidees.poire_volee", "Poire volée");
        add("item.royaumedesidees.poire_volee.citation", "« Ce n'était pas la poire que j'aimais, c'était le vol. » (d'après les Confessions, II)");
        add("block.royaumedesidees.feuilles_poirier", "Feuilles de poirier");
        add("block.royaumedesidees.bois_figuier", "Bois de figuier");
        add("block.royaumedesidees.planches_figuier", "Planches de figuier");
        add("block.royaumedesidees.feuilles_figuier", "Feuilles de figuier");
        add("block.royaumedesidees.confessionnal", "Confessionnal");
        add("block.royaumedesidees.etal_verger", "Étal du verger");
        add("effect.royaumedesidees.culpabilite", "Culpabilité");
        add("message.royaumedesidees.culpabilite.vol_poire", "%1$s a volé une poire. Honte.");
        add("message.royaumedesidees.culpabilite.ecrase", "%1$s croule sous la culpabilité.");
        add("message.royaumedesidees.confession.publique", "%1$s se confesse : « %2$s »");
        add("message.royaumedesidees.confession.allege", "Ta conscience s'allège. Culpabilité restante : %1$s.");
        add("message.royaumedesidees.confession.rien", "Augustin en a écrit treize livres ; toi, tu n'as vraiment rien ?");
        add("message.royaumedesidees.confession.loin", "Il faut un Confessionnal pour se confesser. Il y en a un près de l'Autel, au centre de l'île.");
        add("message.royaumedesidees.confession.trop_tot", "Doucement : attends encore %1$s secondes avant ta prochaine confession.");
        add("message.royaumedesidees.confession.trop_long", "Ta confession est trop longue (%1$s caractères au plus). Augustin, lui, avait treize livres.");
        add("message.royaumedesidees.confession.repetee", "*Claque !* Tu as déjà avoué ça. Augustin a écrit treize livres sans se répéter une seule fois : trouve un autre péché, ou va réfléchir.");
        add("message.royaumedesidees.confession.hors_royaume", "Ici, ta conscience se tait : la Culpabilité et la confession n'existent que dans le Royaume.");
        // v0.3 : Grâce et Voies
        add("message.royaumedesidees.grace.gain", "+%1$s de Grâce (%2$s)");
        add("message.royaumedesidees.grace.don", "La grâce est un don, pas un salaire.");
        add("message.royaumedesidees.grace.rien", "Pas de Grâce cette fois (%1$s). Elle ne se mérite pas.");
        add("message.royaumedesidees.grace.raison.confession", "confession");
        add("message.royaumedesidees.grace.raison.frappe", "frappé sans riposter");
        add("message.royaumedesidees.grace.raison.commande", "commande");
        add("commande.royaumedesidees.grace.valeur", "%1$s : %2$s de Grâce, %3$s.");
        add("voie.royaumedesidees.aucune", "sans Voie");
        add("voie.royaumedesidees.raison", "Voie de la Raison");
        add("voie.royaumedesidees.coeur", "Voie du Cœur");
        // v0.3 : PNJ
        add("entity.royaumedesidees.augustin_jeune", "Augustin");
        add("entity.royaumedesidees.adeodat", "Adéodat");
        add("entity.royaumedesidees.ambroise", "Ambroise");
        add("entity.royaumedesidees.monique", "Monique");
        add("entity.royaumedesidees.pascal", "Blaise Pascal");
        add("pnj.royaumedesidees.augustin_jeune.bonjour", "Salut ! Tu as l'air bien trop sage. Ça se soigne : il y a des poires, là-bas.");
        add("pnj.royaumedesidees.augustin_jeune.coup", "*éclate de rire* Tu frappes comme tu voles : sans conviction !");
        add("pnj.royaumedesidees.adeodat.bonjour", "Bonjour. Mon père dit que je suis né de son péché. Moi, je préfère les mathématiques.");
        add("pnj.royaumedesidees.adeodat.coup", "*soupire* Frapper un génie ne rend pas plus intelligent. J'ai vérifié.");
        add("pnj.royaumedesidees.ambroise.bonjour", "*Ambroise lève les yeux de son livre, puis les rabaisse, sans un mot.*");
        add("pnj.royaumedesidees.ambroise.coup", "*Ambroise tourne une page, sans un mot.*");
        add("pnj.royaumedesidees.monique.coup", "*Monique pleure plus fort.*");
        add("message.royaumedesidees.monique.arrive", "§dSainte Monique§r accourt vers toi, en larmes : tu es le plus grand pécheur du Royaume.");
        add("message.royaumedesidees.monique.revient", "§dMonique§r revient prier pour toi : personne ici n'a l'âme plus lourde.");
        add("message.royaumedesidees.monique.quitte", "§dMonique§r te quitte pour %1$s, qui en a plus besoin que toi.");
        add("message.royaumedesidees.monique.rentre", "§dMonique§r sèche ses larmes et rentre prier au jardin de la villa.");
        add("pnj.royaumedesidees.monique.pas_mon_fils", "Ce n'est pas pour toi que je pleure aujourd'hui. Mais je prierai aussi pour toi.");
        add("pnj.royaumedesidees.monique.priere", "*Monique prie en silence.* Tant que personne ne pèche, je peux prier en paix.");
        add("panneau.royaumedesidees.direction.bibliotheque.1", "À la bibliothèque");
        add("panneau.royaumedesidees.direction.bibliotheque.2", "d'Ambroise");
        add("pnj.royaumedesidees.monique.bonjour", "*Monique pleure.* Tant de larmes… Il est impossible que le fils de tant de larmes périsse.");
        add("pnj.royaumedesidees.monique.apaisee", "*Monique sourit, apaisée.* Mes prières sont exaucées. Je peux m'en aller en paix.");
        add("pnj.royaumedesidees.monique.nourrie", "*Monique mange en silence, puis te bénit.*");
        add("pnj.royaumedesidees.monique.repue", "Merci, mais j'ai déjà mangé aujourd'hui. Garde ça pour les pauvres.");
        add("message.royaumedesidees.grace.raison.monique", "Monique nourrie");
        add("pnj.royaumedesidees.pascal.bonjour", "Le cœur a ses raisons que la raison ne connaît point. Mais les comptes de mon père, eux, n'en ont aucune.");
        add("pnj.royaumedesidees.pascal.coup", "Merci. *Il serre sa ceinture.*");
        add("message.royaumedesidees.pascal.merci", "Pascal remercie %1$s de l'avoir frappé.");
        // v0.3 : Bibliothèque d'Ambroise, cellule de Pascal
        add("block.royaumedesidees.pupitre_ambroise", "Pupitre d'Ambroise");
        add("tooltip.royaumedesidees.actif_partout", "Fonctionne aussi hors du Royaume");
        // v0.3 : quête de conversion, Adéodat, récompenses
        add("item.royaumedesidees.livre_confessions", "Livre des Confessions");
        add("item.royaumedesidees.livre_confessions.citation", "« Tu nous as faits pour toi, et notre cœur est inquiet jusqu'à ce qu'il repose en toi. » Clic droit : soigne les joueurs proches.");
        add("item.royaumedesidees.sceau_conversion", "Sceau de la Conversion");
        add("item.royaumedesidees.sceau_conversion.citation", "Premier des quatre Sceaux. L'Autel de la Cité de Dieu l'attend.");
        add("message.royaumedesidees.livre_confessions.souvenir", "Hors du Royaume, le livre se tait.");
        add("message.royaumedesidees.livre_confessions.muet", "Ce livre ne parle qu'aux cœurs convertis.");
        add("message.royaumedesidees.livre_confessions.lu", "Tu lis une page des Confessions : %1$s âme(s) apaisée(s).");
        add("message.royaumedesidees.quete.poires", "Quête de conversion, 1/4 : « Les poires de Thagaste ». Vole 3 poires avec Augustin à côté.");
        add("message.royaumedesidees.quete.silence", "Quête de conversion, 2/4 : « Le silence d'Ambroise ». Reste 60 secondes près de son pupitre, à la bibliothèque, sans rien dire.");
        add("message.royaumedesidees.quete.silence_compte", "Silence… encore %1$s s.");
        add("message.royaumedesidees.quete.silence_rompu", "Tu as voulu parler : le silence recommence.");
        add("message.royaumedesidees.quete.figuier", "Ambroise lève enfin les yeux, et te montre le jardin du figuier. Quête 3/4 : « Prends, lis ». Pleure sous le figuier (accroupis-toi 5 secondes dessous).");
        add("message.royaumedesidees.quete.romains", "Retourne au banc d'Alypius, où tu avais laissé le livre de l'Apôtre, et lis-le.");
        add("message.royaumedesidees.quete.bapteme", "« Comme si une lumière de paix s'était répandue dans mon cœur… » Quête 4/4 : « Le baptême ». Va trouver Ambroise.");
        add("message.royaumedesidees.bapteme.annonce", "Ambroise baptise %1$s, comme Augustin à Pâques 387. Le Royaume compte un converti de plus.");
        add("message.royaumedesidees.voie.annonce", "%1$s a choisi la %2$s.");
        add("message.royaumedesidees.grace.raison.adeodat", "défi d'Adéodat");
        add("message.royaumedesidees.grace.raison.quete", "quête");
        add("message.royaumedesidees.adeodat.humiliation", "Adéodat, 15 ans, a calculé plus vite que %1$s.");
        add("message.royaumedesidees.monique.consolee", "§dMonique§r pleure de joie : « Il est impossible que le fils de tant de larmes périsse. » Elle rentre au jardin, apaisée.");
        add("pnj.royaumedesidees.augustin_jeune.quete.debut", "Toi, tu as une tête à voler des poires. Viens, on en prend %1$s : pas parce qu'on a faim, juste pour le plaisir de le faire.");
        add("pnj.royaumedesidees.augustin_jeune.quete.poires", "Encore %1$s poire(s) ! Tu vois le verger de Lucius ? Personne ne regarde.");
        add("pnj.royaumedesidees.augustin_jeune.quete.porcs", "Ha ! On ne les mangera même pas : on les jettera aux porcs. Bon… Il paraît qu'à la bibliothèque, un évêque lit sans remuer les lèvres. Va voir ça, moi je n'y crois pas.");
        add("pnj.royaumedesidees.augustin_jeune.quete.silence", "Alors, cet évêque qui lit sans parler ? Va à la bibliothèque, et tais-toi un peu, pour une fois.");
        add("pnj.royaumedesidees.augustin_jeune.quete.figuier", "Le figuier du jardin… Je n'y vais jamais. On y pleure trop.");
        add("pnj.royaumedesidees.augustin_jeune.quete.bapteme", "Tu as l'air changé. Va voir Ambroise, avant que je te fasse changer d'avis.");
        add("pnj.royaumedesidees.augustin_jeune.quete.finie", "Converti, toi ? Moi aussi, un jour. « Donne-moi la chasteté, mais pas tout de suite. »");
        add("pnj.royaumedesidees.augustin_jeune.vol", "*accourt en riant* Bien joué ! Encore %1$s et je te raconte un secret.");
        add("pnj.royaumedesidees.augustin_jeune.vol_encore", "*accourt en riant* Encore une ? Tu as bon goût… pour le vol.");
        add("pnj.royaumedesidees.ambroise.bapteme", "*Ambroise pose son livre, verse l'eau sur ta tête, et dit enfin quelques mots.* Je te baptise.");
        add("pnj.royaumedesidees.ambroise.silence", "*Ambroise pose un doigt sur ses lèvres, et continue de lire.*");
        add("pnj.royaumedesidees.ambroise.pas_assez_de_grace", "*Ambroise secoue la tête : pour changer de Voie, il faut %1$s de Grâce.*");
        add("pnj.royaumedesidees.adeodat.defi", "Combien font %1$s ? Tu as %2$s secondes. Réponds dans le chat.");
        add("pnj.royaumedesidees.adeodat.reussi", "*hoche la tête* Pas mal, pour un adulte.");
        add("pnj.royaumedesidees.adeodat.rate", "C'était %1$s. Je l'avais trouvé avant que tu finisses de lire la question.");
        add("pnj.royaumedesidees.adeodat.repos", "Un défi à la fois. Reviens dans %1$s secondes, même les génies respirent.");
        add("pnj.royaumedesidees.adeodat.attends", "J'attends ta réponse. Dans le chat.");
        add("message.royaumedesidees.silence.chut", "Chut. Ambroise lit.");
        add("panneau.royaumedesidees.bibliotheque.1", "Silentium.");
        add("panneau.royaumedesidees.bibliotheque.2", "Ici, Ambroise");
        add("panneau.royaumedesidees.bibliotheque.3", "lit en silence.");
        add("panneau.royaumedesidees.port_royal.1", "Port-Royal");
        add("panneau.royaumedesidees.port_royal.2", "Abbaye en");
        add("panneau.royaumedesidees.port_royal.3", "construction");
        add("livre.royaumedesidees.hexaemeron.page1", "Hexaemeron\n\nLes six jours de la Création, prêchés par Ambroise à Milan.\n\nAugustin venait l'écouter, d'abord pour sa belle parole, puis pour ce qu'il disait.");
        add("livre.royaumedesidees.hexaemeron.page2", "« Quand il lisait, ses yeux couraient sur les pages et son cœur en cherchait le sens, mais sa voix et sa langue se reposaient. »\n\n(Confessions, VI, 3)");
        add("livre.royaumedesidees.comptes.page1", "Élection de Rouen\nTaille de 1641\n\n   3 livres 7 sols 4 deniers\n+ 12 livres 15 sols 9 deniers\n+  2 livres 19 sols 11 deniers\n= ?\n\n20 sols font une livre,\n12 deniers font un sol.");
        add("livre.royaumedesidees.comptes.page2", "Mon père y passe ses nuits.\n\nIl faudrait une machine pour faire ces additions.\n\nB. P., 19 ans");
        add("sous_titre.royaumedesidees.augustin_rire", "Augustin rit");
        add("sous_titre.royaumedesidees.ambroise_page", "Une page tourne");
        add("message.royaumedesidees.confessionnal.mode_emploi", "Pour te confesser, tape /confesse suivi de ton péché.");
        add("message.royaumedesidees.etal.achat", "Tu achètes %1$s poires, honnêtement. Elles ont l'air moins bonnes.");
        add("message.royaumedesidees.etal.ecriteau", "« Une émeraude dans le tronc, %1$s poires pour toi. Ou alors, tu les voles… »");
        add("panneau.royaumedesidees.verger.lucius", "Verger de Lucius");
        add("panneau.royaumedesidees.verger.severe", "Verger de Sévère");
        add("panneau.royaumedesidees.verger.verecundus", "Verger de Vérécundus");
        add("panneau.royaumedesidees.vigne.1", "Vigne de");
        add("panneau.royaumedesidees.vigne.2", "Patricius");
        add("panneau.royaumedesidees.porcherie.1", "Porcherie");
        add("panneau.royaumedesidees.porcherie.2", "Ici finissent");
        add("panneau.royaumedesidees.porcherie.3", "les poires");
        add("panneau.royaumedesidees.porcherie.4", "volées.");
        add("panneau.royaumedesidees.direction.vergers.1", "Aux vergers");
        add("panneau.royaumedesidees.direction.vergers.2", "et à la vigne");
        add("panneau.royaumedesidees.direction.figuier.1", "Au figuier");
        add("panneau.royaumedesidees.direction.figuier.2", "du jardin");
        add("panneau.royaumedesidees.confessionnal.1", "Inquietum est");
        add("panneau.royaumedesidees.confessionnal.2", "cor nostrum,");
        add("panneau.royaumedesidees.confessionnal.3", "donec requiescat");
        add("panneau.royaumedesidees.confessionnal.4", "in te.");
        add("livre.royaumedesidees.platoniciens.page1", "Livres des Platoniciens\n\nTraduits du grec en latin par Marius Victorinus.\n\nAugustin les lut à Milan, en 386, dans cette maison.");
        add("livre.royaumedesidees.platoniciens.page2", "Il y trouva qu'au commencement était le Verbe, et que le Verbe était Dieu.\n\nIl n'y trouva pas que le Verbe s'est fait chair.\n\n(Confessions, VII, 9)");
        add("livre.royaumedesidees.platoniciens.page3", "Souviens-toi de la Caverne : ce que tu prenais pour le monde n'en était que l'ombre.\n\nPlaton montre la lumière ; il ne dit pas le chemin.");
        add("livre.royaumedesidees.romains.page1", "Épître aux Romains, XIII, 13-14\n\nMarchons honnêtement, comme en plein jour, loin des excès et de l'ivrognerie, de la luxure et de l'impudicité,");
        add("livre.royaumedesidees.romains.page2", "des querelles et des jalousies. Mais revêtez-vous du Seigneur Jésus-Christ, et n'ayez pas soin de la chair pour en satisfaire les convoitises.");
        add("livre.royaumedesidees.romains.page3", "Augustin lut ce passage en silence, là, sur ce banc.\n\n« Comme si une lumière de paix s'était répandue dans mon cœur, toutes les ténèbres du doute s'enfuirent. »\n\n(Confessions, VIII, 12)");
        add("panneau.royaumedesidees.verger.defense", "Défense");
        add("panneau.royaumedesidees.verger.voler", "de voler !");
        add("message.royaumedesidees.figuier.voix", "Une voix d'enfant, venue de la maison voisine : « Prends, lis ; prends, lis. »");
        add("sous_titre.royaumedesidees.sanglots", "Monique sanglote");

        add(ModTags.LIE_AU_ROYAUME, "Lié au Royaume");
    }
}
