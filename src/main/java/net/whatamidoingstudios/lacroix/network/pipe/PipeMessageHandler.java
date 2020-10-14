package net.whatamidoingstudios.lacroix.network.pipe;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.whatamidoingstudios.lacroix.block.pipes.TileEntityEnergyPipe;
import net.whatamidoingstudios.lacroix.block.pipes.TileEntityFluidPipe;

public class PipeMessageHandler implements IMessageHandler<PipeMessage, IMessage>{

	@Override
	public IMessage onMessage(PipeMessage message, MessageContext ctx) {
		if(Minecraft.getMinecraft().world.getTileEntity(message.pos) instanceof TileEntityEnergyPipe) {
			((TileEntityEnergyPipe)Minecraft.getMinecraft().world.getTileEntity(message.pos)).TYPE_UP = message.UP;
			((TileEntityEnergyPipe)Minecraft.getMinecraft().world.getTileEntity(message.pos)).TYPE_DOWN = message.DOWN;
			((TileEntityEnergyPipe)Minecraft.getMinecraft().world.getTileEntity(message.pos)).TYPE_NORTH = message.NORTH;
			((TileEntityEnergyPipe)Minecraft.getMinecraft().world.getTileEntity(message.pos)).TYPE_SOUTH = message.SOUTH;
			((TileEntityEnergyPipe)Minecraft.getMinecraft().world.getTileEntity(message.pos)).TYPE_EAST = message.EAST;
			((TileEntityEnergyPipe)Minecraft.getMinecraft().world.getTileEntity(message.pos)).TYPE_WEST = message.WEST;
		}
		
		if(Minecraft.getMinecraft().world.getTileEntity(message.pos) instanceof TileEntityFluidPipe) {
			((TileEntityFluidPipe)Minecraft.getMinecraft().world.getTileEntity(message.pos)).TYPE_UP = message.UP;
			((TileEntityFluidPipe)Minecraft.getMinecraft().world.getTileEntity(message.pos)).TYPE_DOWN = message.DOWN;
			((TileEntityFluidPipe)Minecraft.getMinecraft().world.getTileEntity(message.pos)).TYPE_NORTH = message.NORTH;
			((TileEntityFluidPipe)Minecraft.getMinecraft().world.getTileEntity(message.pos)).TYPE_SOUTH = message.SOUTH;
			((TileEntityFluidPipe)Minecraft.getMinecraft().world.getTileEntity(message.pos)).TYPE_EAST = message.EAST;
			((TileEntityFluidPipe)Minecraft.getMinecraft().world.getTileEntity(message.pos)).TYPE_WEST = message.WEST;
		}
		return null;
	}
	
}
