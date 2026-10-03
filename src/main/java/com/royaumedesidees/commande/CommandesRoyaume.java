package com.royaumedesidees.commande;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.royaumedesidees.grace.Grace;
import com.royaumedesidees.registre.ModMonde;
import com.royaumedesidees.structures.DonneesStructures;
import com.royaumedesidees.structures.PoseurStructures;
import com.royaumedesidees.structures.StructureRoyaume;
import com.royaumedesidees.structures.StructuresRoyaume;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import java.util.Optional;

/**
 * Commandes opérateur du mod :
 * <ul>
 *   <li>{@code /royaume structures} : liste les structures, leur version posée et attendue ;</li>
 *   <li>{@code /royaume structures reposer <id>} : force la repose d'une structure ;</li>
 *   <li>{@code /royaume grace <joueur> [definir|ajouter <n>]} : lit ou règle la Grâce d'un joueur (pour les tests).</li>
 * </ul>
 */
public final class CommandesRoyaume {
    private static final String CLE = "commande.royaumedesidees.structures.";

    private CommandesRoyaume() {
    }

    public static void enregistrer(RegisterCommandsEvent evenement) {
        evenement.getDispatcher().register(Commands.literal("royaume")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("structures")
                        .executes(CommandesRoyaume::lister)
                        .then(Commands.literal("reposer")
                                .then(Commands.argument("id", StringArgumentType.word())
                                        .suggests((contexte, suggestions) -> SharedSuggestionProvider.suggest(
                                                StructuresRoyaume.TOUTES.stream().map(StructureRoyaume::id), suggestions))
                                        .executes(CommandesRoyaume::reposer))))
                .then(Commands.literal("grace")
                        .then(Commands.argument("joueur", EntityArgument.player())
                                .executes(contexte -> grace(contexte, null))
                                .then(Commands.literal("definir")
                                        .then(Commands.argument("n", IntegerArgumentType.integer(0, Grace.MAXIMUM))
                                                .executes(contexte -> grace(contexte, "definir"))))
                                .then(Commands.literal("ajouter")
                                        .then(Commands.argument("n", IntegerArgumentType.integer(1, Grace.MAXIMUM))
                                                .executes(contexte -> grace(contexte, "ajouter")))))));
    }

    private static int grace(CommandContext<CommandSourceStack> contexte, String action) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        ServerPlayer joueur = EntityArgument.getPlayer(contexte, "joueur");
        if ("definir".equals(action)) {
            Grace.definir(joueur, IntegerArgumentType.getInteger(contexte, "n"));
        } else if ("ajouter".equals(action)) {
            // Passe par les vraies règles : Royaume seulement, moitié pour la Raison, blague du don.
            Grace.ajouter(joueur, IntegerArgumentType.getInteger(contexte, "n"), "commande");
        }
        contexte.getSource().sendSuccess(() -> Component.translatable("commande.royaumedesidees.grace.valeur",
                joueur.getDisplayName(), Grace.valeur(joueur), Component.translatable("voie.royaumedesidees." + Grace.voie(joueur).getSerializedName())), false);
        return Grace.valeur(joueur);
    }

    private static int lister(CommandContext<CommandSourceStack> contexte) {
        ServerLevel royaume = contexte.getSource().getServer().getLevel(ModMonde.ROYAUME);
        if (royaume == null) {
            contexte.getSource().sendFailure(Component.translatable(CLE + "sans_royaume"));
            return 0;
        }
        DonneesStructures donnees = DonneesStructures.de(royaume);
        contexte.getSource().sendSuccess(() -> Component.translatable(CLE + "titre"), false);
        for (StructureRoyaume structure : StructuresRoyaume.TOUTES) {
            int posee = donnees.version(structure.id());
            Component etat = posee == 0 ? Component.translatable(CLE + "jamais")
                    : posee >= structure.version() ? Component.translatable(CLE + "a_jour")
                    : Component.translatable(CLE + "a_reposer");
            contexte.getSource().sendSuccess(() -> Component.translatable(CLE + "ligne", structure.id(), posee,
                    structure.version(), structure.boite().getCenter().toShortString(), etat), false);
        }
        return StructuresRoyaume.TOUTES.size();
    }

    private static int reposer(CommandContext<CommandSourceStack> contexte) {
        String id = StringArgumentType.getString(contexte, "id");
        Optional<StructureRoyaume> structure = StructuresRoyaume.parId(id);
        if (structure.isEmpty()) {
            contexte.getSource().sendFailure(Component.translatable(CLE + "inconnue", id));
            return 0;
        }
        ServerLevel royaume = contexte.getSource().getServer().getLevel(ModMonde.ROYAUME);
        if (royaume == null) {
            contexte.getSource().sendFailure(Component.translatable(CLE + "sans_royaume"));
            return 0;
        }
        PoseurStructures.poser(royaume, structure.get(), true);
        contexte.getSource().sendSuccess(() -> Component.translatable(CLE + "reposee", id, structure.get().version()), true);
        return 1;
    }
}
