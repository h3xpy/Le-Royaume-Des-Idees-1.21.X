package com.royaumedesidees.structures;

import net.minecraft.world.level.levelgen.structure.BoundingBox;

import java.util.function.Consumer;

/**
 * Une structure du Royaume, posée par le mod (jamais par la génération du monde).
 *
 * @param id          identifiant définitif (sert de clé dans la sauvegarde)
 * @param version     numéro de version : l'augmenter fait reposer la structure dans les mondes existants
 * @param boite       boîte englobante, à coordonnées fixes ; rien n'est jamais modifié hors d'elle
 * @param constructeur code qui pose les blocs à travers une {@link Pose}
 */
public record StructureRoyaume(String id, int version, BoundingBox boite, Consumer<Pose> constructeur) {
}
