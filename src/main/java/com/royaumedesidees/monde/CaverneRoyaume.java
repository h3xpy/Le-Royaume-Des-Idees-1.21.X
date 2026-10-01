package com.royaumedesidees.monde;

/**
 * Forme de la Caverne de Platon, creusée sous le centre de l'île, uniquement d'après les coordonnées.
 *
 * <p>Disposition, d'après l'allégorie : les prisonniers arrivent au fond de la salle, face au mur nord où
 * défilent les ombres ; le feu est derrière eux ; plus loin derrière, un long tunnel en pente douce remonte
 * vers le sud jusqu'à la lumière du jour. Le mobilier (chaînes, mur des ombres, feu) est posé par le
 * système de structures, pas ici.
 *
 * <p>Cette classe n'utilise rien de Minecraft. <b>Ne plus la modifier après la v0.1</b>, comme {@link ReliefRoyaume}.
 */
public final class CaverneRoyaume {
    /** Salle : ellipsoïde aplati centré sous (0, 0), sol plat vers y 46, voûte jusqu'à y ≈ 84. */
    public static final int SOL_Y = 46;
    public static final int CENTRE_Y = 64;
    public static final double RAYON = 55;
    public static final double DEMI_HAUTEUR = 20;

    /** Point d'arrivée des joueurs (pieds), tournés vers le nord, face au mur des ombres. */
    public static final int ARRIVEE_X = 0;
    public static final int ARRIVEE_Y = SOL_Y + 1;
    public static final int ARRIVEE_Z = -20;
    /** Mur des ombres : la paroi nord de la salle, vers z = -50. Feu : derrière les prisonniers. */
    public static final int MUR_Z = -50;
    public static final int FEU_Z = 10;

    /** Tunnel de sortie : part du sol de la salle en z = 20 et monte d'un bloc tous les deux vers le sud. */
    public static final int TUNNEL_DEBUT_Z = 20;
    public static final int TUNNEL_LARGEUR = 2;      // demi-largeur : 5 blocs de large
    public static final int TUNNEL_HAUTEUR = 5;

    private static final BruitRoyaume BRUIT_PAROI = new BruitRoyaume(380L);

    private CaverneRoyaume() {
    }

    /** Vrai si le bloc (x, y, z) est creusé (salle ou tunnel). {@code surface} est le sol de la colonne. */
    public static boolean vide(int x, int y, int z, int surface) {
        return (dansSalle(x, y, z) && !dansRampe(x, y, z)) || dansTunnel(x, y, z, surface);
    }

    /**
     * Rampe de roche pleine qui monte depuis le sol de la salle jusqu'à l'entrée du tunnel : sans elle,
     * l'entrée serait une dizaine de blocs au-dessus du sol, hors d'atteinte.
     */
    private static boolean dansRampe(int x, int y, int z) {
        return z >= TUNNEL_DEBUT_Z && Math.abs(x - axeTunnel(z)) <= TUNNEL_LARGEUR + 0.5 && y <= solTunnel(z);
    }

    /** Hauteur du sol de la salle : presque plat, légèrement bosselé. */
    public static int sol(int x, int z) {
        return SOL_Y + (int) Math.floor(1.5 * BRUIT_PAROI.fbm(x / 14.0, z / 14.0, 2));
    }

    private static boolean dansSalle(int x, int y, int z) {
        // Test rapide : la salle ne dépasse jamais 64 blocs du centre ni la tranche y 40 à 90.
        if ((long) x * x + (long) z * z > 64L * 64L || y < 40 || y > 90 || y <= sol(x, z)) {
            return false;
        }
        // Parois irrégulières : le rayon varie d'environ ±15 %, la voûte d'environ ±3 blocs.
        double rayon = RAYON * (1 + 0.15 * BRUIT_PAROI.fbm(x / 25.0, z / 25.0, 3));
        double demiHauteur = DEMI_HAUTEUR + 3 * BRUIT_PAROI.fbm((x + 300) / 12.0, (z - 300) / 12.0, 2);
        double horizontal = ((double) x * x + (double) z * z) / (rayon * rayon);
        double vertical = (y - CENTRE_Y) / demiHauteur;
        return horizontal + vertical * vertical < 1;
    }

    /** Axe du tunnel : il serpente doucement autour de x = 0. */
    public static double axeTunnel(int z) {
        return 3 * StrictMath.sin(z / 18.0);
    }

    /** Sol du tunnel à la position z : un bloc de montée tous les deux blocs. */
    public static int solTunnel(int z) {
        return SOL_Y + Math.floorDiv(z - TUNNEL_DEBUT_Z, 2);
    }

    private static boolean dansTunnel(int x, int y, int z, int surface) {
        if (z < TUNNEL_DEBUT_Z || Math.abs(x) > 8) {
            return false;
        }
        int sol = solTunnel(z);
        // Le tunnel s'arrête là où son sol rejoint la surface : c'est la sortie à l'air libre.
        if (sol >= surface) {
            return false;
        }
        double ecart = Math.abs(x - axeTunnel(z));
        if (ecart > TUNNEL_LARGEUR + 0.5) {
            return false;
        }
        // Voûte arrondie : un bloc plus bas sur les côtés.
        int hauteur = ecart > TUNNEL_LARGEUR - 0.5 ? TUNNEL_HAUTEUR - 1 : TUNNEL_HAUTEUR;
        return y > sol && y <= sol + hauteur;
    }

    /** Vrai si (x, y, z) est dans la paroi du tunnel (jusqu'à 2 blocs autour du vide). */
    public static boolean paroiTunnel(int x, int y, int z) {
        if (z < TUNNEL_DEBUT_Z - 2 || Math.abs(x) > 10) {
            return false;
        }
        int sol = solTunnel(z);
        return Math.abs(x - axeTunnel(z)) <= TUNNEL_LARGEUR + 2.5 && y >= sol - 2 && y <= sol + TUNNEL_HAUTEUR + 2;
    }
}
