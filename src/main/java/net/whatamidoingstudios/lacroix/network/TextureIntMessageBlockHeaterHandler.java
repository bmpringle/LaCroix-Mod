package net.whatamidoingstudios.lacroix.network;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.whatamidoingstudios.lacroix.ModObjects;
import net.whatamidoingstudios.lacroix.block.heater.BlockHeater;
import net.whatamidoingstudios.lacroix.block.heater.EnumUpgrades;
import net.whatamidoingstudios.lacroix.block.heater.TileEntityHeater;
import net.whatamidoingstudios.lacroix.item.ItemBlockHeater;

public class TextureIntMessageBlockHeaterHandler implements IMessageHandler<TextureIntMessage, IMessage> {

	@Override
	public IMessage onMessage(TextureIntMessage message, MessageContext ctx) {
		if(Minecraft.getMinecraft().world.getTileEntity(message.pos) instanceof TileEntityHeater) {	
			switch(message.texture) {
			case 0:
				((TileEntityHeater)Minecraft.getMinecraft().world.getTileEntity(message.pos)).level = EnumUpgrades.Basic;
				break;
			case 1:
				((TileEntityHeater)Minecraft.getMinecraft().world.getTileEntity(message.pos)).level = EnumUpgrades.Advanced;
				break;
			case 2:
				((TileEntityHeater)Minecraft.getMinecraft().world.getTileEntity(message.pos)).level = EnumUpgrades.Excellent;
				break;
			case 3:
				((TileEntityHeater)Minecraft.getMinecraft().world.getTileEntity(message.pos)).level = EnumUpgrades.Perfect;
				break;
			default:
				break;
			}
		}
		((TileEntityHeater)Minecraft.getMinecraft().world.getTileEntity(message.pos)).save();
		return null;
	}

}
