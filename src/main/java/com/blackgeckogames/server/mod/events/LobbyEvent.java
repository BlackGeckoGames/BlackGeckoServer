package com.blackgeckogames.server.mod.events;

import java.awt.Color;

import net.minecraft.block.state.BlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import net.minecraftforge.fml.client.config.GuiConfigEntries.ChatColorEntry;

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
								
				
				if(line1.getUnformattedText().equalsIgnoreCase("[sky battle]")){
					bgsPlayer.setGameMode(GameMode.SKYBATTLE);
					
					if(line2.getUnformattedText().equalsIgnoreCase("BlackGecko")){
						
						if(line3.getUnformattedText().equalsIgnoreCase("==========")){
							
							if(line4.getUnformattedText().equalsIgnoreCase("SkyWars")){
								
								/*
								 *  TP spieler in Lobby für Map SkyWars
								 *  Save Inventory für später --> Clear Inventory
								 *  Save XP --> Set XP 0 --> Set XP auf 60 für Timer
								 * 	Give Admins, Semi Admins etc... Emerald für QuickStart
								 *  Give Player Nethercube für Leaven
								 *  Give Uhr für Achievments
								 *  Give Beacon für Teams
								 *  
								 */
								
							}
							
						}
						
					}
					
				}
				
				
				
				
				
				
			}
			
			
		}
		
		
	}
	
	public static void onPlayerPlaceEvent(PlaceEvent event){
		
		if(event.world.getBlockState(event.pos).getBlock() == Blocks.wall_sign){	
			
			TileEntity tileEntity = event.world.getTileEntity(event.pos);
			
			if(tileEntity!=null && tileEntity instanceof TileEntitySign){
				
				IChatComponent line1= ((TileEntitySign)tileEntity).signText[0];
				IChatComponent line2= ((TileEntitySign)tileEntity).signText[1];
				IChatComponent line3= ((TileEntitySign)tileEntity).signText[2];
				IChatComponent line4= ((TileEntitySign)tileEntity).signText[3];
				
				if(line1.getUnformattedText().equalsIgnoreCase("[skybattle]")){
					
					
					if(line2.getUnformattedText().equalsIgnoreCase("SkyWars") || line2.getUnformattedText().equalsIgnoreCase("MapName")){
						
						//Set Text vom Schild
						line1 = IChatComponent.Serializer.jsonToComponent(Color.BLUE + "[SkyBattle]");
						line2 = IChatComponent.Serializer.jsonToComponent(Color.BLACK + "BlackGecko");
						line3 = IChatComponent.Serializer.jsonToComponent(Color.WHITE + "==========");
						line4 = IChatComponent.Serializer.jsonToComponent(Color.GREEN + "SkyWars");
					
					}
				
				
				}
				
			}
			
		}
		
	}
	

}
