package com.royaumedesidees;

import com.royaumedesidees.dev.VisiteDev;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

/**
 * Partie client du mod (rendu, écrans, sons). Cette classe n'est jamais chargée
 * sur un serveur dédié.
 */
@Mod(value = RoyaumeDesIdees.MODID, dist = Dist.CLIENT)
public class RoyaumeDesIdeesClient {
    public RoyaumeDesIdeesClient(IEventBus modEventBus, ModContainer modContainer) {
        VisiteDev.activerSiDemande();
    }
}
