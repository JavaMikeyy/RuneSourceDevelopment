package com.rs2.model.content.combat.util;

import com.rs2.model.content.Prayer;
import com.rs2.model.players.Player;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.Entity;

public class CombatUtils {
	
	public static int getMeleeMaxHit(Player player) {
		int maxHit = 0;
		double prayerMultiplier = 1;
		double otherBonusMultiplier = 1; 
		int strengthLevel = player.getSkill().getLevel()[2];
		int combatStyleBonus = 0;
		/**
		 * Prayer multipliers here
		 */
		if (player.getIsUsingPrayer()[Prayer.BURST_OF_STRENGTH]) {
			prayerMultiplier = 1.05;
		} else if (player.getIsUsingPrayer()[Prayer.SUPERHUMAN_STRENGTH]) {
			prayerMultiplier = 1.1;
		} else if (player.getIsUsingPrayer()[Prayer.ULTIMATE_STRENGTH]) {
			prayerMultiplier = 1.15;
		} else if (player.getIsUsingPrayer()[Prayer.CHIVALRY]) {
			prayerMultiplier = 1.18;
		} else if (player.getIsUsingPrayer()[Prayer.PIETY]) {
			prayerMultiplier = 1.23;
		}
		
		/**
		 * Combat style multipliers here
		 */
		
		int effectiveStrengthDamage = (int) ((strengthLevel * prayerMultiplier * otherBonusMultiplier) + combatStyleBonus);
		double baseDamage = 1.3 + (effectiveStrengthDamage / 10) + (player.getBonuses().get(10) / 80) + 
			((effectiveStrengthDamage * player.getBonuses().get(10)) / 640);
		maxHit = (int) (baseDamage);
		return maxHit;
	}
	
}
