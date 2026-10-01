package com.royaumedesidees.client;

import com.royaumedesidees.RoyaumeDesIdees;
import com.royaumedesidees.registre.ModMonde;
import com.royaumedesidees.registre.ModSons;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.Music;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.SelectMusicEvent;

/**
 * Musique du Royaume, côté client :
 * <ul>
 *   <li>elle démarre 7 secondes après l'entrée dans le Royaume (sinon Minecraft attendrait de 30 s à 5 min) ;</li>
 *   <li>dans le Royaume, c'est toujours elle qui est choisie, même en créatif (Minecraft y imposerait sinon
 *       sa musique « créative ») ;</li>
 *   <li>une fois finie, elle revient après 30 s à 5 min de silence (mêmes délais que dans les biomes).</li>
 * </ul>
 */
public final class MusiqueRoyaume {
    public static final int DELAI_MIN = 600;
    public static final int DELAI_MAX = 6000;

    private static Music musique;
    /** Ticks d'attente entre l'arrivée et le lancement de la musique. */
    private static final int DEMARRAGE = 140;
    /** Ticks passés dans le Royaume depuis l'arrivée (-1 hors du Royaume). */
    private static int ticksDansLeRoyaume = -1;
    private static int attente;

    private MusiqueRoyaume() {
    }

    private static Music musique() {
        if (musique == null) {
            musique = new Music(ModSons.MUSIQUE_ROYAUME, DELAI_MIN, DELAI_MAX, true);
        }
        return musique;
    }

    /** Vrai si la musique du Royaume est en train de jouer. */
    public static boolean joue() {
        return Minecraft.getInstance().getMusicManager().isPlayingMusic(musique());
    }

    public static void choisir(SelectMusicEvent evenement) {
        Minecraft jeu = Minecraft.getInstance();
        if (jeu.level != null && jeu.level.dimension().equals(ModMonde.ROYAUME)) {
            evenement.setMusic(musique());
        }
    }

    /**
     * À l'entrée dans le Royaume, coupe la musique en cours et lance celle du Royaume après 7 secondes : après un
     * passage de portail, le jeu coupe encore les sons environ 6 secondes plus tard, et un lancement immédiat
     * donnerait un faux départ. Jusqu'à 30 secondes après l'arrivée, on la relance si elle s'est quand même arrêtée.
     */
    public static void tick(ClientTickEvent.Post evenement) {
        Minecraft jeu = Minecraft.getInstance();
        boolean dansLeRoyaume = jeu.level != null && jeu.level.dimension().equals(ModMonde.ROYAUME);
        if (!dansLeRoyaume) {
            ticksDansLeRoyaume = -1;
            return;
        }
        if (ticksDansLeRoyaume == -1) {
            ticksDansLeRoyaume = 0;
            attente = 0;
        }
        if (++ticksDansLeRoyaume < DEMARRAGE || ticksDansLeRoyaume > 600 || joue() || attente-- > 0) {
            return;
        }
        jeu.getMusicManager().stopPlaying();
        jeu.getMusicManager().startPlaying(musique());
        RoyaumeDesIdees.LOGGER.debug("[musique] lancement {} ticks après l'arrivée", ticksDansLeRoyaume);
        attente = 20;
    }
}
