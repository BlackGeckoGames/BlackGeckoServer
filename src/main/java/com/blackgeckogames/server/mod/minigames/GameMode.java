package com.blackgeckogames.server.mod.minigames;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.DimensionManager;

import org.apache.commons.io.FileUtils;

import com.blackgeckogames.server.mod.BlackGeckoServer;



public class GameMode{
	public enum EnumGameMode{
		LOBBY ,SKY_BATTLE, PVP, HUNGERGAMES
	}
	
	
	public int dim;

	public int map;
	
	public List<EntityPlayer> players = new ArrayList<EntityPlayer>();
	
	
	public void joinPlayer(EntityPlayer player){
		players.add(player);
	}
	
	public void disconnectPlayer(EntityPlayer player){
		players.remove(player);
	}
	
	
	
	protected void resetWorld(EnumGameMode gamemode, int mapNumber, int dimNumber) {
		System.out.println(MinecraftServer.getServer().worldServerForDimension(dimNumber).getSaveHandler().getWorldDirectory().toString());
		
		DimensionManager.unloadWorld(dimNumber);
		
		File folder = new File("");
		File newFolder = new File("");
		
		switch(gamemode){
			case SKY_BATTLE:
				folder = new File(MinecraftServer.getServer().worldServerForDimension(dimNumber).getSaveHandler().getWorldDirectory()+"/BlackGeckoServer/DIMENSIONS/SKY_BATTLE/SB_"+dimNumber);
				newFolder = new File(MinecraftServer.getServer().worldServerForDimension(dimNumber).getSaveHandler().getWorldDirectory()+"/BlackGeckoServer/MAPS/SKY_BATTLE/MAP_"+mapNumber);
				break;
				
		}

		
		if(folder.exists()){
			try {
				FileUtils.deleteDirectory(folder);
				
				if(newFolder.exists() && newFolder.isDirectory() && newFolder.list().length >0 ){
				
					FileUtils.copyDirectory(newFolder, folder, new FileFilter() {
					    @Override
					    public boolean accept(File pathname) {
					        return pathname.canRead();
					    }
					});
				
				} else {
					newFolder.mkdir();
					BlackGeckoServer.logger.warn("****WARNING****");
					BlackGeckoServer.logger.warn("********************************************************************************************");
					BlackGeckoServer.logger.warn("UNABLE TO LOAD " + gamemode.toString() + " MAP " + mapNumber + ". THE FOLDER IS EITHER EMPTY OR DOES NOT EXIST.");
					BlackGeckoServer.logger.warn("********************************************************************************************");
				}
			} catch (IOException e) {
				
			}
		}
		
		
	}
}
