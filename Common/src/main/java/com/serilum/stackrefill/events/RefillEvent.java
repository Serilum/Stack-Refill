package com.serilum.stackrefill.events;

import com.natamus.collective.functions.ItemFunctions;
import com.serilum.stackrefill.config.ConfigHandler;
import com.serilum.stackrefill.data.PendingRefill;
import com.serilum.stackrefill.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

import java.util.ArrayList;
import java.util.List;

public class RefillEvent {
	private static final List<PendingRefill> pendingRefills = new ArrayList<>();

	public static void onServerTick(MinecraftServer minecraftServer) {
		if (pendingRefills.isEmpty()) {
			return;
		}

		List<PendingRefill> refillsToProcess = new ArrayList<>(pendingRefills);
		pendingRefills.clear();

		for (PendingRefill pendingRefill : refillsToProcess) {
			if (isStillBeingUsed(pendingRefill)) {
				pendingRefills.add(pendingRefill);
				continue;
			}

			processPendingRefill(pendingRefill);
		}
	}

	private static boolean isStillBeingUsed(PendingRefill pendingRefill) {
		Player player = pendingRefill.player;
		return player.isAlive() && player.isUsingItem() && player.getUsedItemHand().equals(pendingRefill.hand);
	}

	private static void processPendingRefill(PendingRefill pendingRefill) {
		Player player = pendingRefill.player;
		if (!player.isAlive()) {
			return;
		}

		InteractionHand hand = pendingRefill.hand;
		if (hand.equals(InteractionHand.MAIN_HAND) && player.getInventory().getSelectedSlot() != pendingRefill.selectedSlot) {
			return;
		}

		ItemStack handStack = player.getItemInHand(hand);
		if (!handStack.isEmpty()) {
			if (pendingRefill.onlyWhenHandIsEmpty || handStack.getItem().equals(pendingRefill.usedStack.getItem()) || handStack.getCount() > 1) {
				return;
			}
		}

		refillHand(player, hand, pendingRefill.usedStack);
	}

	private static void refillHand(Player player, InteractionHand hand, ItemStack usedStack) {
		int refillSlot = findRefillSlot(player, usedStack);
		if (refillSlot < 0) {
			return;
		}

		Inventory inventory = player.getInventory();
		ItemStack leftoverStack = player.getItemInHand(hand).copy();
		ItemStack refillStack = inventory.getItem(refillSlot);

		inventory.setItem(refillSlot, ItemStack.EMPTY);
		player.setItemInHand(hand, refillStack);

		if (!leftoverStack.isEmpty()) {
			ItemFunctions.giveOrDropItemStack(player, leftoverStack);
		}

		inventory.setChanged();
		player.containerMenu.sendAllDataToRemote();
	}

	private static int findRefillSlot(Player player, ItemStack usedStack) {
		Inventory inventory = player.getInventory();
		for (int i = 35; i > 8; i--) {
			if (isRefillCandidate(inventory.getItem(i), usedStack)) {
				return i;
			}
		}

		if (!ConfigHandler.refillFromOtherHotbarSlots) {
			return -1;
		}

		int selectedSlot = inventory.getSelectedSlot();
		for (int i = 0; i < 9; i++) {
			if (i == selectedSlot) {
				continue;
			}

			if (isRefillCandidate(inventory.getItem(i), usedStack)) {
				return i;
			}
		}
		return -1;
	}

	private static boolean isRefillCandidate(ItemStack slotStack, ItemStack usedStack) {
		if (slotStack.isEmpty() || slotStack == usedStack) {
			return false;
		}
		return Util.isSameRefillItem(usedStack, slotStack);
	}

	private static void addPendingRefill(Player player, InteractionHand hand, ItemStack usedStack, boolean onlyWhenHandIsEmpty) {
		if (!Util.shouldRefillFor(player)) {
			return;
		}

		if (!Util.isAllowedItem(usedStack) || !Util.isRefillableKind(usedStack)) {
			return;
		}

		pendingRefills.add(new PendingRefill(player, hand, usedStack, onlyWhenHandIsEmpty));
	}

	public static ItemStack onItemUse(Player player, ItemStack used, ItemStack newItem, InteractionHand hand) {
		if (used.getCount() > 1) {
			return null;
		}

		addPendingRefill(player, hand, used, false);
		return null;
	}

	public static void onItemBreak(Player player, ItemStack used, InteractionHand hand) {
		if (used == null || hand == null) {
			return;
		}

		addPendingRefill(player, hand, used, false);
	}

	public static void onItemToss(Player player, ItemStack tossedStack) {
		if (!ConfigHandler.refillAfterDroppingLastItem) {
			return;
		}

		if (!Util.shouldRefillFor(player) || !player.isAlive()) {
			return;
		}

		if (!player.getMainHandItem().isEmpty() || tossedStack.getCount() > 1) {
			return;
		}

		if (!Util.isAllowedItem(tossedStack)) {
			return;
		}

		refillHand(player, InteractionHand.MAIN_HAND, tossedStack);
	}

	public static InteractionResult onItemRightClick(Player player, Level world, InteractionHand hand) {
		addPendingRefill(player, hand, player.getItemInHand(hand), true);
		return InteractionResult.PASS;
	}

	public static void onBlockRightClick(Level world, Player player, InteractionHand activehand, BlockPos pos, BlockHitResult hitVec) {
		if (player.isUsingItem()) {
			return;
		}

		ItemStack active = player.getItemInHand(activehand);
		if (active.getCount() > 26) {
			return;
		}

		addPendingRefill(player, activehand, active, false);
	}
}
