package com.blackgeckogames.server.mod.minigames.skybattle;

import com.blackgeckogames.server.mod.data.BGSPlayer;

import net.minecraft.init.Blocks;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;

public class SkyBattleEvents {

	public static void onPlayerBreakEvent(BreakEvent event) {
		
		
		
		/*schoener waere:
		 * 
		 * 
		 * Block block = event.world.getBlockState(event.pos).getBlock();
		 * 
		 * if(block != Blocks.XXX || ...)
		 */
		if(event.world.getBlockState(event.pos).getBlock() != Blocks.sandstone && event.world.getBlockState(event.pos).getBlock() != Blocks.end_stone && event.world.getBlockState(event.pos).getBlock() != Blocks.obsidian  && event.world.getBlockState(event.pos).getBlock() != Blocks.beacon){
			
			System.out.println("You are not allowed to destroy this block!");
			event.setCanceled(true);	
			
		}
		
		else if(event.world.getBlockState(event.pos).getBlock() == Blocks.beacon){
			
			String Team = "";
			
			//Abfrage welcher TeamBeacon zerst�rt wurde
			
			
			System.out.println("You have destroyed the bed from team: " + Team);
			
			
		}
	
	}
	
	public static void onPlayerPlaceEvent(PlaceEvent event){
		
		if(event.world.getBlockState(event.pos).getBlock() != Blocks.sandstone && event.world.getBlockState(event.pos).getBlock() != Blocks.end_stone && event.world.getBlockState(event.pos).getBlock() != Blocks.obsidian  && event.world.getBlockState(event.pos).getBlock() != Blocks.beacon){
			
			System.out.println("You are not allowed to place this block!");
			event.setCanceled(true);	
			
		}
		
	}

}
