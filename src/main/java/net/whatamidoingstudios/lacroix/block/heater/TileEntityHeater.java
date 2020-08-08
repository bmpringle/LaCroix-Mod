package net.whatamidoingstudios.lacroix.block.heater;

import java.util.ArrayList;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.whatamidoingstudios.lacroix.LaCroix;
import net.whatamidoingstudios.lacroix.block.EnumUpgrades;
import net.whatamidoingstudios.lacroix.block.ISteam;
import net.whatamidoingstudios.lacroix.block.ISteamConsumer;
import net.whatamidoingstudios.lacroix.block.ISteamProvider;
import net.whatamidoingstudios.lacroix.block.TileEntityUpgradeable;
import net.whatamidoingstudios.lacroix.block.pipes.BlockSteamPipe;
import net.whatamidoingstudios.lacroix.network.heater.HeaterMessageClient;
import net.whatamidoingstudios.lacroix.network.heater.ProgressBar;

public class TileEntityHeater extends TileEntityUpgradeable implements ITickable, ISteamProvider {

	private ItemStackHandler inventory = new ItemStackHandler(1) {
		@Override
	    protected void onContentsChanged(int slot) {
	      super.onContentsChanged(slot);
	      save();
	    }
	};
	
	public int steam = 0;
	private int burnInTicks = 200;
	private boolean isBurning = false;
	private int steamPerSecond = 1;
	private int burnProgress = 0;
	
	public boolean captureSteam = true;
	public int steamMaxStorage = 10000;
	
	private ArrayList<BlockPos> previousArray = new ArrayList<BlockPos>();
	
	public void save() {
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
				steamMaxStorage = 10000;
				steamPerSecond = 20;
				currentLevel = 0;
				break;
			case Advanced:
				steamMaxStorage = 20000;
				steamPerSecond = 40;
				currentLevel = 1;
				break;
			case Excellent:
				steamMaxStorage = 40000;
				steamPerSecond = 80;
				currentLevel = 2;
				break;
			case Perfect:
				steamMaxStorage = 80000;
				steamPerSecond = 160;
				currentLevel = 3;
				break;
			default:
				break;
			
			}
			
			if(isBurning && burnProgress>=burnInTicks) {
				isBurning = false;
				burnProgress = 0;
				
			}else if(isBurning) {
				++burnProgress;
			}
			
			if(steam > steamMaxStorage) {
				steam = steamMaxStorage;
			}
			
			if(steam < steamMaxStorage && !isBurning && inventory.getStackInSlot(inventory.getSlots()-1).getItem() == Items.COAL) {
				inventory.extractItem(inventory.getSlots()-1, 1, false);
				isBurning = true;
			}
			LaCroix.networkHandler.channel.sendToDimension(new ProgressBar(steam, pos, steamMaxStorage, level.toString()), world.provider.getDimension());
			
			if(isBurning) {
				steam = steam+steamPerSecond/20;
			}
			if(!captureSteam && steam > 0) {
				LaCroix.networkHandler.channel.sendToDimension(new HeaterMessageClient(pos), world.provider.getDimension());
				pushSteam();
			}else {
				for(BlockPos consumer : previousArray) {
					if(world.getTileEntity(consumer) != null ? world.getTileEntity(consumer) instanceof ISteamConsumer : false) {
						((ISteamConsumer)world.getTileEntity(consumer)).setSteamAccess(false);
					}
				}
			}
		}
		save();
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
		compound.setTag("inventory", inventory.serializeNBT());
		compound.setInteger("steam", steam);
		compound.setInteger("burnInTicks", burnInTicks);
		compound.setBoolean("isBurning", isBurning);
		compound.setBoolean("captureSteam", captureSteam);
		compound.setString("level", level.toString());
		
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		inventory.deserializeNBT(compound.getCompoundTag("inventory"));
		steam = compound.getInteger("steam");
		burnInTicks = compound.getInteger("burnInTicks");
		isBurning = compound.getBoolean("isBurning");
		captureSteam = compound.getBoolean("captureSteam");
		level = EnumUpgrades.safeValueOf(compound.getString("level"));

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
	
	public void setCaptureSteam(boolean cap) {
		captureSteam = cap;
		save();
	}
	
	@Override
	public boolean upgrade(EnumUpgrades upgrade) {
		if(super.upgrade(upgrade)) {
			save();
			return true;
		}
		return false;
	}

	public void pushSteam() {
		ArrayList<BlockPos> blocksVisited = new ArrayList<BlockPos>();
		ArrayList<BlockPos> consumers = new ArrayList<BlockPos>();
		
		steamPushRecursive(blocksVisited, consumers, pos);
		
		if(consumers.size() == 0) {
			if(!(world.getTileEntity(pos.up()) instanceof ISteamConsumer)) {
				steam = steam -1;
			}
		}
		
		for(BlockPos consumerPos : consumers) {
			if(world.getTileEntity(consumerPos) instanceof ISteamConsumer) {
				if(steam >= ((ISteamConsumer)world.getTileEntity(consumerPos)).getSteamUsedPerTick()) {
					steam = steam - ((ISteamConsumer)world.getTileEntity(consumerPos)).getSteamUsedPerTick();
					((ISteamConsumer)world.getTileEntity(consumerPos)).setSteamAccess(true);
					previousArray.add(consumerPos);
				}else {
					((ISteamConsumer)world.getTileEntity(consumerPos)).setSteamAccess(false);
					previousArray.remove(consumerPos);
				}
			}
		}
		if(world.getTileEntity(pos.up()) instanceof ISteamConsumer) {
			if(steam >= ((ISteamConsumer)world.getTileEntity(pos.up())).getSteamUsedPerTick()) {
				steam = steam - ((ISteamConsumer)world.getTileEntity(pos.up())).getSteamUsedPerTick();
				((ISteamConsumer)world.getTileEntity(pos.up())).setSteamAccess(true);
				previousArray.add(pos.up());
			}else {
				((ISteamConsumer)world.getTileEntity(pos.up())).setSteamAccess(false);
				previousArray.remove(pos.up());
			}
		}
		for(BlockPos previousConsumer : previousArray) {
			if(!consumers.contains(previousConsumer)) {
				if(world.getTileEntity(previousConsumer) != null) {
					if(world.getTileEntity(previousConsumer) instanceof ISteamConsumer) {
						((ISteamConsumer)world.getTileEntity(previousConsumer)).setSteamAccess(false);
					}
				}
			}
		}
		
	}
	
	private void steamPushRecursive(ArrayList<BlockPos> blocksVisited, ArrayList<BlockPos> consumers, BlockPos currentPos) {
		BlockPos down = currentPos.down();
		BlockPos up = currentPos.up();
		BlockPos north = currentPos.north();
		BlockPos south = currentPos.south();
		BlockPos east = currentPos.east();
		BlockPos west = currentPos.west();
		ArrayList<BlockPos> directions = new ArrayList<BlockPos>();
		directions.add(down); 
		directions.add(up); 
		directions.add(north); 
		directions.add(south); 
		directions.add(east); 
		directions.add(west);
		
		blocksVisited.add(currentPos);
		
		for(BlockPos direction : directions) {
			if(!blocksVisited.contains(direction) && world.getBlockState(direction).getBlock() instanceof BlockSteamPipe) {
				steamPushRecursive(blocksVisited, consumers, direction);
			} else if(!blocksVisited.contains(direction) && world.getTileEntity(direction) instanceof ISteamConsumer) {
				EnumFacing blockDirection = (direction == down) ? EnumFacing.DOWN : (direction == up) ? EnumFacing.UP : (direction == north) ? EnumFacing.NORTH : 
					(direction == south) ? EnumFacing.SOUTH  : (direction == east) ? EnumFacing.EAST  : (direction == west) ? EnumFacing.WEST : EnumFacing.UP;
				
				if(((ISteam)world.getTileEntity(direction)).canConnectOnSide(blockDirection.getOpposite())) {
					consumers.add(direction);
				}
			}
		}
		
	}

	@Override
	public boolean canConnectOnSide(EnumFacing side) {
		return true;
	}
	
	@Override
	public boolean hasSteam() {
		return steam > 0;
	}
}