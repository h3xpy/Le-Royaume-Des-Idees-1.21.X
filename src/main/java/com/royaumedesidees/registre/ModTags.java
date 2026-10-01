package com.royaumedesidees.registre;

import com.royaumedesidees.RoyaumeDesIdees;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class ModTags {
    /** Objets sans effet hors de la dimension (« Souvenir du Royaume »). */
    public static final TagKey<Item> LIE_AU_ROYAUME = TagKey.create(Registries.ITEM, RoyaumeDesIdees.id("lie_au_royaume"));

    private ModTags() {
    }
}
