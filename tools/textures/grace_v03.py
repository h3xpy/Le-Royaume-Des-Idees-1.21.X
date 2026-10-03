"""Interface de la Grâce (v0.3) : l'icône de la jauge, une petite auréole dorée."""

import math

from outils import couleur, enregistrer, nouvelle, planche

OR = {"clair": "#fff2b0", "moyen": "#f0c84a", "fonce": "#b8860b", "lueur": "#fff8d8"}


def aureole():
    """Auréole de 9 x 9 vue de trois quarts : un anneau ovale doré, plus clair en haut, avec un éclat."""
    image = nouvelle(9, 9)
    for y in range(9):
        for x in range(9):
            # Ellipse aplatie : demi-axes 4 (horizontal) et 2,6 (vertical), centrée un peu haut.
            u = (x - 4.0) / 4.0
            v = (y - 4.0) / 2.6
            r = math.sqrt(u * u + v * v)
            if 0.62 <= r <= 1.05:
                teinte = OR["clair"] if y <= 3 else (OR["moyen"] if y <= 5 else OR["fonce"])
                image.putpixel((x, y), couleur(teinte))
            elif r < 0.62 and y <= 4:
                # Lueur douce au creux de l'anneau.
                image.putpixel((x, y), couleur(OR["lueur"], 70))
    image.putpixel((7, 1), couleur("#ffffff"))
    return image


def generer():
    print("Grâce (v0.3) :")
    icone = aureole()
    enregistrer(icone, "gui", "grace")
    return [planche([icone], "grace_icone", echelle=16)]


if __name__ == "__main__":
    generer()
