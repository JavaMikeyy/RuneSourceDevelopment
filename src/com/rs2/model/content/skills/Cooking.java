package com.rs2.model.content.skills;

import com.rs2.util.Misc;
import com.rs2.model.World;
import com.rs2.model.tick.Tick;
import com.rs2.model.Position;
import com.rs2.model.players.Player;
import com.rs2.model.players.Item;
import com.rs2.model.players.ItemManager;
import com.rs2.model.players.WalkToActions;

/**
  * By Mikey` of Rune-Server
  */

public class Cooking {

	private Player player;
	
	public Cooking(Player player) {
		this.player = player;
	}
	
	public static final int[][] COOKING_OBJECTS =
	{//id, anim
	{2728, 883}, {2729, 883}, {2730, 883}, {2731, 883}, {2732, 897}
	};
	
	public static final int[][] COOKING =
	{//levelRequired, cooked
	//Shrimp      Anchovies    Sardine      Herring      Mackerel                                                     
	{1, 315, 1}, {1, 319, 1}, {1, 325, 1}, {5, 347, 1}, {10, 355, 1}, 
	//Trout        Cod           Pike          Salmon        Tuna                                                                            
	{15, 333, 1}, {18, 351, 1}, {20, 351, 1}, {25, 329, 1}, {30, 361, 1},
	//Lobster      Bass          Swordfish     Monkfish       Shark        Turtle                                                
	{40, 379, 1}, {43, 365, 1}, {45, 373, 1}, {62, 7946, 1}, {80, 385, 1}, {82, 397, 1},
	//Manta
	{91, 391},
	};
	
	public static int[] BURNED_ITEMS =
	{//Same order as COOKING
	323, 323, 369, 357, 358,
	343, 343, 343, 343, 367,
	381, 367, 375, 7948, 387, 399,
	393
	};
	
	public static int[][] REQUIRED_ITEMS =
	{//Same order as COOKING
	{317, 1}, {321, 1}, {327, 1}, {345, 1}, {353, 1},
	{335, 1}, {349, 1}, {349, 1}, {331, 1}, {359, 1},
	{377, 1}, {363, 1}, {371, 1}, {7944, 1}, {383, 1}
	};
	
	public static final double[] COOKING_EXP =
	{//Same order as COOKING
	30, 30, 40, 50, 60, 
	70, 75, 80, 90, 100,
	120, 130, 140, 150, 210
	};
	
	private int cookingIndex = 0;
	
	public void sendCookingInterface(int itemId, int itemIndex) {
		player.getActionSender().sendItemOnInterface(13716, 250, itemId);
		player.getActionSender().sendChatInterface(1743);
		cookingIndex = itemIndex;
	}
	
	public void clickingInterfaceButtons(int buttonId) {
		switch (buttonId) {
			case 53152://Cook 1
				player.getSkillResources().makeItem(1, COOKING, REQUIRED_ITEMS, COOKING_EXP, cookingIndex);
				break;
			case 53151://Cook 5
				player.getSkillResources().makeItem(5, COOKING, REQUIRED_ITEMS, COOKING_EXP, cookingIndex);
				break;
			case 53150://Cook X
				player.getSkillResources().makeItem(28, COOKING, REQUIRED_ITEMS, COOKING_EXP, cookingIndex);
				break;
			case 53149://Cook All
				player.getSkillResources().makeItem(28, COOKING, REQUIRED_ITEMS, COOKING_EXP, cookingIndex);
				break;
		}
	}
	
}












