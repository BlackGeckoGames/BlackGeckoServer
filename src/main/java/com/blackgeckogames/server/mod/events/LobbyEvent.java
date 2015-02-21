package com.blackgeckogames.server.mod.events;

import net.minecraft.init.Blocks;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;

public class LobbyEvent {

	public static void onPlayerBreakEvent(BreakEvent event) {
		event.setCanceled(true);
	}
	
	public static void onPlayerInteractEvent(net.minecraftforge.event.entity.player.PlayerInteractEvent event){
		
		if(event.world.getBlockState(event.pos).getBlock() == Blocks.wall_sign){
			
			//if()
			
			
		}
		
		
	}

}
