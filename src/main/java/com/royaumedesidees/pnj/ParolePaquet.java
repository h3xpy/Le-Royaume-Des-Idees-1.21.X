package com.royaumedesidees.pnj;

import com.royaumedesidees.RoyaumeDesIdees;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Consumer;

/**
 * Du serveur vers le client : une réplique de PNJ, affichée dans la boîte de dialogue en haut de l'écran
 * ({@code client/BoiteDialogue}) au lieu du chat. {@code ticks} vaut 0 pour une durée calculée d'après la longueur
 * du texte, ou la durée voulue pour une question qu'il faut garder sous les yeux (somme de Pascal, défi d'Adéodat).
 */
public record ParolePaquet(Component nom, Component texte, int ticks) implements CustomPacketPayload {
    public static final Type<ParolePaquet> TYPE = new Type<>(RoyaumeDesIdees.id("parole"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ParolePaquet> CODEC = StreamCodec.composite(
            ComponentSerialization.TRUSTED_STREAM_CODEC, ParolePaquet::nom,
            ComponentSerialization.TRUSTED_STREAM_CODEC, ParolePaquet::texte,
            ByteBufCodecs.VAR_INT, ParolePaquet::ticks,
            ParolePaquet::new);

    /** Répliques reçues, pas encore prises par la boîte de dialogue (côté client). */
    private static final Deque<ParolePaquet> RECUES = new ArrayDeque<>();
    /** Pour la visite de développement : reçoit le texte de chaque réplique. */
    public static volatile Consumer<String> ecouteur;

    public static void recevoir(ParolePaquet paquet, IPayloadContext contexte) {
        synchronized (RECUES) {
            RECUES.add(paquet);
        }
        Consumer<String> e = ecouteur;
        if (e != null) {
            e.accept(paquet.texte().getString());
        }
    }

    /** La plus ancienne réplique reçue, ou null. */
    public static ParolePaquet prendre() {
        synchronized (RECUES) {
            return RECUES.poll();
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
