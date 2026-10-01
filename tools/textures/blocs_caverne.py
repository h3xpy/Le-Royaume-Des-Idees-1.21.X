"""Blocs de la Caverne de Platon : Pierre d'Ombre, Pierre d'Ombre taillée, Chaîne, Portail du Royaume."""

import math
import random

from outils import couleur, degrade, enregistrer, nouvelle, planche
from palettes import BIBLIOTHEQUE, CAVERNE, FER

NIVEAUX_OMBRE = [couleur(CAVERNE[n]) for n in ("noir", "fonce", "moyen", "clair", "reflet")]


def champ_lisse(graine, taille=16, passes=2):
    """Bruit aléatoire adouci, qui se raccorde sur les bords (la texture se répète sans couture)."""
    alea = random.Random(graine)
    champ = [[alea.random() for _ in range(taille)] for _ in range(taille)]
    for _ in range(passes):
        champ = [[(champ[y][x] * 4
                   + champ[y][(x + 1) % taille] + champ[y][(x - 1) % taille]
                   + champ[(y + 1) % taille][x] + champ[(y - 1) % taille][x]) / 8
                  for x in range(taille)] for y in range(taille)]
    # Ré-étale les valeurs entre 0 et 1 après l'adoucissement.
    bas = min(min(ligne) for ligne in champ)
    haut = max(max(ligne) for ligne in champ)
    return [[(v - bas) / (haut - bas) for v in ligne] for ligne in champ]


def niveau(valeur, seuils):
    """Indice de palette selon des seuils croissants."""
    for indice, seuil in enumerate(seuils):
        if valeur < seuil:
            return indice
    return len(seuils)


def pierre_ombre():
    champ = champ_lisse(graine=1640)
    image = nouvelle()
    for y in range(16):
        for x in range(16):
            image.putpixel((x, y), NIVEAUX_OMBRE[niveau(champ[y][x], [0.08, 0.35, 0.72, 0.93])])
    # Quelques fissures sombres, comme des ombres figées dans la roche.
    alea = random.Random(386)
    for _ in range(3):
        x, y = alea.randrange(16), alea.randrange(16)
        for _ in range(alea.randint(3, 5)):
            image.putpixel((x % 16, y % 16), NIVEAUX_OMBRE[0])
            x += alea.choice((-1, 0, 1))
            y += 1
    return image


def pierre_ombre_taillee():
    """Briques de 8 x 4, décalées d'une rangée à l'autre, avec un biseau clair en haut à gauche."""
    champ = champ_lisse(graine=1654, passes=1)
    image = nouvelle()
    for y in range(16):
        rangee, ly = divmod(y, 4)
        decalage = 4 if rangee % 2 else 0
        for x in range(16):
            lx = (x + decalage) % 8
            if ly == 3 or lx == 7:
                teinte = NIVEAUX_OMBRE[0]            # joint
            elif ly == 0 or lx == 0:
                teinte = NIVEAUX_OMBRE[3]            # arête éclairée
            elif ly == 2 or lx == 6:
                teinte = NIVEAUX_OMBRE[1]            # arête dans l'ombre
            else:
                teinte = NIVEAUX_OMBRE[2 if champ[y][x] < 0.7 else 3]
            image.putpixel((x, y), teinte)
    return image


def chaine_caverne():
    """Deux chaînes verticales sur fond transparent ; le motif se répète tous les 8 pixels."""
    image = nouvelle()
    contour, fonce, moyen, clair = (couleur(FER[n]) for n in ("contour", "fonce", "moyen", "clair"))
    rouille = couleur(FER["rouille"])

    def maillon_face(x0, y0):
        # Anneau vu de face, 4 de large sur 6 de haut.
        for dx in (1, 2):
            image.putpixel((x0 + dx, y0 % 16), clair)
            image.putpixel((x0 + dx, (y0 + 5) % 16), fonce)
        for dy in range(1, 5):
            image.putpixel((x0, (y0 + dy) % 16), clair)
            image.putpixel((x0 + 3, (y0 + dy) % 16), fonce)
        image.putpixel((x0, y0 % 16), contour)
        image.putpixel((x0 + 3, y0 % 16), contour)
        image.putpixel((x0, (y0 + 5) % 16), contour)
        image.putpixel((x0 + 3, (y0 + 5) % 16), contour)

    def maillon_profil(x0, y0):
        # Anneau vu de profil : une barre de 2 de large qui relie deux anneaux de face.
        for dy in range(4):
            image.putpixel((x0 + 1, (y0 + dy) % 16), moyen)
            image.putpixel((x0 + 2, (y0 + dy) % 16), fonce)

    for x0, phase in ((2, 0), (10, 4)):
        for y0 in (phase, phase + 8):
            maillon_profil(x0, y0 + 4)
            maillon_face(x0, y0)
    # Une tache de rouille, pour que ça ait l'air d'avoir servi longtemps.
    image.putpixel((2, 2), rouille)
    image.putpixel((13, 11), rouille)
    return image


IMAGES_PORTAIL = 32


def portail_royaume():
    """Lumière dorée de bibliothèque qui ondule, traversée de lettres qui montent.

    Les ondes utilisent des multiples entiers de 2π/16 en x et y, et de 2π/IMAGES_PORTAIL dans le
    temps : la texture se raccorde avec ses voisines et l'animation boucle sans saut.
    """
    teintes = [couleur(BIBLIOTHEQUE[n], 200) for n in ("or_fonce", "or", "or_clair", "parchemin")]
    lettre = couleur(BIBLIOTHEQUE["blanc"], 240)
    # Lettres : colonne, hauteur de départ, vitesse (tours complets par boucle), forme.
    lettres = [(2, 3, 1, "i"), (6, 11, 2, "l"), (9, 6, 1, "o"), (13, 14, 2, "i"), (11, 1, 1, "l")]
    formes = {"i": [(0, 0), (0, 2)], "l": [(0, 0), (0, 1), (0, 2), (1, 2)], "o": [(0, 0), (1, 0), (0, 1), (1, 1)]}

    planche_animee = nouvelle(16, 16 * IMAGES_PORTAIL)
    for t in range(IMAGES_PORTAIL):
        phase = 2 * math.pi * t / IMAGES_PORTAIL
        for y in range(16):
            for x in range(16):
                ax, ay = 2 * math.pi * x / 16, 2 * math.pi * y / 16
                v = (math.sin(ax + phase) + math.sin(ay * 2 - 2 * phase) + math.sin(ax + ay + phase)) / 3
                planche_animee.putpixel((x, 16 * t + y), degrade(teintes, (v + 1) / 2))
        for colonne, depart, vitesse, forme in lettres:
            haut = (depart - round(16 * vitesse * t / IMAGES_PORTAIL)) % 16
            for dx, dy in formes[forme]:
                planche_animee.putpixel(((colonne + dx) % 16, 16 * t + (haut + dy) % 16), lettre)
    return planche_animee


def generer():
    print("Blocs de la Caverne :")
    images = {
        "pierre_ombre": pierre_ombre(),
        "pierre_ombre_taillee": pierre_ombre_taillee(),
        "chaine_caverne": chaine_caverne(),
    }
    for nom, image in images.items():
        enregistrer(image, "block", nom)
    portail = portail_royaume()
    enregistrer(portail, "block", "portail_royaume",
                mcmeta='{\n  "animation": {\n    "frametime": 2,\n    "interpolate": true\n  }\n}')

    # Aperçus : les blocs seuls, puis en mosaïque 3 x 3 pour vérifier le raccord, puis 8 images du portail.
    mosaiques = []
    for image in images.values():
        mosaique = nouvelle(48, 48)
        for i in range(3):
            for j in range(3):
                mosaique.alpha_composite(image, (16 * i, 16 * j))
        mosaiques.append(mosaique)
    images_portail = [portail.crop((0, 16 * t, 16, 16 * t + 16)) for t in range(0, IMAGES_PORTAIL, 4)]
    return [
        planche(list(images.values()), "blocs_caverne"),
        planche(mosaiques, "blocs_caverne_mosaique", echelle=6),
        planche(images_portail, "portail_images", echelle=8),
    ]


if __name__ == "__main__":
    generer()
