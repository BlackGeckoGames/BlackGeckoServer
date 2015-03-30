package com.blackgeckogames.server.mod.gamemode;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.DimensionManager;

import org.apache.commons.io.FileUtils;

import com.blackgeckogames.server.mod.BlackGeckoServer;
import com.blackgeckogames.server.mod.events.BlackGeckoServerTickHandler;
import com.blackgeckogames.server.mod.utilities.PlaySound;



public class GameMode{
	public enum EnumGameMode{
		LOBBY ,SKY_BATTLE, FREE_BUILD, PVP, HUNGERGAMES
	}
	
	public enum EnumGameState{
		RESTARTING ,WAITING_FOR_PLAYER, STARTING, RUNNING
	}
	
	public int dim;
	
	private boolean createMode=false;
	
	public boolean isCreateMode() {
		return createMode;
	}

	public void setCreateMode(boolean createMode) {
		this.createMode = createMode;
	}

	public int countdown;
	
	public Scoreboard scoreboard;
	
	public int countdownMax = 20*60;	//one minute in ticks	

	public EnumGameState state;
	
	public int map;
	
	public List<EntityPlayer> players = new ArrayList<EntityPlayer>();
	
	public List<EntityPlayer> spectators = new ArrayList<EntityPlayer>();
	
	
	
	
	public void joinPlayer(EntityPlayer player){
		boolean alreadyOnline=false;
		
		for(EntityPlayer player1 : players){
			if(player1 == player)
				alreadyOnline=true;
		}
		if(!alreadyOnline){
		players.add(player);
		}
	}
	
	public void disconnectPlayer(EntityPlayer player){
		players.remove(player);
	}
	
	public void start() {
		
	}

	public void stop(){};
	
	public void countdown(){
		this.state=EnumGameState.STARTING;
		this.countdown=countdownMax;
		BlackGeckoServerTickHandler.games.add(this);
	}
	
	protected void resetWorld(EnumGameMode gamemode) {
		
		//DimensionManager.unloadWorld(this.dim);

		File folder = new File(MinecraftServer.getServer().worldServerForDimension(this.dim).getSaveHandler().getWorldDirectory()+"/BGS/DIMENSIONS/"+gamemode.toString()+"/WORLD_"+this.dim);
		File mapFolder = new File(BlackGeckoServer.baseFolder+"/MAPS/"+gamemode.toString()+"/MAP_"+this.map);

		if(folder.exists()){
			try {
				FileUtils.deleteDirectory(folder);
				
				if(mapFolder.exists() && mapFolder.isDirectory() && mapFolder.list().length >0 ){
				
					FileUtils.copyDirectory(mapFolder, folder, new FileFilter() {
					    @Override
					    public boolean accept(File pathname) {
					        return pathname.canRead();
					    }
					});
				
				} else {
					mapFolder.mkdir();
					BlackGeckoServer.logger.warn("*****************************!!!WARNING!!!***************************************");
					BlackGeckoServer.logger.warn("*********************************************************************************");
					BlackGeckoServer.logger.warn("UNABLE TO LOAD " + gamemode.toString() + " MAP " + this.map + ". THE FOLDER IS EITHER EMPTY OR DOES NOT EXIST.");
					BlackGeckoServer.logger.warn("*********************************************************************************");
				}
			} catch (IOException e) {
				
			}
		}
		DimensionManager.setWorld(this.dim, DimensionManager.getWorld(this.dim));

		this.readFromJson();
		this.state=EnumGameState.WAITING_FOR_PLAYER;
		
	}

	public void makeSpectator(EntityPlayer player){
		
	}
	
	public void readFromJson(){
		
	}
	
	public void writeToJson(){
		
	}
	
	
	public void countdownEvent(){
		setAllPlayerXP(this.countdown/20);
		setAllPlayerXPBarPercent((int)((double)100/(double)this.countdownMax*(double)this.countdown));
		if(countdown/20<=5 && this.countdown/20 >0){
			soundToAllPlayers("note.harp", 18F, 1F);
		} else if(this.countdown/20==0){
			soundToAllPlayers("note.pling", 20F, 2F);
		}
	}
	
	public void messageToAllPlayers(IChatComponent message){
		for (EntityPlayer player : this.players) {
			player.addChatMessage(message);
		}
	}
	
	public void soundToAllPlayers(String soundName, double x, double y, double z, float volume, float pitch){
		for (EntityPlayer player : this.players) {
			if(player!=null){
				PlaySound.play(player, soundName, x, y, z, volume, pitch);
			}
		}
	}
	
	public void soundToAllPlayers(String soundName, float volume, float pitch){
		for (EntityPlayer player : this.players) {
			if(player!=null){
				PlaySound.play(player, soundName, volume, pitch);
			}
		}
	}
	
	public void setAllPlayerXP(int level){
		for (EntityPlayer player : this.players) {
			player.addExperienceLevel(level-player.experienceLevel);
		}
	}
	
	public void setAllPlayerXPBarPercent(int percent){
		for (EntityPlayer player : this.players) {
			float xp = (float)player.xpBarCap() / (float)100 * (float)percent;
			
			player.experience=(float)xp/(float)player.xpBarCap();
		}
	}
	
	public void chatPlayer(EntityPlayer player, String msg){
		player.addChatComponentMessage(new ChatComponentText(msg));
	}
	
	public void chatAllPlayers(String msg){
		
		for(EntityPlayer player : this.players){
			player.addChatComponentMessage(new ChatComponentText(msg));

		}
		for(EntityPlayer player : this.spectators){
			player.addChatComponentMessage(new ChatComponentText(msg));
		}
	}
}
