package com.rs2.model.players;

import com.rs2.Constants;
import com.rs2.model.players.Player.BankOptions;
import com.rs2.model.content.BankPin;

public class BankManager {
	
	public static final int SIZE = 250;
	
	public static void openBank(Player player) {
		if (player.getBankPin().hasBankPin()) {
			if (!player.getBankPin().isBankPinVerified()) {
				player.getBankPin().startPinInterface(BankPin.PinInterfaceStatus.VERIFYING);
				return;
			}
		}
		else {
			player.getActionSender().sendMessage("You do not have a bank pin, it is highly recommended you get one.");
		}
		Item[] inventory = player.getInventory().getItemContainer().toArray();
		Item[] bank = player.getBank().toArray();
		player.getActionSender().sendUpdateItems(5064, inventory);
		player.getActionSender().sendInterface(5292, 5063);
		player.getActionSender().sendUpdateItems(5382, bank);
		player.getAttributes().put("isBanking", Boolean.TRUE);
	}
	
	public static void bankItem(Player player, int slot, int bankItem, int bankAmount) {
		Item inventoryItem = player.getInventory().getItemContainer().get(slot);
		if (inventoryItem == null || inventoryItem.getId() != bankItem) {
			return;
		}
		int amount = player.getInventory().getItemContainer().getCount(bankItem);
		boolean isNote = inventoryItem.getDefinition().isNote();
		if (inventoryItem.getDefinition().getId() > Constants.MAX_ITEMS) {
			player.getActionSender().sendMessage("This item is not supported yet.");
			return;
		}
		int freeSlot = player.getBank().freeSlot();
		if (freeSlot == -1) {
			player.getActionSender().sendMessage("You don't have enough space in your bank account.");
			return;
		}
		if (amount > bankAmount) {
			amount = bankAmount;
		}
		if (!inventoryItem.getDefinition().isStackable()) {
			for (int i = 0; i < amount; i ++) {
				player.getInventory().removeItem(new Item(bankItem, 1));
			}
		} else {
			player.getInventory().removeItem(new Item(bankItem, amount));
		}
		int bankCount = player.getBank().getCount(bankItem);
		int transferId = isNote ? inventoryItem.getDefinition().getParentId() : inventoryItem.getDefinition().getId();
		if (bankCount == 0) {
			player.getBank().add(new Item(transferId, amount));
		} else {
			player.getBank().set(player.getBank().getSlotById(transferId), new Item(transferId, bankCount + amount));
		}
		Item[] bankItems = player.getBank().toArray();
		player.getInventory().refresh(5064);
		player.getActionSender().sendUpdateItems(5382, bankItems);
	}
	
	public static void withdrawItem(Player player, int slot, int bankItem, int bankAmount) {
		Item item = new Item(bankItem + 1);
		boolean noted = item.getDefinition().isNote();
		int freeInventorySlot = player.getInventory().getItemContainer().freeSlot();
		if (!player.getInventory().getItemContainer().hasRoomFor(new Item(bankItem, bankAmount))) {
			player.getActionSender().sendMessage("Not enough space in your inventory.");
			bankAmount = player.getInventory().getItemContainer().freeSlots();
			if (!player.getInventory().getItemContainer().hasRoomFor(new Item(bankItem, bankAmount))) {
				return;
			}
		}
		int inBankAmount = player.getBank().getCount(bankItem);
		if (bankAmount < 1 || bankItem < 0) {
			return;
		}
		if (bankItem > Constants.MAX_ITEMS) {
			player.getActionSender().sendMessage("This item is not supported yet.");
			return;
		}
		if (inBankAmount < bankAmount) {
			bankAmount = inBankAmount;
		}
		if (player.isWithdrawAsNote() && !noted) {
			player.getActionSender().sendMessage("This item cannot be withdrawn as a note.");
			return;
		} 
		if (!player.isWithdrawAsNote()) {
			player.getInventory().addItem(new Item(bankItem, bankAmount));
		} else if (player.isWithdrawAsNote()) {
			player.getInventory().addItem(new Item(bankItem + 1, bankAmount));
		}
		player.getBank().remove(new Item(bankItem, bankAmount), slot);
		player.getBank().shift();
		Item[] bankItems = player.getBank().toArray();
		player.getInventory().refresh(5064);
		player.getActionSender().sendUpdateItems(5382, bankItems);
	}
	
	public static void handleBankOptions(Player player, int fromSlot, int toSlot) {
		if (player.getBankOptions().equals(BankOptions.SWAP_ITEM)) {
			player.getBank().swap(fromSlot, toSlot);
		} else if (player.getBankOptions().equals(BankOptions.INSERT_ITEM)) {
			player.getBank().insert(fromSlot, toSlot);
		}
		Item[] bankItems = player.getBank().toArray();
		player.getActionSender().sendUpdateItems(5382, bankItems);
	}

}
