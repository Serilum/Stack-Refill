package com.natamus.stackrefill.forge.events;

import com.natamus.stackrefill.events.RefillEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.util.HashMap;

@EventBusSubscriber
public class ForgeRefillEvent {
	private static final HashMap<String, InteractionHand> lasthandused = new HashMap<String, InteractionHand>();

	@SubscribeEvent
	public void onWorldTick(TickEvent.ServerTickEvent e) {
		if (!e.phase.equals(Phase.START)) {
			return;
		}

		RefillEvent.processTick(false);
	}

	@SubscribeEvent
	public void onItemUse(LivingEntityUseItemEvent.Start e) {
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
	public void onItemUse(LivingEntityUseItemEvent.Finish e) {
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
	public void onItemBreak(PlayerDestroyItemEvent e) {
		RefillEvent.onItemBreak(e.getPlayer(), e.getOriginal(), e.getHand());
	}
	
	@SubscribeEvent
	public void onItemToss(ItemTossEvent e) {
		RefillEvent.onItemToss(e.getPlayer(), e.getEntityItem().getItem());
	}
	
	@SubscribeEvent
	public void onItemRightClick(PlayerInteractEvent.RightClickItem e) {
		RefillEvent.onItemRightClick(e.getPlayer(), e.getWorld(), e.getHand());
	}
	
	@SubscribeEvent
	public void onBlockRightClick(PlayerInteractEvent.RightClickBlock e) {
		RefillEvent.onBlockRightClick(e.getWorld(), e.getPlayer(), e.getHand(), e.getPos(), e.getHitVec());
	}
}
