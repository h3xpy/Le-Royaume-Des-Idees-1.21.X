package com.royaumedesidees.registre;

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

    private ModPiecesJointes() {
    }
}
