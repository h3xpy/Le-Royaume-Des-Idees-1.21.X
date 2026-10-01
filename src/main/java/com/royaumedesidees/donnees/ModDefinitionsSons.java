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
        // Le fichier n'existe pas forcément encore : on le déclare pour que la vérification ne bloque pas.
        fichiers.trackGenerated(RoyaumeDesIdees.id("portail_chant"), PackType.CLIENT_RESOURCES, ".ogg", "sounds");
        add(ModSons.PORTAIL_CHANT, definition()
                .subtitle("sous_titre.royaumedesidees.portail_chant")
                .with(sound(RoyaumeDesIdees.id("portail_chant")).stream()));
    }
}
