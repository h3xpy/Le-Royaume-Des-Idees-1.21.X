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
| 5. Biomes et Caverne creusée (fige la carte) | À faire |
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
- 2026-10-01 : pas de décoration vanilla dans la dimension, car son placement dépend de la seed. La végétation (herbes, fleurs, roseaux, neige, chênes, oliviers, épicéas) est posée par `VegetationRoyaume`, qui ne dépend que des coordonnées. Les vergers de poiriers restent pour la v0.2.
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
- 2026-10-01 : frontières de biome ondulées (décalées d'environ ±70 blocs par rapport aux axes). Le relief se fond sur une bande d'environ 50 blocs et les blocs de surface s'entremêlent sur une vingtaine. Les règles de jeu d'une zone (interdiction de rire…) suivront le biome où se trouve le joueur, pour rester cohérentes avec ce qu'il voit.
- 2026-10-01 : le relief des zones :
  - Jardin de Milan : collines herbeuses entre y 94 et 110 ;
  - Port-Royal : vallée en creux centrée en (300, -290), fond plat vers y 83 sans mare au centre (pour l'abbaye), mares à fond de boue et d'argile autour, bords qui remontent vers y 105 ;
  - Puy de Dôme : dôme de lave sans cratère, fidèle au vrai, centré en (260, 260), sommet arrondi vers y 300 avec une fine calotte de neige au-dessus de y 272, roche volcanique qui affleure en haut ; trois petits puys à cratère autour, comme le Pariou, en (470, 120), (110, 460) et (440, 330) ; prairie d'altitude vert sombre avec quelques épicéas au pied ;
  - Hippone : garrigue (herbe, terre nue, podzol, rochers, oliviers), mer intérieure centrée en (-330, 320) d'environ 130 blocs de rayon, ville sur un plateau bas (y 88) centré en (-208, 203) qui descend jusqu'au rivage, sable seulement au bord de l'eau.
- 2026-10-01 : le bord de l'île est découpé (caps, criques, dentelures) et se relève en falaises de hauteur variable (10 à 40 blocs) avant de tomber dans le vide. Sept îlots flottants détachés entourent l'île, entre 770 et 830 blocs du centre. Le dessous de l'île est une masse de roche bombée.
- 2026-10-01 : couleurs d'herbe prévues pour l'étape 5 : vert vif méditerranéen pour Milan, vert-gris terne pour Port-Royal, vert sombre de prairie d'altitude pour le Puy, vert olive sec pour Hippone.
- 2026-10-01 : lumière ambiante de 0,05 (l'Overworld est à 0) ; ciel de type Overworld, dont les couleurs viendront des biomes.
- 2026-10-01 : pas de grottes ni de minerais générés dans le Royaume.
- 2026-10-01 : `ReliefRoyaume` et `BruitRoyaume` ne dépendent pas de Minecraft, pour qu'on puisse dessiner la carte hors du jeu. Ils ne doivent plus être modifiés après la v0.1.
- 2026-10-01 : contrôle de développement `VerificationDev`, actif seulement avec `-Droyaumedesidees.verification=true` : au démarrage d'un monde, il génère quelques chunks du Royaume et compare le bloc de surface au relief prévu. Les essais se font sur une copie temporaire du monde `test`.

## Coordonnées réservées des structures

Carte annotée : `docs/images/carte_relief.png`. Les hauteurs sont celles du sol généré.

| Structure | Version | Position (x, z) | Sol | Notes |
| --- | --- | --- | --- | --- |
| Caverne de Platon | v0.1 | sous (0, 0) | y 40 à 90 | Grande grotte, point d'arrivée fixe à l'intérieur |
| Sortie de la Caverne et portail de retour | v0.1 | vers (0, 45) | y ≈ 100 | Décalée pour laisser le centre à l'Autel |
| Autel de la Cité de Dieu | v1.0 | (0, 0) | y 100 | Au-dessus de la Caverne |
| Arène du boss | v1.0 | ciel au-dessus de (0, 0) | y ≈ 250 | Rayon d'environ 50 blocs, au-dessus des nuages |
| Cité de Dieu (ville céleste) | v1.0 | ciel autour de (0, 0) | y 230 à 300 | Rayon d'environ 160 blocs |
| Figuier d'Augustin | v0.2 | (-260, -260) | y 99 | |
| Villa d'Augustin | v0.2 | (-200, -190) | y 100 | |
| Vergers de poiriers | v0.2 | autour de (-330, -170) | y 102 | |
| Bibliothèque d'Ambroise | v0.3 | (-180, -330) | y 100 | |
| Abbaye de Port-Royal | v0.4 | (300, -290) | y 83 | Fond de vallée sans mare |
| Sommet du Puy (expérience du baromètre) | v0.5 | (260, 260) | y 299 | |
| Poêle de Descartes | v0.5 | (285, 240) | y 274 | Caché sous la neige, près du sommet |
| Florin Périer | v0.5 | (120, 90) | y 94 | Au pied du dôme |
| Ville d'Hippone (remparts, basilique) | v0.5 | (-208, 203) | y 88 | Rayon d'environ 60 blocs |
| Port d'Hippone | v0.5 | (-250, 240) | y 82 | Sur le rivage |
| Tonneau de Diogène | v0.5 | (-420, 110) | y 88 | Dans la garrigue |
| Arrêt du Carrosse, centre | v0.5 | (30, 25) | y 99 | |
| Arrêt du Carrosse, Jardin de Milan | v0.5 | (-220, -120) | y 103 | |
| Arrêt du Carrosse, Port-Royal | v0.5 | (230, -200) | y 83 | |
| Arrêt du Carrosse, Puy de Dôme | v0.5 | (95, 120) | y 94 | |
| Arrêt du Carrosse, Hippone | v0.5 | (-140, 140) | y 89 | |

Aucun arbre n'est généré à moins de 170 blocs du centre, 80 blocs de l'abbaye et 100 blocs de la ville d'Hippone.

## Problèmes connus

(aucun)
