package com.blackgeckogames.server.mod.events;

import net.minecraft.block.state.BlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;

import com.blackgeckogames.server.mod.data.BGSPlayer;
import com.blackgeckogames.server.mod.minigames.GameMode;

public class LobbyEvent {

	public static void onPlayerBreakEvent(BreakEvent event) {
		event.setCanceled(true);
		System.out.println("triggered lobby event " + BGSPlayer.get(event.getPlayer()).getGameMode());
	}
	
	public static void onPlayerInteractEvent(net.minecraftforge.event.entity.player.PlayerInteractEvent event){
		BGSPlayer bgsPlayer = BGSPlayer.get(event.entityPlayer);
		
		if(event.world.getBlockState(event.pos).getBlock() == Blocks.wall_sign){
			TileEntity tileEntity = event.world.getTileEntity(event.pos);
			
			if(tileEntity!=null && tileEntity instanceof TileEntitySign){

				IChatComponent line1= ((TileEntitySign)tileEntity).signText[0];
				IChatComponent line2= ((TileEntitySign)tileEntity).signText[1];
				IChatComponent line3= ((TileEntitySign)tileEntity).signText[2];
				IChatComponent line4= ((TileEntitySign)tileEntity).signText[3];
								
				
				if(line1.getUnformattedText().equalsIgnoreCase("sky battle")){
					bgsPlayer.setGameMode(GameMode.SKYBATTLE);
					
				}
			}
			
			
		}
		
		
	}

}
