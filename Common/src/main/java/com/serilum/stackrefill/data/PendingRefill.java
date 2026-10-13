package com.serilum.stackrefill.data;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class PendingRefill {
	public final Player player;
	public final InteractionHand hand;
	public final ItemStack usedStack;
	public final int selectedSlot;
	public final boolean onlyWhenHandIsEmpty;

	public PendingRefill(Player player, InteractionHand hand, ItemStack usedStack, boolean onlyWhenHandIsEmpty) {
		this.player = player;
		this.hand = hand;
		this.usedStack = usedStack.copy();
		this.selectedSlot = player.getInventory().selected;
		this.onlyWhenHandIsEmpty = onlyWhenHandIsEmpty;
	}
}
