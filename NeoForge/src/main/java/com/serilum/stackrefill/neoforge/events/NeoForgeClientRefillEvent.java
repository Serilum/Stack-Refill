package com.serilum.stackrefill.neoforge.events;

import com.serilum.stackrefill.events.RefillEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;

public class NeoForgeClientRefillEvent {
	@SubscribeEvent()
	public static void onWorldTick(ClientTickEvent.Pre e) {
		RefillEvent.processTick(true);
	}
}
