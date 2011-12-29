package com.rs2.model.players;

import com.rs2.model.World;
import com.rs2.model.players.Player.TradeStage;
import com.rs2.util.NameUtil;

public class TradeManager {
	
	public static void refresh(Player player, Player otherPlayer) {
		player.getActionSender().sendUpdateItems(3322, player.getInventory().
				getItemContainer().toArray());
		otherPlayer.getActionSender().sendUpdateItems(3322, otherPlayer.getInventory().
				getItemContainer().toArray());
		player.getActionSender().sendUpdateItems(3415, player.getTrade().toArray());
		player.getActionSender().sendUpdateItems(3416, otherPlayer.getTrade().toArray());
		otherPlayer.getActionSender().sendUpdateItems(3415, otherPlayer.getTrade().toArray());
		otherPlayer.getActionSender().sendUpdateItems(3416, player.getTrade().toArray());
		player.getActionSender().sendString("Trading With: "+ otherPlayer.getUsername() + 
				" has " + otherPlayer.getInventory().getItemContainer().freeSlots() + 
					" free inventory slots.", 3417);
		otherPlayer.getActionSender().sendString("Trading With: "+ player.getUsername() + 
				" has " + player.getInventory().getItemContainer().freeSlots() + 
					" free inventory slots.", 3417);
	}
	
	public static void handleTradeRequest(Player player, Player otherPlayer) {
		if (otherPlayer.getTradeStage().equals(TradeStage.WAITING)) {
			player.getActionSender().sendMessage("Sending trade offer...");
			otherPlayer.getActionSender().sendMessage("" + 
					NameUtil.uppercaseFirstLetter(player.getUsername()) + ":tradereq:");
			player.setTradeStage(TradeStage.SEND_REQUEST);
		} else if (otherPlayer.getTradeStage().equals(TradeStage.SEND_REQUEST)) {
			player.setTradeStage(TradeStage.SEND_REQUEST_ACCEPT);
			otherPlayer.setTradeStage(TradeStage.SEND_REQUEST_ACCEPT);
			sendTrade(player, otherPlayer);
		}
	}
	
	public static void handleWalkAway(Player player) {
		if (player.getTradeStage().equals(TradeStage.WAITING)) {
			return;
		}
		declineTrade(player);
	}
	
	private static void sendTrade(Player player, Player otherPlayer) {
		player.getActionSender().sendInterface(3323, 3321);
		otherPlayer.getActionSender().sendInterface(3323, 3321);
		player.getActionSender().sendString("Trading With: "+ otherPlayer.getUsername() + 
				" has " + otherPlayer.getInventory().getItemContainer().freeSlots() + 
					" free inventory slots.", 3417);
		otherPlayer.getActionSender().sendString("Trading With: "+ player.getUsername() + 
				" has " + player.getInventory().getItemContainer().freeSlots() + 
					" free inventory slots.", 3417);
		player.getActionSender().sendString("" , 3431);
		otherPlayer.getActionSender().sendString("", 3431);
		refresh(player, otherPlayer);
		player.getUpdateFlags().faceEntity(65535);
		otherPlayer.getUpdateFlags().faceEntity(65535);
	}
	
	public static void declineTrade(Player player) {
		Player otherPlayer = World.getPlayers()[player.getClickId()];
		if (otherPlayer.getTradeStage().equals(TradeStage.SEND_REQUEST_ACCEPT)) {
			otherPlayer.getActionSender().sendMessage("Other player has declined the trade.");
		}
		player.getActionSender().removeInterfaces();
		otherPlayer.getActionSender().removeInterfaces();
		player.setTradeStage(TradeStage.WAITING);
		otherPlayer.setTradeStage(TradeStage.WAITING);
		giveBackItems(player);
		player.getUpdateFlags().faceEntity(65535);
		otherPlayer.getUpdateFlags().faceEntity(65535);
		player.setClickId(-1);
	}
	
	public static void giveBackItems(Player player) {
		Player otherPlayer = World.getPlayers()[player.getClickId()];
		for (int i = 0; i < Inventory.SIZE; i ++) {
			if (player.getTrade().get(i) != null) {
				Item item = player.getTrade().get(i);
				if (item != null) {
					player.getTrade().remove(item);
					player.getInventory().addItem(item);
				}
			}
		}
		for (int i = 0; i < Inventory.SIZE; i ++) {
			if (otherPlayer.getTrade().get(i) != null) {
				Item item = otherPlayer.getTrade().get(i);
				if (item != null) {
					otherPlayer.getTrade().remove(item);
					otherPlayer.getInventory().addItem(item);
				}
			}
		}
	}
	
	public static void offerItem(Player player, int slot, int tradeItem, int amount) {
		Player otherPlayer = World.getPlayers()[player.getClickId()];
		if (player.getTradeStage().equals(TradeStage.SECOND_TRADE_WINDOW)) {
			return;
		}
		if (player == null || !player.isLoggedIn() || otherPlayer == null || 
				!otherPlayer.isLoggedIn()) {
			return;
		}
		if (tradeItem == -1) {
			return;
		}
		Item inv = player.getInventory().getItemContainer().get(slot);
		int invAmount = player.getInventory().getItemContainer().getCount(tradeItem);
		if (inv.getId() != tradeItem) {
			return;
		}
		if (inv.getId() <= 0 || tradeItem <= 0 || inv.getCount() < 1 || 
				amount < 1) {
			return;
		}
		if (ItemManager.getInstance().isUntradeable(tradeItem)) {
			player.getActionSender().sendMessage("You cannot trade that item.");
			return;
		}
		if (invAmount > amount) {
			invAmount = amount;
		}
		if (inv.getDefinition().isStackable()) {
			player.getInventory().removeItemSlot(new Item(tradeItem, invAmount), slot);
		} else {
			for (int i = 0; i < invAmount; i ++) {
				player.getInventory().removeItem(new Item(tradeItem, 1));
			}
		}
		int tradeAmount = player.getTrade().getCount(tradeItem);
		if (tradeAmount > 0 && inv.getDefinition().isStackable()) {
			player.getTrade().set(player.getTrade().getSlotById(inv.getId()), 
					new Item(tradeItem, tradeAmount + invAmount));
		} else {
			player.getTrade().add(new Item(inv.getId(), invAmount));
		}
		refresh(player, otherPlayer);
		player.setTradeStage(TradeStage.SEND_REQUEST_ACCEPT);
		otherPlayer.setTradeStage(TradeStage.SEND_REQUEST_ACCEPT);
		player.getActionSender().sendString("", 3431);
		otherPlayer.getActionSender().sendString("", 3431);
	}
	
	public static void removeTradeItem(Player player, int slot, int tradeItem, int amount) {
		Player otherPlayer = World.getPlayers()[player.getClickId()];
		if (player.getTradeStage().equals(TradeStage.SECOND_TRADE_WINDOW)) {
			return;
		}
		if (player == null || !player.isLoggedIn() || otherPlayer == null || 
				!otherPlayer.isLoggedIn()) {
			return;
		}
		if (tradeItem == -1) {
			return;
		}
		Item itemOnScreen = player.getTrade().get(slot);
		int itemOnScreenAmount = player.getTrade().getCount(tradeItem);
		if (itemOnScreen == null || itemOnScreen.getId() <= 0 || 
				itemOnScreen.getId() != tradeItem) {
			return;
		}
		if (itemOnScreenAmount > amount) {
			itemOnScreenAmount = amount;
		}
		player.getInventory().addItem(new Item(itemOnScreen.getId(), itemOnScreenAmount));
		player.getTrade().remove(new Item(tradeItem, itemOnScreenAmount), slot);
		refresh(player, otherPlayer);
		player.setTradeStage(TradeStage.SEND_REQUEST_ACCEPT);
		otherPlayer.setTradeStage(TradeStage.SEND_REQUEST_ACCEPT);
		player.getActionSender().sendString("", 3431);
		otherPlayer.getActionSender().sendString("", 3431);
	}
	
	public static void acceptStageOne(Player player) {
		Player otherPlayer = World.getPlayers()[player.getClickId()];
		if (player.getTrade().freeSlots() < otherPlayer.getInventory().getItemContainer().freeSlot()) {
			player.getActionSender().sendMessage("Other player dosen't have enough inventory space for this trade.");
			return;
		}
		if (otherPlayer.getTrade().freeSlots() < player.getInventory().getItemContainer().freeSlot()) {
			player.getActionSender().sendMessage("You don't have enough inventory space for this trade.");
			return;
		}
		player.setTradeStage(TradeStage.ACCEPT);
		if (!otherPlayer.getTradeStage().equals(TradeStage.ACCEPT)) {
			player.getActionSender().sendString("Waiting for other player...", 3431);
			otherPlayer.getActionSender().sendString("Other player accepted.", 3431);
		} else {
			refresh(player, otherPlayer);
			player.getActionSender().sendInterface(3443, 3213);
			otherPlayer.getActionSender().sendInterface(3443, 3213);
			player.setTradeStage(TradeStage.SECOND_TRADE_WINDOW);
			otherPlayer.setTradeStage(TradeStage.SECOND_TRADE_WINDOW);
			player.getActionSender().sendString("Are you sure you want to accept this trade?", 3535);
			otherPlayer.getActionSender().sendString("Are you sure you want to accept this trade?", 3535);
			sendSecondScreen(player);
			sendSecondScreen(otherPlayer);
		}
	}
	
	public static void acceptStageTwo(Player player) {
		if (!player.getTradeStage().equals(TradeStage.SECOND_TRADE_WINDOW)) {
			return;
		}
		Player otherPlayer = World.getPlayers()[player.getClickId()];
		player.setTradeStage(TradeStage.ACCEPT);
		if (!otherPlayer.getTradeStage().equals(TradeStage.ACCEPT)) {
			player.getActionSender().sendString("Waiting for other player...", 3535);
			otherPlayer.getActionSender().sendString("Other player accepted.", 3535);
		} else {
			for (int i = 0; i < Inventory.SIZE; i ++) {
				Item newItems = player.getTrade().get(i);
				if (newItems == null) {
					continue;
				}
				player.getTrade().remove(newItems);
				otherPlayer.getInventory().addItem(newItems);
			}
			for (int i = 0; i < Inventory.SIZE; i ++) {
				Item newItems = otherPlayer.getTrade().get(i);
				if (newItems == null) {
					continue;
				}
				otherPlayer.getTrade().remove(newItems);
				player.getInventory().addItem(newItems);
			}
			player.setTradeStage(TradeStage.WAITING);
			otherPlayer.setTradeStage(TradeStage.WAITING);
			player.getActionSender().sendMessage("You accept the trade.");
			otherPlayer.getActionSender().sendMessage("You accept the trade.");
			player.getActionSender().removeInterfaces();
			otherPlayer.getActionSender().removeInterfaces();
		}
	}
	
	private static void sendSecondScreen(Player player) {
		Player otherPlayer = World.getPlayers()[player.getClickId()];
		StringBuilder trade = new StringBuilder();
		boolean empty = true;
		for(int i = 0; i < Inventory.SIZE; i++) {
			Item item = player.getTrade().get(i);
			String prefix = "";
			if(item != null) {
				empty = false;
				if(item.getCount() >= 100 && item.getCount() < 1000000) {
					prefix = "@cya@" + (item.getCount()/1000) + "K @whi@(" + item.getCount() + ")";
				} else if(item.getCount() >= 1000000) {
					prefix = "@gre@" + (item.getCount()/1000000) + " million @whi@(" + item.getCount() + ")";
				} else {
					prefix = "" + item.getCount();
				}
				trade.append(item.getDefinition().getName());
				trade.append(" x ");
				trade.append(prefix);
				trade.append("\\n");
			}
		}
		if(empty) {
			trade.append("Absolutely nothing!");
		}
		player.getActionSender().sendString(trade.toString(), 3557);
		trade = new StringBuilder();
		empty = true;
		for(int i = 0; i < Inventory.SIZE; i++) {
			Item item = otherPlayer.getTrade().get(i);
			String prefix = "";
			if(item != null) {
				empty = false;
				if(item.getCount() >= 100 && item.getCount() < 1000000) {
					prefix = "@cya@" + (item.getCount()/1000) + "K @whi@(" + item.getCount() + ")";
				} else if(item.getCount() >= 1000000) {
					prefix = "@gre@" + (item.getCount()/1000000) + " million @whi@(" + item.getCount() + ")";
				} else {
					prefix = "" + item.getCount();
				}
				trade.append(item.getDefinition().getName());
				trade.append(" x ");
				trade.append(prefix);
				trade.append("\\n");
			}
		}
		if(empty) {
			trade.append("Absolutely nothing!");
		}
		player.getActionSender().sendString(trade.toString(), 3558);
	}
	
	public static void handleDisconnect(Player player) {
		Player otherPlayer = World.getPlayers()[player.getClickId()];
		if (otherPlayer.getTradeStage().equals(TradeStage.SEND_REQUEST_ACCEPT)) {
			otherPlayer.getActionSender().sendMessage("Other player has declined the trade.");
		}
		player.getActionSender().removeInterfaces();
		otherPlayer.getActionSender().removeInterfaces();
		player.setTradeStage(TradeStage.WAITING);
		otherPlayer.setTradeStage(TradeStage.WAITING);
		player.getUpdateFlags().faceEntity(65535);
		otherPlayer.getUpdateFlags().faceEntity(65535);
		for (int i = 0; i < Inventory.SIZE; i ++) {
			if (player.getTrade().get(i) != null) {
				Item item = player.getTrade().get(i);
				if (item != null) {
					player.getTrade().remove(item);
					player.getInventory().getItemContainer().add(item);
				}
			}
		}
		for (int i = 0; i < Inventory.SIZE; i ++) {
			if (otherPlayer.getTrade().get(i) != null) {
				Item item = otherPlayer.getTrade().get(i);
				if (item != null) {
					otherPlayer.getTrade().remove(item);
					otherPlayer.getInventory().getItemContainer().add(item);
				}
			}
		}
		player.setClickId(-1);
	}
	
}
