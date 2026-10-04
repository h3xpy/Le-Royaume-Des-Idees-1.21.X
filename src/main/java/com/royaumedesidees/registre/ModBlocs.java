package com.royaumedesidees.registre;

import com.royaumedesidees.RoyaumeDesIdees;
import com.royaumedesidees.bloc.ConfessionnalBloc;
import com.royaumedesidees.bloc.EtalVergerBloc;
import com.royaumedesidees.bloc.FeuillesFiguierBloc;
import com.royaumedesidees.bloc.FeuillesPoirierBloc;
import com.royaumedesidees.bloc.PortailRoyaumeBloc;
import com.royaumedesidees.bloc.PupitreAmbroiseBloc;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
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

    // --- v0.2 : le Jardin de Milan ---

    /** Feuilles de poirier des vergers : chargées de poires ou non. Cueillir = voler. */
    public static final DeferredBlock<FeuillesPoirierBloc> FEUILLES_POIRIER = BLOCS.registerBlock("feuilles_poirier",
            FeuillesPoirierBloc::new, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES));

    /** Bois de figuier : la ressource du Jardin (pour la Bière d'Augustin, en v0.5). */
    public static final DeferredBlock<RotatedPillarBlock> BOIS_FIGUIER = BLOCS.registerBlock("bois_figuier",
            RotatedPillarBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG));
    public static final DeferredBlock<Block> PLANCHES_FIGUIER = BLOCS.registerSimpleBlock("planches_figuier",
            BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS));
    public static final DeferredBlock<FeuillesFiguierBloc> FEUILLES_FIGUIER = BLOCS.registerBlock("feuilles_figuier",
            FeuillesFiguierBloc::new, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES));

    /** Le Confessionnal, deux blocs de haut : on s'y confesse avec /confesse. */
    public static final DeferredBlock<ConfessionnalBloc> CONFESSIONNAL = BLOCS.registerBlock("confessionnal",
            ConfessionnalBloc::new, BlockBehaviour.Properties.ofFullCopy(Blocks.DARK_OAK_PLANKS).noOcclusion());

    /** Étal du verger : une émeraude contre 3 poires achetées. */
    public static final DeferredBlock<EtalVergerBloc> ETAL_VERGER = BLOCS.registerBlock("etal_verger",
            EtalVergerBloc::new, BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL));

    /** Pupitre d'Ambroise (v0.3) : lutrin qui crée une zone de silence de 8 blocs. */
    public static final DeferredBlock<PupitreAmbroiseBloc> PUPITRE_AMBROISE = BLOCS.registerBlock("pupitre_ambroise",
            PupitreAmbroiseBloc::new, BlockBehaviour.Properties.ofFullCopy(Blocks.LECTERN).noOcclusion());

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
