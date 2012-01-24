package com.rs2.net.packet.packets;

import com.rs2.model.Position;
import com.rs2.model.players.BankManager;
import com.rs2.model.players.Item;
import com.rs2.model.players.ItemManager;
import com.rs2.model.players.Player;
import com.rs2.model.players.ShopManager;
import com.rs2.model.players.TradeManager;
import com.rs2.net.StreamBuffer;
import com.rs2.net.packet.Packet;
import com.rs2.net.packet.PacketManager.PacketHandler;

public class ItemPacketHandler implements PacketHandler {
	
	public static final int ITEM_OPERATE = 75;
	public static final int DROP_ITEM = 87;
	public static final int PICKUP_ITEM = 236;
	public static final int HANDLE_OPTIONS = 214;
	public static final int PACKET_145 = 145;
	public static final int PACKET_117 = 117;
	public static final int PACKET_43 = 43;
	public static final int PACKET_129 = 129;
	public static final int EQUIP_ITEM = 41;
	public static final int USE_ITEM_ON_ITEM = 53;
	public static final int FIRST_CLICK_ITEM = 122;
	public static final int THIRD_CLICK_ITEM = 16;
	
	@Override
	public void handlePacket(Player player, Packet packet) {
		switch (packet.getOpcode()) {
		case USE_ITEM_ON_ITEM:
			useItemOnItem(player, packet);
			break;
		case ITEM_OPERATE:
			handleItemOperate(player, packet);
			break;
		case DROP_ITEM:
			handleDropItem(player, packet);
			break;
		case PICKUP_ITEM:
			handlePickupItem(player, packet);
			break;
		case HANDLE_OPTIONS:
			handleOptions(player, packet);
			break;
		case PACKET_145:
			handlePacket145(player, packet);
			break;
		case PACKET_117:
			handlePacket117(player, packet);
			break;
		case PACKET_43:
			handlePacket43(player, packet);
			break;
		case PACKET_129:
			handlePacket129(player, packet);
			break;
		case EQUIP_ITEM:
			handleEquipItem(player, packet);
			break;
		case FIRST_CLICK_ITEM:
			handleFirstClickItem(player, packet);
			break;
		case THIRD_CLICK_ITEM:
			handleThirdClickItem(player, packet);
			break;
		}
	}
	
	private void handleItemOperate(Player player, Packet packet) {
		packet.getIn().readShort(true, StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
		packet.getIn().readShort(true, StreamBuffer.ValueType.A);
		int itemId = packet.getIn().readShort(true, StreamBuffer.ValueType.A);
	}
	
	private void handleDropItem(Player player, Packet packet) {
		int groundItem = packet.getIn().readShort(StreamBuffer.ValueType.A);
		packet.getIn().readShort();
		int itemSlot = packet.getIn().readShort(StreamBuffer.ValueType.A);
		Item item = new Item(groundItem);
		for (int i = 0; i < player.getPets().PET_IDS.length; i++) {
			if (item.getDefinition().getId() == player.getPets().PET_IDS[i][0]) {
				player.getPets().registerPet(player.getPets().PET_IDS[i][0], player.getPets().PET_IDS[i][1]);
				return;
			}
		}
		if (item.getDefinition().isStackable()) {
			item.setCount(player.getInventory().getItemContainer().getCount(item.getId()));
		} else {
			item.setCount(1);
		}
		if (!player.getInventory().getItemContainer().contains(item.getId())) {
			return;
		}
		if (player.getInventory().getItemContainer().contains(item.getId())) {
				ItemManager.getInstance().createGroundItem(player, player.getUsername(), 
						new Item(groundItem, item.getCount()), 
						new Position(player.getPosition().getX(), 
								player.getPosition().getY(),player.getPosition().getZ()));
				player.getInventory().removeItemSlot(item, itemSlot);
				}
	}
	
	private void useItemOnItem(Player player, Packet packet) {
		int itemSecondClickSlot = packet.getIn().readShort();
		int itemFirstClickSlot = packet.getIn().readShort(
				StreamBuffer.ValueType.A);
		packet.getIn().readShort();
		packet.getIn().readShort();
		Item firstClickItem = player.getInventory().getItemContainer().get(
				itemFirstClickSlot);
		Item secondClickItem = player.getInventory().getItemContainer().get(
				itemSecondClickSlot);
		
		if (firstClickItem.getId() == 1755 || secondClickItem.getId() == 1755) {
			for (int i = 0; i < player.getCrafting().CRAFTING.length - 34; i++) {
				if (firstClickItem.getId() == player.getCrafting().REQUIRED_ITEMS[i + 34][0]) {
					player.getSkillInterfaces().sendMake1Interface(player.getCrafting().CRAFTING[i + 34][1]);
					player.getSkillResources().setSkillId(player.getSkill().CRAFTING);
					player.getCrafting().setCraftingIndex(i + 34);
					return;
				}
				else if (secondClickItem.getId() == player.getCrafting().REQUIRED_ITEMS[i + 34][0]) {
					player.getSkillInterfaces().sendMake1Interface(player.getCrafting().CRAFTING[i + 34][1]);
					player.getSkillResources().setSkillId(player.getSkill().CRAFTING);
					player.getCrafting().setCraftingIndex(i + 34);
					return;
				}
			}
		}
		
		if (firstClickItem.getId() == 1733 || secondClickItem.getId() == 1733) {
			for (int i = 0; i < 7; i++) {
				if (firstClickItem.getId() == player.getCrafting().REQUIRED_ITEMS[i][0] ||
				secondClickItem.getId() == player.getCrafting().REQUIRED_ITEMS[i][0]) {
					player.getSkillResources().setSkillId(player.getSkill().CRAFTING);
					player.getSkillInterfaces().sendLeatherCraftingInterface();
					return;
				}
			}
			for (int i = 0; i < player.getSkillInterfaces().DHIDE_ARMOUR_INTERFACE.length; i++) {
				if (firstClickItem.getId() == player.getSkillInterfaces().DHIDE_ARMOUR_INTERFACE[i][0] ||
				secondClickItem.getId() == player.getSkillInterfaces().DHIDE_ARMOUR_INTERFACE[i][0]) {
					player.getSkillResources().setSkillId(player.getSkill().CRAFTING);
					player.getCrafting().setCraftingIndex(7 + i);
					player.getSkillInterfaces().sendMake3Interface(player.getSkillInterfaces().DHIDE_ARMOUR_INTERFACE[i][1], 
					player.getSkillInterfaces().DHIDE_ARMOUR_INTERFACE[i][2], player.getSkillInterfaces().DHIDE_ARMOUR_INTERFACE[i][3]);
					return;
				}
			}
			if (firstClickItem.getId() == 7801 || secondClickItem.getId() == 7801) {
					player.getSkillResources().setSkillId(player.getSkill().CRAFTING);
					player.getCrafting().setCraftingIndex(18);	
					player.getSkillInterfaces().sendMake5Interface(6322, 6324, 6330, 6326, 6328);
					return;
				}
		}
		
		if (firstClickItem.getId() == 434 && secondClickItem.getId() == 1929 
		|| firstClickItem.getId() == 1929 && secondClickItem.getId() == 434) {
			player.getInventory().removeItem(new Item(434, 1));
			player.getInventory().removeItem(new Item(1929, 1));
			player.getInventory().addItem(new Item(1761, 1));
			player.getActionSender().sendMessage("You add water to the clay to soften it up.");
		}
			
		if (firstClickItem.getId() == 227 || secondClickItem.getId() == 227) {
			for (int i = 0; i < player.getHerblore().REQUIRED_ITEMS.length; i++) {
				if (firstClickItem.getId() == player.getHerblore().REQUIRED_ITEMS[i][0] || 
				secondClickItem.getId() == player.getHerblore().REQUIRED_ITEMS[i][0]) {
					player.getSkillResources().setSkillId(player.getSkill().HERBLORE);
					player.getHerblore().setHerbloreIndex(i);
					player.getSkillInterfaces().sendMake1Interface(player.getHerblore().HERBLORE[i][1], 150);
				}
			}
		}
		
		for (int i = 0; i < player.getHerblore().REQUIRED_ITEMS.length; i++) {
			if (firstClickItem.getId() == player.getHerblore().REQUIRED_ITEMS[i][0] || 
			secondClickItem.getId() == player.getHerblore().REQUIRED_ITEMS[i][0]) {
				if (firstClickItem.getId() == player.getHerblore().REQUIRED_ITEMS[i][2] || 
				secondClickItem.getId() == player.getHerblore().REQUIRED_ITEMS[i][2]) {
					player.getSkillResources().setSkillId(player.getSkill().HERBLORE);
					player.getHerblore().setHerbloreIndex(i);
					player.getSkillInterfaces().sendMake1Interface(player.getHerblore().HERBLORE[i][1], 150);
				}
			}
		}
	}
	
	private void handlePickupItem(Player player, Packet packet) {
		player.setClickY(packet.getIn().readShort(StreamBuffer.ByteOrder.LITTLE));
		player.setClickId(packet.getIn().readShort());
		player.setClickX(packet.getIn().readShort(StreamBuffer.ByteOrder.LITTLE));
		if (player.getInventory().getItemContainer().freeSlots() == -1) {
			return;
		}
		if ((Boolean) player.getAttributes().get("canPickup")) {
			ItemManager.getInstance().pickupItem(player, player.getClickId(), 
				new Position(player.getClickX(), player.getClickY()));
			player.getAttributes().put("canPickup", Boolean.FALSE);
		}
	}
	
	private void handleOptions(Player player, Packet packet) {
		int interfaceId = packet.getIn().readShort(StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
		packet.getIn().readByte(StreamBuffer.ValueType.C);
		int fromSlot = packet.getIn().readShort(StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
		int toSlot = packet.getIn().readShort(StreamBuffer.ByteOrder.LITTLE);
		switch (interfaceId) {
		case 5382:
			BankManager.handleBankOptions(player, fromSlot, toSlot);
			break;
		case 3214:
			player.getInventory().swap(fromSlot, toSlot);
			player.getInventory().refresh();
			break;
		}
	}
	
	private void handlePacket145(Player player, Packet packet) {
		int interfaceID = packet.getIn().readShort(StreamBuffer.ValueType.A);
		int slot = packet.getIn().readShort(StreamBuffer.ValueType.A);
		int itemId = packet.getIn().readShort(StreamBuffer.ValueType.A);
		System.out.println(interfaceID + " " + itemId);
		if (interfaceID == 1688) {
			player.getEquipment().unequip(slot);
		} else if (interfaceID == 5064) {
			BankManager.bankItem(player, slot, itemId, 1);
		} else if (interfaceID == 5382) {
			BankManager.withdrawItem(player, slot, itemId, 1);
		} else if (interfaceID == 3900) {
			ShopManager.getBuyValue(player, itemId);
		} else if (interfaceID == 3823) {
			ShopManager.getSellValue(player, itemId);
		} else if (interfaceID == 3322) {
			TradeManager.offerItem(player, slot, itemId, 1);
		} else if(interfaceID == 3415) {
			TradeManager.removeTradeItem(player, slot, itemId, 1);
		}
	}
	
	private void handlePacket117(Player player, Packet packet) {
		int interfaceID = packet.getIn().readShort(true, StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
		int itemId = packet.getIn().readShort(true, StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
		int slot = packet.getIn().readShort(true, StreamBuffer.ByteOrder.LITTLE);
		if (interfaceID == 5064) {
			BankManager.bankItem(player, slot, itemId, 5);
		} else if (interfaceID == 5382) {
			BankManager.withdrawItem(player, slot, itemId, 5);
		} else if (interfaceID == 3900) {
			ShopManager.buyItem(player, slot, itemId, 1);
		} else if (interfaceID == 3823) {
			ShopManager.sellItem(player, slot, itemId, 1);
		} else if (interfaceID == 3322) {
			TradeManager.offerItem(player, slot, itemId, 5);
		} else if(interfaceID == 3415) {
			TradeManager.removeTradeItem(player, slot, itemId, 5);
		}
	}
	
	private void handlePacket43(Player player, Packet packet) {
		int interfaceID = packet.getIn().readShort(StreamBuffer.ByteOrder.LITTLE);
		int itemId = packet.getIn().readShort(StreamBuffer.ValueType.A);
		int slot = packet.getIn().readShort(StreamBuffer.ValueType.A);
		if (interfaceID == 5064) {
			BankManager.bankItem(player, slot, itemId, 10);
		} else if (interfaceID == 5382) {
			BankManager.withdrawItem(player, slot, itemId, 10);
		} else if (interfaceID == 3900) {
			ShopManager.buyItem(player, slot, itemId, 5);
		} else if (interfaceID == 3823) {
			ShopManager.sellItem(player, slot, itemId, 5);
		} else if (interfaceID == 3322) {
			TradeManager.offerItem(player, slot, itemId, 10);
		} else if(interfaceID == 3415) {
			TradeManager.removeTradeItem(player, slot, itemId, 10);
		}
	}
	
	  private void handlePacket129(Player player, Packet packet) {
        int slot = packet.getIn().readShort(StreamBuffer.ValueType.A);
        int interfaceID = packet.getIn().readShort();
        int itemId = packet.getIn().readShort(StreamBuffer.ValueType.A);
		if (interfaceID == 5064) {
            BankManager.bankItem(player, slot, itemId, player.getInventory().getItemContainer().getCount(itemId));
        } else if (interfaceID == 5382) {
            int amount = 0;
            if (player.isWithdrawAsNote()) {
                amount = player.getBank().getCount(itemId);
            } else {
                Item item = new Item(itemId);
                amount = item.getDefinition().isStackable() ? player.getBank().getCount(itemId) : 28;
            }
            BankManager.withdrawItem(player, slot, itemId, amount);
        } else if (interfaceID == 3900) {
            ShopManager.buyItem(player, slot, itemId, 10);
        } else if (interfaceID == 3823) {
			ShopManager.sellItem(player, slot, itemId, 10);
		} else if (interfaceID == 3322) {
            TradeManager.offerItem(player, slot, itemId, player.getInventory().getItemContainer().getCount(itemId));
        } else if (interfaceID == 3415) {
            int amount = 0;
            if (player.isWithdrawAsNote()) {
                amount = player.getTrade().getCount(itemId);
            } else {
                Item item = new Item(itemId);
                amount = item.getDefinition().isStackable() ? player.getTrade().getCount(itemId) : 28;
            }
            TradeManager.removeTradeItem(player, slot, itemId, amount);
        }
    }
	
	private void handleFirstClickItem(Player player, Packet packet) {
		packet.getIn().readShort(StreamBuffer.ValueType.A);
		int slot = packet.getIn().readShort(StreamBuffer.ValueType.A);
		int item = packet.getIn().readShort(StreamBuffer.ByteOrder.LITTLE);
		player.getRunecrafting().fillEssencePouch(item);
		player.getBoneBurying().buryBone(item);
		player.getHerblore().cleanHerb(item);
		player.getFood().eatFood(item, slot);
		if (item == 2528)
			player.getGenie().sendLampInterface();
		if (item == 4155) {
			String slayerNpc = (String) player.getSlayerTask()[0];
			if (!slayerNpc.equalsIgnoreCase("")) {
				player.getDialogue().sendStatement1("Your existing task is to kill " + 
						(Integer) player.getSlayerTask()[1] + 
						" " + slayerNpc + "s.");
				player.getDialogue().setNextDialogue(0);
			}
			else {
				player.getDialogue().sendStatement1("You don't have a slaver task.");
			}
		}
		player.getPotion().drinkPotion(item, slot);
	}
	
	private void handleThirdClickItem(Player player, Packet packet) {
		int item = packet.getIn().readShort(StreamBuffer.ValueType.A);
		int slot = packet.getIn().readShort(true, StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
		packet.getIn().readShort();
		player.getRunecrafting().checkEssencePouch(item);
	}
	
	private void handleEquipItem(Player player, Packet packet) {
		int item = packet.getIn().readShort(); // Item ID.
		int slot = packet.getIn().readShort(StreamBuffer.ValueType.A);
		packet.getIn().readShort(); // Interface ID.
		for (int i = 0; i < player.getRunecrafting().POUCHES.length; i++) {
			if (item == player.getRunecrafting().POUCHES[i][0]) {
				player.getRunecrafting().emptyEssencePouch(item);
				return;
			}
		}
		player.getEquipment().equip(slot);
	}

}
