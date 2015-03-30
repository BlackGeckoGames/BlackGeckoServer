package com.blackgeckogames.server.mod.dimension;

import com.blackgeckogames.server.mod.gamemode.GameMode.EnumGameMode;

import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderFlat;
import net.minecraft.world.gen.ChunkProviderHell;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class WorldProviderSkyBattle extends WorldProvider
{
    private WorldType terrainType =WorldType.FLAT;
    
    public String getDimensionName()
    {
        return "Overworld";
    }
    
    public IChunkProvider createChunkGenerator()
    {
        return new ChunkProviderFlat(this.worldObj, this.worldObj.getSeed(), false, "");
    }

    public String getInternalNameSuffix()
    {
        return "";
    }
    
    public boolean canRespawnHere()
    {
        return false;
    }
    
    
    public String getSaveFolder()
    {
        return ("BGS/DIMENSIONS/"+EnumGameMode.SKY_BATTLE.toString()+"/WORLD_" + dimensionId);
    }


    public String getWelcomeMessage()
    {
        return "Connecting to server...";
    }
    
}