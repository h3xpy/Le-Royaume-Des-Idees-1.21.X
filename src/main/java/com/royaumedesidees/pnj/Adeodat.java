package com.royaumedesidees.pnj;

import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * Adéodat, le fils d'Augustin, quinze ans et plus intelligent que tout le monde (Confessions, IX, 6). Plus petit que
 * les adultes (attribut de taille). Frappé, il soupire. Ses défis de calcul arrivent à l'étape 4 de la v0.3.
 */
public class Adeodat extends PnjRoyaume {
    public Adeodat(EntityType<? extends Adeodat> type, Level niveau) {
        super(type, niveau, ChatFormatting.AQUA);
    }
}
