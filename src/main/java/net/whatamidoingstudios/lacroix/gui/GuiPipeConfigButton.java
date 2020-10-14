package net.whatamidoingstudios.lacroix.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.math.BlockPos;
import net.whatamidoingstudios.lacroix.LaCroix;
import net.whatamidoingstudios.lacroix.block.pipes.TileEntityEnergyPipe;
import net.whatamidoingstudios.lacroix.block.pipes.TileEntityFluidPipe;
import net.whatamidoingstudios.lacroix.network.pipe.PipeMessageReturn;

public class GuiPipeConfigButton extends GuiButton {

	BlockPos pos;
	boolean state = false;
	
	
	public GuiPipeConfigButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, BlockPos pos) {
		super(buttonId, x, y, widthIn, heightIn, buttonText);
		this.pos = pos;
	}
	
	@Override
	public void playPressSound(SoundHandler soundHandlerIn) {		
		state = !state;
		
		if(Minecraft.getMinecraft().world.getTileEntity(pos) instanceof TileEntityEnergyPipe) {
			TileEntityEnergyPipe te = (TileEntityEnergyPipe) Minecraft.getMinecraft().world.getTileEntity(pos);

			switch(id) {
			case 0:
				int type = te.TYPE_UP;
				switch(te.TYPE_UP) {
				case 0:
					++type;
					break;
				case 1:
					++type;
					break;
				case 2:
					type = 0;
					break;
				}
				LaCroix.networkHandler.channel.sendToServer(new PipeMessageReturn(pos, type, te.TYPE_DOWN, te.TYPE_NORTH, te.TYPE_SOUTH, te.TYPE_EAST, te.TYPE_WEST));
				te.TYPE_UP = type;
				break;
			case 1:
				type = te.TYPE_DOWN;
				switch(te.TYPE_DOWN) {
				case 0:
					++type;
					break;
				case 1:
					++type;
					break;
				case 2:
					type = 0;
					break;
				}
				System.out.println(id + "t = "+ type);
				LaCroix.networkHandler.channel.sendToServer(new PipeMessageReturn(pos, te.TYPE_UP, type, te.TYPE_NORTH, te.TYPE_SOUTH, te.TYPE_EAST, te.TYPE_WEST));
				te.TYPE_DOWN = type;
				break;
			case 2:
				type = te.TYPE_NORTH;
				switch(te.TYPE_NORTH) {
				case 0:
					++type;
					break;
				case 1:
					++type;
					break;
				case 2:
					type = 0;
					break;
				}
				LaCroix.networkHandler.channel.sendToServer(new PipeMessageReturn(pos, te.TYPE_UP, te.TYPE_DOWN, type, te.TYPE_SOUTH, te.TYPE_EAST, te.TYPE_WEST));
				te.TYPE_NORTH = type;
				break;
			case 3:
				type = te.TYPE_SOUTH;
				switch(te.TYPE_SOUTH) {
				case 0:
					++type;
					break;
				case 1:
					++type;
					break;
				case 2:
					type = 0;
					break;
				}
				LaCroix.networkHandler.channel.sendToServer(new PipeMessageReturn(pos, te.TYPE_UP, te.TYPE_DOWN, te.TYPE_NORTH, type, te.TYPE_EAST, te.TYPE_WEST));
				te.TYPE_SOUTH = type;
				break;
			case 4:
				type = te.TYPE_EAST;
				switch(te.TYPE_EAST) {
				case 0:
					++type;
					break;
				case 1:
					++type;
					break;
				case 2:
					type = 0;
					break;
				}
				LaCroix.networkHandler.channel.sendToServer(new PipeMessageReturn(pos, te.TYPE_UP, te.TYPE_DOWN, te.TYPE_NORTH, te.TYPE_SOUTH, type, te.TYPE_WEST));
				te.TYPE_EAST = type;
				break;
			case 5:
				type = te.TYPE_WEST;
				switch(te.TYPE_WEST) {
				case 0:
					++type;
					break;
				case 1:
					++type;
					break;
				case 2:
					type = 0;
					break;
				}
				LaCroix.networkHandler.channel.sendToServer(new PipeMessageReturn(pos, te.TYPE_UP, te.TYPE_DOWN, te.TYPE_NORTH, te.TYPE_SOUTH, te.TYPE_EAST, type));
				te.TYPE_WEST = type;
				break;
			}
		}
		
		if(Minecraft.getMinecraft().world.getTileEntity(pos) instanceof TileEntityFluidPipe) {
			TileEntityFluidPipe te = (TileEntityFluidPipe) Minecraft.getMinecraft().world.getTileEntity(pos);
			switch(id) {
			case 0:
				int type = te.TYPE_UP;
				switch(te.TYPE_UP) {
				case 0:
					++type;
					break;
				case 1:
					++type;
					break;
				case 2:
					type = 0;
					break;
				}
				LaCroix.networkHandler.channel.sendToServer(new PipeMessageReturn(pos, type, te.TYPE_DOWN, te.TYPE_NORTH, te.TYPE_SOUTH, te.TYPE_EAST, te.TYPE_WEST));
				te.TYPE_UP = type;
				break;
			case 1:
				type = te.TYPE_DOWN;
				switch(te.TYPE_DOWN) {
				case 0:
					++type;
					break;
				case 1:
					++type;
					break;
				case 2:
					type = 0;
					break;
				}
				LaCroix.networkHandler.channel.sendToServer(new PipeMessageReturn(pos, te.TYPE_UP, type, te.TYPE_NORTH, te.TYPE_SOUTH, te.TYPE_EAST, te.TYPE_WEST));
				te.TYPE_DOWN = type;
				break;
			case 2:
				type = te.TYPE_NORTH;
				switch(te.TYPE_NORTH) {
				case 0:
					++type;
					break;
				case 1:
					++type;
					break;
				case 2:
					type = 0;
					break;
				}
				LaCroix.networkHandler.channel.sendToServer(new PipeMessageReturn(pos, te.TYPE_UP, te.TYPE_DOWN, type, te.TYPE_SOUTH, te.TYPE_EAST, te.TYPE_WEST));
				te.TYPE_NORTH = type;
				break;
			case 3:
				type = te.TYPE_SOUTH;
				switch(te.TYPE_SOUTH) {
				case 0:
					++type;
					break;
				case 1:
					++type;
					break;
				case 2:
					type = 0;
					break;
				}
				LaCroix.networkHandler.channel.sendToServer(new PipeMessageReturn(pos, te.TYPE_UP, te.TYPE_DOWN, te.TYPE_NORTH, type, te.TYPE_EAST, te.TYPE_WEST));
				te.TYPE_SOUTH = type;
				break;
			case 4:
				type = te.TYPE_EAST;
				switch(te.TYPE_EAST) {
				case 0:
					++type;
					break;
				case 1:
					++type;
					break;
				case 2:
					type = 0;
					break;
				}
				LaCroix.networkHandler.channel.sendToServer(new PipeMessageReturn(pos, te.TYPE_UP, te.TYPE_DOWN, te.TYPE_NORTH, te.TYPE_SOUTH, type, te.TYPE_WEST));
				te.TYPE_EAST = type;
				break;
			case 5:
				type = te.TYPE_WEST;
				switch(te.TYPE_WEST) {
				case 0:
					++type;
					break;
				case 1:
					++type;
					break;
				case 2:
					type = 0;
					break;
				}
				LaCroix.networkHandler.channel.sendToServer(new PipeMessageReturn(pos, te.TYPE_UP, te.TYPE_DOWN, te.TYPE_NORTH, te.TYPE_SOUTH, te.TYPE_EAST, type));
				te.TYPE_WEST = type;
				break;
			}
		
		}
		super.playPressSound(soundHandlerIn);
	}
}
