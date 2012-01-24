package com.rs2.model.players;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.rs2.Constants;
import com.rs2.model.players.Item;
import com.rs2.model.players.container.Container;
import com.rs2.model.players.container.Container.Type;
import com.rs2.util.Misc;
import com.rs2.util.XStreamUtil;

public class ShopManager {
	
	public static final int SIZE = 40;
	
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
		Item item = shop.getCurrentStock().get(slot);
		int currency, value;
		if (shop.getCurrencyType() == Shop.CurrencyTypes.ITEM) {
			currency = shop.getCurrency();
		}
		else {
			currency = getCurrencyForShopType(player, shop);
		}
		if (amount < 1 || shopItem < 0) {
			return;
		}
		if (shop.getCurrencyType() == Shop.CurrencyTypes.ITEM) {
			value = ItemManager.getInstance().getItemValue(shopItem, "shopbuy");
			if (inventory.getCount(currency) < value || !inventory.contains(currency) && value > 0) {
				player.getActionSender().sendMessage("You do not have enough " + ItemManager.getInstance().getItemName(currency) +
						" to buy this item.");
				return;
			}
		}
		else {
			value = getSpecialShopPrice(player, shop, shopItem);
			if (currency < (value * amount)) {
				player.getActionSender().sendMessage("You do not have enough server points to buy this item.");
				return;
			}
		}
		if (!inventory.hasRoomFor(new Item(shopItem, amount))) {
			amount = inventory.freeSlots();
			player.getActionSender().sendMessage("You ran out of inventory space.");
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
		for (int i = 0; i < amount; i++) {
			if (shop.getCurrencyType() == Shop.CurrencyTypes.ITEM) {
				player.getInventory().removeItem(new Item(currency, value));
			}
			else {
				decreaseCurrencyForSpecialShop(player, shop, value);
			}
		}
		if (shop.getStock().contains(item.getId())) {
			shop.getCurrentStock().removeOrZero(new Item(item.getId(), amount));
		} else {
			shop.getCurrentStock().remove(new Item(item.getId(), amount));
		}
		player.getInventory().addItem(new Item(shopItem, amount));
		player.getInventory().refresh(3823);
		refresh(player, shop);
	}
	
	public static void sellItem(Player player, int slot, int item, int amount) {
		Shop shop = shops.get(player.getShopId());
		Container inventory = player.getInventory().getItemContainer();
		int currency = shop.getCurrency();
		if (shop.getCurrencyType() != Shop.CurrencyTypes.ITEM) {
			player.getActionSender().sendMessage("This shop can't buy anything.");
			return;
		}
		int value = ItemManager.getInstance().getItemValue(item, "shopsell");
		int totalItems = inventory.getCount(item);
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
		if (amount >= totalItems) {
			amount = totalItems;
		}
		int shopAmount = shop.getCurrentStock().getCount(item);
		player.getInventory().removeItem(new Item(item, amount));
		if (ItemManager.getInstance().getItemValue(item, "shopbuy") > 0)
					player.getInventory().addItem(new Item(currency, ItemManager.getInstance().getItemValue(item, "shopbuy") * amount));
		if (shop.isGeneralStore()) {
			if (shopAmount > 0) {
				shop.getCurrentStock().set(shop.getCurrentStock().getSlotById(item), new Item(item, shopAmount + amount));
			} else {
				shop.getCurrentStock().add(new Item(item, amount));
			}
		} else {
			if (!shop.getStock().contains(item)) {
				player.getActionSender().sendMessage("You cannot sell this item in this shop.");
				return;
			} else {
				shop.getCurrentStock().set(shop.getCurrentStock().getSlotById(item), new Item(item, shopAmount + amount));
			}
		}
		player.getInventory().refresh(3823);
		refresh(player, shop);
	}
	
	public static void getBuyValue(Player player, int id) {
		Shop shop = shops.get(player.getShopId());
		if (shop.getCurrencyType() == Shop.CurrencyTypes.ITEM) {
			int price = ItemManager.getInstance().getItemValue(id, "shopbuy");
			String currencyName = ItemManager.getInstance().getItemName(shop.getCurrency());
			player.getActionSender().sendMessage("" + ItemManager.getInstance().getItemName(id) + ": currently costs " 
					+ Misc.formatNumber(price) + " " + currencyName + ".");
		}
		else {
			int price = getSpecialShopPrice(player, shop, id);
			player.getActionSender().sendMessage("" + ItemManager.getInstance().getItemName(id) + ": currently costs " + 
					price + " " + getCurrencyName(shop) + ".");
		}
	}
	
	public static void getSellValue(Player player, int id) {
		Shop shop = shops.get(player.getShopId());
		if (shop.getCurrencyType() == Shop.CurrencyTypes.ITEM) {
			int price = ItemManager.getInstance().getItemValue(id, "shopsell");
			String currencyName = ItemManager.getInstance().getItemName(shop.getCurrency());
			player.getActionSender().sendMessage("" + ItemManager.getInstance().getItemName(id) +": shop will buy for " 
					+ Misc.formatNumber(price) + " " + getCurrencyName(shop) + ".");
		}
		else {
			player.getActionSender().sendMessage("You cannot sell items to this shop.");
		}
	}
	
	public static int getCurrencyForShopType(Player player, Shop shop) {
		switch (shop.getCurrencyType()) {
			case SERVER_POINTS:
				return player.getServerPoints();
		}
		return -1;
	}
	
	public static int getSpecialShopPrice(Player player, Shop shop, int buyId) {
		switch (shop.getCurrencyType()) {
			case SERVER_POINTS:
				switch (buyId) {
					case 4152:
						return 10;
				}
				break;
		}
		return 0;
	}
	
	public static void decreaseCurrencyForSpecialShop(Player player, Shop shop, int value) {
		switch (shop.getCurrencyType()) {
			case SERVER_POINTS:
				player.decreaseServerPoints(value);
				break;
		}
	}
	
	public static String getCurrencyName(Shop shop) {
		switch (shop.getCurrencyType()) {
			case SERVER_POINTS:
				return "Server Points";
		}
		return "Coins";
	}
	
	@SuppressWarnings("unchecked")
	public static void loadShops() throws FileNotFoundException {
		System.out.println("Loading shops...");
		List<Shop> list = (List<Shop>) XStreamUtil.getxStream().fromXML(new FileInputStream("./data/content/shops.xml"));
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
	
	public static class Shop {
		
		private int shopId;
		private String name;
		private Item[] items;
		private boolean isGeneralStore;
		private CurrencyTypes currencyType;
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
		
		public CurrencyTypes getCurrencyType() {
			return currencyType;
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
		
		enum CurrencyTypes {
			ITEM, SERVER_POINTS
		}
	}

}
