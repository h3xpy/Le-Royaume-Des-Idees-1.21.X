package com.royaumedesidees;

import com.mojang.logging.LogUtils;
import com.royaumedesidees.bloc.ZonesSilence;
import com.royaumedesidees.caverne.PopulationOmbres;
import com.royaumedesidees.caverne.SortieCaverne;
import com.royaumedesidees.commande.CommandesRoyaume;
import com.royaumedesidees.dev.VerificationDev;
import com.royaumedesidees.donnees.GenerateurDonnees;
import com.royaumedesidees.grace.Grace;
import com.royaumedesidees.grace.GracePaquet;
import com.royaumedesidees.grace.Voies;
import com.royaumedesidees.jardin.Confession;
import com.royaumedesidees.jardin.Culpabilite;
import com.royaumedesidees.pnj.Astuces;
import com.royaumedesidees.pnj.Echeancier;
import com.royaumedesidees.pnj.JournalQuetes;
import com.royaumedesidees.pnj.MaisonsPnj;
import com.royaumedesidees.pnj.QueteConversion;
import com.royaumedesidees.pnj.ReponsesChat;
import com.royaumedesidees.pnj.SuiviMonique;
import com.royaumedesidees.registre.ModBlocs;
import com.royaumedesidees.registre.ModBlocsEntites;
import com.royaumedesidees.registre.ModEffets;
import com.royaumedesidees.registre.ModEntites;
import com.royaumedesidees.registre.ModItems;
import com.royaumedesidees.registre.ModMonde;
import com.royaumedesidees.registre.ModOngletsCreatifs;
import com.royaumedesidees.registre.ModPiecesJointes;
import com.royaumedesidees.registre.ModSons;
import com.royaumedesidees.structures.PoseurStructures;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

/**
 * Point d'entrée du mod : branche chaque registre sur le bus du mod.
 */
@Mod(RoyaumeDesIdees.MODID)
public class RoyaumeDesIdees {
    public static final String MODID = "royaumedesidees";
    public static final Logger LOGGER = LogUtils.getLogger();

    public RoyaumeDesIdees(IEventBus modEventBus, ModContainer modContainer) {
        ModBlocs.BLOCS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModOngletsCreatifs.ONGLETS.register(modEventBus);
        ModMonde.GENERATEURS.register(modEventBus);
        ModMonde.SOURCES_BIOMES.register(modEventBus);
        ModSons.SONS.register(modEventBus);
        ModPiecesJointes.PIECES_JOINTES.register(modEventBus);
        ModEntites.ENTITES.register(modEventBus);
        ModEffets.EFFETS.register(modEventBus);
        ModBlocsEntites.BLOCS_ENTITES.register(modEventBus);
        modEventBus.addListener(ModEntites::attributs);

        modEventBus.addListener(GenerateurDonnees::generer);
        NeoForge.EVENT_BUS.addListener(PoseurStructures::demarrage);
        NeoForge.EVENT_BUS.addListener(PoseurStructures::tick);
        NeoForge.EVENT_BUS.addListener(CommandesRoyaume::enregistrer);
        NeoForge.EVENT_BUS.addListener(PopulationOmbres::tick);
        NeoForge.EVENT_BUS.addListener(SortieCaverne::tick);
        NeoForge.EVENT_BUS.addListener(Culpabilite::tick);
        NeoForge.EVENT_BUS.addListener(Culpabilite::changementDimension);
        NeoForge.EVENT_BUS.addListener(Confession::enregistrer);
        // Grâce (v0.3) : synchronisation de la jauge, coups reçus sans riposter.
        modEventBus.addListener((net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent evenement) ->
                evenement.registrar("1").playToClient(GracePaquet.TYPE, GracePaquet.CODEC, GracePaquet::recevoir));
        NeoForge.EVENT_BUS.addListener(Grace::connexion);
        NeoForge.EVENT_BUS.addListener(Grace::changementDimension);
        NeoForge.EVENT_BUS.addListener(Grace::reapparition);
        NeoForge.EVENT_BUS.addListener(Grace::degats);
        NeoForge.EVENT_BUS.addListener(Grace::attaque);
        NeoForge.EVENT_BUS.addListener(Grace::tick);
        // PNJ (v0.3) : réponses données dans le chat.
        // Les réponses attendues par un PNJ passent avant la zone de silence d'Ambroise.
        NeoForge.EVENT_BUS.addListener(net.neoforged.bus.api.EventPriority.HIGH, ReponsesChat::chat);
        NeoForge.EVENT_BUS.addListener(ReponsesChat::tick);
        NeoForge.EVENT_BUS.addListener(SuiviMonique::tick);
        NeoForge.EVENT_BUS.addListener(MaisonsPnj::tick);
        // Quête de conversion (v0.3) : silence, larmes sous le figuier, lecture de Romains ; actions différées.
        NeoForge.EVENT_BUS.addListener(QueteConversion::tick);
        NeoForge.EVENT_BUS.addListener(QueteConversion::lutrin);
        NeoForge.EVENT_BUS.addListener(Echeancier::tick);
        // Tout se découvre en jeu : astuces, journal /quete. Les Voies : effets et liste TAB.
        NeoForge.EVENT_BUS.addListener(Astuces::tick);
        NeoForge.EVENT_BUS.addListener(JournalQuetes::enregistrer);
        NeoForge.EVENT_BUS.addListener(Voies::tick);
        NeoForge.EVENT_BUS.addListener(Voies::nomTab);
        // Zone de silence du Pupitre d'Ambroise : chat muet, monstres sourds.
        NeoForge.EVENT_BUS.addListener(ZonesSilence::chat);
        NeoForge.EVENT_BUS.addListener(ZonesSilence::cible);
        NeoForge.EVENT_BUS.addListener(ZonesSilence::tick);
        VerificationDev.activerSiDemande();
    }

    /** Raccourci pour un identifiant dans l'espace de noms du mod. */
    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
