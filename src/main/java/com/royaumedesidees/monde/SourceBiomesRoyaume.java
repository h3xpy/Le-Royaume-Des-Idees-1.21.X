package com.royaumedesidees.monde;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;

import java.util.stream.Stream;

/**
 * Répartition des biomes du Royaume, uniquement d'après les coordonnées : la zone de surface vient de
 * {@link ReliefRoyaume#zone}, et la Caverne de Platon occupe un volume fixe sous le centre.
 * <b>Ne plus modifier après la v0.1</b> : les biomes des chunks déjà générés ne changent jamais.
 */
public class SourceBiomesRoyaume extends BiomeSource {
    public static final MapCodec<SourceBiomesRoyaume> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Biome.CODEC.fieldOf("caverne_platon").forGetter(source -> source.caverne),
            Biome.CODEC.fieldOf("jardin_milan").forGetter(source -> source.jardin),
            Biome.CODEC.fieldOf("port_royal").forGetter(source -> source.portRoyal),
            Biome.CODEC.fieldOf("puy_de_dome").forGetter(source -> source.puy),
            Biome.CODEC.fieldOf("hippone").forGetter(source -> source.hippone)
    ).apply(instance, SourceBiomesRoyaume::new));

    /** Volume de la Caverne : ellipsoïde centré sous (0, 0), de y ≈ 39 à 91 et d'environ 70 blocs de rayon. */
    public static final int CAVERNE_Y = 65;
    public static final int CAVERNE_RAYON = 70;
    public static final int CAVERNE_DEMI_HAUTEUR = 26;

    private final Holder<Biome> caverne;
    private final Holder<Biome> jardin;
    private final Holder<Biome> portRoyal;
    private final Holder<Biome> puy;
    private final Holder<Biome> hippone;

    public SourceBiomesRoyaume(Holder<Biome> caverne, Holder<Biome> jardin, Holder<Biome> portRoyal, Holder<Biome> puy, Holder<Biome> hippone) {
        this.caverne = caverne;
        this.jardin = jardin;
        this.portRoyal = portRoyal;
        this.puy = puy;
        this.hippone = hippone;
    }

    /** Vrai si le bloc (x, y, z) est dans le volume de la Caverne de Platon. */
    public static boolean dansCaverne(int x, int y, int z) {
        double horizontal = ((double) x * x + (double) z * z) / ((double) CAVERNE_RAYON * CAVERNE_RAYON);
        double vertical = (double) (y - CAVERNE_Y) / CAVERNE_DEMI_HAUTEUR;
        return horizontal + vertical * vertical < 1;
    }

    @Override
    protected MapCodec<? extends BiomeSource> codec() {
        return CODEC;
    }

    @Override
    protected Stream<Holder<Biome>> collectPossibleBiomes() {
        return Stream.of(caverne, jardin, portRoyal, puy, hippone);
    }

    @Override
    public Holder<Biome> getNoiseBiome(int quartX, int quartY, int quartZ, Climate.Sampler echantillonneur) {
        int x = QuartPos.toBlock(quartX);
        int y = QuartPos.toBlock(quartY);
        int z = QuartPos.toBlock(quartZ);
        if (dansCaverne(x, y, z)) {
            return caverne;
        }
        return switch (ReliefRoyaume.zone(x, z)) {
            case JARDIN_MILAN -> jardin;
            case PORT_ROYAL -> portRoyal;
            case PUY_DE_DOME -> puy;
            case HIPPONE -> hippone;
        };
    }
}
