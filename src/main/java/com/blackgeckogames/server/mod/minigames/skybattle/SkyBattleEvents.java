package com.blackgeckogames.server.mod.minigames.skybattle;

import net.minecraft.init.Blocks;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;

public class SkyBattleEvents {

	public static void onPlayerBreakEvent(BreakEvent event) {
		
		
		if(event.world.getBlockState(event.pos).getBlock() != Blocks.sandstone || event.world.getBlockState(event.pos).getBlock() != Blocks.end_stone || event.world.getBlockState(event.pos).getBlock() != Blocks.obsidian  || event.world.getBlockState(event.pos).getBlock() != Blocks.beacon){
			
			System.out.println("You are not allowed to destroy this block!");
			event.setCanceled(true);	
			
		}
		
		else if(event.world.getBlockState(event.pos).getBlock() == Blocks.beacon){
			
			String Team = "";
			
			//Abfrage welcher TeamBeacon zerstört wurde
			
			
			System.out.println("You have destroyed the bed from team: " + Team);
			
			
		}
	
	}

}
