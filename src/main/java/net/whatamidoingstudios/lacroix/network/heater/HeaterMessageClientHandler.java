package net.whatamidoingstudios.lacroix.network.heater;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class HeaterMessageClientHandler implements IMessageHandler<HeaterMessageClient, IMessage> {

	@Override
	public IMessage onMessage(HeaterMessageClient message, MessageContext ctx) {
		
		Minecraft.getMinecraft().addScheduledTask(() -> {
			Minecraft.getMinecraft().world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, message.x + 0.5D, message.y + 1.0D, message.z + 0.5D, 0.0D, 0.0D, 0.0D);
		});	
		return null;
	}

}
