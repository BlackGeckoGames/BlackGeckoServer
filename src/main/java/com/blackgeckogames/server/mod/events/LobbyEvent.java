package com.blackgeckogames.server.mod.events;

import net.minecraftforge.event.world.BlockEvent.BreakEvent;

public class LobbyEvent {

	public static void onPlayerBreakEvent(BreakEvent event) {
		event.setCanceled(true);
	}

}
