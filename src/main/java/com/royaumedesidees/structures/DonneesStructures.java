package com.royaumedesidees.structures;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;

/**
 * Sauvegarde, dans le dossier de la dimension du Royaume, de la version posée de chaque structure
 * ({@code data/royaumedesidees_structures.dat}).
 */
public class DonneesStructures extends SavedData {
    private static final String NOM = "royaumedesidees_structures";

    private final Map<String, Integer> versions = new HashMap<>();

    public static DonneesStructures de(ServerLevel niveau) {
        return niveau.getDataStorage().computeIfAbsent(new Factory<>(DonneesStructures::new, DonneesStructures::charger), NOM);
    }

    /** Version posée, ou 0 si la structure n'a jamais été posée. */
    public int version(String id) {
        return versions.getOrDefault(id, 0);
    }

    void noter(String id, int version) {
        versions.put(id, version);
        setDirty();
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registres) {
        CompoundTag liste = new CompoundTag();
        versions.forEach(liste::putInt);
        tag.put("versions", liste);
        return tag;
    }

    private static DonneesStructures charger(CompoundTag tag, HolderLookup.Provider registres) {
        DonneesStructures donnees = new DonneesStructures();
        CompoundTag liste = tag.getCompound("versions");
        for (String id : liste.getAllKeys()) {
            donnees.versions.put(id, liste.getInt(id));
        }
        return donnees;
    }
}
