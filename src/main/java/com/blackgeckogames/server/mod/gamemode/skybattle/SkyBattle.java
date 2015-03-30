package com.blackgeckogames.server.mod.gamemode.skybattle;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldSettings.GameType;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.blackgeckogames.server.mod.BlackGeckoServer;
import com.blackgeckogames.server.mod.gamemode.GameMode;
import com.blackgeckogames.server.mod.utilities.DisplayTitle;
import com.blackgeckogames.server.mod.utilities.Vec3String;

public class SkyBattle extends GameMode{
	
	public int teamNumber;
	
	public String mapName;
	
	public int teamSize;
	public ArrayList[] teams ={new ArrayList<EntityPlayer>(),new ArrayList<EntityPlayer>(),new ArrayList<EntityPlayer>(),new ArrayList<EntityPlayer>(),new ArrayList<EntityPlayer>(),new ArrayList<EntityPlayer>(),new ArrayList<EntityPlayer>(),new ArrayList<EntityPlayer>()};
	public List<Vec3> teamSpawnPos = new ArrayList<Vec3>();
	public boolean[] teamBeacon = {false, false, false, false, false, false, false, false};
	
	public List<Vec3> ironSpawnPos = new ArrayList<Vec3>();
	public List<Vec3> goldSpawnPos = new ArrayList<Vec3>();
	public List<Vec3> diamondSpawnPos = new ArrayList<Vec3>();
	public List<Vec3> beaconPos = new ArrayList<Vec3>();

	
	
	public Vec3 lobbySpawnPos;
	public Vec3 spectatorSpawnPos;

	
	
	
	public SkyBattle(int i) {
		this.dim=i;
	}
	
	public SkyBattle(int dim, int map) {
		this.dim=dim;
		this.map=map;
		
		this.state=EnumGameState.RESTARTING;
		
		this.resetWorld(EnumGameMode.SKY_BATTLE);

		this.scoreboard= new Scoreboard();
		
		this.createScoreboard();
	}
	

	@Override
	public void joinPlayer(EntityPlayer player){
		boolean alreadyOnline=false;
		
		
		for(EntityPlayer player1 : this.players){
			if(player1 == player)
				alreadyOnline=true;
		}
		if(!alreadyOnline){			
			if(this.players.size()<this.teamNumber*this.teamSize && (this.state==EnumGameState.STARTING || this.state==EnumGameState.WAITING_FOR_PLAYER) ){
				this.players.add(player);
			} else {
				this.spectators.add(player);
				this.makeSpectator(player);
			}
			
			soundToAllPlayers("ambient.weather.thunder", 10F, 1F);

		}
		
		if(this.players.size()>=this.teamSize*this.teamNumber){
			chatAllPlayers("The countdown has started.");
		}
	}
	
	@Override
	public void disconnectPlayer(EntityPlayer player){
		if(this.players.contains(player)){
			players.remove(player);
		    this.scoreboard.removePlayerFromTeam(player.getName(), scoreboard.getPlayersTeam(player.getName()));

		    if(this.state==EnumGameState.STARTING || this.state==EnumGameState.WAITING_FOR_PLAYER){
		    	if(this.players.size()<this.teamNumber*this.teamSize && this.spectators.size()>0){
		    		this.players.add(this.spectators.get(0));
		    		chatPlayer(this.spectators.get(0), "You are now a player!");
		    		this.spectators.remove(0);
		    	}
		    }
		} else if(this.spectators.contains(player)){
			players.remove(player);
		}
		
	    for(int i=0;i<this.teams.length;i++){
	    	if(this.teams[i].contains(player))
	    		this.teams[i].remove(player);
	    }


	    
	    
   
	}

	public void joinTeam(EntityPlayer player, EnumTeam team){
		if(team.ordinal()<this.teamNumber){
				
				if(teams[team.ordinal()].contains(player)){
					
					teams[team.ordinal()].remove(player);
					chatPlayer(player, "You left team " + team.getChatColor() +team.toString());
				} else {
						if(teams[team.ordinal()].size()<this.teamSize){
							
							for(int i=0; i<teams.length;i++){
								if(teams[i].contains(player)){
									teams[i].remove(player);
								}
							}
							
							teams[team.ordinal()].add(player);	
									
							chatPlayer(player, "You joined team " + team.getChatColor() + team.toString());
							this.scoreboard.addPlayerToTeam(player.getName(), team.toString());
							
						} else {
							chatPlayer(player, "This team is already full.");
						}
								
					
					}

		} else {
			chatPlayer(player, EnumChatFormatting.DARK_RED+"The team you were about to join does not exist. Please contact a mod.");
		}
	}

	public void autojoinTeam(){
		for (EntityPlayer player : this.players) {
		    boolean isInTeam=false;
		    
		    for(int i=0;i<this.teams.length;i++){
		    	if(this.teams[i].contains(player))
		    		isInTeam=true;
		    }
		    
		    if(!isInTeam){
			    for(int i=0;i<this.teamNumber;i++){
			    	if(this.teams[i].size()<this.teamSize){
			    		this.teams[i].add(player);
			    		
			    		
			    		
						chatPlayer(player, "You joined team " + EnumTeam.values()[i].getChatColor() + EnumTeam.values()[i].toString());
						
						
						this.scoreboard.addPlayerToTeam(player.getName(), EnumTeam.values()[i].toString());
			    		
			    		
			    		break;
			    	}
			    }
		    }
		
		
		
		}
	}

	@Override
	public void start(){
		this.autojoinTeam();
		
		for(EntityPlayer player : players){
			DisplayTitle.sendTitle(player, S45PacketTitle.Type.TITLE, new ChatComponentText("SKY BATTLE"));
		}
		
		chatAllPlayers("The game has started.");
		
		for(int i=0; i<this.teamNumber;i++){
			this.teamBeacon[i]=true;
			for(int p=0;p<this.teams[i].size(); p++){
				EntityPlayer player = (EntityPlayer) teams[i].get(p);
				player.setPositionAndUpdate(this.teamSpawnPos.get(i).xCoord,this.teamSpawnPos.get(i).yCoord,this.teamSpawnPos.get(i).zCoord);
				DisplayTitle.sendTitle(player, S45PacketTitle.Type.SUBTITLE, new ChatComponentText(EnumTeam.values()[i].getChatColor() +"YOU ARE IN TEAM "+EnumTeam.values()[i].toString()));
			}
		}
		
		
	}

	public void makeSpectator(EntityPlayer player){
		if(this.players.contains(player)){
			this.players.remove(player);
		}
	    for(int i=0;i<this.teams.length;i++){
	    	if(this.teams[i].contains(player))
	    		this.teams[i].remove(player);
	    }
	    this.scoreboard.removePlayerFromTeams(player.getName());
		player.clearActivePotions();
		
		IInventory inv = player.inventory;
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			if (inv.getStackInSlot(i) != null) {
				ItemStack j = inv.getStackInSlot(i);
				if (j.getItem() != null) {
					inv.setInventorySlotContents(i, null);
				}
			}
		}
		player.setGameType(GameType.SPECTATOR);
		player.setPositionAndUpdate(this.spectatorSpawnPos.xCoord, this.spectatorSpawnPos.yCoord, this.spectatorSpawnPos.zCoord);
	}
	
	public void createScoreboard(){		
		for(int i=0; i<teamNumber;i++){
			//if(this.scoreboard.getTeamNames().contains(EnumTeam.values()[i].toString())){
				this.scoreboard.createTeam(EnumTeam.values()[i].toString());
			//}
			//ScorePlayerTeam team = new ScorePlayerTeam(scoreboard,i+"");
			ScorePlayerTeam team = this.scoreboard.getTeam(EnumTeam.values()[i].toString());
			team.setTeamName(EnumTeam.values()[i].toString());
			//team.func_178774_a(EnumTeam.values()[i].getChatColor());
            team.func_178774_a(EnumTeam.values()[i].getChatColor());
            team.setNamePrefix(EnumTeam.values()[i].getChatColor().toString());
            team.setNameSuffix(EnumChatFormatting.RESET.toString());
			
			
			
			//team.setNamePrefix("TEST");
            this.scoreboard.sendTeamUpdate(team);
		}
	}

	@Override
	public void readFromJson() {
		File folder = new File(MinecraftServer.getServer().worldServerForDimension(this.dim).getSaveHandler().getWorldDirectory()+"/BGS/DIMENSIONS/"+EnumGameMode.SKY_BATTLE.toString()+"/WORLD_"+this.dim);

		//File folder = new File(BlackGeckoServer.baseFolder + "/MAPS/" + EnumGameMode.SKY_BATTLE.toString() + "/MAP_" + map);
		JSONParser parser = new JSONParser();
		
		try {

			Object obj = parser.parse(new FileReader(folder +"/BGSData.map"));

			JSONObject jsonObject = (JSONObject) obj;

			
			this.mapName = (String) jsonObject.get("name");
			Long teamSizeLong =(long) jsonObject.get("playerPerTeam");
			
			this.teamSize = teamSizeLong.intValue();
			
			this.lobbySpawnPos=Vec3String.StringToVec3((String)jsonObject.get("lobbySpawnPos"));
			
			this.spectatorSpawnPos=Vec3String.StringToVec3((String)jsonObject.get("spectatorSpawnPos"));

			
			
			this.teamSpawnPos = Vec3String.StringListToVec3((List<String>) jsonObject.get("teamSpawnPos"));
			this.teamNumber =((JSONArray) jsonObject.get("teamSpawnPos")).size();
			
			this.ironSpawnPos = Vec3String.StringListToVec3(((List<String>) jsonObject.get("ironSpawnPos")));
			
			this.goldSpawnPos = Vec3String.StringListToVec3(((List<String>) jsonObject.get("goldSpawnPos")));
			
			this.diamondSpawnPos = Vec3String.StringListToVec3(((List<String>) jsonObject.get("diamondSpawnPos")));

			this.beaconPos = Vec3String.StringListToVec3(((List<String>) jsonObject.get("beaconPos")));



		} catch (FileNotFoundException e) {
			BlackGeckoServer.logger.warn("*****************************!!!WARNING!!!***************************************");
			BlackGeckoServer.logger.warn("*********************************************************************************");
			BlackGeckoServer.logger.warn("FileNotFound : UNABLE TO LOAD " + EnumGameMode.SKY_BATTLE.toString() + " MAP " + map + ". ERROR:" +e);
			BlackGeckoServer.logger.warn("*********************************************************************************");
		} catch (IOException e) {
			BlackGeckoServer.logger.warn("*****************************!!!WARNING!!!***************************************");
			BlackGeckoServer.logger.warn("*********************************************************************************");
			BlackGeckoServer.logger.warn("IOExpection : UNABLE TO LOAD " + EnumGameMode.SKY_BATTLE.toString() + " MAP " + map + ". ERROR:" +e);
			BlackGeckoServer.logger.warn("*********************************************************************************");
		} catch (ParseException e) {
			BlackGeckoServer.logger.warn("*****************************!!!WARNING!!!***************************************");
			BlackGeckoServer.logger.warn("*********************************************************************************");
			BlackGeckoServer.logger.warn("ParseException : UNABLE TO LOAD " + EnumGameMode.SKY_BATTLE.toString() + " MAP " + map + ". ERROR:" +e);
			BlackGeckoServer.logger.warn("*********************************************************************************");
		}

	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void writeToJson(){
		//File folder = new File(MinecraftServer.getServer().worldServerForDimension(dimNumber).getSaveHandler().getWorldDirectory()+"/BGS/DIMENSIONS/"+EnumGameMode.SKY_BATTLE.toString()+"/WORLD_"+dimNumber);

		File folder = new File(BlackGeckoServer.baseFolder+"/CUSTOM_MAPS/"+EnumGameMode.SKY_BATTLE.toString()+"/MAP_"+this.mapName);
		
		JSONObject skyBattleObj = new JSONObject();
		
		skyBattleObj.put("name", this.mapName);
		skyBattleObj.put("playerPerTeam", this.teamSize);
		
		
		skyBattleObj.put("lobbySpawnPos", Vec3String.Vec3ToString(this.lobbySpawnPos));
		
		skyBattleObj.put("spectatorSpawnPos", Vec3String.Vec3ToString(this.spectatorSpawnPos));

		
		
		JSONArray teamSpawnPos = new JSONArray();
		teamSpawnPos.addAll(Vec3String.vec3ListToString(this.teamSpawnPos));
		skyBattleObj.put("teamSpawnPos", teamSpawnPos);
		
		
		
		JSONArray ironSpawnPos = new JSONArray();
		ironSpawnPos.addAll(Vec3String.vec3ListToString(this.ironSpawnPos));
		skyBattleObj.put("ironSpawnPos", ironSpawnPos);
		
		JSONArray goldSpawnPos = new JSONArray();
		goldSpawnPos.addAll(Vec3String.vec3ListToString(this.goldSpawnPos));
		skyBattleObj.put("goldSpawnPos", goldSpawnPos);
		
		JSONArray diamondSpawnPos = new JSONArray();
		diamondSpawnPos.addAll(Vec3String.vec3ListToString(this.diamondSpawnPos));
		skyBattleObj.put("diamondSpawnPos", diamondSpawnPos);
		
		JSONArray beaconPos = new JSONArray();
		beaconPos.addAll(Vec3String.vec3ListToString(this.beaconPos));
		skyBattleObj.put("beaconPos", beaconPos);

		
        try {  
            
            // Writing to a file  
        	if(!folder.exists()){
        		folder.mkdirs();
        	}
        	
            File file=new File(folder +"/BGSData.map");  
            file.createNewFile(); 
            
            FileWriter fileWriter = new FileWriter(file);
            
  
            fileWriter.write(skyBattleObj.toJSONString());  
            fileWriter.flush();  
            fileWriter.close();  
  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  

        
        //Copying world:
        File saveFolder = new File(MinecraftServer.getServer().worldServerForDimension(this.dim).getSaveHandler().getWorldDirectory()+"/BGS/DIMENSIONS/"+EnumGameMode.SKY_BATTLE+"/WORLD_"+this.dim);
        
		if(folder.exists()){
			try {				
				if(saveFolder.exists() && saveFolder.isDirectory() && saveFolder.list().length >0 ){
				
					FileUtils.copyDirectory(saveFolder, folder, new FileFilter() {
					    @Override
					    public boolean accept(File pathname) {
					        return pathname.canRead();
					    }
					});
				
				} else {

					BlackGeckoServer.logger.warn("*****************************!!!WARNING!!!***************************************");
					BlackGeckoServer.logger.warn("*********************************************************************************");
					BlackGeckoServer.logger.warn("UNABLE TO SAVE " + EnumGameMode.SKY_BATTLE.toString() + " MAP " + this.mapName);
					BlackGeckoServer.logger.warn("*********************************************************************************");
				}
			} catch (IOException e) {
				
			}
		}
        
        
	}

	@Override
	public void chatPlayer(EntityPlayer player, String msg){
		player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.AQUA + "[SKY BATTLE]" + msg));
	}
	
	@Override
	public void chatAllPlayers(String msg){
		
		for(EntityPlayer player : this.players){
			player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.AQUA + "[SKY BATTLE]" + msg));

		}
		for(EntityPlayer player : this.spectators){
			player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.AQUA + "[SKY BATTLE]" + msg));
		}
	}

}
