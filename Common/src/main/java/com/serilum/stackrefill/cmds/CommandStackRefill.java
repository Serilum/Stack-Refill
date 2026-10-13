package com.serilum.stackrefill.cmds;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.natamus.collective.functions.MessageFunctions;
import com.serilum.stackrefill.util.Reference;
import com.serilum.stackrefill.util.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.entity.player.Player;

public class CommandStackRefill {
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(Commands.literal("stackrefill")
			.executes((command) -> {
				sendUsage(command.getSource());
				return 1;
			})
			.then(Commands.literal("help")
			.executes((command) -> {
				sendUsage(command.getSource());
				return 1;
			}))
			.then(Commands.literal("toggle")
			.executes((command) -> {
				CommandSourceStack source = command.getSource();
				Player player = getPlayer(source);
				if (player == null) {
					return 1;
				}

				setRefillDisabled(source, player, !Util.isRefillDisabledFor(player));
				return 1;
			}))
			.then(Commands.literal("enable")
			.executes((command) -> {
				CommandSourceStack source = command.getSource();
				Player player = getPlayer(source);
				if (player == null) {
					return 1;
				}

				setRefillDisabled(source, player, false);
				return 1;
			}))
			.then(Commands.literal("disable")
			.executes((command) -> {
				CommandSourceStack source = command.getSource();
				Player player = getPlayer(source);
				if (player == null) {
					return 1;
				}

				setRefillDisabled(source, player, true);
				return 1;
			}))
		);
	}

	private static Player getPlayer(CommandSourceStack source) {
		try {
			return source.getPlayerOrException();
		}
		catch (CommandSyntaxException ex) {
			MessageFunctions.sendTranslatableMessage(source, "collective.shared.message.playeronly", ChatFormatting.RED);
			return null;
		}
	}

	private static void setRefillDisabled(CommandSourceStack source, Player player, boolean disabled) {
		Util.setRefillDisabledFor(player, disabled);

		String playerName = player.getName().getString();
		if (disabled) {
			MessageFunctions.sendTranslatableMessage(source, "collective.stackrefill.message.disabled", ChatFormatting.YELLOW, playerName);
		}
		else {
			MessageFunctions.sendTranslatableMessage(source, "collective.stackrefill.message.enabled", ChatFormatting.DARK_GREEN, playerName);
		}
	}

	private static void sendUsage(CommandSourceStack source) {
		MessageFunctions.sendTranslatableMessage(source, "collective.shared.message.usage", ChatFormatting.DARK_GREEN, Reference.NAME);
		MessageFunctions.sendTranslatableMessage(source, "collective.stackrefill.message.usage.toggle", ChatFormatting.DARK_GREEN);
		MessageFunctions.sendTranslatableMessage(source, "collective.stackrefill.message.usage.enable", ChatFormatting.DARK_GREEN);
		MessageFunctions.sendTranslatableMessage(source, "collective.stackrefill.message.usage.disable", ChatFormatting.DARK_GREEN);
		MessageFunctions.sendTranslatableMessage(source, "collective.stackrefill.message.usage.help", ChatFormatting.DARK_GREEN);
	}
}
