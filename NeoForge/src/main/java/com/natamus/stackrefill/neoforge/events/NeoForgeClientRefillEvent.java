package com.natamus.stackrefill.neoforge.events;

import com.natamus.stackrefill.events.RefillEvent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber(Dist.CLIENT)
public class NeoForgeClientRefillEvent {
	@SubscribeEvent()
	public static void onWorldTick(ClientTickEvent.Pre e) {
		RefillEvent.processTick(true);
	}
}
