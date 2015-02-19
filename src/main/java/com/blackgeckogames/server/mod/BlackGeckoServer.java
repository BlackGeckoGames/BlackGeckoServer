package com.blackgeckogames.server.mod;

import net.minecraft.init.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import com.blackgeckogames.server.mod.events.BlackGeckoEventHandler;
import com.blackgeckogames.server.mod.proxy.CommonProxy;

@Mod(modid = BlackGeckoServer.MODID, version = BlackGeckoServer.VERSION, acceptableRemoteVersions = "*")
public class BlackGeckoServer
{
    public static final String MODID = "blackgeckoserver";
    public static final String VERSION = "1.0";
    
    @Instance(MODID)
	public static BlackGeckoServer instance;
    
    @SidedProxy(serverSide="com.blackgeckogames.server.mod.proxy.CommonProxy")
    public static CommonProxy proxy;
    
    
    @EventHandler
    public void serverLoad(FMLServerStartingEvent event)
    {
      //event.registerServerCommand(new CommandBasic());
      //event.registerServerCommand(new CommandHelp());
      //event.registerServerCommand(new CommandTime());
    }
    
    

    @EventHandler
    public void Preinit(FMLPreInitializationEvent event)
    {

    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
		// some example code
        System.out.println("DIRT BLOCK >> "+Blocks.dirt.getUnlocalizedName());
    }
    
    @EventHandler
    public void Postinit(FMLPostInitializationEvent event)
    {
		MinecraftForge.EVENT_BUS.register(new BlackGeckoEventHandler());
		FMLCommonHandler.instance().bus().register(new BlackGeckoEventHandler());
    }
}
