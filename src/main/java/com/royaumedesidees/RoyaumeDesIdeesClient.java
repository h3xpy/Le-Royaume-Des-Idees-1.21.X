package com.royaumedesidees;

import com.royaumedesidees.client.InfobulleSouvenir;
import com.royaumedesidees.client.JaugeGrace;
import com.royaumedesidees.client.MusiqueRoyaume;
import com.royaumedesidees.client.RenduOmbre;
import com.royaumedesidees.client.RenduPnj;
import com.royaumedesidees.dev.VisiteDev;
import com.royaumedesidees.registre.ModEntites;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.common.NeoForge;

/**
 * Partie client du mod (rendu, écrans, sons). Cette classe n'est jamais chargée
 * sur un serveur dédié.
 */
@Mod(value = RoyaumeDesIdees.MODID, dist = Dist.CLIENT)
public class RoyaumeDesIdeesClient {
    public RoyaumeDesIdeesClient(IEventBus modEventBus, ModContainer modContainer) {
        NeoForge.EVENT_BUS.addListener(MusiqueRoyaume::choisir);
        NeoForge.EVENT_BUS.addListener(MusiqueRoyaume::tick);
        NeoForge.EVENT_BUS.addListener(InfobulleSouvenir::infobulle);
        modEventBus.addListener(JaugeGrace::enregistrer);
        modEventBus.addListener((EntityRenderersEvent.RegisterLayerDefinitions evenement) ->
                evenement.registerLayerDefinition(RenduOmbre.COUCHE, RenduOmbre::couche));
        modEventBus.addListener((EntityRenderersEvent.RegisterRenderers evenement) -> {
            evenement.registerEntityRenderer(ModEntites.OMBRE.get(), RenduOmbre::new);
            evenement.registerEntityRenderer(ModEntites.AUGUSTIN_JEUNE.get(), contexte -> new RenduPnj<>(contexte, false));
            evenement.registerEntityRenderer(ModEntites.ADEODAT.get(), contexte -> new RenduPnj<>(contexte, false));
            evenement.registerEntityRenderer(ModEntites.AMBROISE.get(), contexte -> new RenduPnj<>(contexte, false));
            evenement.registerEntityRenderer(ModEntites.MONIQUE.get(), contexte -> new RenduPnj<>(contexte, true));
            evenement.registerEntityRenderer(ModEntites.PASCAL.get(), contexte -> new RenduPnj<>(contexte, false));
        });
        VisiteDev.activerSiDemande();
    }
}
