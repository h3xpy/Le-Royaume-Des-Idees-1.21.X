"""Textures de l'Ombre, sur le patron 64 x 64 du modèle humanoïde (joueur, zombie).

Deux versions : `ombre` (silhouette noire semi-transparente, telle qu'on la voit sur le mur)
et `ombre_revelee` (sa vraie forme sous la Lanterne de Diogène : un prisonnier de la Caverne).
"""

import random

from outils import couleur, enregistrer, nouvelle, planche
from palettes import CAVERNE, PRISONNIER

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
    yeux = couleur(CAVERNE["reflet"], 230)
    image = nouvelle(64, 64)

    def teinte(face, x, y):
        return alea.choice(niveaux)

    def teinte_tete(face, x, y):
        # Deux reflets à peine visibles à la place des yeux.
        if face == "devant" and y == 4 and x in (2, 5):
            return yeux
        return alea.choice(niveaux)

    peindre(image, TETE, teinte_tete)
    peindre(image, CORPS, teinte)
    for pave in BRAS + JAMBES:
        peindre(image, pave, teinte)
    return image


def ombre_revelee():
    p = {nom: couleur(valeur) for nom, valeur in PRISONNIER.items()}
    alea = random.Random(387)
    image = nouvelle(64, 64)

    def tete(face, x, y):
        if face == "dessus":
            return p["cheveux"]
        if face == "dessous":
            return p["peau_ombre"]
        if face == "dos":
            return p["cheveux"] if y < 6 else p["peau_ombre"]
        if face in ("droite", "gauche"):
            return p["cheveux"] if y < 3 or (y < 5 and x in ((0, 1) if face == "gauche" else (6, 7))) else p["peau"]
        # Visage : frange, sourcils, yeux plissés (la lumière fait mal), barbe naissante.
        if y < 2:
            return p["cheveux"]
        if y == 3 and x in (1, 2, 5, 6):
            return p["cheveux"]
        if y == 4 and x in (2, 5):
            return p["yeux"]
        if y == 6 and 3 <= x <= 4:
            return p["peau_ombre"]
        if y == 7:
            return p["peau_ombre"] if x in (0, 7) else p["cheveux"]
        return p["peau"]

    def corps(face, x, y):
        if face in ("dessus", "dessous"):
            return p["tunique_fonce"]
        if y == 7:
            return p["corde"]                                   # ceinture de corde
        if face == "devant" and y < 2 and 2 <= x <= 5:
            return p["peau"]                                    # encolure
        return p["tunique_fonce"] if alea.random() < 0.18 else p["tunique"]

    def bras(face, x, y):
        if face == "dessus":
            return p["tunique"]
        if face == "dessous":
            return p["peau_ombre"]
        return p["tunique"] if y < 4 else (p["peau_ombre"] if face == "dos" else p["peau"])

    def jambe(face, x, y):
        if face == "dessous" or y == 11:
            return p["terre"]                                   # pieds nus et sales
        if face == "dessus" or y < 3:
            return p["tunique"]
        return p["peau_ombre"] if face in ("dos", "droite") else p["peau"]

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
