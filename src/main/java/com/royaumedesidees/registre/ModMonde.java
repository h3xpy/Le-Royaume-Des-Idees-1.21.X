package com.royaumedesidees.registre;

import com.mojang.serialization.MapCodec;
import com.royaumedesidees.RoyaumeDesIdees;
import com.royaumedesidees.monde.GenerateurRoyaume;
import com.royaumedesidees.monde.SourceBiomesRoyaume;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/** La dimension du Royaume : identifiants, générateur de chunks, source de biomes et biomes. */
public final class ModMonde {
    public static final DeferredRegister<MapCodec<? extends ChunkGenerator>> GENERATEURS =
            DeferredRegister.create(Registries.CHUNK_GENERATOR, RoyaumeDesIdees.MODID);
    public static final DeferredRegister<MapCodec<? extends BiomeSource>> SOURCES_BIOMES =
            DeferredRegister.create(Registries.BIOME_SOURCE, RoyaumeDesIdees.MODID);

    public static final DeferredHolder<MapCodec<? extends ChunkGenerator>, MapCodec<GenerateurRoyaume>> GENERATEUR_ROYAUME =
            GENERATEURS.register("royaume", () -> GenerateurRoyaume.CODEC);
    public static final DeferredHolder<MapCodec<? extends BiomeSource>, MapCodec<SourceBiomesRoyaume>> SOURCE_BIOMES_ROYAUME =
            SOURCES_BIOMES.register("royaume", () -> SourceBiomesRoyaume.CODEC);

    /** royaumedesidees:royaume, sous ses trois formes (type, définition, niveau en jeu). */
    public static final ResourceKey<DimensionType> TYPE_ROYAUME = ResourceKey.create(Registries.DIMENSION_TYPE, RoyaumeDesIdees.id("royaume"));
    public static final ResourceKey<LevelStem> DEFINITION_ROYAUME = ResourceKey.create(Registries.LEVEL_STEM, RoyaumeDesIdees.id("royaume"));
    public static final ResourceKey<Level> ROYAUME = ResourceKey.create(Registries.DIMENSION, RoyaumeDesIdees.id("royaume"));

    // Les cinq biomes. Identifiants définitifs : jamais renommés.
    public static final ResourceKey<Biome> CAVERNE_PLATON = biome("caverne_platon");
    public static final ResourceKey<Biome> JARDIN_MILAN = biome("jardin_milan");
    public static final ResourceKey<Biome> PORT_ROYAL = biome("port_royal");
    public static final ResourceKey<Biome> PUY_DE_DOME = biome("puy_de_dome");
    public static final ResourceKey<Biome> HIPPONE = biome("hippone");

    private static ResourceKey<Biome> biome(String nom) {
        return ResourceKey.create(Registries.BIOME, RoyaumeDesIdees.id(nom));
    }

    private ModMonde() {
    }
}
