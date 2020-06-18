package net.whatamidoingstudios.lacroix.network.heater;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.whatamidoingstudios.lacroix.block.heater.BlockHeater;
import net.whatamidoingstudios.lacroix.block.heater.TileEntityHeater;
import net.whatamidoingstudios.lacroix.gui.GuiHeater;

public class HeaterMessageServerResponseHandler implements IMessageHandler<HeaterMessageServerResponse, IMessage> {

	@Override
	public IMessage onMessage(HeaterMessageServerResponse message, MessageContext ctx) {
		if(Minecraft.getMinecraft().currentScreen instanceof GuiHeater) {
			((GuiHeater)Minecraft.getMinecraft().currentScreen).modifyButtonInList(message.textToggle); 
		}
		return null;
	}

}
