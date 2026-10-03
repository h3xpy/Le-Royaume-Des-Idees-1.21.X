# Le Royaume des Idées — instructions pour Claude Code

Mod Minecraft **NeoForge 1.21.1** (Java 21) qui ajoute une dimension humoristique autour de Blaise Pascal et de saint Augustin, avec des mécaniques tirées de vrais faits historiques.

L'utilisateur débute : il sait lancer le jeu et tester, mais il ne code pas, ne dessine pas de textures et ne construit pas. **Tout ce qui est conséquent est fait par toi**, par du code et des scripts. Il ne fournit que les fichiers audio (.ogg) et ses retours de test. Explique-lui les choses simplement, en français.

## Documents à lire

| Fichier | Rôle |
| --- | --- |
| `docs/conception.md` | Le document de conception complet (univers, biomes, PNJ, systèmes, items, boss, équilibrage). C'est la source de vérité sur le *quoi*. |
| `docs/roadmap.md` | Les versions, dans l'ordre, avec leurs critères de fin. |
| `docs/specs/` | Une spécification détaillée par version. Seule `v0.1-fondations.md` existe au départ. |
| `docs/progression.md` | Journal que tu tiens à jour : fait, en cours, décisions, problèmes. |
| `docs/sons_a_fournir.md` | Liste des sons que l'utilisateur doit trouver et déposer. |

## Façon de travailler

1. **Une version à la fois**, dans l'ordre de `docs/roadmap.md`. Ne commence jamais du contenu d'une version suivante.
2. **Avant chaque version** : si la spec n'existe pas, rédige `docs/specs/vX.Y-nom.md` à partir de `docs/conception.md`, sur le modèle de la v0.1, et fais-la valider par l'utilisateur avant de coder.
3. **Planifie avant de coder** : propose un plan découpé en petites étapes, attends l'accord.
4. **Après chaque étape** : `./gradlew build` doit passer, puis un commit git avec un message clair en français.
5. **Fin de version** : écris `docs/tests/vX.Y.md`, une checklist de tests en jeu que l'utilisateur coche (quoi faire, quoi observer). Mets à jour `docs/progression.md` et `docs/sons_a_fournir.md`.
6. Quand une idée de la conception est techniquement trop lourde, propose une version plus simple qui garde la blague, et note la décision dans `docs/progression.md`.

## Règles techniques (ne pas enfreindre)

### Identité du mod
- modid : `royaumedesidees`. Package Java : `com.royaumedesidees`.
- Base : le MDK officiel NeoForge 1.21.1 avec ModDevGradle.
- Enregistrements via `DeferredRegister`. Tout ce qui peut l'être est généré par data generation (`runData`) : modèles, blockstates, langues, recettes, loot tables, tags, advancements.
- Langue principale `fr_fr.json`, avec `en_us.json` en secours.

### Identifiants : jamais renommés, jamais supprimés
Un identifiant de bloc, d'item, d'entité, de biome ou de dimension publié ne change plus jamais. Un objet abandonné reste enregistré et est seulement retiré des onglets créatifs et des recettes. Renommer un identifiant efface l'objet des mondes existants.

### Génération du monde figée dès la v0.1
Le serveur garde le même monde d'une version à l'autre. Minecraft ne régénère jamais un chunk déjà exploré. Donc :
- La dimension utilise un **générateur de chunks Java personnalisé et déterministe** : une île finie d'environ 2000 x 2000 blocs centrée en (0, 0), entourée de vide. Le relief et les biomes dépendent **uniquement des coordonnées**, jamais de la seed du monde.
- Les biomes sont placés par zones fixes (voir la spec v0.1) via une `BiomeSource` personnalisée.
- **Après la v0.1, ne modifie plus le relief ni la répartition des biomes.** Si c'est vraiment nécessaire, arrête-toi et demande l'accord explicite de l'utilisateur, en expliquant que les zones déjà explorées ne changeront pas.
- **Les structures ne passent pas par la génération du monde.** Elles sont posées par le système de pose du mod (`StructurePlacer`) : chaque structure a un identifiant, une position fixe et un numéro de version, enregistrés dans une `SavedData` de la dimension. Au démarrage du serveur (donc dès la création du monde), le mod pose chaque structure qui manque, ou la remplace si sa version a augmenté ; en filet de sécurité, il refait ce contrôle quand un joueur arrive à moins de 96 blocs. Ainsi, une structure ajoutée en v0.4 apparaît aussi dans un monde créé en v0.1.
- Les structures sont décrites en Java (constructeurs de blocs) ou en fichiers `.nbt` produits par script. Jamais de construction à la main.

### Contenu visuel sans artiste
- **Textures** : générées par des scripts Python (Pillow) dans `tools/textures/`, écrites dans `src/main/resources/assets/royaumedesidees/textures/`. Pixel art 16 x 16 pour items et blocs, une palette par biome. Ouvre les PNG produits pour vérifier le rendu. Ne copie jamais de textures de Mojang ni d'autres mods.
- **Mobs et PNJ** : pas de modèle 3D sur mesure. Réutilise les modèles du jeu (`HumanoidModel`/`PlayerModel`, `VillagerModel`, `IllagerModel`, etc.) avec un renderer et une texture générée.
- **Géants** (Alaric, le Théologien) : un modèle existant agrandi avec l'attribut `Attributes.SCALE`.
- **Sons** : écris `sounds.json` et le code. Pour chaque son, ajoute une ligne dans `docs/sons_a_fournir.md` (nom de fichier exact, dossier, durée, ambiance, boucle ou non). L'absence d'un .ogg ne doit jamais faire planter le jeu.

### Équilibrage
- Tag d'item `royaumedesidees:lie_au_royaume` : hors de la dimension, ces objets n'ont aucun effet et affichent la mention « Souvenir du Royaume » en gris. Ils redeviennent actifs au retour.
- Les effets de mort (Pari) ne s'appliquent qu'aux morts dans la dimension.
- Outils et armures du mod : jamais au-dessus du niveau du fer.

### Multijoueur
- Le mod tourne sur un serveur NeoForge ; les joueurs doivent aussi l'installer. Toute logique de jeu côté serveur, le client ne fait que l'affichage.
- Les messages broadcast (vol de poire, confession) passent par le serveur et sont traduisibles.

## Commandes utiles

- `./gradlew runClient` : lance le jeu avec le mod (pour l'utilisateur).
- `./gradlew runServer` : lance un serveur local.
- `./gradlew runData` : régénère les fichiers de données.
- `./gradlew build` : produit le .jar dans `build/libs/`.
- `python tools/textures/generer_tout.py` : régénère toutes les textures.
