package com.rs2.model.content.dialogue.impl;

import com.rs2.model.players.Player;
import com.rs2.model.players.BankManager;
import com.rs2.util.Misc;

/**
  * By Mikey` of Rune-Server
  */

public class MakeOverMage {

	public static void sendDialogue(Player player, int dialogueId) {
		switch (dialogueId) {
			case 32:
				player.getDialogue().sendNpcChat2("Greetings, " + player.getUsername() + ".", "How may I assist you?", 
						player.getDialogue().CONTENT);
				player.getDialogue().setNextDialogue(33);
				break;
			case 33:
				player.getDialogue().sendOption2("Can you change my appearance?", "I'm fine, thanks.");
				player.getDialogue().setOptionId(4);
				break;
			case 34:
				player.getDialogue().sendPlayerChat1("Can you change my appearance?", player.getDialogue().CONTENT);
				player.getDialogue().setNextDialogue(35);
				break;
			case 35:
				player.getDialogue().sendNpcChat2("Sure.", "Make your selection, and it will be done.", 
						player.getDialogue().CONTENT);
				player.getDialogue().setNextDialogue(36);
				break;
			case 36:
				player.getDialogue().setNextDialogue(0);
				player.getActionSender().sendInterface(3559);
				break;
			case 37:
				player.getDialogue().sendPlayerChat1("I'm fine, thanks.", player.getDialogue().CONTENT);
				player.getDialogue().setNextDialogue(38);
				break;
			case 38:
				player.getDialogue().sendNpcChat2("I'm a busy man.", "Come back when you need something.", 
						player.getDialogue().CONTENT);
				player.getDialogue().setNextDialogue(0);
				break;
			
		}
	}
	
}
