package com.rs2.model.content.dialogue;

import com.rs2.util.Misc;
import com.rs2.model.World;
import com.rs2.model.npcs.NpcDefinition;
import com.rs2.model.players.Player;

/**
  * By Mikey` of Rune-Server
  *		- I took a few methods from Project Insanity,
  *		  edited them to actually work, and then added the rest of the interfaces myself.
  */

public class Dialogue {

	private Player player;
	
	public Dialogue(Player player) {
		this.player = player;
	}
	
	public static final int CONTENT = 591, EVIL = 592, SAD = 596, SLEEPY = 603, 
										LAUGHING = 605, MOURNING = 610, MAD = 614;
	
	private int nextDialogue = 0; //Used to tell what the next option will be
	private int optionId = 0; //Used to tell what the optionId is, so we can have different actions for the same option button
	
	private Banker banker = new Banker();
	private MiscDialogue miscDialogue = new MiscDialogue();
	
	private static final String[] RANDOM_NPC_CHAT =
	{
		"What a beautiful day we're having.", "Nice to meet you.", 
		"I sure hope it doesn't rain.. if that's possible."
	};
	
	public void sendDialogue(int dialogueId) {//Left off at: 8
		player.getQuesting().sendQuestDialogue(dialogueId);
		miscDialogue.sendDialogue(player, dialogueId);
		banker.sendDialogue(player, dialogueId);
		if (dialogueId == 1) {
			sendNpcChat1(RANDOM_NPC_CHAT[Misc.randomNumber(RANDOM_NPC_CHAT.length)], CONTENT);
			nextDialogue = 0;
		}
	}
	
	/**
	  * An information box.
	  */
	public void sendInformationBox(String title, String line1, String line2, String line3, String line4) {//check
		player.getActionSender().sendString(title, 6180);
		player.getActionSender().sendString(line1, 6181);
		player.getActionSender().sendString(line2, 6182);
		player.getActionSender().sendString(line3, 6183);
		player.getActionSender().sendString(line4, 6184);
		player.getActionSender().sendChatInterface(6179);
	}
	
	/**
	  * Option selection.
	  */
	public void sendOption2(String option1, String option2) {
		player.getActionSender().sendString(option1, 2461);
		player.getActionSender().sendString(option2, 2462);
		player.getActionSender().sendChatInterface(2459);
	}
	
	public void sendOption3(String option1, String option2, String option3) {
		player.getActionSender().sendString(option1, 2471);
		player.getActionSender().sendString(option2, 2472);
		player.getActionSender().sendString(option3, 2473);
		player.getActionSender().sendChatInterface(2469);
	}
	
	public void sendOption4(String option1, String option2, String option3, String option4) {
		player.getActionSender().sendString(option1, 2482);
		player.getActionSender().sendString(option2, 2483);
		player.getActionSender().sendString(option3, 2484);
		player.getActionSender().sendString(option4, 2485);
		player.getActionSender().sendChatInterface(2480);
	}
	
	public void sendOption5(String option1, String option2, String option3, String option4, String option5) {
		player.getActionSender().sendString(option1, 2494);
		player.getActionSender().sendString(option2, 2495);
		player.getActionSender().sendString(option3, 2496);
		player.getActionSender().sendString(option4, 2497);
		player.getActionSender().sendString(option5, 2498);
		player.getActionSender().sendChatInterface(2492);
	}

	/**
	  * Statements.
	  */
	public void sendStatement1(String line1) {
		player.getActionSender().sendString(line1, 357);
		player.getActionSender().sendChatInterface(356);
	}
	
	public void sendStatement2(String line1, String line2) {
		player.getActionSender().sendString(line1, 360);
		player.getActionSender().sendString(line2, 361);
		player.getActionSender().sendChatInterface(359);
	}
	
	public void sendStatement3(String line1, String line2, String line3) {
		player.getActionSender().sendString(line1, 364);
		player.getActionSender().sendString(line2, 365);
		player.getActionSender().sendString(line3, 366);
		player.getActionSender().sendChatInterface(363);
	}
	
	public void sendStatement4(String line1, String line2, String line3, String line4) {
		player.getActionSender().sendString(line1, 369);
		player.getActionSender().sendString(line2, 370);
		player.getActionSender().sendString(line3, 371);
		player.getActionSender().sendString(line4, 372);
		player.getActionSender().sendChatInterface(368);
	}
	
	public void sendStatement5(String line1, String line2, String line3, String line4, String line5) {
		player.getActionSender().sendString(line1, 375);
		player.getActionSender().sendString(line2, 376);
		player.getActionSender().sendString(line3, 377);
		player.getActionSender().sendString(line4, 378);
		player.getActionSender().sendString(line5, 379);
		player.getActionSender().sendChatInterface(374);
	}
	
	/**
	  * Timed statements.
	  * These statements have no close/click options, so should only be used with a timer.
	  */
	public void sendTimedStatement1(String line1) {
		player.getActionSender().sendString(line1, 12789);
		player.getActionSender().sendChatInterface(12788);
	}
	
	public void sendTimedStatement2(String line1, String line2) {
		player.getActionSender().sendString(line1, 12791);
		player.getActionSender().sendString(line2, 12792);
		player.getActionSender().sendChatInterface(12790);
	}
	
	public void sendTimedStatement3(String line1, String line2, String line3) {
		player.getActionSender().sendString(line1, 12794);
		player.getActionSender().sendString(line2, 12795);
		player.getActionSender().sendString(line3, 12796);
		player.getActionSender().sendChatInterface(12793);
	}
	
	public void sendTimedStatement4(String line1, String line2, String line3, String line4) {
		player.getActionSender().sendString(line1, 12798);
		player.getActionSender().sendString(line2, 12799);
		player.getActionSender().sendString(line3, 12800);
		player.getActionSender().sendString(line4, 12801);
		player.getActionSender().sendChatInterface(12797);
	}
	
	public void sendTimedStatement5(String line1, String line2, String line3, String line4, String line5) {
		player.getActionSender().sendString(line1, 12803);
		player.getActionSender().sendString(line2, 12804);
		player.getActionSender().sendString(line3, 12805);
		player.getActionSender().sendString(line4, 12806);
		player.getActionSender().sendString(line5, 12807);
		player.getActionSender().sendChatInterface(12802);
	}
	
	/**
	  * NPC dialogue.
	  */
	public void sendNpcChat1(String line1, int emotion) {
		NpcDefinition def = World.getDefinitions()[player.getClickId()];
		String npcName = def.getName();
		player.getActionSender().sendDialogueAnimation(4883, emotion);
		player.getActionSender().sendString(npcName, 4884);
		player.getActionSender().sendString(line1, 4885);
		player.getActionSender().sendNPCDialogueHead(player.getClickId(), 4883);
		player.getActionSender().sendChatInterface(4882);
	}
	
	public void sendNpcChat2(String line1, String line2, int emotion) {
		NpcDefinition def = World.getDefinitions()[player.getClickId()];
		String npcName = def.getName();
		player.getActionSender().sendDialogueAnimation(4888, emotion);
		player.getActionSender().sendString(npcName, 4889);
		player.getActionSender().sendString(line1, 4890);
		player.getActionSender().sendString(line2, 4891);
		player.getActionSender().sendNPCDialogueHead(player.getClickId(), 4888);
		player.getActionSender().sendChatInterface(4887);
	}
	
	public void sendNpcChat3(String line1, String line2, String line3, int emotion) {
		NpcDefinition def = World.getDefinitions()[player.getClickId()];
		String npcName = def.getName();
		player.getActionSender().sendDialogueAnimation(4894, emotion);
		player.getActionSender().sendString(npcName, 4895);
		player.getActionSender().sendString(line1, 4896);
		player.getActionSender().sendString(line2, 4897);
		player.getActionSender().sendString(line3, 4898);
		player.getActionSender().sendNPCDialogueHead(player.getClickId(), 4894);
		player.getActionSender().sendChatInterface(4893);
	}
	
	public void sendNpcChat4(String line1, String line2, String line3, String line4, int emotion) {
		NpcDefinition def = World.getDefinitions()[player.getClickId()];
		String npcName = def.getName();
		player.getActionSender().sendDialogueAnimation(4901, emotion);
		player.getActionSender().sendString(npcName, 4902);
		player.getActionSender().sendString(line1, 4903);
		player.getActionSender().sendString(line2, 4904);
		player.getActionSender().sendString(line3, 4905);
		player.getActionSender().sendString(line4, 4906);
		player.getActionSender().sendNPCDialogueHead(player.getClickId(), 4901);
		player.getActionSender().sendChatInterface(4900);
	}
	
	/**
	  * Timed NPC dialogue.
	  * These NPC dialogues have no close/click options, so should only be used with a timer.
	  */
	public void sendTimedNpcChat2(String line1, String line2, int emotion) {
		NpcDefinition def = World.getDefinitions()[player.getClickId()];
		String npcName = def.getName();
		player.getActionSender().sendDialogueAnimation(12379, emotion);
		player.getActionSender().sendString(npcName, 12380);
		player.getActionSender().sendString(line1, 12381);
		player.getActionSender().sendString(line2, 12382);
		player.getActionSender().sendNPCDialogueHead(player.getClickId(), 12379);
		player.getActionSender().sendChatInterface(12378);
	}
	
	public void sendTimedNpcChat3(String line1, String line2, String line3, int emotion) {
		NpcDefinition def = World.getDefinitions()[player.getClickId()];
		String npcName = def.getName();
		player.getActionSender().sendDialogueAnimation(12384, emotion);
		player.getActionSender().sendString(npcName, 12385);
		player.getActionSender().sendString(line1, 12386);
		player.getActionSender().sendString(line2, 12387);
		player.getActionSender().sendString(line3, 12388);
		player.getActionSender().sendNPCDialogueHead(player.getClickId(), 12384);
		player.getActionSender().sendChatInterface(12383);
	}
	
	public void sendTimedNpcChat4(String line1, String line2, String line3, String line4, int emotion) {
		NpcDefinition def = World.getDefinitions()[player.getClickId()];
		String npcName = def.getName();
		player.getActionSender().sendDialogueAnimation(11892, emotion);
		player.getActionSender().sendString(npcName, 11893);
		player.getActionSender().sendString(line1, 11894);
		player.getActionSender().sendString(line2, 11895);
		player.getActionSender().sendString(line3, 11896);
		player.getActionSender().sendString(line4, 11897);
		player.getActionSender().sendNPCDialogueHead(player.getClickId(), 11892);
		player.getActionSender().sendChatInterface(11891);
	}
	
	/**
	  * Player dialogue.
	  */
	public void sendPlayerChat1(String line1, int emotion) {
		player.getActionSender().sendDialogueAnimation(969, emotion);
		player.getActionSender().sendString(player.getUsername(), 970);
		player.getActionSender().sendString(line1, 971);
		player.getActionSender().sendPlayerDialogueHead(969);
		player.getActionSender().sendChatInterface(968);
	}
	
	public void sendPlayerChat2(String line1, String line2, int emotion) {
		player.getActionSender().sendDialogueAnimation(974, emotion);
		player.getActionSender().sendString(player.getUsername(), 975);
		player.getActionSender().sendString(line1, 976);
		player.getActionSender().sendString(line2, 977);
		player.getActionSender().sendPlayerDialogueHead(974);
		player.getActionSender().sendChatInterface(973);
	}
	
	public void sendPlayerChat3(String line1, String line2, String line3, int emotion) {
		player.getActionSender().sendDialogueAnimation(980, emotion);
		player.getActionSender().sendString(player.getUsername(), 981);
		player.getActionSender().sendString(line1, 982);
		player.getActionSender().sendString(line2, 983);
		player.getActionSender().sendString(line3, 984);
		player.getActionSender().sendPlayerDialogueHead(980);
		player.getActionSender().sendChatInterface(979);
	}
	
	public void sendPlayerChat4(String line1, String line2, String line3, String line4, int emotion) {
		player.getActionSender().sendDialogueAnimation(987, emotion);
		player.getActionSender().sendString(player.getUsername(), 988);
		player.getActionSender().sendString(line1, 989);
		player.getActionSender().sendString(line2, 990);
		player.getActionSender().sendString(line3, 991);
		player.getActionSender().sendString(line4, 992);
		player.getActionSender().sendPlayerDialogueHead(987);
		player.getActionSender().sendChatInterface(986);
	}
	
	/**
	  * Timed player dialogue.
	  * These player dialogues have no close/click options, so should only be used with a timer.
	  */
	public void sendTimedPlayerChat1(String line1, int emotion) {
		player.getActionSender().sendDialogueAnimation(12774, emotion);
		player.getActionSender().sendString(player.getUsername(), 12775);
		player.getActionSender().sendString(line1, 12776);
		player.getActionSender().sendPlayerDialogueHead(12774);
		player.getActionSender().sendChatInterface(12773);
	}
	
	public void sendTimedPlayerChat2(String line1, String line2, int emotion) {
		player.getActionSender().sendDialogueAnimation(12778, emotion);
		player.getActionSender().sendString(player.getUsername(), 12779);
		player.getActionSender().sendString(line1, 12780);
		player.getActionSender().sendString(line2, 12781);
		player.getActionSender().sendPlayerDialogueHead(12778);
		player.getActionSender().sendChatInterface(12777);
	}
	
	public void sendTimedPlayerChat3(String line1, String line2, String line3, int emotion) {
		player.getActionSender().sendDialogueAnimation(12783, emotion);
		player.getActionSender().sendString(player.getUsername(), 12784);
		player.getActionSender().sendString(line1, 12785);
		player.getActionSender().sendString(line2, 12786);
		player.getActionSender().sendString(line3, 12787);
		player.getActionSender().sendPlayerDialogueHead(12783);
		player.getActionSender().sendChatInterface(12782);
	}
	
	public void sendTimedPlayerChat4(String line1, String line2, String line3, String line4, int emotion) {
		player.getActionSender().sendDialogueAnimation(11885, emotion);
		player.getActionSender().sendString(player.getUsername(), 11886);
		player.getActionSender().sendString(line1, 11887);
		player.getActionSender().sendString(line2, 11888);
		player.getActionSender().sendString(line3, 11889);
		player.getActionSender().sendString(line4, 11890);
		player.getActionSender().sendPlayerDialogueHead(11885);
		player.getActionSender().sendChatInterface(11884);
	}
	
	/**
	  * The buttons for the option interfaces.
	  */
	public void optionButtons(int buttonId) {
		switch (buttonId) {//optionInterface, optionId
			case 9157://2options, option1
				switch (optionId) {
					case 1:
						banker.sendDialogue(player, 4);
						break;
					case 2:
						sendDialogue(11);
						break;
				}
				break;
			case 9158://2options, option2
				switch (optionId) {
					case 1:
						banker.sendDialogue(player, 6);
						break;
					case 2:
						sendDialogue(12);
						break;
				}
				break;
			case 9167://3options, option1
				break;
			case 9168://3options, option2
				break;
			case 9169://3options, option3
				break;
			case 9178://4options, option1
				break;
			case 9179://4options, option2
				break;
			case 9180://4options, option3
				break;
			case 9181://4options, option4
				break;
			case 9190://5options, option1
				break;
			case 9191://5options, option2
				break;
			case 9192://5options, option3
				break;
			case 9193://5options, option4
				break;
			case 9194://5options, option5
				break;
		}
	}
	
	public void setNextDialogue(int nextDialogue) {
		this.nextDialogue = nextDialogue;
	}
	
	public int getNextDialogue() {
		return nextDialogue;
	}
	
	public void setOptionId(int optionId) {
		this.optionId = optionId;
	}
	
	public int getOptionId() {
		return optionId;
	}
	
}
