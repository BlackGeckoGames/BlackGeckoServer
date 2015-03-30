package com.blackgeckogames.server.mod;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.Vec3;

public class Utilities {
	
	public static void deleteFolder(File folder) {
	    File[] files = folder.listFiles();
	    if(files!=null) { //some JVMs return null for empty dirs
	        for(File f: files) {
	            if(f.isDirectory()) {
	                deleteFolder(f);
	            } else {
	                f.delete();
	            }
	        }
	    }
	    folder.delete();
	}
}

