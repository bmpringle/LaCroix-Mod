package net.whatamidoingstudios.lacroix.network.pipe;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.whatamidoingstudios.lacroix.block.pipes.TileEntityEnergyPipe;
import net.whatamidoingstudios.lacroix.block.pipes.TileEntityFluidPipe;

public class PipeMessageReturnHandler implements IMessageHandler<PipeMessageReturn, IMessage>{

	@Override
	public IMessage onMessage(PipeMessageReturn message, MessageContext ctx) {
		if(!ctx.getServerHandler().player.world.isRemote) {
			if(ctx.getServerHandler().player.world.getTileEntity(message.pos) instanceof TileEntityEnergyPipe) {
				
				TileEntityEnergyPipe te = ((TileEntityEnergyPipe)ctx.getServerHandler().player.world.getTileEntity(message.pos));
				
				//System.out.println("TYPE_UP = " + te.TYPE_UP + " TYPE_DOWN = " + te.TYPE_DOWN + " TYPE_NORTH = " + te.TYPE_NORTH + " TYPE_SOUTH = " + te.TYPE_SOUTH + " TYPE_EAST = " + te.TYPE_EAST + " TYPE_WEST = " + te.TYPE_WEST);
				
				((TileEntityEnergyPipe)ctx.getServerHandler().player.world.getTileEntity(message.pos)).TYPE_UP = message.TYPE_UP;
				((TileEntityEnergyPipe)ctx.getServerHandler().player.world.getTileEntity(message.pos)).TYPE_DOWN = message.TYPE_DOWN;
				((TileEntityEnergyPipe)ctx.getServerHandler().player.world.getTileEntity(message.pos)).TYPE_NORTH = message.TYPE_NORTH;
				((TileEntityEnergyPipe)ctx.getServerHandler().player.world.getTileEntity(message.pos)).TYPE_SOUTH = message.TYPE_SOUTH;
				((TileEntityEnergyPipe)ctx.getServerHandler().player.world.getTileEntity(message.pos)).TYPE_EAST = message.TYPE_EAST;
				((TileEntityEnergyPipe)ctx.getServerHandler().player.world.getTileEntity(message.pos)).TYPE_WEST = message.TYPE_WEST;
				
				te = ((TileEntityEnergyPipe)ctx.getServerHandler().player.world.getTileEntity(message.pos));
				
				//System.out.println("TYPE_UP = " + te.TYPE_UP + " TYPE_DOWN = " + te.TYPE_DOWN + " TYPE_NORTH = " + te.TYPE_NORTH + " TYPE_SOUTH = " + te.TYPE_SOUTH + " TYPE_EAST = " + te.TYPE_EAST + " TYPE_WEST = " + te.TYPE_WEST);
			}
			
			if(ctx.getServerHandler().player.world.getTileEntity(message.pos) instanceof TileEntityFluidPipe) {
				((TileEntityFluidPipe)ctx.getServerHandler().player.world.getTileEntity(message.pos)).TYPE_UP = message.TYPE_UP;
				((TileEntityFluidPipe)ctx.getServerHandler().player.world.getTileEntity(message.pos)).TYPE_DOWN = message.TYPE_DOWN;
				((TileEntityFluidPipe)ctx.getServerHandler().player.world.getTileEntity(message.pos)).TYPE_NORTH = message.TYPE_NORTH;
				((TileEntityFluidPipe)ctx.getServerHandler().player.world.getTileEntity(message.pos)).TYPE_SOUTH = message.TYPE_SOUTH;
				((TileEntityFluidPipe)ctx.getServerHandler().player.world.getTileEntity(message.pos)).TYPE_EAST = message.TYPE_EAST;
				((TileEntityFluidPipe)ctx.getServerHandler().player.world.getTileEntity(message.pos)).TYPE_WEST = message.TYPE_WEST;
			}
		}
		return null;
	}

}
