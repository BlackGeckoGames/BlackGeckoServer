package com.blackgeckogames.server.mod.utilities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S29PacketSoundEffect;

public class PlaySound {
	
	public static void play(EntityPlayer player, String soundName, double x, double y, double z, float volume, float pitch){
		
		
        ((EntityPlayerMP)player).playerNetServerHandler.sendPacket(new S29PacketSoundEffect(soundName, x, y, z, volume, pitch));

	}
	public static void play(EntityPlayer player, String soundName, float volume, float pitch){
		
		
        ((EntityPlayerMP)player).playerNetServerHandler.sendPacket(new S29PacketSoundEffect(soundName, player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ(), volume, pitch));

	}
	

}
