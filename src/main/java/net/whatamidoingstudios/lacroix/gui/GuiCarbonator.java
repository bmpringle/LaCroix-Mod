package net.whatamidoingstudios.lacroix.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.energy.CapabilityEnergy;
import net.whatamidoingstudios.lacroix.LaCroix;
import net.whatamidoingstudios.lacroix.block.EnumUpgrades;
import net.whatamidoingstudios.lacroix.block.carbonator.BlockCarbonator;
import net.whatamidoingstudios.lacroix.block.carbonator.TileEntityCarbonator;
import net.whatamidoingstudios.lacroix.block.steamturbine.BlockSteamTurbine;

public class GuiCarbonator extends GuiScreen {
	public BlockPos pos;
	private static final ResourceLocation BG_TEXTURE = new ResourceLocation(LaCroix.MODID, "textures/gui/carbonator.png");
	private static final ResourceLocation WIDGETS = new ResourceLocation(LaCroix.MODID, "textures/gui/widgets.png");
	protected int xSize = 176;
	protected int ySize = 166;
	
	public GuiCarbonator(int x, int y, int z) {
		pos = new BlockPos(x, y, z);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		GlStateManager.color(1, 1, 1, 1);
		mc.getTextureManager().bindTexture(BG_TEXTURE);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
		
		float energy = ((TileEntityCarbonator)Minecraft.getMinecraft().world.getTileEntity(pos)).getCapability(CapabilityEnergy.ENERGY, EnumFacing.NORTH).getEnergyStored();
		float capacity = ((TileEntityCarbonator)Minecraft.getMinecraft().world.getTileEntity(pos)).getCapability(CapabilityEnergy.ENERGY, EnumFacing.NORTH).getMaxEnergyStored();
		
		float carbonatedWater = ((TileEntityCarbonator)Minecraft.getMinecraft().world.getTileEntity(pos)).fluidHandlerCarbonatedWater.getFluidAmount();
		
		String typename = ((TileEntityCarbonator)Minecraft.getMinecraft().world.getTileEntity(pos)).level.toString();
		String name = I18n.format(new BlockCarbonator().getUnlocalizedName() + ".name");
		
		fontRenderer.drawString(typename + " " + name, x+xSize / 2 - (fontRenderer.getStringWidth(typename+" "+name)) / 2, y+6, EnumUpgrades.mcFormatcolorOfToHex(EnumUpgrades.colorOf("", EnumUpgrades.safeValueOf(typename))));
		
		GlStateManager.color(1, 1, 1, 1);
		
		mc.getTextureManager().bindTexture(WIDGETS);
		
		//steam on/off
		if(mc.world.getTileEntity(pos) instanceof TileEntityCarbonator) {
			if(((TileEntityCarbonator)mc.world.getTileEntity(pos)).hasSteam()) {
				drawTexturedModalRect(x+20, y+ySize/2-9, 107, 19, 18, 18);
			}else {
				drawTexturedModalRect(x+20, y+ySize/2-9, 107, 0, 18, 18);
			}
		}else {
			drawTexturedModalRect(x+20, y+ySize/2-9, 107, 0, 18, 18);
		}	
		
		//water bar
		
		int bars = (int) ((TileEntityCarbonator)Minecraft.getMinecraft().world.getTileEntity(pos)).fluidHandlerWater.getFluidAmount()/(((TileEntityCarbonator)Minecraft.getMinecraft().world.getTileEntity(pos)).liquidCapacityPerFluid/50);
		
		drawTexturedModalRect(x+60, y+ySize/2-52/2, 126, 0, 18, 52);
		drawTexturedModalRect(x+60+1, y+ySize/2-52/2+51-bars, 145, 0, 16, bars);
		
		//energy bar
		
		bars = Math.round(energy/(capacity/50));
		
		drawTexturedModalRect(x+100, y+ySize/2-52/2, 126, 0, 18, 52);
		drawTexturedModalRect(x+100+1, y+ySize/2-52/2+51-bars, 162, 0, 16, bars);
		
		//carbonated water bar
		
		bars = Math.round(carbonatedWater/(((TileEntityCarbonator)Minecraft.getMinecraft().world.getTileEntity(pos)).liquidCapacityPerFluid/50));
		
		drawTexturedModalRect(x+140, y+ySize/2-52/2, 126, 0, 18, 52);
		drawTexturedModalRect(x+140+1, y+ySize/2-52/2+51-bars, 179, 0, 16, bars);
		
		if(mouseX >= x+20 && mouseX < x+38 && mouseY >= y+ySize/2-9 && mouseY < y+ySize/2+9) {
			this.drawHoveringText("steam is currently " + ((((TileEntityCarbonator)mc.world.getTileEntity(pos)).hasSteam()) ? "provided" : "not provided") + " to the carbonator.", mouseX, mouseY);
		}
		
		if(mouseX >= x+60 && mouseX < x+78 && mouseY >= y+ySize/2-52/2 && mouseY < y+ySize/2-52/2+52) {
			this.drawHoveringText(((TileEntityCarbonator)Minecraft.getMinecraft().world.getTileEntity(pos)).fluidHandlerWater.getFluidAmount() + "/" + ((TileEntityCarbonator)Minecraft.getMinecraft().world.getTileEntity(pos)).liquidCapacityPerFluid + " water", mouseX, mouseY);
		}
		
		if(mouseX >= x+100 && mouseX < x+118 && mouseY >= y+ySize/2-52/2 && mouseY < y+ySize/2-52/2+52) {
			this.drawHoveringText((int)energy + "/" + (int)capacity + " energy", mouseX, mouseY);
		}
		
		if(mouseX >= x+140 && mouseX < x+158 && mouseY >= y+ySize/2-52/2 && mouseY < y+ySize/2-52/2+52) {
			this.drawHoveringText((int)carbonatedWater + "/" + (int)((TileEntityCarbonator)Minecraft.getMinecraft().world.getTileEntity(pos)).liquidCapacityPerFluid + " carbonated water", mouseX, mouseY);
		}
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
}
