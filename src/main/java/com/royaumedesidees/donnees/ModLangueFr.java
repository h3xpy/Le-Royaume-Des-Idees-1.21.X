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
        add("sous_titre.royaumedesidees.sanglots", "Quelqu'un sanglote");

        add(ModTags.LIE_AU_ROYAUME, "Lié au Royaume");
    }
}
