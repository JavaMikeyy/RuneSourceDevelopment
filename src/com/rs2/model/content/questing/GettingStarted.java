package com.rs2.model.content.questing;

import com.rs2.model.World;
import com.rs2.model.players.Player;

/**
  * By Mikey` of Rune-Server
  */
public class GettingStarted {

	public static void sendDialogue(final Player player, int dialogueId) {
		switch (dialogueId) {
			case 1:
				if (player.getQuesting().getQuestStage(0) == 0)
					sendDialogue(player, 2);
				if (player.getQuesting().getQuestStage(0) == 1)
					sendDialogue(player, 4);
				if (player.getQuesting().getQuestStage(0) == 2)
					sendDialogue(player, 4);
				break;
			case 2:
				player.getDialogue().sendNpcChat2("Hello, " + player.getUsername() + ".", "What can I help you with?", player.getDialogue().CONTENT);
				player.getDialogue().setNextDialogue(3);
				break;
			case 3:
				player.getDialogue().sendOption2("Tell me about the server.", "Nothing, just admiring you.");
				player.getDialogue().setOptionId(2);
				player.getDialogue().setNextDialogue(0);
				break;
			case 4:
				if (player.getQuesting().getQuestStage(0) == 2) {
					player.getDialogue().sendPlayerChat1("Tell me about the server again.", player.getDialogue().CONTENT);
					player.getDialogue().setNextDialogue(9);
				}
				else {
					player.getQuesting().setQuestStage(0, 1);
					player.getDialogue().sendPlayerChat1("Tell me about the server.", player.getDialogue().CONTENT);
					player.getDialogue().setNextDialogue(9);
				}
				break;
			case 5:
				player.getDialogue().sendPlayerChat1("Nothing, just admiring you.", player.getDialogue().CONTENT);
				player.getDialogue().setNextDialogue(6);
				break;
			case 6:
				player.getDialogue().sendNpcChat1("Well.. that's.. odd..", player.getDialogue().CONTENT);
				player.getDialogue().setNextDialogue(7);
				break;
			case 7:
				player.getDialogue().sendPlayerChat2("Yes..", "Yes, it is.", player.getDialogue().LAUGHING);
				player.getDialogue().setNextDialogue(8);
				break;
			case 8:
				player.getDialogue().sendStatement1("He backs away slowly.");
				player.getDialogue().setNextDialogue(0);
				break;
			case 9:
				player.getQuesting().executeTimedDialogue(3, 10, 4);
				break;
			case 10:
				player.getDialogue().sendTimedNpcChat2("This source is based off Azure (RuneSource).", 
				"It has been modified by Mikey` of Rune-Server.", player.getDialogue().CONTENT);
				player.getDialogue().setNextDialogue(11);
				break;
			case 11:
				player.getDialogue().sendTimedNpcChat2("There's no way to test content simply by playing.", 
				"Search classes and spawn items to test content.", player.getDialogue().CONTENT);
				player.getDialogue().setNextDialogue(12);
				break;
			case 12:
				player.getDialogue().sendTimedNpcChat2("If you have any questions, please contact Mikey` at", 
				"@blu@metallic_mike@yahoo.com@bla@ (on MSN).", player.getDialogue().CONTENT);
				player.getDialogue().setNextDialogue(13);
				break;
			case 13:
				player.getDialogue().sendPlayerChat1("Thanks!", player.getDialogue().CONTENT);
				if (player.getQuesting().getQuestStage(0) == 2)
					player.getDialogue().setNextDialogue(0);
				else
					player.getDialogue().setNextDialogue(14);
				break;
			case 14:
				player.getQuesting().completeQuest(0, "", "", "", "", "");
				player.getDialogue().setNextDialogue(0);
				break;
		}
	}
	
}
















