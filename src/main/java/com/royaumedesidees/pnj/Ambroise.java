package com.royaumedesidees.pnj;

import com.royaumedesidees.registre.ModSons;
import net.minecraft.ChatFormatting;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

/**
 * Saint Ambroise, évêque de Milan, qui lit en silence sans bouger les lèvres (Confessions, VI, 3). Il tient un livre
 * et ne répond que par des gestes, sauf pour baptiser : c'est lui qui baptisa Augustin à Pâques 387 (IX, 6), et
 * c'est lui qui donne la Voie du Cœur à la fin de la quête de conversion. Frappé, il tourne une page.
 */
public class Ambroise extends PnjRoyaume {
    public Ambroise(EntityType<? extends Ambroise> type, Level niveau) {
        super(type, niveau, ChatFormatting.WHITE);
        tenir(new ItemStack(Items.BOOK));
    }

    @Override
    protected void parleAvec(ServerPlayer joueur) {
        if (!QueteConversion.bapteme(this, joueur)) {
            super.parleAvec(joueur);
        }
    }

    @Override
    protected void reagirCoup(ServerPlayer joueur) {
        level().playSound(null, this, ModSons.AMBROISE_PAGE.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);
        parler(joueur, "coup");
    }
}
