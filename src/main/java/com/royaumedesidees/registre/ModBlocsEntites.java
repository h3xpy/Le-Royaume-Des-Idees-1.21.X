package com.royaumedesidees.registre;

import com.royaumedesidees.RoyaumeDesIdees;
import com.royaumedesidees.bloc.PupitreAmbroiseBlocEntite;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Entités de bloc du mod. Identifiants définitifs. */
public final class ModBlocsEntites {
    public static final DeferredRegister<BlockEntityType<?>> BLOCS_ENTITES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, RoyaumeDesIdees.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PupitreAmbroiseBlocEntite>> PUPITRE_AMBROISE =
            BLOCS_ENTITES.register("pupitre_ambroise",
                    () -> BlockEntityType.Builder.of(PupitreAmbroiseBlocEntite::new, ModBlocs.PUPITRE_AMBROISE.get()).build(null));

    private ModBlocsEntites() {
    }
}
