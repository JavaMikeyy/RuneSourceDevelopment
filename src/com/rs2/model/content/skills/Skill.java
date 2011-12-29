package com.rs2.model.content.skills;

import com.rs2.Constants;
import com.rs2.model.World;
import com.rs2.model.players.Player;

public class Skill {
	
	private Player player;
	
	public static final int SKILL_COUNT = 22;
	public static final double MAXIMUM_EXP = 200000000;
	
	private int[] level = new int[SKILL_COUNT];
	private double[] exp = new double[SKILL_COUNT];
	
	public static final String[] SKILL_NAME = { "Attack", "Defence",
		"Strength", "Hitpoints", "Ranged", "Prayer", "Magic", "Cooking",
		"Woodcutting", "Fletching", "Fishing", "Firemaking", "Crafting",
		"Smithing", "Mining", "Herblore", "Agility", "Thieving", "Slayer",
		"Farming", "Runecrafting" };

	public static final int ATTACK = 0, DEFENCE = 1, STRENGTH = 2,
		HITPOINTS = 3, RANGE = 4, PRAYER = 5, MAGIC = 6, COOKING = 7,
		WOODCUTTING = 8, FLETCHING = 9, FISHING = 10, FIREMAKING = 11,
		CRAFTING = 12, SMITHING = 13, MINING = 14, HERBLORE = 15,
		AGILITY = 16, THIEVING = 17, SLAYER = 18, FARMING = 19,
		RUNECRAFTING = 20;
	
	private boolean stopSkillTick = false;
	private boolean skillTickRunning = false;
	
	public Skill(Player player) {
		this.player = player;
		for (int i = 0; i < level.length; i ++) {
			if (i == 3) {
				level[i] = 10;
				exp[i] = 1154;
			} else {
				level[i] = 1;
				exp[i] = 0;
			}
		}
	}
	
	public int[][] CHAT_INTERFACES = {
		{ATTACK, 6247, 0, 0},
		{DEFENCE, 6253, 0, 0},
		{STRENGTH, 6206, 0, 0},
		{HITPOINTS, 6216, 0, 0},
		{RANGE, 4443, 5453, 6114},
		{PRAYER, 6242, 0, 0},
		{MAGIC, 6211, 0, 0},
		{COOKING, 6226, 0, 0},
		{WOODCUTTING, 4272, 0, 0},
		{FLETCHING, 6231, 0, 0},
		{FISHING, 6258, 0, 0},
		{FIREMAKING, 4282, 0, 0},
		{CRAFTING, 6263, 0, 0},
		{SMITHING, 6221, 0, 0},
		{MINING, 4416, 4417, 4438},
		{HERBLORE, 6237, 0, 0},
		{AGILITY, 4277, 0, 0},
		{THIEVING, 4261, 4263, 4264},
		{SLAYER, 12122, 0, 0},
		{FARMING, 4887, 4889, 4890},//TODO: FIND THE REAL ID
		{RUNECRAFTING, 4267, 0, 0},
	};
	
	public void sendSkillsOnLogin() {
		refresh();
	}
	
	public void refresh() {
		for (int i = 0; i < level.length; i++) {
			player.getActionSender().sendSkill(i, level[i], exp[i]);
		}
		player.setAppearanceUpdateRequired(true);
	}
	
	public void refresh(int skill) {
		player.getActionSender().sendSkill(skill, level[skill], exp[skill]);
		player.setAppearanceUpdateRequired(true);
	}
	
	public int getLevelForXP(double exp) {
		int points = 0;
		int output = 0;
		for (int lvl = 1; lvl <= 99; lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			output = (int) Math.floor(points / 4);
			if (output >= exp + 1)
				return lvl;
		}
		return 99;
	}

	public int getXPForLevel(int level) {
		int points = 0;
		int output = 0;
		for (int lvl = 1; lvl <= level; lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			if (lvl >= level) {
				return output;
			}
			output = (int) Math.floor(points / 4);
		}
		return 0;
	}
	
	public int getTotalLevel() {
		int total = 0;
		for (int i = 0; i < level.length; i++) {
			total += getLevelForXP(i);
		}
		return total;
	}
	
	public void addExp(int skill, double xp) {
		int oldLevel = getLevelForXP(exp[skill]);
		exp[skill] += xp;
		if (exp[skill] > MAXIMUM_EXP) {
			exp[skill] = MAXIMUM_EXP;
		}
		int newLevel = getLevelForXP(exp[skill]);
		int levelDiff = newLevel - oldLevel;
		if (levelDiff > 0) {
			level[skill] += levelDiff;
			player.setAppearanceUpdateRequired(true);
			for (Player players : World.getPlayers()) {
				if (players == null)
					continue;
				players.getActionSender().sendStillGraphic(199,
						player.getPosition(), 0);
			}
			sendLevelUpMessage(skill);
		}
		refresh(skill);
	}
	
	private void sendLevelUpMessage(int skill) {
		final String line1 = "Congratulations! You've just advanced a " 
			+ SKILL_NAME[skill] + " level!";
		final String line2 = "You have reached level " 
			+ level[skill] + "!";
		player.getActionSender().sendMessage(line1);
		player.getActionSender().sendMessage(line2);
		for (int[] chatData : CHAT_INTERFACES) {
			if (chatData[0] == skill) {
				player.getActionSender().sendChatInterface(chatData[1]);
				if (skill != RANGE && skill != MINING && skill != THIEVING && 
						skill != FARMING) {
					player.getActionSender().sendString(line1, chatData[1] + 1);
					player.getActionSender().sendString(line2, chatData[1] + 2);
				} else {
					player.getActionSender().sendString(line1, chatData[2]);
					player.getActionSender().sendString(line2, chatData[3]);
				}
			}
		}
	}
	
	public int getCombatLevel() {
		final int attack = getLevelForXP(exp[ATTACK]);
		final int defence = getLevelForXP(exp[DEFENCE]);
		final int strength = getLevelForXP(exp[STRENGTH]);
		final int hp = getLevelForXP(exp[HITPOINTS]);
		final int prayer = getLevelForXP(exp[PRAYER]);
		final int ranged = getLevelForXP(exp[RANGE]);
		final int magic = getLevelForXP(exp[MAGIC]);
		int combatLevel = 3;
		combatLevel = (int) ((defence + hp + Math.floor(prayer / 2)) * 0.2535) + 1;
		final double melee = (attack + strength) * 0.325;
		final double ranger = Math.floor(ranged * 1.5) * 0.325;
		final double mage = Math.floor(magic * 1.5) * 0.325;
		if (melee >= ranger && melee >= mage) {
			combatLevel += melee;
		} else if (ranger >= melee && ranger >= mage) {
			combatLevel += ranger;
		} else if (mage >= melee && mage >= ranger) {
			combatLevel += mage;
		}
		if (combatLevel <= 126) {
			return combatLevel;
		} else {
			return 126;
		}
	}

	public void setLevel(int[] level) {
		this.level = level;
	}

	public int[] getLevel() {
		return level;
	}

	public void setExp(double[] exp) {
		this.exp = exp;
	}

	public double[] getExp() {
		return exp;
	}
	
	
	public boolean skillTickRunning() {
		return skillTickRunning;
	}
	
	public void setSkillTickRunning(boolean skillTickRunning) {
		this.skillTickRunning = skillTickRunning;
	}
	
	public boolean stopSkillTick() {
		return stopSkillTick;
	}
	
	public void setStopSkillTick(boolean set) {
		stopSkillTick = set;
	}

}
