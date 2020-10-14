package net.whatamidoingstudios.lacroix.network.pipe;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PipeMessageReturn implements IMessage {

	
	int TYPE_UP = 0;
	int TYPE_DOWN = 0;
	int TYPE_NORTH = 0;
	int TYPE_SOUTH = 0;
	int TYPE_EAST = 0;
	int TYPE_WEST = 0;
	BlockPos pos = new BlockPos(0, 0, 0);
	
	public PipeMessageReturn() {
		
	}
	
	public PipeMessageReturn(BlockPos position, int up, int down, int north, int south, int east, int west) {
		TYPE_UP = up;
		TYPE_DOWN = down;
		TYPE_NORTH = north;
		TYPE_SOUTH = south;
		TYPE_EAST = east;
		TYPE_WEST = west;
		pos = position;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		TYPE_UP = buf.readInt();
		TYPE_DOWN = buf.readInt();
		TYPE_NORTH = buf.readInt();
		TYPE_SOUTH = buf.readInt();
		TYPE_EAST = buf.readInt();
		TYPE_WEST = buf.readInt();	
		
		System.out.println("recieve " + TYPE_DOWN);

	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeInt(TYPE_UP);
		buf.writeInt(TYPE_DOWN);
		buf.writeInt(TYPE_NORTH);
		buf.writeInt(TYPE_SOUTH);
		buf.writeInt(TYPE_EAST);
		buf.writeInt(TYPE_WEST);
		
		System.out.println("send " + TYPE_DOWN);
	}

}
