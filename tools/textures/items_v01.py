"""Items de la v0.1 : le livre Tolle, Lege et la Lanterne de Diogène, dessinés pixel par pixel."""

from outils import depuis_grille, enregistrer, planche
from palettes import BIBLIOTHEQUE, LANTERNE

# Livre de cuir rouge, tranche des pages à droite, cœur enflammé doré sur la couverture
# (le cœur ardent est l'attribut traditionnel de saint Augustin).
TOLLE_LEGE = [
    "................",
    "..oooooooooooo..",
    ".oDrrrrrrrrrro..",
    ".oDrRRRRORRRRpo.",
    ".oDrRRROFORRRqo.",
    ".oDrRRGGRGGRRpo.",
    ".oDrRGGGGGGGRqo.",
    ".oDrRGYGGGGGRpo.",
    ".oDrRRGGGGGRRqo.",
    ".oDrRRRGGGRRRpo.",
    ".oDrRRRRGRRRRqo.",
    ".oDrRRRRRRRRRpo.",
    ".oDrRRRRRRRRRqo.",
    ".oDRRRRRRRRRRpo.",
    ".oooooooooooopo.",
    "..oooooooooooo..",
]
LEGENDE_TOLLE_LEGE = {
    "o": BIBLIOTHEQUE["contour"],
    "D": BIBLIOTHEQUE["cuir_fonce"],
    "R": BIBLIOTHEQUE["cuir"],
    "r": BIBLIOTHEQUE["cuir_clair"],
    "p": BIBLIOTHEQUE["pages"],
    "q": BIBLIOTHEQUE["pages_ombre"],
    "G": BIBLIOTHEQUE["or_clair"],
    "g": BIBLIOTHEQUE["or"],
    "Y": BIBLIOTHEQUE["blanc"],
    "O": LANTERNE["flamme_bord"],
    "F": LANTERNE["flamme"],
}

# Lanterne à main en laiton : anneau, chapeau, cage vitrée avec sa flamme, socle.
LANTERNE_DIOGENE = [
    "......kkkk......",
    ".....k....k.....",
    ".....k....k.....",
    "......kkkk......",
    "......knnk......",
    ".....kbBBbk.....",
    "....kbBBBBbk....",
    "...knnnnnnnnk...",
    "...kbwvvvvwbk...",
    "...kbvvOFvvbk...",
    "...kbvOFFOvbk...",
    "...kbvOfFOvbk...",
    "...kbwvvvvwbk...",
    "...knnnnnnnnk...",
    "....kbBBBBbk....",
    ".....kkkkkk.....",
]
LEGENDE_LANTERNE = {
    "k": LANTERNE["contour"],
    "n": LANTERNE["laiton_fonce"],
    "b": LANTERNE["laiton"],
    "B": LANTERNE["laiton_clair"],
    "v": LANTERNE["verre"],
    "w": LANTERNE["verre_bord"],
    "O": LANTERNE["flamme_bord"],
    "F": LANTERNE["flamme"],
    "f": LANTERNE["flamme_coeur"],
}


def generer():
    print("Items de la v0.1 :")
    tolle_lege = depuis_grille(TOLLE_LEGE, LEGENDE_TOLLE_LEGE)
    lanterne = depuis_grille(LANTERNE_DIOGENE, LEGENDE_LANTERNE)
    enregistrer(tolle_lege, "item", "tolle_lege")
    enregistrer(lanterne, "item", "lanterne_diogene")
    return [planche([tolle_lege, lanterne], "items_v01")]


if __name__ == "__main__":
    generer()
