package com.rs2.model.content.skills;

import com.rs2.model.players.Player;
import com.rs2.model.players.Item;
import com.rs2.model.players.ItemManager;

/**
  * By Mikey` of Rune-Server (MSN: metallic_mike@yahoo.com)
  */

public class Herblore {

	private Player player;
	
	public Herblore(Player player) {
		this.player = player;
	}

	public static int[][] CLEAN_HERB =
	{//oldHerb, newHerb, levelRequired
	{199, 249, 1}, {201, 251, 5}, {202, 253, 11}, {205, 255, 20}, {207, 257, 25}, {3049, 2998, 30}, 
	{209, 259, 40}, {211, 261, 48}, {213, 263, 54}, {3051, 3000, 59}, {215, 265, 65}, {2485, 2486, 67},
	{217, 267, 70}, {219, 269, 75}
	};
	
	public static double[] CLEAN_HERB_EXP =
	{//Same order as CLEAN_HERB
	2.5, 3.8, 5, 6.3, 7.5, 8,
	8.8, 10, 11.3, 11.8, 12.5, 13.1,
	13.8, 15
	};
	
	public static int[][] HERBLORE =
	{//levelRequired, itemId, animationId
	//Unfinished potions
	{1, 91, 1, 363}, {5, 93, 1, 363}, {11, 95, 1, 363}, {20, 97, 1, 363}, {25, 99, 1, 363}, {30, 3002, 1, 363},
	{40, 101, 1, 363}, {48, 103, 1, 363}, {54, 105, 1, 363}, {59, 3004, 1, 363}, {65, 107, 1, 363}, {67, 2483, 1, 363},
	{70, 109, 1, 363}, {75, 111, 1, 363},
	//Finished potions
	//Attack        Antipoison     Strength        Restore         Energy          Defence   
	{1, 121, 1, 363}, {5, 175, 1, 363}, {12, 115, 1, 363}, {22, 127, 1, 363}, {26, 3010, 1, 363}, {30, 133, 1, 363},
	//Agility          Combat           Prayer          Sup. Attack    Sup. Antiposion     
	{34, 3034, 1, 363}, {36, 9741, 1, 363}, {38, 139, 1, 363}, {45, 145, 1, 363}, {48, 181, 1, 363},
	//Fishing        Sup. Energy      Sup. Strength    Sup. Restore     Sup. Defence           
	{50, 151, 1, 363}, {52, 3018, 1, 363}, {55, 157, 1, 363}, {63, 3026, 1, 363}, {66, 163, 1, 363},
	//Ranging         Magic            Zamorak Brew     Sara Brew             
	{72, 169, 1, 363}, {76, 3042, 1, 363}, {78, 189, 1, 363}, {78, 6687, 1, 363}
	};
	
	public static int[][] REQUIRED_ITEMS =
	{//Same order as HERBLORE
	{249, 1, 227, 1}, {251, 1, 227, 1}, {253, 1, 227, 1}, {255, 1, 227, 1}, {257, 1, 227, 1}, {2998, 1, 227, 1},
	{259, 1, 227, 1}, {261, 1, 227, 1}, {263, 1, 227, 1}, {3000, 1, 227, 1}, {265, 1, 227, 1}, {2486, 1, 227, 1},
	{267, 1, 227, 1}, {269, 1, 227, 1},
	{91, 1, 221, 1}, {93, 1, 235, 1}, {95, 1, 226, 1}, {97, 1, 223, 1}, {97, 1, 1975, 1}, {99, 1, 239, 1},
	{3002, 1, 2152, 1}, {97, 1, 9736, 1}, {99, 1, 231, 1}, {101, 1, 221, 1}, {101, 1, 235, 1},
	{103, 1, 231, 1}, {103, 1, 2970, 1}, {105, 1, 226, 1}, {3004, 1, 223, 1}, {107, 1, 239, 1},
	{109, 1, 245, 1}, {2483, 1, 3138, 1}, {111, 1, 247, 1}, {3002, 1, 6693, 1}
	};
	
	public static double[] HERBLORE_EXP =
	{//Same order as HERBLORE
	0, 0, 0, 0, 0, 0,
	0, 0, 0, 0, 0, 0,
	0, 0,
	25, 37.5, 50, 62.5, 67.5, 75, 
	80, 84, 87.5, 100, 106.3,
	112.5, 117.5, 125, 142.5, 150,
	162.5, 172.5, 175, 180
	};
	
	private int herbloreIndex;
	
	public void cleanHerb(int itemId) {
		int level = player.getSkill().getLevelForXP((int) player.getSkill().getExp()[Skill.HERBLORE]);
		for (int i = 0; i < CLEAN_HERB.length; i++) {
			if (itemId == CLEAN_HERB[i][0]) {
				if (level >= CLEAN_HERB[i][2]) {
					player.getInventory().removeItem(new Item(CLEAN_HERB[i][0], 1));
					player.getSkill().addExp(15, CLEAN_HERB_EXP[i]);
					player.getInventory().addItem(new Item(CLEAN_HERB[i][1], 1));
					player.getActionSender().sendMessage("You clean the " + ItemManager.getInstance().getItemName(CLEAN_HERB[i][0]) + ".");
					return;
				}
				else {
					player.getActionSender().sendMessage("You need a Herblore level of " + CLEAN_HERB[i][2] + " to clean this herb.");
				}
			}
		}
	}
	
	public void fillEmptyVial() {
		player.getInventory().removeItem(new Item(229, 1));
		player.getInventory().addItem(new Item(227, 1));
		player.getActionSender().sendMessage("You fill the vial with water.");
	}
	
	public void clickingInterfaceButtons(int buttonId) {
		switch (buttonId) {//Interface, Amount
				case 10239://Make1, 1
				player.getSkillResources().makeItem(1, HERBLORE, REQUIRED_ITEMS, HERBLORE_EXP, getHerbloreIndex());
				break;
			case 10238://Make1, 5
				player.getSkillResources().makeItem(5, HERBLORE, REQUIRED_ITEMS, HERBLORE_EXP, getHerbloreIndex());
				break;
			case 6212://Make1, X
			case 6211://Make1, All
				player.getSkillResources().makeItem(28, HERBLORE, REQUIRED_ITEMS, HERBLORE_EXP, getHerbloreIndex());
				break;
		}
	}
	
	public int getHerbloreIndex() {
		return herbloreIndex;
	}
	
	public void setHerbloreIndex(int herbloreIndex) {
		this.herbloreIndex = herbloreIndex;
	}
	
}