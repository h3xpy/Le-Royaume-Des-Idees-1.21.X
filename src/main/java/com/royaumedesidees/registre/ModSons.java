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

    /** Pleurs de Monique, qui suit les pécheurs (plus forts à la Culpabilité III ou plus). */
    public static final DeferredHolder<SoundEvent, SoundEvent> SANGLOTS = SONS.register("sanglots",
            () -> SoundEvent.createVariableRangeEvent(RoyaumeDesIdees.id("sanglots")));

    /** Musique dramatique, en boucle, pour un joueur à la Culpabilité IV ou plus. */
    public static final DeferredHolder<SoundEvent, SoundEvent> MUSIQUE_CULPABILITE = SONS.register("musique_culpabilite",
            () -> SoundEvent.createVariableRangeEvent(RoyaumeDesIdees.id("musique_culpabilite")));

    /** Rire d'Augustin jeune, quand on le frappe (v0.3). */
    public static final DeferredHolder<SoundEvent, SoundEvent> AUGUSTIN_RIRE = SONS.register("augustin_rire",
            () -> SoundEvent.createVariableRangeEvent(RoyaumeDesIdees.id("augustin_rire")));

    /** Page tournée par Ambroise, qui ne parle pas (v0.3). */
    public static final DeferredHolder<SoundEvent, SoundEvent> AMBROISE_PAGE = SONS.register("ambroise_page",
            () -> SoundEvent.createVariableRangeEvent(RoyaumeDesIdees.id("ambroise_page")));

    private ModSons() {
    }
}
