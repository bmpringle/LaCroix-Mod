package net.whatamidoingstudios.lacroix.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.whatamidoingstudios.lacroix.LaCroix;
import net.whatamidoingstudios.lacroix.block.heater.TileEntityHeater;

public class HeaterMessageServerHandler implements IMessageHandler<HeaterMessageServer, HeaterMessageServerResponse> {

	@Override
	public HeaterMessageServerResponse onMessage(HeaterMessageServer message, MessageContext ctx) {
		if(message.s == '0') {
			BlockPos pos = new BlockPos(message.x, message.y, message.z);
			if(ctx.getServerHandler().player.world.getTileEntity(pos) instanceof TileEntityHeater) {
				((TileEntityHeater)ctx.getServerHandler().player.world.getTileEntity(pos)).captureSteam = message.cap;
			}
			return null;
		}else {
			BlockPos pos = new BlockPos(message.x, message.y, message.z);
			if(ctx.getServerHandler().player.world.getTileEntity(pos) instanceof TileEntityHeater) {
				return new HeaterMessageServerResponse(((TileEntityHeater)ctx.getServerHandler().player.world.getTileEntity(pos)).captureSteam, pos);
			}
		}
		return null;
	}

}
