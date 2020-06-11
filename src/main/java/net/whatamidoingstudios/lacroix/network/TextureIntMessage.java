package net.whatamidoingstudios.lacroix.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class TextureIntMessage implements IMessage {

	BlockPos pos;
	int texture;
	
	public TextureIntMessage() {

	}
	
	public TextureIntMessage(BlockPos pos, int texture) {
		this.pos = pos;
		this.texture = texture;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		texture = buf.readInt();
		
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeInt(texture);
	}

}
