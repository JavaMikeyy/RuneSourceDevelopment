package com.rs2.model.content.dialogue.impl;

import com.rs2.model.players.Player;
import com.rs2.model.players.BankManager;

/**
  * By Mikey` of Rune-Server
  */

public class Banker {

	public static void sendDialogue(Player player, int dialogueId) {
		switch (dialogueId) {
			case 2:
				player.getDialogue().sendNpcChat1("What can I do for you?", player.getDialogue().CONTENT);
				player.getDialogue().setNextDialogue(3);
				break;
			case 3:
				player.getDialogue().sendOption2("I would like to access my bank account.", "Nothing.");
				player.getDialogue().setOptionId(1);
				break;
			case 4:
				player.getDialogue().sendPlayerChat1("I would like to access my bank account.", player.getDialogue().CONTENT);
				player.getDialogue().setNextDialogue(5);
				break;
			case 5:
				BankManager.openBank(player);
				player.getDialogue().setNextDialogue(0);
				player.getDialogue().setOptionId(0);
				break;
			case 6:
				player.getDialogue().sendPlayerChat1("Nothing.", player.getDialogue().CONTENT);
				player.getDialogue().setNextDialogue(7);
				break;
			case 7:
				player.getDialogue().sendNpcChat1("Well, just let me know when I can help.", player.getDialogue().CONTENT);
				player.getDialogue().setNextDialogue(0);
				player.getDialogue().setOptionId(0);
				break;
			
		}
	}
	
}
