package com.blackgeckogames.server.mod.events;

import java.awt.Color;

import org.apache.commons.lang3.StringUtils;

import net.minecraft.block.state.BlockState;
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
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import net.minecraftforge.fml.client.config.GuiConfigEntries.ChatColorEntry;

import com.blackgeckogames.server.mod.BlackGeckoServer;
import com.blackgeckogames.server.mod.dimension.teleporter.TeleporterPosition;
import com.blackgeckogames.server.mod.gamemode.GameMode;
import com.blackgeckogames.server.mod.gamemode.GameMode.EnumGameMode;

public class LobbyEvent {

	public static void onPlayerBreakEvent(BreakEvent event) {
		if(!event.getPlayer().capabilities.isCreativeMode){
			event.setCanceled(true);
		}
	}
	
	public static void onPlayerInteractEvent(net.minecraftforge.event.entity.player.PlayerInteractEvent event) {
		if (event.world.getBlockState(event.pos).getBlock() == Blocks.wall_sign) {
			TileEntity tileEntity = event.world.getTileEntity(event.pos);

			if (tileEntity != null && tileEntity instanceof TileEntitySign) {

				IChatComponent line1 = ((TileEntitySign) tileEntity).signText[0];
				IChatComponent line2 = ((TileEntitySign) tileEntity).signText[1];
				IChatComponent line3 = ((TileEntitySign) tileEntity).signText[2];
				IChatComponent line4 = ((TileEntitySign) tileEntity).signText[3];

				if (StringUtils.containsIgnoreCase(line1.getUnformattedText(), "sky battle")) {

					String numberString = line2.getUnformattedText();
					if (numberString.length() > 0) {
						int number = 0;

						try {
							number = Integer.parseInt(numberString);
						} catch (NumberFormatException nfe) {
							// bad data - set to sentinel
						}

						boolean foundServer = false;
						if (number == 0) {

						} else {
							if (BlackGeckoServer.hasGame(BlackGeckoServer.firstSkyBattleServer + number - 1)) {

								if (DimensionManager.isDimensionRegistered(BlackGeckoServer.firstSkyBattleServer + number - 1)) {
									TeleporterPosition.teleport(event.entityPlayer, BlackGeckoServer.firstSkyBattleServer + number - 1, 0, 100, 0);
									foundServer = true;
								}

							}
						}
						if (!foundServer) {
							event.entityPlayer.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.RED + "WARNING! Failed to connect to "+EnumGameMode.FREE_BUILD + " " + number));
							BlackGeckoServer.logger.warn("WARNING! Player " + event.entityPlayer.getName() + " failed to connect to "+EnumGameMode.FREE_BUILD + " " + number + " dim: " + (BlackGeckoServer.firstSkyBattleServer + number - 1));
						}
					}


				}
				
				if (StringUtils.containsIgnoreCase(line1.getUnformattedText(), "free build")) {

					String numberString = line2.getUnformattedText();
					if (numberString.length() > 0) {
						int number = 0;

						try {
							number = Integer.parseInt(numberString);
						} catch (NumberFormatException nfe) {
							// bad data - set to sentinel
						}

						boolean foundServer = false;
						if (number == 0) {

						} else {
							//if (BlackGeckoServer.gameServer.get(BlackGeckoServer.firstFreeBuildServer + number - 1) != null) {

								if (DimensionManager.isDimensionRegistered(BlackGeckoServer.firstFreeBuildServer + number - 1)) {
									TeleporterPosition.teleport(event.entityPlayer, BlackGeckoServer.firstFreeBuildServer + number - 1, 0, 100, 0);
									foundServer = true;
								}

							//}
						}
						if (!foundServer) {
							event.entityPlayer.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.RED + "WARNING! Failed to connect to " + EnumGameMode.FREE_BUILD + " " + number));
							BlackGeckoServer.logger.warn("WARNING! Player " + event.entityPlayer.getName() + " failed to connect to " + EnumGameMode.FREE_BUILD + " " + number + " dim: " + (BlackGeckoServer.firstSkyBattleServer + number - 1));
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
