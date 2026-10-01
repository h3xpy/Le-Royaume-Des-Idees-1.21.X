package com.royaumedesidees.monde;

/**
 * Le relief de l'île du Royaume, calculé uniquement à partir des coordonnées (x, z).
 *
 * <p>Cette classe n'utilise rien de Minecraft : on peut l'appeler hors du jeu pour dessiner la carte.
 * <b>Ne plus la modifier après la v0.1</b> (voir CLAUDE.md) : le relief des chunks déjà générés ne
 * change jamais, donc toute modification créerait des marches aux limites des zones explorées.
 *
 * <p>Le nord est vers les z négatifs, l'est vers les x positifs.
 */
public final class ReliefRoyaume {
    public static final int NIVEAU_MER = 80;
    /** Rayon moyen de l'île (environ 1 300 blocs de diamètre). */
    public static final int RAYON_ILE = 650;

    /** Plaine au-dessus de la Caverne, au centre de l'île (l'Autel y sera posé). */
    public static final int CENTRE_PLAINE = 100;

    /** Port-Royal : centre du fond de la vallée, où sera nichée l'abbaye. */
    public static final int VALLEE_X = 300;
    public static final int VALLEE_Z = -290;

    /** Puy de Dôme : dôme de lave sans cratère, arrondi au sommet. */
    public static final int DOME_X = 260;
    public static final int DOME_Z = 260;
    public static final double DOME_RAYON = 230;
    public static final int DOME_SOMMET = 300;
    /** Au-dessus de cette hauteur, une fine couche de neige couvre l'herbe du dôme. */
    public static final int NEIGE_Y = 272;
    /** Petits puys à cratère autour du dôme, comme le Pariou : x, z, rayon, hauteur au-dessus de la plaine. */
    public static final int[][] PETITS_PUYS = {{470, 120, 80, 60}, {110, 460, 70, 50}, {440, 330, 55, 40}};

    /** Hippone : mer intérieure, et ville sur un plateau bas au bord de l'eau. */
    public static final int MER_X = -330;
    public static final int MER_Z = 320;
    public static final double MER_RAYON = 130;
    public static final int VILLE_X = -208;
    public static final int VILLE_Z = 203;
    public static final int VILLE_HAUTEUR = 88;

    /** Îlots flottants détachés de l'île : angle (degrés, 0 = est, 90 = sud), distance au centre, rayon, hauteur du sol. */
    public static final int[][] ILOTS = {
            {20, 790, 22, 110}, {75, 770, 16, 96}, {130, 800, 28, 125}, {200, 780, 18, 100},
            {250, 815, 30, 140}, {305, 770, 14, 92}, {340, 830, 24, 118}};

    // Une graine fixe par usage : changer l'une d'elles changerait toute la carte.
    private static final BruitRoyaume BRUIT_BORD = new BruitRoyaume(1623L);
    private static final BruitRoyaume BRUIT_COLLINES = new BruitRoyaume(354L);
    private static final BruitRoyaume BRUIT_DETAIL = new BruitRoyaume(430L);
    private static final BruitRoyaume BRUIT_MARAIS = new BruitRoyaume(1656L);
    private static final BruitRoyaume BRUIT_ROCHE = new BruitRoyaume(1648L);
    private static final BruitRoyaume BRUIT_DESSOUS = new BruitRoyaume(1662L);
    private static final BruitRoyaume BRUIT_FRONTIERE = new BruitRoyaume(386L);

    /** Ce qu'il y a dans une colonne du Royaume. */
    public record Colonne(
            /** false si la colonne est vide (hors de l'île et des îlots). */
            boolean ile,
            /** y du bloc le plus haut du sol. */
            int surface,
            /** y du bloc le plus bas (dessous de l'île flottante). */
            int fond,
            /** Zone du biome, avec des frontières ondulées. */
            Zone zone,
            /** Zone utilisée pour les blocs de surface : la même, mais mélangée près des frontières. */
            Zone zoneSol,
            /** Part du relief due aux falaises du bord (0 à l'intérieur). */
            double rebord,
            /** Hauteur relative sur le dôme du Puy (0 au pied, 1 au sommet), 0 ailleurs. */
            double altitudeDome,
            /** Roche qui affleure en surface (flancs du dôme, garrigue d'Hippone). */
            boolean affleurement) {

        public boolean sousLEau() {
            return ile && surface < NIVEAU_MER;
        }
    }

    private static final Colonne VIDE = new Colonne(false, 0, 0, Zone.JARDIN_MILAN, Zone.JARDIN_MILAN, 0, 0, false);

    private ReliefRoyaume() {
    }

    /** Zone (et donc biome) en (x, z). Les frontières suivent les axes, déformées d'environ ±70 blocs. */
    public static Zone zone(int x, int z) {
        return Zone.en((int) Math.floor(deformationX(x, z)), (int) Math.floor(deformationZ(x, z)));
    }

    private static double deformationX(int x, int z) {
        return x + 60 * BRUIT_FRONTIERE.fbm(x / 190.0, z / 190.0, 2) + 12 * BRUIT_DETAIL.fbm(x / 45.0, z / 45.0, 2);
    }

    private static double deformationZ(int x, int z) {
        return z + 60 * BRUIT_FRONTIERE.fbm((x + 5000) / 190.0, (z - 5000) / 190.0, 2) + 12 * BRUIT_DETAIL.fbm((x - 5000) / 45.0, (z + 5000) / 45.0, 2);
    }

    public static Colonne colonne(int x, int z) {
        double distance = Math.sqrt((double) x * x + (double) z * z);
        // Contour irrégulier : grands caps et criques, plus des dentelures.
        double bord = (distance + 55 * BRUIT_BORD.fbm(x / 170.0, z / 170.0, 3) + 20 * BRUIT_DETAIL.fbm(x / 45.0, z / 45.0, 2)) / RAYON_ILE;
        if (bord >= 1) {
            return ilot(x, z);
        }

        double xd = deformationX(x, z);
        double zd = deformationZ(x, z);
        Zone zone = Zone.en((int) Math.floor(xd), (int) Math.floor(zd));
        // Près des frontières, les blocs de surface des deux zones s'entremêlent sur une vingtaine de blocs.
        int h = hachage(x, z);
        Zone zoneSol = Zone.en((int) Math.floor(xd + (h % 21) - 10), (int) Math.floor(zd + ((h >> 8) % 21) - 10));

        // Mélange des quatre zones le long des frontières ondulées, sur une bande d'environ 50 blocs.
        double est = lisser(-25, 25, xd);
        double sud = lisser(-25, 25, zd);
        double hauteur = 0;
        double altitudeDome = 0;
        double poids;
        if ((poids = (1 - est) * (1 - sud)) > 0) {
            hauteur += poids * jardinMilan(x, z);
        }
        if ((poids = est * (1 - sud)) > 0) {
            hauteur += poids * portRoyal(x, z);
        }
        if ((poids = est * sud) > 0) {
            double distanceDome = Math.sqrt(carre(x - DOME_X) + carre(z - DOME_Z)) * (1 + 0.08 * BRUIT_ROCHE.fbm(x / 150.0, z / 150.0, 2));
            altitudeDome = profilDome(distanceDome / DOME_RAYON);
            hauteur += poids * puyDeDome(x, z, altitudeDome);
        }
        if ((poids = (1 - est) * sud) > 0) {
            hauteur += poids * hippone(x, z);
        }

        // Plaine douce au-dessus de la Caverne, au centre.
        double centre = 1 - lisser(80, 150, distance);
        hauteur = centre * (CENTRE_PLAINE + 2 * BRUIT_DETAIL.fbm(x / 60.0, z / 60.0, 2)) + (1 - centre) * hauteur;

        // Falaises du bord, de hauteur variable : le terrain se relève avant de tomber dans le vide.
        double rebord = lisser(0.92, 0.975, bord) * (10 + 30 * (0.5 + 0.5 * BRUIT_ROCHE.fbm(x / 90.0, z / 90.0, 3)));
        hauteur += rebord;

        int surface = (int) Math.floor(hauteur);
        // Dessous de l'île : épais au centre, de plus en plus fin vers le bord, un peu bosselé.
        double epaisseur = 14 + 110 * Math.sqrt(1 - bord) + 8 * BRUIT_DESSOUS.fbm(x / 50.0, z / 50.0, 3);
        int fond = Math.min(Math.max(1, (int) Math.floor(hauteur - epaisseur)), surface - 4);

        boolean affleurement = switch (zoneSol) {
            case PUY_DE_DOME -> altitudeDome > 0.5 && BRUIT_ROCHE.fbm(x / 18.0, z / 18.0, 2) > 0.25;
            case HIPPONE -> BRUIT_ROCHE.fbm(x / 14.0, z / 14.0, 2) > 0.45;
            default -> false;
        };
        return new Colonne(true, surface, fond, zone, zoneSol, rebord, altitudeDome, affleurement);
    }

    /** Les îlots flottants au-delà du bord ; vide partout ailleurs. */
    private static Colonne ilot(int x, int z) {
        for (int[] ilot : ILOTS) {
            double angle = Math.toRadians(ilot[0]);
            double cx = ilot[1] * StrictMath.cos(angle);
            double cz = ilot[1] * StrictMath.sin(angle);
            double rayon = ilot[2] * (1 + 0.2 * BRUIT_DETAIL.fbm(x / 12.0, z / 12.0, 2));
            double rapport = Math.sqrt(carre(x - cx) + carre(z - cz)) / rayon;
            if (rapport < 1) {
                double bombe = 1 - rapport * rapport;
                int surface = (int) Math.floor(ilot[3] + 3 * bombe + BRUIT_COLLINES.fbm(x / 10.0, z / 10.0, 2));
                int fond = (int) Math.floor(ilot[3] - 4 - 1.3 * ilot[2] * Math.sqrt(bombe) + 3 * BRUIT_DESSOUS.fbm(x / 8.0, z / 8.0, 2));
                Zone zone = zone(x, z);
                return new Colonne(true, surface, Math.min(fond, surface - 2), zone, zone, 0, 0, false);
            }
        }
        return VIDE;
    }

    /** Collines basses et herbeuses, entre y ≈ 94 et 110. */
    private static double jardinMilan(int x, int z) {
        return 94 + 14 * (0.5 + 0.5 * BRUIT_COLLINES.fbm(x / 160.0, z / 160.0, 3))
                + 2 * BRUIT_DETAIL.fbm(x / 40.0, z / 40.0, 2);
    }

    /**
     * Vallée en creux : fond plat et marécageux vers y ≈ 83, bords qui remontent vers y ≈ 105.
     * Pas de mares au centre, où sera l'abbaye.
     */
    private static double portRoyal(int x, int z) {
        double distanceVallee = Math.sqrt(carre(x - VALLEE_X) + carre(z - VALLEE_Z));
        double hauteur = 83 + 22 * lisser(110, 240, distanceVallee)
                + 3 * BRUIT_COLLINES.fbm(x / 150.0, z / 150.0, 3) + 1.5 * BRUIT_DETAIL.fbm(x / 35.0, z / 35.0, 2);
        double marais = BRUIT_MARAIS.fbm(x / 55.0, z / 55.0, 2);
        if (marais > 0.3) {
            hauteur -= (marais - 0.3) * 30 * lisser(35, 60, distanceVallee) * (1 - lisser(150, 230, distanceVallee));
        }
        return hauteur;
    }

    /** Hauteur relative du dôme pour une distance normalisée t (0 au centre, 1 au pied) : sommet arrondi, flancs longs. */
    private static double profilDome(double t) {
        if (t >= 1) {
            return 0;
        }
        return (StrictMath.pow(1 - t * t, 2) + StrictMath.pow(1 - t, 1.5)) / 2;
    }

    /** Prairie vers y ≈ 92, dôme de lave culminant vers y ≈ 300, petits puys à cratère autour. */
    private static double puyDeDome(int x, int z, double altitudeDome) {
        double plaine = 92 + 4 * BRUIT_DETAIL.fbm(x / 50.0, z / 50.0, 2);
        // Ravines sur les flancs, plus marquées en haut, nulles au sommet pour qu'il reste rond.
        double ravines = 10 * altitudeDome * (1 - altitudeDome) * 4 * BRUIT_ROCHE.fbm(x / 40.0, z / 40.0, 3);
        double hauteur = plaine + (DOME_SOMMET - 92) * altitudeDome + ravines;
        for (int[] puy : PETITS_PUYS) {
            double rapport = Math.sqrt(carre(x - puy[0]) + carre(z - puy[1])) * (1 + 0.1 * BRUIT_DETAIL.fbm(x / 30.0, z / 30.0, 2)) / puy[2];
            if (rapport < 1) {
                double cone;
                if (rapport >= 0.35) {
                    cone = puy[3] * StrictMath.pow((1 - rapport) / 0.65, 1.2);
                } else {
                    // Cratère en cuvette, à mi-hauteur du cône.
                    cone = puy[3] * (1 - 0.55 * (1 - carre(rapport / 0.35)));
                }
                hauteur = Math.max(hauteur, plaine + cone);
            }
        }
        return hauteur;
    }

    /** Garrigue vallonnée, mer intérieure, et plateau bas de la ville qui descend jusqu'au rivage. */
    private static double hippone(int x, int z) {
        double garrigue = 88 + 4 * BRUIT_COLLINES.fbm(x / 120.0, z / 120.0, 3) + 4 * BRUIT_DETAIL.fbm(x / 60.0, z / 60.0, 2);
        double distanceVille = Math.sqrt(carre(x - VILLE_X) + carre(z - VILLE_Z)) * (1 + 0.12 * BRUIT_DETAIL.fbm(x / 70.0, z / 70.0, 2));
        double hauteur = interpoler(VILLE_HAUTEUR, garrigue, lisser(55, 90, distanceVille));
        double distanceMer = Math.sqrt(carre(x - MER_X) + carre(z - MER_Z));
        double rapport = distanceMer / (MER_RAYON * (1 + 0.15 * BRUIT_BORD.fbm(x / 80.0, z / 80.0, 2)));
        if (rapport < 1) {
            // La mer creuse aussi le bord du plateau : la ville finit sur une plage, où viendra le port.
            hauteur = Math.min(hauteur, interpoler(58, garrigue, lisser(0.55, 1.0, rapport)));
        }
        return hauteur;
    }

    /** Nombre pseudo-aléatoire fixe pour une colonne. */
    static int hachage(int x, int z) {
        int h = x * 73856093 ^ z * 83492791;
        h ^= h >>> 13;
        h *= 0x5bd1e995;
        h ^= h >>> 15;
        return h & Integer.MAX_VALUE;
    }

    /** 0 avant a, 1 après b, transition en S entre les deux. */
    static double lisser(double a, double b, double valeur) {
        double t = Math.min(Math.max((valeur - a) / (b - a), 0), 1);
        return t * t * (3 - 2 * t);
    }

    private static double interpoler(double a, double b, double t) {
        return a + (b - a) * t;
    }

    private static double carre(double valeur) {
        return valeur * valeur;
    }
}
