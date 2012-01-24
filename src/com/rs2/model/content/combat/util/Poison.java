package com.rs2.model.content.combat.util;

import com.rs2.model.Entity;
import com.rs2.model.players.Player;
import com.rs2.model.players.ItemManager;
import com.rs2.util.Misc;

public class Poison {
	
	public static final int POISON_HIT_TIMER = 30;
	
	public static void poisonTick(Entity entity) {
		if (entity.isPoisoned()) {
			if (entity.getPoisonHitTimer() == 0) {
				entity.hit(entity.getPoisonDamage(), 2);
				entity.setPoisonHitTimer(POISON_HIT_TIMER);
			}
			else {
				entity.setPoisonHitTimer(entity.getPoisonHitTimer() - 1);
			}
			for (int i = 1; i < 10; i++) {
				if (entity.getPoisonedTimer() == (POISON_HIT_TIMER * 5) * i)
					entity.setPoisonDamage(entity.getPoisonDamage() - 1);
			}
			if (entity.getPoisonDamage() == 1) {
				appendPoison(entity, false, 0);
				if (entity instanceof Player) {
					Player player = (Player) entity;
					player.getActionSender().sendMessage("The poison damaging you wears off..");
				}
			}
			entity.setPoisonedTimer(entity.getPoisonedTimer() + 1);
		}
		if (entity.getPoisonImmunityTimer() > 0) {
			entity.setPoisonImmunityTimer(entity.getPoisonImmunityTimer() - 1);
		}
	}
	
	public static void appendPoison(Entity entity, boolean addingPoison, int poisonDamage) {
		if (entity.getPoisonImmunityTimer() > 0 && addingPoison) {
			return;
		}
		if (addingPoison && entity.getPoisonDamage() < poisonDamage) {
			entity.setPoisonDamage(poisonDamage);
			entity.setPoisonHitTimer(POISON_HIT_TIMER);
			entity.setPoisonedTimer(0);
			entity.setPoisoned(true);
			if (entity instanceof Player) {
				Player player = (Player) entity;
				player.getActionSender().sendMessage("You have been poisoned!");
			}
		}
		else if (!addingPoison) {
			entity.setPoisonDamage(0);
			entity.setPoisonHitTimer(0);
			entity.setPoisonedTimer(0);
			entity.setPoisoned(false);
		}
	}
	
	public static void applyPoisonFromWeapons(Entity attacker, Entity victim) {
		if (attacker instanceof Player) {
			if (Misc.randomNumber(10) != 0)
				return;
			Player player = (Player) attacker;
			int weaponId = player.getEquipment().getItemContainer().get(3).getId();
			String weaponName = ItemManager.getInstance().getItemName(weaponId).toLowerCase();
			if (weaponName.contains("dart") || weaponName.contains("javelin") || 
					weaponName.contains("knife") || weaponName.contains("bolt") || 
					weaponName.contains("arrow") || weaponName.contains("jav'n")) {
				if (weaponName.contains("(p)")) {
					appendPoison(victim, true, 2);
				}
				else if (weaponName.contains("(p+)")) {
					appendPoison(victim, true, 3);
				}
				else if (weaponName.contains("(p++)")) {
					appendPoison(victim, true, 4);
				}
				else if (weaponName.contains("emerald bolts (e)")) {
					appendPoison(victim, true, 5);
				}
			}
			if (weaponName.contains("dagger") || weaponName.contains("spear") ||
					weaponName.contains("hasta")) {
				if (weaponName.contains("(p)")) {
					appendPoison(victim, true, 4);
				}
				else if (weaponName.contains("(p+)")) {
					appendPoison(victim, true, 5);
				}
				else if (weaponName.contains("(p++)")) {
					appendPoison(victim, true, 6);
				}
			}
		}
	}
	
}





