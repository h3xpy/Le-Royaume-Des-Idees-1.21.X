package com.royaumedesidees.client;

import com.royaumedesidees.RoyaumeDesIdees;
import com.royaumedesidees.pnj.PnjRoyaume;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Rendu des PNJ du Royaume : le modèle du joueur (bras fins pour Monique), avec un skin généré par script
 * ({@code tools/textures/pnj_v03.py}) dans {@code textures/entity/pnj/<id>.png}. La taille (Adéodat, plus petit)
 * vient de l'attribut de taille du jeu.
 */
public class RenduPnj<T extends PnjRoyaume> extends HumanoidMobRenderer<T, PlayerModel<T>> {
    public RenduPnj(EntityRendererProvider.Context contexte, boolean brasFins) {
        super(contexte, new PlayerModel<>(contexte.bakeLayer(brasFins ? ModelLayers.PLAYER_SLIM : ModelLayers.PLAYER), brasFins), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(T pnj) {
        return RoyaumeDesIdees.id("textures/entity/pnj/" + pnj.id() + ".png");
    }
}
