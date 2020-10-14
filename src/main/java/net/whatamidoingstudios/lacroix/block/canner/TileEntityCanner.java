package net.whatamidoingstudios.lacroix.block.canner;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.whatamidoingstudios.lacroix.LaCroix;
import net.whatamidoingstudios.lacroix.ModObjects;
import net.whatamidoingstudios.lacroix.block.EnumUpgrades;
import net.whatamidoingstudios.lacroix.block.TileEntityUpgradeable;
import net.whatamidoingstudios.lacroix.capabilities.EnergyStorageUpgradeable;
import net.whatamidoingstudios.lacroix.capabilities.FluidTankLaCroix;
import net.whatamidoingstudios.lacroix.network.canner.CannerMessage;

public class TileEntityCanner extends TileEntityUpgradeable implements ITickable {
	
	private int capacity = 40000;
	private int rfUsedPerTick = 150;
	public int liquidCapacityPerFluid = 25000;
	
	private EnergyStorageUpgradeable energyStorage = new EnergyStorageUpgradeable(capacity, capacity, capacity, 0);
	private ItemStackHandler itemHandler = new ItemStackHandler(2) {
		
		@Override
	    protected void onContentsChanged(int slot) {
	      super.onContentsChanged(slot);
	      save();
	    }
	};
	
	private FluidTankLaCroix fluidHandler = new FluidTankLaCroix(FluidRegistry.getFluid("apricotfluid"), 0, liquidCapacityPerFluid);
	private int counter = 0;
	
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
		fluidHandler.writeToNBT(compound);
		compound.setTag("inventory", itemHandler.serializeNBT());
		return super.writeToNBT(compound);
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if(capability == CapabilityEnergy.ENERGY || capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if(capability == CapabilityEnergy.ENERGY) {
			return (T)energyStorage;
		}
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return (T)itemHandler;
		}
		if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return (T)fluidHandler;
		}
		return super.getCapability(capability, facing);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		level = EnumUpgrades.safeValueOf(compound.getString("level"));
		fluidHandler.readFromNBT(compound);
		
		itemHandler.deserializeNBT(compound.getCompoundTag("inventory"));
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
	
	public void resetEnergyStorage(int energy, int capacity) {
		this.capacity = capacity;
		energyStorage = new EnergyStorageUpgradeable(capacity, capacity, capacity, energy);
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
			
			if(fluidHandler.getFluidAmount() == 0) {
				fluidHandler.setFluid(null);
			}
			
			if(fluidHandler.getFluidAmount() >= 500 && fluidHandler.getFluid().getFluid() != null) {
				String fluidname = fluidHandler.getFluid().getFluid().getUnlocalizedName().substring(6);
				String itemname = fluidname.substring(0, fluidname.length()-5)+"lacroix";
				boolean itemcompat = itemHandler.getStackInSlot(1).isEmpty();
				if(itemHandler.getStackInSlot(0).getItem() == ModObjects.emptycan && itemcompat) {				
					if(energyStorage.extractEnergy(rfUsedPerTick, true) == rfUsedPerTick) {
						energyStorage.extractEnergy(rfUsedPerTick, false);
						++counter;
					}else {
						counter = 0;
					} 
				}else {
					counter = 0;
				}
			}else {
				counter = 0;
			}
			
			if(counter == 120) {
				counter = 0;
				itemHandler.extractItem(0, 1, false);
				String fluidname = fluidHandler.getFluid().getFluid().getUnlocalizedName().substring(6);
				String itemname = fluidname.substring(0, fluidname.length()-5)+"lacroix";
				itemHandler.insertItem(1, new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(LaCroix.MODID, itemname))), false);
				fluidHandler.drain(new FluidStack(fluidHandler.getFluid().getFluid(), 500), true);
			}			
			LaCroix.networkHandler.channel.sendToDimension(new CannerMessage(pos, (fluidHandler.getFluid() != null) ? fluidHandler.getFluid().getFluid().getName() : "nullfluid", fluidHandler.getFluidAmount(), capacity, energyStorage.getEnergyStored(), EnumUpgrades.enumToInt(level)), world.provider.getDimension());
		}
	}
}
