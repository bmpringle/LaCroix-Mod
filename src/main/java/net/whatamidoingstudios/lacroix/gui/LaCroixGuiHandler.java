package net.whatamidoingstudios.lacroix.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.whatamidoingstudios.lacroix.block.canner.ContainerCanner;
import net.whatamidoingstudios.lacroix.block.canner.TileEntityCanner;
import net.whatamidoingstudios.lacroix.block.heater.ContainerHeater;
import net.whatamidoingstudios.lacroix.block.heater.TileEntityHeater;

public class LaCroixGuiHandler implements IGuiHandler {
	public static final int HEATER = 0;
	public static final int STEAMTURBINE = 1;
	public static final int CARBONATOR = 2;
	public static final int CANNER = 3;
	public static final int PIPE = 4;
	
	@Override
	public Container getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
			case HEATER:
				return new ContainerHeater(player.inventory, (TileEntityHeater)world.getTileEntity(new BlockPos(x, y, z)));
			case CANNER:
				return new ContainerCanner(player.inventory, (TileEntityCanner)world.getTileEntity(new BlockPos(x, y, z)));
			default:
				return null;
		}
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
			case HEATER:
				return new GuiHeater(getServerGuiElement(ID, player, world, x, y, z), player.inventory);
			case STEAMTURBINE:
				return new GuiSteamTurbine(x, y, z);
			case CARBONATOR:
				return new GuiCarbonator(x, y, z);
			case CANNER:
				return new GuiCanner((ContainerCanner)getServerGuiElement(ID, player, world, x, y, z), player.inventory);
			case PIPE:
				return new GuiPipe(x, y, z);
			default:
				return null;
		}
	}

}
