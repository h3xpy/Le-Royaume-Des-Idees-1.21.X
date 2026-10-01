package com.royaumedesidees;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

/**
 * Point d'entrée du mod. Les registres (blocs, items, entités…) seront branchés ici
 * au fil des étapes, chacun dans sa propre classe.
 */
@Mod(RoyaumeDesIdees.MODID)
public class RoyaumeDesIdees {
    public static final String MODID = "royaumedesidees";
    public static final Logger LOGGER = LogUtils.getLogger();

    public RoyaumeDesIdees(IEventBus modEventBus, ModContainer modContainer) {
    }

    /** Raccourci pour un identifiant dans l'espace de noms du mod. */
    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
