package com.royaumedesidees.registre;

import com.royaumedesidees.RoyaumeDesIdees;
import com.royaumedesidees.entite.Ombre;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
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

    private ModEntites() {
    }

    public static void attributs(EntityAttributeCreationEvent evenement) {
        evenement.put(OMBRE.get(), Ombre.attributs().build());
    }
}
