package com.blackgeckogames.server.mod.minigames.skybattle;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.DimensionManager;

import org.apache.commons.io.FileUtils;

import com.blackgeckogames.server.mod.BlackGeckoServer;
import com.blackgeckogames.server.mod.Utilities;
import com.blackgeckogames.server.mod.minigames.GameMode;

public class SkyBattle extends GameMode{
	
	public int teamNumber;
	
	public int playerPerTeam;
	
	public boolean isRunning;
		
	public List[] teams ={new ArrayList<EntityPlayer>(),new ArrayList<EntityPlayer>(),new ArrayList<EntityPlayer>(),new ArrayList<EntityPlayer>(),new ArrayList<EntityPlayer>(),new ArrayList<EntityPlayer>(),new ArrayList<EntityPlayer>(),new ArrayList<EntityPlayer>()};

	
	
	public SkyBattle(int i) {
		this(i,1, 2,2);
	}
	
	
	public SkyBattle(int i, int map, int teams, int playersPerTeam) {
		this.dim=i;
		this.map=map;
		this.teamNumber=teams;
		this.playerPerTeam=playersPerTeam;
		this.isRunning=false;
		
		this.resetWorld(EnumGameMode.SKY_BATTLE, this.map, this.dim);
	}
	



	@Override
	public void joinPlayer(EntityPlayer player){
		players.add(player);
		
		if(this.players.size()==this.playerPerTeam*this.teamNumber){
			for (EntityPlayer player1 : this.players) {
			    player1.addChatComponentMessage(new ChatComponentText("The game has started."));
			    this.startGame();
			}
		}
	}
	
	@Override
	public void disconnectPlayer(EntityPlayer player){
		players.remove(player);
	}
	
	public void joinTeam(EntityPlayer player, EnumTeam team){
		System.out.println("joining");
		if(team.ordinal()<this.teamNumber){
				
				if(teams[team.ordinal()].contains(player)){
					
					teams[team.ordinal()].remove(player);
					
					player.addChatComponentMessage(new ChatComponentText("You left Team " + team.toString()));
				} else {
						if(teams[team.ordinal()].size()<this.playerPerTeam){
							
							for(int i=0; i<teams.length;i++){
								if(teams[i].contains(player)){
									teams[i].remove(player);
								}
							}
							
							teams[team.ordinal()].add(player);
							
							EnumChatFormatting format=EnumChatFormatting.WHITE;
							
							switch(team){
								case BLUE:
									format=EnumChatFormatting.BLUE;
									break;
								case RED:
									format=EnumChatFormatting.RED;
									break;
								case GREEN:
									format=EnumChatFormatting.GREEN;
									break;
								case YELLOW:
									format=EnumChatFormatting.YELLOW;
									break;
								case BLACK:
									format=EnumChatFormatting.BLACK;
									break;
								case PINK:
									format=EnumChatFormatting.LIGHT_PURPLE;
									break;
								case CYAN:
									format=EnumChatFormatting.AQUA;
									break;
								case ORANGE:
									format=EnumChatFormatting.GOLD;
									break;
							}	
									
									
							player.addChatComponentMessage(new ChatComponentText(format + "You joined team " + team.toString() + "."));
							player.setCustomNameTag(format+player.getName());
							
						} else {
							player.addChatComponentMessage(new ChatComponentText("This team is already full."));
						}
								
					
					}

		} else {
			player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.DARK_RED+"The team you were about to join does not exist."));
		}
	}

	public void startGame(){
		for (EntityPlayer player1 : this.players) {
		    boolean isInTeam=false;
		    
		    for(int i=0;i<this.teams.length;i++){
		    	if(this.teams[i].contains(player1))
		    		isInTeam=true;
		    }
		}
	}
}
