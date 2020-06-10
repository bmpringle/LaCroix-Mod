package net.whatamidoingstudios.lacroix.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.whatamidoingstudios.lacroix.block.heater.ContainerHeater;
import net.whatamidoingstudios.lacroix.block.heater.GuiHeater;
import net.whatamidoingstudios.lacroix.block.heater.TileEntityHeater;

public class LaCroixGuiHandler implements IGuiHandler {
	public static final int HEATER = 0;
	
	@Override
	public Container getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
			case HEATER:
				return new ContainerHeater(player.inventory, (TileEntityHeater)world.getTileEntity(new BlockPos(x, y, z)));
			default:
				return null;
		}
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
			case HEATER:
				return new GuiHeater(getServerGuiElement(ID, player, world, x, y, z), player.inventory);
			default:
				return null;
		}
	}

}
