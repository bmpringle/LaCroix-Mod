package net.whatamidoingstudios.lacroix.block.carbonator;

import java.util.Hashtable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.whatamidoingstudios.lacroix.LaCroix;
import net.whatamidoingstudios.lacroix.block.EnumUpgrades;
import net.whatamidoingstudios.lacroix.block.ISteamConsumer;
import net.whatamidoingstudios.lacroix.block.TileEntityUpgradeable;
import net.whatamidoingstudios.lacroix.block.canner.TileEntityCanner;
import net.whatamidoingstudios.lacroix.capabilities.EnergyStorageUpgradeable;
import net.whatamidoingstudios.lacroix.capabilities.FluidTankLaCroix;
import net.whatamidoingstudios.lacroix.network.carbonator.CarbonatorDataMessage;

public class TileEntityCarbonator extends TileEntityUpgradeable implements ITickable, ISteamConsumer {
	
	private int capacity = 40000;
	private EnergyStorageUpgradeable energyStorage = new EnergyStorageUpgradeable(capacity, capacity, capacity, 0);
	private boolean hasSteamAccess = false;
	private int steamUsedPerTick = 5;
	private int rfUsedPerTick = 150;
	public int liquidCapacityPerFluid = 25000;
	public FluidTankLaCroix fluidHandlerWater = new FluidTankLaCroix(FluidRegistry.WATER, 0, liquidCapacityPerFluid);
	public FluidTankLaCroix fluidHandlerCarbonatedWater = new FluidTankLaCroix(FluidRegistry.getFluid("purefluid"), 0, liquidCapacityPerFluid);
	
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
		compound.setInteger("water", fluidHandlerWater.getFluidAmount());
		compound.setInteger("carbonatedwater", fluidHandlerCarbonatedWater.getFluidAmount());
		compound.setBoolean("hasSteam", hasSteamAccess);
		return super.writeToNBT(compound);
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if(capability == CapabilityEnergy.ENERGY) {
			return true;
		}
		if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if(capability == CapabilityEnergy.ENERGY) {
			return (T)energyStorage;
		}
		if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			if(facing == EnumFacing.DOWN) {
				return (T) fluidHandlerWater;
			}
			return (T)fluidHandlerCarbonatedWater;
		}
		return super.getCapability(capability, facing);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		level = EnumUpgrades.safeValueOf(compound.getString("level"));
		fluidHandlerWater.setFluidAmount(compound.getInteger("water"));
		fluidHandlerCarbonatedWater.setFluidAmount(compound.getInteger("carbonatedwater"));
		hasSteamAccess = compound.getBoolean("hasSteam");
		
		switch(level) {
		case Basic:
			capacity = 40000;		
			break;
		case Advanced:
			capacity = 80000;
			break;
		case Excellent:
			capacity = 160000;
			break;
		case Perfect:
			capacity = 320000;
			break;
		default:
			break;
		
		}
		energyStorage = new EnergyStorageUpgradeable(capacity, capacity, capacity, 0);
		energyStorage.setEnergy(compound.getInteger("energy"));
		super.readFromNBT(compound);
	}
		
	@Override
	public boolean upgrade(EnumUpgrades upgrade) {
		if(super.upgrade(upgrade)) {
			switch(level) {
			case Basic:
				capacity = 40000;		
				break;
			case Advanced:
				capacity = 80000;
				break;
			case Excellent:
				capacity = 160000;
				break;
			case Perfect:
				capacity = 320000;
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
			fluidHandlerWater.getFluidAmount();
			fluidHandlerCarbonatedWater.getFluidAmount();
			int currentLevel = 0;
			switch(level) {
			case Basic:
				capacity = 40000;
				currentLevel = 0;
				break;
			case Advanced:
				capacity = 80000;
				currentLevel = 1;
				break;
			case Excellent:
				capacity = 160000;
				currentLevel = 2;
				break;
			case Perfect:
				capacity = 320000;
				currentLevel = 3;
				break;
			default:
				break;
			
			}
			energyStorage.increaseCapacity(capacity);
			
			if(hasSteamAccess) {
				if(energyStorage.getEnergyStored() >= rfUsedPerTick && fluidHandlerWater.getFluidAmount() >= 10 && fluidHandlerCarbonatedWater.getFluidAmount() != liquidCapacityPerFluid) {
					energyStorage.extractEnergy(rfUsedPerTick, false);
					fluidHandlerWater.drain(((currentLevel+1)*10), true);
					fluidHandlerCarbonatedWater.fill(new FluidStack(FluidRegistry.getFluid("purefluid"), (currentLevel+1)*20), true);
				}
			}
			LaCroix.networkHandler.channel.sendToDimension(new CarbonatorDataMessage(pos, hasSteamAccess, energyStorage.getEnergyStored(), energyStorage.getMaxEnergyStored(), fluidHandlerWater.getFluidAmount(), liquidCapacityPerFluid, fluidHandlerCarbonatedWater.getFluidAmount(), liquidCapacityPerFluid, EnumUpgrades.enumToInt(level)), 0);
			
			Hashtable<BlockPos, EnumFacing> consumers = new Hashtable<BlockPos, EnumFacing>();
			
			if(world.getTileEntity(pos.up()) != null ? world.getTileEntity(pos.up()).hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.DOWN) && world.getTileEntity(pos.up()).getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.DOWN).getTankProperties()[0].canFillFluidType(fluidHandlerCarbonatedWater.getFluid()) : false) {
				consumers.put(pos.up(), EnumFacing.DOWN);
			}
			if(world.getTileEntity(pos.down()) != null ? world.getTileEntity(pos.down()).hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.UP) && world.getTileEntity(pos.down()).getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.UP).getTankProperties()[0].canFillFluidType(fluidHandlerCarbonatedWater.getFluid()) : false) {
				consumers.put(pos.down(), EnumFacing.UP);
			}
			if(world.getTileEntity(pos.north()) != null ? world.getTileEntity(pos.north()).hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.SOUTH) && world.getTileEntity(pos.north()).getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.SOUTH).getTankProperties()[0].canFillFluidType(fluidHandlerCarbonatedWater.getFluid()) : false) {
				consumers.put(pos.north(), EnumFacing.SOUTH);
			}
			if(world.getTileEntity(pos.south()) != null ? world.getTileEntity(pos.south()).hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.NORTH) && world.getTileEntity(pos.south()).getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.NORTH).getTankProperties()[0].canFillFluidType(fluidHandlerCarbonatedWater.getFluid()) : false) {
				consumers.put(pos.south(), EnumFacing.NORTH);
			}
			if(world.getTileEntity(pos.east()) != null ? world.getTileEntity(pos.east()).hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.WEST) && world.getTileEntity(pos.east()).getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.WEST).getTankProperties()[0].canFillFluidType(fluidHandlerCarbonatedWater.getFluid()) : false) {
				consumers.put(pos.east(), EnumFacing.WEST);
			}
			if(world.getTileEntity(pos.west()) != null ? world.getTileEntity(pos.west()).hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.EAST) && world.getTileEntity(pos.west()).getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.EAST).getTankProperties()[0].canFillFluidType(fluidHandlerCarbonatedWater.getFluid()) : false) {
				consumers.put(pos.west(), EnumFacing.EAST);
			}
			
			int c_size = consumers.size();
			int fluid = fluidHandlerCarbonatedWater.getFluidAmount();
			
			for(BlockPos consumer : consumers.keySet()) {
				fluidHandlerCarbonatedWater.drain(world.getTileEntity(consumer).getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, consumers.get(consumer)).fill((fluidHandlerCarbonatedWater.getFluidAmount() > fluid/c_size) ? new FluidStack(fluidHandlerCarbonatedWater.getFluid().getFluid(), fluid/c_size) : fluidHandlerCarbonatedWater.getFluid(), true), true);
			}
		}else {
			fluidHandlerWater.getFluidAmount();
			fluidHandlerCarbonatedWater.getFluidAmount();
		}
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
		return true;
	}

	@Override
	public boolean hasSteam() {
		return hasSteamAccess;
	}
}