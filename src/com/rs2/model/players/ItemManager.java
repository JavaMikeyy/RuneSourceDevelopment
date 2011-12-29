package com.rs2.model.players;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.players.Equipment.EquipmentDefinition;
import com.rs2.model.tick.Tick;
import com.rs2.util.Misc;
import com.rs2.util.XStreamUtil;

public class ItemManager {
	
	private static ItemManager instance = new ItemManager();
	
	private List<GroundItem> groundItems;
	private ItemDefinition[] itemDefinitions;
	private EquipmentDefinition[] equipmentDefinition;
	private Player owner;
	
	public void startup() {
		groundItems = new ArrayList<GroundItem>();
		itemDefinitions = new ItemDefinition[Constants.MAX_ITEMS];
		equipmentDefinition = new EquipmentDefinition[Constants.MAX_ITEMS];
	}
	
	public void addGroundItem(GroundItem g) {
		synchronized(groundItems) {
			groundItems.add(g);
		}
	}
	
	public void removeGroundItem(GroundItem g) {
		synchronized(groundItems) {
			groundItems.remove(g);
		}
	}
	
	public void tick() {
		for (final GroundItem g : groundItems) {
			if (g == null) {
				continue;
			}
			Player player = World.getPlayerByName(g.getOwner());
			this.owner = player;
			if (g.getTime() > 0) {
				g.setTime(g.getTime() - 1);
			}
			if (g.getTime() == Constants.SHOW_ALL_GROUND_ITEMS) {
				if (!isUntradeable(g.getItem().getId())) {
					showGlobalItem(g);
				} else {
					player.getActionSender().sendGroundItem(g);
				}
				player.getActionSender().sendGroundItem(g);
				g.setGlobal(true);
			}
			if (g.getTime() == 0) {
				removeGroundItem(g);
				owner.getActionSender().removeGroundItem(g);
				removeGlobalItem(g);
				player.getActionSender().removeGroundItem(g);
				g.setGlobal(false);
				break;
			}
		}
	}
	
	public void pickupItem(final Player p, final int itemId, Position pos) {
		World.submit(new Tick(1) {
			@Override
			public void execute() {
				for (final GroundItem g : groundItems) {
					int x = g.getPos().getX();
					int y = g.getPos().getY();
					int z = g.getPos().getZ();
					int playerX = p.getPosition().getX();
					int playerY = p.getPosition().getY();
					int playerZ = p.getPosition().getZ();
					if (g.getItem().getId() == itemId && x == playerX && y == playerY && 
							playerZ == z) {
						if (p.getPrimaryDirection() < 1 && 
								p.getSecondaryDirection() < 1) {
							if (g.getTime() <= 0) {
								this.stop();
								return;
							}
							if (p.getInventory().getItemContainer().freeSlot() > - 1) {
								if (g.getItem().getDefinition().isStackable()) {
									p.getInventory().addItem(new Item(p.getClickId(), 
											g.getItem().getCount()));
								} else {
									p.getInventory().addItem(new Item(p.getClickId(), 1));
								}
								removeGroundItem(g);
								p.getActionSender().removeGroundItem(g);
								removeGlobalItem(g);
								this.stop();
								break;
							} else {
								p.getActionSender().sendMessage("Not enough space in your inventory.");
								this.stop();
							}
						}
					}
				}
			}
		});
	}
	
	public void showGlobalItem(GroundItem g) {
		for (Player players : World.getPlayers()) {
			if (players == null) {
				continue;
			}
			if (!players.getUsername().equals(g.getOwner())) {
				if (players.getPosition().getZ() == g.getPos().getZ() 
						&& Misc.getDistance(players.getPosition(), g.getPos()) <= 60) {
					players.getActionSender().sendGroundItem(g);
				}
			}
		}
	}
	
	public void removeGlobalItem(GroundItem g) {
		for (Player players : World.getPlayers()) {
			if (players == null) {
				continue;
			}
			if (!players.getUsername().equals(g.getOwner())) {
				if (players.getPosition().getZ() == g.getPos().getZ() 
						&& Misc.getDistance(players.getPosition(), g.getPos()) <= 60) {
					players.getActionSender().removeGroundItem(g);
				}
			}
		}
	}
	
	public void loadOnRegion(Player player) {
		for (GroundItem g : groundItems) {
			if (g != null) {
				if (player.getPosition().getZ() == g.getPos().getZ() 
						&& Misc.getDistance(player.getPosition(), g.getPos()) <= 60) {
					if (g.isGlobal()) {
						if (!isUntradeable(g.getItem().getId())) {
							player.getActionSender().removeGroundItem(g);
							player.getActionSender().sendGroundItem(g);
						} else {
							owner.getActionSender().removeGroundItem(g);
							owner.getActionSender().sendGroundItem(g);
						}
					}
				}
				break;
			}
		}
	}
	
	public void createGroundItem(Player player, String owner, Item item,
			Position pos) {
		if (item != null) {
			GroundItem g = new GroundItem(owner, item, pos, false);
			addGroundItem(g);
			g.setTime(Constants.GROUND_START_TIME);
			if (addToStack(player, g.getItem(), g.getPos())) {
				return;
			}
			player.getActionSender().sendGroundItem(g);
		}
	}
	
	public boolean addToStack(Player player, Item item, Position position) {
		if (!item.getDefinition().isStackable()) {
			return false;
		}
		for (GroundItem g : groundItems) {
			if (g.getPos().getX() == position.getX() && g.getPos().getY() == position.getY() && g.getItem().getId() == item.getId()) {
				if (!g.isRespawn() && g.getOwner() == player.getUsername()) {
					g.getItem().setCount((g.getItem().getCount()/* + item.getCount()*/));
					player.getActionSender().removeGroundItem(g);
					player.getActionSender().sendGroundItem(g);
					return true;
				}
			}
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public void loadItemDefinitions() throws FileNotFoundException, IOException {
		System.out.println("Loading item definitions...");
		List<ItemDefinition> list = (List<ItemDefinition>) XStreamUtil.getxStream().fromXML(new FileInputStream("./data/itemdefs.xml"));
		for (ItemDefinition def : list) {
			itemDefinitions[def.getId()] = def;
		}
		System.out.println("Loading " + list.size() + " item definitions.");
	}
	
	@SuppressWarnings("unchecked")
	public void loadEquipmentDefinitions() throws FileNotFoundException {
		System.out.println("Loading equipment definitions...");
		List<EquipmentDefinition> list = (List<EquipmentDefinition>)
			XStreamUtil.getxStream().fromXML(new FileInputStream("./data/equipdefs.xml"));
		for (EquipmentDefinition def : list) {
			equipmentDefinition[def.getId()] = def;
		}
		System.out.println("Loaded " + list.size() + " equipment definitions");
	}
	
	public int getItemValue(int itemId, String type) {
		int value = 0;
		ItemDefinition def = itemDefinitions[itemId];
		if (type.equals("shopsell")) {
			value = def.getShopSellValue();
		} else if (type.equals("shopbuy")) {
			value = def.getShopBuyValue();
		} else if (type.equals("lowalch")) {
			value = def.getLowAlchValue();
		} else if (type.equals("highalch")) {
			value = def.getHighAlchValue();
		} else if (type.equals("market")) {
			value = def.getMarketPrice();
		}
		return value;
	}
	
	public boolean isUntradeable(int id) {
		return Arrays.binarySearch(Constants.UNTRADEABLE_ITEMS, id) > -1;
	}
	
	public String getItemName(int itemId) {
		return itemDefinitions[itemId].getName();
	}

	public void setGroundItems(List<GroundItem> groundItems) {
		this.groundItems = groundItems;
	}

	public List<GroundItem> getGroundItems() {
		return groundItems;
	}

	public void setItemDefinitions(ItemDefinition[] itemDefinitions) {
		this.itemDefinitions = itemDefinitions;
	}

	public ItemDefinition[] getItemDefinitions() {
		return itemDefinitions;
	}

	public EquipmentDefinition[] getEquipmentDefinitions() {
		return equipmentDefinition;
	}
	
	public static ItemManager getInstance() {
		return instance;
	}

	public class ItemDefinition {
		
		private int id;
		private String name;
		private int lowAlchValue;
		private int highAlchValue;
		private int shopBuyValue;
		private int shopSellValue;
		private int marketPrice;
		private boolean stackable;
		private boolean note;
		private int parentId;
		
		public void setId(int id) {
			this.id = id;
		}
		
		public int getId() {
			return id;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setLowAlchValue(int lowAlchValue) {
			this.lowAlchValue = lowAlchValue;
		}

		public int getLowAlchValue() {
			return lowAlchValue;
		}

		public void setHighAlchValue(int highAlchValue) {
			this.highAlchValue = highAlchValue;
		}

		public int getHighAlchValue() {
			return highAlchValue;
		}

		public void setShopBuyValue(int shopBuyValue) {
			this.shopBuyValue = shopBuyValue;
		}

		public int getShopBuyValue() {
			return shopBuyValue;
		}

		public void setShopSellValue(int shopSellValue) {
			this.shopSellValue = shopSellValue;
		}

		public int getShopSellValue() {
			return shopSellValue;
		}

		public void setMarketPrice(int marketPrice) {
			this.marketPrice = marketPrice;
		}

		public int getMarketPrice() {
			return marketPrice;
		}

		public void setStackable(boolean stackable) {
			this.stackable = stackable;
		}

		public boolean isStackable() {
			return stackable;
		}

		public void setNote(boolean note) {
			this.note = note;
		}

		public boolean isNote() {
			return note;
		}

		public void setParentId(int parentId) {
			this.parentId = parentId;
		}

		public int getParentId() {
			return parentId;
		}
	}

}
