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

    /**
     * Musique du Royaume, jouée dans toute la dimension à la place de celle de Minecraft. Le fichier
     * (sounds/musique_royaume.ogg) est local et n'est pas publié sur le dépôt git : voir docs/sons_a_fournir.md.
     */
    public static final DeferredHolder<SoundEvent, SoundEvent> MUSIQUE_ROYAUME = SONS.register("musique_royaume",
            () -> SoundEvent.createVariableRangeEvent(RoyaumeDesIdees.id("musique_royaume")));

    /** Sanglots lointains autour d'un joueur à la Culpabilité III ou plus. */
    public static final DeferredHolder<SoundEvent, SoundEvent> SANGLOTS = SONS.register("sanglots",
            () -> SoundEvent.createVariableRangeEvent(RoyaumeDesIdees.id("sanglots")));

    /** Musique dramatique, en boucle, pour un joueur à la Culpabilité IV ou plus. */
    public static final DeferredHolder<SoundEvent, SoundEvent> MUSIQUE_CULPABILITE = SONS.register("musique_culpabilite",
            () -> SoundEvent.createVariableRangeEvent(RoyaumeDesIdees.id("musique_culpabilite")));

    private ModSons() {
    }
}
