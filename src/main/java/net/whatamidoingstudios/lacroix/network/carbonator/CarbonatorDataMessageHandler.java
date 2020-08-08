package net.whatamidoingstudios.lacroix.network.carbonator;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.whatamidoingstudios.lacroix.block.EnumUpgrades;
import net.whatamidoingstudios.lacroix.block.carbonator.TileEntityCarbonator;
import net.whatamidoingstudios.lacroix.capabilities.EnergyStorageUpgradeable;

public class CarbonatorDataMessageHandler implements IMessageHandler<CarbonatorDataMessage, IMessage> {

	@Override
	public IMessage onMessage(CarbonatorDataMessage message, MessageContext ctx) {
		if ((Minecraft.getMinecraft().world.getTileEntity(message.pos) !=  null) ? (Minecraft.getMinecraft().world.getTileEntity(message.pos) instanceof TileEntityCarbonator) : false)  {
			
			if(((TileEntityCarbonator)(Minecraft.getMinecraft().world.getTileEntity(message.pos))).fluidHandlerCarbonatedWater.getFluidAmount() > message.carbonatedWater) {
				((TileEntityCarbonator)(Minecraft.getMinecraft().world.getTileEntity(message.pos))).fluidHandlerCarbonatedWater.drain(((TileEntityCarbonator)(Minecraft.getMinecraft().world.getTileEntity(message.pos))).fluidHandlerCarbonatedWater.getFluidAmount()-message.carbonatedWater, true);
			}else {
				((TileEntityCarbonator)(Minecraft.getMinecraft().world.getTileEntity(message.pos))).fluidHandlerCarbonatedWater.fill(new FluidStack(FluidRegistry.getFluid("purefluid"), message.carbonatedWater-((TileEntityCarbonator)(Minecraft.getMinecraft().world.getTileEntity(message.pos))).fluidHandlerCarbonatedWater.getFluidAmount()), true);
			}
			
			if(((TileEntityCarbonator)(Minecraft.getMinecraft().world.getTileEntity(message.pos))).fluidHandlerWater.getFluidAmount() > message.water) {
				((TileEntityCarbonator)(Minecraft.getMinecraft().world.getTileEntity(message.pos))).fluidHandlerWater.drain(((TileEntityCarbonator)(Minecraft.getMinecraft().world.getTileEntity(message.pos))).fluidHandlerWater.getFluidAmount()-message.water, true);
			}else {
				((TileEntityCarbonator)(Minecraft.getMinecraft().world.getTileEntity(message.pos))).fluidHandlerWater.fill(new FluidStack(FluidRegistry.WATER, message.water-((TileEntityCarbonator)(Minecraft.getMinecraft().world.getTileEntity(message.pos))).fluidHandlerWater.getFluidAmount()), true);
			}
			((TileEntityCarbonator)(Minecraft.getMinecraft().world.getTileEntity(message.pos))).setSteamAccess(message.steamAccess);
			((TileEntityCarbonator)(Minecraft.getMinecraft().world.getTileEntity(message.pos))).level = EnumUpgrades.enumFromInt(message.level);
			((EnergyStorageUpgradeable)((TileEntityCarbonator)(Minecraft.getMinecraft().world.getTileEntity(message.pos))).getCapability(CapabilityEnergy.ENERGY, EnumFacing.NORTH)).increaseCapacity(message.maxEnergy);
			((EnergyStorageUpgradeable)((TileEntityCarbonator)(Minecraft.getMinecraft().world.getTileEntity(message.pos))).getCapability(CapabilityEnergy.ENERGY, EnumFacing.NORTH)).setEnergy(message.energy);
		}
		return null;
	}
}
