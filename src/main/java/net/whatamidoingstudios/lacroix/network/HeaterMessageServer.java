package net.whatamidoingstudios.lacroix.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class HeaterMessageServer implements IMessage {
	
	float x;
	float y;
	float z;
	boolean cap;
	char s = '0';
	
	public HeaterMessageServer() {
		
	}
	
	public HeaterMessageServer(BlockPos pos, boolean captureCO2, boolean sendinitdatainstead) {
		x = pos.getX();
		y = pos.getY();
		z = pos.getZ();
		if(sendinitdatainstead) {
			cap = sendinitdatainstead;
			s = '1';
		}else {
			cap = captureCO2;
		}
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		x = buf.readFloat();
		y = buf.readFloat();
		z = buf.readFloat();
		s = buf.readChar();
		cap = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeFloat(x);
		buf.writeFloat(y);
		buf.writeFloat(z);
		buf.writeChar(s);
		buf.writeBoolean(cap);
		
	}
	
}
