package net.whatamidoingstudios.lacroix.network.heater;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class HeaterMessageClient implements IMessage {

	float x;
	float y;
	float z;
	
	public HeaterMessageClient() {
		
	}
	
	public HeaterMessageClient(BlockPos pos) {
		x = pos.getX();
		y = pos.getY();
		z = pos.getZ();
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		x = buf.readFloat();
		y = buf.readFloat();
		z = buf.readFloat();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeFloat(x);
		buf.writeFloat(y);
		buf.writeFloat(z);		
	}

}
