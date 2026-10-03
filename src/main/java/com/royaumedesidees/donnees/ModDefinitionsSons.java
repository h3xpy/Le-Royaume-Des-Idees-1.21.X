package com.royaumedesidees.donnees;

import com.royaumedesidees.RoyaumeDesIdees;
import com.royaumedesidees.registre.ModSons;
import net.minecraft.data.PackOutput;
import net.minecraft.server.packs.PackType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

/** Écrit sounds.json. Les .ogg sont fournis par l'utilisateur : s'ils manquent, le son est muet. */
public class ModDefinitionsSons extends SoundDefinitionsProvider {
    private final ExistingFileHelper fichiers;

    public ModDefinitionsSons(PackOutput sortie, ExistingFileHelper fichiers) {
        super(sortie, RoyaumeDesIdees.MODID, fichiers);
        this.fichiers = fichiers;
    }

    @Override
    public void registerSounds() {
        // Le fichier n'est pas sur le dépôt git (droits d'auteur) : on le déclare pour que la vérification
        // ne bloque pas sur une copie du projet qui ne l'a pas.
        fichiers.trackGenerated(RoyaumeDesIdees.id("musique_royaume"), PackType.CLIENT_RESOURCES, ".ogg", "sounds");
        // Morceau long : lu en flux (stream) plutôt que chargé d'un bloc en mémoire.
        add(ModSons.MUSIQUE_ROYAUME, definition().with(sound(RoyaumeDesIdees.id("musique_royaume")).stream()));

        // Sons de la Culpabilité (v0.2), à fournir : voir docs/sons_a_fournir.md.
        fichiers.trackGenerated(RoyaumeDesIdees.id("sanglots"), PackType.CLIENT_RESOURCES, ".ogg", "sounds");
        add(ModSons.SANGLOTS, definition().subtitle("sous_titre.royaumedesidees.sanglots")
                .with(sound(RoyaumeDesIdees.id("sanglots")).stream()));
        fichiers.trackGenerated(RoyaumeDesIdees.id("musique_culpabilite"), PackType.CLIENT_RESOURCES, ".ogg", "sounds");
        add(ModSons.MUSIQUE_CULPABILITE, definition().with(sound(RoyaumeDesIdees.id("musique_culpabilite")).stream()));
    }
}
