package com.blackgeckogames.server.mod.dimension;

import net.minecraftforge.common.DimensionManager;

import com.blackgeckogames.server.mod.BlackGeckoServer;
import com.blackgeckogames.server.mod.minigames.skybattle.SkyBattle;

public class RegisterDimensions {
	
	public static void register(boolean preInit){
		
		//SKYBATTLE
		for(int i=BlackGeckoServer.firstSkyBattleServer;i<25;i++){
			if(preInit){
				DimensionManager.registerDimension(i, BlackGeckoServer.providerTypeSkyBattle);
			} else {
				BlackGeckoServer.gameServer.put(i, new SkyBattle(i));
			}
		}
		
		
		
		
		
	}

}
