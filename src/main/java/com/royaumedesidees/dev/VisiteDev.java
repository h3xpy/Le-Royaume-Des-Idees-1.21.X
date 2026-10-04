package com.royaumedesidees.dev;

import com.royaumedesidees.RoyaumeDesIdees;
import com.royaumedesidees.client.MusiqueRoyaume;
import com.royaumedesidees.monde.CaverneRoyaume;
import com.royaumedesidees.monde.ReliefRoyaume;
import com.royaumedesidees.bloc.FeuillesPoirierBloc;
import com.royaumedesidees.registre.ModBlocs;
import com.royaumedesidees.registre.ModEffets;
import com.royaumedesidees.registre.ModItems;
import com.royaumedesidees.structures.StructuresJardin;
import com.royaumedesidees.structures.StructuresRoyaume;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.client.gui.screens.GenericMessageScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.common.NeoForge;

import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Visite automatique réservée au développement, côté client : active seulement avec
 * {@code -Droyaumedesidees.visite=true}, à n'utiliser que sur une copie du monde de test.
 * <ol>
 *   <li>voyage aller : le joueur (en créatif) entre dans le portail de test construit par {@link VerificationDev} ;</li>
 *   <li>voyage retour : il entre dans le portail de Pierre d'Ombre ;</li>
 *   <li>puis, en spectateur et en plein jour, il visite quelques points de vue.</li>
 * </ol>
 * Chaque étape note dans le journal la dimension et la position du joueur, et prend une capture
 * ({@code run/screenshots/visite_*.png}). À la fin, le joueur quitte le monde, ce qui le sauvegarde.
 */
public final class VisiteDev {
    /**
     * Une étape : commandes au début, puis une action éventuelle (clic du joueur, réapparition) tentée à partir de
     * la mi-parcours, toutes les demi-secondes, jusqu'à ce qu'elle réussisse (le temps que les chunks arrivent).
     */
    private record Etape(String nom, Supplier<List<String>> commandes, Predicate<Minecraft> action, int duree) {
        Etape(String nom, Supplier<List<String>> commandes, Predicate<Minecraft> action) {
            this(nom, commandes, action, ATTENTE_ETAPE);
        }

        Etape(String nom, Supplier<List<String>> commandes) {
            this(nom, commandes, null, ATTENTE_ETAPE);
        }
    }

    /** Dernière question d'Adéodat lue dans le chat (« 47 + 38 », « 23 × 7 »). */
    private static volatile String questionAdeodat;

    private static final List<Etape> ETAPES = List.of(
            new Etape("aller", () -> {
                BlockPos portail = VerificationDev.portailTest;
                return portail == null ? List.of() : List.of("gamemode creative", String.format(Locale.ROOT,
                        "execute in minecraft:overworld run tp @s %.1f %d %.1f 0 0", portail.getX() + 0.5, portail.getY(), portail.getZ() + 0.5));
            }),
            // Une Ombre figée (sans IA, pour qu'elle reste à portée) devant le joueur enchaîné : sans Lanterne, le coup est refusé.
            new Etape("sans_lanterne", () -> List.of("summon royaumedesidees:ombre ~ ~ ~-3 {NoAI:1b}",
                    "damage @e[type=royaumedesidees:ombre,limit=1,sort=nearest] 4 minecraft:player_attack by @s")),
            // Avec la Lanterne en main, l'Ombre se révèle (capture) puis le coup porte.
            new Etape("avec_lanterne", () -> List.of("item replace entity @s weapon.mainhand with royaumedesidees:lanterne_diogene")),
            new Etape("frappe", () -> List.of(
                    "damage @e[type=royaumedesidees:ombre,limit=1,sort=nearest] 4 minecraft:player_attack by @s",
                    "data get entity @e[type=royaumedesidees:ombre,limit=1,sort=nearest] Health")),
            // Remontée par le tunnel, puis sortie à l'air libre : aveuglement, succès et Lanterne.
            new Etape("dans_le_tunnel", () -> {
                int z = 80;
                return List.of("item replace entity @s weapon.mainhand with minecraft:air",
                        "clear @s royaumedesidees:lanterne_diogene",
                        String.format(Locale.ROOT, "execute in royaumedesidees:royaume run tp @s %d %d %d 0 0",
                                Math.round(CaverneRoyaume.axeTunnel(z)), CaverneRoyaume.solTunnel(z) + 1, z));
            }),
            new Etape("lumiere", () -> List.of(String.format(Locale.ROOT, "execute in royaumedesidees:royaume run tp @s 0 %d 128 0 0",
                    ReliefRoyaume.colonne(0, 128).surface() + 1))),
            new Etape("redescente", () -> List.of("execute in royaumedesidees:royaume run tp @s 0 47 0 180 0")),
            new Etape("retour", () -> List.of(String.format(Locale.ROOT,
                    "execute in royaumedesidees:royaume run tp @s %.1f %d %.1f 0 0", StructuresRoyaume.PORTAIL_X + 1.5,
                    StructuresRoyaume.PORTAIL_Y + 1, StructuresRoyaume.PORTAIL_Z + 0.5))),
            // Jardin de Milan (v0.2), en survie : vol de poires, Culpabilité, mort, confession, achat honnête.
            new Etape("verger", () -> List.of("gamemode survival", "effect clear @s", "clear @s",
                    tpRoyaume(StructuresJardin.POS_POIRIER.offset(2, 0, 2), 135f, -60f)), VisiteDev::cueillir),
            new Etape("vol_2", () -> List.of(), VisiteDev::cueillir),
            new Etape("vol_3", () -> List.of(), VisiteDev::cueillir),
            new Etape("effect_clear", () -> List.of("effect clear @s")),
            new Etape("confesse_loin", () -> List.of("confesse J'ai vole des poires")),
            new Etape("mort", () -> List.of("kill @s"), jeu -> {
                jeu.player.respawn();
                // Interface visible pour cette étape (Overworld : pas de jauge) et la confession (Royaume : jauge).
                jeu.options.hideGui = false;
                return true;
            }),
            new Etape("confession", () -> List.of(tpRoyaume(StructuresJardin.POS_CONFESSIONNAL.offset(-2, 0, 0), 270f, 0f),
                    "confesse J'ai vole des poires pour le plaisir de mal faire")),
            new Etape("confesse_trop_tot", () -> List.of("confesse Encore une fois"), jeu -> {
                jeu.options.hideGui = true;
                return true;
            }),
            // Le même péché qu'à la première confession : claque attendue (un demi-cœur, un recul, une remarque).
            new Etape("confesse_repetee", () -> List.of("confesse J'ai vole des poires pour le plaisir de mal faire")),
            new Etape("etal", () -> List.of("item replace entity @s weapon.mainhand with minecraft:emerald 2",
                    tpRoyaume(StructuresJardin.POS_ETAL.offset(0, 0, 2), 180f, 35f)), jeu -> utiliser(jeu, StructuresJardin.POS_ETAL)),
            // Monique (v0.3) : elle a suivi le joueur jusqu'ici ; on la nourrit (+3 de Grâce, une fois par jour).
            new Etape("monique_nourrir", () -> List.of("item replace entity @s weapon.mainhand with minecraft:bread 2"),
                    jeu -> parlerA(jeu, "monique")),
            // PNJ (v0.3) : les cinq en rang, face au joueur ; un coup à Pascal, un mot à Augustin.
            // Interface visible pour voir les noms au-dessus des têtes.
            new Etape("pnj", VisiteDev::invoquerPnj, jeu -> {
                jeu.options.hideGui = false;
                return true;
            }),
            new Etape("pnj_coup", () -> List.of(
                    "damage @e[type=royaumedesidees:pascal,limit=1,sort=nearest] 4 minecraft:player_attack by @s",
                    "damage @e[type=royaumedesidees:augustin_jeune,limit=1,sort=nearest] 4 minecraft:player_attack by @s")),
            new Etape("pnj_parler", () -> List.of(), jeu -> {
                jeu.options.hideGui = true;
                return parlerA(jeu, "ambroise") && parlerA(jeu, "adeodat");
            }),
            // Quête de conversion (v0.3), de bout en bout : Augustin, trois poires, le silence, le figuier, Romains, le baptême.
            new Etape("quete_augustin", () -> List.of("gamemode survival", tpRoyaume(com.royaumedesidees.structures.StructuresJardin.POS_ALLEE_VERGERS, 0f, 0f)),
                    jeu -> parlerA(jeu, "augustin_jeune")),
            new Etape("quete_vol_1", () -> List.of(tpRoyaume(posVerger(), 135f, -60f)), VisiteDev::cueillir),
            new Etape("quete_vol_2", () -> List.of(), VisiteDev::cueillir),
            new Etape("quete_vol_3", () -> List.of(), VisiteDev::cueillir),
            new Etape("quete_silence", () -> List.of(tpRoyaume(com.royaumedesidees.structures.StructuresPnj.POS_AMBROISE.offset(0, 0, 3), 180f, 0f)),
                    null, 66 * 20),
            new Etape("quete_figuier", () -> List.of(tpRoyaume(com.royaumedesidees.structures.StructuresJardin.POS_FIGUIER.offset(2, 0, 1), 200f, 10f)),
                    jeu -> {
                        jeu.options.keyShift.setDown(true);
                        return true;
                    }, 10 * 20),
            new Etape("quete_romains", () -> List.of(tpRoyaume(com.royaumedesidees.structures.StructuresJardin.POS_LUTRIN_FIGUIER.offset(-1, 0, -1), 135f, 30f)),
                    jeu -> {
                        jeu.options.keyShift.setDown(false);
                        return utiliser(jeu, com.royaumedesidees.structures.StructuresJardin.POS_LUTRIN_FIGUIER);
                    }),
            new Etape("quete_bapteme", () -> List.of(tpRoyaume(com.royaumedesidees.structures.StructuresPnj.POS_AMBROISE.offset(0, 0, 1), 180f, 0f)),
                    jeu -> {
                        jeu.player.closeContainer();
                        return parlerA(jeu, "ambroise");
                    }),
            new Etape("livre", () -> List.of("damage @s 8 minecraft:generic", "item replace entity @s weapon.mainhand with royaumedesidees:livre_confessions"),
                    jeu -> jeu.gameMode.useItem(jeu.player, InteractionHand.MAIN_HAND).consumesAction()),
            // Un défi d'Adéodat, résolu en lisant la question dans le chat.
            new Etape("adeodat_defi", () -> List.of(tpRoyaume(com.royaumedesidees.structures.StructuresJardin.POS_TABLINUM.offset(0, 0, 1), 180f, 0f)),
                    jeu -> parlerA(jeu, "adeodat")),
            new Etape("adeodat_reponse", () -> List.of(), VisiteDev::repondreAdeodat),
            // Bibliothèque d'Ambroise : on entre, on essaie de parler (le chat doit être muet), Ambroise est chez lui.
            new Etape("bibliotheque_silence", () -> List.of(tpRoyaume(com.royaumedesidees.structures.StructuresPnj.POS_AMBROISE.offset(0, 0, 4), 180f, 5f)),
                    jeu -> {
                        jeu.player.connection.sendChat("Bonjour, Ambroise !");
                        return true;
                    }),
            new Etape("cellule_pascal", () -> List.of(tpRoyaume(com.royaumedesidees.structures.StructuresPnj.POS_PASCAL.offset(0, 0, 1), 180f, 10f))),
            vue("villa", -226, 0, -164, 225f, 22f, true),
            vue("bibliotheque", -158, -16, -290, 150f, 28f, false),
            vue("bibliotheque_atrium", -180, com.royaumedesidees.structures.StructuresPnj.POS_ENTREE_BIBLIOTHEQUE.getY() + 7, -306, 180f, 28f, false),
            vue("bibliotheque_abside", -180, com.royaumedesidees.structures.StructuresPnj.POS_ENTREE_BIBLIOTHEQUE.getY() + 1, -336, 180f, -12f, false),
            vue("bibliotheque_nef", -180, com.royaumedesidees.structures.StructuresPnj.POS_ENTREE_BIBLIOTHEQUE.getY() + 1, -324, 180f, 6f, false),
            vue("cellule", 309, -6, -280, 135f, 18f, false),
            vue("villa_dessus", -200, StructuresJardin.VILLA_SOL + 32, -152, 180f, 48f, false),
            vue("atrium", -200, StructuresJardin.VILLA_SOL + 1, -183, 180f, 8f, false),
            vue("tablinum", -200, StructuresJardin.VILLA_SOL + 1, -192, 180f, 18f, false),
            vue("peristyle", -200, StructuresJardin.VILLA_SOL + 7, -199, 180f, 40f, false),
            vue("chemin_vergers", -224, -6, -202, 90f, 18f, false),
            vue("chemin_figuier", -208, -8, -224, 125f, 22f, false),
            vue("vergers", -290, 0, -148, 125f, 25f, false),
            vue("vigne", -301, -1, -171, 180f, 5f, false),
            vue("porcherie", -312, -7, -171, 0f, 35f, false),
            vue("figuier", -238, 0, -238, 135f, 25f, false),
            vue("sous_figuier", -262, -1, -257, 310f, 0f, false),
            vue("maison_voisine", -260, -2, -265, 180f, 5f, false),
            vue("confessionnal", 9, -4, -10, 245f, 15f, false),
            vue("jardin", 0, 135, 0, 135f, 12f, false),
            vue("port_royal", 220, 104, -190, 225f, 18f, false),
            vue("puy", 40, 150, 40, 315f, 2f, false),
            vue("hippone", -140, 118, 140, 45f, 14f, false),
            vue("bord", 742, 128, 270, 90f, 6f, false),
            vue("caverne", 0, 52, 15, 180f, 5f, false),
            vue("rampe", 0, 52, -15, 0f, 5f, false),
            vue("portail", 7, 100, 142, 180f, 10f, false),
            vue("tunnel", 0, 62, 50, 0f, -12f, false));

    /** Ticks d'attente avant de commencer, puis à chaque étape (voyage ou chargement des chunks). */
    private static final int ATTENTE_DEPART = 100;
    private static final int ATTENTE_ETAPE = 160;

    private static int compteur;
    private static int etape = -1;
    private static boolean actionFaite;

    /** Les PNJ invoqués en rang (Monique n'y est pas : elle suit son joueur et s'en va si on l'invoque seule). */
    private static final String[] PNJ = {"augustin_jeune", "adeodat", "ambroise", "pascal"};

    /** Les cinq PNJ en rang, immobiles (sans IA), tournés vers le joueur placé 4 blocs au nord. */
    private static List<String> invoquerPnj() {
        int z = 8;
        int x0 = 30;
        List<String> commandes = new java.util.ArrayList<>();
        int sol = ReliefRoyaume.colonne(x0 + 4, z - 4).surface();
        commandes.add(String.format(Locale.ROOT, "execute in royaumedesidees:royaume run tp @s %d %d %d 0 8", x0 + 4, sol + 1, z - 4));
        for (int i = 0; i < PNJ.length; i++) {
            int x = x0 + 2 * i;
            commandes.add(String.format(Locale.ROOT,
                    "execute in royaumedesidees:royaume run summon royaumedesidees:%s %d %d %d {NoAI:1b,Rotation:[180f,0f]}",
                    PNJ[i], x, ReliefRoyaume.colonne(x, z).surface() + 1, z));
        }
        return commandes;
    }

    /** Clic droit du joueur sur le PNJ le plus proche de ce type. */
    private static boolean parlerA(Minecraft jeu, String id) {
        return jeu.level.getEntitiesOfClass(com.royaumedesidees.pnj.PnjRoyaume.class, jeu.player.getBoundingBox().inflate(24),
                        pnj -> pnj.id().equals(id)).stream()
                .min(java.util.Comparator.comparingDouble(pnj -> pnj.distanceToSqr(jeu.player)))
                .map(pnj -> {
                    if (pnj.distanceTo(jeu.player) > 2.5F) {
                        // Trop loin pour le bras du joueur : on le rapproche, et on réessaie au prochain passage.
                        jeu.getConnection().sendCommand("tp @s " + pnj.getStringUUID());
                        return false;
                    }
                    InteractionResult resultat = jeu.gameMode.interact(jeu.player, pnj, InteractionHand.MAIN_HAND);
                    RoyaumeDesIdees.LOGGER.info("[visite] clic droit sur {} : {}", id, resultat);
                    return true;
                })
                .orElse(false);
    }

    /** Sous un poirier du verger de Sévère, à portée d'Augustin (qui vit dans l'allée). */
    private static BlockPos posVerger() {
        int x = -325 + 2;
        int z = -181 + 2;
        return new BlockPos(x, ReliefRoyaume.colonne(x, z).surface() + 1, z);
    }

    private static boolean repondreAdeodat(Minecraft jeu) {
        String question = questionAdeodat;
        if (question == null) {
            return false;
        }
        String[] morceaux = question.split(" ");
        int a = Integer.parseInt(morceaux[0]);
        int b = Integer.parseInt(morceaux[2]);
        int reponse = morceaux[1].equals("+") ? a + b : a * b;
        RoyaumeDesIdees.LOGGER.info("[visite] question d'Adéodat : {} = {}", question, reponse);
        jeu.player.connection.sendChat(Integer.toString(reponse));
        return true;
    }

    private static String tpRoyaume(BlockPos pos, float orientation, float inclinaison) {
        return String.format(Locale.ROOT, "execute in royaumedesidees:royaume run tp @s %.1f %d %.1f %.1f %.1f",
                pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, orientation, inclinaison);
    }

    /** Cueille (vole) la poire la plus proche à portée de main, comme un clic droit du joueur. */
    private static boolean cueillir(Minecraft jeu) {
        BlockPos joueur = jeu.player.blockPosition();
        // La position du flux est mutable et réutilisée : on la fige avant de la garder.
        return BlockPos.betweenClosedStream(joueur.offset(-4, -1, -4), joueur.offset(4, 5, 4))
                .map(BlockPos::immutable)
                .filter(pos -> {
                    BlockState etat = jeu.level.getBlockState(pos);
                    return etat.is(ModBlocs.FEUILLES_POIRIER.get()) && etat.getValue(FeuillesPoirierBloc.POIRES);
                })
                .min(java.util.Comparator.comparingDouble(pos -> pos.distToCenterSqr(jeu.player.getEyePosition())))
                .map(pos -> utiliser(jeu, pos))
                .orElse(false);
    }

    private static boolean utiliser(Minecraft jeu, BlockPos pos) {
        BlockHitResult impact = new BlockHitResult(Vec3.atCenterOf(pos), Direction.UP, pos, false);
        InteractionResult resultat = jeu.gameMode.useItemOn(jeu.player, InteractionHand.MAIN_HAND, impact);
        RoyaumeDesIdees.LOGGER.info("[visite] clic droit sur {} en {} : {}", jeu.level.getBlockState(pos).getBlock().getName().getString(),
                pos.toShortString(), resultat);
        return resultat.consumesAction();
    }

    private static Etape vue(String nom, int x, int y, int z, float orientation, float inclinaison, boolean spectateur) {
        // y = 0 : à 12 blocs au-dessus du sol généré ; y négatif : à -y blocs au-dessus du sol.
        int hauteur = y > 0 ? y : ReliefRoyaume.colonne(x, z).surface() + (y == 0 ? 12 : -y);
        String tp = String.format(Locale.ROOT, "execute in royaumedesidees:royaume run tp @s %d %d %d %.1f %.1f",
                x, hauteur, z, orientation, inclinaison);
        return new Etape(nom, () -> spectateur ? List.of("gamemode spectator", tp) : List.of(tp));
    }

    public static void activerSiDemande() {
        if (Boolean.getBoolean(RoyaumeDesIdees.MODID + ".visite")) {
            NeoForge.EVENT_BUS.addListener(VisiteDev::tick);
            NeoForge.EVENT_BUS.addListener((net.neoforged.neoforge.client.event.ClientChatReceivedEvent evenement) -> {
                java.util.regex.Matcher m = java.util.regex.Pattern.compile("(\\d+) ([+×]) (\\d+)")
                        .matcher(evenement.getMessage().getString());
                if (m.find()) {
                    questionAdeodat = m.group(0);
                }
            });
        }
    }

    private static void tick(ClientTickEvent.Post evenement) {
        Minecraft jeu = Minecraft.getInstance();
        if (jeu.player == null || jeu.getConnection() == null || etape >= ETAPES.size()) {
            return;
        }
        // Une fenêtre qui perd le focus met le jeu solo en pause (serveur arrêté, chunks qui n'arrivent plus).
        jeu.options.pauseOnLostFocus = false;
        if (jeu.screen instanceof net.minecraft.client.gui.screens.PauseScreen) {
            jeu.setScreen(null);
        }
        compteur++;
        if (etape == -1) {
            if (compteur < ATTENTE_DEPART) {
                return;
            }
            jeu.options.hideGui = true;
            jeu.getConnection().sendCommand("gamerule doDaylightCycle false");
            jeu.getConnection().sendCommand("time set 6000");
            jeu.getConnection().sendCommand("weather clear");
            jeu.getConnection().sendCommand("royaume structures");
            commencer(jeu, 0);
            return;
        }
        Etape courante = ETAPES.get(etape);
        if (!actionFaite && compteur >= Math.min(ATTENTE_ETAPE / 2, courante.duree() / 2) && compteur % 10 == 0 && courante.action() != null) {
            actionFaite = courante.action().test(jeu);
            if (!actionFaite && compteur >= courante.duree() - 10) {
                RoyaumeDesIdees.LOGGER.info("[visite] {} : action ECHEC", courante.nom());
            }
        }
        if (compteur < courante.duree()) {
            return;
        }
        String nom = ETAPES.get(etape).nom();
        RoyaumeDesIdees.LOGGER.info("[visite] {} : dimension {}, position {}, musique du Royaume en cours : {}", nom,
                jeu.level.dimension().location(), jeu.player.blockPosition().toShortString(), MusiqueRoyaume.joue());
        long ombres = jeu.level.getEntitiesOfClass(com.royaumedesidees.entite.Ombre.class, jeu.player.getBoundingBox().inflate(80)).size();
        RoyaumeDesIdees.LOGGER.info("[visite] {} : ombres visibles {}, chaînes autour {}, aveuglé ou ténèbres {}, Lanterne dans l'inventaire {}, infobulle « Souvenir » {}",
                nom, ombres, chainesAutour(jeu), jeu.player.hasEffect(MobEffects.BLINDNESS) || jeu.player.hasEffect(MobEffects.DARKNESS),
                jeu.player.getInventory().hasAnyMatching(pile -> pile.is(ModItems.LANTERNE_DIOGENE.get())), infobulleSouvenir(jeu));
        MobEffectInstance culpabilite = jeu.player.getEffect(ModEffets.CULPABILITE);
        RoyaumeDesIdees.LOGGER.info("[visite] {} : vie {}, Culpabilité {}, poires volées {}, poires achetées {}, émeraudes {}", nom,
                jeu.player.getHealth(),
                culpabilite == null ? 0 : culpabilite.getAmplifier() + 1, compter(jeu, ModItems.POIRE_VOLEE.get()),
                compter(jeu, ModItems.POIRE.get()), compter(jeu, net.minecraft.world.item.Items.EMERALD));
        if (nom.equals("porcherie")) {
            long cochons = jeu.level.getEntitiesOfClass(net.minecraft.world.entity.animal.Pig.class,
                    new net.minecraft.world.phys.AABB(-317, 0, -164, -306, 400, -157)).size();
            RoyaumeDesIdees.LOGGER.info("[visite] porcherie : cochons restés dans l'enclos {} {}", cochons, cochons >= 3 ? "OK" : "ECHEC");
        }
        if (nom.startsWith("pnj") || nom.startsWith("bibliotheque") || nom.startsWith("cellule")) {
            for (com.royaumedesidees.pnj.PnjRoyaume pnj : jeu.level.getEntitiesOfClass(com.royaumedesidees.pnj.PnjRoyaume.class,
                    jeu.player.getBoundingBox().inflate(16))) {
                RoyaumeDesIdees.LOGGER.info("[visite] {} : PNJ {} vie {}", nom, pnj.id(), pnj.getHealth());
            }
        }
        java.util.List<com.royaumedesidees.pnj.Monique> moniques = jeu.level.getEntitiesOfClass(com.royaumedesidees.pnj.Monique.class,
                jeu.player.getBoundingBox().inflate(48));
        RoyaumeDesIdees.LOGGER.info("[visite] {} : Monique présente {}, distance {}", nom, moniques.size(),
                moniques.isEmpty() ? "-" : String.format(Locale.ROOT, "%.1f", moniques.get(0).distanceTo(jeu.player)));
        if (nom.startsWith("quete") || nom.startsWith("livre") || nom.startsWith("adeodat")) {
            RoyaumeDesIdees.LOGGER.info("[visite] {} : Livre des Confessions {}, Sceau {}", nom,
                    compter(jeu, ModItems.LIVRE_CONFESSIONS.get()), compter(jeu, ModItems.SCEAU_CONVERSION.get()));
        }
        RoyaumeDesIdees.LOGGER.info("[visite] {} : Grâce reçue {}, jauge visible {}", nom,
                com.royaumedesidees.grace.GracePaquet.recue(), com.royaumedesidees.client.JaugeGrace.visible());
        Screenshot.grab(jeu.gameDirectory, "visite_" + nom + ".png", jeu.getMainRenderTarget(), message -> { });
        if (etape + 1 < ETAPES.size()) {
            commencer(jeu, etape + 1);
        } else {
            etape++;
            // Quitte le monde comme « Sauvegarder et quitter » (le jeu attend la fin de la sauvegarde), puis ferme le
            // jeu : la fenêtre ne reste pas ouverte sur un écran qui pourrait faire croire à un blocage.
            RoyaumeDesIdees.LOGGER.info("[visite] terminee");
            jeu.level.disconnect();
            jeu.disconnect(new GenericMessageScreen(Component.literal("Visite automatique terminée : le jeu se ferme")));
            RoyaumeDesIdees.LOGGER.info("[visite] monde sauvegardé, fermeture du jeu");
            jeu.stop();
        }
    }

    private static int compter(Minecraft jeu, Item item) {
        return jeu.player.getInventory().countItem(item);
    }

    private static int chainesAutour(Minecraft jeu) {
        int nombre = 0;
        for (BlockPos pos : BlockPos.betweenClosed(jeu.player.blockPosition().offset(-1, 0, -1), jeu.player.blockPosition().offset(1, 2, 1))) {
            if (jeu.level.getBlockState(pos).is(ModBlocs.CHAINE_CAVERNE.get())) {
                nombre++;
            }
        }
        return nombre;
    }

    /** Vrai si l'infobulle de la Lanterne contient la ligne « Souvenir du Royaume ». */
    private static boolean infobulleSouvenir(Minecraft jeu) {
        ItemStack lanterne = new ItemStack(ModItems.LANTERNE_DIOGENE.get());
        return lanterne.getTooltipLines(Item.TooltipContext.of(jeu.level), jeu.player, TooltipFlag.NORMAL).stream()
                .anyMatch(ligne -> ligne.getContents() instanceof TranslatableContents traduit
                        && traduit.getKey().equals("tooltip.royaumedesidees.souvenir"));
    }

    private static void commencer(Minecraft jeu, int numero) {
        for (String commande : ETAPES.get(numero).commandes().get()) {
            jeu.getConnection().sendCommand(commande);
        }
        etape = numero;
        compteur = 0;
        actionFaite = false;
    }

    private VisiteDev() {
    }
}
