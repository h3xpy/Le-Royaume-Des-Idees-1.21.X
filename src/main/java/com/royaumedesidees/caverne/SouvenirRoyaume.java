package com.royaumedesidees.caverne;

import com.royaumedesidees.registre.ModMonde;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

/**
 * Règle « Souvenir du Royaume » : les objets du tag {@code royaumedesidees:lie_au_royaume} n'ont d'effet que dans
 * la dimension du Royaume. Ailleurs, ce ne sont que des souvenirs inertes, et ils se réactivent au retour.
 */
public final class SouvenirRoyaume {
    private SouvenirRoyaume() {
    }

    /** Vrai si les objets liés au Royaume fonctionnent dans ce niveau. */
    public static boolean actif(@Nullable Level niveau) {
        return niveau != null && niveau.dimension().equals(ModMonde.ROYAUME);
    }
}
