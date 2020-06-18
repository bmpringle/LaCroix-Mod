package net.whatamidoingstudios.lacroix.network.heater;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.whatamidoingstudios.lacroix.gui.GuiHeater;

public class ProgressBarHandler implements IMessageHandler<ProgressBar, IMessage> {

	@Override
	public IMessage onMessage(ProgressBar message, MessageContext ctx) {
		if(Minecraft.getMinecraft().currentScreen instanceof GuiHeater) {			
			int x1 = ((GuiHeater)Minecraft.getMinecraft().currentScreen).blockpos.getX();
			int y1 = ((GuiHeater)Minecraft.getMinecraft().currentScreen).blockpos.getY();
			int z1 = ((GuiHeater)Minecraft.getMinecraft().currentScreen).blockpos.getZ();
			int x = message.pos.getX();
			int y = message.pos.getY();
			int z  = message.pos.getZ();
			
			if(x == x1 && y == y1 && z == z1) {
				((GuiHeater)Minecraft.getMinecraft().currentScreen).steam = message.steam;
				((GuiHeater)Minecraft.getMinecraft().currentScreen).maxSteam = message.maxsteam;
				((GuiHeater)Minecraft.getMinecraft().currentScreen).typename = message.typename;
			}
		}
		return null;
	}

}
