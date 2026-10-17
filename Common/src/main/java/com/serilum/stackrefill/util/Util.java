package com.serilum.stackrefill.util;

import com.natamus.collective.functions.ConfigFunctions;
import com.serilum.stackrefill.config.ConfigHandler;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

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

		String itemId = BuiltInRegistries.ITEM.getKey(itemStack.getItem()).toString();
		if (!ConfigHandler.refillBuckets && itemId.endsWith("bucket")) {
			return false;
		}

		if (ConfigFunctions.isInIdList(ConfigHandler.itemBlacklist, itemId)) {
			return false;
		}

		if (!ConfigHandler.itemWhitelist.isBlank()) {
			return ConfigFunctions.isInIdList(ConfigHandler.itemWhitelist, itemId);
		}
		return true;
	}

	public static boolean isSameRefillItem(ItemStack usedStack, ItemStack candidateStack) {
		if (!usedStack.getItem().equals(candidateStack.getItem())) {
			return false;
		}
		return Objects.equals(usedStack.get(DataComponents.POTION_CONTENTS), candidateStack.get(DataComponents.POTION_CONTENTS));
	}
}
