package net.whatamidoingstudios.lacroix.network.pipe;

import java.util.ArrayList;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PipeMessage implements IMessage {
	BlockPos pos = new BlockPos(0, 0, 0);
	int UP = 0;
	int DOWN = 0;
	int NORTH = 0;
	int SOUTH = 0;
	int EAST = 0;
	int WEST = 0;
	
	public PipeMessage() {
		
	}
	
	public PipeMessage(BlockPos _pos, int up, int down, int north, int south, int east, int west) {
		pos = _pos;
		UP = up;
		DOWN = down;
		NORTH = north;
		SOUTH = south;
		EAST = east; 
		WEST = west;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		pos = new BlockPos(buf.readFloat(), buf.readFloat(), buf.readFloat());
		UP = buf.readInt();
		DOWN = buf.readInt();
		NORTH = buf.readInt();
		SOUTH = buf.readInt();
		EAST = buf.readInt();
		WEST = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeFloat(pos.getX());
		buf.writeFloat(pos.getY());
		buf.writeFloat(pos.getZ());		
		buf.writeInt(UP);
		buf.writeInt(DOWN);
		buf.writeInt(NORTH);
		buf.writeInt(SOUTH);
		buf.writeInt(EAST);
		buf.writeInt(WEST);
	}

}
