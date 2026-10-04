package com.royaumedesidees.registre;

import com.royaumedesidees.RoyaumeDesIdees;
import com.royaumedesidees.item.ItemAvecCitation;
import com.royaumedesidees.item.LanterneDiogeneItem;
import com.royaumedesidees.item.TolleLegeItem;
import net.minecraft.world.food.FoodProperties;
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

    // --- v0.2 : le Jardin de Milan ---

    /** Poire achetée : nourrit moins que la volée (Augustin l'avait remarqué). */
    public static final DeferredItem<ItemAvecCitation> POIRE = ITEMS.registerItem("poire", ItemAvecCitation::new,
            new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.3F).build()));
    /** Poire volée : bien meilleure. La Culpabilité est donnée au moment du vol, pas en la mangeant. */
    public static final DeferredItem<ItemAvecCitation> POIRE_VOLEE = ITEMS.registerItem("poire_volee", ItemAvecCitation::new,
            new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.8F).build()));

    public static final DeferredItem<BlockItem> FEUILLES_POIRIER = ITEMS.registerSimpleBlockItem(ModBlocs.FEUILLES_POIRIER);
    public static final DeferredItem<BlockItem> BOIS_FIGUIER = ITEMS.registerSimpleBlockItem(ModBlocs.BOIS_FIGUIER);
    public static final DeferredItem<BlockItem> PLANCHES_FIGUIER = ITEMS.registerSimpleBlockItem(ModBlocs.PLANCHES_FIGUIER);
    public static final DeferredItem<BlockItem> FEUILLES_FIGUIER = ITEMS.registerSimpleBlockItem(ModBlocs.FEUILLES_FIGUIER);
    public static final DeferredItem<BlockItem> CONFESSIONNAL = ITEMS.registerSimpleBlockItem(ModBlocs.CONFESSIONNAL);
    public static final DeferredItem<BlockItem> ETAL_VERGER = ITEMS.registerSimpleBlockItem(ModBlocs.ETAL_VERGER);
    public static final DeferredItem<BlockItem> PUPITRE_AMBROISE = ITEMS.registerSimpleBlockItem(ModBlocs.PUPITRE_AMBROISE);

    private ModItems() {
    }
}
