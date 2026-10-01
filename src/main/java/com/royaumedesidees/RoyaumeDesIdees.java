package com.royaumedesidees;

import com.mojang.logging.LogUtils;
import com.royaumedesidees.dev.VerificationDev;
import com.royaumedesidees.donnees.GenerateurDonnees;
import com.royaumedesidees.registre.ModBlocs;
import com.royaumedesidees.registre.ModItems;
import com.royaumedesidees.registre.ModMonde;
import com.royaumedesidees.registre.ModOngletsCreatifs;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
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

        modEventBus.addListener(GenerateurDonnees::generer);
        VerificationDev.activerSiDemande();
    }

    /** Raccourci pour un identifiant dans l'espace de noms du mod. */
    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
