package com.serilum.stackrefill.events;

import net.minecraft.client.Minecraft;

public class ClientRefillEvent {
	public static void onClientTick(Minecraft mc) {
		RefillEvent.processTick(true);
	}
}
