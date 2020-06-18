package net.whatamidoingstudios.lacroix.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.whatamidoingstudios.lacroix.network.heater.HeaterMessageClient;
import net.whatamidoingstudios.lacroix.network.heater.HeaterMessageClientHandler;
import net.whatamidoingstudios.lacroix.network.heater.HeaterMessageServer;
import net.whatamidoingstudios.lacroix.network.heater.HeaterMessageServerHandler;
import net.whatamidoingstudios.lacroix.network.heater.HeaterMessageServerResponse;
import net.whatamidoingstudios.lacroix.network.heater.HeaterMessageServerResponseHandler;
import net.whatamidoingstudios.lacroix.network.heater.ProgressBar;
import net.whatamidoingstudios.lacroix.network.heater.ProgressBarHandler;

public class LaCroixNetworkHandler {
	public SimpleNetworkWrapper channel;
	
	public LaCroixNetworkHandler() {
		channel = NetworkRegistry.INSTANCE.newSimpleChannel("heatersettings");
		channel.registerMessage(HeaterMessageServerHandler.class, HeaterMessageServer.class, 0, Side.SERVER);
		channel.registerMessage(HeaterMessageClientHandler.class, HeaterMessageClient.class, 1, Side.CLIENT);
		channel.registerMessage(HeaterMessageServerResponseHandler.class, HeaterMessageServerResponse.class, 2, Side.CLIENT);
		channel.registerMessage(ProgressBarHandler.class, ProgressBar.class, 3, Side.CLIENT);
	}
}