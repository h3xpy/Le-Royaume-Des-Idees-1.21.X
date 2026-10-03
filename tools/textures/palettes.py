"""Palettes de couleurs du mod, une par biome, plus quelques palettes communes.

Chaque texture puise ses couleurs ici : pour changer l'ambiance d'un biome,
on modifie sa palette puis on relance generer_tout.py.
"""

# Caverne de Platon : noirs bleutés, violets éteints, et la lueur du feu.
CAVERNE = {
    "noir": "#0d0b14",
    "fonce": "#17142a",
    "moyen": "#221d3a",
    "clair": "#2f2850",
    "reflet": "#40376a",
    "feu_fonce": "#c4581c",
    "feu": "#ff9a2e",
    "feu_clair": "#ffcf5a",
}

# Jardin de Milan : herbe tendre, poires, figuier, terre cuite des villas.
JARDIN_MILAN = {
    "herbe_fonce": "#4f7a2a",
    "herbe": "#6fa23a",
    "herbe_clair": "#93c45a",
    "poire": "#d8c84a",
    "poire_fonce": "#a89a2c",
    "figuier": "#5b4632",
    "terre_cuite": "#b5603a",
    "chaux": "#ece2cc",
}

# Port-Royal : abbaye grise, ardoise, encre janséniste, aucune couleur vive.
PORT_ROYAL = {
    "ardoise_fonce": "#34383d",
    "ardoise": "#4c5258",
    "pierre": "#7c8186",
    "pierre_clair": "#a3a7aa",
    "encre": "#151820",
    "parchemin": "#d9d2bd",
    "herbe_terne": "#66705a",
}

# Puy de Dôme : basalte, roche volcanique, neige du sommet, verre de Clermont.
PUY_DE_DOME = {
    "basalte_fonce": "#2b2624",
    "basalte": "#463d39",
    "scorie": "#6e4436",
    "roche": "#8a7c70",
    "neige": "#f2f5f7",
    "neige_ombre": "#c9d4dd",
    "mercure": "#b9c0c8",
    "verre": "#cfe8ec",
}

# Hippone : ocre des remparts, bronze romain, mer turquoise, olivier.
HIPPONE = {
    "ocre_fonce": "#9a6a34",
    "ocre": "#c8955a",
    "ocre_clair": "#e3bf86",
    "bronze": "#a0682c",
    "bronze_clair": "#d09a52",
    "mer": "#2a9a9a",
    "mer_clair": "#58c4bc",
    "olive": "#7a8a3a",
}

# Communes : métal des chaînes et lumière dorée des bibliothèques (portail, livres).
FER = {
    "contour": "#141418",
    "fonce": "#2a2a30",
    "moyen": "#4a4a55",
    "clair": "#6e6e7c",
    "reflet": "#9a9aaa",
    "rouille": "#6b3a22",
}

BIBLIOTHEQUE = {
    "or_fonce": "#7a4a10",
    "or": "#c88a1e",
    "or_clair": "#f2c14e",
    "parchemin": "#f6e3b0",
    "blanc": "#fff7dc",
    "cuir_fonce": "#5e1a1a",
    "cuir": "#8a2a22",
    "cuir_clair": "#b0402e",
    "pages": "#e8dcc0",
    "pages_ombre": "#c9b98f",
    "contour": "#2a0d0d",
}

# Laiton et flamme de la Lanterne de Diogène.
LANTERNE = {
    "contour": "#1e140c",
    "laiton_fonce": "#5a3816",
    "laiton": "#8a5a24",
    "laiton_clair": "#c08a3e",
    "verre": "#f7e7a8",
    "verre_bord": "#d9b964",
    "flamme_bord": "#ff9a2e",
    "flamme": "#ffcf5a",
    "flamme_coeur": "#fff7dc",
}

# Vraie forme des Ombres, une fois révélées : une statue de bois, comme les objets que les porteurs font
# passer derrière le muret dans l'allégorie de Platon (« des statues d'hommes en bois et en pierre »).
STATUE = {
    "clair": "#c9a26b",
    "moyen": "#a67c46",
    "fonce": "#7a5530",
    "rainure": "#5a3b20",
    "yeux": "#2e1e10",
}
