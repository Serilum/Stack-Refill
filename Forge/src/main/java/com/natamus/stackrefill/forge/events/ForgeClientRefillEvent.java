package com.natamus.stackrefill.forge.events;

import com.natamus.stackrefill.events.RefillEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ForgeClientRefillEvent {
    @SubscribeEvent
    public static void onWorldTick(TickEvent.ClientTickEvent e) {
        if (!e.phase.equals(TickEvent.Phase.START)) {
            return;
        }

        RefillEvent.processTick(true);
    }
}
