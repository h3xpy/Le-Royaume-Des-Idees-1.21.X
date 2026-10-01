package com.royaumedesidees.registre;

import com.royaumedesidees.RoyaumeDesIdees;
import com.royaumedesidees.bloc.PortailRoyaumeBloc;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Tous les blocs du mod. Rappel : un identifiant publié ne se renomme et ne se supprime jamais.
 */
public final class ModBlocs {
    public static final DeferredRegister.Blocks BLOCS = DeferredRegister.createBlocks(RoyaumeDesIdees.MODID);

    /** Butin des Ombres et pierre décorative de la Caverne. */
    public static final DeferredBlock<Block> PIERRE_OMBRE = BLOCS.registerSimpleBlock("pierre_ombre",
            proprietesPierreOmbre().sound(SoundType.DEEPSLATE));

    public static final DeferredBlock<Block> PIERRE_OMBRE_TAILLEE = BLOCS.registerSimpleBlock("pierre_ombre_taillee",
            proprietesPierreOmbre().sound(SoundType.DEEPSLATE_BRICKS));

    /** Intérieur du portail : non obtenable, traversable, lumineux. */
    public static final DeferredBlock<PortailRoyaumeBloc> PORTAIL_ROYAUME = BLOCS.registerBlock("portail_royaume",
            PortailRoyaumeBloc::new,
            BlockBehaviour.Properties.of()
                    .noCollission()
                    .strength(-1.0F)
                    .sound(SoundType.GLASS)
                    .lightLevel(etat -> 11)
                    .pushReaction(PushReaction.BLOCK)
                    .noLootTable());

    /** Chaînes de l'arrivée dans la Caverne : se cassent vite, à la main, et ne lâchent rien. */
    public static final DeferredBlock<Block> CHAINE_CAVERNE = BLOCS.registerSimpleBlock("chaine_caverne",
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(0.4F)
                    .sound(SoundType.CHAIN)
                    .noOcclusion()
                    .noLootTable());

    /** Pierre sombre, aussi dure que la pierre vanilla ; il faut une pioche pour la récupérer. */
    private static BlockBehaviour.Properties proprietesPierreOmbre() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_BLACK)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .strength(1.5F, 6.0F);
    }

    private ModBlocs() {
    }
}
