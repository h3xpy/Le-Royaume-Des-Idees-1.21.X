"""Textures de l'Ombre, sur le patron 64 x 64 du modèle humanoïde (joueur, zombie).

Deux versions : `ombre` (silhouette noire semi-transparente, telle qu'on la voit sur le mur, sans visage :
c'est une ombre portée) et `ombre_revelee` (sa vraie forme sous la Lanterne de Diogène : une statue de bois,
comme celles que les porteurs font passer derrière le muret dans l'allégorie de Platon).
"""

import random

from outils import couleur, enregistrer, nouvelle, planche
from palettes import CAVERNE, STATUE

# Pavés du patron : (u, v, largeur, hauteur, profondeur). Les membres gauches existent deux fois :
# à côté des droits (modèle humanoïde classique, en miroir) et en bas (patron du joueur).
TETE = (0, 0, 8, 8, 8)
CORPS = (16, 16, 8, 12, 4)
BRAS = [(40, 16, 4, 12, 4), (32, 48, 4, 12, 4)]
JAMBES = [(0, 16, 4, 12, 4), (16, 48, 4, 12, 4)]


def faces(u, v, l, h, p):
    """Rectangles (nom, x, y, largeur, hauteur) des six faces d'un pavé sur le patron."""
    return [
        ("dessus", u + p, v, l, p),
        ("dessous", u + p + l, v, l, p),
        ("droite", u, v + p, p, h),
        ("devant", u + p, v + p, l, h),
        ("gauche", u + p + l, v + p, p, h),
        ("dos", u + 2 * p + l, v + p, l, h),
    ]


def peindre(image, pave, teinte):
    """Remplit chaque pixel des faces du pavé avec teinte(face, x local, y local)."""
    for face, x0, y0, largeur, hauteur in faces(*pave):
        for y in range(hauteur):
            for x in range(largeur):
                image.putpixel((x0 + x, y0 + y), teinte(face, x, y))


def ombre():
    alea = random.Random(1619)
    niveaux = [couleur(CAVERNE[n], 210) for n in ("noir", "noir", "fonce", "moyen")]
    image = nouvelle(64, 64)

    def teinte(face, x, y):
        return alea.choice(niveaux)

    for pave in [TETE, CORPS] + BRAS + JAMBES:
        peindre(image, pave, teinte)
    return image


def ombre_revelee():
    s = {nom: couleur(valeur) for nom, valeur in STATUE.items()}
    alea = random.Random(387)
    image = nouvelle(64, 64)

    def bois(x, y):
        """Veinage vertical du bois, avec quelques nœuds sombres."""
        tirage = alea.random()
        if tirage < 0.04:
            return s["fonce"]
        return s["clair"] if (x * 7 + y // 3) % 5 == 0 else s["moyen"]

    def tete(face, x, y):
        if face == "dessus":
            return s["rainure"] if x % 2 == 0 else s["fonce"]          # cheveux sculptés en sillons
        if face == "dessous":
            return s["fonce"]
        if face in ("dos", "droite", "gauche"):
            return (s["rainure"] if x % 2 == 0 else s["fonce"]) if y < 3 else bois(x, y)
        # Visage sculpté : frange, yeux creusés, nez en relief, bouche fendue.
        if y < 2:
            return s["rainure"] if x % 2 == 0 else s["fonce"]
        if y == 4 and x in (2, 5):
            return s["yeux"]
        if 3 <= x <= 4 and 4 <= y <= 5:
            return s["clair"]
        if y == 6 and 2 <= x <= 5:
            return s["rainure"]
        return bois(x, y)

    def corps(face, x, y):
        if face in ("dessus", "dessous"):
            return s["fonce"]
        # Plis de toge sculptés en diagonale.
        if (x + y) % 4 == 0:
            return s["rainure"]
        return bois(x, y)

    def bras(face, x, y):
        if face == "dessus":
            return s["clair"]
        if face == "dessous":
            return s["fonce"]
        if y == 6:
            return s["rainure"]                                     # articulation du coude
        return bois(x, y)

    def jambe(face, x, y):
        if face == "dessous" or y == 11:
            return s["fonce"]
        if y == 6:
            return s["rainure"]                                     # articulation du genou
        return bois(x, y)

    peindre(image, TETE, tete)
    peindre(image, CORPS, corps)
    for pave in BRAS:
        peindre(image, pave, bras)
    for pave in JAMBES:
        peindre(image, pave, jambe)
    return image


def generer():
    print("Entités de la Caverne :")
    textures = {"ombre": ombre(), "ombre_revelee": ombre_revelee()}
    for nom, image in textures.items():
        enregistrer(image, "entity", nom)
    return [planche(list(textures.values()), "entites_caverne", echelle=6)]


if __name__ == "__main__":
    generer()
