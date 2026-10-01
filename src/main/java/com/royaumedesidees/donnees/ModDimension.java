package com.royaumedesidees.donnees;

import com.royaumedesidees.monde.GenerateurRoyaume;
import com.royaumedesidees.monde.SourceBiomesRoyaume;
import com.royaumedesidees.registre.ModMonde;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;

import java.util.OptionalLong;

/** Type de dimension, biomes et définition du Royaume, écrits en JSON par la data generation. */
public final class ModDimension {
    public static RegistrySetBuilder registres() {
        return new RegistrySetBuilder()
                .add(Registries.DIMENSION_TYPE, ModDimension::typeDimension)
                .add(Registries.BIOME, ModDimension::biomes)
                .add(Registries.LEVEL_STEM, ModDimension::definition);
    }

    private static void typeDimension(BootstrapContext<DimensionType> contexte) {
        contexte.register(ModMonde.TYPE_ROYAUME, new DimensionType(
                OptionalLong.empty(),          // le jour et la nuit passent
                true,                          // ciel
                false,                         // pas de plafond
                false,                         // l'eau ne s'évapore pas
                true,                          // boussoles et horloges fonctionnent
                1.0,                           // pas de mise à l'échelle des coordonnées
                true,                          // on peut dormir : les lits n'explosent pas
                false,                         // pas d'ancre de réapparition
                0,                             // y minimal
                GenerateurRoyaume.HAUTEUR_MONDE,
                GenerateurRoyaume.HAUTEUR_MONDE,
                BlockTags.INFINIBURN_OVERWORLD,
                BuiltinDimensionTypes.OVERWORLD_EFFECTS, // ciel de l'Overworld, couleurs données par les biomes
                0.1F,                          // lumière ambiante : les ombres ne sont jamais tout à fait noires
                new DimensionType.MonsterSettings(false, false, ConstantInt.of(0), 0)));
    }

    /**
     * Ambiance éthérée : ciels pâles, brumes claires et nacrées, eau turquoise, herbes lumineuses
     * et fines particules qui flottent dans l'air. Aucun mob ni décoration vanilla.
     */
    private static void biomes(BootstrapContext<Biome> contexte) {
        // Caverne : obscurité violette et cendres, les ombres du mur de Platon.
        contexte.register(ModMonde.CAVERNE_PLATON, biome(0.8F, false,
                0x2a2440, 0x120f1e, 0x3a3560, 0x0d0b14, 0x4a4a5a, 0x3e4a3a, ParticleTypes.ASH, 0.006F));
        // Jardin de Milan : ciel pervenche, brume lavande, vert de printemps, pétales qui volent.
        contexte.register(ModMonde.JARDIN_MILAN, biome(0.8F, true,
                0xb6c2f6, 0xece6ff, 0x86e0ea, 0x3a6a8a, 0xb0f08e, 0x9ee080, ParticleTypes.CHERRY_LEAVES, 0.0025F));
        // Port-Royal : ciel de perle, brume blanche du marais, vert argenté, poussière blanche en suspension.
        contexte.register(ModMonde.PORT_ROYAL, biome(0.7F, true,
                0xccd0e2, 0xe8eaf0, 0x9cc4c0, 0x4a5e60, 0xbccaae, 0xa8b89c, ParticleTypes.WHITE_ASH, 0.008F));
        // Puy de Dôme : ciel froid et limpide, vert alpin bleuté, fines poussières dans l'air rare.
        // Température choisie pour que la neige ne tombe qu'au-dessus de y ≈ 264.
        contexte.register(ModMonde.PUY_DE_DOME, biome(0.38F, true,
                0xa8caf6, 0xeaf2fc, 0x8ad0f0, 0x3a6a8a, 0x8cc890, 0x7ab47e, ParticleTypes.WHITE_ASH, 0.004F));
        // Hippone : ciel clair, brume dorée, mer turquoise, garrigue vert sauge, poussière dorée dans l'air.
        contexte.register(ModMonde.HIPPONE, biome(0.95F, true,
                0xc2d4f4, 0xf8ecd8, 0x5fd8c8, 0x2a7a78, 0xccd290, 0xb0c070, ParticleTypes.WHITE_ASH, 0.003F));
    }

    private static Biome biome(float temperature, boolean precipitations, int ciel, int brume, int eau, int brumeEau,
                               int herbe, int feuillage, SimpleParticleType particule, float densite) {
        return new Biome.BiomeBuilder()
                .hasPrecipitation(precipitations)
                .temperature(temperature)
                .downfall(0.5F)
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .skyColor(ciel)
                        .fogColor(brume)
                        .waterColor(eau)
                        .waterFogColor(brumeEau)
                        .grassColorOverride(herbe)
                        .foliageColorOverride(feuillage)
                        .ambientParticle(new AmbientParticleSettings(particule, densite))
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .build())
                .mobSpawnSettings(MobSpawnSettings.EMPTY)
                .generationSettings(BiomeGenerationSettings.EMPTY)
                .build();
    }

    private static void definition(BootstrapContext<LevelStem> contexte) {
        HolderGetter<DimensionType> types = contexte.lookup(Registries.DIMENSION_TYPE);
        HolderGetter<Biome> biomes = contexte.lookup(Registries.BIOME);
        contexte.register(ModMonde.DEFINITION_ROYAUME, new LevelStem(
                types.getOrThrow(ModMonde.TYPE_ROYAUME),
                new GenerateurRoyaume(new SourceBiomesRoyaume(
                        biomes.getOrThrow(ModMonde.CAVERNE_PLATON),
                        biomes.getOrThrow(ModMonde.JARDIN_MILAN),
                        biomes.getOrThrow(ModMonde.PORT_ROYAL),
                        biomes.getOrThrow(ModMonde.PUY_DE_DOME),
                        biomes.getOrThrow(ModMonde.HIPPONE)))));
    }

    private ModDimension() {
    }
}
