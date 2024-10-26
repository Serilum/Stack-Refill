package com.natamus.stackrefill.neoforge.events;

import com.natamus.stackrefill.events.RefillEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.player.PlayerDestroyItemEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.HashMap;

@EventBusSubscriber
public class NeoForgeRefillEvent {
	private static final HashMap<String, InteractionHand> lasthandused = new HashMap<String, InteractionHand>();

	@SubscribeEvent
	public static void onWorldTick(ServerTickEvent.Pre e) {
		RefillEvent.processTick(false);
	}

	@SubscribeEvent
	public static void onItemUse(LivingEntityUseItemEvent.Start e) {
		Entity livingEntity = e.getEntity();
		if (!(livingEntity instanceof Player)) {
			return;
		}

		Player player = (Player)livingEntity;
		ItemStack mainstack = player.getMainHandItem();
		ItemStack used = e.getItem();

		InteractionHand hand = InteractionHand.MAIN_HAND;
		if (!(mainstack.getItem().equals(used.getItem()) && mainstack.getCount() == used.getCount())) {
			hand = InteractionHand.OFF_HAND;
		}

		lasthandused.put(player.getName().getString(), hand);
	}

	@SubscribeEvent
	public static void onItemUse(LivingEntityUseItemEvent.Finish e) {
		Entity livingEntity = e.getEntity();
		if (!(livingEntity instanceof Player)) {
			return;
		}
		
		Player player = (Player)livingEntity;
		String playername = player.getName().getString();
		if (!lasthandused.containsKey(playername)) {
			return;
		}

		RefillEvent.onItemUse(player, e.getItem(), null, lasthandused.get(playername));
	}
	
	@SubscribeEvent
	public static void onItemBreak(PlayerDestroyItemEvent e) {
		RefillEvent.onItemBreak(e.getEntity(), e.getOriginal(), e.getHand());
	}
	
	@SubscribeEvent
	public static void onItemToss(ItemTossEvent e) {
		RefillEvent.onItemToss(e.getPlayer(), e.getEntity().getItem());
	}
	
	@SubscribeEvent
	public static void onItemRightClick(PlayerInteractEvent.RightClickItem e) {
		RefillEvent.onItemRightClick(e.getEntity(), e.getLevel(), e.getHand());
	}
	
	@SubscribeEvent
	public static void onBlockRightClick(PlayerInteractEvent.RightClickBlock e) {
		RefillEvent.onBlockRightClick(e.getLevel(), e.getEntity(), e.getHand(), e.getPos(), e.getHitVec());
	}
}
