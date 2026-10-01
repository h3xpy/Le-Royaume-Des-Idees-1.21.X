package com.royaumedesidees.monde;

import java.util.Random;

/**
 * Bruit de Perlin 2D à graine fixe, écrit à la main pour que le relief du Royaume ne dépende
 * ni de la seed du monde ni de l'implémentation du bruit de Minecraft (qui peut changer d'une
 * version à l'autre).
 *
 * <p><b>Ne jamais modifier ce fichier après la v0.1</b> : changer un seul calcul déplacerait le relief
 * des chunks encore inexplorés et créerait des falaises aux limites des zones déjà visitées.
 * {@link Random} a un algorithme fixé par la spécification Java, et on n'utilise que + - * / et floor,
 * dont le résultat est identique sur toutes les machines.
 */
public final class BruitRoyaume {
    private final int[] permutation = new int[512];

    public BruitRoyaume(long graine) {
        Random alea = new Random(graine);
        int[] base = new int[256];
        for (int i = 0; i < 256; i++) {
            base[i] = i;
        }
        for (int i = 255; i > 0; i--) {
            int j = alea.nextInt(i + 1);
            int echange = base[i];
            base[i] = base[j];
            base[j] = echange;
        }
        for (int i = 0; i < 512; i++) {
            permutation[i] = base[i & 255];
        }
    }

    /** Bruit de Perlin en (x, z), entre -1 et 1 environ. */
    public double bruit(double x, double z) {
        double fx = Math.floor(x);
        double fz = Math.floor(z);
        int ix = (int) fx & 255;
        int iz = (int) fz & 255;
        double dx = x - fx;
        double dz = z - fz;
        double u = lissage(dx);
        double v = lissage(dz);
        int a = permutation[ix] + iz;
        int b = permutation[ix + 1] + iz;
        double haut = interpoler(u, gradient(permutation[a], dx, dz), gradient(permutation[b], dx - 1, dz));
        double bas = interpoler(u, gradient(permutation[a + 1], dx, dz - 1), gradient(permutation[b + 1], dx - 1, dz - 1));
        return interpoler(v, haut, bas) * 1.4142;
    }

    /** Somme d'octaves de bruit (relief plus naturel), normalisée entre -1 et 1 environ. */
    public double fbm(double x, double z, int octaves) {
        double somme = 0;
        double amplitude = 1;
        double total = 0;
        for (int i = 0; i < octaves; i++) {
            somme += bruit(x, z) * amplitude;
            total += amplitude;
            x *= 2;
            z *= 2;
            amplitude *= 0.5;
        }
        return somme / total;
    }

    private static double lissage(double t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    private static double interpoler(double t, double a, double b) {
        return a + t * (b - a);
    }

    private static double gradient(int hachage, double x, double z) {
        return switch (hachage & 7) {
            case 0 -> x + z;
            case 1 -> -x + z;
            case 2 -> x - z;
            case 3 -> -x - z;
            case 4 -> x;
            case 5 -> -x;
            case 6 -> z;
            default -> -z;
        };
    }
}
