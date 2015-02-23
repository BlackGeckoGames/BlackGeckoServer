package com.blackgeckogames.server.mod.events;

import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;

import com.blackgeckogames.server.mod.BlackGeckoServer;
import com.blackgeckogames.server.mod.dimension.teleporter.TeleporterPosition;
import com.blackgeckogames.server.mod.minigames.GameMode;
import com.blackgeckogames.server.mod.minigames.GameMode.EnumGameMode;
import com.blackgeckogames.server.mod.player.BGSPlayer;

public class LobbyEvent {

	public static void onPlayerBreakEvent(BreakEvent event) {
		if(!event.getPlayer().capabilities.isCreativeMode){
			event.setCanceled(true);
		}
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
								
				
				if(line1.getUnformattedText().contains("sky battle")){		            
					
		            		            
		            String numberString= line1.getUnformattedText().replace("sky battle ", ""); //removes "sky battle " from first line, leaving only the dim id
		            if(numberString.length()>0){
			            
		            	int number=0;
		            	
		            	try
		            	{
		            		number = Integer.parseInt(numberString);
		            	}
		            	catch (NumberFormatException nfe)
		            	{
		            	   // bad data - set to sentinel
		            	}
		            	
	            		boolean foundServer = false;
		            	if(BlackGeckoServer.gameServer.get(BlackGeckoServer.firstSkyBattleServer+number-1)!= null){
		            		
				            if(DimensionManager.isDimensionRegistered(BlackGeckoServer.firstSkyBattleServer+number-1)){
					            TeleporterPosition.teleport(event.entityPlayer, BlackGeckoServer.firstSkyBattleServer+number-1, 0, 100, 0);
								bgsPlayer.setGameMode(EnumGameMode.SKY_BATTLE);
					            foundServer=true;
				            }
		     

		            	}
		            	
	            		if(!foundServer){
	            			event.entityPlayer.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.RED + "WARNING! Failed to connect to SKYBATTLE" + number));
	            			BlackGeckoServer.logger.warn("WARNING! Player " + event.entityPlayer.getName()+" failed to connect to SKYBATTLE" + number + " dim: " +(BlackGeckoServer.firstSkyBattleServer+number-1));
	            		}
		            }
				}
			}
			
			
		}
		
		
	}

	public static void onLivingDeath(LivingDeathEvent event) {
		event.entity.isDead=false;
		event.entityLiving.setHealth(20f);
		event.setCanceled(true);
		
	}

	public static void onServerChatEvent(ServerChatEvent event) {
			event.setCanceled(true);

		
	}

	public static void onCommandEvent(CommandEvent event) {
		if(event.command.getName().contains("help")){
			event.setCanceled(true);
			event.sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD +"Welcome to the BlackGeckoServer!"));
			event.sender.addChatMessage(new ChatComponentText(EnumChatFormatting.WHITE +"Click on a sign to join a minigame."));
		}
		
	}

}
