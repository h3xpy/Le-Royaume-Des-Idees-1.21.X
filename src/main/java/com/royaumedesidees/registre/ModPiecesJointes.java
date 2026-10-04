package com.royaumedesidees.registre;

import com.mojang.serialization.Codec;
import com.royaumedesidees.RoyaumeDesIdees;
import com.royaumedesidees.grace.CompteursJour;
import com.royaumedesidees.grace.Voie;
import com.royaumedesidees.portail.PointRetour;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

/** Données attachées aux joueurs, sauvegardées avec eux et conservées à la mort. Identifiants définitifs. */
public final class ModPiecesJointes {
    public static final DeferredRegister<AttachmentType<?>> PIECES_JOINTES =
            DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, RoyaumeDesIdees.MODID);

    /** Portail de bibliothèques par lequel le joueur est entré dans le Royaume. */
    public static final Supplier<AttachmentType<PointRetour>> POINT_RETOUR = PIECES_JOINTES.register("point_retour",
            () -> AttachmentType.builder(() -> PointRetour.AUCUN).serialize(PointRetour.CODEC).copyOnDeath().build());

    /** Vrai une fois que le joueur est arrivé enchaîné dans la Caverne (les fois suivantes, il arrive libre). */
    public static final Supplier<AttachmentType<Boolean>> ENCHAINE = PIECES_JOINTES.register("enchaine",
            () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).copyOnDeath().build());

    /** Vrai une fois que le joueur est sorti de la Caverne et a reçu la Lanterne de Diogène. */
    public static final Supplier<AttachmentType<Boolean>> SORTIE_FAITE = PIECES_JOINTES.register("sortie_faite",
            () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).copyOnDeath().build());

    /** Niveau de Culpabilité, de 0 à 5. Conservé à la mort : on ne meurt pas de ses péchés. */
    public static final Supplier<AttachmentType<Integer>> CULPABILITE = PIECES_JOINTES.register("culpabilite",
            () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).copyOnDeath().build());

    /** Vrai une fois que le joueur a entendu « Prends, lis » en touchant un figuier. */
    public static final Supplier<AttachmentType<Boolean>> FIGUIER_ENTENDU = PIECES_JOINTES.register("figuier_entendu",
            () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).copyOnDeath().build());

    // ------------------------------------------------------------------ v0.3 : Grâce, Voies, quêtes, Monique

    /** Grâce, de 0 à 100 (voir {@link com.royaumedesidees.grace.Grace}). */
    public static final Supplier<AttachmentType<Integer>> GRACE = PIECES_JOINTES.register("grace",
            () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).copyOnDeath().build());

    /** Voie choisie : aucune, Raison ou Cœur. */
    public static final Supplier<AttachmentType<Voie>> VOIE = PIECES_JOINTES.register("voie",
            () -> AttachmentType.builder(() -> Voie.AUCUNE).serialize(Voie.CODEC).copyOnDeath().build());

    /** Étape atteinte dans la quête de conversion d'Augustin (0 = pas commencée). */
    public static final Supplier<AttachmentType<Integer>> QUETE_CONVERSION = PIECES_JOINTES.register("quete_conversion",
            () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).copyOnDeath().build());

    /** Étape atteinte dans la quête des impôts de Pascal (0 = pas commencée). */
    public static final Supplier<AttachmentType<Integer>> QUETE_IMPOTS = PIECES_JOINTES.register("quete_impots",
            () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).copyOnDeath().build());

    /** Vrai une fois que le joueur a tenu 60 s de silence près du pupitre d'Ambroise : il sait fabriquer le pupitre. */
    public static final Supplier<AttachmentType<Boolean>> PUPITRE_APPRIS = PIECES_JOINTES.register("pupitre_appris",
            () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).copyOnDeath().build());

    /** Vrai une fois que Monique est apparue pour ce joueur. */
    public static final Supplier<AttachmentType<Boolean>> MONIQUE_APPARUE = PIECES_JOINTES.register("monique_apparue",
            () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).copyOnDeath().build());

    /** Gains de Grâce limités par jour de jeu (Monique nourrie, coups reçus sans riposter). */
    public static final Supplier<AttachmentType<CompteursJour>> COMPTEURS_JOUR = PIECES_JOINTES.register("compteurs_jour",
            () -> AttachmentType.builder(() -> CompteursJour.VIDE).serialize(CompteursJour.CODEC).copyOnDeath().build());

    /** Astuces déjà montrées au joueur (un bit par astuce, voir {@link com.royaumedesidees.pnj.Astuces}). */
    public static final Supplier<AttachmentType<Integer>> ASTUCES = PIECES_JOINTES.register("astuces",
            () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).copyOnDeath().build());

    private ModPiecesJointes() {
    }
}
