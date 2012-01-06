package com.rs2.model.content.skills;

import com.rs2.model.players.Player;
import com.rs2.util.Misc;


/**
  * By Mikey` of Rune-Server
  */

public class Slayer {

	private Player player;
	
	public Slayer(Player player) {
		this.player = player;
	}
	
	private TaskLevels taskLevel = TaskLevels.LOW;
	
	public String determineTaskLevel() {
		if (player.getSkill().getCombatLevel() <= 50) {
			taskLevel = TaskLevels.LOW;
			return "low";
		}
		else if (player.getSkill().getCombatLevel() <= 100) {
			taskLevel = TaskLevels.MEDIUM;
			return "medium";
		}
		else if (player.getSkill().getCombatLevel() <= 126) {
			taskLevel = TaskLevels.HIGH;
			return "high";
		}
		taskLevel = TaskLevels.LOW;
		return "low";
	}
	
	public Object[] getSlayerTask() {
		Object[] task = new Object[2]; 
		int randomIndex;
		switch (taskLevel) {
			case LOW:
				randomIndex = Misc.randomNumber(LOW_LEVEL_NPCS.length);
				task[0] = (String) LOW_LEVEL_NPCS[randomIndex][0];
				task[1] = Misc.randomNumber(50);
				break;
			case MEDIUM:
				randomIndex = Misc.randomNumber(MEDIUM_LEVEL_NPCS.length);
				task[0] = (String) MEDIUM_LEVEL_NPCS[randomIndex][0];
				task[1] = Misc.randomNumber(50);
				break;
			case HIGH:
				randomIndex = Misc.randomNumber(HIGH_LEVEL_NPCS.length);
				task[0] = (String) HIGH_LEVEL_NPCS[randomIndex][0];
				task[1] = Misc.randomNumber(50);
				break;
		}
		player.setSlayerTask(task);
		return task;
	}
	
	private static final Object[][] LOW_LEVEL_NPCS = {
		{"Goblin"}, {"Zombie"}, {"Skeleton"}, {"Ghost"}, {"Chaos druid"}, {"Rock crab"}
	};
	private static final Object[][] MEDIUM_LEVEL_NPCS = {
		{"Hill Giant"}, {"Greater demon"}, {"Lesser demon"}, {"Fire giant"}, {"Moss giant"}
	};
	private static final Object[][] HIGH_LEVEL_NPCS = {
		{"Red dragon"}, {"Black dragon"}, {"Blue dragon"}, {"Hellhound"}, {"Fire giant"}, {"Moss giant"}
	};
	
	enum TaskLevels	{
		LOW, MEDIUM, HIGH
	}
	
}