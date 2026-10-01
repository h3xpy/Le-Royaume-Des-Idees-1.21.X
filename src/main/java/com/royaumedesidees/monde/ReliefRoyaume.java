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
    public static final int RAYON_ILE = 1000;

    /** Plaine au-dessus de la Caverne, au centre de l'île. */
    public static final int CENTRE_PLAINE = 100;
    /** Cône du Puy de Dôme : centre, rayon de la base, sommet du bord du cratère. */
    public static final int PUY_X = 480;
    public static final int PUY_Z = 470;
    public static final double PUY_RAYON = 400;
    /** Mer intérieure d'Hippone. */
    public static final int MER_X = -500;
    public static final int MER_Z = 480;
    public static final double MER_RAYON = 200;
    /** Plateau qui accueillera la ville d'Hippone et ses remparts, au bord de la mer. */
    public static final int PLATEAU_X = -337;
    public static final int PLATEAU_Z = 324;
    public static final int PLATEAU_HAUTEUR = 100;

    // Une graine fixe par usage : changer l'une d'elles changerait toute la carte.
    private static final BruitRoyaume BRUIT_BORD = new BruitRoyaume(1623L);
    private static final BruitRoyaume BRUIT_COLLINES = new BruitRoyaume(354L);
    private static final BruitRoyaume BRUIT_DETAIL = new BruitRoyaume(430L);
    private static final BruitRoyaume BRUIT_MARAIS = new BruitRoyaume(1656L);
    private static final BruitRoyaume BRUIT_ROCHE = new BruitRoyaume(1648L);
    private static final BruitRoyaume BRUIT_DESSOUS = new BruitRoyaume(1662L);

    /** Ce qu'il y a dans une colonne de l'île. */
    public record Colonne(
            /** false au-delà du bord : la colonne est vide. */
            boolean ile,
            /** y du bloc le plus haut du sol. */
            int surface,
            /** y du bloc le plus bas de l'île (dessous de l'île flottante). */
            int fond,
            Zone zone,
            /** Part du relief due aux falaises du bord (0 à l'intérieur). */
            double rebord,
            /** Hauteur relative sur le Puy de Dôme (0 au pied, 1 au sommet), 0 ailleurs. */
            double altitudePuy,
            /** Distance au centre du cratère du Puy de Dôme. */
            double distancePuy) {

        public boolean sousLEau() {
            return ile && surface < NIVEAU_MER;
        }
    }

    private ReliefRoyaume() {
    }

    public static Colonne colonne(int x, int z) {
        double distance = Math.sqrt((double) x * x + (double) z * z);
        // Bord irrégulier : le rayon effectif varie d'environ ±70 blocs.
        double bord = (distance + 70 * BRUIT_BORD.fbm(x / 260.0, z / 260.0, 3)) / RAYON_ILE;
        Zone zone = Zone.en(x, z);
        if (bord >= 1) {
            return new Colonne(false, 0, 0, zone, 0, 0, 0);
        }

        // Mélange des quatre zones : chacune pèse selon le côté de l'île, avec 90 blocs de transition.
        double est = lisser(-90, 90, x);
        double sud = lisser(-90, 90, z);
        double hauteur = 0;
        double poids;
        if ((poids = (1 - est) * (1 - sud)) > 0) {
            hauteur += poids * jardinMilan(x, z);
        }
        if ((poids = est * (1 - sud)) > 0) {
            hauteur += poids * portRoyal(x, z);
        }
        // Le cône n'est pas parfaitement rond : son rayon varie d'environ ±12 % selon la direction.
        double distancePuy = Math.sqrt(carre(x - PUY_X) + carre(z - PUY_Z))
                * (1 + 0.12 * BRUIT_ROCHE.fbm(x / 150.0, z / 150.0, 2));
        double altitudePuy = 0;
        if ((poids = est * sud) > 0) {
            altitudePuy = profilPuy(distancePuy);
            hauteur += poids * puyDeDome(x, z, distancePuy, altitudePuy);
        }
        if ((poids = (1 - est) * sud) > 0) {
            hauteur += poids * hippone(x, z);
        }

        // Plaine douce au-dessus de la Caverne, au centre.
        double centre = 1 - lisser(110, 230, distance);
        hauteur = centre * (CENTRE_PLAINE + 2 * BRUIT_DETAIL.fbm(x / 60.0, z / 60.0, 2)) + (1 - centre) * hauteur;

        // Falaises du bord : le terrain se relève avant de tomber dans le vide.
        double rebord = 32 * lisser(0.935, 0.98, bord) * (0.75 + 0.25 * BRUIT_ROCHE.fbm(x / 40.0, z / 40.0, 2));
        hauteur += rebord;

        int surface = (int) Math.floor(hauteur);
        // Dessous de l'île : épais au centre, de plus en plus fin vers le bord, un peu bosselé.
        double epaisseur = 14 + 110 * Math.sqrt(1 - bord) + 8 * BRUIT_DESSOUS.fbm(x / 50.0, z / 50.0, 3);
        int fond = Math.max(1, (int) Math.floor(hauteur - epaisseur));
        return new Colonne(true, surface, Math.min(fond, surface - 4), zone, rebord, altitudePuy, distancePuy);
    }

    /** Collines basses et herbeuses, entre y ≈ 94 et 110. */
    private static double jardinMilan(int x, int z) {
        return 94 + 14 * (0.5 + 0.5 * BRUIT_COLLINES.fbm(x / 180.0, z / 180.0, 3))
                + 2 * BRUIT_DETAIL.fbm(x / 40.0, z / 40.0, 2);
    }

    /** Vallée plate et humide vers y ≈ 85, avec des mares là où le sol s'affaisse sous le niveau de l'eau. */
    private static double portRoyal(int x, int z) {
        double hauteur = 85 + 3 * BRUIT_COLLINES.fbm(x / 150.0, z / 150.0, 3) + 1.5 * BRUIT_DETAIL.fbm(x / 35.0, z / 35.0, 2);
        double marais = BRUIT_MARAIS.fbm(x / 60.0, z / 60.0, 2);
        if (marais > 0.3) {
            hauteur -= (marais - 0.3) * 30;
        }
        return hauteur;
    }

    /** Hauteur relative du cône (0 au pied, 1 sur le bord du cratère). */
    private static double profilPuy(double distance) {
        if (distance >= PUY_RAYON) {
            return 0;
        }
        return StrictMath.pow(1 - Math.max(distance, 40) / PUY_RAYON, 1.6) / StrictMath.pow(1 - 40 / PUY_RAYON, 1.6);
    }

    /** Plaine vers y ≈ 92 et volcan dont le bord du cratère culmine vers y ≈ 300. */
    private static double puyDeDome(int x, int z, double distance, double altitude) {
        double hauteur = 92 + 4 * BRUIT_DETAIL.fbm(x / 50.0, z / 50.0, 2);
        // Ravines sur les flancs, plus marquées en haut.
        hauteur += altitude * (207 + 14 * BRUIT_ROCHE.fbm(x / 45.0, z / 45.0, 3));
        // Cratère : fond plat 25 blocs sous le bord, parois entre 24 et 40 blocs du centre.
        hauteur -= 25 * (1 - lisser(24, 40, distance));
        return hauteur;
    }

    /** Plaine côtière, mer intérieure, et plateau plat pour la ville. */
    private static double hippone(int x, int z) {
        double hauteur = 90 + 4 * BRUIT_COLLINES.fbm(x / 120.0, z / 120.0, 3);
        double distanceMer = Math.sqrt(carre(x - MER_X) + carre(z - MER_Z));
        double rayonMer = MER_RAYON * (1 + 0.15 * BRUIT_BORD.fbm(x / 120.0, z / 120.0, 2));
        double rapport = distanceMer / rayonMer;
        if (rapport < 1) {
            hauteur = interpoler(58, hauteur, lisser(0.55, 1.0, rapport));
        }
        // Bord du plateau irrégulier, qui descend en pente douce jusqu'au rivage (le futur port).
        double distancePlateau = Math.sqrt(carre(x - PLATEAU_X) + carre(z - PLATEAU_Z))
                * (1 + 0.15 * BRUIT_DETAIL.fbm(x / 90.0, z / 90.0, 2));
        return interpoler(PLATEAU_HAUTEUR, hauteur, lisser(80, 120, distancePlateau));
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
