package com.royaumedesidees.donnees;

import com.royaumedesidees.monde.GenerateurRoyaume;
import com.royaumedesidees.registre.ModMonde;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;

import java.util.OptionalLong;

/** Type de dimension et définition du Royaume, écrits en JSON par la data generation. */
public final class ModDimension {
    public static RegistrySetBuilder registres() {
        return new RegistrySetBuilder()
                .add(Registries.DIMENSION_TYPE, ModDimension::typeDimension)
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
                0.05F,                         // lumière ambiante faible
                new DimensionType.MonsterSettings(false, false, ConstantInt.of(0), 0)));
    }

    private static void definition(BootstrapContext<LevelStem> contexte) {
        HolderGetter<DimensionType> types = contexte.lookup(Registries.DIMENSION_TYPE);
        HolderGetter<Biome> biomes = contexte.lookup(Registries.BIOME);
        // Biome provisoire : les cinq biomes du Royaume et leur répartition arrivent à l'étape 5.
        contexte.register(ModMonde.DEFINITION_ROYAUME, new LevelStem(
                types.getOrThrow(ModMonde.TYPE_ROYAUME),
                new GenerateurRoyaume(new FixedBiomeSource(biomes.getOrThrow(Biomes.THE_VOID)))));
    }

    private ModDimension() {
    }
}
