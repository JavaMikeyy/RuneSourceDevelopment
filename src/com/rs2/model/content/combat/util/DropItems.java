package com.rs2.model.content.combat.util;

import com.rs2.model.players.Player;
import com.rs2.model.players.Item;
import com.rs2.model.npcs.Npc;
import com.rs2.model.Entity;

public class DropItems {
	
	private Player player;
	
	public DropItems(Player player) {
		this.player = player;
	}
	
	public static void dropPlayerItems(Player player) {
		int[][] playerItems = new int[28 + 11][3];
		for (int i = 0; i < 27; i++) {
			Item item = player.getInventory().getItemContainer().get(i);
			if (item != null) {
				playerItems[i][0] = item.getDefinition().getId();
				playerItems[i][1] = player.getInventory().getItemContainer().getCount(item.getDefinition().getId());
				playerItems[i][2] = item.getDefinition().getShopBuyValue();
			}
		}
		for (int i = 0; i < 10; i++) {
			Item item = player.getEquipment().getItemContainer().get(i);
			if (item != null) {
				playerItems[i + 28][0] = item.getDefinition().getId();
				playerItems[i + 28][1] = player.getEquipment().getItemContainer().getCount(item.getDefinition().getId());
				playerItems[i + 28][2] = item.getDefinition().getShopBuyValue();
			}
		}
		int[][] protectedItems = new int[3][2];
		for (int i2 = 0; i2 < playerItems.length; i2++) {
			for (int i = 0; i < protectedItems.length; i++) {
				if (protectedItems[i][1] <= playerItems[i2][2] && playerItems[i2][0] != 0) {
					protectedItems[i][0] = playerItems[i2][0];
					protectedItems[i][1] = playerItems[i2][2];
				}
			}
		}
		for (int i = 0; i < protectedItems.length; i++)
			player.getActionSender().sendMessage("" + protectedItems[i][0]);
	}
	
}
