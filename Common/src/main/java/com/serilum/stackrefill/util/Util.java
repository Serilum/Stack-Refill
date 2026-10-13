package com.serilum.stackrefill.util;

import com.serilum.stackrefill.config.ConfigHandler;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

public class Util {
	private static final String REFILL_DISABLED_TAG = Reference.MOD_ID + ".disabled";

	public static boolean shouldRefillFor(Player player) {
		if (!(player instanceof ServerPlayer serverPlayer)) {
			return false;
		}

		if (serverPlayer.isCreative() || serverPlayer.isSpectator() || isRefillDisabledFor(serverPlayer)) {
			return false;
		}

		MinecraftServer server = serverPlayer.level().getServer();
		return server.getPlayerList().getPlayer(serverPlayer.getUUID()) == serverPlayer;
	}

	public static boolean isRefillDisabledFor(Player player) {
		return player.entityTags().contains(REFILL_DISABLED_TAG);
	}

	public static void setRefillDisabledFor(Player player, boolean disabled) {
		if (disabled) {
			player.addTag(REFILL_DISABLED_TAG);
		}
		else {
			player.removeTag(REFILL_DISABLED_TAG);
		}
	}

	public static boolean isRefillableKind(ItemStack itemStack) {
		if (itemStack.isDamageableItem()) {
			return ConfigHandler.refillToolsAndWeapons;
		}
		return ConfigHandler.refillBlocksAndItems;
	}

	public static boolean isAllowedItem(ItemStack itemStack) {
		if (itemStack.isEmpty()) {
			return false;
		}

		String itemId = BuiltInRegistries.ITEM.getKey(itemStack.getItem()).toString();
		if (!ConfigHandler.refillBuckets && itemId.endsWith("bucket")) {
			return false;
		}

		if (isInItemList(ConfigHandler.itemBlacklist, itemId)) {
			return false;
		}

		if (!ConfigHandler.itemWhitelist.isBlank()) {
			return isInItemList(ConfigHandler.itemWhitelist, itemId);
		}
		return true;
	}

	public static boolean isSameRefillItem(ItemStack usedStack, ItemStack candidateStack) {
		if (!usedStack.getItem().equals(candidateStack.getItem())) {
			return false;
		}
		return Objects.equals(usedStack.get(DataComponents.POTION_CONTENTS), candidateStack.get(DataComponents.POTION_CONTENTS));
	}

	private static boolean isInItemList(String itemList, String itemId) {
		String itemPath = itemId.substring(itemId.indexOf(':') + 1);

		for (String rawEntry : itemList.split(",")) {
			String entry = rawEntry.strip().toLowerCase(Locale.ROOT);
			if (entry.isEmpty()) {
				continue;
			}

			String comparedId = itemPath;
			if (entry.contains(":")) {
				comparedId = itemId;
			}

			if (comparedId.matches(wildcardToRegex(entry))) {
				return true;
			}
		}
		return false;
	}

	private static String wildcardToRegex(String entry) {
		return Pattern.quote(entry).replace("*", "\\E.*\\Q");
	}
}
