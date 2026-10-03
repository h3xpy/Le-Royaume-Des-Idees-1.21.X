package com.royaumedesidees.jardin;

import com.royaumedesidees.RoyaumeDesIdees;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.common.EffectCure;

import java.util.Set;

/**
 * L'effet de statut « Culpabilité », niveaux I à V (amplificateur 0 à 4). Il ne sert qu'à l'affichage et à la
 * lenteur légère : le vrai niveau est gardé par {@link Culpabilite}, qui remet l'effet s'il disparaît.
 * Rien ne l'efface (ni le lait, ni le miel) : seule la confession le fait.
 */
public class CulpabiliteEffet extends MobEffect {
    public CulpabiliteEffet() {
        super(MobEffectCategory.HARMFUL, 0x5a5a6e);
        // Lenteur légère, la même quel que soit le niveau : -10 % de vitesse.
        addAttributeModifier(Attributes.MOVEMENT_SPEED, RoyaumeDesIdees.id("effect.culpabilite"),
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL, niveau -> -0.10);
    }

    @Override
    public void fillEffectCures(Set<EffectCure> remedes, MobEffectInstance instance) {
        // Aucun remède : on laisse l'ensemble vide.
    }
}
