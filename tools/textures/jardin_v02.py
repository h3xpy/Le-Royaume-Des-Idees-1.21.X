"""Textures du Jardin de Milan (v0.2) : poires, poirier, figuier, Confessionnal, Étal du verger, Culpabilité."""

import math
import random

from outils import couleur, enregistrer, melanger, nouvelle, planche
from palettes import JARDIN_MILAN

POIRE = {"clair": "#f2e98a", "corps": "#d8c84a", "ombre": "#a89a2c", "contour": "#5e5418"}
POIRE_VOLEE = {"clair": "#fff3a8", "corps": "#f0c43c", "ombre": "#c08a1e", "contour": "#6e4a10"}
FIGUIER = {"ecorce_clair": "#a7a49a", "ecorce": "#8a877d", "ecorce_fonce": "#6c695f",
           "coeur": "#d9c9a0", "cernes": "#bba77a", "planche_clair": "#d6c7a2", "planche": "#c2b088",
           "planche_fonce": "#9c8a62", "feuille_clair": "#5f9a3a", "feuille": "#477a2a", "feuille_fonce": "#30561c"}
BOIS_SOMBRE = {"clair": "#6b4a2e", "moyen": "#4e3420", "fonce": "#352214", "noir": "#1c120a", "laiton": "#c9a24a"}


def poire(palette, eclat=False):
    """Poire dessinée : un profil qui s'élargit du col vers la base arrondie, ombrée en haut à gauche,
    avec sa queue et sa feuille."""
    p = {nom: couleur(valeur) for nom, valeur in palette.items()}
    image = nouvelle()

    def demi_largeur(y):
        if y < 3.5 or y > 14.5:
            return -1.0
        t = min(max((y - 4.0) / 6.0, 0.0), 1.0)
        rayon = 1.6 + 3.2 * t * t * (3 - 2 * t)          # col fin, puis panse large
        if y > 11.0:                                      # base arrondie
            rayon *= math.sqrt(max(0.0, 1 - ((y - 11.0) / 3.6) ** 2))
        return rayon

    def dedans(x, y):
        return abs(x - 7.5) <= demi_largeur(y)

    for y in range(16):
        for x in range(16):
            if not dedans(x, y):
                continue
            bord = not all(dedans(x + dx, y + dy) for dx, dy in ((1, 0), (-1, 0), (0, 1), (0, -1)))
            if bord:
                image.putpixel((x, y), p["contour"])
            elif x - 7.5 + (y - 8) * 0.4 < -1.5:
                image.putpixel((x, y), p["clair"])
            elif x - 7.5 + (y - 8) * 0.4 > 1.8:
                image.putpixel((x, y), p["ombre"])
            else:
                image.putpixel((x, y), p["corps"])
    tige = couleur(JARDIN_MILAN["figuier"])
    feuille = couleur(JARDIN_MILAN["herbe"])
    image.putpixel((7, 2), tige)
    image.putpixel((8, 1), tige)
    for x, y in ((9, 1), (10, 1), (9, 2), (11, 0)):
        image.putpixel((x, y), feuille)
    if eclat:
        # Un reflet blanc : la poire volée brille, elle a l'air bien meilleure.
        for x, y in ((5, 8), (5, 9), (6, 7)):
            image.putpixel((x, y), couleur("#ffffff"))
    return image


def feuillage(graine, teintes, trous=0.12):
    """Feuillage de 16 x 16 avec quelques trous transparents (rendu « cutout », comme les feuilles vanilla)."""
    alea = random.Random(graine)
    t = [couleur(c) for c in teintes]
    image = nouvelle()
    for y in range(16):
        for x in range(16):
            if alea.random() < trous:
                continue
            image.putpixel((x, y), t[min(int(alea.random() ** 1.3 * len(t)), len(t) - 1)])
    return image


def feuilles_poirier(avec_poires):
    image = feuillage(386, [JARDIN_MILAN["herbe_clair"], JARDIN_MILAN["herbe"], JARDIN_MILAN["herbe_fonce"]])
    if avec_poires:
        p = {nom: couleur(valeur) for nom, valeur in POIRE.items()}
        for x0, y0 in ((2, 3), (10, 2), (6, 9), (12, 11)):
            # Petite poire de 2 x 3 : col clair, corps, base ombrée.
            image.putpixel((x0, y0), p["clair"])
            image.putpixel((x0 + 1, y0), p["corps"])
            image.putpixel((x0, y0 + 1), p["corps"])
            image.putpixel((x0 + 1, y0 + 1), p["corps"])
            image.putpixel((x0, y0 + 2), p["ombre"])
            image.putpixel((x0 + 1, y0 + 2), p["ombre"])
            image.putpixel((x0, y0 - 1), couleur(JARDIN_MILAN["figuier"]))
    return image


def bois_figuier_cote():
    """Écorce de figuier : lisse et grise, avec de fines lignes verticales."""
    alea = random.Random(1662)
    image = nouvelle()
    for x in range(16):
        base = FIGUIER["ecorce_clair"] if x % 5 == 1 else FIGUIER["ecorce"]
        for y in range(16):
            teinte = base
            if alea.random() < 0.08:
                teinte = FIGUIER["ecorce_fonce"]
            image.putpixel((x, y), couleur(teinte))
    return image


def bois_figuier_dessus():
    image = nouvelle()
    for y in range(16):
        for x in range(16):
            r = math.hypot(x - 7.5, y - 7.5)
            if r > 7.0:
                teinte = FIGUIER["ecorce"]
            elif int(r) % 2 == 0:
                teinte = FIGUIER["cernes"]
            else:
                teinte = FIGUIER["coeur"]
            image.putpixel((x, y), couleur(teinte))
    return image


def planches(palette, graine):
    """Quatre planches horizontales de 4 pixels, joints sombres, clous aux extrémités."""
    alea = random.Random(graine)
    image = nouvelle()
    for y in range(16):
        rangee, ly = divmod(y, 4)
        decalage = (rangee * 7) % 16
        for x in range(16):
            if ly == 3 or (x + decalage) % 16 == 0:
                teinte = palette["fonce"]
            elif ly == 0:
                teinte = palette["clair"]
            else:
                teinte = palette["moyen"] if alea.random() < 0.85 else palette["fonce"]
            image.putpixel((x, y), couleur(teinte))
    return image


def confessionnal(partie):
    """Cabine en bois sombre : panneaux sur les côtés, porte en bas, grille de confession en haut."""
    b = {nom: couleur(valeur) for nom, valeur in BOIS_SOMBRE.items()}
    image = nouvelle()
    for y in range(16):
        for x in range(16):
            cadre = x in (0, 15) or y in (0, 15)
            if partie == "dessus":
                teinte = b["fonce"] if cadre or y % 4 == 3 else b["moyen"]
            elif partie == "cote":
                panneau = 3 <= x <= 12 and 3 <= y <= 12
                teinte = b["fonce"] if cadre else (b["clair"] if panneau and (x == 3 or y == 3) else b["moyen"])
            elif partie == "bas":
                porte = 2 <= x <= 13 and y >= 1
                teinte = b["fonce"] if cadre or not porte else (b["clair"] if x in (2, 13) else b["moyen"])
                if (x, y) in ((11, 7), (11, 8)):
                    teinte = b["laiton"]           # poignée
            else:
                # Grille en losanges : on devine l'ombre du prêtre derrière.
                dans_grille = 3 <= x <= 12 and 3 <= y <= 12
                if cadre or not dans_grille:
                    teinte = b["fonce"] if cadre else b["moyen"]
                elif (x + y) % 3 == 0 or (x - y) % 3 == 0:
                    teinte = b["clair"]
                else:
                    teinte = b["noir"]
            image.putpixel((x, y), teinte)
    return image


def etal(partie):
    """Caisse de verger à claire-voie ; dessus chargé de poires ; devant, un tronc à aumônes."""
    bois = {"clair": FIGUIER["planche_clair"], "moyen": FIGUIER["planche"], "fonce": FIGUIER["planche_fonce"]}
    image = planches(bois, 1648)
    if partie == "dessus":
        p = {nom: couleur(valeur) for nom, valeur in POIRE.items()}
        for x0, y0 in ((2, 2), (7, 3), (11, 2), (3, 8), (8, 9), (12, 8), (5, 12), (10, 13)):
            for dx, dy, teinte in ((0, 0, "clair"), (1, 0, "corps"), (0, 1, "corps"), (1, 1, "ombre")):
                image.putpixel((x0 + dx, y0 + dy), p[teinte])
    elif partie == "face":
        # Tronc à aumônes : une boîte sombre avec une fente.
        for y in range(5, 11):
            for x in range(5, 11):
                image.putpixel((x, y), couleur(BOIS_SOMBRE["fonce"]))
        for x in range(6, 10):
            image.putpixel((x, 7), couleur(BOIS_SOMBRE["noir"]))
        image.putpixel((7, 9), couleur("#3fbf6a"))   # une émeraude dessinée, pour l'idée
        image.putpixel((8, 9), couleur("#2e9a52"))
    return image


def icone_culpabilite():
    """Icône d'effet (18 x 18) : un cœur gris, fendu en zigzag."""
    image = nouvelle(18, 18)
    gris_clair, gris, gris_fonce, fente = (couleur(c) for c in ("#b8b8c4", "#8a8a98", "#5a5a6e", "#2a2a34"))
    for y in range(18):
        for x in range(18):
            u = (x - 8.5) / 7.5
            v = (8.5 - y) / 7.5
            # Courbe du cœur : (u² + v² - 1)³ - u²v³ ≤ 0.
            if (u * u + v * v - 1) ** 3 - u * u * v ** 3 <= 0:
                teinte = gris_clair if u < -0.3 and v > 0.1 else (gris_fonce if u > 0.4 or v < -0.6 else gris)
                image.putpixel((x, y), teinte)
    for y, x in ((4, 9), (5, 8), (6, 8), (7, 9), (8, 10), (9, 9), (10, 8), (11, 8), (12, 9), (13, 9)):
        image.putpixel((x, y), fente)
    return image


def generer():
    print("Jardin de Milan (v0.2) :")
    items = {"poire": poire(POIRE), "poire_volee": poire(POIRE_VOLEE, eclat=True),
             "confessionnal": None}
    blocs = {
        "feuilles_poirier": feuilles_poirier(False),
        "feuilles_poirier_poires": feuilles_poirier(True),
        "bois_figuier": bois_figuier_cote(),
        "bois_figuier_top": bois_figuier_dessus(),
        "planches_figuier": planches({"clair": FIGUIER["planche_clair"], "moyen": FIGUIER["planche"], "fonce": FIGUIER["planche_fonce"]}, 386),
        "feuilles_figuier": feuillage(430, [FIGUIER["feuille_clair"], FIGUIER["feuille"], FIGUIER["feuille_fonce"]], trous=0.08),
        "confessionnal_cote": confessionnal("cote"),
        "confessionnal_bas_face": confessionnal("bas"),
        "confessionnal_haut_face": confessionnal("haut"),
        "confessionnal_dessus": confessionnal("dessus"),
        "etal_verger_cote": etal("cote"),
        "etal_verger_face": etal("face"),
        "etal_verger_dessus": etal("dessus"),
    }
    # Icône d'inventaire du Confessionnal : la moitié haute posée sur la moitié basse, réduites à 16 x 16.
    icone = nouvelle(16, 32)
    icone.alpha_composite(blocs["confessionnal_haut_face"], (0, 0))
    icone.alpha_composite(blocs["confessionnal_bas_face"], (0, 16))
    icone = icone.resize((8, 16))
    items["confessionnal"] = nouvelle()
    items["confessionnal"].alpha_composite(icone, (4, 0))

    for nom, image in items.items():
        enregistrer(image, "item", nom)
    for nom, image in blocs.items():
        enregistrer(image, "block", nom)
    effet = icone_culpabilite()
    enregistrer(effet, "mob_effect", "culpabilite")
    return [planche(list(items.values()) + [effet], "jardin_items"),
            planche(list(blocs.values())[:7], "jardin_blocs_1", echelle=8),
            planche(list(blocs.values())[7:], "jardin_blocs_2", echelle=8)]


if __name__ == "__main__":
    generer()
