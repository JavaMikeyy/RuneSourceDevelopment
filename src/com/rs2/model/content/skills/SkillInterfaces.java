package com.rs2.model.content.skills;

import com.rs2.model.World;
import com.rs2.model.tick.Tick;
import com.rs2.model.players.Player;
import com.rs2.model.players.Item;
import com.rs2.model.players.ItemManager;

/**
  * By Mikey` of Rune-Server (MSN: metallic_mike@yahoo.com)
  */

public class SkillInterfaces {

	private Player player;
	
	public SkillInterfaces(Player player) {
		this.player = player;
	}
	
	public static int[][] DHIDE_ARMOUR_INTERFACE =
	{//leatherId, bodyId, vambsId, chapsId
	//Green                    Blue                     Red                Black                                                      
	{1745, 1135, 1065, 1099}, {2505, 2499, 2487, 2493}, {2507, 2501, 2489, 2495}, {2509, 2503, 2491, 2497}
	};
	
	public void sendMake1Interface(int itemId) {
		player.getActionSender().sendItemOnInterface(1746, 200, itemId);//Item 1
		player.getActionSender().sendChatInterface(4429);
	}
	
	public void sendMake1Interface(int itemId, int zoom) {
		player.getActionSender().sendItemOnInterface(1746, zoom, itemId);//Item 1
		player.getActionSender().sendChatInterface(4429);
	}
	
	public void sendMake2Interface(int itemId1, int itemId2) {
		player.getActionSender().sendItemOnInterface(8869, 200, itemId1);//Item 1
		player.getActionSender().sendItemOnInterface(8870, 200, itemId2);//Item 2
		player.getActionSender().sendChatInterface(8866);
	}
	
	public void sendMake3Interface(int itemId1, int itemId2, int itemId3) {
		player.getActionSender().sendItemOnInterface(8883, 200, itemId1);//Item 1
		player.getActionSender().sendItemOnInterface(8884, 200, itemId2);//Item 2
		player.getActionSender().sendItemOnInterface(8885, 200, itemId3);//Item 3
		player.getActionSender().sendChatInterface(8880);
	}
	
	public void sendMake4Interface(int itemId1, int itemId2, int itemId3, int itemId4) {
		player.getActionSender().sendItemOnInterface(8902, 200, itemId1);//Item 1
		player.getActionSender().sendItemOnInterface(8903, 200, itemId2);//Item 2
		player.getActionSender().sendItemOnInterface(8904, 200, itemId3);//Item 3
		player.getActionSender().sendItemOnInterface(8905, 200, itemId4);//Item 4
		player.getActionSender().sendChatInterface(8899);
	}
	
	public void sendMake5Interface(int itemId1, int itemId2, int itemId3, int itemId4, int itemId5) {
		player.getActionSender().sendItemOnInterface(8941, 150, itemId1);//Item 1
		player.getActionSender().sendItemOnInterface(8942, 150, itemId2);//Item 2
		player.getActionSender().sendItemOnInterface(8943, 150, itemId3);//Item 3
		player.getActionSender().sendItemOnInterface(8944, 150, itemId4);//Item 4
		player.getActionSender().sendItemOnInterface(8945, 150, itemId5);//Item 5
		player.getActionSender().sendChatInterface(8938);
	}
	
	public void sendLeatherCraftingInterface() {
		player.getActionSender().sendInterface(2311);
	}
	
	public void sendTanningInterface() {
		for (int i = 0; i < player.getCrafting().TANNING_HIDE.length; i++) {
			player.getActionSender().sendItemOnInterface(14769 + i, 250, player.getCrafting().TANNING_HIDE[i][1]);
			player.getActionSender().sendString(ItemManager.getInstance().getItemName(player.getCrafting().TANNING_HIDE[i][1]), 14777 + i);
			player.getActionSender().sendString(player.getCrafting().TANNING_HIDE[i][2] + " Coins", 14785 + i);
		}
		player.getActionSender().sendString("", 14784);
		player.getActionSender().sendString("", 14792);
		player.getActionSender().sendInterface(14670);
	}
	
	public void sendCraftingJewelryInterface(int item1, int item2, int item3, int item4, int item5, int item6, int item7) {
		player.getActionSender().sendItemOnInterface(2405, 140, item1);//Item 1
		player.getActionSender().sendString("", 3987);
		player.getActionSender().sendItemOnInterface(2406, 150, item2);//Item 2
		player.getActionSender().sendString("", 3991);
		player.getActionSender().sendItemOnInterface(2407, 150, item3);//Item 3
		player.getActionSender().sendString("", 3995);
		player.getActionSender().sendItemOnInterface(2409, 150, item4);//Item 4
		player.getActionSender().sendString("", 3999);
		player.getActionSender().sendItemOnInterface(2410, 150, item5);//Item 5
		player.getActionSender().sendString("", 4003);
		player.getActionSender().sendItemOnInterface(2411, 150, item6);//Item 6
		player.getActionSender().sendString("", 7441);
		player.getActionSender().sendItemOnInterface(2412, 150, item7);//Item 7
		player.getActionSender().sendString("", 7446);
		player.getActionSender().sendItemOnInterface(2413, 1, 0);//Item 8
		player.getActionSender().sendString("", 7450);
		player.getActionSender().sendString("What would you like to make?", 2403);
		player.getActionSender().sendChatInterface(2400);
	}
	
	public void clickingInterfaceButtons(int buttonId) {
		switch (buttonId) {//Interface, Amount
			case 10239://Make1, 1
				if (player.getSkillResources().getSkillId() == player.getSkill().CRAFTING)
					player.getCrafting().clickingInterfaceButtons(buttonId);
				if (player.getSkillResources().getSkillId() == player.getSkill().HERBLORE)
					player.getHerblore().clickingInterfaceButtons(buttonId);
				break;
			case 10238://Make1, 5
				if (player.getSkillResources().getSkillId() == player.getSkill().CRAFTING)
					player.getCrafting().clickingInterfaceButtons(buttonId);
				if (player.getSkillResources().getSkillId() == player.getSkill().HERBLORE)
					player.getHerblore().clickingInterfaceButtons(buttonId);
				break;
			case 6212://Make1, X
			case 6211://Make1, All
				if (player.getSkillResources().getSkillId() == player.getSkill().CRAFTING)
					player.getCrafting().clickingInterfaceButtons(buttonId);
				if (player.getSkillResources().getSkillId() == player.getSkill().HERBLORE)
					player.getHerblore().clickingInterfaceButtons(buttonId);
				break;
			case 34185://Make3, option 1, 1
				if (player.getSkillResources().getSkillId() == player.getSkill().CRAFTING)
					player.getCrafting().clickingInterfaceButtons(buttonId);
				break;
			case 34189://Make3, option 2, 1
				if (player.getSkillResources().getSkillId() == player.getSkill().CRAFTING)
					player.getCrafting().clickingInterfaceButtons(buttonId);
				break;
			case 34193://Make3, option 3, 1
				if (player.getSkillResources().getSkillId() == player.getSkill().CRAFTING)
					player.getCrafting().clickingInterfaceButtons(buttonId);
				break;
			case 34184://Make3, option 1, 5
				if (player.getSkillResources().getSkillId() == player.getSkill().CRAFTING)
					player.getCrafting().clickingInterfaceButtons(buttonId);
				break;
			case 34188://Make3, option 2, 5
				if (player.getSkillResources().getSkillId() == player.getSkill().CRAFTING)
					player.getCrafting().clickingInterfaceButtons(buttonId);
				break;
			case 34192://Make3, option 3, 5
				if (player.getSkillResources().getSkillId() == player.getSkill().CRAFTING)
					player.getCrafting().clickingInterfaceButtons(buttonId);
				break;
			case 34183://Make3, option 1, 10
				if (player.getSkillResources().getSkillId() == player.getSkill().CRAFTING)
					player.getCrafting().clickingInterfaceButtons(buttonId);
				break;
			case 34187://Make3, option 2, 10
				if (player.getSkillResources().getSkillId() == player.getSkill().CRAFTING)
					player.getCrafting().clickingInterfaceButtons(buttonId);
				break;
			case 34191://Make3, option 3, 10
				if (player.getSkillResources().getSkillId() == player.getSkill().CRAFTING)
					player.getCrafting().clickingInterfaceButtons(buttonId);
				break;
			case 34182://Make3, option 1, X(all)
				if (player.getSkillResources().getSkillId() == player.getSkill().CRAFTING)
					player.getCrafting().clickingInterfaceButtons(buttonId);
				break;
			case 34186://Make3, option 2, X(all)
				if (player.getSkillResources().getSkillId() == player.getSkill().CRAFTING)
					player.getCrafting().clickingInterfaceButtons(buttonId);
				break;
			case 34190://Make3, option 3, X(all)
				if (player.getSkillResources().getSkillId() == player.getSkill().CRAFTING)
					player.getCrafting().clickingInterfaceButtons(buttonId);
				break;
			case 34245://Make5, option 1, 1
				if (player.getSkillResources().getSkillId() == player.getSkill().CRAFTING)
					player.getCrafting().clickingInterfaceButtons(buttonId);
				break;
			case 34244://Make5, option 1, 5
				if (player.getSkillResources().getSkillId() == player.getSkill().CRAFTING)
				player.getCrafting().clickingInterfaceButtons(buttonId);
				break;
			case 34243://Make5, option 1, 10
				if (player.getSkillResources().getSkillId() == player.getSkill().CRAFTING)
					player.getCrafting().clickingInterfaceButtons(buttonId);
				break;
			case 34242://Make5, option 1, X(all)
				if (player.getSkillResources().getSkillId() == player.getSkill().CRAFTING)
					player.getCrafting().clickingInterfaceButtons(buttonId);
				break;
			case 34249://Make5, option 2, 1
				if (player.getSkillResources().getSkillId() == player.getSkill().CRAFTING)
					player.getCrafting().clickingInterfaceButtons(buttonId);
				break;
			case 34248://Make5, option 2, 5
				if (player.getSkillResources().getSkillId() == player.getSkill().CRAFTING)
					player.getCrafting().clickingInterfaceButtons(buttonId);
				break;
			case 34247://Make5, option 2, 10
				if (player.getSkillResources().getSkillId() == player.getSkill().CRAFTING)
					player.getCrafting().clickingInterfaceButtons(buttonId);
				break;
			case 34246://Make5, option 2, X(all)
				if (player.getSkillResources().getSkillId() == player.getSkill().CRAFTING)
					player.getCrafting().clickingInterfaceButtons(buttonId);
				break;
			case 34253://Make5, option 3, 1
				if (player.getSkillResources().getSkillId() == player.getSkill().CRAFTING)
					player.getCrafting().clickingInterfaceButtons(buttonId);
				break;
			case 34252://Make5, option 3, 5
				if (player.getSkillResources().getSkillId() == player.getSkill().CRAFTING)
					player.getCrafting().clickingInterfaceButtons(buttonId);
				break;
			case 34251://Make5, option 3, 10
				if (player.getSkillResources().getSkillId() == player.getSkill().CRAFTING)
					player.getCrafting().clickingInterfaceButtons(buttonId);
				break;
			case 34250://Make5, option 3, X(all)
				if (player.getSkillResources().getSkillId() == player.getSkill().CRAFTING)
					player.getCrafting().clickingInterfaceButtons(buttonId);
				break;
			case 35001://Make5, option 4, 1
				if (player.getSkillResources().getSkillId() == player.getSkill().CRAFTING)
					player.getCrafting().clickingInterfaceButtons(buttonId);
				break;
			case 35000://Make5, option 4, 5
				if (player.getSkillResources().getSkillId() == player.getSkill().CRAFTING)
					player.getCrafting().clickingInterfaceButtons(buttonId);
				break;
			case 34255://Make5, option 4, 10
				if (player.getSkillResources().getSkillId() == player.getSkill().CRAFTING)
					player.getCrafting().clickingInterfaceButtons(buttonId);
				break;
			case 34254://Make5, option 4, X(all)
				if (player.getSkillResources().getSkillId() == player.getSkill().CRAFTING)
					player.getCrafting().clickingInterfaceButtons(buttonId);
				break;
			case 35005://Make5, option 5, 1
				if (player.getSkillResources().getSkillId() == player.getSkill().CRAFTING)
					player.getCrafting().clickingInterfaceButtons(buttonId);
				break;
			case 35004://Make5, option 5, 5
				if (player.getSkillResources().getSkillId() == player.getSkill().CRAFTING)
					player.getCrafting().clickingInterfaceButtons(buttonId);
				break;
			case 35003://Make5, option 5, 10
				if (player.getSkillResources().getSkillId() == player.getSkill().CRAFTING)
					player.getCrafting().clickingInterfaceButtons(buttonId);
				break;
			case 35002://Make5, option 5, X(all)
				if (player.getSkillResources().getSkillId() == player.getSkill().CRAFTING)
					player.getCrafting().clickingInterfaceButtons(buttonId);
				break;
			case 9118://Make Leather, Close
			case 33203://Make Leather, Armour 10
			case 33200://Make Leather, Coif 10
			case 33197://Make Leather, Chaps 10
			case 33194://Make Leather, Vambraces 10
			case 33191://Make Leather, Boots 10
			case 33188://Make Leather, Gloves 10
			case 33185://Make Leather, Armour 10
			case 33204://Make Leather, Cowl 5
			case 33201://Make Leather, Coif 5
			case 33198://Make Leather, Chaps 5
			case 33195://Make Leather, Vambraces 5
			case 33192://Make Leather, Boots 5
			case 33189://Make Leather, Gloves 5
			case 33186://Make Leather, Armour 5
			case 33205://Make Leather, Cowl 1
			case 33202://Make Leather, Coif 1
			case 33199://Make Leather, Chaps 1
			case 33196://Make Leather, Vambraces 1
			case 33193://Make Leather, Boots 1
			case 33190://Make Leather, Gloves 1
			case 33187://Make Leather, Armour 1
				player.getCrafting().clickingInterfaceButtons(buttonId);
				break;
		}
	}
	
}
