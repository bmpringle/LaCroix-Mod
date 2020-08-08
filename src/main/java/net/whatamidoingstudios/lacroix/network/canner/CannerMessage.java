package net.whatamidoingstudios.lacroix.network.canner;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class CannerMessage implements IMessage {

	
	String fluidname = "";
	int amount = 0;
	int energyCapacity = 0;
	int energyStored = 0;
	BlockPos pos = new BlockPos(0, 0, 0);
	int type = 0;
	
	public CannerMessage() {

	}
	
	public CannerMessage(BlockPos pos, String fluidname, int amount, int energyCapacity, int energyStored, int type) {
		this.fluidname = fluidname;
		this.pos = pos;
		this.amount = amount;
		this.energyCapacity = energyCapacity;
		this.energyStored = energyStored;
		this.type = type;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		amount = buf.readInt();
		energyCapacity = buf.readInt();
		energyStored = buf.readInt();
		type = buf.readInt();
		fluidname = buf.readCharSequence(buf.readableBytes(), Charset.defaultCharset()).toString();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeInt(amount);
		buf.writeInt(energyCapacity);
		buf.writeInt(energyStored);
		buf.writeInt(type);
		buf.writeCharSequence(fluidname, Charset.defaultCharset());
	}

}
