package com.royaumedesidees.pnj;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.royaumedesidees.grace.Grace;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

/**
 * La commande {@code /quete}, ouverte à tous : où on en est de chaque quête, ce qu'il faut faire et où aller, plus un
 * rappel de la Voie et de la Grâce. C'est le carnet du joueur : rien dans le Royaume ne doit rester sans explication.
 */
public final class JournalQuetes {
    private static final String CLE = "message.royaumedesidees.journal.";

    private JournalQuetes() {
    }

    public static void enregistrer(RegisterCommandsEvent evenement) {
        evenement.getDispatcher().register(Commands.literal("quete").executes(JournalQuetes::afficher));
    }

    private static int afficher(CommandContext<CommandSourceStack> contexte) throws CommandSyntaxException {
        ServerPlayer joueur = contexte.getSource().getPlayerOrException();
        joueur.sendSystemMessage(Component.translatable(CLE + "titre").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD));
        joueur.sendSystemMessage(Component.translatable(CLE + "voie",
                Component.translatable("voie.royaumedesidees." + Grace.voie(joueur).getSerializedName()), Grace.valeur(joueur))
                .withStyle(ChatFormatting.GRAY));
        joueur.sendSystemMessage(ligne("conversion", etapeConversion(QueteConversion.etat(joueur))));
        joueur.sendSystemMessage(ligne("impots", etapeImpots(joueur)));
        joueur.sendSystemMessage(Component.translatable(CLE + "autres").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        return 1;
    }

    private static Component ligne(String quete, String etape) {
        return Component.empty()
                .append(Component.translatable(CLE + quete).withStyle(ChatFormatting.YELLOW))
                .append(Component.literal(" : "))
                .append(Component.translatable(CLE + quete + "." + etape));
    }

    private static String etapeConversion(int etat) {
        if (etat == QueteConversion.AUCUNE) {
            return "aucune";
        }
        if (etat < QueteConversion.SILENCE) {
            return "poires";
        }
        if (etat < QueteConversion.FIGUIER) {
            return "silence";
        }
        if (etat < QueteConversion.ROMAINS) {
            return "figuier";
        }
        if (etat < QueteConversion.BAPTEME) {
            return "romains";
        }
        return etat < QueteConversion.FINIE ? "bapteme" : "finie";
    }

    private static String etapeImpots(ServerPlayer joueur) {
        return QueteImpots.etapeJournal(joueur);
    }
}
