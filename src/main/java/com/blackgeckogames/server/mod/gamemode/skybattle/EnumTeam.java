package com.blackgeckogames.server.mod.gamemode.skybattle;

import net.minecraft.util.EnumChatFormatting;

public enum EnumTeam {
	BLUE, RED, GREEN, YELLOW, PINK, BLACK, CYAN, ORANGE;
	
	public EnumChatFormatting getChatColor(){
		
		EnumChatFormatting format=EnumChatFormatting.WHITE;
		
		switch(this){
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
			case PINK:
				format=EnumChatFormatting.LIGHT_PURPLE;
				break;
			case BLACK:
				format=EnumChatFormatting.BLACK;
				break;
			case CYAN:
				format=EnumChatFormatting.AQUA;
				break;
			case ORANGE:
				format=EnumChatFormatting.GOLD;
				break;
		}
				
		return format;	
		
	}
}
