package com.royaumedesidees.mixin;

import net.minecraft.client.gui.Gui;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Les messages au-dessus de la barre d'objets restent 7 secondes au lieu de 3 : on a le temps de les lire
 * (demande de Maxime, v0.3).
 */
@Mixin(Gui.class)
public abstract class GuiMixin {
    @Shadow
    private int overlayMessageTime;

    @Inject(method = "setOverlayMessage", at = @At("TAIL"))
    private void royaumedesidees$plusLongtemps(Component message, boolean couleurAnimee, CallbackInfo info) {
        overlayMessageTime = 140;
    }
}
