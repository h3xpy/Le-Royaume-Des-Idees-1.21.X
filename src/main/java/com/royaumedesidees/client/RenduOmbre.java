package com.royaumedesidees.client;

import com.royaumedesidees.RoyaumeDesIdees;
import com.royaumedesidees.entite.Ombre;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Rendu de l'Ombre sur le modèle humanoïde du jeu. Non révélée : une silhouette noire semi-transparente, aplatie
 * selon l'axe nord-sud pour ressembler à une ombre projetée sur la paroi (vue de face, on voit exactement la
 * silhouette ; vue de biais, une découpe plate), une fois et demie plus grande que la statue, comme une ombre portée
 * par un feu proche. Révélée : une statue de bois en volume, à sa vraie taille. Une ombre n'a pas d'ombre au sol.
 */
public class RenduOmbre extends HumanoidMobRenderer<Ombre, HumanoidModel<Ombre>> {
    public static final ModelLayerLocation COUCHE = new ModelLayerLocation(RoyaumeDesIdees.id("ombre"), "main");
    private static final ResourceLocation SILHOUETTE = RoyaumeDesIdees.id("textures/entity/ombre.png");
    private static final ResourceLocation REVELEE = RoyaumeDesIdees.id("textures/entity/ombre_revelee.png");
    /** Épaisseur de l'ombre projetée, selon l'axe du mur des ombres. */
    private static final float EPAISSEUR_OMBRE = 0.06F;
    /** Projetée par un feu proche des statues, l'ombre est plus grande que la statue elle-même. */
    private static final float AGRANDISSEMENT_OMBRE = 1.5F;

    public RenduOmbre(EntityRendererProvider.Context contexte) {
        super(contexte, new HumanoidModel<>(contexte.bakeLayer(COUCHE)), 0.0F);
    }

    public static LayerDefinition couche() {
        return LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 64);
    }

    @Override
    public void render(Ombre ombre, float orientation, float partiel, PoseStack pose, MultiBufferSource tampons, int lumiere) {
        if (ombre.estRevelee()) {
            super.render(ombre, orientation, partiel, pose, tampons, lumiere);
            return;
        }
        // Une ombre projetée se voit toujours de face, depuis les prisonniers : on la tourne vers le sud
        // (orientation 0) pendant qu'elle glisse le long de l'écran. Seul l'affichage est concerné.
        ombre.yBodyRot = ombre.yBodyRotO = ombre.yHeadRot = ombre.yHeadRotO = 0.0F;
        pose.pushPose();
        pose.scale(AGRANDISSEMENT_OMBRE, AGRANDISSEMENT_OMBRE, EPAISSEUR_OMBRE);
        super.render(ombre, orientation, partiel, pose, tampons, lumiere);
        pose.popPose();
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
