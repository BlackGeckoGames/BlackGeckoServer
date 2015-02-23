package com.blackgeckogames.server.mod.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;

import com.blackgeckogames.server.mod.BlackGeckoServer;
import com.blackgeckogames.server.mod.dimension.teleporter.TeleporterPosition;
import com.blackgeckogames.server.mod.minigames.skybattle.SkyBattleEvent;
import com.blackgeckogames.server.mod.player.BGSPlayer;

public class BlackGeckoEventHandler {
    
	@SubscribeEvent
	public void onPlayerLoggedOutEvent(PlayerLoggedOutEvent event){
    	TeleporterPosition.teleport(event.player, 0 ,0,100,0);
    }
	
	
	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing event){
		
		//Be sure to check if the entity being constructed is the correct type for the extended properties you're about to add! The null check may not be necessary - I only use it to make sure properties are only registered once per entity
		
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
			bgsPlayer.saveNBTData(playerData);
			
			
			BlackGeckoServer.proxy.storeEntityData(((EntityPlayer) event.entity).getUniqueID().toString(), playerData);
			// call our handy static one-liner to save custom data to the proxy
			//MoroPlayer.saveNBTData((EntityPlayer) event.entity);
			
			
			
			switch(bgsPlayer.getGameMode()){
			case SKY_BATTLE:
				SkyBattleEvent.onLivingDeath(event);
				break;
			case LOBBY:
			default:
				LobbyEvent.onLivingDeath(event);
				break;
		
		}
		}
	}

	@SubscribeEvent
	public void onServerChatEvent(ServerChatEvent event){
		EntityPlayer player = event.player;
		BGSPlayer bgsPlayer = BGSPlayer.get(player);


		
		switch(bgsPlayer.getGameMode()){
			case LOBBY:
				LobbyEvent.onServerChatEvent(event);
				break;
		
		}
	}
	
	@SubscribeEvent
	public void onCommandEvent(CommandEvent event){
		if(event.sender instanceof EntityPlayer){
			EntityPlayer player = (EntityPlayer) event.sender;
			BGSPlayer bgsPlayer = BGSPlayer.get(player);
	
			
			switch(bgsPlayer.getGameMode()){
				case SKY_BATTLE:
					SkyBattleEvent.onCommandEvent(event);
					break;
				case LOBBY:
				default:
					LobbyEvent.onCommandEvent(event);
					break;
			}
	
	}
	}
	
	
	@SubscribeEvent
	public void onPlayerInteractEvent(net.minecraftforge.event.entity.player.PlayerInteractEvent event){
	
		EntityPlayer player = event.entityPlayer;
		BGSPlayer bgsPlayer = BGSPlayer.get(player);


		
		switch(bgsPlayer.getGameMode()){
			case SKY_BATTLE:
				SkyBattleEvent.onPlayerInteractEvent(event);
				break;
			case LOBBY:
				LobbyEvent.onPlayerInteractEvent(event);
				break;
		
		}
		
		
		
	}
	
	@SubscribeEvent
	public void onPlayerBreakEvent(net.minecraftforge.event.world.BlockEvent.BreakEvent event){
		
		EntityPlayer player = event.getPlayer();
		BGSPlayer bgsPlayer = BGSPlayer.get(player);
		
		switch(bgsPlayer.getGameMode()){
			case SKY_BATTLE:
				SkyBattleEvent.onPlayerBreakEvent(event);
				break;
			case LOBBY:
			default:
				LobbyEvent.onPlayerBreakEvent(event);
				break;
		
		}
		

		
	}
	

}