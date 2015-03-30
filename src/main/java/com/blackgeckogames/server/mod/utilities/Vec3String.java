package com.blackgeckogames.server.mod.utilities;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.Vec3;

public class Vec3String {

	
	public static String Vec3ToString(Vec3 vec){
		
		String s = vec.xCoord+ "," +vec.yCoord+ ","+ vec.zCoord;
		
		
		return s;
		
	}
	public static List<String> vec3ListToString(List<Vec3> input){
		List<String> list = new ArrayList<String>();
		
		for(Vec3 vec : input){
			list.add(Vec3ToString(vec));
		}
		
		return list;
		
	}
	
	
	public static Vec3 StringToVec3(String s){		
		double numberX = 0;
		try {
			numberX = Double.parseDouble(s.substring(0,s.indexOf(",")));
		} catch (NumberFormatException nfe) {}
		
		double numberY = 0;
		try {
			numberY = Double.parseDouble(s.substring(s.indexOf(",")+1, s.indexOf(",", s.indexOf(",")+1)));
		} catch (NumberFormatException nfe) {}
				
		double numberZ = 0;
		try {
			numberZ = Double.parseDouble(s.substring(s.indexOf(",", s.indexOf(",")+1)+1, s.length()-1));
		} catch (NumberFormatException nfe) {}		
		
		return new Vec3(numberX,numberY,numberZ);
		
	}
	public static List<Vec3> StringListToVec3(List<String> input){
		List<Vec3> list = new ArrayList<Vec3>();
		
		for(String s : input){
			list.add(StringToVec3(s));
		}
		
		return list;
		
	}
}
