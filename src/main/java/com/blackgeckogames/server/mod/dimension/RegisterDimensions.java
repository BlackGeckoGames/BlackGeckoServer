package com.blackgeckogames.server.mod.dimension;

import net.minecraftforge.common.DimensionManager;

import com.blackgeckogames.server.mod.BlackGeckoServer;
import com.blackgeckogames.server.mod.gamemode.Lobby;
import com.blackgeckogames.server.mod.gamemode.skybattle.SkyBattle;

public class RegisterDimensions {
	
	public static void register(boolean preInit){
		
		if(!preInit){
			BlackGeckoServer.gameServer.put(0, new Lobby());
		}
		
		
		//SKYBATTLE
		for(int i=BlackGeckoServer.firstSkyBattleServer;i<BlackGeckoServer.firstSkyBattleServer+5;i++){
			if(preInit){
				DimensionManager.registerDimension(i, BlackGeckoServer.providerTypeSkyBattle);
			} else {
				BlackGeckoServer.gameServer.put(i, new SkyBattle(i,1));
			}
		}
		
		//FREEBUILD
		for(int i=BlackGeckoServer.firstFreeBuildServer;i<BlackGeckoServer.firstFreeBuildServer+5;i++){
			if(preInit){
				DimensionManager.registerDimension(i, BlackGeckoServer.providerTypeSkyBattle);
			} else {
				//BlackGeckoServer.gameServer.put(i, new SkyBattle(i));
			}
		}
		
		
		
		
		
	}

}
