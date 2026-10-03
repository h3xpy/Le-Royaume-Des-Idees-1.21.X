# Le Royaume des Idées — Document de conception

Mod NeoForge 1.21.1 · 1er octobre 2026

## Vision

Le Royaume des Idées est une dimension d'aventure où Pascal et saint Augustin servent de guides, de boss et de running gags, avec des mécaniques tirées de vrais faits historiques.

**Pitch :** tu entres par un portail-bibliothèque, tu sors de la caverne de Platon, tu explores quatre biomes inspirés de la vie des deux penseurs, tu récupères quatre Sceaux, puis tu affrontes la Cité terrestre pour atteindre la Cité de Dieu.

**Ton :** absurde mais documenté. Chaque blague repose sur un fait réel. Le joueur rit, puis découvre que c'est vrai.

**Piliers de design**

1. **Chaque fait = une mécanique.** Pas de texte décoratif : si Pascal a inventé le bus, il y a un bus.
2. **Deux voies qui se complètent.** La Raison (Pascal : calcul, physique, pari) et le Cœur (Augustin : grâce, confession, conversion). Le boss final exige les deux.
3. **Conçu pour le multijoueur.** Les punitions sont publiques (messages broadcast), les boss demandent de la coopération, les potes se dénoncent entre eux.
4. **Du chaos, mais lisible.** Les effets bizarres durent peu et s'expliquent toujours par un message ou une citation.

**Public :** un serveur entre amis, NeoForge 1.21.1, sessions de 1 à 3 heures, 2 à 6 joueurs.

## Accès et progression

La progression tient en trois actes : sortir de la Caverne, réunir quatre Sceaux dans quatre biomes, puis vaincre la Cité terrestre.

**Le portail.** Un cadre de bibliothèques (4 x 5) avec des lanternes aux coins. On l'allume en lisant le livre *Tolle, Lege*, trouvé dans les coffres de village. À l'allumage, une voix d'enfant chante en boucle, comme dans le jardin de Milan en 386.

**Acte I — La Caverne (tutoriel, 10 à 15 min)**

- Le joueur arrive enchaîné face à un mur où défilent des ombres de mobs.
- Il brise ses chaînes avec n'importe quel outil, puis remonte vers la lumière.
- À la sortie : Aveuglement 5 secondes et succès « Allégorie vécue ».
- Récompense : la Lanterne de Diogène, premier outil de la dimension.

**Acte II — Les quatre Sceaux (cœur du jeu, dans l'ordre que l'on veut)**

| Biome | Sceau | Comment l'obtenir | Débloque |
| --- | --- | --- | --- |
| Jardin de Milan | Sceau de la Conversion | Quête « Tolle, Lege » d'Augustin | La Voie du Cœur |
| Port-Royal | Sceau du Mémorial | Retrouver le Mémorial cousu dans un pourpoint | La Voie de la Raison |
| Puy de Dôme | Sceau du Vide | Réussir l'expérience du baromètre au sommet | Le Carrosse à 5 sols vers tous les biomes |
| Hippone | Sceau de la Cité | Tenir le siège des Vandales | L'Autel de la Cité de Dieu |

**Acte III — La Cité de Dieu.** Les quatre Sceaux posés sur l'Autel ouvrent l'arène du boss final. Il faut au moins un joueur de chaque Voie.

**Après le boss :** la dimension reste ouverte. Pages des Pensées à compléter, défis quotidiens de la Pascaline, Pari à chaque mort.

## Les biomes

Cinq biomes principaux et deux structures secrètes. Chacun a une ressource exclusive dont un autre biome a besoin, ce qui force les allers-retours.

### La Caverne de Platon (zone d'arrivée)

- **Ambiance :** grotte immense, feu au fond, murs couverts d'ombres animées.
- **Mécanique :** dans la Caverne, les mobs n'apparaissent que comme des ombres plates sur les murs. Les frapper ne fait rien. La Lanterne de Diogène révèle leur vraie forme.
- **Ressource :** Pierre d'Ombre, pour le portail de retour et des blocs décoratifs.

### Le Jardin de Milan (territoire d'Augustin)

- **Fait réel :** Augustin se convertit en 386 dans un jardin à Milan, après avoir entendu un enfant chanter « prends, lis ».
- **Ambiance :** vergers de poiriers, villas romaines, figuier au centre (Augustin pleurait sous un figuier).
- **Mécaniques :** les poiriers appartiennent à des PNJ. Voler une poire donne de la Culpabilité (voir Systèmes). La Bibliothèque d'Ambroise impose une zone de silence.
- **Ressource :** Poires, Bois de figuier.

### Port-Royal (territoire de Pascal)

- **Fait réel :** l'abbaye janséniste où Pascal faisait retraite, en guerre contre les Jésuites.
- **Ambiance :** abbaye grise, palette désaturée, cloches, aucune fleur.
- **Mécaniques :** le rire est interdit. Écrire « mdr », « lol » ou « xd » dans le chat retire un demi-cœur et sonne une cloche. Des Jésuites casuistes attaquent la nuit.
- **Ressource :** Encre janséniste, Parchemin.

### Le Puy de Dôme (montagne de la physique)

- **Fait réel :** le 19 septembre 1648, Florin Périer, beau-frère de Pascal, monte au Puy de Dôme avec un tube de mercure. Le niveau baisse en altitude : l'air a un poids.
- **Ambiance :** volcan endormi très haut (jusqu'à y=300 si la dimension le permet), vent constant.
- **Mécaniques :** plus on monte, plus la pression baisse : saut plus haut, chute plus lente, mais faim plus rapide. Au sommet, l'expérience du baromètre (un mini-puzzle).
- **Ressource :** Mercure, Verre de Clermont.

### Hippone (ville assiégée)

- **Fait réel :** Augustin meurt en 430 dans Hippone, assiégée par les Vandales.
- **Ambiance :** ville romaine côtière, remparts, port, basilique.
- **Mécanique :** un siège en vagues se déclenche quand un joueur entre dans la basilique. Il faut tenir les remparts.
- **Ressource :** Bronze romain, Huile d'olive.

### Structures secrètes

- **Le Poêle de Descartes :** petite pièce chauffée cachée sous la neige, en souvenir de la nuit de 1619 où Descartes réfléchit enfermé dans un poêle en Allemagne.
- **Le Tonneau de Diogène :** un tonneau seul dans une plaine, avec le PNJ Diogène dedans.

## Personnages et mobs

Les PNJ donnent les quêtes et les blagues ; les mobs hostiles incarnent les adversaires historiques des deux penseurs.

### PNJ principaux

| PNJ | Lieu | Rôle en jeu | Clin d'œil réel |
| --- | --- | --- | --- |
| Blaise Pascal | Port-Royal | Donne la Voie de la Raison, propose le Pari, vend la Pascaline | Porte une ceinture à pointes sous ses habits (rapporté par sa sœur Gilberte). Si on le frappe, il dit « merci » et gagne de la Grâce. |
| Augustin jeune | Jardin de Milan | Fêtard, vole des poires avec le joueur, donne la quête de conversion | Ses *Confessions* racontent ses années de jeunesse agitée. |
| Augustin vieux | Hippone | Évêque, donne la Voie du Cœur, lance le siège | Évêque d'Hippone pendant 35 ans environ. |
| Sainte Monique | Partout (elle te suit) | Compagnon invincible qui pleure tant que tu n'as pas la Voie du Cœur | Elle a prié des années pour la conversion de son fils. Enfant, une servante l'a traitée de petite ivrogne parce qu'elle buvait du vin en cachette : elle commente si tu bois de la Bière d'Augustin. |
| Saint Ambroise | Bibliothèque de Milan | Lit en silence, crée une zone où le chat est muet | Augustin s'étonnait de le voir lire sans bouger les lèvres. |
| Adéodat | Jardin de Milan | Mini-PNJ génie, défis de calcul mental | Fils d'Augustin, réputé très brillant. |
| Florin Périer | Pied du Puy de Dôme | Guide l'expérience du baromètre | Beau-frère de Pascal, il a fait l'ascension de 1648. |
| Descartes | Le Poêle | Donne la potion de Doute | « Je pense donc je suis ». |
| Diogène | Le Tonneau | Échange la Lanterne contre de l'ombre | Il cherchait « un homme » avec une lanterne en plein jour. Si tu lui caches le soleil, il te repousse. |

### Mobs hostiles

- **Ombres (Caverne) :** invulnérables tant qu'on ne les éclaire pas à la Lanterne de Diogène.
- **Manichéens :** mobs moitié blancs, moitié noirs. Le jour, seul leur côté noir prend des dégâts, la nuit l'inverse. Augustin a été manichéen pendant environ 9 ans.
- **Jésuites casuistes (Port-Royal, la nuit) :** une fois sur trois, ils « argumentent » et annulent un coup. La *Lettre Provinciale* lancée sur eux les fait fuir, comme les *Provinciales* de Pascal (1656-1657).
- **Vandales (Hippone) :** vagues du siège, avec des béliers qui cassent les remparts.
- **Le Divertissement :** mob farceur qui apparaît derrière un joueur inactif depuis 2 heures. Il imite un coffre plein de diamants. Pascal pensait que l'homme se divertit pour ne pas penser à sa condition.

### Mobs passifs

- **Roseau pensant :** canne à sucre vivante qui récite une pensée quand on la touche. Fragile, elle se brise si on court à côté.

## Systèmes centraux

Quatre systèmes relient tout le reste : la Grâce (ressource positive), la Culpabilité (malus), les Pensées (collection) et les deux Voies (classes).

### La Grâce

- Jauge de 0 à 100, affichée à côté de la barre d'XP.
- **Gagner :** se confesser, aider un autre joueur à se relever, nourrir Monique, défendre Hippone, se faire frapper sans riposter (style Pascal).
- **Dépenser :** recharger le Totem de la Grâce, ouvrir l'Autel final, acheter des faveurs aux PNJ.
- **Blague récurrente :** la Grâce ne se mérite jamais vraiment. Chaque gain a 10 % de chance d'afficher « La grâce est un don, pas un salaire » et de doubler, ou de ne rien donner.

### La Culpabilité

- Effet de statut qui se cumule (niveaux I à V).
- **Causes :** voler une poire, tuer un PNJ, rire à Port-Royal, mentir au Pari.
- **Effets par niveau :** I lenteur légère · II nuage de pluie personnel au-dessus de la tête · III Monique pleure plus fort · IV musique dramatique en boucle · V message à tout le serveur : « [pseudo] croule sous la culpabilité ».
- **Retirer :** le Confessionnal. Le joueur tape `/confesse <son péché>` et le texte s'affiche à tout le serveur. Chaque confession retire un niveau et donne 5 de Grâce.

### Les Pensées

- 27 pages éparpillées dans la dimension, en clin d'œil aux 27 liasses titrées retrouvées après la mort de Pascal, qui n'avait pas rangé ses notes.
- Chaque page débloque un petit bonus passif et une citation. Exemple : *Le roseau pensant* donne Résistance aux dégâts de chute ; *Le nez de Cléopâtre* débloque l'item du même nom.
- Les 27 pages réunies font le livre complet : il permet de recommencer le Pari autant de fois qu'on veut.

### Les deux Voies (classes)

Chaque joueur choisit une Voie, et peut en changer contre beaucoup de Grâce.

| | Voie de la Raison (Pascal) | Voie du Cœur (Augustin) |
| --- | --- | --- |
| Obtenue à | Port-Royal | Jardin de Milan |
| Force | Calcul, physique, pièges, outils | Soin, protection, conversion des mobs |
| Outil signature | Pascaline (calcule les faiblesses des mobs) | Livre des Confessions (soigne la zone) |
| Faiblesse | Gagne peu de Grâce | Lent à fabriquer des outils |
| Rôle au boss | Détruit les murs de la Cité terrestre | Résiste au Divertissement |

### Le Pari (à chaque mort, une fois la Raison débloquée)

À la mort dans la dimension, un écran demande « Dieu existe ? Oui / Non ». Parier « Oui » garde l'inventaire avec 70 % de chance. Parier « Non » donne 30 % de chance de tout garder… sinon on réapparaît dans un pot de fleurs. Le calcul est faux exprès : Pascal disait qu'on a tout à gagner et rien à perdre.

## Items, blocs et recettes

Chaque recette clé mélange au moins deux biomes, pour que les joueurs aient besoin de tout explorer et de s'échanger des ressources.

### Outils et items

| Item | Recette (ingrédients) | Effet | Fait réel |
| --- | --- | --- | --- |
| Lanterne de Diogène | Récompense de la Caverne | Révèle les Ombres ; dans la dimension, montre les joueurs proches à travers les murs | Diogène cherchait « un homme » à la lanterne. |
| Pascaline | Bronze romain + Mercure + Encre janséniste | Affiche les PV et la faiblesse du mob visé ; additionne les items d'un coffre | Machine à calculer de Pascal, 1642, faite pour aider son père, collecteur d'impôts. |
| Baromètre de Périer | Verre de Clermont + Mercure | Affiche l'altitude ; vibre avant un orage | Expérience du Puy de Dôme, 1648. |
| Livre *Tolle, Lege* | Coffres de village (Overworld) | Allume le portail ; relu dans la dimension, téléporte au Jardin de Milan | Conversion d'Augustin, 386. |
| Lettre Provinciale | Parchemin + Encre janséniste | Projectile ; fait fuir les Jésuites | *Les Provinciales*, 1656-1657. |
| Le Mémorial | Quête de Port-Royal | Clé du Sceau ; porté au torse, immunité au feu 30 s une fois par jour | Note de la « nuit de feu » du 23 novembre 1654, retrouvée cousue dans le vêtement de Pascal après sa mort. |
| Nez de Cléopâtre | Page des Pensées + Or | Change une chose au hasard dans un rayon de 10 blocs (couleur des moutons, météo, un bloc) | « Le nez de Cléopâtre, s'il eût été plus court… » |
| Totem de la Grâce | Sceau de la Cité + 50 de Grâce | Totem d'immortalité rechargeable, mais 50 % d'échec ; dimension seulement | La grâce ne se mérite pas. |

### Nourriture et potions

| Consommable | Recette | Effet |
| --- | --- | --- |
| Poire (volée) | Poirier d'un PNJ | Nourrit bien, mais +1 Culpabilité |
| Poire (achetée) | Échange avec un PNJ | Nourrit, sans culpabilité, mais moins bonne (blague : le vol est plus savoureux, comme le dit Augustin) |
| Bière d'Augustin | Blé + Poire + Bois de figuier dans un tonneau | Force I et Nausée ; Monique commente. Augustin est le saint patron des brasseurs. |
| Potion « Le cœur a ses raisons » | Poire + Rose + fiole | Le premier mob regardé te suit avec des cœurs pendant 3 min |
| Potion « Le moi est haïssable » | Pierre d'Ombre + fiole | Invisibilité, et ton pseudo disparaît de la liste TAB |
| Potion de Doute | Échange avec Descartes | Tous les blocs autour deviennent fantômes ; écrire « je pense donc je suis » dans le chat annule l'effet |
| « Chasteté, mais pas tout de suite » | Récompense d'Augustin | Gros buff (Force, Vitesse, Régénération) de 60 s qui ne s'active qu'au bout de 10 minutes ; dimension seulement |

### Blocs

- **Confessionnal :** retire la Culpabilité (voir Systèmes).
- **Autel de la Cité de Dieu :** reçoit les quatre Sceaux, ouvre l'arène.
- **Arrêt de Carrosse :** voir Transport.
- **Tonneau de brasserie :** fabrique la Bière d'Augustin en 5 minutes.
- **Pupitre d'Ambroise :** crée une zone de silence de 8 blocs (chat bloqué, mobs sourds au joueur).

### Transport : le Carrosse à 5 sols

En 1662, Pascal lance à Paris les carrosses à cinq sols, souvent cités comme les premiers transports en commun. Dans la dimension, un carrosse relie les arrêts des cinq biomes pour 5 Sols (monnaie gagnée en vendant aux PNJ). Il part toutes les 5 minutes, avec 30 % de chance de retard, et un PNJ contrôleur expulse ceux qui n'ont pas payé.

## Boss final : la Cité de Dieu

Un combat en trois phases, pensé pour 2 à 6 joueurs, qui exige au moins un joueur de chaque Voie.

**Contexte réel :** Rome est pillée par les Wisigoths d'Alaric en 410. Augustin répond en écrivant *La Cité de Dieu*, qui oppose la cité des hommes et celle de Dieu.

### Phase 1 — La Cité terrestre

- L'arène est une Rome miniature qui s'effondre petit à petit.
- Le boss est Alaric, géant entouré de murailles. La Voie de la Raison doit calculer à la Pascaline quel pan de mur est fragile, puis le faire tomber.
- Les colonnes qui tombent écrasent les joueurs immobiles.

### Phase 2 — Le Divertissement

- Le boss devient invisible et fait apparaître des distractions : faux coffres de diamants, faux messages « [pote] a rejoint le serveur », faux succès, musique de disque aléatoire.
- Un joueur qui interagit avec une distraction est figé 5 secondes.
- La Voie du Cœur est immunisée et doit guider les autres.

### Phase 3 — Le Théologien

- Pascal et Augustin fusionnent en un boss à deux têtes qui se disputent entre elles.
- Attaques : citations-projectiles, cercle de Culpabilité, Pari forcé (un joueur au hasard parie, et perd la moitié de ses PV s'il a tort).
- Quand les deux têtes sont d'accord, elles font une attaque combinée : le signal pour se mettre à l'abri.

### Récompenses

- **Totem de la Grâce** pour chaque participant.
- **Titre** « Docteur de l'Église » affiché dans le chat.
- **Disque « Grégorien Drill »**.
- Accès à **la Cité de Dieu**, une ville céleste de construction libre, protégée, où il ne pleut jamais : la base de fin de jeu du serveur.

## Carte des synergies

Aucun biome ne se suffit à lui-même : les ressources circulent, les objets fabriqués mènent aux deux Voies, et seules les deux Voies réunies ouvrent le boss.

```
 Caverne       Jardin de Milan   Port-Royal        Puy de Dôme     Hippone
 (Pierre       (Poires,          (Encre,           (Mercure,       (Bronze,
  d'Ombre)      figuier)          parchemin)        verre)          huile)
      \______________|________________|________________|______________/
                          ressources échangées
                                   |
   Pascaline     Lettre Provinciale     Bière d'Augustin     Confessionnal
        \______________/                       \______________/
        Voie de la Raison                       Voie du Cœur
                 \_______________________________/
                Autel de la Cité de Dieu : 4 Sceaux + 2 Voies
```

### Boucles de jeu entre ajouts

| Si tu fais… | Alors… | Ajouts reliés |
| --- | --- | --- |
| Voler une poire | Culpabilité, Monique pleure, confession publique, puis Grâce gagnée | Poiriers, Culpabilité, Monique, Confessionnal, Grâce |
| Voler dans la zone de silence d'Ambroise | Les PNJ ne t'entendent pas, mais la Culpabilité tombe quand même | Pupitre d'Ambroise, poiriers |
| Brasser la Bière d'Augustin | Force I utile pour tenir le siège d'Hippone, mais Monique te sermonne | Jardin, Hippone, Monique |
| Fabriquer la Pascaline | Il faut trois biomes ; elle révèle les faiblesses des Manichéens et les murs fragiles d'Alaric | Hippone, Puy de Dôme, Port-Royal, boss |
| Garder la Lanterne de Diogène | Elle révèle les Ombres et l'emplacement du boss invisible en phase 2 | Caverne, boss |
| Boire la potion de Doute | Rend un joueur de la Raison insensible aux fausses distractions du Divertissement | Descartes, boss |
| Boire « Le cœur a ses raisons » près d'un Vandale | Le Vandale te suit et se bat pour toi pendant le siège | Potions, Hippone |
| Mourir | Le Pari ; avec les 27 Pensées, il devient rejouable | Pari, Pensées |
| Obtenir le Sceau du Vide | Le Carrosse à 5 sols relie tous les biomes, payé en Sols gagnés auprès des PNJ | Puy de Dôme, Carrosse, économie |

## Gameplay imaginé : une session type

Voici à quoi ressemble une soirée de 2 heures à trois joueurs (Toi, Léo et Sam), du portail jusqu'au premier Sceau.

1. **0:00 — Le portail.** Sam trouve *Tolle, Lege* dans un village. Les trois allument le portail ; la voix d'enfant chante, Léo coupe le son, le jeu le remet.
2. **0:05 — La Caverne.** Chacun arrive enchaîné. Léo tape une Ombre pendant deux minutes sans résultat. Toi, tu sors en premier, tu es aveuglé, tu tombes dans un trou. Succès « Allégorie vécue ».
3. **0:20 — Le Jardin de Milan.** Sam vole une poire « pour voir ». Message serveur : « Sam a volé une poire. Honte. » Augustin jeune court vers lui… et lui propose d'en voler d'autres. Culpabilité II, un nuage de pluie suit Sam.
4. **0:35 — Monique.** Sainte Monique se met à suivre Sam et pleure. Impossible de s'en débarrasser. Sam fait `/confesse j'ai volé 14 poires` ; tout le serveur le voit.
5. **0:50 — Le choix des Voies.** Toi, tu prends le Cœur avec Augustin. Léo, tu l'envoies à Port-Royal pour la Raison. Il écrit « mdr » dans le chat et perd un demi-cœur. Les cloches sonnent.
6. **1:10 — La nuit à Port-Royal.** Les Jésuites attaquent. Ils annulent la moitié des coups de Léo. Sam fabrique des Lettres Provinciales et les fait fuir.
7. **1:30 — Le Mémorial.** Indice de Pascal : « cousu près du cœur ». Il faut fouiller les pourpoints de PNJ endormis. Léo meurt pendant la recherche : premier Pari. Il dit « Non », perd, réapparaît dans un pot de fleurs.
8. **1:50 — Premier Sceau.** Le Sceau du Mémorial est récupéré. Pascal donne la Pascaline à Léo, qui découvre que le mob à côté de lui a 2 PV et 0 faiblesse.
9. **2:00 — Fin de soirée.** Tout le monde se retrouve à l'arrêt du Carrosse. Il a 4 minutes de retard. Session terminée.

**Ce que la session doit produire :** au moins un message serveur humiliant par joueur, une coopération forcée (Lettres Provinciales), et l'envie de revenir pour le Sceau suivant.

## Faits réels utilisés

Liste de référence des faits historiques derrière chaque mécanique. Elle vient de connaissances générales, non sourcées ici : à vérifier avant de les afficher en jeu.

| Date | Fait | Utilisé pour |
| --- | --- | --- |
| IVe siècle av. J.-C. | Diogène, son tonneau et sa lanterne | Lanterne, PNJ |
| vers 370 | Augustin vole des poires à 16 ans, par plaisir du mal (*Confessions*, livre II) | Poires, Culpabilité |
| IVe siècle | Augustin manichéen pendant environ 9 ans | Mobs Manichéens |
| IVe siècle | Ambroise lit en silence, ce qui surprend Augustin | Pupitre d'Ambroise |
| 386 | Conversion d'Augustin dans un jardin à Milan, « prends, lis » | Portail, Jardin de Milan |
| 387 | Augustin est baptisé par Ambroise à Milan ; Monique meurt à Ostie | Ambroise, Monique |
| 410 | Sac de Rome par Alaric ; Augustin écrit ensuite *La Cité de Dieu* | Boss final, phase 1 |
| 430 | Augustin meurt à Hippone pendant le siège des Vandales | Biome Hippone, siège |
| — | Augustin, saint patron des brasseurs | Bière d'Augustin |
| — | Prière « donne-moi la chasteté, mais pas tout de suite » (*Confessions*) | Buff à retardement |
| 1619 | Descartes réfléchit enfermé dans un « poêle » en Allemagne | Structure secrète |
| 1642 | Pascal invente la Pascaline pour son père | Pascaline |
| 1648 | Expérience du Puy de Dôme par Florin Périer | Biome, baromètre |
| 1654 | Nuit de feu du 23 novembre, le Mémorial cousu dans son vêtement | Item Mémorial, Sceau |
| 1656-1657 | *Les Provinciales* contre les Jésuites | Lettres Provinciales, Jésuites |
| 1662 | Lancement des carrosses à cinq sols à Paris ; mort de Pascal à 39 ans | Carrosse, transport |
| 1670 | Publication posthume des *Pensées*, à partir de notes non rangées | 27 pages des Pensées |
| — | Pascal porte une ceinture à pointes (selon sa sœur Gilberte) | Réaction quand on le frappe |

## Production : qui fait quoi

Claude Code produit tout le contenu, y compris les images, les mobs et les constructions, par du code et des scripts. L'utilisateur fournit seulement les sons et teste en jeu.

| Élément | Comment Claude Code le fait | Ce que fait l'utilisateur |
| --- | --- | --- |
| Code (items, blocs, PNJ, IA, systèmes, boss) | Java NeoForge 1.21.1, directement | Lancer le jeu, tester, décrire les bugs |
| Textures d'items et de blocs (16 x 16) | Un script Python (Pillow) dessine le pixel art pixel par pixel avec une palette par biome. Claude Code ouvre les PNG pour vérifier le rendu. | Dire « trop sombre », « pas lisible », etc. |
| Mobs et PNJ | Pas de modèles 3D sur mesure : réutiliser les modèles du jeu (villageois, illageois, zombie, joueur) et générer seulement leur texture. Les géants (Alaric, le Théologien) = un modèle existant agrandi avec l'attribut de taille du jeu. | Rien |
| Skins de Pascal, Augustin, Monique… | Texture de modèle joueur ou villageois générée par script | Optionnel : proposer un skin libre de droits |
| Dimension et biomes | Générateur Java + fichiers JSON écrits par Claude Code | Rien |
| Structures (abbaye, villas, remparts, arène, Cité de Dieu) | Générées par code (Java qui place les blocs, ou .nbt écrits par script). Aucune construction à la main. | Visiter et dire ce qui ne va pas |
| Textes, citations, messages | Fichiers de langue fr_fr.json et en_us.json | Relire |
| Musiques et sons | Claude Code écrit sounds.json, le code et la liste des fichiers attendus | Trouver des fichiers libres de droits (CC0 ou domaine public), les convertir en .ogg, les déposer dans le dossier indiqué |

Claude Code ne peut pas jouer au jeu : chaque version se termine par une checklist de tests à faire en jeu.

La musique d'un disque reste soumise au droit d'auteur, même trouvée gratuitement : n'utiliser que des fichiers dont la licence autorise la réutilisation.

## Équilibrage : rien de trop fort hors de la dimension

Tout objet puissant est « lié au Royaume » : hors de la dimension, il devient un *Souvenir du Royaume* inerte (description grisée, aucun effet), et se réactive au retour.

| Objet ou effet | Puissance | Règle |
| --- | --- | --- |
| Totem de la Grâce | Très forte | Fonctionne dans la dimension seulement |
| Pari à la mort (garder son inventaire) | Très forte | Morts dans la dimension seulement |
| Lanterne de Diogène (voir les joueurs à travers les murs) | Forte en PvP | Dimension seulement |
| Potion « Le moi est haïssable » (invisible + caché du TAB) | Forte en PvP | Dimension seulement |
| Buff « Chasteté, mais pas tout de suite » | Forte | Dimension seulement, dure 60 s |
| Nez de Cléopâtre, potion de Doute | Peut abîmer des constructions | Dimension seulement |
| Bière d'Augustin | Moyenne | Partout, Force I et Nausée |
| Pascaline, Baromètre, Lettre Provinciale, disques, blocs décoratifs | Faible ou cosmétique | Partout |

**Autres règles :** les outils et armures de la dimension ne dépassent jamais le niveau du fer. Aucune ressource du Royaume ne remplace les diamants ou la netherite. La Cité de Dieu reste une ville de construction, sans coffre de butin.

## Mettre à jour le serveur sans casser le monde

La carte du Royaume est figée dès la v0.1 ; les versions suivantes n'ajoutent que du contenu qui ne dépend pas de la génération du monde.

**Le problème :** Minecraft ne régénère jamais un chunk déjà exploré. Si on change le relief ou les biomes après coup, les zones visitées gardent l'ancienne version, avec des coupures nettes aux bords des chunks. Une structure ajoutée plus tard n'apparaît que dans les zones encore jamais visitées.

**Les cinq règles :**

1. **Une carte fixe et finie.** Le Royaume est une grande île d'environ 2 000 x 2 000 blocs entourée de vide. Chaque biome occupe une zone définie par ses coordonnées, pas par le hasard. La carte est donc identique à chaque génération.
2. **Relief et biomes terminés en v0.1.** Tout le terrain des cinq biomes est livré dès la première version, même si les biomes sont encore vides de PNJ.
3. **Structures posées par le mod, pas par la génération.** Chaque structure a des coordonnées fixes et un numéro de version. Au démarrage du serveur, le mod vérifie si elle est construite et à jour, et la pose ou la remplace si besoin : tout est en place avant l'arrivée des joueurs.
4. **Ne jamais renommer ni supprimer un identifiant.** Un bloc ou un item retiré du mod disparaît des coffres et du monde. Les objets abandonnés restent enregistrés, juste cachés du menu créatif.
5. **Plan B : la remise à zéro.** La dimension vit dans son propre dossier de sauvegarde. On peut la supprimer serveur éteint sans toucher à l'Overworld ni au Nether. À réserver aux cas graves, en prévenant les joueurs.

**Routine de mise à jour :** sauvegarder le monde, tester la nouvelle version sur une copie en local, puis remplacer le fichier .jar sur le serveur.

## Priorités et découpage en versions

Voir `docs/roadmap.md` pour le détail des six versions (v0.1 Les fondations → v1.0 La Cité de Dieu). Chaque version doit être jouable et drôle toute seule.
