package net.whatamidoingstudios.lacroix.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class HeaterMessageServerResponse implements IMessage {

	boolean textToggle;
	BlockPos pos;
	
	public HeaterMessageServerResponse() {

	}
	
	public HeaterMessageServerResponse(boolean textToggle, BlockPos pos) {
		this.textToggle = textToggle;
		this.pos = pos;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		textToggle = buf.readBoolean();
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(textToggle);
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());

	}

}
