package com.royaumedesidees.client;

import com.royaumedesidees.RoyaumeDesIdees;
import com.royaumedesidees.entite.Ombre;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Rendu de l'Ombre sur le modèle humanoïde du jeu : une silhouette noire semi-transparente, ou le prisonnier
 * en couleur une fois révélée par la Lanterne. Une ombre n'a pas d'ombre au sol.
 */
public class RenduOmbre extends HumanoidMobRenderer<Ombre, HumanoidModel<Ombre>> {
    public static final ModelLayerLocation COUCHE = new ModelLayerLocation(RoyaumeDesIdees.id("ombre"), "main");
    private static final ResourceLocation SILHOUETTE = RoyaumeDesIdees.id("textures/entity/ombre.png");
    private static final ResourceLocation REVELEE = RoyaumeDesIdees.id("textures/entity/ombre_revelee.png");

    public RenduOmbre(EntityRendererProvider.Context contexte) {
        super(contexte, new HumanoidModel<>(contexte.bakeLayer(COUCHE)), 0.0F);
    }

    public static LayerDefinition couche() {
        return LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 64);
    }

    @Override
    public ResourceLocation getTextureLocation(Ombre ombre) {
        return ombre.estRevelee() ? REVELEE : SILHOUETTE;
    }

    @Override
    protected RenderType getRenderType(Ombre ombre, boolean visible, boolean translucide, boolean lumineux) {
        if (!ombre.estRevelee()) {
            // La texture de la silhouette est semi-transparente : il faut un rendu qui respecte la transparence.
            return RenderType.entityTranslucent(SILHOUETTE);
        }
        return super.getRenderType(ombre, visible, translucide, lumineux);
    }
}
