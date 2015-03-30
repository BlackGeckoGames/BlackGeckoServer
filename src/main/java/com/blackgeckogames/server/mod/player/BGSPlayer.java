package com.blackgeckogames.server.mod.player;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

import com.blackgeckogames.server.mod.BlackGeckoServer;
import com.blackgeckogames.server.mod.gamemode.GameMode;
import com.blackgeckogames.server.mod.gamemode.GameMode.EnumGameMode;

public class BGSPlayer implements IExtendedEntityProperties{
	/*
	 * Here I create a constant EXT_PROP_NAME for this class of properties. You
	 * need a unique name for every instance of IExtendedEntityProperties you
	 * make, and doing it at the top of each class as a constant makes it very
	 * easy to organize and avoid typos. It's easiest to keep the same constant
	 * name in every class, as it will be distinguished by the class name:
	 * ExtendedPlayer.EXT_PROP_NAME vs. ExtendedEntity.EXT_PROP_NAME
	 * 
	 * Note that a single entity can have multiple extended properties, so each
	 * property should have a unique name. Try to come up with something more
	 * unique than the tutorial example.
	 */
	public final static String EXT_PROP_NAME = "MoroPlayer";

	// I always include the entity to which the properties belong for easy
	// access
	// It's final because we won't be changing which player it is
	private final EntityPlayer player;

	// Declare other variables you want to add here
	private EnumGameMode gameMode;


	private int experience;
	
	
	
	/*
	 * CONSTRUCTOR
	 * 
	 * The default constructor takes no arguments, but I put in the Entity so I
	 * can initialize the above variable 'player'
	 * 
	 * Also, it's best to initialize any other variables you may have added,
	 * just like in any constructor.
	 */
	public BGSPlayer(EntityPlayer player) {
		this.player = player;

		//Set Starting values
		this.experience=0;
		this.gameMode=EnumGameMode.LOBBY;
		
	}

	/**
	 * Used to register these extended properties for the player during
	 * EntityConstructing event This method is for convenience only; it will
	 * make your code look nicer
	 */
	public static final void register(EntityPlayer player) {
		player.registerExtendedProperties(BGSPlayer.EXT_PROP_NAME,new BGSPlayer(player));
	}

	/**
	 * Returns ExtendedPlayer properties for player This method is for
	 * convenience only; it will make your code look nicer
	 */
	public static final BGSPlayer get(EntityPlayer player) {
		return (BGSPlayer) player.getExtendedProperties(EXT_PROP_NAME);
	}

	// Save any custom data that needs saving here
	@Override
	public void saveNBTData(NBTTagCompound compound) {
		// We need to create a new tag compound that will save everything for
		// our Extended Properties
		NBTTagCompound properties = new NBTTagCompound();

		//save player data here:
		 properties.setInteger("xp", this.experience);
		
		compound.setTag(EXT_PROP_NAME, properties);
	}

	// Load whatever data you saved
	@Override
	public void loadNBTData(NBTTagCompound compound) {
		
		// Here we fetch the unique tag compound we set for this class of
		// Extended Properties
		

		if (!compound.hasKey(EXT_PROP_NAME)) {
			compound.setTag(EXT_PROP_NAME, new NBTTagCompound());
		}
		
		NBTTagCompound properties = (NBTTagCompound) compound.getTag(EXT_PROP_NAME);
		// Get our data from the custom tag compound
		
		
		//get player data
		this.experience = properties.getInteger("xp");
		
	}
	
	
	public void load(EntityPlayer player){
		NBTTagCompound playerData = BlackGeckoServer.proxy.getEntityData(((EntityPlayer) player).getUniqueID().toString());
		if (playerData != null) {
			this.loadNBTData(playerData);
		
		}
	}
	

	/*
	 * I personally have yet to find a use for this method. If you know of any,
	 * please let me know and I'll add it in!
	 */
	@Override
	public void init(Entity entity, World world) {
	}

	
	
	
	//GETTERS AND SETTERS
	
	public int getExperience() {
		return experience;
	}

	public void setExperience(int experience) {
		this.experience = experience;
	}

	
	
	public EnumGameMode getGameMode() {
		return gameMode;
	}

	public void setGameMode(EnumGameMode gameMode) {
		this.gameMode = gameMode;
	}


}
