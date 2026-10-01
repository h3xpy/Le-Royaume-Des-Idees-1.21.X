"""Outils communs aux scripts de textures : création, dessin, enregistrement, aperçus."""

from pathlib import Path

from PIL import Image

RACINE = Path(__file__).resolve().parents[2]
TEXTURES = RACINE / "src" / "main" / "resources" / "assets" / "royaumedesidees" / "textures"
# Les aperçus agrandis vont dans build/, ignoré par git.
APERCUS = RACINE / "build" / "apercus_textures"


def couleur(hexa, alpha=255):
    """Convertit '#rrggbb' en tuple RGBA."""
    hexa = hexa.lstrip("#")
    return (int(hexa[0:2], 16), int(hexa[2:4], 16), int(hexa[4:6], 16), alpha)


def nouvelle(largeur=16, hauteur=16):
    return Image.new("RGBA", (largeur, hauteur), (0, 0, 0, 0))


def depuis_grille(lignes, legende):
    """Dessine une image à partir d'une grille de caractères.

    Chaque caractère de `lignes` est cherché dans `legende` (caractère -> '#rrggbb');
    un '.' est transparent. Toutes les lignes doivent avoir la même longueur.
    """
    largeur = len(lignes[0])
    for numero, ligne in enumerate(lignes):
        if len(ligne) != largeur:
            raise ValueError(f"ligne {numero} : {len(ligne)} caractères au lieu de {largeur} : {ligne!r}")
    image = nouvelle(largeur, len(lignes))
    for y, ligne in enumerate(lignes):
        for x, caractere in enumerate(ligne):
            if caractere != ".":
                image.putpixel((x, y), couleur(legende[caractere]))
    return image


def melanger(c1, c2, t):
    """Mélange linéaire de deux couleurs RGBA, t entre 0 et 1."""
    return tuple(round(a + (b - a) * t) for a, b in zip(c1, c2))


def degrade(couleurs, t):
    """Couleur à la position t (0 à 1) d'un dégradé passant par la liste `couleurs` (RGBA)."""
    t = min(max(t, 0.0), 1.0) * (len(couleurs) - 1)
    i = min(int(t), len(couleurs) - 2)
    return melanger(couleurs[i], couleurs[i + 1], t - i)


def enregistrer(image, categorie, nom, mcmeta=None):
    """Écrit textures/<categorie>/<nom>.png (et son .mcmeta si fourni) dans les ressources du mod."""
    dossier = TEXTURES / categorie
    dossier.mkdir(parents=True, exist_ok=True)
    chemin = dossier / f"{nom}.png"
    image.save(chemin)
    if mcmeta is not None:
        (dossier / f"{nom}.png.mcmeta").write_text(mcmeta + "\n", encoding="utf-8")
    print(f"  {chemin.relative_to(RACINE)}")
    return chemin


def damier(largeur, hauteur, case=8):
    """Fond en damier gris, pour voir la transparence dans les aperçus."""
    fond = Image.new("RGBA", (largeur, hauteur))
    for y in range(hauteur):
        for x in range(largeur):
            clair = ((x // case) + (y // case)) % 2 == 0
            fond.putpixel((x, y), (200, 200, 200, 255) if clair else (150, 150, 150, 255))
    return fond


def planche(entrees, nom, echelle=12, marge=8):
    """Assemble des textures agrandies côte à côte sur un damier, pour les vérifier d'un coup d'œil."""
    agrandies = [image.resize((image.width * echelle, image.height * echelle), Image.NEAREST) for image in entrees]
    largeur = sum(image.width for image in agrandies) + marge * (len(agrandies) + 1)
    hauteur = max(image.height for image in agrandies) + 2 * marge
    sortie = damier(largeur, hauteur)
    x = marge
    for image in agrandies:
        sortie.alpha_composite(image, (x, marge))
        x += image.width + marge
    APERCUS.mkdir(parents=True, exist_ok=True)
    chemin = APERCUS / f"{nom}.png"
    sortie.save(chemin)
    return chemin
