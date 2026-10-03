package com.royaumedesidees.caverne;

import com.royaumedesidees.RoyaumeDesIdees;
import com.royaumedesidees.monde.CaverneRoyaume;
import com.royaumedesidees.monde.ReliefRoyaume;
import com.royaumedesidees.monde.SourceBiomesRoyaume;
import com.royaumedesidees.registre.ModItems;
import com.royaumedesidees.registre.ModMonde;
import com.royaumedesidees.registre.ModPiecesJointes;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Sortie de la Caverne vers la lumière. Quand un joueur qui était dans la Caverne ou le tunnel arrive à l'air libre
 * au débouché du tunnel : 5 secondes d'aveuglement, un message, le succès « Allégorie vécue », et à la toute
 * première sortie, la Lanterne de Diogène. En redescendant ensuite dans la salle, quelques secondes de ténèbres.
 */
public final class SortieCaverne {
    public static final net.minecraft.resources.ResourceLocation SUCCES = RoyaumeDesIdees.id("royaume/allegorie_vecue");
    /** Débouché du tunnel : vers z = 125. La zone de sortie couvre l'air libre autour. */
    private static final int SORTIE_Z_MIN = 100;
    private static final int SORTIE_DEMI_LARGEUR = 12;

    /** Joueurs qui sont passés par la Caverne ou le tunnel depuis leur dernière sortie (mémoire du serveur). */
    private static final Set<UUID> SOUS_TERRE = new HashSet<>();
    /** Joueurs sortis à la lumière, qui n'ont pas encore redescendu dans la Caverne. */
    private static final Set<UUID> A_LA_LUMIERE = new HashSet<>();
    /** Durée des ténèbres au retour dans la Caverne : le temps que les yeux se réhabituent. */
    private static final int DUREE_TENEBRES = 200;

    private SortieCaverne() {
    }

    public static void tick(PlayerTickEvent.Post evenement) {
        if (!(evenement.getEntity() instanceof ServerPlayer joueur) || !joueur.level().dimension().equals(ModMonde.ROYAUME)
                || joueur.tickCount % 5 != 0 || joueur.isSpectator()) {
            return;
        }
        int x = joueur.getBlockX();
        int y = joueur.getBlockY();
        int z = joueur.getBlockZ();
        int surface = ReliefRoyaume.colonne(x, z).surface();
        if (y < surface - 1 && (SourceBiomesRoyaume.dansCaverne(x, y, z) || CaverneRoyaume.paroiTunnel(x, y, z))) {
            SOUS_TERRE.add(joueur.getUUID());
            if (SourceBiomesRoyaume.dansCaverne(x, y, z) && A_LA_LUMIERE.remove(joueur.getUUID())) {
                redescendre(joueur);
            }
        } else if (y >= surface && z >= SORTIE_Z_MIN && Math.abs(x) <= SORTIE_DEMI_LARGEUR && SOUS_TERRE.remove(joueur.getUUID())) {
            sortir(joueur);
        }
    }

    /**
     * Comme le prisonnier de Platon qui redescend dans la caverne, « les yeux pleins de ténèbres » : quelques
     * secondes d'obscurité totale en revenant dans la salle après être sorti.
     */
    private static void redescendre(ServerPlayer joueur) {
        joueur.addEffect(new MobEffectInstance(MobEffects.DARKNESS, DUREE_TENEBRES, 0, false, false, true));
        joueur.displayClientMessage(Component.translatable("message.royaumedesidees.caverne.tenebres"), false);
    }

    /** La lumière du jour : aveuglement, message, succès, et la Lanterne la première fois. */
    public static void sortir(ServerPlayer joueur) {
        A_LA_LUMIERE.add(joueur.getUUID());
        joueur.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 0, false, false, true));
        joueur.sendSystemMessage(Component.translatable("message.royaumedesidees.caverne.lumiere"));
        AdvancementHolder succes = joueur.server.getAdvancements().get(SUCCES);
        if (succes != null) {
            joueur.getAdvancements().award(succes, "sortie");
        }
        if (!joueur.getData(ModPiecesJointes.SORTIE_FAITE)) {
            joueur.setData(ModPiecesJointes.SORTIE_FAITE, true);
            donner(joueur, new ItemStack(ModItems.LANTERNE_DIOGENE.get()));
            joueur.sendSystemMessage(Component.translatable("message.royaumedesidees.caverne.lanterne"));
        }
    }

    private static void donner(Player joueur, ItemStack pile) {
        if (!joueur.getInventory().add(pile)) {
            joueur.drop(pile, false);
        }
    }
}
