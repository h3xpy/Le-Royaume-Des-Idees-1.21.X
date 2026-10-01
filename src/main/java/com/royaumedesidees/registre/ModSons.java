package com.royaumedesidees.registre;

import com.royaumedesidees.RoyaumeDesIdees;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Sons du mod. Chaque fichier .ogg est fourni par l'utilisateur (voir docs/sons_a_fournir.md) ;
 * s'il manque, le son est simplement muet.
 */
public final class ModSons {
    public static final DeferredRegister<SoundEvent> SONS = DeferredRegister.create(Registries.SOUND_EVENT, RoyaumeDesIdees.MODID);

    /** Voix d'enfant qui chante près d'un portail allumé (« prends, lis », jardin de Milan, 386). */
    public static final DeferredHolder<SoundEvent, SoundEvent> PORTAIL_CHANT = SONS.register("portail_chant",
            () -> SoundEvent.createVariableRangeEvent(RoyaumeDesIdees.id("portail_chant")));

    private ModSons() {
    }
}
