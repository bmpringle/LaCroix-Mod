package net.whatamidoingstudios.lacroix.network.canner;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.whatamidoingstudios.lacroix.block.EnumUpgrades;
import net.whatamidoingstudios.lacroix.block.canner.TileEntityCanner;
import net.whatamidoingstudios.lacroix.capabilities.FluidTankLaCroix;
import net.whatamidoingstudios.lacroix.capabilities.EnergyStorageUpgradeable;

public class CannerMessageHandler implements IMessageHandler<CannerMessage, IMessage> {

	@Override
	public IMessage onMessage(CannerMessage message, MessageContext ctx) {
		if(Minecraft.getMinecraft().world.getTileEntity(message.pos) instanceof TileEntityCanner) {
			if(FluidRegistry.getFluid(message.fluidname) != null) {
				((FluidTankLaCroix)((TileEntityCanner)Minecraft.getMinecraft().world.getTileEntity(message.pos)).getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.NORTH)).setFluid(new FluidStack(FluidRegistry.getFluid(message.fluidname), message.amount));
			}else {
				((FluidTankLaCroix)((TileEntityCanner)Minecraft.getMinecraft().world.getTileEntity(message.pos)).getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.NORTH)).setFluid(new FluidStack(FluidRegistry.WATER, 0));
			}
			((TileEntityCanner)Minecraft.getMinecraft().world.getTileEntity(message.pos)).resetEnergyStorage(message.energyStored, message.energyCapacity);
			((TileEntityCanner)Minecraft.getMinecraft().world.getTileEntity(message.pos)).level = EnumUpgrades.enumFromInt(message.type);
		}
		return null;
	}

}
