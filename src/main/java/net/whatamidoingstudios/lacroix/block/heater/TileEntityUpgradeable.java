package net.whatamidoingstudios.lacroix.block.heater;

import net.minecraft.tileentity.TileEntity;

public class TileEntityUpgradeable extends TileEntity {
	public EnumUpgrades level = EnumUpgrades.Basic;
	
	public boolean upgrade(EnumUpgrades upgrade) {
		int lvl = 0;
		int upg = 0;
		
		switch(level) {
		case Basic:
			lvl=0;
			break;
		case Advanced:
			lvl=1;
			break;
		case Excellent:
			lvl=2;
			break;
		case Perfect:
			lvl=3;
			break;
		default:
			break;
		}
		
		switch(upgrade) {
		case Basic:
			upg=0;
			break;
		case Advanced:
			upg=1;
			break;
		case Excellent:
			upg=2;
			break;
		case Perfect:
			upg=3;
			break;
		default:
			break;
		
		}
		if(upg == lvl+1) {
			level = upgrade;
			return true;
		}
		return false;
	}
}