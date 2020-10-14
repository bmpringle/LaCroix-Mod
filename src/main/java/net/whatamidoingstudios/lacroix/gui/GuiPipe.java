package net.whatamidoingstudios.lacroix.gui;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.BlockPos;
import net.whatamidoingstudios.lacroix.LaCroix;
import net.whatamidoingstudios.lacroix.block.pipes.TileEntityEnergyPipe;
import net.whatamidoingstudios.lacroix.block.pipes.TileEntityFluidPipe;
import net.whatamidoingstudios.lacroix.block.utils.NumToIO;
import net.whatamidoingstudios.lacroix.network.pipe.PipeMessageReturn;

public class GuiPipe extends GuiScreen {
	
	BlockPos pos = new BlockPos(0, 0, 0);
	
	GuiPipeConfigButton UP;
	GuiPipeConfigButton DOWN;
	GuiPipeConfigButton NORTH;
	GuiPipeConfigButton SOUTH;
	GuiPipeConfigButton EAST;
	GuiPipeConfigButton WEST;
	
	public GuiPipe(int x, int y, int z) {
		pos = new BlockPos(x, y, z);
	}
	
	@Override
	public void initGui() {
		super.initGui();
		int _x = (width - 176) / 2;
		int _y = (height - 166) / 2;
		UP = new GuiPipeConfigButton(0, _x+52, _y, 36, 20, "UP", pos);
		DOWN = new GuiPipeConfigButton(1, _x+52, _y+27, 36, 20, "DOWN", pos);
		NORTH = new GuiPipeConfigButton(2, _x+52, _y+54, 36, 20, "NORTH", pos);
		SOUTH = new GuiPipeConfigButton(3, _x+52, _y+81, 36, 20, "SOUTH", pos);
		EAST = new GuiPipeConfigButton(4, _x+52, _y+108, 36, 20, "EAST", pos);
		WEST = new GuiPipeConfigButton(5, _x+52, _y+135, 36, 20, "WEST", pos);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		GlStateManager.color(1, 1, 1, 1);
		buttonList.clear();
		buttonList.add(UP);
		buttonList.add(DOWN);
		buttonList.add(NORTH);
		buttonList.add(SOUTH);
		buttonList.add(EAST);
		buttonList.add(WEST);
		
		for(GuiButton button : buttonList) {
			button.drawButton(mc, mouseX, mouseY, partialTicks);
		}


		int _x = (width - 176) / 2;
		int _y = (height - 166) / 2;
		
		if(Minecraft.getMinecraft().world.getTileEntity(pos) instanceof TileEntityEnergyPipe) {
			TileEntityEnergyPipe te = (TileEntityEnergyPipe) Minecraft.getMinecraft().world.getTileEntity(pos);
			this.drawHoveringText(": " + NumToIO.numToIO(te.TYPE_UP), _x+81, _y+18);
			this.drawHoveringText(": " + NumToIO.numToIO(te.TYPE_DOWN), _x+81, _y+45);
			this.drawHoveringText(": " + NumToIO.numToIO(te.TYPE_NORTH), _x+81, _y+72);
			this.drawHoveringText(": " + NumToIO.numToIO(te.TYPE_SOUTH), _x+81, _y+99);
			this.drawHoveringText(": " + NumToIO.numToIO(te.TYPE_EAST), _x+81, _y+126);
			this.drawHoveringText(": " + NumToIO.numToIO(te.TYPE_WEST), _x+81, _y+153);
		}
		
		if(Minecraft.getMinecraft().world.getTileEntity(pos) instanceof TileEntityFluidPipe) {
			TileEntityFluidPipe te = (TileEntityFluidPipe) Minecraft.getMinecraft().world.getTileEntity(pos);
			this.drawHoveringText(": " + NumToIO.numToIO(te.TYPE_UP), _x+81, _y+18);
			this.drawHoveringText(": " + NumToIO.numToIO(te.TYPE_DOWN), _x+81, _y+45);
			this.drawHoveringText(": " + NumToIO.numToIO(te.TYPE_NORTH), _x+81, _y+72);
			this.drawHoveringText(": " + NumToIO.numToIO(te.TYPE_SOUTH), _x+81, _y+99);
			this.drawHoveringText(": " + NumToIO.numToIO(te.TYPE_EAST), _x+81, _y+126);
			this.drawHoveringText(": " + NumToIO.numToIO(te.TYPE_WEST), _x+81, _y+153);
		}
	}
}
