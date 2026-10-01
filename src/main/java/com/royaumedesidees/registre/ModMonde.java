package com.royaumedesidees.registre;

import com.mojang.serialization.MapCodec;
import com.royaumedesidees.RoyaumeDesIdees;
import com.royaumedesidees.monde.GenerateurRoyaume;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/** La dimension du Royaume : identifiants et générateur de chunks. */
public final class ModMonde {
    public static final DeferredRegister<MapCodec<? extends ChunkGenerator>> GENERATEURS =
            DeferredRegister.create(Registries.CHUNK_GENERATOR, RoyaumeDesIdees.MODID);

    public static final DeferredHolder<MapCodec<? extends ChunkGenerator>, MapCodec<GenerateurRoyaume>> GENERATEUR_ROYAUME =
            GENERATEURS.register("royaume", () -> GenerateurRoyaume.CODEC);

    /** royaumedesidees:royaume, sous ses trois formes (type, définition, niveau en jeu). */
    public static final ResourceKey<DimensionType> TYPE_ROYAUME = ResourceKey.create(Registries.DIMENSION_TYPE, RoyaumeDesIdees.id("royaume"));
    public static final ResourceKey<LevelStem> DEFINITION_ROYAUME = ResourceKey.create(Registries.LEVEL_STEM, RoyaumeDesIdees.id("royaume"));
    public static final ResourceKey<Level> ROYAUME = ResourceKey.create(Registries.DIMENSION, RoyaumeDesIdees.id("royaume"));

    private ModMonde() {
    }
}
