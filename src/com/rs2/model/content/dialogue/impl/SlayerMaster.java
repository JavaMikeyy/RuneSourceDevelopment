package com.rs2.model.content.dialogue.impl;

import com.rs2.model.players.Player;
import com.rs2.model.players.BankManager;
import com.rs2.model.players.ShopManager;
import com.rs2.util.Misc;

/**
  * By Mikey` of Rune-Server
  */

public class SlayerMaster {

	public static void sendDialogue(Player player, int dialogueId) {
		String slayerNpc = (String) player.getSlayerTask()[0];
		switch (dialogueId) {
			case 1:
				player.getDialogue().sendNpcChat2("Hello, " + player.getUsername() + ".", "What can I do for you?", 
						player.getDialogue().CONTENT);
				player.getDialogue().setNextDialogue(2);
				break;
			case 2:
				player.getDialogue().sendOption3("Who are you?", "I'd like a slayer task.", "Can I view your Slayer shop?");
				player.getDialogue().setOptionId(3);
				break;
			case 3:
				player.getDialogue().sendPlayerChat1("Who are you?", player.getDialogue().CONTENT);
				player.getDialogue().setNextDialogue(4);
				break;
			case 4:
				player.getDialogue().sendNpcChat2("I am Vannaka, the slayer master.", "I assign slayer tasks for you to complete.", 
						player.getDialogue().CONTENT);
				player.getDialogue().setNextDialogue(2);
				break;
			case 5:
				player.getDialogue().sendPlayerChat1("I'd like a slayer task.", player.getDialogue().CONTENT);
				player.getDialogue().setNextDialogue(6);
				break;
			case 6:
				if (!slayerNpc.equalsIgnoreCase("")) {
					player.getDialogue().sendNpcChat2("You must complete your current task before", "getting a new one.",
						player.getDialogue().CONTENT);
					player.getDialogue().setNextDialogue(7);
					return;
				}
				player.getDialogue().sendNpcChat2("Based on your combat level, you will", "recieve a " +
						player.getSlayer().determineTaskLevel() + "-level task.", 
						player.getDialogue().CONTENT);
				player.getDialogue().setNextDialogue(7);
				break;
			case 7:
				if (!slayerNpc.equalsIgnoreCase("")) {
					player.getDialogue().sendNpcChat1("Your existing task is to kill " + (Integer) player.getSlayerTask()[1] + 
							" " + slayerNpc + "s.", player.getDialogue().CONTENT);
					player.getDialogue().setNextDialogue(8);
					return;
				}
				Object[] task = player.getSlayer().getSlayerTask();
				player.getDialogue().sendNpcChat1("Your task is to kill " + (Integer) task[1] + " " + (String) task[0] + 
						"s.", player.getDialogue().CONTENT);
				player.getDialogue().setNextDialogue(8);
				break;
			case 8:
				String[] randomNcpMessages = {
					"Return to me afterwards for another, good luck."
				};
				player.getDialogue().sendNpcChat1(randomNcpMessages[Misc.randomNumber(randomNcpMessages.length)], 
						player.getDialogue().CONTENT);
				player.getDialogue().endDialogue();
				break;
			case 9:
				player.getDialogue().sendPlayerChat1("Can I view your Slayer shop?", player.getDialogue().CONTENT);
				player.getDialogue().setNextDialogue(10);
				break;
			case 10:
				player.getDialogue().endDialogue();
				ShopManager.openShop(player, 5);
				break;
		}
	}
	
}
