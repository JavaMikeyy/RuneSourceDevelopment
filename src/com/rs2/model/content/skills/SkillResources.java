package com.rs2.model.content.skills;

import com.rs2.util.Misc;
import com.rs2.model.World;
import com.rs2.model.tick.Tick;
import com.rs2.model.players.Player;
import com.rs2.model.players.Item;
import com.rs2.model.players.ItemManager;

/**
  * By Mikey` of Rune-Server (MSN: metallic_mike@yahoo.com)
  */

public class SkillResources {

	private Player player;
	
	public SkillResources(Player player) {
		this.player = player;
	}
	
	private int skillId = 0;
	
	public void makeItem(final int amount, final int[][] SKILL_INFO, final int[][] REQUIRED_ITEMS, final double[] SKILL_EXP, final int SKILL_INDEX) {
		player.getActionSender().removeInterfaces();
		int level = player.getSkill().getLevelForXP((int) player.getSkill().getExp()[skillId]);
		if (!player.getSkill().skillTickRunning()) {
			player.getSkill().setSkillTickRunning(true);
			player.getSkill().setStopSkillTick(false);
			if (level >= SKILL_INFO[SKILL_INDEX][0]) {
				World.submit(new Tick(3) {
					int amountToMake = amount;
					boolean wasMaking;
					@Override
					public void execute() {
						if (amountToMake > 0 && !player.getSkill().stopSkillTick()) {
							int[] ITEMS_TO_REMOVE = new int[10];
							for (int i = 0; i < REQUIRED_ITEMS[SKILL_INDEX].length; i += 2) {
								if (player.getInventory().getItemContainer().getCount(REQUIRED_ITEMS[SKILL_INDEX][i]) < REQUIRED_ITEMS[SKILL_INDEX][i + 1]) {
									if (!wasMaking)
										player.getActionSender().sendMessage("You need  " + REQUIRED_ITEMS[SKILL_INDEX][i + 1] +
										" " + ItemManager.getInstance().getItemName(REQUIRED_ITEMS[SKILL_INDEX][i]) + " to make that.");
									else
										player.getActionSender().sendMessage("You ran out of " 
										+ ItemManager.getInstance().getItemName(REQUIRED_ITEMS[SKILL_INDEX][i]) + ".");
									player.getSkill().setStopSkillTick(true);
									return;
								}
								ITEMS_TO_REMOVE[i] = REQUIRED_ITEMS[SKILL_INDEX][i];
								ITEMS_TO_REMOVE[i + 1] = REQUIRED_ITEMS[SKILL_INDEX][i + 1];
							}
							for (int i = 0; i < ITEMS_TO_REMOVE.length; i += 2) {
								removeItems(ITEMS_TO_REMOVE, i);
							}
							wasMaking = true;
							amountToMake--;
							finish(SKILL_INDEX, SKILL_INFO, SKILL_EXP);
						}
						else {
							player.getUpdateFlags().sendAnimation(65535, 0);
							stop();
							player.getSkill().setSkillTickRunning(false);
							player.getSkill().setStopSkillTick(false);
							
						}
					}
				});
			}
			else {
				player.getActionSender().sendMessage("You need a " + player.getSkill().SKILL_NAME[skillId] + " level of " + SKILL_INFO[SKILL_INDEX][0] + " to make this.");
				player.getSkill().setSkillTickRunning(false);
			}
		}
	}
	
	public void removeItems(final int[] ITEMS_TO_REMOVE, int index) {
		if (ITEMS_TO_REMOVE[index] == 1734) {
			if (Misc.randomNumber(3) == 0)
				player.getInventory().removeItem(new Item(1734, 1));
			return;
		}
		else if (ITEMS_TO_REMOVE[index] == 1733) {
			if (Misc.randomNumber(5) == 0)
				player.getInventory().removeItem(new Item(1733, 1));
			return;
		}
		player.getInventory().removeItem(new Item(ITEMS_TO_REMOVE[index], ITEMS_TO_REMOVE[index + 1]));
	}
	
	public void finish(final int SKILL_INDEX, final int[][] SKILL_INFO, final double[] SKILL_EXP) {
		switch (skillId) {
			case 7://Cooking
				for (int i = 0; i < player.getCooking().COOKING_OBJECTS.length; i++)
					if (player.getClickId() == player.getCooking().COOKING_OBJECTS[i][0])
						player.getUpdateFlags().sendAnimation(player.getCooking().COOKING_OBJECTS[i][1], 0);
				if (isSuccess(SKILL_INDEX, SKILL_INFO)) {
					player.getSkill().addExp(12, SKILL_EXP[SKILL_INDEX]);
					player.getInventory().addItem(new Item(SKILL_INFO[SKILL_INDEX][1], SKILL_INFO[SKILL_INDEX][2]));
					player.getActionSender().sendMessage("You cook the " + ItemManager.getInstance().getItemName(SKILL_INFO[SKILL_INDEX][1]) + ".");
				}
				else {
					player.getInventory().addItem(new Item(player.getCooking().BURNED_ITEMS[SKILL_INDEX], 1));
					player.getActionSender().sendMessage("You accidently burn the " + ItemManager.getInstance().getItemName(SKILL_INFO[SKILL_INDEX][1]) + ".");
				}
				break;
			default://Anything not covered
				player.getUpdateFlags().sendAnimation(SKILL_INFO[SKILL_INDEX][3], 0);
				player.getSkill().addExp(12, SKILL_EXP[SKILL_INDEX]);
				player.getInventory().addItem(new Item(SKILL_INFO[SKILL_INDEX][1], SKILL_INFO[SKILL_INDEX][2]));
				player.getActionSender().sendMessage("You make " + textAddon(SKILL_INFO, SKILL_INDEX) + " "
				+ ItemManager.getInstance().getItemName(SKILL_INFO[SKILL_INDEX][1]) + ".");
				break;
		}
	}
	
	public final String[][] ITEM_NAMES = 
	{
	{"boots", "gloves", "chaps", "vambraces"}, {"armour"}
	};
	
	public String textAddon(int[][] SKILL_INFO, int SKILL_INDEX) {
		String name = ItemManager.getInstance().getItemName(SKILL_INFO[SKILL_INDEX][1]);
		char letter = ItemManager.getInstance().getItemName(SKILL_INFO[SKILL_INDEX][1]).charAt(0);
		for (int i = 0; i < ITEM_NAMES[0].length; i++) {
			if (name.contains(ITEM_NAMES[0][i]))
				return "a pair of";
		}
		for (int i = 0; i < ITEM_NAMES[1].length; i++) {
			if (name.contains(ITEM_NAMES[1][i]))
				return "some";
		}
		if(letter == 'A' || letter == 'E' || letter == 'I' || letter == 'O' || letter == 'U')
			return "an";
		return "a";
	}
	
	public boolean isSuccess(final int SKILL_INDEX, final int[][] SKILL_INFO) {
		int level = player.getSkill().getLevelForXP((int) player.getSkill().getExp()[skillId]);
		double successChance = Math.ceil((level * 50 - SKILL_INFO[SKILL_INDEX][0] * 15) / SKILL_INFO[SKILL_INDEX][0] / 3 * 4);
		int randomizedSuccessChance = Misc.randomNumber(99);
		if (successChance >= randomizedSuccessChance) {
			return true;
		}
		return false;
	}
	
	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}
	
	public int getSkillId() {
		return skillId;
	}
	
	
}
