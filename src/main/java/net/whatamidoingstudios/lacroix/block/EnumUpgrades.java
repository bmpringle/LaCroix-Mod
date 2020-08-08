package net.whatamidoingstudios.lacroix.block;

public enum EnumUpgrades {
	Basic,
	Advanced,
	Excellent,
	Perfect;
	
	public static int enumToInt(EnumUpgrades _enum) {
		switch(_enum) {
		case Basic:
			return 0;
		case Advanced:
			return 1;
		case Excellent:
			return 2;
		case Perfect:
			return 3;
		default:
			return -1;		
		}
	}
	
	public static EnumUpgrades enumFromInt(int _enum) {
		switch(_enum) {
		case 0:
			return Basic;
		case 1:
			return Advanced;
		case 2:
			return Excellent;
		case 3:
			return Perfect;
		default:
			return Basic;		
		}
	}
	
	public static String colorOf(String arg0, EnumUpgrades arg1) {
		String prepend = "§";
		switch(arg1) {
		case Basic:
			prepend = prepend+"f";
			break;
		case Advanced:
			prepend = prepend+"e";
			break;
		case Excellent:
			prepend = prepend+"b";
			break;
		case Perfect:
			prepend = prepend+"2";
			break;
		default:
			prepend = prepend+"f";
			break;
		}
		return prepend+arg0;
	}
	
	public static int mcFormatcolorOfToHex(String arg0) {
		switch(arg0) {
		case "§f":
			return 0xFFFFFF;
		case "§e":
			return 0xFFFF55;
		case "§b":
			return 0x55FFFF;
		case "§2":
			return 0x00AA00;
		default:
			return 0xABCDEF;
		}
	}
	
	public static EnumUpgrades safeValueOf(String arg0) {
		EnumUpgrades value = Basic;
		try {
			value = valueOf(arg0);
		}catch(IllegalArgumentException e) {
			value = Basic;
		}catch(NullPointerException e) {
			value = Basic;
		}
		return value;
	}
}
