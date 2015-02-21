package com.blackgeckogames.server.mod.minigames.skybattle;

import net.minecraft.init.Blocks;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;

public class SkyBattleEvents {

	public static void onPlayerBreakEvent(BreakEvent event) {
		System.out.println("You are not allowed to destroye this block!");
		
		if(event.world.getBlockState(event.pos).getBlock()!=Blocks.sandstone){
			
			event.setCanceled(true);	
			
		}
	
	}

}
