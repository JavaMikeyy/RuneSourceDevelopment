package com.rs2.model.content.questing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.rs2.model.World;
import com.rs2.model.tick.Tick;
import com.rs2.model.players.Player;

/**
  * By Mikey` of Rune-Server (MSN: metallic_mike@yahoo.com)
  * 	- Credit to Killamess for the most of the openQuestGuide method.
  */
public class Questing {

	private Player player;
	
	public Questing(Player player) {
		this.player = player;
	}
	
	public Object[][] questData = {
	//questName, currentStage, finishedStage, questPoints
	{"Getting Started", 0, 2, 1}
	};
	
	private static final int[] QUEST_IDS =
	{//Same order as questData
	7332, 7333, 7334, 7336, 7383, 7339, 7338, 7340,
	7346, 7341, 7342, 7337, 7343, 7335, 7344, 7345, 7347, 7348
	};
	
	private static final int[] QUEST_BUTTON_IDS =
	{//Same order as QUEST_IDS and questData
	28164, 28165, 28166, 28168, 28215, 28171, 28170, 28172, 28178, 28173, 28174, 28169, 
	28175, 28167, 28176, 27177, 28179, 28180
	};
	
	private GettingStarted gettingStarted = new GettingStarted();
	
	public void sendQuestDialogue(int dialogueId) {
		gettingStarted.sendDialogue(player, dialogueId);
	}
	
	private boolean questDialogueTicking = false;
	
	public void updateQuestList() {
		player.getActionSender().sendString("Quests", 663);
		for (int i = 0; i < QUEST_IDS.length; i++) {
			if (i < questData.length)
				if (questData[i][1] == questData[i][2])
					player.getActionSender().sendString("@gre@" + questData[i][0], QUEST_IDS[i]);
				else if ((Integer) questData[i][1] > 0)
					player.getActionSender().sendString("@yel@" + questData[i][0], QUEST_IDS[i]);
				else
					player.getActionSender().sendString("" + questData[i][0], QUEST_IDS[i]);
			else
				player.getActionSender().sendString("", QUEST_IDS[i]);
		}
	}
	
	public void clickQuestGuide(int buttonId) {
		for (int i = 0; i < questData.length; i++) {
			if (buttonId == QUEST_BUTTON_IDS[i])
				openQuestGuide((String) questData[i][0], (Integer) questData[i][1]);
		}	
	}
	
	public void clearQuestGuide() {
		for (int i = 0; i < 100; i++) {
				player.getActionSender().sendString("", 8144 + i);
		}
	}
	
	public void openQuestGuide(String directory, int stage) {
		boolean endOfFile = false;
		String line = "";
		BufferedReader questFile = null;
		try {	
			questFile = new BufferedReader(new FileReader("./data/content/questing/" + directory + "/" + stage + ".txt"));
			int lineId = 8144;
			while(!endOfFile) {
				line = questFile.readLine();
				if (!line.equals("[END]") && !line.startsWith("//")) {
					if (lineId == 8146)
						lineId += 1;
					player.getActionSender().sendString(line, lineId);
					lineId++;
				}
				else if (line.equals("[END]")) {
					endOfFile = true;
				}
			}
			questFile.close();
			player.getActionSender().sendInterface(8134);
		} catch(IOException e) {
			System.out.println(e);
		}
	}
	
	public void completeQuest(int questId, String reward1, String reward2, 
	String reward3, String reward4, String reward5) {
		setQuestStage(questId, (Integer) questData[questId][2]);
		player.getActionSender().sendString("You've completed '" + (String) questData[questId][0] + "'!", 12144);
		player.getActionSender().sendString(questData[questId][3] + " quest point(s)!", 12150);
		player.getActionSender().sendString(reward1, 12151);
		player.getActionSender().sendString(reward2, 12152);
		player.getActionSender().sendString(reward3, 12153);
		player.getActionSender().sendString(reward4, 12154);
		player.getActionSender().sendString(reward5, 12155);
		player.setQuestPoints(player.getQuestPoints() + (Integer) questData[questId][3]);
		player.getActionSender().sendString("QP: " + player.getQuestPoints(), 3985);
		player.getActionSender().sendString("" + player.getQuestPoints(), 12147);
		player.getActionSender().sendString("@gre@" + questData[questId][0], QUEST_IDS[questId]);
		player.getActionSender().sendInterface(12140);
	}

	public void executeTimedDialogue(int tickCount, int startId, final int amount) {
		player.getDialogue().setNextDialogue(startId);
		player.getDialogue().sendDialogue(player.getDialogue().getNextDialogue());
		World.submit(new Tick(10) {
			int currentAmount = 1;
			@Override
			public void execute() {
				player.getDialogue().sendDialogue(player.getDialogue().getNextDialogue());
				currentAmount++;
				if (currentAmount == amount) {
					stop();
					if (player.getDialogue().getNextDialogue() == 0)
						player.getActionSender().removeInterfaces();
				}
			}
		});
	}
	
	public void setQuestStage(int questId, int newQuestStage) {
		questData[questId][1] = newQuestStage;
		if (questData[questId][1] == questData[questId][2])
			player.getActionSender().sendString("@gre@" + questData[questId][0], QUEST_IDS[questId]);
		else if ((Integer) questData[questId][1] > 0)
			player.getActionSender().sendString("@yel@" + questData[questId][0], QUEST_IDS[questId]);
		else
			player.getActionSender().sendString("" + questData[questId][0], QUEST_IDS[questId]);
	}
	
	public int getQuestStage(int questId) {
		return (Integer) questData[questId][1];
	}
	
	public void setQuestDialogueTicking(boolean questDialogueTicking) {
		this.questDialogueTicking = questDialogueTicking;
	}
	
	public boolean isQuestDialogueTicking() {
		return questDialogueTicking;
	}
	
}