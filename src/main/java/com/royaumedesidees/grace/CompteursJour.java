package com.royaumedesidees.grace;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * Ce qu'un joueur a déjà fait aujourd'hui (jour de jeu de l'Overworld), pour les gains de Grâce limités par jour.
 *
 * @param jour     numéro du jour de jeu auquel ces compteurs se rapportent
 * @param monique  vrai si le joueur a déjà nourri Monique aujourd'hui
 * @param frappes  nombre de fois où il a déjà gagné de la Grâce en se laissant frapper sans riposter
 */
public record CompteursJour(long jour, boolean monique, int frappes) {
    public static final CompteursJour VIDE = new CompteursJour(-1, false, 0);

    public static final Codec<CompteursJour> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.LONG.fieldOf("jour").forGetter(CompteursJour::jour),
            Codec.BOOL.fieldOf("monique").forGetter(CompteursJour::monique),
            Codec.INT.fieldOf("frappes").forGetter(CompteursJour::frappes)
    ).apply(instance, CompteursJour::new));

    /** Les compteurs pour le jour donné : remis à zéro si le jour a changé. */
    public CompteursJour pour(long aujourdhui) {
        return jour == aujourdhui ? this : new CompteursJour(aujourdhui, false, 0);
    }
}
