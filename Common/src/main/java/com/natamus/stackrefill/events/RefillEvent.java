package com.natamus.stackrefill.events;

import com.mojang.datafixers.util.Pair;
import com.natamus.collective.functions.ItemFunctions;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

public class RefillEvent {
	private static final List<Pair<Player, ItemStack>> addStackList = Collections.synchronizedList(new ArrayList<>());
	private static final List<Pair<InteractionHand, Pair<Player, ItemStack>>> addSingleList = Collections.synchronizedList(new ArrayList<>());

	private static final List<Pair<Player, InteractionHand>> checkFishingRodList = Collections.synchronizedList(new ArrayList<>());
	private static final List<Pair<InteractionHand, Pair<Player, ItemStack>>> checkItemUsedList = Collections.synchronizedList(new ArrayList<>());

	public static void onWorldTick(ServerLevel world) {
		processTick(false);
	}

	public static void processTick(boolean isClientSide) {
		try {
			if (!addStackList.isEmpty()) {
				Pair<Player, ItemStack> pair = addStackList.get(0);
				if (pair != null) {
					Player player = pair.getFirst();
					ItemStack stackToGive = pair.getSecond();

					if (player.isAlive()) {
						ItemStack heldmainhand = player.getMainHandItem();
						if (heldmainhand.isEmpty()) {
							player.setItemInHand(InteractionHand.MAIN_HAND, stackToGive);
						} else {
							ItemFunctions.giveOrDropItemStack(player, stackToGive);
						}
					}
					else {
						player.drop(stackToGive, false);
					}

					player.getInventory().setChanged();
				}
				addStackList.remove(0);
			}
			if (!addSingleList.isEmpty()) {
				Pair<InteractionHand, Pair<Player, ItemStack>> pair = addSingleList.get(0);
				if (pair != null) {
					Pair<Player, ItemStack> insidepair = pair.getSecond();

					InteractionHand hand = pair.getFirst();
					Player player = insidepair.getFirst();
					ItemStack stackToGive = insidepair.getSecond();

					if (player.isAlive()) {
						ItemStack handstack = player.getItemInHand(hand).copy();

						player.setItemInHand(hand, stackToGive);

						if (!handstack.isEmpty()) {
							ItemFunctions.giveOrDropItemStack(player, handstack);
						}
					}
					else {
						player.drop(stackToGive, false);
					}

					player.getInventory().setChanged();
				}
				addSingleList.remove(0);
			}
			if (!checkFishingRodList.isEmpty()) {
				Pair<Player, InteractionHand> pair = checkFishingRodList.get(0);
				if (pair != null) {
					Player player = pair.getFirst();

					if (player.isAlive()) {
						InteractionHand hand = pair.getSecond();
						if (player.getItemInHand(hand).isEmpty()) {
							Inventory inv = player.getInventory();

							for (int i = 35; i > 8; i--) {
								ItemStack slot = inv.getItem(i);
								if (slot.getItem() instanceof FishingRodItem) {
									player.setItemInHand(hand, slot.copy());
									slot.setCount(0);
									break;
								}
							}
						}
					}

					player.getInventory().setChanged();
				}
				checkFishingRodList.remove(0);
			}
			if (!checkItemUsedList.isEmpty()) {
				Pair<InteractionHand, Pair<Player, ItemStack>> pair = checkItemUsedList.get(0);
				if (pair != null) {
					Pair<Player, ItemStack> insidepair = pair.getSecond();

					InteractionHand hand = pair.getFirst();
					Player player = insidepair.getFirst();
					if (player.isAlive() && !player.isUsingItem()) {
						ItemStack usedstack = insidepair.getSecond();
						ItemStack handstack = player.getItemInHand(hand).copy();
						if (!(usedstack.getItem().equals(handstack.getItem()) && usedstack.getCount() == handstack.getCount())) {
							boolean shouldcontinue = false;
							if (handstack.getCount() <= 1) {
								if (usedstack.getItem().equals(handstack.getItem())) {
									if (handstack.isEmpty()) {
										shouldcontinue = true;
									}
								} else {
									shouldcontinue = true;
								}
							}

							if (shouldcontinue) {
								Item useditem = usedstack.getItem();

								Inventory inv = player.getInventory();
								for (int i = 35; i > 8; i--) {
									ItemStack slot = inv.getItem(i);
									Item slotitem = slot.getItem();
									if (useditem.equals(slotitem)) {
										if (slotitem instanceof PotionItem) {
											if (!PotionUtils.getPotion(usedstack).equals(PotionUtils.getPotion(slot))) {
												continue;
											}
										}

										player.setItemInHand(hand, slot.copy());
										slot.setCount(0);

										if (!handstack.isEmpty()) {
											ItemFunctions.giveOrDropItemStack(player, handstack);
										}

										player.getInventory().setChanged();
										break;
									}
								}
							}
						}
					}
				}
				checkItemUsedList.remove(0);
			}
		}
		catch(IndexOutOfBoundsException | NoSuchElementException ignored) {}
	}

	public static ItemStack onItemUse(Player player, ItemStack used, ItemStack newItem, InteractionHand hand) {
		if (player.isCreative()) {
			return null;
		}

		int amount = used.getCount();
		if (amount > 1) {
			return null;
		}

		Pair<Player, ItemStack> insidepair = new Pair<>(player, used.copy());
		Pair<InteractionHand, Pair<Player, ItemStack>> pair = new Pair<>(hand, insidepair);
		checkItemUsedList.add(pair);
		return null;
	}

	public static void onItemBreak(Player player, ItemStack used, InteractionHand hand) {
		if (player.isCreative()) {
			return;
		}

		if (used == null) {
			return;
		}

		Item useditem = used.getItem();
		if (useditem instanceof BlockItem || useditem instanceof BucketItem || useditem instanceof PotionItem) {
			return;
		}

		int amount = used.getCount();
		if (amount > 1) {
			return;
		}

		if (hand == null) {
			return;
		}

		Inventory inv = player.getInventory();
		for (int i=35; i > 8; i--) {
			ItemStack slot = inv.getItem(i);
			Item slotitem = slot.getItem();
			if (useditem.equals(slotitem)) {
				Pair<Player, ItemStack> insidepair = new Pair<>(player, slot.copy());
				Pair<InteractionHand, Pair<Player, ItemStack>> pair = new Pair<>(hand, insidepair);
				addSingleList.add(pair);
				slot.setCount(0);
				break;
			}
		}

		player.getInventory().setChanged();
	}

	public static void onItemToss(Player player, ItemStack tossedstack) {
		if (player.isCreative()) {
			return;
		}

		Item tosseditem = tossedstack.getItem();

		InteractionHand activehand = InteractionHand.MAIN_HAND;
		ItemStack activestack = player.getMainHandItem();

		if (!activestack.isEmpty()) {
			return;
		}

		if (tossedstack.getCount() > 1) {
			return;
		}

		Inventory inv = player.getInventory();
		for (int i=35; i > 8; i--) {
			ItemStack slot = inv.getItem(i);
			Item slotitem = slot.getItem();
			if (tosseditem.equals(slotitem)) {
				if (slotitem instanceof PotionItem) {
					if (!PotionUtils.getPotion(tossedstack).equals(PotionUtils.getPotion(slot))) {
						continue;
					}
				}

				player.setItemInHand(activehand, slot.copy());
				slot.setCount(0);
				break;
			}
		}

		player.getInventory().setChanged();
	}

	public static InteractionResultHolder<ItemStack> onItemRightClick(Player player, Level world, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (player.isCreative()) {
			return InteractionResultHolder.pass(stack);
		}

		Item item = stack.getItem();
		if (item instanceof FishingRodItem) {
			int damage = stack.getDamageValue();
			int maxdamage = stack.getMaxDamage();

			if (maxdamage - damage < 5) {
				Pair<Player, InteractionHand> toadd = new Pair<>(player, hand);
				checkFishingRodList.add(toadd);
			}
		}
		else if (item instanceof EggItem || item instanceof SnowballItem || item instanceof FireworkRocketItem) {
			if (stack.getCount() > 1) {
				return InteractionResultHolder.pass(stack);
			}

			Pair<Player, ItemStack> insidepair = new Pair<>(player, stack.copy());
			Pair<InteractionHand, Pair<Player, ItemStack>> pair = new Pair<>(hand, insidepair);
			checkItemUsedList.add(pair);
		}

		return InteractionResultHolder.pass(stack);
	}

	public static void onBlockRightClick(Level world, Player player, InteractionHand activehand, BlockPos pos, BlockHitResult hitVec) {
		if (player.isCreative()) {
			return;
		}

		if (player.isUsingItem()) {
			return;
		}

		ItemStack active = player.getItemInHand(activehand);

		int amount = active.getCount();
		if (amount > 26) {
			return;
		}

		Pair<Player, ItemStack> insidepair = new Pair<>(player, active.copy());
		Pair<InteractionHand, Pair<Player, ItemStack>> pair = new Pair<>(activehand, insidepair);

		try {
			checkItemUsedList.add(pair);
		}
		catch (ArrayIndexOutOfBoundsException ignored) {}
	}
}
