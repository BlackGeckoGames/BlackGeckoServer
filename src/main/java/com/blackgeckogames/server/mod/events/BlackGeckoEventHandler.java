package com.blackgeckogames.server.mod.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;

import com.blackgeckogames.server.mod.BlackGeckoServer;
import com.blackgeckogames.server.mod.dimension.teleporter.TeleporterPosition;
import com.blackgeckogames.server.mod.gamemode.Lobby;
import com.blackgeckogames.server.mod.gamemode.skybattle.SkyBattle;
import com.blackgeckogames.server.mod.gamemode.skybattle.SkyBattleEvent;
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

			if(BlackGeckoServer.hasGame(event.world.provider.getDimensionId())){
				BlackGeckoServer.getGame(event.world.provider.getDimensionId()).joinPlayer(player);
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
			
			
			

			if(BlackGeckoServer.hasGame(event.entity.worldObj.provider.getDimensionId())){
				if(BlackGeckoServer.getGame(event.entity.worldObj.provider.getDimensionId()) instanceof SkyBattle){
					SkyBattleEvent.onLivingDeath(event);
				} else if(BlackGeckoServer.getGame(event.entity.worldObj.provider.getDimensionId()) instanceof Lobby){
					LobbyEvent.onLivingDeath(event);
				}

			}

		
		
		}
	}

	@SubscribeEvent
	public void onServerChatEvent(ServerChatEvent event){
		if(BlackGeckoServer.hasGame(event.player.worldObj.provider.getDimensionId())){
			if(BlackGeckoServer.getGame(event.player.worldObj.provider.getDimensionId()) instanceof SkyBattle){

			} else if(BlackGeckoServer.getGame(event.player.worldObj.provider.getDimensionId()) instanceof Lobby){
				LobbyEvent.onServerChatEvent(event);
			}

		}
	}
	
	@SubscribeEvent
	public void onCommandEvent(CommandEvent event){
		if(event.sender instanceof EntityPlayer){
			EntityPlayer player = (EntityPlayer)event.sender;
			if(BlackGeckoServer.hasGame(player.worldObj.provider.getDimensionId())){
				if(BlackGeckoServer.getGame(player.worldObj.provider.getDimensionId()) instanceof SkyBattle){
					SkyBattleEvent.onCommandEvent(event);
				} else if(BlackGeckoServer.getGame(player.worldObj.provider.getDimensionId()) instanceof Lobby){
					LobbyEvent.onCommandEvent(event);
				}

			}
	}
	}
	
	
	@SubscribeEvent
	public void onPlayerInteractEvent(net.minecraftforge.event.entity.player.PlayerInteractEvent event){
	
		EntityPlayer player = event.entityPlayer;
		if(BlackGeckoServer.hasGame(player.worldObj.provider.getDimensionId())){
			if(BlackGeckoServer.getGame(player.worldObj.provider.getDimensionId()) instanceof SkyBattle){
				SkyBattleEvent.onPlayerInteractEvent(event);
			} else if(BlackGeckoServer.getGame(player.worldObj.provider.getDimensionId()) instanceof Lobby){
				LobbyEvent.onPlayerInteractEvent(event);
			}
		
		}
		
		
		
	}
	
	@SubscribeEvent
	public void onPlayerBreakEvent(net.minecraftforge.event.world.BlockEvent.BreakEvent event){
		
		EntityPlayer player = event.getPlayer();

		if(BlackGeckoServer.hasGame(player.worldObj.provider.getDimensionId())){
			if(BlackGeckoServer.getGame(player.worldObj.provider.getDimensionId()) instanceof SkyBattle){
				SkyBattleEvent.onPlayerBreakEvent(event);
			} else if(BlackGeckoServer.getGame(player.worldObj.provider.getDimensionId()) instanceof Lobby){
				LobbyEvent.onPlayerBreakEvent(event);
			}

		}
		

		
	}
	
	@SubscribeEvent
	public void onPlayerPlaceEvent(net.minecraftforge.event.world.BlockEvent.PlaceEvent event){
		
		EntityPlayer player = event.player;

		if(BlackGeckoServer.hasGame(player.worldObj.provider.getDimensionId())){
			if(BlackGeckoServer.getGame(player.worldObj.provider.getDimensionId()) instanceof SkyBattle){
				SkyBattleEvent.onPlayerPlaceEvent(event);
			} else if(BlackGeckoServer.getGame(player.worldObj.provider.getDimensionId()) instanceof Lobby){
				LobbyEvent.onPlayerPlaceEvent(event);
			}

		
		}	
	}
	
	@SubscribeEvent
	public void onPlayerChangedDimensionEvent(PlayerChangedDimensionEvent event){
		if(BlackGeckoServer.hasGame(event.fromDim)){
			BlackGeckoServer.getGame(event.fromDim).disconnectPlayer(event.player);
		}
	}
	

}
