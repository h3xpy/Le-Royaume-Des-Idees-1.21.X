"""Textures de la Bibliothèque d'Ambroise (v0.3) : le Pupitre d'Ambroise, un lutrin de noyer sombre à filets d'or,
avec un livre ouvert sur le dessus.

Le bloc reprend la forme du lutrin de Minecraft (modèle « block/lectern ») ; seules ses quatre textures sont à nous.
"""

import random

from outils import couleur, enregistrer, nouvelle, planche

NOYER = {"clair": "#6a4a32", "moyen": "#4e3424", "fonce": "#352216", "noir": "#21140c"}
OR = {"clair": "#f2d27a", "fonce": "#b08a2e"}
POURPRE = {"clair": "#7a2a4a", "fonce": "#4e1a30"}
PAGE = {"clair": "#f4ecd4", "ombre": "#d8cba4", "texte": "#6a5a44"}


def bois(graine):
    """Noyer : veines verticales, un peu de bruit."""
    alea = random.Random(graine)
    image = nouvelle()
    for x in range(16):
        base = NOYER["moyen"] if x % 4 else NOYER["fonce"]
        for y in range(16):
            r = alea.random()
            teinte = NOYER["clair"] if r < 0.12 else (NOYER["noir"] if r > 0.94 else base)
            image.putpixel((x, y), couleur(teinte))
    return image


def filets(image, rangs=(), colonnes=()):
    for y in rangs:
        for x in range(16):
            image.putpixel((x, y), couleur(OR["clair"] if x % 3 else OR["fonce"]))
    for x in colonnes:
        for y in range(16):
            image.putpixel((x, y), couleur(OR["clair"] if y % 3 else OR["fonce"]))
    return image


def dessus():
    """Le pupitre vu de dessus : un drap pourpre bordé d'or, et un livre ouvert dont on devine les lignes."""
    image = nouvelle()
    for y in range(16):
        for x in range(16):
            bord = x in (0, 15) or y in (0, 15)
            teinte = OR["fonce"] if bord else (POURPRE["clair"] if (x + y) % 5 else POURPRE["fonce"])
            image.putpixel((x, y), couleur(teinte))
    for y in range(3, 13):
        for x in range(2, 14):
            if x in (7, 8):
                teinte = PAGE["ombre"]  # pli du livre
            elif y in (3, 12) or x in (2, 13):
                teinte = PAGE["ombre"]
            elif y % 2 == 1 and x not in (3, 12) and (x + y) % 7:
                teinte = PAGE["texte"]
            else:
                teinte = PAGE["clair"]
            image.putpixel((x, y), couleur(teinte))
    return image


def generer():
    print("Bibliothèque d'Ambroise (v0.3) :")
    textures = {
        "pupitre_ambroise_base": filets(bois(386), rangs=(0, 6, 7, 14, 15)),
        "pupitre_ambroise_front": filets(bois(387), colonnes=(0, 7), rangs=(0,)),
        "pupitre_ambroise_sides": filets(bois(388), rangs=(0, 3, 4, 7, 8, 15)),
        "pupitre_ambroise_top": dessus(),
    }
    for nom, image in textures.items():
        enregistrer(image, "block", nom)
    return [planche(list(textures.values()), "pupitre_ambroise", echelle=8)]


if __name__ == "__main__":
    generer()
