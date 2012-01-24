package com.rs2.model.content;

import com.rs2.model.players.Player;
import com.rs2.util.Areas;

/**
  * By Mikey` of Rune-Server
  */

public class WalkInterfaces {

	public static void addWalkableInterfaces(Player player) {
		sendWildernessInterface(player);
		sendMultiInterface(player);
	}
	
	public static void sendWildernessInterface(Player player) {
		if (Areas.inWilderness(player.getPosition())) {
			player.getActionSender().sendWalkableInterface(197);
			player.getActionSender().sendString("@yel@Level: " + Areas.getWildernessLevel(player), 199);
		}
		else {
			player.getActionSender().sendWalkableInterface(-1);
		}
	}
	
	public static void sendMultiInterface(Player player) {
		if (Areas.inMultiArea(player.getPosition())) {
			player.getActionSender().sendMultiInterface(true);
		}
		else {
			player.getActionSender().sendMultiInterface(false);
		}
	}
	
}
