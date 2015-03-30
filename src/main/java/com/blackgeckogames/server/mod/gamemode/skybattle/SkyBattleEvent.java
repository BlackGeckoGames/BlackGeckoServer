package com.blackgeckogames.server.mod.gamemode.skybattle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldSettings.GameType;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;

import org.apache.commons.lang3.StringUtils;

import com.blackgeckogames.server.mod.BlackGeckoServer;
import com.blackgeckogames.server.mod.dimension.teleporter.TeleporterPosition;
import com.blackgeckogames.server.mod.gamemode.GameMode;
import com.blackgeckogames.server.mod.player.BGSPlayer;

public class SkyBattleEvent {
	
	public static final List destroyableBlocks = new ArrayList(Arrays.asList(Blocks.sandstone, Blocks.ladder, Blocks.chest));
	
	public static void onPlayerBreakEvent(BreakEvent event) {
		if(BlackGeckoServer.hasGame(event.world.provider.getDimensionId()) && BlackGeckoServer.getGame(event.world.provider.getDimensionId()).players.contains(event.getPlayer())){
			GameMode game = BlackGeckoServer.getGame(event.world.provider.getDimensionId());
	
			if(!event.getPlayer().capabilities.isCreativeMode){
				if(!destroyableBlocks.contains(event.world.getBlockState(event.pos))){
					event.setCanceled(true);	
						game.chatPlayer(event.getPlayer(), EnumChatFormatting.RED+"You can't break this block.");
				
				}
				
			}
			
			if(event.world.getBlockState(event.pos).getBlock() == Blocks.beacon){
				if(game instanceof SkyBattle){
					if(((SkyBattle)game).beaconPos.contains(new Vec3(event.pos.getX(), event.pos.getY(), event.pos.getZ()))){
						int i=((SkyBattle)game).beaconPos.indexOf(new Vec3(event.pos.getX(), event.pos.getY(), event.pos.getZ()));
						((SkyBattle)game).teamBeacon[i]=false;
						
						
						game.chatAllPlayers(EnumChatFormatting.RED+"The beacon of team " + EnumTeam.values()[i].getChatColor() + EnumTeam.values()[i].toString() + EnumChatFormatting.RED+" has been destroyed by " + event.getPlayer().getName() + EnumChatFormatting.RED +".");
					}
				}
				
			}
		} else {
			TeleporterPosition.teleport(event.getPlayer(), 0, BlackGeckoServer.lobbySpawn);
			event.getPlayer().addChatComponentMessage(new ChatComponentText(EnumChatFormatting.RED+"Uups?"));
		}
	
	}

	public static void onLivingDeath(LivingDeathEvent event) {		//EVENT IS CANCELED BECASUE WE DONT WANT THE RESAPWN SCREEN
		if(event.entity instanceof EntityPlayer){
			
			EntityPlayer player = (EntityPlayer)event.entity;
			if(BlackGeckoServer.hasGame(event.entity.worldObj.provider.getDimensionId()) && BlackGeckoServer.getGame(event.entity.worldObj.provider.getDimensionId()).players.contains(player)){
				
				SkyBattle game = (SkyBattle)BlackGeckoServer.getGame(event.entity.worldObj.provider.getDimensionId());
				
				if(game.players.contains(player)){
					
					
					if(event.entityLiving.getCombatTracker().func_94550_c() instanceof EntityPlayer){
						game.chatAllPlayers(EnumChatFormatting.RED +"Player " +player.getName() + EnumChatFormatting.RED+ " was killed by " +event.entityLiving.getCombatTracker().func_94550_c() + EnumChatFormatting.RED+ ".");
					} else {
						game.chatAllPlayers(EnumChatFormatting.RED +"Player " +player.getName() + EnumChatFormatting.RED+ " died.");
					}
	
					
					
					boolean bedBroken=false;
					if(bedBroken){
						
					    for(int i=0;i<game.teamNumber;i++){
					    	if(game.teams[i].contains(player)){
					    		if(game.teams[i].size()<=1){
					    			game.chatAllPlayers(EnumChatFormatting.RED + "Team " + EnumTeam.values()[i].getChatColor() + EnumTeam.values().toString() + EnumChatFormatting.RED +" was annihilated.");
					    		}
					    	}
					    }
						
						game.makeSpectator(player);
	
						
					} else {
						player.clearActivePotions();
						player.setPosition(0, 100, 0);
						IInventory inv = player.inventory;
						for (int i = 0; i < inv.getSizeInventory(); i++) {
							if (inv.getStackInSlot(i) != null) {
								ItemStack j = inv.getStackInSlot(i);
								if (j.getItem() != null) {
									inv.setInventorySlotContents(i, null);
								}
							}
						}
						
		
					
					}
				} else {
					player.setPositionAndUpdate(game.spectatorSpawnPos.xCoord, game.spectatorSpawnPos.yCoord, game.spectatorSpawnPos.zCoord);
				}

				
				event.entity.isDead=false;
				event.entityLiving.setHealth(20f);
				event.setCanceled(true);
			}
		}
		
	}

	public static void onCommandEvent(CommandEvent event) {
		if(event.command.getName().contains("help")){
			event.setCanceled(true);
			event.sender.addChatMessage(new ChatComponentText(EnumChatFormatting.BLUE +"Welcome to Sky Battle!"));
			event.sender.addChatMessage(new ChatComponentText(EnumChatFormatting.WHITE +"Destroy the enmies beacon to win."));
		}
		
	}

	public static void onPlayerInteractEvent(PlayerInteractEvent event) {
		EntityPlayer player = event.entityPlayer;
		BGSPlayer bgsPlayer = BGSPlayer.get(event.entityPlayer);
		
		
		if(event.world.getBlockState(event.pos).getBlock() == Blocks.wall_sign){
			TileEntity tileEntity = event.world.getTileEntity(event.pos);
			System.out.println("triggered1");

			if(tileEntity!=null && tileEntity instanceof TileEntitySign){
				System.out.println("triggeredte");

				IChatComponent line1= ((TileEntitySign)tileEntity).signText[0];
				IChatComponent line2= ((TileEntitySign)tileEntity).signText[1];
				IChatComponent line3= ((TileEntitySign)tileEntity).signText[2];
				IChatComponent line4= ((TileEntitySign)tileEntity).signText[3];
								
				
				if(StringUtils.containsIgnoreCase(line1.getUnformattedText(), "team")){
					
					if(BlackGeckoServer.getGame(player.dimension) instanceof SkyBattle){
						
						
						
						System.out.println("triggered2");
						if(EnumTeam.valueOf(line3.getUnformattedText())!=null){
							System.out.println("triggered3");
							
							
							((SkyBattle)BlackGeckoServer.getGame(player.dimension)).joinTeam(player, EnumTeam.valueOf(line3.getUnformattedText()));
							
													
						}
					
					
					
					} else {
						player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.DARK_RED+"UUPS! Something wrent wrong, please rejoin the game."));
					}
				}
			}
			
			
		}
		
		
	}

	public static void onPlayerPlaceEvent(PlaceEvent event) {
		if(!event.player.capabilities.isCreativeMode){
			
			
			
		}
		
	}

	
}
