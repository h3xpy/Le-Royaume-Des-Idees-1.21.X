package com.royaumedesidees.pnj;

import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * Sainte Monique, la mère d'Augustin, qui a prié et pleuré des années pour sa conversion. Frappée, elle pleure plus
 * fort. Elle suivra les pécheurs à l'étape 6 de la v0.3.
 */
public class Monique extends PnjRoyaume {
    public Monique(EntityType<? extends Monique> type, Level niveau) {
        super(type, niveau, ChatFormatting.LIGHT_PURPLE);
    }
}
