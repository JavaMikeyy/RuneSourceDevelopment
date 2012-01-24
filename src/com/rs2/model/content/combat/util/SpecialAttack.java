package com.rs2.model.content.combat.util;

import com.rs2.model.Entity;
import com.rs2.model.players.Player;

public class SpecialAttack {
	
	public static void specialAttackTick(Player player) {
		if (player.getSpecialAmount() < 10.0) {
			if (player.getSpecialRechargeTimer() == 0) {
				if (player.getSpecialAmount() + 1.0 > 10.0) {
					player.setSpecialAmount(10.0);
				}
				else {
					player.setSpecialAmount(player.getSpecialAmount() + 1.0);
				}
				if (player.getEquipment().getItemContainer().get(3) != null) {
					int weaponId = player.getEquipment().getItemContainer().get(3).getId();
					player.getActionSender().updateSpecialAmount(getSpecialBarData(weaponId)[1]);
					player.getActionSender().updateSpecialBarText(getSpecialBarData(weaponId)[1]);
				}
				player.setSpecialRechargeTimer(100);
			}
			else {
				player.setSpecialRechargeTimer(player.getSpecialRechargeTimer() - 1);
			}
		}
	}
	
	public static void performSpecialAttack(Entity attacker, Entity victim) {
		if (attacker instanceof Player) {
			Player player = (Player) attacker;
			int weaponId = player.getEquipment().getItemContainer().get(3).getId();
			switch (weaponId) {
				case 4151:
					victim.getUpdateFlags().sendHighGraphic(341, 0);
					break;
				case 1215:
				case 5698:
					HitDelay hit = new HitDelay(player, attacker, victim, 2, 0);
					attacker.getUpdateFlags().sendHighGraphic(252, 0);
					break;
			}
			attacker.getUpdateFlags().sendAnimation(attacker.grabAttackAnimation(), 0);
			HitDelay hit = new HitDelay(player, attacker, victim, 1, 0);
			player.setSpecialAttackActive(false);
			player.setSpecialAmount(player.getSpecialAmount() - amountRequiredForSpecial(player));
			player.getActionSender().updateSpecialAmount(getSpecialBarData(weaponId)[1]);
			player.getActionSender().updateSpecialBarText(getSpecialBarData(weaponId)[1]);
		}
	}
	
	public static boolean specialActivated(Entity attacker) {
		if (attacker instanceof Player) {
			Player player = (Player) attacker;
			return player.isSpecialAttackActive();
		}
		return false;
	}
	
	/**
	  * Returns the special bar's id, then the special bar's text id
	  */
	public static int[] getSpecialBarData(int weaponId) {
		int[] data = {0, 0};
		switch (weaponId) {
			case 4151:
				data[0] = 12323;
				data[1] = 12335;
				return data;
			case 1215:
			case 5698:
				data[0] = 7574;
				data[1] = 7586;
				break;
		}
		return data;
	}
	
	public static void addSpecialBar(Player player, int weaponId) {
		int[] specialBarData = getSpecialBarData(weaponId);
		if (specialBarData[0] != 0) {
			player.getActionSender().sendSpecialBar(0, specialBarData[0]);
			player.getActionSender().updateSpecialAmount(specialBarData[1]);
			player.getActionSender().updateSpecialBarText(specialBarData[1]);
		}
	}
	
	public static double amountRequiredForSpecial(Player player) {
		int itemId = player.getEquipment().getItemContainer().get(3).getId();
		double specialAmount = player.getSpecialAmount();
		switch (itemId) {
			case 4151:
				return 5.0;
			case 5698:
			case 1215:
				return 2.5;
		}
		return 5.5;
	}
	
	public static int getSpecialAttackAnimation(Player player, int animId) {
		int itemId = player.getEquipment().getItemContainer().get(3).getId();
		switch (itemId) {
			case 1215:
			case 5698:
				return 1062;
		}
		return animId;
	}
	
	public static void performRandomSpecialAttack(Entity attacker) {
		if (attacker instanceof Player) {
			Player player = (Player) attacker;
			int[][][] randomSpecialWeapons = {
				//{item, slot}
				{{0, 0}, {0, 0}}
			};
			int itemId = player.getEquipment().getItemContainer().get(3).getId();
		}
	}
	
	public static void clickSpecialBar(Player player, int buttonId) {
		if (player.getEquipment().getItemContainer().get(3) == null)
			return;
		int weaponId = player.getEquipment().getItemContainer().get(3).getId();
		switch (buttonId) {
			case 29188:
			case 29163:
			case 33033:
			case 29038:
			case 48023: //Abyssal whip
			case 29138:
			case 29113:
			case 29238:
				if (amountRequiredForSpecial(player) <= player.getSpecialAmount()) {
					player.setSpecialAttackActive(!player.isSpecialAttackActive());
					player.getActionSender().updateSpecialBarText(getSpecialBarData(weaponId)[1]);
				}
				else {
					player.getActionSender().sendMessage("You don't have enough special attack to do that.");
				}
				break;
			
			case 29063: //Dragon battleaxe
				break;
		}
	}

}














