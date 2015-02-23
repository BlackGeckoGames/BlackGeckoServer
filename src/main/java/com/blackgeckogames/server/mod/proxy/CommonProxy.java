package com.blackgeckogames.server.mod.proxy;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;

public class CommonProxy {
	private static final Map<String, NBTTagCompound> extendedEntityData = new HashMap<String, NBTTagCompound>();

	public void storeEntityData(String uuid, NBTTagCompound compound) {
		extendedEntityData.put(uuid, compound);
	}
	/**
	 * Removes the compound from the map and returns the NBT tag stored for name
	 * or null if none exists
	 */
	public NBTTagCompound getEntityData(String uuid) {
		return extendedEntityData.remove(uuid);
	}
	
	
	
	
}
