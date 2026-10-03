"""Régénère toutes les textures du mod.

Usage, depuis la racine du projet :  python tools/textures/generer_tout.py

Les PNG sont écrits dans src/main/resources/assets/royaumedesidees/textures/,
et des aperçus agrandis dans build/apercus_textures/.
"""

import sys
from pathlib import Path

# Permet de lancer le script depuis n'importe quel dossier.
sys.path.insert(0, str(Path(__file__).resolve().parent))

import blocs_caverne  # noqa: E402
import entites_caverne  # noqa: E402
import items_v01  # noqa: E402
import grace_v03  # noqa: E402
import jardin_v02  # noqa: E402
import pnj_v03  # noqa: E402

GROUPES = [blocs_caverne, items_v01, entites_caverne, jardin_v02, grace_v03, pnj_v03]


def main():
    sys.stdout.reconfigure(encoding="utf-8")
    apercus = []
    for groupe in GROUPES:
        apercus.extend(groupe.generer())
    print("Aperçus :")
    for chemin in apercus:
        print(f"  {chemin}")


if __name__ == "__main__":
    main()
