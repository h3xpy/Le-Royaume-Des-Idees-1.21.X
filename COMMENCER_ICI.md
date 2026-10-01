# Commencer ici

## 1. Installer (une seule fois)

- **Java 21** (par exemple Eclipse Temurin 21)
- **IntelliJ IDEA** (la version Community gratuite suffit)
- **Python 3** (pour les scripts de textures)
- **Claude Code**

## 2. Préparer le projet

1. Télécharge le **MDK NeoForge 1.21.1 (ModDevGradle)** et décompresse-le, par exemple dans `Documents/royaume-des-idees`.
2. Copie à la racine de ce dossier (là où il y a `build.gradle`) : `CLAUDE.md`, `COMMENCER_ICI.md` et le dossier `docs`.
3. Ouvre le dossier dans IntelliJ et attends que Gradle ait fini de charger.

## 3. Premier message à envoyer à Claude Code

> Lis CLAUDE.md, docs/conception.md, docs/roadmap.md et docs/specs/v0.1-fondations.md. Ce projet est le mod d'exemple NeoForge : transforme-le en notre mod (modid royaumedesidees) puis commence la v0.1. Propose-moi d'abord un plan en étapes, sans écrire de code, et attends mon accord.

## 4. Tester

- Dans IntelliJ, lance la configuration **runClient** (ou `./gradlew runClient`, `gradlew.bat runClient` sur Windows).
- Suis la checklist `docs/tests/v0.1.md` que Claude Code écrira à la fin.
- Si quelque chose cloche, décris-le à Claude Code, avec une capture d'écran si possible.

## 5. Mettre sur le serveur

1. `./gradlew build` : le mod est dans `build/libs/` (le .jar sans « sources »).
2. **Sauvegarde le monde du serveur.**
3. Le serveur doit tourner sous **NeoForge 1.21.1**. Mets le .jar dans son dossier `mods/`.
4. **Chaque joueur doit aussi installer NeoForge 1.21.1 et le même .jar.**

## 6. Versions suivantes

Dis simplement à Claude Code : « On passe à la version suivante. » Il rédigera la spec, te la fera valider, puis codera. Pense aux sons listés dans `docs/sons_a_fournir.md`.
