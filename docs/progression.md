# Progression

Journal tenu par Claude Code.

## Version en cours

v0.1 — Les fondations, en cours.

| Étape | État |
| --- | --- |
| 1. Transformer le mod d'exemple en notre mod | Fait |
| 2. Data generation et les 6 objets | Fait |
| 3. Pipeline de textures | Fait |
| 4. Dimension : relief de l'île | Fait |
| 4 bis. Révision du relief après relecture | Fait |
| 4 ter. Marais, sols par biome, références, emplacements | Fait |
| 4 quater. Ambiance éthérée, biomes et leurs couleurs | Fait (les biomes de l'étape 5 sont faits) |
| 5. Biomes et Caverne creusée (fige la carte) | Biomes faits ; reste la Caverne |
| 6. Système de pose des structures | À faire |
| 7. Portail et livre | À faire |
| 8. Caverne jouable | À faire |
| 9. Fin de version (tests, docs, serveur) | À faire |

## Fait

- v0.1 étape 1 : modid `royaumedesidees`, package `com.royaumedesidees`, nom « Le Royaume des Idées », version 0.1.0. Contenu d'exemple du MDK supprimé.
- v0.1 étape 2 : `runData` en place (modèles, blockstates, langues fr/en, tags, recettes, loot tables). Blocs `pierre_ombre`, `pierre_ombre_taillee`, `portail_royaume`, `chaine_caverne` ; items `tolle_lege`, `lanterne_diogene` ; onglet créatif ; tag `lie_au_royaume`. Pas encore de textures (damier violet et noir en jeu).
- v0.1 étape 3 : pipeline `tools/textures/` (palettes des 5 biomes + communes, outils, un script par groupe, `generer_tout.py`). Textures : Pierre d'Ombre, Pierre d'Ombre taillée, Chaîne de la Caverne, portail animé (32 images), Tolle, Lege, Lanterne de Diogène, Ombre (silhouette) et Ombre révélée (prisonnier). Aperçus agrandis dans `build/apercus_textures/`.
- v0.1 étape 4 : dimension `royaumedesidees:royaume` (y de 0 à 384) et générateur de chunks Java `GenerateurRoyaume`, qui ne lit jamais la seed. Relief calculé par `ReliefRoyaume` (bruit de Perlin maison à graines fixes) : île flottante d'environ 1000 blocs de rayon, falaises au bord puis vide. Carte vue du dessus : `docs/images/carte_relief.png`. Biome provisoire `minecraft:the_void` jusqu'à l'étape 5.

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
- 2026-10-01 : pas de grottes ni de minerais générés dans le Royaume.
- 2026-10-01 : `ReliefRoyaume` et `BruitRoyaume` ne dépendent pas de Minecraft, pour qu'on puisse dessiner la carte hors du jeu. Ils ne doivent plus être modifiés après la v0.1.
- 2026-10-01 : visite de développement `VisiteDev`, active seulement avec `-Droyaumedesidees.visite=true` : sur une copie du monde de test, le joueur passe en spectateur, visite cinq points de vue en plein jour et prend une capture à chacun (`run/screenshots/visite_*.png`).
- 2026-10-01 : contrôle de développement `VerificationDev`, actif seulement avec `-Droyaumedesidees.verification=true` : au démarrage d'un monde, il génère quelques chunks du Royaume et compare le bloc de surface et le biome au relief prévu. Les essais se font sur une copie temporaire du monde `test`.

## Coordonnées réservées des structures

Carte annotée : `docs/images/carte_relief.png`. Les hauteurs sont celles du sol généré.

| Structure | Version | Position (x, z) | Sol | Notes |
| --- | --- | --- | --- | --- |
| Caverne de Platon | v0.1 | sous (0, 0) | y 40 à 90 | Grande grotte, point d'arrivée fixe à l'intérieur |
| Sortie de la Caverne et portail de retour | v0.1 | (0, 60) | y 100 | En surface, à 60 blocs au sud de l'Autel : le passage n'est pas sous l'Autel |
| Autel de la Cité de Dieu | v1.0 | (0, 0) | y 100 | Au-dessus de la Caverne, 25 blocs dégagés autour |
| Confessionnal | v0.2 | (18, -14) | y 100 | Près de l'Autel, au centre |
| Arène du boss | v1.0 | ciel au-dessus de (0, 0) | y ≈ 330 | Rayon d'environ 50 blocs |
| Cité de Dieu (ville céleste) | v1.0 | ciel autour de (0, 0) | y 320 à 370 | Rayon d'environ 160 blocs, au-dessus du sommet du Puy |
| Figuier d'Augustin | v0.2 | (-260, -260) | y 99 | |
| Villa d'Augustin | v0.2 | (-200, -190) | y 100 | |
| Vergers de poiriers | v0.2 | autour de (-330, -170) | y 102 | |
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

## Problèmes connus

(aucun)
