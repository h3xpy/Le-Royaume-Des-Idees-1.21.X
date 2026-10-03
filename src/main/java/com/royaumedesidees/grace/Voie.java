package com.royaumedesidees.grace;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

/**
 * Les deux Voies (classes) du Royaume, plus l'absence de Voie. Les noms sérialisés sont définitifs : ils sont écrits
 * dans les sauvegardes des joueurs.
 */
public enum Voie implements StringRepresentable {
    AUCUNE("aucune"),
    /** Voie de la Raison, donnée par Pascal. */
    RAISON("raison"),
    /** Voie du Cœur, donnée au baptême par Ambroise, à la fin de la quête de conversion. */
    COEUR("coeur");

    public static final Codec<Voie> CODEC = StringRepresentable.fromEnum(Voie::values);

    private final String nom;

    Voie(String nom) {
        this.nom = nom;
    }

    @Override
    public String getSerializedName() {
        return nom;
    }
}
