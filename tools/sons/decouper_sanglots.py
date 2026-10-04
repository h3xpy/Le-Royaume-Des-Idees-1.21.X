"""Découpe le long enregistrement de sanglots en trois extraits courts, en mono, pour les pleurs de Monique.

Le fichier d'origine (52 s, stéréo) est d'abord copié dans run/sons_originaux/ (dossier ignoré par git), puis
remplacé par le premier extrait ; les deux autres deviennent sanglots_2.ogg et sanglots_3.ogg. Le jeu tire un des
trois au hasard à chaque sanglot. En mono, le son est placé dans l'espace : on entend Monique là où elle est.

Les passages ont été choisis d'après le volume, mesuré par demi-seconde. Relancer ce script sur l'original
redonne les mêmes extraits.
"""

import shutil
from pathlib import Path

import numpy as np
import soundfile as sf

RACINE = Path(__file__).resolve().parents[2]
SONS = RACINE / "src/main/resources/assets/royaumedesidees/sounds"
ORIGINAL = RACINE / "run/sons_originaux/sanglots_original.ogg"

# (début, fin) en secondes, dans l'enregistrement d'origine.
EXTRAITS = {
    "sanglots": (5.8, 10.0),
    "sanglots_2": (21.8, 27.0),
    "sanglots_3": (35.3, 39.9),
}
FONDU = 0.15


def main():
    if not ORIGINAL.exists():
        ORIGINAL.parent.mkdir(parents=True, exist_ok=True)
        shutil.copy2(SONS / "sanglots.ogg", ORIGINAL)
    donnees, frequence = sf.read(ORIGINAL)
    mono = donnees.mean(axis=1) if donnees.ndim > 1 else donnees
    for nom, (debut, fin) in EXTRAITS.items():
        extrait = mono[int(debut * frequence):int(fin * frequence)].copy()
        n = int(FONDU * frequence)
        extrait[:n] *= np.linspace(0.0, 1.0, n)
        extrait[-n:] *= np.linspace(1.0, 0.0, n)
        # Volume ramené à un pic de 0,9, pour que les trois extraits sonnent pareil.
        extrait *= 0.9 / max(1e-6, float(np.abs(extrait).max()))
        sf.write(SONS / f"{nom}.ogg", extrait, frequence, format="OGG", subtype="VORBIS")
        print(f"  {nom}.ogg : {len(extrait) / frequence:.1f} s, mono")


if __name__ == "__main__":
    main()
