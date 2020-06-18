package net.whatamidoingstudios.lacroix.block;

import net.minecraft.util.EnumFacing;

public interface ISteamConsumer extends ISteam {
	public void setSteamAccess(boolean hasSteamAccess);
	public int getSteamUsedPerTick();
}
