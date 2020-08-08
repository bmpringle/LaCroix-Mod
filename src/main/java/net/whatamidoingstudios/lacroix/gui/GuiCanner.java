package net.whatamidoingstudios.lacroix.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.whatamidoingstudios.lacroix.LaCroix;
import net.whatamidoingstudios.lacroix.ModObjects;
import net.whatamidoingstudios.lacroix.block.EnumUpgrades;
import net.whatamidoingstudios.lacroix.block.canner.ContainerCanner;
import net.whatamidoingstudios.lacroix.block.canner.TileEntityCanner;
import net.whatamidoingstudios.lacroix.capabilities.EnergyStorageUpgradeable;
import net.whatamidoingstudios.lacroix.capabilities.FluidTankLaCroix;

public class GuiCanner extends GuiContainer {

	private static final ResourceLocation BG_TEXTURE = new ResourceLocation(LaCroix.MODID, "textures/gui/canner.png");
	private static final ResourceLocation WIDGETS = new ResourceLocation(LaCroix.MODID, "textures/gui/widgets.png");
	
	private InventoryPlayer playerInv;
	private BlockPos pos;
	
	private int fluidBarX = 20;
	private int fluidBarY = ySize/4;
	
	private int energyBarX = 40;
	private int energyBarY = ySize/4;
	
	public GuiCanner(ContainerCanner inventorySlotsIn, InventoryPlayer playerInv) {
		super(inventorySlotsIn);
		pos = inventorySlotsIn.pos;
		this.playerInv = playerInv;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1, 1, 1, 1);
		mc.getTextureManager().bindTexture(BG_TEXTURE);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
		
		TileEntityCanner te = (TileEntityCanner)mc.world.getTileEntity(pos);
		FluidTankLaCroix tank = ((FluidTankLaCroix)te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.NORTH));
		EnergyStorageUpgradeable energy = ((EnergyStorageUpgradeable)te.getCapability(CapabilityEnergy.ENERGY, EnumFacing.NORTH));
		
		int fluidamount = tank.getFluidAmount();
		int fluidcapacity = tank.getCapacity();
		int energyamount = energy.getEnergyStored();
		int energycapacity = energy.getMaxEnergyStored();
		
		
		
		int bars = (int) fluidamount/(fluidcapacity/50);
	
		mc.getTextureManager().bindTexture(WIDGETS);		
		drawTexturedModalRect(x+fluidBarX, y+fluidBarY-52/2, 126, 0, 18, 52);
		drawTexturedModalRect(x+fluidBarX+1, y+fluidBarY-52/2+51-bars, 145, 0, 16, bars);
		
		bars = Math.round(energyamount/(energycapacity/50));
		
		drawTexturedModalRect(x+energyBarX, y+energyBarY-52/2, 126, 0, 18, 52);
		drawTexturedModalRect(x+energyBarX+1, y+energyBarY-52/2+51-bars, 162, 0, 16, bars);
		
		
		fontRenderer.drawString(((TileEntityCanner)mc.world.getTileEntity(pos)).level + " " + I18n.format(ModObjects.canner.getUnlocalizedName()+".name"), x+xSize / 2 - (fontRenderer.getStringWidth(((TileEntityCanner)mc.world.getTileEntity(pos)).level + " " + "Canner")) / 2, y+6, EnumUpgrades.mcFormatcolorOfToHex(EnumUpgrades.colorOf("", ((TileEntityCanner)mc.world.getTileEntity(pos)).level)));
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		TileEntityCanner te = (TileEntityCanner)mc.world.getTileEntity(pos);
		FluidTankLaCroix tank = ((FluidTankLaCroix)te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.NORTH));
		EnergyStorageUpgradeable energy = ((EnergyStorageUpgradeable)te.getCapability(CapabilityEnergy.ENERGY, EnumFacing.NORTH));
		
		
		String fluidname = I18n.format(tank.getFluid().getFluid().getUnlocalizedName());
		int fluidamount = tank.getFluidAmount();
		int fluidcapacity = tank.getCapacity();
		int energyamount = energy.getEnergyStored();
		int energycapacity = energy.getMaxEnergyStored();
		
		if(mouseX >= guiLeft+fluidBarX && mouseX < guiLeft+fluidBarX+18) {
			if(mouseY >= guiTop+fluidBarY-52/2 && mouseY < guiTop+fluidBarY-52/2+52) {
				this.drawHoveringText((fluidamount >  0) ? fluidamount + "/" + fluidcapacity + " " + fluidname : "no fluid", mouseX, mouseY);						
			}
		}
		
		if(mouseX >= guiLeft+energyBarX && mouseX < guiLeft+energyBarX+18) {
			if(mouseY >= guiTop+energyBarY-52/2 && mouseY < guiTop+energyBarY-52/2+52) {
				this.drawHoveringText(energyamount + "/" + energycapacity + " energy", mouseX, mouseY);						
			}
		}
		this.renderHoveredToolTip(mouseX, mouseY);
	}

}
