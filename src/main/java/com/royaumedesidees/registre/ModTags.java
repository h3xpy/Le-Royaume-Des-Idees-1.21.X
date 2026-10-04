package com.royaumedesidees.registre;

import com.royaumedesidees.RoyaumeDesIdees;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class ModTags {
    /** Objets sans effet hors de la dimension (« Souvenir du Royaume »). */
    public static final TagKey<Item> LIE_AU_ROYAUME = TagKey.create(Registries.ITEM, RoyaumeDesIdees.id("lie_au_royaume"));

    /**
     * Les exceptions à la règle « Royaume seulement » : objets et blocs du Royaume dont l'effet marche aussi hors de
     * la dimension (le Pupitre d'Ambroise, par exemple). Tout le reste ne fonctionne que dans le Royaume.
     */
    public static final TagKey<Item> ACTIF_HORS_ROYAUME = TagKey.create(Registries.ITEM, RoyaumeDesIdees.id("actif_hors_royaume"));

    /** Les quatre Sceaux, que l'Autel de la Cité de Dieu recevra (v1.0). */
    public static final TagKey<Item> SCEAUX = TagKey.create(Registries.ITEM, RoyaumeDesIdees.id("sceaux"));

    private ModTags() {
    }
}
