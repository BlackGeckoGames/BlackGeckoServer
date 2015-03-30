package com.blackgeckogames.server.mod.utilities;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentProcessor;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class DisplayTitle {

	public static boolean sendTitle(EntityPlayer player, S45PacketTitle.Type type){
		if (type != S45PacketTitle.Type.CLEAR && type != S45PacketTitle.Type.RESET){
			return false;
		} else {
			return sendTitle(player, type, null);
		}
	}
	
	
	public static boolean sendTitle(EntityPlayer player, S45PacketTitle.Type type, ChatComponentText message){
		return sendTitle(player, type, message, 1, 5, 1);
	}

	
	public static boolean sendTitle(EntityPlayer player, S45PacketTitle.Type type, ChatComponentText message, int fadeIn, int stay, int fadeOut){
		EntityPlayerMP entityplayermp =(EntityPlayerMP) player;
		
		ICommandSender commandSender = MinecraftServer.getServer();
		
		
		if (type != S45PacketTitle.Type.CLEAR && type != S45PacketTitle.Type.RESET){
			
			S45PacketTitle s45packettitle1;
			try {
				s45packettitle1 = new S45PacketTitle(type, ChatComponentProcessor.func_179985_a(commandSender, message, player));
			    entityplayermp.playerNetServerHandler.sendPacket(s45packettitle1);
			    return true;
			} catch (CommandException e) {
				// TODO Auto-generated catch block
				return false;
			}
		    
		} else {
	        S45PacketTitle s45packettitle = new S45PacketTitle(type, (IChatComponent)null);
	        entityplayermp.playerNetServerHandler.sendPacket(s45packettitle);	
	        return true;
	}
}
}
