# Progression

Journal tenu par Claude Code.

## Version en cours

v0.1 — Les fondations, en cours.

| Étape | État |
| --- | --- |
| 1. Transformer le mod d'exemple en notre mod | Fait |
| 2. Data generation et les 6 objets | Fait |
| 3. Pipeline de textures | Fait |
| 4. Dimension : relief de l'île | Fait, à vérifier dans un monde |
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
- 2026-10-01 : pas de végétation ni de minerais vanilla dans la dimension, car leur placement dépend de la seed. Les éléments décoratifs viendront sous forme de structures.
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
- 2026-10-01 : le relief des zones : Jardin de Milan, collines entre y 94 et 110 ; Port-Royal, vallée plate vers y 85 avec des mares (fond en boue et argile) ; Puy de Dôme, cône centré en (480, 470), bord du cratère vers y 299, fond du cratère vers y 274, neige en haut, tuf et basalte sur les flancs ; Hippone, plaine vers y 90, mer intérieure centrée en (-500, 480) d'environ 200 blocs de rayon, plateau plat à y 100 centré en (-337, 324) qui descend vers le rivage.
- 2026-10-01 : le bord de l'île se relève en falaises rocheuses (environ 30 blocs) avant de tomber dans le vide, pour qu'on ne sorte pas de l'île en marchant. Le dessous de l'île est une masse de roche bombée, visible depuis le bord.
- 2026-10-01 : lumière ambiante de 0,05 (l'Overworld est à 0) ; ciel de type Overworld, dont les couleurs viendront des biomes.
- 2026-10-01 : pas de grottes ni de minerais générés dans le Royaume.
- 2026-10-01 : `ReliefRoyaume` et `BruitRoyaume` ne dépendent pas de Minecraft, pour qu'on puisse dessiner la carte hors du jeu. Ils ne doivent plus être modifiés après la v0.1.
- 2026-10-01 : contrôle de développement `VerificationDev`, actif seulement avec `-Droyaumedesidees.verification=true` : au démarrage d'un monde, il génère quelques chunks du Royaume et compare la hauteur du sol au relief prévu.

## Coordonnées réservées des structures

(à remplir pendant la v0.1)

## Problèmes connus

(aucun)
