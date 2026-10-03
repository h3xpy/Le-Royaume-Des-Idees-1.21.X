# Progression

Journal tenu par Claude Code.

## Version en cours

v0.3 — Les PNJ : spec `docs/specs/v0.3-pnj.md` rédigée le 2026-10-03, en attente de validation (six questions).

v0.2 — Le Jardin : spec `docs/specs/v0.2-jardin.md` validée le 2026-10-03. Code terminé et vérifié automatiquement ; checklist `docs/tests/v0.2.md` à passer par Maxime.

| Étape | État |
| --- | --- |
| 1. Blocs, items, effet, Culpabilité, `/confesse`, textures | Fait |
| 2. Structures du Jardin, vérification en jeu, tests | Fait |
| 3. Structures embellies, chemins, confession (10 s, claque) | Fait |
| 4. Sanglots un par un, structures posées au démarrage | Fait ; checklist presque passée par Maxime |

v0.1 — Les fondations : terminée et validée en jeu par Maxime le 2026-10-03 (checklist `docs/tests/v0.1.md` passée, y compris sur serveur local).

| Étape | État |
| --- | --- |
| 1. Transformer le mod d'exemple en notre mod | Fait |
| 2. Data generation et les 6 objets | Fait |
| 3. Pipeline de textures | Fait |
| 4. Dimension : relief de l'île | Fait |
| 4 bis. Révision du relief après relecture | Fait |
| 4 ter. Marais, sols par biome, références, emplacements | Fait |
| 4 quater. Ambiance éthérée, biomes et leurs couleurs | Fait (les biomes de l'étape 5 sont faits) |
| 5. Biomes et Caverne creusée (fige la carte) | Fait ; carte figée le 2026-10-01 |
| 6. Système de pose des structures | Fait |
| 7. Portail et livre | Fait |
| 8. Caverne jouable | Fait |
| 9. Fin de version (tests, docs, serveur) | Fait ; checklist passée par Maxime |

## Fait

- v0.1 étape 1 : modid `royaumedesidees`, package `com.royaumedesidees`, nom « Le Royaume des Idées », version 0.1.0. Contenu d'exemple du MDK supprimé.
- v0.1 étape 2 : `runData` en place (modèles, blockstates, langues fr/en, tags, recettes, loot tables). Blocs `pierre_ombre`, `pierre_ombre_taillee`, `portail_royaume`, `chaine_caverne` ; items `tolle_lege`, `lanterne_diogene` ; onglet créatif ; tag `lie_au_royaume`. Pas encore de textures (damier violet et noir en jeu).
- v0.1 étape 3 : pipeline `tools/textures/` (palettes des 5 biomes + communes, outils, un script par groupe, `generer_tout.py`). Textures : Pierre d'Ombre, Pierre d'Ombre taillée, Chaîne de la Caverne, portail animé (32 images), Tolle, Lege, Lanterne de Diogène, Ombre (silhouette) et Ombre révélée (prisonnier). Aperçus agrandis dans `build/apercus_textures/`.
- v0.1 étape 4 : dimension `royaumedesidees:royaume` (y de 0 à 384) et générateur de chunks Java `GenerateurRoyaume`, qui ne lit jamais la seed. Relief calculé par `ReliefRoyaume` (bruit de Perlin maison à graines fixes) : île flottante d'environ 1000 blocs de rayon, falaises au bord puis vide. Carte vue du dessus : `docs/images/carte_relief.png`. Biome provisoire `minecraft:the_void` jusqu'à l'étape 5.
- v0.1 étape 6 : système de pose des structures (`structures/`). Registre `StructuresRoyaume` (identifiant, version, boîte englobante, constructeur Java), sauvegarde `DonneesStructures` dans le dossier de la dimension (`data/royaumedesidees_structures.dat`), pose ou remplacement quand un joueur passe à moins de 96 blocs (contrôle une fois par seconde), commandes opérateur `/royaume structures` et `/royaume structures reposer <id>`. Structures : `caverne` (écran du mur des ombres, muret, feu, poteaux et chaînes) et `portail_retour` (cadre en Pierre d'Ombre à la sortie du tunnel).
- v0.1 étape 7 : portail du Royaume. Clic droit avec Tolle, Lege sur un cadre de bibliothèques complet : l'intérieur se remplit de portail (le livre n'est pas consommé). Aller vers le point d'arrivée fixe de la Caverne, retour par le portail de Pierre d'Ombre vers le portail de départ (mémorisé pour chaque joueur, conservé à la mort), ou vers le point d'apparition du monde s'il a disparu. Le portail s'éteint si son cadre est cassé. Tolle, Lege dans environ 15 % des coffres de village (vérifié : 151 sur 1000). Portail silencieux ; musique du Royaume à la place (voir les décisions).

- v0.1 étape 8 : Caverne jouable. Première entrée enchaînée (cage de 17 Chaînes de la Caverne autour du joueur), entrées suivantes libres à côté. Entité `ombre` : silhouette noire semi-transparente, intouchable, révélée en prisonnier par la Lanterne de Diogène tenue à moins de 8 blocs, attaque faiblement (1 dégât), 10 PV, lâche 1 ou 2 Pierres d'Ombre. Six Ombres entretenues devant le mur tant qu'un joueur est dans la Caverne. Sortie à l'air libre au débouché du tunnel : aveuglement 5 s, message, succès « Allégorie vécue » (onglet de succès du Royaume), Lanterne à la première sortie. Infobulle « Souvenir du Royaume » hors de la dimension pour tous les objets du tag `lie_au_royaume`.

- v0.1 étape 9 : checklist de tests en jeu `docs/tests/v0.1.md`. Le test sur serveur local (`runServer`) reste à faire par Maxime : il demande d'accepter la licence de Minecraft (`run/eula.txt`), ce que Claude ne fait pas à sa place.
- v0.2 étape 1 : poires (achetée, volée), feuilles de poirier (propriété `poires`, cueillette = vol, repousse lente), bois, planches et feuilles de figuier, Confessionnal (deux blocs de haut, fabricable), Étal du verger (émeraude → 3 poires), effet Culpabilité I à V (lenteur, nuage, sanglots, musique, message), commande `/confesse`, voix « Prends, lis » au figuier, textures `tools/textures/jardin_v02.py`.
- v0.2 étape 2 : structures `confessionnal_centre`, `figuier`, `villa_augustin` et `vergers` (`structures/StructuresJardin.java`), qui suivent le relief. Trois vergers clos de 8 poiriers (Lucius, Sévère, Vérécundus), panneaux traduisibles. Vérification automatique étendue : blocs clés des structures, Culpabilité insensible au lait, et une visite en survie (3 vols → Culpabilité III, `effect clear` et mort sans effet, confession refusée loin puis acceptée, anti-spam, achat à l'Étal).
- v0.2 étape 3 : structures refaites en version 2. Villa : domus à atrium, impluvium, tablinum (Livres des Platoniciens, table de jeu), chambres, triclinium, cuisine, cellier, toit à compluvium, jardin à colonnade au nord. Vergers : murets moussus, allée, vigne de Patricius, porcherie. Jardin du figuier : haie, grand figuier, exèdre d'Alypius avec l'Épître aux Romains, puits, maison voisine. Confessionnal sous un baldaquin. Deux chemins (`chemin_villa_vergers`, `chemin_villa_figuier`). Outils communs : `Decor` (escaliers, plantes, panneaux, livres, cyprès, animaux) et le raccord des clôtures, murets, escaliers et vitres en fin de pose (`Pose.poserRaccorde`). Confession : 10 s entre deux confessions, claque si le même péché est répété.
- v0.2 étape 4 : les sanglots ne se superposent plus (un à la fois, 53 s, puis 10 à 40 s de silence ; arrêt quand la Culpabilité retombe sous III ; son lu en flux). Toutes les structures sont posées au démarrage du serveur (8 structures en 1,7 s), la pose de proximité reste en filet de sécurité. Une nouvelle pose de la porcherie remplace ses cochons au lieu d'en ajouter.

## Décisions

- 2026-10-01 : génération du monde figée dès la v0.1, structures posées par le mod (voir CLAUDE.md).
- 2026-10-01 : pas de décoration vanilla dans la dimension, car son placement dépend de la seed. La végétation (herbes, fleurs, plantes d'eau, neige, buissons, cyprès, pins parasols, saules, épicéas, hêtres, oliviers) est posée par `VegetationRoyaume`, qui ne dépend que des coordonnées. Les vergers de poiriers restent pour la v0.2.
- 2026-10-01 : les lanternes du portail sont posées sur ou devant les 4 blocs de coin du cadre ; les lanternes normales et d'âme comptent.
- 2026-10-01 : en v0.1, la Lanterne de Diogène révèle seulement les Ombres (la vision des joueurs à travers les murs viendra plus tard).
- 2026-10-01 : l'arrivée enchaînée est par joueur, à sa première entrée.
- 2026-10-01 : licence « All Rights Reserved », celle du MDK.
- 2026-10-01 : `Config` du MDK supprimé, aucun réglage n'est nécessaire en v0.1.
- 2026-10-01 : Pierre d'Ombre taillée : 4 Pierres d'Ombre en carré donnent 4 Pierres taillées (comme les briques de pierre), et 1 pour 1 au tailleur de pierre.
- 2026-10-01 : la Chaîne de la Caverne est un bloc plein (on ne peut pas passer à travers), qui se casse à la main en moins d'une seconde.
- 2026-10-01 : la Pierre d'Ombre a la dureté de la pierre et demande une pioche pour être récupérée.
- 2026-10-01 : l'Ombre a deux textures : `ombre` (silhouette noire semi-transparente) et `ombre_revelee` (un prisonnier de la Caverne en tunique, sa « vraie forme » sous la Lanterne).
- 2026-10-01 : la couverture de Tolle, Lege porte un cœur enflammé doré, attribut traditionnel de saint Augustin.
- 2026-10-01 : niveau de l'eau à y = 80. Plaine centrale vers y = 100 (la Caverne, entre y 40 et 90, aura un plafond d'au moins 10 blocs).
- 2026-10-01 (révisé après relecture) : île de 1 300 blocs de diamètre (rayon 650) au lieu de 2 000, pour que les structures ne soient pas noyées dans le vide.
- 2026-10-01 : frontières de biome ondulées (décalées d'environ ±100 blocs par rapport aux axes). Le relief se fond sur une bande d'environ 50 blocs et les blocs de surface s'entremêlent sur une vingtaine. Les règles de jeu d'une zone (interdiction de rire…) suivront le biome où se trouve le joueur, pour rester cohérentes avec ce qu'il voit.
- 2026-10-01 : le relief des zones (coupes chiffrées : `docs/images/coupes_relief.png`) :
  - Jardin de Milan : collines herbeuses entre y 94 et 110 ;
  - Port-Royal : vallée en creux centrée en (300, -290). Le fond est un vrai marais qui ondule autour du niveau de l'eau (flaques, chenaux, étangs), avec une butte sèche à y 84 pour l'abbaye. Les bords remontent vers y 105 à 125 ;
  - Puy de Dôme : dôme de lave sans cratère, fidèle au vrai, centré en (260, 260), sommet arrondi à y 300, fine calotte de neige au-dessus de y 272 ; trois petits puys à cratère autour, comme le Pariou, en (470, 120), (110, 460) et (440, 330) ;
  - Hippone : garrigue, mer intérieure centrée en (-330, 320) d'environ 130 blocs de rayon, ville sur un plateau bas (y 88) centré en (-208, 203) qui descend jusqu'au rivage, sable seulement au bord de l'eau.
- 2026-10-01 : sols adaptés à chaque biome, et roche nue sur les pentes raides (3 blocs de dénivelé ou plus) au lieu de laisser voir la terre :
  - Jardin de Milan : herbe sur terre de jardin, pierre et andésite dans les pentes ;
  - Port-Royal : marais en herbe détrempée et boue à nu, sur de la boue puis de l'argile ; coteaux en herbe sur argile ;
  - Puy de Dôme : dôme en trachyte clair (diorite et andésite, la « domite » du vrai Puy) sous une herbe rase ; petits puys en scories (tuf, basalte, pouzzolane rouge) ; plaine en prairie sur tuf ;
  - Hippone : garrigue (herbe sèche, quelques plaques de terre nue et de gravier) sur terre rouge méditerranéenne (terre cuite) et calcaire (grès, calcite qui affleure).
- 2026-10-01 : le bord de l'île est découpé (caps, criques, dentelures) et se relève en falaises de hauteur variable (10 à 40 blocs) avant de tomber dans le vide. Sept îlots flottants détachés entourent l'île, entre 770 et 830 blocs du centre. Le dessous de l'île est une masse de roche pleine et bombée, y compris sous le Puy.
- 2026-10-01 : couleurs d'herbe prévues pour l'étape 5 : vert vif méditerranéen pour Milan, vert-gris terne pour Port-Royal, vert sombre de prairie d'altitude pour le Puy, vert olive sec pour Hippone. Brume possible sur le marais de Port-Royal (couleur du brouillard du biome).
- 2026-10-01 : la Cité de Dieu et l'arène sont réservées au plus haut de la dimension, entre y 320 et 370, au-dessus du sommet du Puy (y 300). Le Puy garde sa hauteur pour la mécanique de pression.
- 2026-10-01 : ambiance éthérée (aperçus : `docs/images/apercu_*.jpg`) :
  - cinq biomes, `caverne_platon`, `jardin_milan`, `port_royal`, `puy_de_dome` et `hippone`, répartis par `SourceBiomesRoyaume` (zone de surface, et un ellipsoïde fixe sous le centre pour la Caverne, de y 39 à 91 et de 70 blocs de rayon) ;
  - ciels pâles (pervenche, perle, bleu froid), brumes claires (lavande, blanc nacré, dorée à Hippone), eau turquoise, herbes et feuillages éclaircis ;
  - particules dans l'air : pétales roses au Jardin, poussière blanche à Port-Royal, au Puy et à Hippone, cendres dans la Caverne ;
  - lumière ambiante de 0,1 (l'Overworld est à 0) : les ombres ne sont jamais tout à fait noires ;
  - falaises du bord et dessous de l'île entièrement en roche claire (calcite, diorite), avec du lichen lumineux et quelques fleurs de spores qui pendent dans le vide ;
  - les marches d'un ou deux blocs sont bordées de mousse au lieu de montrer le flanc de terre des blocs d'herbe ;
  - Puy de Dôme : température choisie pour que la neige ne tombe qu'au-dessus de y ≈ 264.
- 2026-10-01 : pas de grottes ni de minerais générés dans le Royaume, à part la Caverne de Platon.
- 2026-10-01 : la Caverne de Platon (`CaverneRoyaume`, coupe : `docs/images/coupe_caverne.png`) suit l'allégorie :
  - une salle sombre (roche des abîmes, tuf, basalte) sous le centre : un dôme posé sur un sol presque plat (y 46) qui s'étend jusqu'aux parois, à environ 58 blocs du centre, voûte jusqu'à y ≈ 84. (Corrigé le 2026-10-03 : la première forme, en ballon couché, n'avait de sol plat que sur 24 blocs de rayon, et les Ombres apparaissaient dans la roche devant le mur.) Le biome de la Caverne est un cylindre de 70 blocs de rayon entre y 38 et 92 ;
  - les prisonniers arrivent en (0, 47, -20), tournés vers le nord, face au mur des ombres (paroi nord, vers z = -50) ; le feu brûle derrière eux et en hauteur, sur une butte de roche en (0, 10), 4 blocs au-dessus du sol ;
  - derrière le feu, une rampe de roche monte du sol de la salle jusqu'à un tunnel de 5 blocs de large qui serpente vers le sud (un bloc de montée pour deux d'avancée) et débouche à l'air libre vers (0, 125), à y ≈ 91 ;
  - les parois du tunnel s'éclaircissent en montant (roche des abîmes, tuf, pierre, puis calcite près de la sortie) : on remonte littéralement vers la lumière ;
  - le mobilier (chaînes, mur des ombres, feu) sera posé par le système de structures à l'étape 6.
- 2026-10-01 : `ReliefRoyaume` et `BruitRoyaume` ne dépendent pas de Minecraft, pour qu'on puisse dessiner la carte hors du jeu. Ils ne doivent plus être modifiés après la v0.1.
- 2026-10-01 : visite de développement `VisiteDev`, active seulement avec `-Droyaumedesidees.visite=true` : sur une copie du monde de test, le joueur passe en spectateur, visite cinq points de vue en plein jour et prend une capture à chacun (`run/screenshots/visite_*.png`).
- 2026-10-01 : contrôle de développement `VerificationDev`, actif seulement avec `-Droyaumedesidees.verification=true` : au démarrage d'un monde, il génère quelques chunks du Royaume et compare le bloc de surface et le biome au relief prévu. Les essais se font sur une copie temporaire du monde `test`.
- 2026-10-01 : carte figée (relief, biomes, végétation, Caverne) après l'accord pour passer à l'étape 6. `ReliefRoyaume`, `BruitRoyaume`, `VegetationRoyaume`, `CaverneRoyaume`, `SourceBiomesRoyaume` et le choix des blocs dans `GenerateurRoyaume` ne doivent plus changer.
- 2026-10-01 : pour remplacer une structure (version augmentée ou commande `reposer`), sa boîte est d'abord remise dans l'état exact de la génération (le terrain est recalculé, puisqu'il ne dépend que des coordonnées), puis la nouvelle version est posée. La végétation de la boîte n'est pas recréée. Les blocs sont posés sans prévenir leurs voisins, pour ne rien déclencher hors de la boîte.
- 2026-10-01 : la Caverne est éclairée sur l'écran des ombres par des blocs de lumière invisibles (niveau 11) : le feu seul, à 55 blocs, ne l'éclairerait pas.
- 2026-10-01 : le mobilier de la Caverne est en briques d'ardoise et pierre noire, pas en Pierre d'Ombre, pour qu'on ne puisse pas récolter de Pierre d'Ombre sans tuer d'Ombres. Seul le portail de retour est en Pierre d'Ombre, comme le demande la spec.
- 2026-10-01 : le portail de retour est un cadre de 4 sur 5 en Pierre d'Ombre taillée, de x 6 à 9 dans le plan z = 130, sur une plateforme en Pierre d'Ombre, avec deux lanternes d'âme sur les coins du haut. Son intérieur ne téléporte pas encore (étape 7).
- 2026-10-01 : les lanternes du portail comptent si elles touchent un coin du cadre par n'importe quelle face hors du cadre (dessus, à côté, devant ou derrière). Elles ne servent qu'à l'allumage : retirer une lanterne n'éteint pas un portail déjà ouvert.
- 2026-10-01 : seuls les joueurs passent le portail (ni mobs ni objets). Comme au Nether, il faut y rester environ 4 secondes, et c'est immédiat en créatif (mêmes règles de jeu que le portail du Nether).
- 2026-10-01 : on ne peut pas allumer de portail dans le Royaume : le livre répond « Ici, on ne lit plus : on cherche la sortie. » Le seul portail du Royaume est celui de retour.
- 2026-10-01 : le portail est silencieux ; seules des lettres dorées (particules de table d'enchantement) s'en échappent. À la place, la musique `musique_royaume` joue dans les cinq biomes du Royaume (elle remplace celle de Minecraft, 7 secondes après l'arrivée, y compris en créatif, puis revient après 30 secondes à 5 minutes de silence). Le fichier fourni (« tolle lege, tolle lege… », Alexander Garsden, 2019) est probablement sous droit d'auteur : il reste hors du dépôt git, et les .jar construits par GitHub n'ont donc pas de musique.
- 2026-10-03 (relecture de l'allégorie) : les Ombres sont, comme chez Platon, les ombres de statues de bois portées derrière le muret. Non révélées, elles glissent le long de l'écran du mur nord, aplaties contre la paroi, toujours tournées vers les prisonniers et une fois et demie plus grandes que la statue (une ombre portée par un feu proche est agrandie). Révélées par la Lanterne, elles se détachent du mur : ce sont des statues de bois sculptées. Les détruire affiche « Ce n'était qu'une statue de bois. Diogène cherche toujours un homme. »
- 2026-10-03 : en redescendant dans la Caverne après être sorti à la lumière, 10 secondes de ténèbres et le message « Revenu dans la Caverne, tes yeux pleins de soleil ne voient plus que des ténèbres. » (Platon : le prisonnier qui redescend a « les yeux pleins de ténèbres »).
- 2026-10-03 : structure `caverne` passée en version 2 (feu sur une butte) : elle sera reposée d'elle-même dans les mondes où la version 1 a été posée.
- 2026-10-03 : l'Ombre est révélée tant qu'un joueur tient la Lanterne en main (ou dans l'autre main) à moins de 8 blocs ; elle ne l'est jamais hors du Royaume. Non révélée, un coup la traverse avec un peu de fumée et le message « Ce n'est qu'une ombre : ton coup la traverse. » Elle n'a pas d'ombre au sol.
- 2026-10-03 : la Lanterne est donnée par magie à la première sortie (« une vieille lanterne t'attendait »), pas par un PNJ : Diogène n'arrive qu'en v0.5. Si l'inventaire est plein, elle tombe aux pieds du joueur.
- 2026-10-03 : la sortie se déclenche à chaque fois qu'un joueur remonte de la Caverne ou du tunnel jusqu'à l'air libre au débouché du tunnel (aveuglement et message à chaque fois, succès et Lanterne la première fois seulement).
- 2026-10-03 : la visite de développement ferme le jeu toute seule après avoir sauvegardé, pour qu'on ne la prenne pas pour un blocage.
- 2026-10-03 : les quatre propositions de la spec v0.2 sont acceptées (Étal sans PNJ, sanglots en attendant Monique, Culpabilité partout, figuier décor avec « Prends, lis »).
- 2026-10-03 : trois vergers au lieu de deux, pour la « vingtaine de poiriers » de la spec. Le troisième appartient à Vérécundus, l'ami milanais d'Augustin (celui qui lui prêta sa villa de Cassiciacum).
- 2026-10-03 : la visite de développement empêche la pause du jeu solo quand la fenêtre perd le focus (sinon les chunks n'arrivent plus et les clics échouent).
- 2026-10-03 : détails tirés des *Confessions* pour le Jardin, au-delà de la spec : la table de jeu où Ponticianus trouva les épîtres de Paul (VIII, 6), le petit jardin de la maison (VIII, 8), le banc d'Alypius où le livre était resté (VIII, 12), la maison voisine d'où venait la voix, la vigne de la famille près du poirier et les porcs à qui les poires furent jetées (II, 4). Les versets cités sont dans des traductions du domaine public (Segond 1910, King James) ; les phrases des Confessions sont traduites par Claude.
- 2026-10-03 : des cochons apparaissent avec la porcherie (une fois, à la pose). Le sol de l'enclos est aplani et rien n'est posé contre la clôture, sinon ils s'échappent.
- 2026-10-03 : les chemins sont des structures séparées dont la boîte ne chevauche aucun bâtiment, car remplacer une structure remet toute sa boîte dans l'état d'origine.
- 2026-10-03 : à la demande de Maxime, les structures sont là dès la création du monde. Elles restent posées par le système de pose (pas par la génération du monde, comme le veut CLAUDE.md), mais au démarrage du serveur au lieu d'attendre qu'un joueur approche.
- 2026-10-03 : la console du serveur local reçoit ce qu'on tape dans le terminal (`build.gradle`, tâche `runServer`) : taper `stop` l'arrête proprement. En développement, la licence de Minecraft est acceptée d'office : pas de `eula.txt` à remplir.

## Coordonnées réservées des structures

Carte annotée : `docs/images/carte_relief.png`. Les hauteurs sont celles du sol généré.

| Structure | Version | Position (x, z) | Sol | Notes |
| --- | --- | --- | --- | --- |
| Caverne de Platon | v0.1 | sous (0, 0) | y 40 à 90 | Grande grotte, point d'arrivée fixe à l'intérieur |
| Point d'arrivée dans la Caverne | v0.1 | (0, -20) | y 47 | Tourné vers le nord, face au mur des ombres |
| Mur des ombres | v0.1 | paroi nord, vers (0, -50) | y 47 à 75 | |
| Feu de la Caverne | v0.1 | (0, 10) | y 47 | Derrière les prisonniers |
| Sortie de la Caverne | v0.1 | (0, 125) | y ≈ 91 | Débouché du tunnel, à 125 blocs au sud de l'Autel : le passage n'est pas sous l'Autel |
| Portail de retour (structure `portail_retour`) | v0.1 | x 6 à 9, z 130 | y 91 | À côté de la sortie du tunnel |
| Autel de la Cité de Dieu | v1.0 | (0, 0) | y 100 | Au-dessus de la Caverne, 25 blocs dégagés autour |
| Confessionnal (structure `confessionnal_centre`) | v0.2 | (18, -14) | y 100 | Près de l'Autel, au centre ; posé |
| Arène du boss | v1.0 | ciel au-dessus de (0, 0) | y ≈ 330 | Rayon d'environ 50 blocs |
| Cité de Dieu (ville céleste) | v1.0 | ciel autour de (0, 0) | y 320 à 370 | Rayon d'environ 160 blocs, au-dessus du sommet du Puy |
| Figuier d'Augustin (structure `figuier`) | v0.2 | x -276 à -244, z -280 à -244 | y 99 | Posé ; jardin, maison voisine au nord |
| Villa d'Augustin (structure `villa_augustin`) | v0.2 | x -216 à -184, z -220 à -174 | y 100 | Posée ; Étal devant l'entrée sud, jardin au nord |
| Vergers de poiriers (structure `vergers`) | v0.2 | x -356 à -294, z -188 à -150 | y 102 | Posés ; trois vergers, vigne, porcherie |
| Chemin villa ↔ vergers | v0.2 | x -293 à -217, z ≈ -204 à -162 | sol | Posé |
| Chemin villa ↔ figuier | v0.2 | x -243 à -193, z -262 à -221 | sol | Posé |
| Bibliothèque d'Ambroise | v0.3 | (-180, -330) | y 100 | |
| Abbaye de Port-Royal | v0.4 | (300, -290) | y 84 | Butte sèche au milieu du marais |
| Collège de Clermont (camp des Jésuites) | v0.4 | (380, -80) | y 99 | Sud de Port-Royal, côté Puy : zone frontière |
| Sommet du Puy (expérience du baromètre) | v0.5 | (260, 260) | y 300 | |
| Poêle de Descartes | v0.5 | (305, 305) | y 274 | Caché sous la neige, versant sud-est, à l'opposé du chemin de montée |
| Florin Périer | v0.5 | (112, 128) | y 103 | Au pied du flanc nord-ouest, près de l'arrêt du Carrosse |
| Ville d'Hippone (remparts) | v0.5 | (-208, 203) | y 88 | Rayon d'environ 60 blocs |
| Basilique d'Hippone (déclenche le siège) | v0.5 | (-200, 195) | y 88 | Dans la ville |
| Port d'Hippone | v0.5 | (-250, 240) | y 82 | Sur le rivage |
| Tonneau de Diogène | v0.5 | (-420, 110) | y 88 | Dans la garrigue |
| Arrêt du Carrosse, centre | v0.5 | (30, 25) | y 99 | |
| Arrêt du Carrosse, Jardin de Milan | v0.5 | (-220, -120) | y 103 | |
| Arrêt du Carrosse, Port-Royal | v0.5 | (180, -150) | y 90 | Sur le coteau, au sec |
| Arrêt du Carrosse, Puy de Dôme | v0.5 | (119, 119) | y 105 | Au pied du flanc nord-ouest : l'ascension commence là |
| Arrêt du Carrosse, Hippone | v0.5 | (-140, 140) | y 89 | |

Aucun arbre n'est généré à moins de 170 blocs du centre, 70 blocs de l'abbaye et 100 blocs de la ville d'Hippone.

## Usage prévu des zones extérieures (proposition, à confirmer)

Les points d'intérêt sont regroupés à moins de 300 blocs du centre. La moitié extérieure de chaque quartier servira à :

| Zone | Usage | Version |
| --- | --- | --- |
| Jardin de Milan, nord-ouest | Apparition des Manichéens (Augustin a été manichéen avant Milan) ; vergers sauvages | v0.5 |
| Port-Royal, sud | Collège de Clermont et rondes de nuit des Jésuites, au contact du Puy | v0.4 |
| Port-Royal, est | Coteaux où récolter de quoi faire l'Encre et le Parchemin | v0.4 |
| Puy de Dôme, est | Petits puys : gisements de Mercure (cinabre) dans les cratères, sable volcanique pour le Verre de Clermont | v0.5 |
| Hippone, ouest et sud | Oliveraies (Huile d'olive), campement des Vandales d'où partent les vagues du siège | v0.5 |
| Les 7 îlots flottants | Une page des Pensées « hors d'atteinte » par îlot : il faut construire ou ruser pour l'atteindre | v0.5 |
| Partout ailleurs | Les 20 autres pages des Pensées, environ 5 par quartier, dans les zones extérieures | v0.5 |

## Correspondance avec les vrais lieux

| Élément | Référence réelle | Rendu dans le Royaume |
| --- | --- | --- |
| Jardin de Milan | Jardin de la maison d'Augustin à Milan, où il entend « prends, lis » en 386 | Collines de Lombardie, cyprès et pins parasols d'un jardin de villa italienne, lauriers, coquelicots, rosiers |
| Port-Royal | Port-Royal des Champs, abbaye janséniste au fond de la vallée marécageuse et malsaine du Rhodon, que les Solitaires ont travaillé à assécher | Vallée en creux, marais à flaques et étangs, boue et argile, saules, roseaux, nénuphars, aucune fleur |
| Puy de Dôme | Dôme de lave en trachyte (la « domite »), sans cratère, entouré d'autres puys de la chaîne, dont le Pariou qui en a un | Dôme arrondi en roche claire, trois petits puys à cratère en scories, prairie, épicéas et hêtres au pied |
| Neige au sommet | Le vrai Puy n'est enneigé qu'en hiver | Fine calotte gardée exprès, pour cacher le Poêle de Descartes (stylisation assumée) |
| Poêle de Descartes | Nuit du 10 novembre 1619, en Allemagne ; Descartes a aussi affirmé avoir soufflé à Pascal l'idée de l'expérience du Puy de Dôme en 1647 | Pièce cachée sous la neige du Puy : de quoi faire dialoguer Descartes et Florin Périer en v0.5 |
| Hippone | Hippo Regius, port romain d'Afrique du Nord (Annaba, en Algérie), région fertile | Garrigue sur terre rouge et calcaire, oliviers, quelques pins, lentisques, ville au bord de l'eau avec un port |
| Collège de Clermont | Collège jésuite de Paris, au cœur de la querelle des Provinciales | Camp des Jésuites au sud de Port-Royal |
| Vol des poires | En réalité à Thagaste, dans la jeunesse d'Augustin, et non à Milan | Placé au Jardin de Milan par la conception (choix de jeu, à signaler dans les textes) |
| Cité de Dieu | Œuvre d'Augustin écrite après le sac de Rome en 410 | Ville céleste au plus haut de la dimension |
| Caverne : les prisonniers | Enchaînés depuis l'enfance, face à la paroi du fond (*République*, livre VII) | Arrivée enchaînée face à l'écran du mur des ombres |
| Caverne : le feu | Il brûle derrière les prisonniers, en hauteur | Feu sur une butte de roche derrière eux |
| Caverne : le muret | Entre le feu et les prisonniers, un chemin longé d'un muret, comme le paravent des montreurs de marionnettes | Muret entre l'arrivée et le feu |
| Caverne : les ombres | Ombres de statues d'hommes et d'animaux, en bois et en pierre, portées le long du muret et projetées sur la paroi | Ombres plates et agrandies qui glissent sur l'écran ; révélées, ce sont des statues de bois (seulement des hommes : le modèle ne permet pas d'animaux) |
| Caverne : la sortie | Le prisonnier libéré monte une pente rude et longue, ébloui par le soleil | Rampe et tunnel qui s'éclaircit, aveuglement à la sortie |
| Caverne : le retour | Redescendu, il a « les yeux pleins de ténèbres » | Ténèbres en redescendant dans la salle |
| Lanterne de Diogène | Diogène cherchait « un homme » en plein jour avec une lanterne | La Lanterne révèle que les ombres ne sont que des statues : toujours pas d'homme |

## Problèmes connus

- Si la porcherie est reposée (nouvelle version) au démarrage d'un monde où ses cochons sont déjà sauvegardés, les anciens ne sont pas encore chargés et ne peuvent pas être retirés : il peut alors y en avoir 6. Sans conséquence, à revoir si la porcherie change de version.
