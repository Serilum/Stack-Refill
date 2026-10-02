package com.serilum.stackrefill.forge.events;

import com.serilum.stackrefill.events.RefillEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;

public class ForgeClientRefillEvent {
	public static void registerEventsInBus() {
		// BusGroup.DEFAULT.register(MethodHandles.lookup(), ForgeClientRefillEvent.class);

		TickEvent.ClientTickEvent.Pre.BUS.addListener(ForgeClientRefillEvent::onWorldTick);
	}

	@SubscribeEvent
	public static void onWorldTick(TickEvent.ClientTickEvent.Pre e) {
		RefillEvent.processTick(true);
	}
}
