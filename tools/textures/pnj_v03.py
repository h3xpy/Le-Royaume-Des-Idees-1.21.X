"""Skins des PNJ (v0.3), au format du skin joueur 64 x 64 : Augustin jeune, Adéodat, Ambroise, Monique, Pascal.

Chaque partie du corps est une boîte dépliée (dessus, dessous, droite, devant, gauche, dos). Un « peintre » donne la
couleur de chaque pixel d'une face. Les tissus ont un léger grain, pour ne pas faire d'aplats.
"""

import random

from outils import couleur, enregistrer, nouvelle, planche

# Origine (u, v), largeur, hauteur, profondeur de chaque boîte, dans le skin 64 x 64.
TETE = (0, 0, 8, 8, 8)
CHAPEAU = (32, 0, 8, 8, 8)
CORPS = (16, 16, 8, 12, 4)
BRAS_D = (40, 16, 4, 12, 4)
BRAS_G = (32, 48, 4, 12, 4)
JAMBE_D = (0, 16, 4, 12, 4)
JAMBE_G = (16, 48, 4, 12, 4)


def bras(fins):
    largeur = 3 if fins else 4
    return (40, 16, largeur, 12, 4), (32, 48, largeur, 12, 4)


def grain(hexa, x, y, graine, force=10):
    """La couleur, un peu plus claire ou plus sombre selon le pixel (grain du tissu, de la peau)."""
    r = random.Random(x * 7919 + y * 104729 + graine * 1299709).randint(-force, force)
    c = couleur(hexa)
    return tuple(max(0, min(255, v + r)) for v in c[:3]) + (255,)


def boite(image, partie, peintre, graine=0):
    u, v, w, h, d = partie
    faces = {
        "dessus": (u + d, v, w, d),
        "dessous": (u + d + w, v, w, d),
        "droite": (u, v + d, d, h),
        "devant": (u + d, v + d, w, h),
        "gauche": (u + d + w, v + d, d, h),
        "dos": (u + d + w + d, v + d, w, h),
    }
    for nom, (x0, y0, fw, fh) in faces.items():
        for y in range(fh):
            for x in range(fw):
                c = peintre(nom, x, y, fw, fh)
                if c is None:
                    continue
                image.putpixel((x0 + x, y0 + y), grain(c, x0 + x, y0 + y, graine) if isinstance(c, str) else c)


def yeux_bouche(x, y, peau, iris, sourcils, bouche, blanc="#f4f0e8"):
    """Visage de base, 8 x 8 : sourcils (rang 3), yeux (rang 4), bouche (rang 6)."""
    if y == 3 and x in (1, 2, 5, 6):
        return sourcils
    if y == 4:
        return {1: blanc, 2: iris, 5: iris, 6: blanc}.get(x, peau)
    if y == 6 and x in (3, 4):
        return bouche
    if y == 5 and x in (3, 4):
        return ombre(peau)
    return peau


def ombre(hexa, facteur=0.85):
    c = couleur(hexa)
    return "#%02x%02x%02x" % tuple(int(v * facteur) for v in c[:3])


# ------------------------------------------------------------------ Augustin jeune


def augustin_jeune(adeodat=False):
    """Augustin jeune : tunique blanche à bandes rouges (clavi), ceinture, sandales, cheveux noirs bouclés et couronne
    de vigne de fêtard. Adéodat : même allure, plus jeune, tunique bleue, cheveux courts, sans couronne."""
    peau = "#b98560" if adeodat else "#b07a52"
    cheveux = "#1e1510" if adeodat else "#2b1d14"
    boucle = "#4a3426"
    tissu = "#8fb3cc" if adeodat else "#e8e0cc"
    bande = "#5a7f9a" if adeodat else "#9b2b24"
    ceinture = "#c8b48a" if adeodat else "#6b4423"
    sandale = "#6b4423"
    image = nouvelle(64, 64)

    def tete(face, x, y, w, h):
        if face == "dessus":
            return boucle if (x + y) % 3 == 0 and not adeodat else cheveux
        if face == "dessous":
            return peau
        if face == "dos":
            return cheveux if y <= (3 if adeodat else 5) else peau
        if face in ("droite", "gauche"):
            limite = 1 if adeodat else 2
            if y <= limite or (not adeodat and y == 3 and x % 2 == 0):
                return boucle if (x + y) % 3 == 0 and not adeodat else cheveux
            return peau
        # Devant.
        if y == 0 or (y == 1 and not (adeodat and 2 <= x <= 5)):
            return cheveux
        if y == 2 and not adeodat and x in (0, 7):
            return cheveux
        if not adeodat and y == 6 and x in (2, 5):
            return ombre(peau, 0.75)  # coins du sourire
        return yeux_bouche(x, y, peau, "#3a2414", cheveux, "#7a3a2a")

    def couronne(face, x, y, w, h):
        if adeodat:
            return None
        if face == "dessus":
            if x in (0, w - 1) or y in (0, h - 1):
                return "#4f7a2a" if (x + y) % 2 == 0 else "#355a1a"
            return None
        if face == "dessous":
            return None
        if y == 1:
            return "#4f7a2a" if x % 2 == 0 else "#355a1a"
        if y == 2 and face in ("droite", "gauche") and x in (2, 5):
            return "#5a2a5a"  # grappes de raisin, sur les côtés pour ne pas cacher les yeux
        return None

    def corps(face, x, y, w, h):
        if face in ("dessus", "dessous"):
            return tissu
        if y == 6:
            return "#d8b048" if face == "devant" and x in (3, 4) else ceinture
        if face in ("devant", "dos") and x in (1, 6) and not adeodat:
            return bande
        if y == 11:
            return ombre(tissu)
        return tissu

    def bras_(face, x, y, w, h):
        if face == "dessus":
            return tissu
        if face == "dessous":
            return peau
        if y <= 3:
            return bande if y == 3 else tissu
        return peau

    def jambe(face, x, y, w, h):
        if face == "dessus":
            return tissu
        if face == "dessous":
            return sandale
        if y <= 3:
            return ombre(tissu) if y == 3 else tissu
        if y == 11:
            return sandale
        if y == 9 and x == 1 and face == "devant":
            return sandale  # lanière
        return peau

    boite(image, TETE, tete, 1)
    boite(image, CHAPEAU, couronne, 2)
    boite(image, CORPS, corps, 3)
    bd, bg = bras(False)
    boite(image, bd, bras_, 4)
    boite(image, bg, bras_, 5)
    boite(image, JAMBE_D, jambe, 6)
    boite(image, JAMBE_G, jambe, 7)
    return image


# ------------------------------------------------------------------ Ambroise


def ambroise():
    """Ambroise, évêque : cheveux et barbe blancs, aube blanche jusqu'aux pieds, chasuble dorée, pallium blanc marqué
    de croix noires."""
    peau = "#c99a76"
    blanc_poil = "#d8d8cc"
    aube = "#f0ece0"
    chasuble = "#c9a24a"
    bord = "#9a7a2a"
    pallium = "#fafaf2"
    image = nouvelle(64, 64)

    def tete(face, x, y, w, h):
        if face == "dessus":
            # Front dégarni : un peu de peau au milieu du crâne.
            return peau if 2 <= x <= 5 and 1 <= y <= 4 else blanc_poil
        if face == "dessous":
            return blanc_poil
        if face == "dos":
            return blanc_poil if y <= 4 else peau
        if face in ("droite", "gauche"):
            return blanc_poil if y <= 2 or y >= 5 else peau
        # Devant : barbe blanche du rang 5 au rang 7.
        if y == 0:
            return blanc_poil
        if y >= 5:
            return "#8a8a80" if y == 6 and x in (3, 4) else blanc_poil
        return yeux_bouche(x, y, peau, "#4a3a2a", "#f0f0e8", "#8a8a80")

    def corps(face, x, y, w, h):
        if face in ("dessus", "dessous"):
            return chasuble
        if face in ("devant", "dos"):
            if y == 1 and 1 <= x <= 6:
                return pallium
            if x in (3, 4) and y >= 1:
                return "#1a1a1a" if y in (4, 9) else pallium
            if x in (0, w - 1):
                return bord
        return chasuble

    def bras_(face, x, y, w, h):
        if face == "dessus":
            return chasuble
        if face == "dessous":
            return peau
        if y <= 7:
            return bord if y == 7 else chasuble
        return peau if y == 11 else aube

    def jambe(face, x, y, w, h):
        if face == "dessus":
            return aube
        if face == "dessous" or y == 11:
            return "#3a2a1a"
        return ombre(aube, 0.93) if y == 10 else aube

    boite(image, TETE, tete, 11)
    boite(image, CORPS, corps, 13)
    bd, bg = bras(False)
    boite(image, bd, bras_, 14)
    boite(image, bg, bras_, 15)
    boite(image, JAMBE_D, jambe, 16)
    boite(image, JAMBE_G, jambe, 17)
    return image


# ------------------------------------------------------------------ Monique


def monique():
    """Monique : voile sombre qui encadre le visage et couvre les épaules, longue robe brune, des larmes."""
    peau = "#a87452"
    voile = "#2c2f4a"
    robe = "#4b3a2e"
    larme = "#8fd0ff"
    image = nouvelle(64, 64)

    def tete(face, x, y, w, h):
        if face == "dessous":
            return peau
        if face != "devant":
            return voile
        if y <= 1 or x in (0, 7):
            return voile
        if y == 3 and x in (2, 5):
            return "#2a1a14"  # sourcils
        if y == 4:
            return {1: "#f0ece4", 2: "#2a1a14", 5: "#2a1a14", 6: "#f0ece4"}.get(x, peau)
        if (y == 5 and x in (2, 5)) or (y == 6 and x == 2):
            return larme
        if y == 6 and x in (3, 4):
            return "#7a3a3a"
        return peau

    def voile_dessus(face, x, y, w, h):
        if face == "dessous":
            return None
        if face != "devant":
            return voile
        return voile if y == 0 or x in (0, 7) else None

    def corps(face, x, y, w, h):
        if face in ("dessus",):
            return voile
        if face == "dessous":
            return robe
        if y <= 3:
            return voile
        if y == 6:
            return "#8a7a5a"  # cordelière
        return robe

    def bras_(face, x, y, w, h):
        if face == "dessus":
            return voile
        if face == "dessous":
            return peau
        if y <= 2:
            return voile
        return peau if y >= 10 else robe

    def jambe(face, x, y, w, h):
        if face == "dessus":
            return robe
        if face == "dessous" or y == 11:
            return "#151515"
        return ombre(robe, 0.9) if y == 10 else robe

    boite(image, TETE, tete, 21)
    boite(image, CHAPEAU, voile_dessus, 22)
    boite(image, CORPS, corps, 23)
    bd, bg = bras(True)
    boite(image, bd, bras_, 24)
    boite(image, bg, bras_, 25)
    boite(image, JAMBE_D, jambe, 26)
    boite(image, JAMBE_G, jambe, 27)
    return image


# ------------------------------------------------------------------ Pascal


def pascal():
    """Pascal, d'après ses portraits : longs cheveux bruns, fine moustache, habit noir à grand rabat blanc, boutons,
    manchettes blanches, bas sombres, souliers à boucle."""
    peau = "#e6c4a2"
    cheveux = "#3a2a1c"
    noir = "#1c1c22"
    blanc = "#f4f4f0"
    image = nouvelle(64, 64)

    def tete(face, x, y, w, h):
        if face == "dessous":
            return peau
        if face != "devant":
            return cheveux
        if y == 0 or x in (0, 7):
            return cheveux
        if y == 6 and 2 <= x <= 5:
            return cheveux  # moustache
        if y == 7 and x in (3, 4):
            return cheveux  # mouche sous la lèvre
        if y == 6:
            return peau
        return yeux_bouche(x, y, peau, "#3a2a1a", cheveux, "#9a5a4a")

    def chevelure(face, x, y, w, h):
        # Volume des cheveux longs, sur la couche extérieure de la tête.
        if face in ("dessous",):
            return None
        if face == "devant":
            return cheveux if x in (0, 7) and y >= 1 else (cheveux if y == 0 else None)
        return cheveux

    def corps(face, x, y, w, h):
        if face == "dessus":
            return noir
        if face == "dessous":
            return noir
        if face == "devant":
            if y == 0 or (y <= 2 and 2 <= x <= 5):
                return blanc  # rabat
            if x == 4 and y >= 4 and y % 2 == 0:
                return "#8a8a90"  # boutons
        if face == "dos" and y == 0 and 1 <= x <= 6:
            return cheveux
        return noir

    def bras_(face, x, y, w, h):
        if face == "dessus":
            return noir
        if face == "dessous":
            return peau
        if y == 10:
            return blanc  # manchette
        return peau if y == 11 else noir

    def jambe(face, x, y, w, h):
        if face == "dessus":
            return noir
        if face == "dessous":
            return "#111111"
        if y == 11:
            return "#b0b0b8" if face == "devant" and x in (1, 2) else "#111111"
        return "#2b2b33" if y >= 6 else noir

    boite(image, TETE, tete, 31)
    boite(image, CHAPEAU, chevelure, 32)
    boite(image, CORPS, corps, 33)
    bd, bg = bras(False)
    boite(image, bd, bras_, 34)
    boite(image, bg, bras_, 35)
    boite(image, JAMBE_D, jambe, 36)
    boite(image, JAMBE_G, jambe, 37)
    return image


def generer():
    print("PNJ (v0.3) :")
    skins = {
        "augustin_jeune": augustin_jeune(),
        "adeodat": augustin_jeune(adeodat=True),
        "ambroise": ambroise(),
        "monique": monique(),
        "pascal": pascal(),
    }
    for nom, image in skins.items():
        enregistrer(image, "entity/pnj", nom)
    return [planche(list(skins.values()), "pnj_skins", echelle=6)]


if __name__ == "__main__":
    generer()
