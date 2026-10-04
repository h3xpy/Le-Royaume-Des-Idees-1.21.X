package com.royaumedesidees.pnj;

import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/** Petites actions différées de quelques ticks (une suite de notes, trois coups de cloche). Côté serveur. */
public final class Echeancier {
    private record Tache(long tick, Runnable action) {
    }

    private static final List<Tache> TACHES = new ArrayList<>();
    private static long maintenant;

    private Echeancier() {
    }

    public static void plusTard(int ticks, Runnable action) {
        TACHES.add(new Tache(maintenant + ticks, action));
    }

    public static void tick(ServerTickEvent.Post evenement) {
        maintenant++;
        List<Runnable> pretes = new ArrayList<>();
        Iterator<Tache> iterateur = TACHES.iterator();
        while (iterateur.hasNext()) {
            Tache tache = iterateur.next();
            if (tache.tick() <= maintenant) {
                iterateur.remove();
                pretes.add(tache.action());
            }
        }
        pretes.forEach(Runnable::run);
    }
}
