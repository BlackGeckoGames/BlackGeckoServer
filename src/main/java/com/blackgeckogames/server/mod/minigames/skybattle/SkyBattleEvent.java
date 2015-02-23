package com.blackgeckogames.server.mod.minigames.skybattle;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.WorldSettings.GameType;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;

import org.apache.commons.lang3.StringUtils;

import com.blackgeckogames.server.mod.BlackGeckoServer;
import com.blackgeckogames.server.mod.player.BGSPlayer;

public class SkyBattleEvent {

	public static void onPlayerBreakEvent(BreakEvent event) {
		
		
		
		/*schoener waere:
		 * 
		 * 
		 * Block block = event.world.getBlockState(event.pos).getBlock();
		 * 
		 * if(block != Blocks.XXX || ...)
		 */
		if(event.world.getBlockState(event.pos).getBlock() != Blocks.sandstone && event.world.getBlockState(event.pos).getBlock() != Blocks.end_stone && event.world.getBlockState(event.pos).getBlock() != Blocks.obsidian  && event.world.getBlockState(event.pos).getBlock() != Blocks.beacon){
			
			/*
			 * System out wird in die Konsole geschrieben, du brauchst daher eine Chat Message.
			 * 
			 * Probier mal:
			 * 
			 * player.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD +"Welcome!"));
			 * 
			 */
			
			
			
			
			System.out.println("You are not allowed to destroy this block!");
			if(!event.getPlayer().capabilities.isCreativeMode){
				event.setCanceled(true);	
			}
			
		}
		
		else if(event.world.getBlockState(event.pos).getBlock() == Blocks.beacon){
			
			String Team = "";
			
			//Abfrage welcher TeamBeacon zerst�rt wurde
			
			
			System.out.println("You have destroyed the bed from team: " + Team);
			
			
		}
	
	}

	public static void onLivingDeath(LivingDeathEvent event) {		//EVENT IS CANCELED BECASUE WE DONT WANT THE RESAPWN SCREEN
		if(event.entity instanceof EntityPlayer){
			
			EntityPlayer player = (EntityPlayer) event.entity;
			
			
			boolean bedBroken=false;
			if(bedBroken){
				
				player.clearActivePotions();
				
				IInventory inv = player.inventory;
				for (int i = 0; i < inv.getSizeInventory(); i++) {
					if (inv.getStackInSlot(i) != null) {
						ItemStack j = inv.getStackInSlot(i);
						if (j.getItem() != null) {
							inv.setInventorySlotContents(i, null);
						}
					}
				}
				
				player.setPosition(0, 100, 0);
				player.setGameType(GameType.SPECTATOR);
				
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
			
			
			
			
			event.entity.isDead=false;
			event.entityLiving.setHealth(20f);
			event.setCanceled(true);
			
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
					
					System.out.println("triggeredTEAM");

					if(BlackGeckoServer.gameServer.get(player.dimension) instanceof SkyBattle){
						
						
						
						System.out.println("triggered2");
						if(EnumTeam.valueOf(line3.getUnformattedText())!=null){
							System.out.println("triggered3");
							
							
							((SkyBattle)BlackGeckoServer.gameServer.get(player.dimension)).joinTeam(player, EnumTeam.valueOf(line3.getUnformattedText()));
							
													
						}
					
					
					
					} else {
						player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.DARK_RED+"UUPS! Something wrent wrong, please rejoin the game."));
					}
				}
			}
			
			
		}
		
		
	}

	
}
