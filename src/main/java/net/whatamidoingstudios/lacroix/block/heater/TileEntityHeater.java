package net.whatamidoingstudios.lacroix.block.heater;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.whatamidoingstudios.lacroix.LaCroix;
import net.whatamidoingstudios.lacroix.network.HeaterMessageClient;
import net.whatamidoingstudios.lacroix.network.ProgressBar;

public class TileEntityHeater extends TileEntityUpgradeable implements ITickable {

	private ItemStackHandler inventory = new ItemStackHandler(1) {
		@Override
	    protected void onContentsChanged(int slot) {
	      super.onContentsChanged(slot);
	      save();
	    }
	};
	
	public int steam = 0;
	private int burnInSeconds = 10;
	private int burnInTicks = burnInSeconds * 20;
	private int currentBurnInTicks = 0;
	private int currentReleaseInTicks = 0;
	private boolean isBurning = false;
	private int steamPerSecond = 1;
	
	public boolean captureSteam = true;
	public int steamMaxStorage = 500;
	
	private void save() {
		world.markBlockRangeForRenderUpdate(pos, pos);
		world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
		world.scheduleBlockUpdate(pos, this.getBlockType(), 0, 0);
		markDirty();
	}
	
	@Override
	public void update() {
		if(!world.isRemote) {
			int currentLevel = 0;
			
			switch(level) {
			case Basic:
				steamMaxStorage = 500;
				steamPerSecond = 1;
				currentLevel = 0;
				break;
			case Advanced:
				steamMaxStorage = 1000;
				steamPerSecond = 2;
				currentLevel = 1;
				break;
			case Excellent:
				steamMaxStorage = 2000;
				steamPerSecond = 4;
				currentLevel = 2;
				break;
			case Perfect:
				steamMaxStorage = 4000;
				steamPerSecond = 8;
				currentLevel = 3;
				break;
			default:
				break;
			
			}
			
			if(currentBurnInTicks != burnInTicks && isBurning) {
				++currentBurnInTicks;
				handleBurningTick();
			}else {
				currentBurnInTicks = 0;
				isBurning = false;
			}
			
			if(currentReleaseInTicks != burnInTicks && !captureSteam) {
				++currentReleaseInTicks;
				handleBurningTick();
			}else {
				currentReleaseInTicks = 0;
			}
			
			if(steam > steamMaxStorage) {
				steam = steamMaxStorage;
			}
			
			if(steam < steamMaxStorage && !isBurning && inventory.getStackInSlot(inventory.getSlots()-1).getItem() == Items.COAL) {
				inventory.extractItem(inventory.getSlots()-1, 1, false);
				isBurning = true;
			}
			LaCroix.networkHandler.channel.sendToDimension(new ProgressBar(steam, pos, steamMaxStorage, level.toString()), world.provider.getDimension());
			this.world.setBlockState(pos, this.world.getBlockState(pos).withProperty(BlockHeater.TEXTURE, currentLevel));
			this.world.setBlockState(pos, this.world.getBlockState(pos).withProperty(BlockHeater.OPENHATCH, !captureSteam));
			save();
		}
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
		if(oldState.getBlock() != newSate.getBlock()) {
			return true;
		}else {
			return false;
		}
	}
	
	private void handleBurningTick() {
		if(currentBurnInTicks % 20 == 0 && steam < steamMaxStorage && isBurning) {
			steam = steam + steamPerSecond;
		}
		if(currentReleaseInTicks % 20 == 0 && steam > 0 && !captureSteam) {
			--steam;
			LaCroix.networkHandler.channel.sendToDimension(new HeaterMessageClient(pos), world.provider.getDimension());
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setTag("inventory", inventory.serializeNBT());
		compound.setInteger("steam", steam);
		compound.setInteger("currentBurnInTicks", currentBurnInTicks);
		compound.setBoolean("isBurning", isBurning);
		compound.setBoolean("captureSteam", captureSteam);
		compound.setString("level", level.toString());
		
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		inventory.deserializeNBT(compound.getCompoundTag("inventory"));
		steam = compound.getInteger("steam");
		currentBurnInTicks = compound.getInteger("currentBurnInTicks");
		isBurning = compound.getBoolean("isBurning");
		captureSteam = compound.getBoolean("captureSteam");
		level = EnumUpgrades.valueOf(compound.getString("level"));

		super.readFromNBT(compound);
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}
	
	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T)inventory : super.getCapability(capability, facing);
	}

}