package net.whatamidoingstudios.lacroix.block.heater;

public enum EnumUpgrades {
	Basic,
	Advanced,
	Excellent,
	Perfect;
	
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
