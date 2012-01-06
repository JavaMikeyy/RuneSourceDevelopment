package com.rs2.model.content.dialogue.impl;

import com.rs2.model.players.Player;
import com.rs2.model.players.BankManager;

/**
  * By Mikey` of Rune-Server
  */

public class SlayerMaster {

	public static void sendDialogue(Player player, int dialogueId) {
		switch (dialogueId) {
			case 22:
				player.getDialogue().sendNpcChat2("Hello, " + player.getUsername() + ".", "What can I do for you?", 
						player.getDialogue().CONTENT);
				player.getDialogue().setNextDialogue(23);
				break;
			case 23:
				player.getDialogue().sendOption3("Who are you?", "I'd like a slayer task.", "Can I view your Slayer shop?");
				player.getDialogue().setOptionId(3);
				break;
			case 24:
				player.getDialogue().sendPlayerChat1("Who are you?", player.getDialogue().CONTENT);
				player.getDialogue().setNextDialogue(25);
				break;
			case 25:
				player.getDialogue().sendNpcChat2("I am a Vannaka, the slayer master.", "I assign slayer tasks for you to complete.", 
						player.getDialogue().CONTENT);
				player.getDialogue().setNextDialogue(23);
				break;
			case 26:
				player.getDialogue().sendPlayerChat1("I'd like a slayer task.", player.getDialogue().CONTENT);
				player.getDialogue().setNextDialogue(27);
				break;
			case 27:
				if (player.getSlayerTask()[0] != "") {
					player.getDialogue().sendNpcChat2("You must complete your current task before", "getting a new one.",
						player.getDialogue().CONTENT);
					player.getDialogue().setNextDialogue(28);
					return;
				}
				player.getDialogue().sendNpcChat2("Based on your combat level, you will", "recieve a " +
						player.getSlayer().determineTaskLevel() + "-level task.", 
						player.getDialogue().CONTENT);
				player.getDialogue().setNextDialogue(28);
				break;
			case 28:
				if (player.getSlayerTask()[0] != "") {
					player.getDialogue().sendNpcChat1("Your existing task is to kill " + (Integer) player.getSlayerTask()[1] + 
							" " + (String) player.getSlayerTask()[0] + 
							"s.", player.getDialogue().CONTENT);
					player.getDialogue().setNextDialogue(29);
					return;
				}
				Object[] task = player.getSlayer().getSlayerTask();
				player.getDialogue().sendNpcChat1("Your task is to kill " + (Integer) task[1] + " " + (String) task[0] + 
						"s.", player.getDialogue().CONTENT);
				player.getDialogue().setNextDialogue(29);
				break;
			case 29:
				player.getDialogue().sendPlayerChat1("I'll get right on that!", player.getDialogue().CONTENT);
				player.getDialogue().setNextDialogue(0);
				break;
			case 30:
				player.getDialogue().sendPlayerChat1("Can I view your Slayer shop?", player.getDialogue().CONTENT);
				player.getDialogue().setNextDialogue(31);
				break;
			case 31:
				//openshop
				player.getDialogue().setNextDialogue(0);
				break;
			
		}
	}
	
}
