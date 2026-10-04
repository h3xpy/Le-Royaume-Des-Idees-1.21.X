# Sons à fournir

Claude Code ajoute une ligne ici à chaque nouveau son. Toi, tu trouves un fichier libre de droits (CC0, domaine public, ou licence qui autorise la réutilisation), tu le convertis en **.ogg** (Audacity ou un convertisseur en ligne) et tu le déposes au chemin indiqué, avec le nom exact.

Note la source et la licence de chaque fichier dans la dernière colonne, au cas où.

| Fichier | Dossier | Version | Ambiance | Durée | Boucle | Source et licence |
| --- | --- | --- | --- | --- | --- | --- |
| `musique_royaume.ogg` | `src/main/resources/assets/royaumedesidees/sounds/` | v0.1 | Musique de tout le Royaume, à la place de celle de Minecraft (démarre 7 s après l'arrivée, même en créatif, puis revient après 30 s à 5 min de silence) | 17 min 32 s (mono, 48 kHz) | Non (musique) | Fourni : « tolle lege, tolle lege… » (2019), Alexander Garsden. Licence à vérifier : fichier gardé hors du dépôt git (.gitignore), donc absent des builds GitHub. |
| `sanglots.ogg`, `sanglots_2.ogg`, `sanglots_3.ogg` | `src/main/resources/assets/royaumedesidees/sounds/` | v0.2, v0.3 | Pleurs de Monique, qui suit le joueur (plus forts à la Culpabilité III ou plus) | 4 à 5 s chacun, **mono** (pour que le son vienne de Monique) | Non | Fournis par Maxime ; découpés dans son enregistrement de 52 s par `tools/sons/decouper_sanglots.py` (original gardé dans `run/sons_originaux/`, hors git). |
| `musique_culpabilite.ogg` | `src/main/resources/assets/royaumedesidees/sounds/` | v0.2 | Musique dramatique, un peu exagérée (orgue, cordes graves), pour un joueur à la Culpabilité IV ou plus. Remplace toute autre musique | 1 à 3 min | Oui | |

Si un fichier manque, le jeu ne plante pas : le son est juste muet.

Le rire d'Augustin et les pages d'Ambroise utilisent des sons de Minecraft : rien à fournir.

Le portail du Royaume est silencieux : l'ancienne ligne `portail_chant.ogg` a été retirée.
