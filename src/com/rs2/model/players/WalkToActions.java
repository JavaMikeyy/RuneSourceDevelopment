package com.rs2.model.players;

import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.tick.Tick;
import com.rs2.model.npcs.Npc;

public class WalkToActions {
	
	private static Actions actions = Actions.OBJECT_FIRST_CLICK;
	
	public static void doActions(Player player) {
		switch (actions) {
		case OBJECT_FIRST_CLICK:
			doObjectFirstClick(player);
			break;
		case OBJECT_SECOND_CLICK:
			doObjectSecondClick(player);
			break;
		case OBJECT_THIRD_CLICK:
			doObjectThirdClick(player);
			break;
		case NPC_FIRST_CLICK:
			doNpcFirstClick(player);
			break;
		case NPC_SECOND_CLICK:
			doNpcSecondClick(player);
			break;
		case NPC_THIRD_CLICK:
			break;
		case ITEM_ON_OBJECT:
			doItemOnObject(player);
			break;
		}
		player.getUpdateFlags().sendFaceToDirection(new Position(player.getClickX(), player.getClickY()));
	}
	
	public static void doObjectFirstClick(final Player player) {
		player.setWalkToAction(new Tick(1) {
			@Override
			public void execute() {
				int x = player.getClickX();
				int y = player.getClickY();
				if (!player.getMovementHandler().walkToAction(new Position(x, y), getObjectSize(player.getClickId()))) {
					return;
				}
				player.getRunecrafting().clickObjects(player.getClickId());
				switch (player.getClickId()) {
				case 2213:
				case 11758:
					BankManager.openBank(player);
					break;
				}
				actions = null;
				this.stop();
			}
		});
	}
	
	public static void doObjectSecondClick(final Player player) {
		player.setWalkToAction(new Tick(1) {
			@Override
			public void execute() {
				int x = player.getClickX();
				int y = player.getClickY();
				if (!player.getMovementHandler().walkToAction(new Position(x, y), 1)) {
					return;
				}
				switch (player.getClickId()) {
				case 2213:
				case 11758:
					BankManager.openBank(player);
					break;
				}
				actions = null;
				this.stop();
			}
		});
	}
	
	public static void doObjectThirdClick(final Player player) {
		player.setWalkToAction(new Tick(1) {
			@Override
			public void execute() {
				int x = player.getClickX();
				int y = player.getClickY();
				if (!player.getMovementHandler().walkToAction(new Position(x, y), 1)) {
					return;
				}
				switch (player.getClickId()) {
				
				}
				actions = null;
				this.stop();
			}
		});
	}
	
	private static void doNpcFirstClick(final Player player) {
		for (int i = 0; i < player.getFishing().FISHING.length; i++) {
			if (player.getClickId() == player.getFishing().FISHING[i][0]) {
				player.getFishing().startFishing(player.getClickId(), 1);
				return;
			}
		}
		for (int i = 0; i < player.getPets().PET_IDS.length; i++) {
			if (player.getClickId() == player.getPets().PET_IDS[i][1]) {
				player.getPets().unregisterPet();
				return;
			}
		}
		player.setWalkToAction(new Tick(1) {
			@Override
			public void execute() {
				int x = player.getClickX();
				int y = player.getClickY();
				if (!player.getMovementHandler().walkToAction(new Position(x, y), 1)) {
					return;
				}
				Npc npc = World.getNpcs()[player.getNpcClickIndex()];
				npc.getUpdateFlags().faceEntity(player.getIndex() + 32768);
				switch (player.getClickId()) {
					case 494:
						player.getDialogue().sendDialogue(2);
						break;
					case 2244:
						player.getDialogue().sendDialogue(8);
						break;
					case 1597:
						player.getDialogue().sendDialogue(22);
						break;
					case 599:
						player.getDialogue().sendDialogue(32);
						break;
					default:
						player.getDialogue().sendDialogue(1);
						break;
				}
				actions = null;
				this.stop();
			}
		});
	}
	
	private static void doNpcSecondClick(final Player player) {
		player.getFishing().startFishing(player.getClickId(), 2);
		player.setWalkToAction(new Tick(1) {
			@Override
			public void execute() {
				int x = player.getClickX();
				int y = player.getClickY();
				if (!player.getMovementHandler().walkToAction(new Position(x, y), 1)) {
					return;
				}
				Npc npc = World.getNpcs()[player.getNpcClickIndex()];
				npc.getUpdateFlags().faceEntity(player.getIndex() + 32768);
				switch (player.getClickId()) {
				case 571:
					ShopManager.openShop(player, 0);
					break;
				case 553:
					ShopManager.openShop(player, 1);
					break;
				case 546:
					ShopManager.openShop(player, 2);
					break;
				case 550:
					ShopManager.openShop(player, 3);
					break;
				case 538:
					ShopManager.openShop(player, 4);
					break;
				case 1597:
					ShopManager.openShop(player, 5);
					break;
				case 520:
				case 521:
					ShopManager.openShop(player, 6);
					break;
				}
				actions = null;
				this.stop();
			}
		});
	}
	
	private static void doItemOnObject(final Player player) {
		final int x = player.getClickX();
		final int y = player.getClickY();
		final int id = player.getClickId();
		final int size = getObjectSize(id);
		player.setWalkToAction(new Tick(1) {
			@Override
			public void execute() {
				if (!player.getMovementHandler().walkToAction(new Position(x, y), getObjectSize(player.getClickId()))) {
					return;
				}
				if (player.getClickId() == 11661 && player.getMiscId() == 229) {
					player.getHerblore().fillEmptyVial();
				}
				if (player.getClickId() == 11666 && player.getMiscId() == 2357) {
					player.getSkillResources().setSkillId(player.getSkill().CRAFTING);
					//jewelry
				}
				if (player.getClickId() == 2642 && player.getMiscId() == 1761) {
					player.getSkillResources().setSkillId(player.getSkill().CRAFTING);
					player.getCrafting().setCraftingIndex(24);
					player.getSkillInterfaces().sendMake5Interface(1787, 1789, 1791, 5352, 4438);
				}
				if (player.getClickId() == 11601) {
					switch (player.getMiscId()) {
						case 1787:
							player.getSkillResources().setSkillId(player.getSkill().CRAFTING);
							player.getCrafting().setCraftingIndex(29);
							player.getSkillInterfaces().sendMake1Interface(1931, 150);
							break;
						case 1789:
							player.getSkillResources().setSkillId(player.getSkill().CRAFTING);
							player.getCrafting().setCraftingIndex(30);
							player.getSkillInterfaces().sendMake1Interface(2313);
							break;
						case 1791:
							player.getSkillResources().setSkillId(player.getSkill().CRAFTING);
							player.getCrafting().setCraftingIndex(31);
							player.getSkillInterfaces().sendMake1Interface(1923);
							break;
						case 5352:
							player.getSkillResources().setSkillId(player.getSkill().CRAFTING);
							player.getCrafting().setCraftingIndex(32);
							player.getSkillInterfaces().sendMake1Interface(5350);
							break;
						case 4438:
							player.getSkillResources().setSkillId(player.getSkill().CRAFTING);
							player.getCrafting().setCraftingIndex(33);
							player.getSkillInterfaces().sendMake1Interface(4440);
							break;
					}
				}
				for (int i = 0; i < player.getCooking().COOKING_OBJECTS.length; i++) {
					if (player.getClickId() == player.getCooking().COOKING_OBJECTS[i][0]) {
						for (int i2 = 0; i2 < player.getCooking().REQUIRED_ITEMS.length; i2++) {
							if (player.getMiscId() == player.getCooking().REQUIRED_ITEMS[i2][0]) {
								player.getSkillResources().setSkillId(player.getSkill().COOKING);
								player.getCooking().sendCookingInterface(player.getCooking().REQUIRED_ITEMS[i2][0], i2);
							}
						}
					}
				}
				switch (player.getClickId()) {
				case 2638:
					player.getActionSender().sendMessage("You dip your amulet into the fountain...");
					player.getUpdateFlags().sendAnimation(827, 0);
					for (int i = 0; i < Inventory.SIZE; i ++) {
						int[] glorys = {1704, 1706, 1708, 1710, 1712};
						for (int glory : glorys) {
							if (player.getInventory().getItemContainer().contains(glory)) {
								player.getInventory().addItemToSlot(new Item(1712,
										1), player.getInventory().
										getItemContainer().getSlotById(glory));
							}
						}
					}
					break;
				}
				actions = null;
				this.stop();
			}
		});
		switch (player.getClickId()) {
		
		}
	}
	
	private static int getObjectSize(int objectId) {
		switch (objectId) {
			case 2486:
			case 2484:
			case 2482:
			case 2481:
			case 2483:
			case 2479:
			case 2478:
			case 2480:
			case 2485:
			case 2487:
			case 2488:
				return 4;
			case 11661:
				return 3;
		}
		return 1;
	}

	public static void setActions(Actions actions) {
		WalkToActions.actions = actions;
	}

	public static Actions getActions() {
		return actions;
	}
	
	public static enum Actions {

		OBJECT_FIRST_CLICK, OBJECT_SECOND_CLICK, OBJECT_THIRD_CLICK, ITEM_ON_OBJECT,

		NPC_FIRST_CLICK, NPC_SECOND_CLICK, NPC_THIRD_CLICK
	}

}
