package com.royaumedesidees.registre;

import com.royaumedesidees.RoyaumeDesIdees;
import com.royaumedesidees.item.LanterneDiogeneItem;
import com.royaumedesidees.item.TolleLegeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Tous les items du mod. Rappel : un identifiant publié ne se renomme et ne se supprime jamais.
 */
public final class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(RoyaumeDesIdees.MODID);

    /** Allume le portail de bibliothèques (étape 7). */
    public static final DeferredItem<TolleLegeItem> TOLLE_LEGE = ITEMS.registerItem("tolle_lege",
            TolleLegeItem::new, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));

    /** Révèle les Ombres de la Caverne. Liée au Royaume. */
    public static final DeferredItem<LanterneDiogeneItem> LANTERNE_DIOGENE = ITEMS.registerItem("lanterne_diogene",
            LanterneDiogeneItem::new, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));

    public static final DeferredItem<BlockItem> PIERRE_OMBRE = ITEMS.registerSimpleBlockItem(ModBlocs.PIERRE_OMBRE);
    public static final DeferredItem<BlockItem> PIERRE_OMBRE_TAILLEE = ITEMS.registerSimpleBlockItem(ModBlocs.PIERRE_OMBRE_TAILLEE);
    public static final DeferredItem<BlockItem> CHAINE_CAVERNE = ITEMS.registerSimpleBlockItem(ModBlocs.CHAINE_CAVERNE);

    private ModItems() {
    }
}
