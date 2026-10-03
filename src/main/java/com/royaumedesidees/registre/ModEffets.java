package com.royaumedesidees.registre;

import com.royaumedesidees.RoyaumeDesIdees;
import com.royaumedesidees.jardin.CulpabiliteEffet;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Effets de statut du mod. Identifiants définitifs. */
public final class ModEffets {
    public static final DeferredRegister<MobEffect> EFFETS = DeferredRegister.create(Registries.MOB_EFFECT, RoyaumeDesIdees.MODID);

    /** Culpabilité I à V : voir {@link com.royaumedesidees.jardin.Culpabilite}. */
    public static final DeferredHolder<MobEffect, CulpabiliteEffet> CULPABILITE = EFFETS.register("culpabilite", CulpabiliteEffet::new);

    private ModEffets() {
    }
}
