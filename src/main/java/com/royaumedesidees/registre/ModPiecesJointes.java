package com.royaumedesidees.registre;

import com.mojang.serialization.Codec;
import com.royaumedesidees.RoyaumeDesIdees;
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

    private ModPiecesJointes() {
    }
}
