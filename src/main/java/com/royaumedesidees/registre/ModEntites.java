package com.royaumedesidees.registre;

import com.royaumedesidees.RoyaumeDesIdees;
import com.royaumedesidees.entite.Ombre;
import com.royaumedesidees.pnj.Adeodat;
import com.royaumedesidees.pnj.Ambroise;
import com.royaumedesidees.pnj.AugustinJeune;
import com.royaumedesidees.pnj.Monique;
import com.royaumedesidees.pnj.Pascal;
import com.royaumedesidees.pnj.PnjRoyaume;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Entités du mod. Identifiants définitifs : jamais renommés ni supprimés. */
public final class ModEntites {
    public static final DeferredRegister<EntityType<?>> ENTITES = DeferredRegister.create(Registries.ENTITY_TYPE, RoyaumeDesIdees.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<Ombre>> OMBRE = ENTITES.register("ombre",
            () -> EntityType.Builder.of(Ombre::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(8)
                    .build("ombre"));

    // ------------------------------------------------------------------ PNJ (v0.3)

    public static final DeferredHolder<EntityType<?>, EntityType<AugustinJeune>> AUGUSTIN_JEUNE = ENTITES.register("augustin_jeune",
            () -> pnj(AugustinJeune::new, "augustin_jeune"));
    public static final DeferredHolder<EntityType<?>, EntityType<Adeodat>> ADEODAT = ENTITES.register("adeodat",
            () -> pnj(Adeodat::new, "adeodat"));
    public static final DeferredHolder<EntityType<?>, EntityType<Ambroise>> AMBROISE = ENTITES.register("ambroise",
            () -> pnj(Ambroise::new, "ambroise"));
    public static final DeferredHolder<EntityType<?>, EntityType<Monique>> MONIQUE = ENTITES.register("monique",
            () -> pnj(Monique::new, "monique"));
    public static final DeferredHolder<EntityType<?>, EntityType<Pascal>> PASCAL = ENTITES.register("pascal",
            () -> pnj(Pascal::new, "pascal"));

    private static <T extends PnjRoyaume> EntityType<T> pnj(EntityType.EntityFactory<T> fabrique, String id) {
        return EntityType.Builder.of(fabrique, MobCategory.MISC)
                .sized(0.6F, 1.8F)
                .eyeHeight(1.62F)
                .fireImmune()
                .clientTrackingRange(10)
                .build(id);
    }

    private ModEntites() {
    }

    public static void attributs(EntityAttributeCreationEvent evenement) {
        evenement.put(OMBRE.get(), Ombre.attributs().build());
        evenement.put(AUGUSTIN_JEUNE.get(), PnjRoyaume.attributs().build());
        // Adéodat a quinze ans : un peu plus petit que les adultes.
        evenement.put(ADEODAT.get(), PnjRoyaume.attributs().add(Attributes.SCALE, 0.85).build());
        evenement.put(AMBROISE.get(), PnjRoyaume.attributs().build());
        evenement.put(MONIQUE.get(), PnjRoyaume.attributs().build());
        evenement.put(PASCAL.get(), PnjRoyaume.attributs().build());
    }
}
