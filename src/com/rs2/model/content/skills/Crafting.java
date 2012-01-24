package com.rs2.model.content.skills;

import com.rs2.util.Misc;
import com.rs2.model.World;
import com.rs2.model.tick.Tick;
import com.rs2.model.players.Player;
import com.rs2.model.players.Item;
import com.rs2.model.players.ItemManager;

/**
  * By Mikey` of Rune-Server
  */

public class Crafting {

	private Player player;
	
	public Crafting(Player player) {
		this.player = player;
	}
	
	public static final int[][] TANNING_HIDE =
	{//untannedItem, tannedItem, cost
	/*{1739, 1741, 2}, {1739, 1743, 3}, {6287, 6289, 45}, 
	{1753, 1745, 45}, {1751, 2505, 45}, {1749, 2507, 45}, {1747, 2509, 45}*/
	{1739, 1741, 0}, {1739, 1743, 0}, {6287, 6289, 0}, 
	{1753, 1745, 0}, {1751, 2505, 0}, {1749, 2507, 0}, {1747, 2509, 0}
	};
	
	public static int[][] CRAFTING =
	{//levelRequired, itemId, animationId
	//(Regular)Body        Gloves              Boots               Vambs.               Chaps                Coif                Cowl                                   
	{14, 1129, 1, 1249}, {1,  1059, 1, 1249}, {7, 1061, 1, 1249}, {11, 1063, 1, 1249}, {18, 1095, 1, 1249}, {38, 1169, 1, 1249}, {9, 1167, 1, 1249},
	//(d-hide)Green body  Green vambs          Green chaps           Blue body            Blue vambs          Blue chaps                                                     
	{63, 1135, 1, 1249}, {57, 1065, 1, 1249}, {60, 1099, 1, 1249}, {71, 2499, 1, 1249}, {66, 2487, 1, 1249}, {68, 2493, 1, 1249},
	//Red body             Red vambs             Red chaps           Black Body           Black vambs          Black chaps                                                      
	{77, 2501, 1, 1249}, {73, 2489, 1, 1249}, {75, 2495, 1, 1249}, {84, 2503, 1, 1249}, {79, 2491, 1, 1249}, {82, 2497, 1, 1249},
	//(snake)Body           Chaps                 Vambs               Bandana             Boots                                              
	{53, 6322, 1, 1249}, {51, 6324, 1, 1249}, {47, 6330, 1, 1249}, {48, 6326, 1, 1249}, {45, 6328, 1, 1249},
	//(unf) Pot          Pie dish            Bowl             Plant pot            Pot lid                                                  
	{1, 1787, 1, 894}, {7, 1789, 1, 894}, {8, 1791, 1, 894}, {19, 5352, 1, 894}, {25, 4438, 1, 894},
	//(fir)Pot           Pie dish            Bowl              Plant pot           Pot lid                                                     
	{1, 1931, 1, 899}, {7, 2313, 1, 899}, {8, 1923, 1, 899}, {19, 5350, 1, 899}, {25, 4440, 1, 899},
	//Opal               Jade                 Red topaz           Sapphire           Emerald                                                         
	{1, 1609, 1, 891}, {13, 1611, 1, 891}, {16, 1613, 1, 892}, {20, 1607, 1, 888}, {27, 1605, 1, 889},
	//Ruby                Diamond              Dragonstone         Onyx                                                      
	{34, 1603, 1, 887}, {43, 1601, 1, 886}, {55, 1615, 1, 885}, {67, 6573, 1, 886}
	};
	
	public static int[][] REQUIRED_ITEMS =
	{//Same order as CRAFTING
	{1741, 1, 1734, 1, 1733, 1}, {1741, 1, 1734, 1, 1733, 1}, {1741, 1, 1734, 1, 1733, 1}, {1741, 1, 1734, 1, 1733, 1}, {1741, 1, 1734, 1, 1733, 1}, {1741, 1, 1734, 1, 1733, 1}, {1741, 1, 1734, 1, 1733, 1},
	{1745, 3, 1734, 1, 1733, 1}, {1745, 1, 1734, 1, 1733, 1}, {1745, 2, 1734, 1, 1733, 1}, {2505, 3, 1734, 1, 1733, 1}, {2505, 1, 1734, 1, 1733, 1}, {2505, 2, 1734, 1, 1733, 1},
	{2507, 3, 1734, 1, 1733, 1}, {2507, 1, 1734, 1, 1733, 1}, {2507, 2, 1734, 1, 1733, 1}, {2509, 3, 1734, 1, 1733, 1}, {2509, 1, 1734, 1, 1733, 1}, {2509, 2, 1734, 1, 1733, 1},
	{7801, 15, 1734, 1, 1733, 1}, {7801, 12, 1734, 1, 1733, 1}, {7801, 8, 1734, 1, 1733, 1}, {7801, 5, 1734, 1, 1733, 1}, {7801, 6, 1734, 1, 1733, 1},
	{1761, 1}, {1761, 1}, {1761, 1}, {1761, 1}, {1761, 1}, 
	{1787, 1}, {1789, 1}, {1791, 1}, {5352, 1}, {4438, 1},
	{1625, 1}, {1627, 1}, {1629, 1}, {1623, 1}, {1621, 1}, 
	{1619, 1}, {1617, 1}, {1631, 1}, {6571, 1}
	};
	
	public static double[] CRAFTING_EXP =
	{//Same order as CRAFTING
	25, 13.8, 16.3, 22, 27, 37, 18.5, 
	186, 62, 125, 210, 70, 140,
	234, 78, 156, 258, 86, 172,
	55, 50, 35, 45, 30,
	6.3, 15, 18, 20, 20,
	6.3, 10, 15, 17.5, 20,
	15, 20, 25, 50, 67,
	85, 107.5, 137.5, 167.5
	};
	
	private int craftingIndex = 0;
	
	public void tanHide(int amount, int tanIndex) {
		int inventoryAmount = player.getInventory().getItemContainer().getCount(TANNING_HIDE[tanIndex][0]);
		int inventoryMoney = player.getInventory().getItemContainer().getCount(995);
		int amountToTan = 0;
		int goldToPay = 0;
		if (inventoryAmount > 0) {
			if (inventoryMoney >= TANNING_HIDE[tanIndex][2]) {
				for (int i = 0; i < amount; i++) {
					if (inventoryAmount >= amountToTan && (((inventoryAmount - amountToTan) - 1) > -1)) {
						if (goldToPay <= inventoryMoney && ((goldToPay + TANNING_HIDE[tanIndex][2]) <= inventoryMoney)) {
							goldToPay += TANNING_HIDE[tanIndex][2];
							amountToTan++;
						}
					}
				}
				player.getInventory().removeItem(new Item(995, goldToPay));
				player.getInventory().removeItem(new Item(TANNING_HIDE[tanIndex][0], amountToTan));
				player.getInventory().addItem(new Item(TANNING_HIDE[tanIndex][1], amountToTan));
				player.getActionSender().sendMessage("You tan " + amountToTan + " " + 
				ItemManager.getInstance().getItemName(TANNING_HIDE[tanIndex][0]) + " for " + goldToPay + " gp.");
			}
			else {
				player.getActionSender().sendMessage("You don't have enough gold to make this purchase.");
			}
		}
		else {
			player.getActionSender().sendMessage("You don't have any hides to tan.");
		}
	}
	
	public void clickingInterfaceButtons(int buttonId) {
		switch (buttonId) {//Interface, Amount
			case 10239://Make1, 1
				if (getCraftingIndex() >= 29 && getCraftingIndex() <= 33)
					player.getSkillResources().makeItem(1, CRAFTING, REQUIRED_ITEMS, CRAFTING_EXP, 
					getCraftingIndex());
				else if (getCraftingIndex() >= 29 && getCraftingIndex() <= 42)
					player.getSkillResources().makeItem(1, CRAFTING, REQUIRED_ITEMS, CRAFTING_EXP, 
					getCraftingIndex());
				break;
			case 10238://Make1, 5
				if (getCraftingIndex() >= 29 && getCraftingIndex() <= 33)
					player.getSkillResources().makeItem(5, CRAFTING, REQUIRED_ITEMS, CRAFTING_EXP, 
					getCraftingIndex());
				else if (getCraftingIndex() >= 34 && getCraftingIndex() <= 42)
					player.getSkillResources().makeItem(5, CRAFTING, REQUIRED_ITEMS, CRAFTING_EXP, 
					getCraftingIndex());
				break;
			case 6212://Make1, X
			case 6211://Make1, All
				if (getCraftingIndex() >= 29 && getCraftingIndex() <= 33)
					player.getSkillResources().makeItem(28, CRAFTING, REQUIRED_ITEMS, CRAFTING_EXP, 
					getCraftingIndex());
				else if (getCraftingIndex() >= 34 && getCraftingIndex() <= 42)
					player.getSkillResources().makeItem(28, CRAFTING, REQUIRED_ITEMS, CRAFTING_EXP, 
					getCraftingIndex());
				break;
			case 34185://Make3, option 1, 1
				if (getCraftingIndex() == 7)
					setCraftingIndex(getCraftingIndex() + 0);
				else if (getCraftingIndex() == 8)
					setCraftingIndex(getCraftingIndex() + 2);
				else if (getCraftingIndex() == 9)
					setCraftingIndex(getCraftingIndex() + 4);
				else if (getCraftingIndex() == 10)
					setCraftingIndex(getCraftingIndex() + 6);
				player.getSkillResources().makeItem(1, CRAFTING, REQUIRED_ITEMS, CRAFTING_EXP, 
				getCraftingIndex());
				break;
			case 34189://Make3, option 2, 1
				if (getCraftingIndex() == 7)
					setCraftingIndex(getCraftingIndex() + 1);
				else if (getCraftingIndex() == 8)
					setCraftingIndex(getCraftingIndex() + 3);
				else if (getCraftingIndex() == 9)
					setCraftingIndex(getCraftingIndex() + 5);
				else if (getCraftingIndex() == 10)
					setCraftingIndex(getCraftingIndex() + 7);
				player.getSkillResources().makeItem(1, CRAFTING, REQUIRED_ITEMS, CRAFTING_EXP, 
				getCraftingIndex());
				break;
			case 34193://Make3, option 3, 1
				if (getCraftingIndex() == 7)
					setCraftingIndex(getCraftingIndex() + 2);
				else if (getCraftingIndex() == 8)
					setCraftingIndex(getCraftingIndex() + 4);
				else if (getCraftingIndex() == 9)
					setCraftingIndex(getCraftingIndex() + 6);
				else if (getCraftingIndex() == 10)
					setCraftingIndex(getCraftingIndex() + 8);
				player.getSkillResources().makeItem(1, CRAFTING, REQUIRED_ITEMS, CRAFTING_EXP, 
				getCraftingIndex());
				break;
			case 34184://Make3, option 1, 5
				if (getCraftingIndex() == 7)
					setCraftingIndex(getCraftingIndex() + 0);
				else if (getCraftingIndex() == 8)
					setCraftingIndex(getCraftingIndex() + 2);
				else if (getCraftingIndex() == 9)
					setCraftingIndex(getCraftingIndex() + 4);
				else if (getCraftingIndex() == 10)
					setCraftingIndex(getCraftingIndex() + 6);
				player.getSkillResources().makeItem(5, CRAFTING, REQUIRED_ITEMS, CRAFTING_EXP, 
				getCraftingIndex());
				break;
			case 34188://Make3, option 2, 5
				if (getCraftingIndex() == 7)
					setCraftingIndex(getCraftingIndex() + 1);
				else if (getCraftingIndex() == 8)
					setCraftingIndex(getCraftingIndex() + 3);
				else if (getCraftingIndex() == 9)
					setCraftingIndex(getCraftingIndex() + 5);
				else if (getCraftingIndex() == 10)
					setCraftingIndex(getCraftingIndex() + 7);
				player.getSkillResources().makeItem(5, CRAFTING, REQUIRED_ITEMS, CRAFTING_EXP, 
				getCraftingIndex());
				break;
			case 34192://Make3, option 3, 5
				if (getCraftingIndex() == 7)
					setCraftingIndex(getCraftingIndex() + 2);
				else if (getCraftingIndex() == 8)
					setCraftingIndex(getCraftingIndex() + 4);
				else if (getCraftingIndex() == 9)
					setCraftingIndex(getCraftingIndex() + 6);
				else if (getCraftingIndex() == 10)
					setCraftingIndex(getCraftingIndex() + 8);
				player.getSkillResources().makeItem(5, CRAFTING, REQUIRED_ITEMS, CRAFTING_EXP, 
				getCraftingIndex());
				break;
			case 34183://Make3, option 1, 10
				if (getCraftingIndex() == 7)
					setCraftingIndex(getCraftingIndex() + 0);
				else if (getCraftingIndex() == 8)
					setCraftingIndex(getCraftingIndex() + 2);
				else if (getCraftingIndex() == 9)
					setCraftingIndex(getCraftingIndex() + 4);
				else if (getCraftingIndex() == 10)
					setCraftingIndex(getCraftingIndex() + 6);
				player.getSkillResources().makeItem(10, CRAFTING, REQUIRED_ITEMS, CRAFTING_EXP, 
				getCraftingIndex());
				break;
			case 34187://Make3, option 2, 10
				if (getCraftingIndex() == 7)
					setCraftingIndex(getCraftingIndex() + 1);
				else if (getCraftingIndex() == 8)
					setCraftingIndex(getCraftingIndex() + 3);
				else if (getCraftingIndex() == 9)
					setCraftingIndex(getCraftingIndex() + 5);
				else if (getCraftingIndex() == 10)
					setCraftingIndex(getCraftingIndex() + 7);
				player.getSkillResources().makeItem(10, CRAFTING, REQUIRED_ITEMS, CRAFTING_EXP, 
				getCraftingIndex());
				break;
			case 34191://Make3, option 3, 10
				if (getCraftingIndex() == 7)
					setCraftingIndex(getCraftingIndex() + 2);
				else if (getCraftingIndex() == 8)
					setCraftingIndex(getCraftingIndex() + 4);
				else if (getCraftingIndex() == 9)
					setCraftingIndex(getCraftingIndex() + 6);
				else if (getCraftingIndex() == 10)
					setCraftingIndex(getCraftingIndex() + 8);
				player.getSkillResources().makeItem(10, CRAFTING, REQUIRED_ITEMS, CRAFTING_EXP, 
				getCraftingIndex());
				break;
			case 34182://Make3, option 1, X(all)
				if (getCraftingIndex() == 7)
					setCraftingIndex(getCraftingIndex() + 0);
				else if (getCraftingIndex() == 8)
					setCraftingIndex(getCraftingIndex() + 2);
				else if (getCraftingIndex() == 9)
					setCraftingIndex(getCraftingIndex() + 4);
				else if (getCraftingIndex() == 10)
					setCraftingIndex(getCraftingIndex() + 6);
				player.getSkillResources().makeItem(28, CRAFTING, REQUIRED_ITEMS, CRAFTING_EXP, 
				getCraftingIndex());
				break;
			case 34186://Make3, option 2, X(all)
				if (getCraftingIndex() == 7)
					setCraftingIndex(getCraftingIndex() + 1);
				else if (getCraftingIndex() == 8)
					setCraftingIndex(getCraftingIndex() + 3);
				else if (getCraftingIndex() == 9)
					setCraftingIndex(getCraftingIndex() + 5);
				else if (getCraftingIndex() == 10)
					setCraftingIndex(getCraftingIndex() + 7);
				player.getSkillResources().makeItem(28, CRAFTING, REQUIRED_ITEMS, CRAFTING_EXP, 
				getCraftingIndex());
				break;
			case 34190://Make3, option 3, X(all)
				if (getCraftingIndex() == 7)
					setCraftingIndex(getCraftingIndex() + 2);
				else if (getCraftingIndex() == 8)
					setCraftingIndex(getCraftingIndex() + 4);
				else if (getCraftingIndex() == 9)
					setCraftingIndex(getCraftingIndex() + 6);
				else if (getCraftingIndex() == 10)
					setCraftingIndex(getCraftingIndex() + 8);
				player.getSkillResources().makeItem(28, CRAFTING, REQUIRED_ITEMS, CRAFTING_EXP, 
				getCraftingIndex());
				break;
			case 34245://Make5, option 1, 1
				if (getCraftingIndex() == 19)
					setCraftingIndex(getCraftingIndex() + 1);
				if (getCraftingIndex() == 24)
					setCraftingIndex(getCraftingIndex() + 0);
				player.getSkillResources().makeItem(1, CRAFTING, REQUIRED_ITEMS, CRAFTING_EXP, 
				getCraftingIndex());
				break;
			case 34244://Make5, option 1, 5
				if (getCraftingIndex() == 19)
					setCraftingIndex(getCraftingIndex() + 1);
				if (getCraftingIndex() == 24)
					setCraftingIndex(getCraftingIndex() + 0);
				player.getSkillResources().makeItem(5, CRAFTING, REQUIRED_ITEMS, CRAFTING_EXP, 
				getCraftingIndex());
				break;
			case 34243://Make5, option 1, 10
				if (getCraftingIndex() == 19)
					setCraftingIndex(getCraftingIndex() + 1);
				if (getCraftingIndex() == 24)
					setCraftingIndex(getCraftingIndex() + 0);
				player.getSkillResources().makeItem(10, CRAFTING, REQUIRED_ITEMS, CRAFTING_EXP, 
				getCraftingIndex());
				break;
			case 34242://Make5, option 1, X(all)
				if (getCraftingIndex() == 19)
					setCraftingIndex(getCraftingIndex() + 1);
				if (getCraftingIndex() == 24)
					setCraftingIndex(getCraftingIndex() + 0);
				player.getSkillResources().makeItem(28, CRAFTING, REQUIRED_ITEMS, CRAFTING_EXP, 
				getCraftingIndex());
				break;
			case 34249://Make5, option 2, 1
				if (getCraftingIndex() == 19)
					setCraftingIndex(getCraftingIndex() + 2);
				if (getCraftingIndex() == 24)
					setCraftingIndex(getCraftingIndex() + 1);
				player.getSkillResources().makeItem(1, CRAFTING, REQUIRED_ITEMS, CRAFTING_EXP, 
				getCraftingIndex());
				break;
			case 34248://Make5, option 2, 5
				if (getCraftingIndex() == 19)
					setCraftingIndex(getCraftingIndex() + 2);
				if (getCraftingIndex() == 24)
					setCraftingIndex(getCraftingIndex() + 1);
				player.getSkillResources().makeItem(5, CRAFTING, REQUIRED_ITEMS, CRAFTING_EXP, 
				getCraftingIndex());
				break;
			case 34247://Make5, option 2, 10
				if (getCraftingIndex() == 19)
					setCraftingIndex(getCraftingIndex() + 2);
				if (getCraftingIndex() == 24)
					setCraftingIndex(getCraftingIndex() + 1);
				player.getSkillResources().makeItem(10, CRAFTING, REQUIRED_ITEMS, CRAFTING_EXP, 
				getCraftingIndex());
				break;
			case 34246://Make5, option 2, X(all)
				if (getCraftingIndex() == 19)
					setCraftingIndex(getCraftingIndex() + 2);
				if (getCraftingIndex() == 24)
					setCraftingIndex(getCraftingIndex() + 1);
				player.getSkillResources().makeItem(28, CRAFTING, REQUIRED_ITEMS, CRAFTING_EXP, 
				getCraftingIndex());
				break;
			case 34253://Make5, option 3, 1
				if (getCraftingIndex() == 19)
					setCraftingIndex(getCraftingIndex() + 3);
				if (getCraftingIndex() == 24)
					setCraftingIndex(getCraftingIndex() + 2);
				player.getSkillResources().makeItem(1, CRAFTING, REQUIRED_ITEMS, CRAFTING_EXP, 
				getCraftingIndex());
				break;
			case 34252://Make5, option 3, 5
				if (getCraftingIndex() == 19)
					setCraftingIndex(getCraftingIndex() + 3);
				if (getCraftingIndex() == 24)
					setCraftingIndex(getCraftingIndex() + 2);
				player.getSkillResources().makeItem(5, CRAFTING, REQUIRED_ITEMS, CRAFTING_EXP, 
				getCraftingIndex());
				break;
			case 34251://Make5, option 3, 10
				if (getCraftingIndex() == 19)
					setCraftingIndex(getCraftingIndex() + 3);
				if (getCraftingIndex() == 24)
					setCraftingIndex(getCraftingIndex() + 2);
				player.getSkillResources().makeItem(10, CRAFTING, REQUIRED_ITEMS, CRAFTING_EXP, 
				getCraftingIndex());
				break;
			case 34250://Make5, option 3, X(all)
				if (getCraftingIndex() == 19)
					setCraftingIndex(getCraftingIndex() + 3);
				if (getCraftingIndex() == 24)
					setCraftingIndex(getCraftingIndex() + 2);
				player.getSkillResources().makeItem(28, CRAFTING, REQUIRED_ITEMS, CRAFTING_EXP, 
				getCraftingIndex());
				break;
			case 35001://Make5, option 4, 1
				if (getCraftingIndex() == 19)
					setCraftingIndex(getCraftingIndex() + 4);
				if (getCraftingIndex() == 24)
					setCraftingIndex(getCraftingIndex() + 3);	
				player.getSkillResources().makeItem(1, CRAFTING, REQUIRED_ITEMS, CRAFTING_EXP, 
				getCraftingIndex());
				break;
			case 35000://Make5, option 4, 5
				if (getCraftingIndex() == 19)
					setCraftingIndex(getCraftingIndex() + 4);
				if (getCraftingIndex() == 24)
					setCraftingIndex(getCraftingIndex() + 3);	
				player.getSkillResources().makeItem(5, CRAFTING, REQUIRED_ITEMS, CRAFTING_EXP, 
				getCraftingIndex());
				break;
			case 34255://Make5, option 4, 10
				if (getCraftingIndex() == 19)
					setCraftingIndex(getCraftingIndex() + 4);
				if (getCraftingIndex() == 24)
					setCraftingIndex(getCraftingIndex() + 3);	
				player.getSkillResources().makeItem(10, CRAFTING, REQUIRED_ITEMS, CRAFTING_EXP, 
				getCraftingIndex());
				break;
			case 34254://Make5, option 4, X(all)
				if (getCraftingIndex() == 19)
					setCraftingIndex(getCraftingIndex() + 4);
				if (getCraftingIndex() == 24)
					setCraftingIndex(getCraftingIndex() + 3);	
				player.getSkillResources().makeItem(28, CRAFTING, REQUIRED_ITEMS, CRAFTING_EXP, 
				getCraftingIndex());
				break;
			case 35005://Make5, option 5, 1
				if (getCraftingIndex() == 19)
					setCraftingIndex(getCraftingIndex() + 5);
				if (getCraftingIndex() == 24)
					setCraftingIndex(getCraftingIndex() + 4);	
				player.getSkillResources().makeItem(1, CRAFTING, REQUIRED_ITEMS, CRAFTING_EXP, 
				getCraftingIndex());
				break;
			case 35004://Make5, option 5, 5
				if (getCraftingIndex() == 19)
					setCraftingIndex(getCraftingIndex() + 5);
				if (getCraftingIndex() == 24)
					setCraftingIndex(getCraftingIndex() + 4);	
				player.getSkillResources().makeItem(5, CRAFTING, REQUIRED_ITEMS, CRAFTING_EXP, 
				getCraftingIndex());
				break;
			case 35003://Make5, option 5, 10
				if (getCraftingIndex() == 19)
					setCraftingIndex(getCraftingIndex() + 5);
				if (getCraftingIndex() == 24)
					setCraftingIndex(getCraftingIndex() + 4);	
				player.getSkillResources().makeItem(10, CRAFTING, REQUIRED_ITEMS, CRAFTING_EXP, 
				getCraftingIndex());
				break;
			case 35002://Make5, option 5, X(all)
				if (getCraftingIndex() == 19)
					setCraftingIndex(getCraftingIndex() + 5);
				if (getCraftingIndex() == 24)
					setCraftingIndex(getCraftingIndex() + 4);	
				player.getSkillResources().makeItem(28, CRAFTING, REQUIRED_ITEMS, CRAFTING_EXP, 
				getCraftingIndex());
				break;
				
			case 33187://Make Leather, Armour 1
				setCraftingIndex(0);
				player.getSkillResources().makeItem(1, CRAFTING, REQUIRED_ITEMS, CRAFTING_EXP, 
				getCraftingIndex());
				break;
			case 33190://Make Leather, Gloves 1
				setCraftingIndex(1);
				player.getSkillResources().makeItem(1, CRAFTING, REQUIRED_ITEMS, CRAFTING_EXP, 
				getCraftingIndex());
				break;
			case 33193://Make Leather, Boots 1
				setCraftingIndex(2);
				player.getSkillResources().makeItem(1, CRAFTING, REQUIRED_ITEMS, CRAFTING_EXP, 
				getCraftingIndex());
				break;
			case 33196://Make Leather, Vambraces 1
				setCraftingIndex(3);
				player.getSkillResources().makeItem(1, CRAFTING, REQUIRED_ITEMS, CRAFTING_EXP, 
				getCraftingIndex());
				break;
			case 33199://Make Leather, Chaps 1
				setCraftingIndex(4);
				player.getSkillResources().makeItem(1, CRAFTING, REQUIRED_ITEMS, CRAFTING_EXP, 
				getCraftingIndex());
				break;
			case 33202://Make Leather, Coif 1
				setCraftingIndex(5);
				player.getSkillResources().makeItem(1, CRAFTING, REQUIRED_ITEMS, CRAFTING_EXP, 
				getCraftingIndex());
				break;
			case 33205://Make Leather, Cowl 1
				setCraftingIndex(6);
				player.getSkillResources().makeItem(1, CRAFTING, REQUIRED_ITEMS, CRAFTING_EXP, 
				getCraftingIndex());
				break;
			case 33186://Make Leather, Armour 5
				setCraftingIndex(0);
				player.getSkillResources().makeItem(5, CRAFTING, REQUIRED_ITEMS, CRAFTING_EXP, 
				getCraftingIndex());
				break;
			case 33189://Make Leather, Gloves 5
				setCraftingIndex(1);
				player.getSkillResources().makeItem(5, CRAFTING, REQUIRED_ITEMS, CRAFTING_EXP, 
				getCraftingIndex());
				break;
			case 33192://Make Leather, Boots 5
				setCraftingIndex(2);
				player.getSkillResources().makeItem(5, CRAFTING, REQUIRED_ITEMS, CRAFTING_EXP, 
				getCraftingIndex());
				break;
			case 33195://Make Leather, Vambraces 5
				setCraftingIndex(3);
				player.getSkillResources().makeItem(5, CRAFTING, REQUIRED_ITEMS, CRAFTING_EXP, 
				getCraftingIndex());
				break;
			case 33198://Make Leather, Chaps 5
				setCraftingIndex(4);
				player.getSkillResources().makeItem(5, CRAFTING, REQUIRED_ITEMS, CRAFTING_EXP, 
				getCraftingIndex());
				break;
			case 33201://Make Leather, Coif 5
				setCraftingIndex(5);
				player.getSkillResources().makeItem(5, CRAFTING, REQUIRED_ITEMS, CRAFTING_EXP, 
				getCraftingIndex());
				break;
			case 33204://Make Leather, Cowl 5
				setCraftingIndex(6);
				player.getSkillResources().makeItem(5, CRAFTING, REQUIRED_ITEMS, CRAFTING_EXP, 
				getCraftingIndex());
				break;
			case 33185://Make Leather, Armour 10
				setCraftingIndex(0);
				player.getSkillResources().makeItem(10, CRAFTING, REQUIRED_ITEMS, CRAFTING_EXP, 
				getCraftingIndex());
				break;
			case 33188://Make Leather, Gloves 10
				setCraftingIndex(1);
				player.getSkillResources().makeItem(10, CRAFTING, REQUIRED_ITEMS, CRAFTING_EXP, 
				getCraftingIndex());
				break;
			case 33191://Make Leather, Boots 10
				setCraftingIndex(2);
				player.getSkillResources().makeItem(10, CRAFTING, REQUIRED_ITEMS, CRAFTING_EXP, 
				getCraftingIndex());
				break;
			case 33194://Make Leather, Vambraces 10
				setCraftingIndex(3);
				player.getSkillResources().makeItem(10, CRAFTING, REQUIRED_ITEMS, CRAFTING_EXP, 
				getCraftingIndex());
				break;
			case 33197://Make Leather, Chaps 10
				setCraftingIndex(4);
				player.getSkillResources().makeItem(10, CRAFTING, REQUIRED_ITEMS, CRAFTING_EXP, 
				getCraftingIndex());
				break;
			case 33200://Make Leather, Coif 10
				setCraftingIndex(5);
				player.getSkillResources().makeItem(10, CRAFTING, REQUIRED_ITEMS, CRAFTING_EXP, 
				getCraftingIndex());
				break;
			case 33203://Make Leather, Armour 10
				setCraftingIndex(6);
				player.getSkillResources().makeItem(10, CRAFTING, REQUIRED_ITEMS, CRAFTING_EXP, 
				getCraftingIndex());
				break;
			case 9118://Make Leather, Close
				player.getActionSender().removeInterfaces();
				break;
		}
	}
	
	public void tanningHideButtons(int buttonId) {
		switch (buttonId) {
			//tanHide(amount, tanningIndex);
			case 57225:
				player.getCrafting().tanHide(1, 0);
				break;
			case 57226:
				player.getCrafting().tanHide(1, 1);
				break;
			case 57227:
				player.getCrafting().tanHide(1, 2);
				break;
			case 57228:
				player.getCrafting().tanHide(1, 3);
				break;
			case 57229:
				player.getCrafting().tanHide(1, 4);
				break;
			case 57230:
				player.getCrafting().tanHide(1, 5);
				break;
			case 57231:
				player.getCrafting().tanHide(1, 6);
				break;
			case 57217:
				player.getCrafting().tanHide(5, 0);
				break;
			case 57218:
				player.getCrafting().tanHide(5, 1);
				break;
			case 57219:
				player.getCrafting().tanHide(5, 2);
				break;
			case 57220:
				player.getCrafting().tanHide(5, 3);
				break;
			case 57221:
				player.getCrafting().tanHide(5, 4);
				break;
			case 57222:
				player.getCrafting().tanHide(5, 5);
				break;
			case 57223:
				player.getCrafting().tanHide(5, 6);
				break;
			case 57209:
			case 57201:	
				player.getCrafting().tanHide(player.getInventory().getItemContainer().getCount(player.getCrafting().TANNING_HIDE[0][0]), 0);
				break;
			case 57210:
			case 57202:	
				player.getCrafting().tanHide(player.getInventory().getItemContainer().getCount(player.getCrafting().TANNING_HIDE[1][0]), 1);
				break;
			case 57211:
			case 57203:	
				player.getCrafting().tanHide(player.getInventory().getItemContainer().getCount(player.getCrafting().TANNING_HIDE[2][0]), 2);
				break;
			case 57212:
			case 57204:	
				player.getCrafting().tanHide(player.getInventory().getItemContainer().getCount(player.getCrafting().TANNING_HIDE[3][0]), 3);
				break;
			case 57213:
			case 57205:	
				player.getCrafting().tanHide(player.getInventory().getItemContainer().getCount(player.getCrafting().TANNING_HIDE[4][0]), 4);
				break;
			case 57214:
			case 57206:	
				player.getCrafting().tanHide(player.getInventory().getItemContainer().getCount(player.getCrafting().TANNING_HIDE[5][0]), 5);
				break;
			case 57215:
			case 57207:	
				player.getCrafting().tanHide(player.getInventory().getItemContainer().getCount(player.getCrafting().TANNING_HIDE[6][0]), 6);
				break;
		}
	}
	
	public void setCraftingIndex(int craftingIndex) {
		this.craftingIndex = craftingIndex;
	}
	
	public int getCraftingIndex() {
		return craftingIndex;
	}
	
}