package com.serilum.stackrefill.util;

import com.serilum.stackrefill.config.ConfigHandler;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;

import java.util.Locale;
import java.util.Optional;
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
		return player.getTags().contains(REFILL_DISABLED_TAG);
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

		Optional<ResourceKey<Item>> itemKey = itemStack.getItemHolder().unwrapKey();
		if (itemKey.isEmpty()) {
			return true;
		}

		String itemId = itemKey.get().location().toString();
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
		return PotionUtils.getPotion(usedStack).equals(PotionUtils.getPotion(candidateStack));
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
