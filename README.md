# Le Royaume des Idées

Mod Minecraft **NeoForge 1.21.1** (Java 21) : une dimension d'aventure humoristique où Blaise Pascal et saint Augustin servent de guides, de boss et de running gags, avec des mécaniques tirées de vrais faits historiques.

- Conception : [`docs/conception.md`](docs/conception.md)
- Versions prévues : [`docs/roadmap.md`](docs/roadmap.md)
- Avancement : [`docs/progression.md`](docs/progression.md)
- Pour démarrer : [`COMMENCER_ICI.md`](COMMENCER_ICI.md)

## Commandes

| Commande | Effet |
| --- | --- |
| `./gradlew runClient` | Lance le jeu avec le mod |
| `./gradlew runServer` | Lance un serveur local |
| `./gradlew runData` | Régénère les fichiers de données |
| `./gradlew build` | Produit le .jar dans `build/libs/` |

Sous Windows, remplace `./gradlew` par `gradlew.bat`.

## Installation sur un serveur

Le serveur et chaque joueur ont besoin de NeoForge 1.21.1 et du même .jar (celui sans « sources » dans `build/libs/`). Sauvegarde le monde avant chaque mise à jour.
