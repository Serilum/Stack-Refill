package com.natamus.stackrefill.forge.events;

import com.natamus.stackrefill.events.RefillEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ForgeClientRefillEvent {
    @SubscribeEvent()
    public void onWorldTick(TickEvent.ClientTickEvent e) {
        if (!e.phase.equals(TickEvent.Phase.START)) {
            return;
        }

        RefillEvent.processTick(true);
    }
}
