package com.rs2.model.content.skills;

import com.rs2.model.players.Player;
import com.rs2.model.players.Item;
import com.rs2.model.players.ItemManager;

/**
  * By Mikey` of Rune-Server
  */

public class Runecrafting {

	private Player player;
	
	public Runecrafting(Player player) {
		this.player = player;
	}

	public static final int[][] RUNECRAFTING =
	{//Same order as ABYSS_RIFTS
	{2486, 44, 561}, {2484, 27, 564}, {2482, 14, 554}, {2481, 9, 557}, {2483, 20, 559}, {2479, 2, 558},
	{2478, 1, 556}, {2480, 5, 555}, {2485, 54, 563}, {2487, 35, 562}, {2488, 65, 560}
	};
	
	public static final double[] RUNE_EXP =
	{
	9, 8, 7, 6.5, 7.5, 5.5, 6, 6, 9.5, 8.5, 10
	};
	
	public static final int[][] ABYSS_RIFTS =
	{/*Nature     		 Cosmic      	     Fire                Body                Mind*/
	{7133, 2400, 4835}, {7132, 2162, 4833}, {7129, 2574, 4849}, {7131, 2523, 4826}, {7140, 2793, 4828}, 
	 /*Air               Water              Earth                Law                 Chaos*/
	{7139, 2841, 4829}, {7137, 2726, 4832}, {7130, 2655, 4830}, {7135, 2464, 4818}, {7134, 2281, 4837}, {7136, 2208, 4830}
	};
	
	public static final int[] ALTAR_PORTALS =
	//Same order as ABYSS_RIFTS
	{
	2473, 2471, 2469, 2468, 2470, 2466, 2465, 2467, 2472, 2474, 2475
	};
	
	public static final int[][] POUCHES =
	{
	{5509, 3, 1}, {5510, 6, 25}, {5512, 9, 50}, {5514, 12, 75}
	};
	
	private int pouchData[] = 
	{
	0, 0, 0, 0
	};
	
	
	public void craftRunes(int levelRequired, double experience, int runeId) {
		int level = player.getSkill().getLevelForXP((int) player.getSkill().getExp()[Skill.RUNECRAFTING]);
		if (level < levelRequired) {
			player.getActionSender().sendMessage("You need a Runecrafting level of " + levelRequired + 
			" to craft " + ItemManager.getInstance().getItemName(runeId) + "s.");
			return;
		}
		int amount = player.getInventory().getItemContainer().getCount(7936);
		if (amount < 1) {
			player.getActionSender().sendMessage("You need Pure essence to craft runes.");
			return;
		}
		player.getInventory().removeItem(new Item(7936, amount));
		player.getSkill().addExp(20, experience * amount);
		player.getInventory().addItem(new Item(runeId, amount * multiplyRunes(runeId)));
		player.getUpdateFlags().sendAnimation(791, 0);
		player.getUpdateFlags().sendHighGraphic(186, 0);
		player.getActionSender().sendMessage("You craft some " + ItemManager.getInstance().getItemName(runeId) + "s.");
	}
	
	public int multiplyRunes(int runeId) {
		int level = player.getSkill().getLevelForXP((int) player.getSkill().getExp()[Skill.RUNECRAFTING]);
		int multiplier = 1;
		int doubleRunes = 0;
		switch (runeId) {
			case 556://Air rune
				doubleRunes = 11;
				break;
			case 558://Mind rune
				doubleRunes = 14;
				break;
			case 555://Water rune
				doubleRunes = 19;
				break;
			case 557://Earth rune
				doubleRunes = 26;
				break;
			case 554://Fire rune
				doubleRunes = 35;
				break;
			case 559://Body rune
				doubleRunes = 46;
				break;
			case 564://Cosmic rune
				doubleRunes = 59;
				break;
			case 562://Chaos rune
				doubleRunes = 74;
				break;
			case 561://Nature rune
				doubleRunes = 91;
				break;
		}
		if (doubleRunes != 0)
			return multiplier += (level / doubleRunes);
		else
			return multiplier;
	}
	
	public void clickObjects(int objectId) {
		try {
		for (int i = 0; i < ABYSS_RIFTS.length; i++) {
			if (objectId == ABYSS_RIFTS[i][0]) {
				player.sendTeleport(ABYSS_RIFTS[i][1], ABYSS_RIFTS[i][2], 0);
				return;
			}
		}
		for (int i = 0; i < ALTAR_PORTALS.length; i++) {
			if (objectId == ALTAR_PORTALS[i]) {
				player.sendTeleport(2967, 3385, 0);
				return;
			}
		}
		for (int i = 0; i < RUNECRAFTING.length; i++) {
			if (objectId == RUNECRAFTING[i][0]) {
				craftRunes(RUNECRAFTING[i][1], RUNE_EXP[i], RUNECRAFTING[i][2]);
				return;
			}
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void fillEssencePouch(int itemId) {
		int level = player.getSkill().getLevelForXP((int) player.getSkill().getExp()[Skill.RUNECRAFTING]);
		for (int i = 0; i < POUCHES.length; i++) {
			if (itemId == POUCHES[i][0]) {
				if (level < POUCHES[i][2]) {
					player.getActionSender().sendMessage("You need " + POUCHES[i][2] + " Runecrafting to use this pouch.");
					return;
				}
				int amount = player.getInventory().getItemContainer().getCount(7936);
				if (pouchData[i] < POUCHES[i][1]) {	
					if (amount > 0) {
						int spaceAvailable = POUCHES[i][1] - pouchData[i];
						int fillAmount = 0;
						for (int i2 = 0; i2 < spaceAvailable; i2++) {
							if (amount > 0 && pouchData[i] <= POUCHES[i][1]) {
								amount--;
								fillAmount++;
								pouchData[i] += 1;
							}
						}
						player.getInventory().removeItem(new Item(7936, fillAmount));
						return;
					}
					else {
						player.getActionSender().sendMessage("You don't have any Pure essence.");
						return;
					}
				}
				else {
					player.getActionSender().sendMessage("Your " + ItemManager.getInstance().getItemName(itemId) + " is full.");
					return;
				}
			}
		}
	}
	
	public void emptyEssencePouch(int itemId) {
		for (int i = 0; i < POUCHES.length; i++) {
			if (itemId == POUCHES[i][0]) {
				if (pouchData[i] > 0) {
					if (player.getInventory().getItemContainer().freeSlots() >= pouchData[i]) {
						player.getInventory().addItem(new Item(7936, pouchData[i]));
						pouchData[i] = 0;
						return;
					}
					else {
						player.getActionSender().sendMessage("There's not enough free space in your inventory to empty this pouch.");
					}
				}
				else {
					player.getActionSender().sendMessage("Your " + ItemManager.getInstance().getItemName(itemId) + " is empty.");
					return;
				}
			}
		}
	}
	
	public void checkEssencePouch(int itemId) {
		for (int i = 0; i < POUCHES.length; i++) {
			if (itemId == POUCHES[i][0]) {
				if (pouchData[i] > 0) {
					player.getActionSender().sendMessage("Your " + ItemManager.getInstance().getItemName(itemId) + " contains " 
					+ pouchData[i] + " Pure essence.");
					return;
				}
				else {
					player.getActionSender().sendMessage("Your " + ItemManager.getInstance().getItemName(itemId) + " is empty.");
					return;
				}
			}
		}
	}
	
	public int getPouchData(int i) {
		return pouchData[i];
	}
	
	public void setPouchData(int i, int amount) {
		pouchData[i] = amount;
	}
	
	
	
	
}
