# Progression

Journal tenu par Claude Code.

## Version en cours

v0.1 — Les fondations, en cours.

| Étape | État |
| --- | --- |
| 1. Transformer le mod d'exemple en notre mod | Fait |
| 2. Data generation et les 6 objets | Fait |
| 3. Pipeline de textures | Fait |
| 4. Dimension : relief de l'île | À faire |
| 5. Biomes et Caverne creusée (fige la carte) | À faire |
| 6. Système de pose des structures | À faire |
| 7. Portail et livre | À faire |
| 8. Caverne jouable | À faire |
| 9. Fin de version (tests, docs, serveur) | À faire |

## Fait

- v0.1 étape 1 : modid `royaumedesidees`, package `com.royaumedesidees`, nom « Le Royaume des Idées », version 0.1.0. Contenu d'exemple du MDK supprimé.
- v0.1 étape 2 : `runData` en place (modèles, blockstates, langues fr/en, tags, recettes, loot tables). Blocs `pierre_ombre`, `pierre_ombre_taillee`, `portail_royaume`, `chaine_caverne` ; items `tolle_lege`, `lanterne_diogene` ; onglet créatif ; tag `lie_au_royaume`. Pas encore de textures (damier violet et noir en jeu).
- v0.1 étape 3 : pipeline `tools/textures/` (palettes des 5 biomes + communes, outils, un script par groupe, `generer_tout.py`). Textures : Pierre d'Ombre, Pierre d'Ombre taillée, Chaîne de la Caverne, portail animé (32 images), Tolle, Lege, Lanterne de Diogène, Ombre (silhouette) et Ombre révélée (prisonnier). Aperçus agrandis dans `build/apercus_textures/`.

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

## Coordonnées réservées des structures

(à remplir pendant la v0.1)

## Problèmes connus

(aucun)
