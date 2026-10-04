package com.royaumedesidees.pnj;

import com.royaumedesidees.registre.ModItems;

import com.royaumedesidees.structures.StructuresJardin;
import net.minecraft.ChatFormatting;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Augustin jeune, le fêtard des Confessions (livre II) : il traîne dans l'allée des vergers, près de la vigne de son
 * père, une poire volée à la main. Quand quelqu'un vole une poire près de lui, il accourt et en redemande ; il donne la
 * quête de conversion ({@link QueteConversion}). Frappé, il rit.
 */
public class AugustinJeune extends PnjRoyaume {
    /** Pendant qu'il accourt vers un voleur, il peut s'éloigner de chez lui (10 s). */
    private long finCourse;

    public AugustinJeune(EntityType<? extends AugustinJeune> type, Level niveau) {
        super(type, niveau, ChatFormatting.GOLD);
        tenir(new ItemStack(ModItems.POIRE_VOLEE.get()));
    }

    @Override
    protected void parleAvec(ServerPlayer joueur) {
        QueteConversion.parlerAugustin(this, joueur);
    }

    @Override
    protected void reagirCoup(ServerPlayer joueur) {
        rire(1.0F);
        parler(joueur, "coup");
    }

    /** Un éclat de rire de jeune homme ({@code augustin_rire.ogg}, fourni par Maxime). */
    private void rire(float volume) {
        level().playSound(null, this, com.royaumedesidees.registre.ModSons.AUGUSTIN_RIRE.get(), SoundSource.NEUTRAL, volume,
                0.95F + getRandom().nextFloat() * 0.1F);
    }

    /** Court rejoindre un voleur de poires. */
    public void accourir(ServerPlayer joueur) {
        clearRestriction();
        finCourse = level().getGameTime() + 200;
        getNavigation().moveTo(joueur, 0.8);
        getLookControl().setLookAt(joueur);
        rire(0.8F);
    }

    @Override
    public void tick() {
        super.tick();
        // Il habitait l'atrium de la villa avant de s'installer dans l'allée des vergers : l'ancien s'efface, le
        // nouveau apparaît dans l'allée (voir MaisonsPnj).
        if (!level().isClientSide && StructuresJardin.POS_ATRIUM.equals(maison())) {
            discard();
            return;
        }
        if (!level().isClientSide && finCourse != 0 && level().getGameTime() >= finCourse && maison() != null) {
            finCourse = 0;
            restrictTo(maison(), rayonMaison());
        }
    }
}
