package com.blackgeckogames.server.mod.dimension.teleporter;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

public class TeleporterPosition extends Teleporter {

	private final WorldServer worldServerInstance;

	private double x;
	private double y;
	private double z;

	public TeleporterPosition(WorldServer world, double x, double y, double z) {
		super(world);
		this.worldServerInstance = world;
		this.x = x;
		this.y = y;
		this.z = z;

	}

	@Override
	public void placeInPortal(Entity entityIn, float rotationYaw) {

		this.worldServerInstance.getBlockState(new BlockPos((int) this.x, (int) this.y, (int) this.z)); // dummy
																						// load
																						// to
																						// maybe
																						// gen
																						// chunk

		entityIn.setPosition(this.x, this.y, this.z);
	}

	public static void teleport(EntityPlayer player, int dim, double x, double y, double z) {

		MinecraftServer mServer = MinecraftServer.getServer();
		Side sidex = FMLCommonHandler.instance().getEffectiveSide();
		if (sidex == Side.SERVER) {
			if (player instanceof EntityPlayerMP) {
				EntityPlayerMP playerMP = (EntityPlayerMP) player;
				if (player.ridingEntity == null && player.riddenByEntity == null && player instanceof EntityPlayer) {
					FMLCommonHandler.instance().getMinecraftServerInstance();
					playerMP.mcServer.getConfigurationManager().transferPlayerToDimension(playerMP, dim, new TeleporterPosition(mServer.worldServerForDimension(dim), x, y, z));

				}

			}
		}
	}

}