package com.royaumedesidees.portail;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

/**
 * Où ramener un joueur qui ressort du Royaume : la position où il est entré dans le portail de bibliothèques.
 *
 * @param defini    faux tant que le joueur n'est jamais passé par un portail
 * @param dimension dimension du portail de départ (en général l'Overworld)
 * @param pos       position du joueur dans le portail de départ
 * @param yRot      orientation du joueur au départ
 */
public record PointRetour(boolean defini, ResourceKey<Level> dimension, BlockPos pos, float yRot) {
    public static final PointRetour AUCUN = new PointRetour(false, Level.OVERWORLD, BlockPos.ZERO, 0);

    public static final Codec<PointRetour> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.fieldOf("defini").forGetter(PointRetour::defini),
            ResourceKey.codec(Registries.DIMENSION).fieldOf("dimension").forGetter(PointRetour::dimension),
            BlockPos.CODEC.fieldOf("pos").forGetter(PointRetour::pos),
            Codec.FLOAT.fieldOf("orientation").forGetter(PointRetour::yRot)
    ).apply(instance, PointRetour::new));
}
