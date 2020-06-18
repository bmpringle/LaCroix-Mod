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
import net.whatamidoingstudios.lacroix.block.steamturbine.BlockSteamTurbine;
import net.whatamidoingstudios.lacroix.block.steamturbine.TileEntityTurbine;

public class GuiSteamTurbine extends GuiScreen {
	public BlockPos pos;
	private static final ResourceLocation BG_TEXTURE = new ResourceLocation(LaCroix.MODID, "textures/gui/steamturbine.png");
	private static final ResourceLocation ENERGY_TEXTURE = new ResourceLocation(LaCroix.MODID, "textures/gui/widgets.png");
	protected int xSize = 176;
	protected int ySize = 166;
	
	public GuiSteamTurbine(int x, int y, int z) {
		pos = new BlockPos(x, y, z);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		GlStateManager.color(1, 1, 1, 1);
		mc.getTextureManager().bindTexture(BG_TEXTURE);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
		
		mc.getTextureManager().bindTexture(ENERGY_TEXTURE);
		drawTexturedModalRect((x+xSize/2)-11, y+ySize/4, 63, 0, 21, 103);
		
		float energy = ((TileEntityTurbine)Minecraft.getMinecraft().world.getTileEntity(pos)).getCapability(CapabilityEnergy.ENERGY, EnumFacing.NORTH).getEnergyStored();
		float capacity = ((TileEntityTurbine)Minecraft.getMinecraft().world.getTileEntity(pos)).getCapability(CapabilityEnergy.ENERGY, EnumFacing.NORTH).getMaxEnergyStored();
		String typename = ((TileEntityTurbine)Minecraft.getMinecraft().world.getTileEntity(pos)).level.toString();
		String name = I18n.format(new BlockSteamTurbine().getUnlocalizedName() + ".name");
		
		int bars = (int)(energy/(capacity/101));
		if(energy+(capacity/101)>capacity) {
			++bars;
		}
		
		drawTexturedModalRect((x+xSize/2)-11, y+ySize/4+(102-bars), 85, 102-bars, 21, 103-(102-bars));
		
		fontRenderer.drawString(typename + " " + name, x+xSize / 2 - (fontRenderer.getStringWidth(typename+" "+name)) / 2, y+6, EnumUpgrades.mcFormatcolorOfToHex(EnumUpgrades.colorOf("", EnumUpgrades.safeValueOf(typename))));
		
		if(mouseX >= (x+xSize/2)-11 && mouseX < (x+xSize/2)-11+21) {
			if(mouseY >= y+ySize/4 && mouseY < y+ySize/4+103) {
				this.drawHoveringText((int)energy + "/" + (int)capacity + " energy", mouseX, mouseY);
			}
		}
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
}
