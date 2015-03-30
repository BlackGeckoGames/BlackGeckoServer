package com.blackgeckogames.server.mod.events;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import com.blackgeckogames.server.mod.gamemode.GameMode;
import com.blackgeckogames.server.mod.gamemode.GameMode.EnumGameState;

public class BlackGeckoServerTickHandler {
	
	public static List<GameMode> games = new ArrayList();
	
	
	
	
	@SubscribeEvent
	 public void onPlayerTick(TickEvent.PlayerTickEvent event) {

	}
	 

	 //Called when the server ticks. Usually 20 ticks a second. 
	 @SubscribeEvent
	 public void onServerTick(TickEvent.ServerTickEvent event) {
		 if(event.phase==TickEvent.Phase.START){
			List <GameMode> gamesCopy = new ArrayList<>(games);
		 
		 
			if(gamesCopy.size()>0){
				 for(int i =0; i<gamesCopy.size();i++){
					GameMode game = gamesCopy.get(i);
					game.countdown--;
					 
					
					 if(game.countdown % 20 ==0){
						 game.countdownEvent();
					 }
					 
					 if(game.countdown==0){
						 game.countdown=0;
						 game.state=EnumGameState.RUNNING;
						 game.start();
						 games.remove(game);
					 } else if(game.countdown<0){
						 if(game.state==EnumGameState.STARTING){
							 game.countdown=0;
							 game.state=EnumGameState.RUNNING;
							 game.start();
							 games.remove(game);
						 } else {
							 games.remove(game);
						 }
					 }
				}
			 
			 }
		 }

	}
	 
	 //Called when a new frame is displayed (See fps) 
	 @SubscribeEvent
	 public void onRenderTick(TickEvent.RenderTickEvent event) {

	}
	 
	 //Called when the world ticks
	 @SubscribeEvent
	 public void onWorldTick(TickEvent.WorldTickEvent event) {

	}
}
