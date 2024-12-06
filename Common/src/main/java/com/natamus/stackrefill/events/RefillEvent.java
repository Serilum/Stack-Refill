package com.natamus.stackrefill.events;

import com.mojang.datafixers.util.Pair;
import com.natamus.collective.functions.ItemFunctions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

public class RefillEvent {
	private static final List<Pair<Player, ItemStack>> addstack = new ArrayList<Pair<Player, ItemStack>>();
	private static final List<Pair<InteractionHand, Pair<Player, ItemStack>>> addsingle = new ArrayList<Pair<InteractionHand, Pair<Player, ItemStack>>>();

	private static final List<Pair<Player, InteractionHand>> checkfishingrod = new ArrayList<Pair<Player, InteractionHand>>();
	private static final List<Pair<InteractionHand, Pair<Player, ItemStack>>> checkitemused = new ArrayList<Pair<InteractionHand, Pair<Player, ItemStack>>>();

	public static void onWorldTick(ServerLevel world) {
		processTick(false);
	}

	public static void processTick(boolean isClientSide) {
		try {
			if (addstack.size() > 0) {
				Pair<Player, ItemStack> pair = addstack.getFirst();
				if (pair != null) {
					Player player = pair.getFirst();
					ItemStack togive = pair.getSecond();

					ItemStack heldmainhand = player.getMainHandItem();
					if (heldmainhand.isEmpty()) {
						player.setItemInHand(InteractionHand.MAIN_HAND, togive);
					} else {
						ItemFunctions.giveOrDropItemStack(player, togive);
					}

					player.getInventory().setChanged();
				}
				addstack.removeFirst();
			}
			if (addsingle.size() > 0) {
				Pair<InteractionHand, Pair<Player, ItemStack>> pair = addsingle.getFirst();
				if (pair != null) {
					Pair<Player, ItemStack> insidepair = pair.getSecond();

					InteractionHand hand = pair.getFirst();
					Player player = insidepair.getFirst();
					ItemStack handstack = player.getItemInHand(hand).copy();

					player.setItemInHand(hand, insidepair.getSecond());

					if (!handstack.isEmpty()) {
						ItemFunctions.giveOrDropItemStack(player, handstack);
					}

					player.getInventory().setChanged();
				}
				addsingle.removeFirst();
			}
			if (checkfishingrod.size() > 0) {
				Pair<Player, InteractionHand> pair = checkfishingrod.getFirst();
				if (pair != null) {
					Player player = pair.getFirst();
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

					player.getInventory().setChanged();
				}
				checkfishingrod.removeFirst();
			}
			if (checkitemused.size() > 0) {
				Pair<InteractionHand, Pair<Player, ItemStack>> pair = checkitemused.getFirst();
				if (pair != null) {
					Pair<Player, ItemStack> insidepair = pair.getSecond();

					InteractionHand hand = pair.getFirst();
					Player player = insidepair.getFirst();
					if (!player.isUsingItem()) {
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
											if (!Objects.equals(usedstack.get(DataComponents.POTION_CONTENTS), slot.get(DataComponents.POTION_CONTENTS))) {
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
				checkitemused.removeFirst();
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

		Pair<Player, ItemStack> insidepair = new Pair<Player, ItemStack>(player, used.copy());
		Pair<InteractionHand, Pair<Player, ItemStack>> pair = new Pair<InteractionHand, Pair<Player, ItemStack>>(hand, insidepair);
		checkitemused.add(pair);
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
				Pair<Player, ItemStack> insidepair = new Pair<Player, ItemStack>(player, slot.copy());
				Pair<InteractionHand, Pair<Player, ItemStack>> pair = new Pair<InteractionHand, Pair<Player, ItemStack>>(hand, insidepair);
				addsingle.add(pair);
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
					if (!Objects.equals(tossedstack.get(DataComponents.POTION_CONTENTS), slot.get(DataComponents.POTION_CONTENTS))) {
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
	
	public static InteractionResult onItemRightClick(Player player, Level world, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (player.isCreative()) {
			return InteractionResult.PASS;
		}

		Item item = stack.getItem();
		if (item instanceof FishingRodItem) {
			int damage = stack.getDamageValue();
			int maxdamage = stack.getMaxDamage();

			if (maxdamage - damage < 5) {
				Pair<Player, InteractionHand> toadd = new Pair<Player, InteractionHand>(player, hand);
				checkfishingrod.add(toadd);
			}
		}
		else if (item instanceof EggItem || item instanceof SnowballItem || item instanceof FireworkRocketItem) {
			if (stack.getCount() > 1) {
				return InteractionResult.PASS;
			}

			Pair<Player, ItemStack> insidepair = new Pair<Player, ItemStack>(player, stack.copy());
			Pair<InteractionHand, Pair<Player, ItemStack>> pair = new Pair<InteractionHand, Pair<Player, ItemStack>>(hand, insidepair);
			checkitemused.add(pair);
		}

		return InteractionResult.PASS;
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

		Pair<Player, ItemStack> insidepair = new Pair<Player, ItemStack>(player, active.copy());
		Pair<InteractionHand, Pair<Player, ItemStack>> pair = new Pair<InteractionHand, Pair<Player, ItemStack>>(activehand, insidepair);

		try {
			checkitemused.add(pair);
		}
		catch (ArrayIndexOutOfBoundsException ignored) {}
	}
}
