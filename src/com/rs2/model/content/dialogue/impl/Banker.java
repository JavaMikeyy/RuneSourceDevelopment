package com.rs2.model.content.dialogue.impl;

import com.rs2.model.players.Player;
import com.rs2.model.players.BankManager;
import com.rs2.model.content.BankPin;

/**
  * By Mikey` of Rune-Server
  */

public class Banker {

	public static void sendDialogue(Player player, int dialogueId) {
		switch (dialogueId) {
			case 1:
				player.getDialogue().sendNpcChat1("What can I do for you?", player.getDialogue().CONTENT);
				player.getDialogue().setNextDialogue(2);
				break;
			case 2:
				player.getDialogue().sendOption3("I would like to access my bank account.", 
						"I would like to edit my Bank Pin settings.",
						"Nothing.");
				player.getDialogue().setOptionId(1);
				break;
			case 3:
				player.getDialogue().sendPlayerChat1("I would like to access my bank account.", player.getDialogue().CONTENT);
				player.getDialogue().setNextDialogue(4);
				break;
			case 4:
				BankManager.openBank(player);
				player.getDialogue().endDialogue();
				break;
			case 5:
				player.getDialogue().sendPlayerChat1("Nothing.", player.getDialogue().CONTENT);
				player.getDialogue().setNextDialogue(6);
				break;
			case 6:
				player.getDialogue().sendNpcChat1("Well, just let me know when I can help.", player.getDialogue().CONTENT);
				player.getDialogue().endDialogue();
				break;
			case 7:
				player.getDialogue().sendPlayerChat1("I would like to edit my Bank Pin settings.", player.getDialogue().CONTENT);
				player.getDialogue().setNextDialogue(8);
				break;
			case 8:
				player.getDialogue().sendNpcChat1("What would you like to do?", player.getDialogue().CONTENT);
				player.getDialogue().setNextDialogue(9);
				break;
			case 9:
				if (player.getBankPin().hasBankPin() && 
						!player.getBankPin().hasPendingBankPinRequest()) {
					player.getDialogue().sendOption2("I would like to change my bank pin.", 
							"I would like to delete my bank pin.");
					player.getDialogue().setOptionId(6);
				}
				else if (player.getBankPin().hasBankPin() && 
						player.getBankPin().hasPendingBankPinRequest()) {
					player.getDialogue().sendOption2("I would like to delete my pending bank pin request.", 
							"No, nevermind.");
					player.getDialogue().setOptionId(7);
				}
				else {
					player.getDialogue().sendOption2("I would like to set a bank pin.", 
							"No, nevermind.");
					player.getDialogue().setOptionId(8);
				}
				break;
			case 10:
				player.getDialogue().sendPlayerChat1("I would like to change my bank pin.", player.getDialogue().CONTENT);
				player.getDialogue().setNextDialogue(11);
				break;
			case 11:
				player.getDialogue().sendNpcChat1("Please carefully select your bank pin.", player.getDialogue().CONTENT);
				player.getDialogue().setNextDialogue(12);
				break;
			case 12:
				player.getDialogue().endDialogue();
				player.getBankPin().startPinInterface(BankPin.PinInterfaceStatus.CHANGING);
				break;
			case 13:
				int[] pBP = player.getBankPin().getPendingBankPin();
				player.getDialogue().sendNpcChat2("Your bank pin will be set to " +
						pBP[0] + " " + pBP[1] + " " + pBP[2] + " " + pBP[3] + ".",
						"Does that sound correct?", player.getDialogue().CONTENT);
				player.getDialogue().setNextDialogue(14);
				break;
			case 14:
				player.getDialogue().sendOption3("Yes.", 
							"No, may I try again?", "No, nevermind.");
					player.getDialogue().setOptionId(9);
				break;
			case 15:
				player.getBankPin().setChangingBankPin();
				player.getDialogue().sendPlayerChat1("Yes.", player.getDialogue().CONTENT);
				player.getDialogue().setNextDialogue(16);
				break;
			case 16:
				if (player.getBankPin().hasBankPin())
					player.getDialogue().sendNpcChat2("Changes will take affect in 7 days.", 
							"Return to me to edit or delete this change.", player.getDialogue().CONTENT);
				else
					player.getDialogue().sendNpcChat1("Your bank pin will be set accordingly.", player.getDialogue().CONTENT);
				player.getBankPin().checkBankPinChangeStatus();
				player.getDialogue().setNextDialogue(17);
				break;
			case 17:
				player.getDialogue().sendPlayerChat1("Will do.", player.getDialogue().CONTENT);
				player.getDialogue().endDialogue();
				break;
			case 18:
				player.getDialogue().sendPlayerChat1("No, may I try again?", player.getDialogue().CONTENT);
				player.getDialogue().setNextDialogue(19);
				break;
			case 19:
				player.getDialogue().sendNpcChat1("Sure.", player.getDialogue().CONTENT);
				player.getDialogue().setNextDialogue(12);
				player.getBankPin().clearPendingBankPinRequest();
				break;
			case 20:
				player.getDialogue().sendPlayerChat1("No, nevermind.", player.getDialogue().CONTENT);
				player.getDialogue().setNextDialogue(21);
				break;
			case 21:
				player.getDialogue().sendNpcChat1("Return to me if you change your mind.", player.getDialogue().CONTENT);
				player.getDialogue().endDialogue();
				player.getBankPin().clearPendingBankPinRequest();
				break;
			case 22:
				player.getDialogue().sendPlayerChat1("I would like to delete my bank pin.", player.getDialogue().CONTENT);
				player.getDialogue().setNextDialogue(23);
				break;
			case 23:
				player.getDialogue().sendNpcChat1("Are you sure you would like to delete your bank pin?"
						, player.getDialogue().CONTENT);
				player.getDialogue().setNextDialogue(24);
				break;
			case 24:
				player.getDialogue().sendOption2("Yes.", "No, nevermind.");
				player.getDialogue().setOptionId(10);
				break;
			case 25:
				player.getBankPin().setDeletingBankPin();
				player.getDialogue().sendPlayerChat1("Yes.", player.getDialogue().CONTENT);
				player.getDialogue().setNextDialogue(16);
				break;
			case 26:
				player.getDialogue().sendPlayerChat1("No, nevermind.", player.getDialogue().CONTENT);
				player.getDialogue().setNextDialogue(34);
				break;
			case 27:
				player.getDialogue().sendPlayerChat1("I would like to delete my pending bank pin request.", player.getDialogue().CONTENT);
				player.getDialogue().setNextDialogue(28);
				break;
			case 28:
				player.getDialogue().sendNpcChat2("Are you sure?", "This clears any deletion or change request."
						, player.getDialogue().CONTENT);
				player.getDialogue().setNextDialogue(29);
				break;
			case 29:
				player.getDialogue().sendOption2("Yes.", "No, nevermind.");
				player.getDialogue().setOptionId(11);
				break;
			case 30:
				player.getDialogue().sendPlayerChat1("Yes.", player.getDialogue().CONTENT);
				player.getDialogue().setNextDialogue(31);
				break;
			case 31:
				player.getBankPin().clearPendingBankPinRequest();
				player.getDialogue().sendNpcChat1("Your pending bank pin request has been deleted."
						, player.getDialogue().CONTENT);
				player.getDialogue().setNextDialogue(32);
				break;
			case 32:
				player.getDialogue().sendPlayerChat1("Thanks.", player.getDialogue().CONTENT);
				player.getDialogue().endDialogue();
				break;
			case 33:
				player.getDialogue().sendPlayerChat1("I would like to set my bank pin.", player.getDialogue().CONTENT);
				player.getDialogue().setNextDialogue(11);
				break;
			case 34:
				player.getDialogue().sendNpcChat1("Return to me if you change your mind.", player.getDialogue().CONTENT);
				player.getDialogue().endDialogue();
				break;
		}
	}
	
}
