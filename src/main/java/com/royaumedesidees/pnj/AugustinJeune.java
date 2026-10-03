package com.royaumedesidees.pnj;

import com.royaumedesidees.registre.ModItems;
import com.royaumedesidees.registre.ModSons;
import net.minecraft.ChatFormatting;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Augustin jeune, dans sa villa de Milan : un fêtard qui adore voler des poires. Il tient toujours une poire volée.
 * Frappé, il rit. Sa quête de conversion arrive à l'étape 4 de la v0.3.
 */
public class AugustinJeune extends PnjRoyaume {
    public AugustinJeune(EntityType<? extends AugustinJeune> type, Level niveau) {
        super(type, niveau, ChatFormatting.GOLD);
        tenir(new ItemStack(ModItems.POIRE_VOLEE.get()));
    }

    @Override
    protected void reagirCoup(ServerPlayer joueur) {
        level().playSound(null, this, ModSons.AUGUSTIN_RIRE.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);
        parler(joueur, "coup");
    }
}
