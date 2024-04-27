package com.natamus.stackrefill.neoforge.events;

import com.natamus.stackrefill.events.RefillEvent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.TickEvent;

@EventBusSubscriber(Dist.CLIENT)
public class NeoForgeClientRefillEvent {
	@SubscribeEvent()
	public static void onWorldTick(TickEvent.ClientTickEvent e) {
		if (!e.phase.equals(TickEvent.Phase.START)) {
			return;
		}

		RefillEvent.processTick(true);
	}
}
