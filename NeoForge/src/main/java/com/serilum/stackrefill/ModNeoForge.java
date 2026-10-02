package com.serilum.stackrefill;

import com.natamus.collective.check.RegisterMod;
import com.natamus.collective.check.ShouldLoadCheck;
import com.serilum.stackrefill.neoforge.events.NeoForgeClientRefillEvent;
import com.serilum.stackrefill.neoforge.events.NeoForgeRefillEvent;
import com.serilum.stackrefill.util.Reference;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.fml.loading.FMLEnvironment;

@Mod(Reference.MOD_ID)
public class ModNeoForge {
	
	public ModNeoForge(IEventBus modEventBus) {
		if (!ShouldLoadCheck.shouldLoad(Reference.MOD_ID)) {
			return;
		}

		modEventBus.addListener(this::loadComplete);

		setGlobalConstants();
		ModCommon.init();

		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}

	private void loadComplete(final FMLLoadCompleteEvent event) {
		NeoForge.EVENT_BUS.register(NeoForgeRefillEvent.class);

		if (FMLEnvironment.dist.equals(Dist.CLIENT)) {
			NeoForge.EVENT_BUS.register(NeoForgeClientRefillEvent.class);
		}
	}

	private static void setGlobalConstants() {

	}
}