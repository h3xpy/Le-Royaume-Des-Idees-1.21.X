"""Récompenses de la quête de conversion (v0.3) : le Livre des Confessions et le Sceau de la Conversion.

Le symbole d'Augustin dans l'art chrétien est un cœur enflammé : on le retrouve sur la couverture du livre et sur le
sceau.
"""

import math

from outils import couleur, enregistrer, nouvelle, planche

CUIR = {"clair": "#9a2a2a", "moyen": "#7a1e1e", "fonce": "#4e1212"}
OR = {"clair": "#f6dc84", "moyen": "#d8a838", "fonce": "#9a7420"}
PAGES = "#efe4c4"
COEUR = {"clair": "#ff5a4a", "moyen": "#d42a2a", "fonce": "#8a1414"}
FLAMME = {"clair": "#fff0a0", "moyen": "#ffb030", "fonce": "#e06010"}


def coeur_enflamme(image, cx, cy, petit=False):
    """Un petit cœur rouge surmonté de trois flammes (5 x 6 pixels, ou 3 x 4 en petit)."""
    if petit:
        motif = ["F.F", "CCC", ".C."]
    else:
        motif = [".F.F.", "F.F.F", "CC.CC", "CCCCC", ".CCC.", "..C.."]
    h = len(motif)
    w = len(motif[0])
    for y, ligne in enumerate(motif):
        for x, c in enumerate(ligne):
            if c == "C":
                teinte = COEUR["clair"] if y <= 2 and x <= 1 else COEUR["moyen"]
            elif c == "F":
                teinte = FLAMME["clair"] if y == 0 else FLAMME["moyen"]
            else:
                continue
            image.putpixel((cx - w // 2 + x, cy - h // 2 + y), couleur(teinte))


def livre():
    """Livre relié de cuir rouge, coins et fermoir d'or, tranche des pages visible à droite, cœur enflammé."""
    image = nouvelle()
    for y in range(2, 15):
        for x in range(2, 13):
            bord = x in (2, 12) or y in (2, 14)
            teinte = CUIR["fonce"] if bord else (CUIR["clair"] if x <= 4 else CUIR["moyen"])
            image.putpixel((x, y), couleur(teinte))
        image.putpixel((13, y), couleur(PAGES if 3 <= y <= 13 else CUIR["fonce"]))
    for x, y in ((3, 3), (11, 3), (3, 13), (11, 13)):
        image.putpixel((x, y), couleur(OR["moyen"]))
    for y in range(7, 10):
        image.putpixel((13, y), couleur(OR["fonce"]))
    coeur_enflamme(image, 7, 8)
    return image


def sceau():
    """Sceau d'or rond, bordé d'un cordon, cœur enflammé au centre, avec un ruban rouge qui pend."""
    image = nouvelle()
    for y in range(16):
        for x in range(16):
            d = math.hypot(x - 7.5, y - 6.5)
            if d <= 6.2:
                if d > 5.2:
                    teinte = OR["fonce"]
                elif d > 4.5:
                    teinte = OR["clair"] if (x + y) % 2 else OR["moyen"]
                else:
                    teinte = OR["moyen"] if x + y > 14 else OR["clair"]
                image.putpixel((x, y), couleur(teinte))
    for y in range(12, 16):
        for x in (5, 6, 9, 10):
            if (x in (5, 10) and y >= 14) or y < 15 or x in (6, 9):
                image.putpixel((x, y), couleur(COEUR["moyen"] if x in (6, 9) else COEUR["fonce"]))
    coeur_enflamme(image, 8, 7)
    return image


def generer():
    print("Quête de conversion (v0.3) :")
    images = {"livre_confessions": livre(), "sceau_conversion": sceau()}
    for nom, image in images.items():
        enregistrer(image, "item", nom)
    return [planche(list(images.values()), "quete_v03", echelle=12)]


if __name__ == "__main__":
    generer()
