package com.blackgeckogames.server.mod.events;

import com.blackgeckogames.server.mod.BlackGeckoServer;
import com.blackgeckogames.server.mod.data.BGSPlayer;
import com.blackgeckogames.server.mod.minigames.skybattle.SkyBattleEvents;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BlackGeckoEventHandler {

	
	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing event){
		/*
		Be sure to check if the entity being constructed is the correct type for the extended properties you're about to add! The null check may not be necessary - I only use it to make sure properties are only registered once per entity
		*/
		if (event.entity instanceof EntityPlayer && BGSPlayer.get((EntityPlayer) event.entity) == null)
			BGSPlayer.register((EntityPlayer) event.entity);
			// That will call the constructor as well as cause the init() method
			// to be called automatically
		
	
		// If you didn't make the two convenient methods from earlier, your code would be
		// much uglier:
		if (event.entity instanceof EntityPlayer && event.entity.getExtendedProperties(BGSPlayer.EXT_PROP_NAME) == null)
			event.entity.registerExtendedProperties(BGSPlayer.EXT_PROP_NAME, new BGSPlayer((EntityPlayer) event.entity));
	}
	
	
	@SubscribeEvent
	public void onEntityJoinWorldEvent(EntityJoinWorldEvent event){
		if(event.entity instanceof EntityPlayer){
			EntityPlayer player = (EntityPlayer)event.entity;
			BGSPlayer bgsPlayer = BGSPlayer.get(player);
			NBTTagCompound playerData = BlackGeckoServer.proxy.getEntityData(((EntityPlayer) event.entity).getUniqueID().toString());
			// make sure the compound isn't null then load the data back into the player's IExtendedEntityProperties
			

			if (playerData != null){
				bgsPlayer.loadNBTData(playerData);
				System.out.println("loading data");
			}
			
			
			
			
		}
		
		
	}	
		

	
	@SubscribeEvent
	public void onLivingDeath(LivingDeathEvent event){
		// we only want to save data for players (most likely, anyway)
		
		
		
		
		if (!event.entity.worldObj.isRemote && event.entity instanceof EntityPlayer)
		{
			
			EntityPlayer player = ((EntityPlayer)event.entity);
			BGSPlayer bgsPlayer = BGSPlayer.get(player);

			
			NBTTagCompound playerData = new NBTTagCompound();
			bgsPlayer.get((EntityPlayer)event.entity).saveNBTData(playerData);
			
			
			BlackGeckoServer.proxy.storeEntityData(((EntityPlayer) event.entity).getUniqueID().toString(), playerData);
			// call our handy static one-liner to save custom data to the proxy
			//MoroPlayer.saveNBTData((EntityPlayer) event.entity);
		}
	}

	
	
	@SubscribeEvent
	public void onPlayerInteractEvent(net.minecraftforge.event.entity.player.PlayerInteractEvent event){
		
	
		EntityPlayer player = event.entityPlayer;
		BGSPlayer bgsPlayer = BGSPlayer.get(player);

		System.out.println("Your xp is: " + bgsPlayer.getExperience());
		
		bgsPlayer.setExperience(bgsPlayer.getExperience()+1);
		
		switch(bgsPlayer.getGameMode()){
			
		case LOBBY:
		default:
			LobbyEvent.onPlayerInteractEvent(event);
	
		}
		
		
		
	}
	
	@SubscribeEvent
	public void onPlayerBreakEvent(net.minecraftforge.event.world.BlockEvent.BreakEvent event){
		
		EntityPlayer player = event.getPlayer();
		BGSPlayer bgsPlayer = BGSPlayer.get(player);
		
		switch(bgsPlayer.getGameMode()){
			case SKYBATTLE:
				SkyBattleEvents.onPlayerBreakEvent(event);
				
			case LOBBY:
			default:
				LobbyEvent.onPlayerBreakEvent(event);
		
		}
		

		
	}
	

}