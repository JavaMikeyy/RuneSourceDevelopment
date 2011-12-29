package com.rs2.model.players;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.rs2.Constants;
import com.rs2.model.players.container.Container;
import com.rs2.model.players.container.Container.Type;
import com.rs2.util.Misc;
import com.rs2.util.XStreamUtil;

public class ShopManager {
	
	public static final int SIZE = 28;
	
	private static List<Shop> shops = new ArrayList<Shop>(SIZE);
	
	public static void refresh(Player player, Shop shop) {
		Item[] shopItems = shop.getCurrentStock().toArray();
		player.getActionSender().sendUpdateItems(3900, shopItems);
	}
	
	public static void openShop(Player player, int shopId) {
		Shop shop = shops.get(shopId);
		Item[] shopItems = shop.getCurrentStock().toArray();
		player.getActionSender().sendInterface(3824, 3822);
		player.getInventory().refresh(3823);
		player.getActionSender().sendUpdateItems(3900, shopItems);
		player.getActionSender().sendString(shop.getName(), 3901);
		player.setShopId(shopId);
		player.getAttributes().put("isShopping", Boolean.TRUE);
	}
	
	public static void buyItem(Player player, int slot, int shopItem, int amount) {
		Shop shop = shops.get(player.getShopId());
		Container inventory = player.getInventory().getItemContainer();
		int currency = shop.getCurrency();
		int value = ItemManager.getInstance().getItemValue(shopItem, "shopbuy");
		Item item = shop.getCurrentStock().get(slot);
		if (amount < 1 || shopItem < 0) {
			return;
		}
		if (inventory.getCount(currency) < value || !inventory.contains(currency)) {
			player.getActionSender().sendMessage("You do not have enough coins to buy this item.");
			return;
		}
		if (inventory.freeSlots() == -1) {
			return;
		}
		if (shop.getCurrentStock().get(slot).getCount() < amount) {
			amount = shop.getCurrentStock().get(slot).getCount();
		}
		if (shop.isGeneralStore()) {
			if (shop.getCurrentStock().get(slot).getCount() == 0) {
				player.getActionSender().sendMessage("This item is out of stock.");
				return;
			}
		}
		for (int i = 0; i < amount; i ++) {
			player.getInventory().removeItem(new Item(currency, value));
		}
		if (shop.getStock().contains(item.getId())) {
			shop.getCurrentStock().removeOrZero(new Item(item.getId(), amount));
		} else {
			shop.getCurrentStock().remove(new Item(item.getId(), amount));
		}
		player.getInventory().addItem(new Item(shopItem, amount));
		Item[] shopItems = shop.getCurrentStock().toArray();
		player.getInventory().refresh(3823);
		player.getActionSender().sendUpdateItems(3900, shopItems);
	}
	
	public static void sellItem(Player player, int slot, int item, int amount) {
		Shop shop = shops.get(player.getShopId());
		Container inventory = player.getInventory().getItemContainer();
		int currency = shop.getCurrency();
		int value = ItemManager.getInstance().getItemValue(item, "shopsell");
		int transferAmount = inventory.getCount(item);
		if (amount < 1 || item < 0) {
			return;
		}
		if (item > Constants.MAX_ITEMS) {
			player.getActionSender().sendMessage("This item is not supported yet.");
			return;
		}
		if (!inventory.contains(item)) {
			return;
		}
		if (transferAmount > amount) {
			transferAmount = amount;
		}
		int shopAmount = shop.getCurrentStock().getCount(item);
		if (shop.isGeneralStore()) {
			if (shopAmount > 0) {
				shop.getCurrentStock().set(shop.getCurrentStock().getSlotById(item), new Item(item, shopAmount + transferAmount));
			} else {
				shop.getCurrentStock().add(new Item(item, transferAmount));
			}
		} else {
			if (!shop.getStock().contains(item)) {
				player.getActionSender().sendMessage("You cannot sell this item in this shop.");
				return;
			} else {
				shop.getCurrentStock().set(shop.getCurrentStock().getSlotById(item), new Item(item, shopAmount + transferAmount));
			}
		}
		for (int i = 0; i < transferAmount; i ++) {
			player.getInventory().removeItemSlot(new Item(item, 1), slot);
		}
		if (value > 0) {
			player.getInventory().addItem(new Item(currency, value));
		}
		Item[] shopItems = shop.getCurrentStock().toArray();
		player.getInventory().refresh(3823);
		player.getActionSender().sendUpdateItems(3900, shopItems);
	}
	
	public static void getBuyValue(Player player, int id) {
		int price = ItemManager.getInstance().getItemValue(id, "shopbuy");
		player.getActionSender().sendMessage("" + ItemManager.getInstance().getItemName(id) + " currently costs " 
				+ Misc.formatNumber(price) + " coins.");
	}
	
	public static void getSellValue(Player player, int id) {
		int price = ItemManager.getInstance().getItemValue(id, "shopsell");
		player.getActionSender().sendMessage("" + ItemManager.getInstance().getItemName(id) +": shop will buy for " 
				+ Misc.formatNumber(price) + " coins.");
	}
	
	@SuppressWarnings("unchecked")
	public static void loadShops() throws FileNotFoundException {
		System.out.println("Loading shops...");
		List<Shop> list = (List<Shop>) XStreamUtil.getxStream().fromXML(new FileInputStream("./data/shops.xml"));
		for (Shop shop : list) {
			Container stock = new Container(Type.ALWAYS_STACK, SIZE);
			Container currentStock = new Container(Type.ALWAYS_STACK, SIZE);
			shops.add(shop);
			for (Item item : shop.getItems()) {
				if (item != null) {
					stock.add(item);
					currentStock.add(item);
				}
			}
			shop.setStock(stock);
			shop.setCurrentStock(currentStock);
		}
		System.out.println("Loaded  " + list.size() + " shop definitions.");
	}

	public static void setShops(List<Shop> items) {
		ShopManager.shops = items;
	}

	public static List<Shop> getShops() {
		return shops;
	}
	
	public class Shop {
		
		private int shopId;
		private String name;
		private Item[] items;
		private boolean isGeneralStore;
		private int currency;
		private Container stock;
		private Container currentStock;

		public void setName(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setShopId(int shopId) {
			this.shopId = shopId;
		}

		public int getShopId() {
			return shopId;
		}

		public void setGeneralStore(boolean isGeneralStore) {
			this.isGeneralStore = isGeneralStore;
		}

		public boolean isGeneralStore() {
			return isGeneralStore;
		}

		public void setCurrency(int currency) {
			this.currency = currency;
		}

		public int getCurrency() {
			return currency;
		}

		public void setItems(Item[] items) {
			this.items = items;
		}

		public Item[] getItems() {
			return items;
		}

		public void setStock(Container stock) {
			this.stock = stock;
		}

		public Container getStock() {
			return stock;
		}

		public void setCurrentStock(Container currentStock) {
			this.currentStock = currentStock;
		}

		public Container getCurrentStock() {
			return currentStock;
		}
	}

}
