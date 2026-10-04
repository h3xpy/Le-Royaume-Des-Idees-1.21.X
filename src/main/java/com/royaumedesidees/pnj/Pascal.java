package com.royaumedesidees.pnj;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

/**
 * Blaise Pascal, qui porte sous ses habits une ceinture de fer à pointes et l'enfonce quand il se sent fier (récit de
 * sa sœur Gilberte). Frappé, il dit merci, et tout le serveur l'apprend. Il tient les comptes de son père ; la quête
 * des impôts arrive à l'étape 5 de la v0.3.
 */
public class Pascal extends PnjRoyaume {
    /** Le merci public n'est annoncé qu'une fois toutes les 10 secondes, pour ne pas inonder le chat. */
    private static final int DELAI_ANNONCE = 200;
    private long derniereAnnonce = -DELAI_ANNONCE;

    public Pascal(EntityType<? extends Pascal> type, Level niveau) {
        super(type, niveau, ChatFormatting.DARK_AQUA);
        tenir(new ItemStack(Items.PAPER));
    }

    @Override
    protected void reagirCoup(ServerPlayer joueur) {
        parler(joueur, "coup");
        long maintenant = level().getGameTime();
        if (maintenant - derniereAnnonce >= DELAI_ANNONCE) {
            derniereAnnonce = maintenant;
            joueur.server.getPlayerList().broadcastSystemMessage(
                    Component.translatable("message.royaumedesidees.pascal.merci", joueur.getDisplayName()), false);
        }
    }
}
