package com.blackgeckogames.server.mod;

import java.io.File;

import net.minecraft.util.Vec3;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.blackgeckogames.server.mod.commands.SkyBattleCommand;
import com.blackgeckogames.server.mod.dimension.RegisterDimensions;
import com.blackgeckogames.server.mod.dimension.WorldProviderSkyBattle;
import com.blackgeckogames.server.mod.events.BlackGeckoEventHandler;
import com.blackgeckogames.server.mod.events.BlackGeckoServerTickHandler;
import com.blackgeckogames.server.mod.gamemode.GameMode;
import com.blackgeckogames.server.mod.proxy.CommonProxy;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

@Mod(modid = BlackGeckoServer.MODID, version = BlackGeckoServer.VERSION, acceptableRemoteVersions = "*")
public class BlackGeckoServer
{
    public static final String MODID = "blackgeckoserver";
    public static final String VERSION = "1.0";
    
    @Instance(MODID)
	public static BlackGeckoServer instance;
    
    @SidedProxy(serverSide="com.blackgeckogames.server.mod.proxy.CommonProxy")
    public static CommonProxy proxy;
    
    public static final Logger logger = LogManager.getLogger("DimensionShift");
    
    public static final File baseFolder = new File("./BlackGeckoServer");
    
    
    
    
    
    
	public static int providerTypeSkyBattle = 10;    
	
	public static int firstSkyBattleServer=20;
	
	
	public static int firstFreeBuildServer=90;


	public static BiMap<Integer, GameMode> gameServer = HashBiMap.create();


	public static Vec3 lobbySpawn = new Vec3(0, 100, 0);

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	if(!baseFolder.exists()){
    		baseFolder.mkdir();
    	}
    	
		DimensionManager.registerProviderType(providerTypeSkyBattle, WorldProviderSkyBattle.class, true);

    	RegisterDimensions.register(true);

    	
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
		MinecraftForge.EVENT_BUS.register(new BlackGeckoEventHandler());
		FMLCommonHandler.instance().bus().register(new BlackGeckoEventHandler());
		
		FMLCommonHandler.instance().bus().register(new BlackGeckoServerTickHandler());
		
		
		
		
    }
    
    
    @EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
      event.registerServerCommand(new SkyBattleCommand());
      //event.registerServerCommand(new CommandHelp());
      //event.registerServerCommand(new CommandTime());
    	
    	//ForgeChunkManager.setForcedChunkLoadingCallback(this, new ChunkLoader());
    }
    
    
    @EventHandler
    public void serverStarted(FMLServerStartedEvent event){
    	RegisterDimensions.register(false);
    }
    
    
    public static GameMode getGame(int dim){
    	if(gameServer.containsKey(dim)){
    		return gameServer.get(dim);
    	} else {
    		return null;
    	}
    	
    }
    public static boolean hasGame(int dim){
    	if(gameServer.containsKey(dim)){
    		return true;
    	} else {
    		return false;
    	}
    	
    }

    
}
