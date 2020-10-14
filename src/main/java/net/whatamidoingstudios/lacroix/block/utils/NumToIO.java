package net.whatamidoingstudios.lacroix.block.utils;

public class NumToIO {
	public static String numToIO(int num) {
		switch(num) {
		case 0:
			return "normal";
		case 1:
			return "output";
		case 2:
			return "input";
		default:
			return "invalid";		
		}
	}
	
	public static int IOToNum(String io) {
		switch(io.toLowerCase()) {
		case "input":
			return 2;
		case "output":
			return 1;
		case "normal":
			return 0;
		default: 
			return -1;
		}
	}
}
