package com.royaumedesidees.bloc;

import com.royaumedesidees.registre.ModBlocsEntites;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Entité du Pupitre d'Ambroise : elle ne stocke rien, elle sert seulement à inscrire le pupitre dans la liste des zones
 * de silence quand il est chargé, et à l'en retirer quand il est cassé ou déchargé.
 */
public class PupitreAmbroiseBlocEntite extends BlockEntity {
    public PupitreAmbroiseBlocEntite(BlockPos pos, BlockState etat) {
        super(ModBlocsEntites.PUPITRE_AMBROISE.get(), pos, etat);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (level != null && !level.isClientSide) {
            ZonesSilence.ajouter(level, worldPosition);
        }
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        if (level != null && !level.isClientSide) {
            ZonesSilence.retirer(level, worldPosition);
        }
    }

    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();
        if (level != null && !level.isClientSide) {
            ZonesSilence.retirer(level, worldPosition);
        }
    }
}
