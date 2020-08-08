package net.whatamidoingstudios.lacroix.network.carbonator;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class CarbonatorDataMessage implements IMessage {

	public BlockPos pos;
	public boolean steamAccess;
	public int energy;
	public int maxEnergy;
	public int water;
	public int maxWater;
	public int carbonatedWater;
	public int maxCarbonatedWater;
	public int level;
	
	public CarbonatorDataMessage() {

	}
	
	public CarbonatorDataMessage(BlockPos pos, boolean steamAccess, int energy, int maxEnergy, int water, int maxWater, int carbonatedWater, int maxCarbonatedWater, int level) {
		this.pos = pos;
		this.steamAccess = steamAccess;
		this.energy = energy;
		this.maxEnergy = maxEnergy;
		this.water = water;
		this.maxWater = maxWater;
		this.carbonatedWater = carbonatedWater;
		this.maxCarbonatedWater = maxCarbonatedWater;
		this.level = level;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		steamAccess = buf.readBoolean();
		energy = buf.readInt();
		maxEnergy = buf.readInt();
		water = buf.readInt();
		maxWater = buf.readInt();
		carbonatedWater = buf.readInt();
		maxCarbonatedWater = buf.readInt();
		level = buf.readInt();
		
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeBoolean(steamAccess);
		buf.writeInt(energy);
		buf.writeInt(maxEnergy);
		buf.writeInt(water);
		buf.writeInt(maxWater);
		buf.writeInt(carbonatedWater);
		buf.writeInt(maxCarbonatedWater);
		buf.writeInt(level);
	}

}
