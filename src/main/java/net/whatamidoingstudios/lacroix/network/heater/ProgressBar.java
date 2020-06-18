package net.whatamidoingstudios.lacroix.network.heater;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ProgressBar implements IMessage {
	
	int steam;
	BlockPos pos;
	int maxsteam;
	String typename;
	
	public ProgressBar() {
		
	}
	
	public ProgressBar(int _steam, BlockPos _pos, int _maxsteam, String _typename) {
		steam = _steam;
		pos = _pos;
		maxsteam = _maxsteam;
		typename = _typename;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		steam = buf.readInt();
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		maxsteam = buf.readInt();
		typename = buf.readCharSequence(buf.readableBytes(), Charset.defaultCharset()).toString();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(steam);
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeInt(maxsteam);
		buf.writeCharSequence(typename, Charset.defaultCharset());
	}

}
