# Sons à fournir

Claude Code ajoute une ligne ici à chaque nouveau son. Toi, tu trouves un fichier libre de droits (CC0, domaine public, ou licence qui autorise la réutilisation), tu le convertis en **.ogg** (Audacity ou un convertisseur en ligne) et tu le déposes au chemin indiqué, avec le nom exact.

Note la source et la licence de chaque fichier dans la dernière colonne, au cas où.

| Fichier | Dossier | Version | Ambiance | Durée | Boucle | Source et licence |
| --- | --- | --- | --- | --- | --- | --- |
| `musique_royaume.ogg` | `src/main/resources/assets/royaumedesidees/sounds/` | v0.1 | Musique de tout le Royaume, à la place de celle de Minecraft (démarre 7 s après l'arrivée, même en créatif, puis revient après 30 s à 5 min de silence) | 17 min 32 s (mono, 48 kHz) | Non (musique) | Fourni : « tolle lege, tolle lege… » (2019), Alexander Garsden. Licence à vérifier : fichier gardé hors du dépôt git (.gitignore), donc absent des builds GitHub. |
| `sanglots.ogg` | `src/main/resources/assets/royaumedesidees/sounds/` | v0.2 | Sanglots lointains, étouffés (une mère qui pleure son fils : Monique, en attendant qu'elle arrive en v0.3). Joué de temps en temps autour d'un joueur à la Culpabilité III ou plus | 3 à 8 s | Non | |
| `musique_culpabilite.ogg` | `src/main/resources/assets/royaumedesidees/sounds/` | v0.2 | Musique dramatique, un peu exagérée (orgue, cordes graves), pour un joueur à la Culpabilité IV ou plus. Remplace toute autre musique | 1 à 3 min | Oui | |
| `augustin_rire.ogg` | `src/main/resources/assets/royaumedesidees/sounds/` | v0.3 | Rire franc d'un jeune homme (Augustin le fêtard), quand on le frappe | 1 à 2 s | Non | |
| `ambroise_page.ogg` | `src/main/resources/assets/royaumedesidees/sounds/` | v0.3 | Une page de parchemin qu'on tourne, très doucement (Ambroise lit en silence) | 1 s | Non | |
| `pascal_merci.ogg` | `src/main/resources/assets/royaumedesidees/sounds/` | v0.3 | Facultatif : un « merci » sec et poli (Pascal, quand on le frappe) | 1 s | Non | |

Si un fichier manque, le jeu ne plante pas : le son est juste muet.

Le portail du Royaume est silencieux : l'ancienne ligne `portail_chant.ogg` a été retirée.
