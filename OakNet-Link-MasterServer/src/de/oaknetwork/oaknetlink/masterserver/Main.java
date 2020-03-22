package de.oaknetwork.oaknetlink.masterserver;

public class Main {

	public static void main(String[] args) {
		if(args.length>0&&args[0].equalsIgnoreCase("nogui"))
			System.out.println("Start in nogui mode");
		else
			Console.open();
		System.out.println("Hallo Welt!");
		
	}
	
}
