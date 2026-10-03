package com.royaumedesidees.grace;

import com.royaumedesidees.RoyaumeDesIdees;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * Du serveur vers le client : la Grâce du joueur, pour la jauge. Le client ne fait que l'afficher ; tout le calcul
 * reste sur le serveur.
 */
public record GracePaquet(int valeur) implements CustomPacketPayload {
    public static final Type<GracePaquet> TYPE = new Type<>(RoyaumeDesIdees.id("grace"));
    public static final StreamCodec<ByteBuf, GracePaquet> CODEC =
            StreamCodec.composite(ByteBufCodecs.VAR_INT, GracePaquet::valeur, GracePaquet::new);

    /** Dernière valeur reçue (côté client) ; -1 tant que le serveur n'a rien envoyé. */
    private static volatile int recue = -1;

    public static int recue() {
        return recue;
    }

    public static void recevoir(GracePaquet paquet, IPayloadContext contexte) {
        recue = paquet.valeur();
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
