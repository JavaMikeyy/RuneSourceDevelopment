package com.rs2.model.players;

import com.rs2.model.players.container.Container;
import com.rs2.model.players.container.Container.Type;

public class Inventory {
	
	public static final int DEFAULT_INVENTORY_INTERFACE = 3214;
	public static final int SIZE = 28;
	
	private Player player;
	private Container itemContainer = new Container(Type.STANDARD, SIZE);
	
	public Inventory(Player player) {
		this.player = player;
	}
	
	public void sendInventoryOnLogin() {
		refresh();
	}
	
	public void refresh() {
		Item[] inv = itemContainer.toArray();
		player.getActionSender().sendUpdateItems(DEFAULT_INVENTORY_INTERFACE, inv);
	}
	
	public void refresh(int inventoryId) {
		Item[] inv = itemContainer.toArray();
		player.getActionSender().sendUpdateItems(inventoryId, inv);
	}
	
	public void addItem(Item item) {
		if (item == null) {
			return;
		}
		if (itemContainer.freeSlot() == -1 && !itemContainer.hasRoomFor(item)) {
			player.getActionSender().sendMessage("Not enough space in your inventory.");
			return;
		}
		int amount = item.getCount();
		if (!item.getDefinition().isStackable()) {
			if (amount > itemContainer.freeSlots()) {
				amount = itemContainer.freeSlots();
			}
		}
		itemContainer.add(new Item(item.getId(), amount));
		refresh();
	}
	
	public void addItemToSlot(Item item, int slot) {
		if (item == null) {
			return;
		}
		if (itemContainer.freeSlot() == -1) {
			player.getActionSender().sendMessage("Not enough space in your inventory.");
			return;
		}
		itemContainer.set(slot, item);
		refresh();
	}
	
	public void removeItem(Item item) {
		if (item == null || item.getId() == -1) {
			return;
		}
		if (!itemContainer.contains(item.getId())) {
			return;
		}
		itemContainer.remove(item);
		refresh();
	}
	
	/**
	  * Removes an item and adds an item
	  * - I'd recommend using this since it checks for you having the item before it gives it to you.
	  * - Should help combat duping.
	  */
	public void replaceItemWithItem(Item oldItem, Item newItem) {
		if (getItemContainer().getCount(oldItem.getId()) >= oldItem.getCount()) {
			removeItem(oldItem);
			addItem(newItem);
		}
	}
	
	public void removeItemSlot(Item item, int slot) {
		if (item == null || item.getId() == -1) {
			return;
		}
		if (slot == -1) {
			return;
		}
		if (itemContainer.get(slot) == null) {
			return;
		}
		if (!itemContainer.contains(item.getId())) {
			return;
		}
		itemContainer.remove(item, slot);
		refresh();
	}
	
	public void swap(int fromSlot, int toSlot) {
		itemContainer.swap(fromSlot, toSlot);
		refresh();
	}

	public void setItemContainer(Container itemContainer) {
		this.itemContainer = itemContainer;
	}

	public Container getItemContainer() {
		return itemContainer;
	}

}
