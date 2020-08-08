package net.whatamidoingstudios.lacroix.block.steamturbine;

import java.util.Hashtable;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.whatamidoingstudios.lacroix.LaCroix;
import net.whatamidoingstudios.lacroix.block.EnumUpgrades;
import net.whatamidoingstudios.lacroix.block.ISteamConsumer;
import net.whatamidoingstudios.lacroix.block.TileEntityUpgradeable;
import net.whatamidoingstudios.lacroix.block.heater.TileEntityHeater;
import net.whatamidoingstudios.lacroix.capabilities.EnergyStorageUpgradeable;

public class TileEntityTurbine extends TileEntityUpgradeable implements ITickable, ISteamConsumer {
	
	
	private int capacity = 20000;
	private EnergyStorageUpgradeable energyStorage = new EnergyStorageUpgradeable(capacity, capacity, capacity, 0);
	private boolean hasSteamAccess = false;
	private int steamUsedPerTick = 1;
	
	public void save() {
		world.markBlockRangeForRenderUpdate(pos, pos);
		world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
		world.scheduleBlockUpdate(pos, this.getBlockType(), 0, 0);
		markDirty();
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
		if(oldState.getBlock() != newSate.getBlock()) {
			return true;
		}else {
			return false;
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setString("level", level.toString());
		compound.setInteger("energy", energyStorage.getEnergyStored());
		return super.writeToNBT(compound);
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if(capability == CapabilityEnergy.ENERGY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if(capability == CapabilityEnergy.ENERGY) {
			return (T)energyStorage;
		}
		return super.getCapability(capability, facing);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		level = EnumUpgrades.safeValueOf(compound.getString("level"));
		switch(level) {
		case Basic:
			capacity = 40000;		
			break;
		case Advanced:
			capacity = 60000;
			break;
		case Excellent:
			capacity = 80000;
			break;
		case Perfect:
			capacity = 100000;
			break;
		default:
			break;
		
		}
		energyStorage = new EnergyStorageUpgradeable(capacity, capacity, capacity, 0);
		energyStorage.setEnergy(compound.getInteger("energy"));
		super.readFromNBT(compound);
	}
	
	@Override
	@Nullable
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.pos, 3, this.getUpdateTag());
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);
		handleUpdateTag(pkt.getNbtCompound());
	}
	
	@Override
	public boolean upgrade(EnumUpgrades upgrade) {
		if(super.upgrade(upgrade)) {
			switch(level) {
			case Basic:
				capacity = 40000;
				
				break;
			case Advanced:
				capacity = 60000;
				break;
			case Excellent:
				capacity = 80000;
				break;
			case Perfect:
				capacity = 100000;
				break;
			default:
				break;
			
			}
			energyStorage = new EnergyStorageUpgradeable(capacity, capacity, capacity, energyStorage.getEnergyStored());
			return true;
		}
		return false;
	}

	@Override
	public void update() {
		if(!world.isRemote) {
			int currentLevel = 0;
			switch(level) {
			case Basic:
				currentLevel = 0;
				capacity = 40000;		
				break;
			case Advanced:
				currentLevel = 1;
				capacity = 60000;
				break;
			case Excellent:
				currentLevel = 2;
				capacity = 80000;
				break;
			case Perfect:
				currentLevel = 3;
				capacity = 100000;
				break;
			default:
				break;
			
			}
			energyStorage.increaseCapacity(capacity);

			if(hasSteamAccess) {				
				energyStorage.receiveEnergy((currentLevel+1)*125, false);
			}
			
			Hashtable<BlockPos, EnumFacing> consumers = new Hashtable<BlockPos, EnumFacing>();
			
			if(world.getTileEntity(pos.up()) != null ? world.getTileEntity(pos.up()).hasCapability(CapabilityEnergy.ENERGY, EnumFacing.DOWN) : false) {
				consumers.put(pos.up(), EnumFacing.DOWN);
			}
			if(world.getTileEntity(pos.down()) != null ? world.getTileEntity(pos.down()).hasCapability(CapabilityEnergy.ENERGY, EnumFacing.UP) : false) {
				consumers.put(pos.down(), EnumFacing.UP);
			}
			if(world.getTileEntity(pos.north()) != null ? world.getTileEntity(pos.north()).hasCapability(CapabilityEnergy.ENERGY, EnumFacing.SOUTH) : false) {
				consumers.put(pos.north(), EnumFacing.SOUTH);
			}
			if(world.getTileEntity(pos.south()) != null ? world.getTileEntity(pos.south()).hasCapability(CapabilityEnergy.ENERGY, EnumFacing.NORTH) : false) {
				consumers.put(pos.south(), EnumFacing.NORTH);
			}
			if(world.getTileEntity(pos.east()) != null ? world.getTileEntity(pos.east()).hasCapability(CapabilityEnergy.ENERGY, EnumFacing.WEST) : false) {
				consumers.put(pos.east(), EnumFacing.WEST);
			}
			if(world.getTileEntity(pos.west()) != null ? world.getTileEntity(pos.west()).hasCapability(CapabilityEnergy.ENERGY, EnumFacing.EAST) : false) {
				consumers.put(pos.west(), EnumFacing.EAST);
			}
			
			int ePerO = (consumers.size() > 0) ? energyStorage.getEnergyStored()/consumers.size() : 0;
						
			for(BlockPos output : consumers.keySet()) {
				energyStorage.extractEnergy((world.getTileEntity(output)).getCapability(CapabilityEnergy.ENERGY, consumers.get(output)).receiveEnergy(ePerO, false), false);
			}
			
			for(BlockPos output : consumers.keySet()) {
				energyStorage.extractEnergy((world.getTileEntity(output)).getCapability(CapabilityEnergy.ENERGY, consumers.get(output)).receiveEnergy(energyStorage.getEnergyStored(), false), false);
			}
		}
		save();
	}

	@Override
	public void setSteamAccess(boolean hasSteamAccess) {
		this.hasSteamAccess = hasSteamAccess;
	}

	@Override
	public int getSteamUsedPerTick() {
		return steamUsedPerTick;
	}

	@Override
	public boolean canConnectOnSide(EnumFacing side) {
		if(side == EnumFacing.DOWN) {
			return true;
		}
		return false;
	}

	@Override
	public boolean hasSteam() {
		return hasSteamAccess;
	}
}
