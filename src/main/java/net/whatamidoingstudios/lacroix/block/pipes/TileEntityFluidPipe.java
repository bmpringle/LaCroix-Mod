package net.whatamidoingstudios.lacroix.block.pipes;

import java.util.ArrayList;
import java.util.Hashtable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.whatamidoingstudios.lacroix.LaCroix;
import net.whatamidoingstudios.lacroix.capabilities.FluidTankLaCroix;
import net.whatamidoingstudios.lacroix.network.pipe.PipeMessage;

public class TileEntityFluidPipe extends TileEntity implements ITickable {
	
	FluidTankLaCroix fluidHandler = new FluidTankLaCroix(FluidRegistry.WATER, 0, 800);
	
	/*
	 * 0: normal
	 * 1: output
	 * 2: input
	 */
	
	public int TYPE_NORTH = 0;
	public int TYPE_SOUTH = 0;
	public int TYPE_EAST = 0;
	public int TYPE_WEST = 0;
	public int TYPE_UP = 0;
	public int TYPE_DOWN = 0;
	
	public void toggleType(EnumFacing facing) {
		switch(facing) {
		case DOWN:
			if(TYPE_DOWN == 0) {
				TYPE_DOWN = 1;
			}else if(TYPE_DOWN == 1) {
				TYPE_DOWN = 2;
			}else if(TYPE_DOWN == 2) {
				TYPE_DOWN = 0;
			}
			break;
		case EAST:
			if(TYPE_EAST == 0) {
				TYPE_EAST = 1;
			}else if(TYPE_EAST == 1) {
				TYPE_EAST = 2;
			}else if(TYPE_EAST == 2) {
				TYPE_EAST = 0;
			}
			break;
		case NORTH:
			if(TYPE_NORTH == 0) {
				TYPE_NORTH = 1;
			}else if(TYPE_NORTH == 1) {
				TYPE_NORTH = 2;
			}else if(TYPE_NORTH == 2) {
				TYPE_NORTH = 0;
			}
			break;
		case SOUTH:
			if(TYPE_SOUTH == 0) {
				TYPE_SOUTH = 1;
			}else if(TYPE_SOUTH == 1) {
				TYPE_SOUTH = 2;
			}else if(TYPE_SOUTH == 2) {
				TYPE_SOUTH = 0;
			}
			break;
		case UP:
			if(TYPE_UP == 0) {
				TYPE_UP = 1;
			}else if(TYPE_UP == 1) {
				TYPE_UP = 2;
			}else if(TYPE_UP == 2) {
				TYPE_UP = 0;
			}
			break;
		case WEST:
			if(TYPE_WEST == 0) {
				TYPE_WEST = 1;
			}else if(TYPE_WEST == 1) {
				TYPE_WEST = 2;
			}else if(TYPE_WEST == 2) {
				TYPE_WEST = 0;
			}
			break;
		default:
			break;
		
		}
	}
	
	public void setType(EnumFacing facing, int type) {
		switch(facing) {
		case DOWN:
			TYPE_DOWN = type;
			break;
		case EAST:
			TYPE_EAST = type;
			break;
		case NORTH:
			TYPE_NORTH = type;
			break;
		case SOUTH:
			TYPE_SOUTH = type;
			break;
		case UP:
			TYPE_UP = type;
			break;
		case WEST:
			TYPE_WEST = type;
			break;
		default:
			break;
		
		}
	}
	/*
	 * 0: normal
	 * 1: output
	 * 2: input
	 */	
	@Override
	public void update() {
		if(!world.isRemote) {
			if(fluidHandler.getFluidAmount() == 0) {
				fluidHandler.setFluid(null);
			}
			
			ArrayList<BlockPos> outputs = getOutputs();
			int fPer = (outputs.size() > 0) ? fluidHandler.getFluidAmount()/outputs.size() : 0;
			
			if(fluidHandler.getFluid() == null) {
				fPer = 0;
			}
			
			for(BlockPos output : outputs) {
				if(fPer == 0) {
					break;
				}
				fluidHandler.drain(((TileEntityFluidPipe) world.getTileEntity(output)).privateGetfluidHandler().fill(new FluidStack(fluidHandler.getFluid().getFluid(),  fPer), true), true);
			}
			
			if(fluidHandler.getFluid() == null) {
				fPer = 0;
			}
			
			for(BlockPos output : outputs) {
				if(fPer == 0) {
					break;
				}
				fluidHandler.drain(((TileEntityFluidPipe) world.getTileEntity(output)).privateGetfluidHandler().fill(new FluidStack(fluidHandler.getFluid().getFluid(),  fluidHandler.getFluidAmount()), true), true);
			}
			
			Hashtable<BlockPos, EnumFacing> outputsO = new Hashtable<BlockPos, EnumFacing>();
			
			if(TYPE_UP == 1) {
				if((world.getTileEntity(pos.up()) != null) ? world.getTileEntity(pos.up()).hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.DOWN) : false) {
					outputsO.put(pos.up(), EnumFacing.DOWN);
				}
			}
			if(TYPE_DOWN == 1) {
				if((world.getTileEntity(pos.down()) != null) ? world.getTileEntity(pos.down()).hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.UP) : false) {
					outputsO.put(pos.down(), EnumFacing.UP);
				}
			}
			if(TYPE_NORTH == 1) {
				if((world.getTileEntity(pos.north()) != null) ? world.getTileEntity(pos.north()).hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.SOUTH) : false) {
					outputsO.put(pos.north(), EnumFacing.SOUTH);
				}
			}
			if(TYPE_SOUTH == 1) {
				if((world.getTileEntity(pos.south()) != null) ? world.getTileEntity(pos.south()).hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.NORTH) : false) {
					outputsO.put(pos.south(), EnumFacing.NORTH);
				}
			}
			if(TYPE_EAST == 1) {
				if((world.getTileEntity(pos.east()) != null) ? world.getTileEntity(pos.east()).hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.WEST) : false) {
					outputsO.put(pos.east(), EnumFacing.WEST);
				}
			}
			if(TYPE_WEST == 1) {
				if((world.getTileEntity(pos.west()) != null) ? world.getTileEntity(pos.west()).hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.EAST) : false) {
					outputsO.put(pos.west(), EnumFacing.EAST);
				}
			}
 
			int fPerO = (outputsO.size() > 0) ? fluidHandler.getFluidAmount()/outputsO.size() : 0;
			
			if(fluidHandler.getFluid() == null) {
				fPerO = 0;
			}
			
			if(fPerO == 0) {
				return;
			}
			
			for(BlockPos output : outputsO.keySet()) {
				fluidHandler.drain((world.getTileEntity(output)).getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, outputsO.get(output)).fill(new FluidStack(fluidHandler.getFluid().getFluid(), fPer), true), true);
			}
			
			if(fluidHandler.getFluid() == null) {
				fPerO = 0;
			}
			
			if(fPerO == 0) {
				return;
			}
			
			for(BlockPos output : outputsO.keySet()) {
				fluidHandler.drain((world.getTileEntity(output)).getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, outputsO.get(output)).fill(new FluidStack(fluidHandler.getFluid().getFluid(), fluidHandler.getFluidAmount()), true), true);
			}
			
			LaCroix.networkHandler.channel.sendToDimension(new PipeMessage(pos, TYPE_UP, TYPE_DOWN, TYPE_NORTH, TYPE_SOUTH, TYPE_EAST, TYPE_WEST), world.provider.getDimension());
		}
	}

	private ArrayList<BlockPos> getOutputs() {
		ArrayList<BlockPos> outputs = new ArrayList<BlockPos>();
		ArrayList<BlockPos> visitedPipes = new ArrayList<BlockPos>();
		getOutputsRecursive(outputs, visitedPipes, pos);
		
		if(TYPE_DOWN == 1 || TYPE_UP == 1 || TYPE_NORTH == 1 || TYPE_SOUTH == 1 || TYPE_EAST == 1 || TYPE_WEST == 1) {			
			outputs.add(pos);
		}
		return outputs;
	}
	
	private void getOutputsRecursive(ArrayList<BlockPos> outputs, ArrayList<BlockPos> visitedPipes, BlockPos pos) {
		if(visitedPipes.contains(pos)) {
			return;
		}
		
		visitedPipes.add(pos);
		
		TileEntityFluidPipe up = world.getTileEntity(pos.up()) != null ? (world.getTileEntity(pos.up()) instanceof TileEntityFluidPipe ? (TileEntityFluidPipe)(world.getTileEntity(pos.up())) : null) : null;
		boolean upIndicator = !(up == null);
		TileEntityFluidPipe down = world.getTileEntity(pos.down()) != null ? (world.getTileEntity(pos.down()) instanceof TileEntityFluidPipe ? (TileEntityFluidPipe)(world.getTileEntity(pos.down())) : null) : null;
		boolean downIndicator = !(down == null);
		TileEntityFluidPipe north = world.getTileEntity(pos.north()) != null ? (world.getTileEntity(pos.north()) instanceof TileEntityFluidPipe ? (TileEntityFluidPipe)(world.getTileEntity(pos.north())) : null) : null;
		boolean northIndicator = !(north == null);
		TileEntityFluidPipe south = world.getTileEntity(pos.south()) != null ? (world.getTileEntity(pos.south()) instanceof TileEntityFluidPipe ? (TileEntityFluidPipe)(world.getTileEntity(pos.south())) : null) : null;
		boolean southIndicator = !(south == null);
		TileEntityFluidPipe east = world.getTileEntity(pos.east()) != null ? (world.getTileEntity(pos.east()) instanceof TileEntityFluidPipe ? (TileEntityFluidPipe)(world.getTileEntity(pos.east())) : null) : null;
		boolean eastIndicator = !(east == null);
		TileEntityFluidPipe west = world.getTileEntity(pos.west()) != null ? (world.getTileEntity(pos.west()) instanceof TileEntityFluidPipe ? (TileEntityFluidPipe)(world.getTileEntity(pos.west())) : null) : null;
		boolean westIndicator = !(west == null);
		
		if(upIndicator) {
			if(!visitedPipes.contains(pos.up())) {
				if(up.TYPE_DOWN == 1 || up.TYPE_UP == 1 || up.TYPE_NORTH == 1 || up.TYPE_SOUTH == 1 || up.TYPE_EAST == 1 || up.TYPE_WEST == 1) {			
					outputs.add(pos.up());
				}
				getOutputsRecursive(outputs, visitedPipes, pos.up());
			}
		}
		if(downIndicator) {
			if(!visitedPipes.contains(pos.down())) {
				if(down.TYPE_DOWN == 1 || down.TYPE_UP == 1 || down.TYPE_NORTH == 1 || down.TYPE_SOUTH == 1 || down.TYPE_EAST == 1 || down.TYPE_WEST == 1) {			
					outputs.add(pos.down());
				}
				getOutputsRecursive(outputs, visitedPipes, pos.down());
			}
		}
		if(northIndicator) {
			if(!visitedPipes.contains(pos.north())) {
				if(north.TYPE_DOWN == 1 || north.TYPE_UP == 1 || north.TYPE_NORTH == 1 || north.TYPE_SOUTH == 1 || north.TYPE_EAST == 1 || north.TYPE_WEST == 1) {			
					outputs.add(pos.north());
				}
				getOutputsRecursive(outputs, visitedPipes, pos.north());
			}
		}
		if(southIndicator) {
			if(!visitedPipes.contains(pos.south())) {
				if(south.TYPE_DOWN == 1 || south.TYPE_UP == 1 || south.TYPE_NORTH == 1 || south.TYPE_SOUTH == 1 || south.TYPE_EAST == 1 || south.TYPE_WEST == 1) {			
					outputs.add(pos.south());
				}
				getOutputsRecursive(outputs, visitedPipes, pos.south());
			}
		}
		if(eastIndicator) {
			if(!visitedPipes.contains(pos.east())) {
				if(east.TYPE_DOWN == 1 || east.TYPE_UP == 1 || east.TYPE_NORTH == 1 || east.TYPE_SOUTH == 1 || east.TYPE_EAST == 1 || east.TYPE_WEST == 1) {			
					outputs.add(pos.east());
				}
				getOutputsRecursive(outputs, visitedPipes, pos.east());
			}
		}
		if(westIndicator) {
			if(!visitedPipes.contains(pos.west())) {
				if(west.TYPE_DOWN == 1 || west.TYPE_UP == 1 || west.TYPE_NORTH == 1 || west.TYPE_SOUTH == 1 || west.TYPE_EAST == 1 || west.TYPE_WEST == 1) {			
					outputs.add(pos.west());
				}
				getOutputsRecursive(outputs, visitedPipes, pos.west());
			}
		}
	}
	
	//for internal use
	public FluidTankLaCroix privateGetfluidHandler() {
		return fluidHandler;
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		switch(facing) {
		case DOWN:
			if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && TYPE_DOWN == 2) {
				return true;
			}
			break;
		case EAST:
			if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && TYPE_EAST == 2) {
				return true;
			}
			break;
		case NORTH:
			if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && TYPE_NORTH == 2) {
				return true;
			}
			break;
		case SOUTH:
			if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && TYPE_SOUTH == 2) {
				return true;
			}
			break;
		case UP:
			if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && TYPE_UP == 2) {
				return true;
			}
			break;
		case WEST:
			if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && TYPE_WEST == 2) {
				return true;
			}
			break;
		default:
			break;
		
		}
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		switch(facing) {
		case DOWN:
			if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && TYPE_DOWN == 2) {
				return (T)fluidHandler;
			}
			break;
		case EAST:
			if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && TYPE_EAST == 2) {
				return (T)fluidHandler;
			}
			break;
		case NORTH:
			if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && TYPE_NORTH == 2) {
				return (T)fluidHandler;
			}
			break;
		case SOUTH:
			if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && TYPE_SOUTH == 2) {
				return (T)fluidHandler;
			}
			break;
		case UP:
			if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && TYPE_UP == 2) {
				return (T)fluidHandler;
			}
			break;
		case WEST:
			if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && TYPE_WEST == 2) {
				return (T)fluidHandler;
			}
			break;
		default:
			break;
		
		}
		return super.getCapability(capability, facing);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		fluidHandler.readFromNBT(compound);
		
		TYPE_NORTH = compound.getInteger("TYPE_NORTH");
		TYPE_SOUTH = compound.getInteger("TYPE_SOUTH");
		TYPE_EAST = compound.getInteger("TYPE_EAST");
		TYPE_WEST = compound.getInteger("TYPE_WEST");
		TYPE_UP = compound.getInteger("TYPE_UP");
		TYPE_DOWN = compound.getInteger("TYPE_DOWN");
		super.readFromNBT(compound);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		fluidHandler.writeToNBT(compound);
		compound.setInteger("TYPE_NORTH", TYPE_NORTH);
		compound.setInteger("TYPE_SOUTH", TYPE_SOUTH);
		compound.setInteger("TYPE_EAST", TYPE_EAST);
		compound.setInteger("TYPE_WEST", TYPE_WEST);
		compound.setInteger("TYPE_UP", TYPE_UP);
		compound.setInteger("TYPE_DOWN", TYPE_DOWN);
		return super.writeToNBT(compound);
	}
}
