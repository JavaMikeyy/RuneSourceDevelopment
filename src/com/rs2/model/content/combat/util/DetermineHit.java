package com.rs2.model.content.combat.util;

import com.rs2.model.content.Prayer;
import com.rs2.model.players.Player;
import com.rs2.model.players.Item;
import com.rs2.model.players.ItemManager;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.Entity;
import com.rs2.util.Misc;

public class DetermineHit {
	
	public static int determineHit(Entity attacker, Entity victim) {
		if (attacker.getAttackType() == Entity.AttackTypes.MAGIC) {
			double accuracy = getMagicAccuracy(attacker, victim);
			int chance = Misc.randomNumber(100);
			if (chance > accuracy)
				return 0;
			else
				return calculateHit(attacker);
		}
		else if (attacker.getAttackType() == Entity.AttackTypes.RANGED) {
			double accuracy = getRangedAccuracy(attacker, victim);
			int chance = Misc.randomNumber(100);
			if (chance > accuracy)
				return 0;
			else
				return calculateHit(attacker);
		}
		else if (attacker.getAttackType() == Entity.AttackTypes.MELEE) {
			double accuracy = getMeleeAccuracy(attacker, victim);
			int chance = Misc.randomNumber(100);
			if (chance > accuracy)
				return 0;
			else
				return calculateHit(attacker);
		}
		System.out.println("Unable to determine hit");
		return 0;
	}
	
	public static double getSpecialStr(Entity entity) {
		if (entity instanceof Player) {
			double[][] specialStrengthModifiers = {
					{5698, 1.1}, {5680, 1.1}, {1231, 1.1}, {1215, 1.1}, {3204, 1.1}, {1305, 1.15}, 
					{1434, 1.45}, {11694, 1.34375}, {11696, 1.1825}, {11698, 1.075}, 
					{11700, 1.075}, {861, 1.1}
			};
			Player player = (Player) entity;
			int itemId = player.getEquipment().getItemContainer().get(3).getId();
			for (double[] data : specialStrengthModifiers) {
				if (data[0] == itemId) {
					return data[1];
				}
			}
		}
		return 1.0;
	}
	
	public static int getMeleeMaxHit(Entity entity) {
		if (entity instanceof Player) {
			Player player = (Player) entity;
			double strengthModifier = 1.0; 
				if (player.getIsUsingPrayer()[Prayer.BURST_OF_STRENGTH])
					strengthModifier = 1.05;
				if (player.getIsUsingPrayer()[Prayer.SUPERHUMAN_STRENGTH])
					strengthModifier = 1.1;
				if (player.getIsUsingPrayer()[Prayer.ULTIMATE_STRENGTH])
					strengthModifier = 1.15;
				if (player.getIsUsingPrayer()[Prayer.CHIVALRY])
					strengthModifier = 1.1;
				if (player.getIsUsingPrayer()[Prayer.PIETY])
					strengthModifier = 1.23;
			int effectiveStrength = (int) (entity.getSkillLevel(Skill.STRENGTH) * strengthModifier);
			System.out.println("" + effectiveStrength);
			int strengthBonus = entity.getBonus(10);
			//Needs prayer bonus, style bonuses, and void bonuses
			return (int) ((13 + effectiveStrength + (strengthBonus / 8) + 
					((effectiveStrength * strengthBonus) / 64)) / 10 *
					(SpecialAttack.specialActivated(entity) ? getSpecialStr(entity) : 1));
		}
		else {
			entity.getNpcMaxHit();
		}
		return 0;
	}
	
	
	public static double getMeleeAccuracy(Entity attacker, Entity victim) {
		double attackModifier = 1.0; 
		if (attacker instanceof Player) {
			Player player = (Player) attacker;
			if (player.getIsUsingPrayer()[Prayer.CLARITY_OF_THOUGHT])
				attackModifier = 1.05;
			if (player.getIsUsingPrayer()[Prayer.IMPROVED_REFLEXES])
				attackModifier = 1.1;
			if (player.getIsUsingPrayer()[Prayer.INCREDIBLE_REFLEXES])
				attackModifier = 1.15;
			if (player.getIsUsingPrayer()[Prayer.CHIVALRY])
				attackModifier = 1.15;
			if (player.getIsUsingPrayer()[Prayer.PIETY])
				attackModifier = 1.2;
		}
		int attackLevel = (int) (attacker.getSkillLevel(Skill.ATTACK) * attackModifier);
		System.out.println("attack: " + attackLevel);
		int attackBonus = (attacker.getBonus(0) + attacker.getBonus(1) + attacker.getBonus(2)) / 3;
		double defenceModifier = 1.0; 
			if (victim instanceof Player) {
				Player otherPlayer = (Player) victim;
				if (otherPlayer.getIsUsingPrayer()[Prayer.THICK_SKIN])
					defenceModifier = 1.05;
				if (otherPlayer.getIsUsingPrayer()[Prayer.ROCK_SKIN])
					defenceModifier = 1.1;
				if (otherPlayer.getIsUsingPrayer()[Prayer.STEEL_SKIN])
					defenceModifier = 1.15;
				if (otherPlayer.getIsUsingPrayer()[Prayer.CHIVALRY])
					defenceModifier = 1.2;
				if (otherPlayer.getIsUsingPrayer()[Prayer.PIETY])
					defenceModifier = 1.25;
			}
		int defenceLevel = (int) (victim.getSkillLevel(Skill.DEFENCE) * defenceModifier);
		int defenceBonus = (victim.getBonus(5) + victim.getBonus(6) + victim.getBonus(7)) / 3;
		
		double effectiveAttack = (attackLevel + attackBonus + 8);
		double effectiveDefense = (defenceLevel + defenceBonus + 8);

		double hitChance = effectiveAttack * (1 + attackBonus / 20);
		double blockChance = effectiveDefense * (1 + defenceBonus / 64);
		if(hitChance < blockChance) {
			System.out.println("hitChance < blockChance");
			return ((hitChance - 1) / (1.8 * blockChance)) * 100;
		}
		else {
			System.out.println("else");
			return (1 - (blockChance + 1) / (1.8 * hitChance)) * 100;
		}
	}
	
	public static int getRangedMaxHit(Entity entity) {
		double rangedModifier = 1.0; 
		if (entity instanceof Player) {
			Player player = (Player) entity;
			if (player.getIsUsingPrayer()[Prayer.SHARP_EYE])
				rangedModifier = 1.05;
			if (player.getIsUsingPrayer()[Prayer.HAWK_EYE])
				rangedModifier = 1.1;
			if (player.getIsUsingPrayer()[Prayer.EAGLE_EYE])
				rangedModifier = 1.15;
		}
		int effectiveRanged = (int) (entity.getSkillLevel(Skill.RANGED) * rangedModifier);
		if (entity instanceof Player) {
			Player player = (Player) entity;
			effectiveRanged = (int) ((effectiveRanged / 2));
		}
		int rangedBonus = entity.getBonus(4);
		//Needs prayer bonus, style bonuses, and void bonuses
		return (int)
				((13 + effectiveRanged + (rangedBonus / 8) + 
				((effectiveRanged * rangedBonus) / 64)) / 6);
	}
	
	
	public static double getRangedAccuracy(Entity attacker, Entity victim) {
		double rangedModifier = 1.0; 
		if (attacker instanceof Player) {
			Player player = (Player) attacker;
			if (player.getIsUsingPrayer()[Prayer.SHARP_EYE])
				rangedModifier = 1.0;
			if (player.getIsUsingPrayer()[Prayer.HAWK_EYE])
				rangedModifier = 1.1;
			if (player.getIsUsingPrayer()[Prayer.EAGLE_EYE])
				rangedModifier = 1.15;
		}
		int rangedLevel = (int) (attacker.getSkillLevel(Skill.RANGED) * rangedModifier);
		double modifier = 1.0;
		if (attacker instanceof Player) {
			Player player = (Player) attacker;
			rangedLevel = rangedLevel;
			modifier = arrowStrengthModifier(player);
		}
		int rangedBonus = attacker.getBonus(4);
		double defenceModifier = 1.0; 
			if (victim instanceof Player) {
				Player otherPlayer = (Player) victim;
				if (otherPlayer.getIsUsingPrayer()[Prayer.THICK_SKIN])
					defenceModifier = 1.05;
				if (otherPlayer.getIsUsingPrayer()[Prayer.ROCK_SKIN])
					defenceModifier = 1.1;
				if (otherPlayer.getIsUsingPrayer()[Prayer.STEEL_SKIN])
					defenceModifier = 1.15;
				if (otherPlayer.getIsUsingPrayer()[Prayer.CHIVALRY])
					defenceModifier = 1.2;
				if (otherPlayer.getIsUsingPrayer()[Prayer.PIETY])
					defenceModifier = 1.25;
			}
		int defenceLevel = (int) (victim.getSkillLevel(Skill.DEFENCE) * defenceModifier);
		int defenceBonus = victim.getBonus(9);
		
		double effectiveRanged = (rangedLevel + rangedBonus + 8);
		double effectiveDefense = (defenceLevel + defenceBonus + 8);

		double hitChance = ((effectiveRanged * (1 + rangedBonus / 40)) / 2) * modifier;
		double blockChance = effectiveDefense * (1 + defenceBonus / 190);
		if(hitChance < blockChance)
			return ((hitChance - 1) / (1.8 * blockChance)) * 100;
		else
			return (1 - (blockChance + 1) / (1.8 * hitChance)) * 100;
	}
	
	public static int getMagicMaxHit(Entity entity) {
		return entity.getMagicMaxHit();
	}
	
	
	public static double getMagicAccuracy(Entity attacker, Entity victim) {
		double magicModifier = 1.0; 
		if (attacker instanceof Player) {
			Player player = (Player) attacker;
			if (player.getIsUsingPrayer()[Prayer.MYSTIC_WILL])
				magicModifier = 1.05;
			if (player.getIsUsingPrayer()[Prayer.MYSTIC_LORE])
				magicModifier = 1.1;
			if (player.getIsUsingPrayer()[Prayer.MYSTIC_MIGHT])
				magicModifier = 1.15;
		}
		int attackerMagicLevel = (int) (attacker.getSkillLevel(Skill.MAGIC) * magicModifier);
		int magicBonus = attacker.getBonus(3);
		double defenceModifier = 1.0; 
			if (victim instanceof Player) {
				Player otherPlayer = (Player) victim;
				if (otherPlayer.getIsUsingPrayer()[Prayer.THICK_SKIN])
					defenceModifier = 1.05;
				if (otherPlayer.getIsUsingPrayer()[Prayer.ROCK_SKIN])
					defenceModifier = 1.1;
				if (otherPlayer.getIsUsingPrayer()[Prayer.STEEL_SKIN])
					defenceModifier = 1.15;
				if (otherPlayer.getIsUsingPrayer()[Prayer.CHIVALRY])
					defenceModifier = 1.2;
				if (otherPlayer.getIsUsingPrayer()[Prayer.PIETY])
					defenceModifier = 1.25;
			}
		int defenceLevel = (int) (victim.getSkillLevel(Skill.DEFENCE) * defenceModifier);
		int defenceBonus = victim.getBonus(8);
		
		double effectiveMagic = (attackerMagicLevel + magicBonus  + 8);
		double defenderMagicLevel = victim.getSkillLevel(Skill.MAGIC) * 0.7;
		double effectiveDefense = ((defenceLevel + defenceBonus + 8) * 0.3) + defenderMagicLevel;
		
		double hitChance = effectiveMagic * (1 + magicBonus / 64);
		double blockChance = effectiveDefense * (1 + defenceBonus / 64);
		if(hitChance < blockChance)
			return ((hitChance - 1) / (1.8 * blockChance)) * 100;
		else
			return (1 - (blockChance + 1) / (1.8 * hitChance)) * 100;
	}
	
	public static int calculateHit(Entity attacker) {
		int hit = 0;
		if (attacker instanceof Player) {
			Player player = (Player) attacker;
			if (attacker.getAttackType() == Entity.AttackTypes.MAGIC) {
				int randomHit = Misc.randomNumber(getMagicMaxHit(attacker));
				hit = (randomHit);
			}
			else if (attacker.getAttackType() == Entity.AttackTypes.RANGED) {
				int randomHit = Misc.randomNumber(getRangedMaxHit(attacker));
				hit = (randomHit);
			}
			else if (attacker.getAttackType() == Entity.AttackTypes.MELEE) {
				int randomHit = Misc.randomNumber(getMeleeMaxHit(attacker));
				hit = (randomHit);
			}
		}
		else {
			int randomHit = Misc.randomNumber(attacker.getNpcMaxHit());
			hit = (randomHit);
		}
		return hit;
	}
	
	public static double arrowStrengthModifier(Player player) {
		int arrowId = player.getEquipment().getItemContainer().get(13).getId();
		String bowName = ItemManager.getInstance().getItemName(
				player.getEquipment().getItemContainer().get(3).getId());
		if (bowName.toLowerCase().contains("knife")) {
			return 2.0;
		}
		if (bowName.toLowerCase().contains("shortbow") || 
				bowName.toLowerCase().contains("longbow")) {
			switch (arrowId) {
				case 882: // Bronze
				case 883: // (p)
					return 1.0;
				case 884: // Iron
				case 885: // (p)
					return 1.2;
				case 886: // Steel
				case 887: // (p)
					return 1.4;
				case 888: // Mithril
				case 889: // (p)
					return 1.6;
				case 890: // Adamant
				case 891: // (p)
					return 1.8;
				case 892: // Rune
				case 893: // (p)
					return 2.0;
			}
		}
		if (bowName.toLowerCase().contains("c'bow") || 
				bowName.toLowerCase().contains("crossbow")) {
			switch (arrowId) {
				case 0:
					break;
			}
		}
		return 0.0;
	}
	
}













