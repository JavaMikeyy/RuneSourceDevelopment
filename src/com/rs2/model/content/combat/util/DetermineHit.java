package com.rs2.model.content.combat.util;

import com.rs2.model.content.Prayer;
import com.rs2.model.players.Player;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.Entity;
import com.rs2.util.Misc;

public class DetermineHit {
	
	public static int determineHit(Entity attacker, Entity victim) {
		if (attacker.getAttackType() == Entity.AttackTypes.MAGIC) {
			double accuracy = getMagicAccuracy(attacker, victim);
			int chance = Misc.randomNumber(100);
			System.out.println("" + accuracy + " " + chance);
			System.out.println("" + attacker);
			if (chance > accuracy)
				return 0;
			else
				return calculateHit(attacker);
		}
		else if (attacker.getAttackType() == Entity.AttackTypes.RANGED) {
			double accuracy = getRangedAccuracy(attacker, victim);
			int chance = Misc.randomNumber(100);
			System.out.println("" + accuracy + " " + chance);
			if (chance > accuracy)
				return 0;
			else
				return calculateHit(attacker);
		}
		else if (attacker.getAttackType() == Entity.AttackTypes.MELEE) {
			double accuracy = getMeleeAccuracy(attacker, victim);
			int chance = Misc.randomNumber(100);
			System.out.println("" + accuracy + " " + chance);
			System.out.println("" + attacker);
			if (chance > accuracy)
				return 0;
			else
				return calculateHit(attacker);
		}
		System.out.println("Unable to determine hit");
		return 0;
	}
	
	public static int getMeleeMaxHit(Entity entity) {
		if (entity instanceof Player) {
			int effectiveStrength = entity.getSkillLevel(Skill.STRENGTH);
			int strengthBonus = entity.getBonus(10);
			//Needs prayer bonus, style bonuses, and void bonuses
			return Math.round(13 + effectiveStrength + (strengthBonus / 8) + ((effectiveStrength * strengthBonus) / 64)) / 10;
		}
		else {
			entity.getNpcMaxHit();
		}
		return 0;
	}
	
	
	public static double getMeleeAccuracy(Entity attacker, Entity victim) {
		int attackLevel = attacker.getSkillLevel(Skill.ATTACK);
		int attackBonus = (attacker.getBonus(0) + attacker.getBonus(1) + attacker.getBonus(2)) / 3;
		int defenceLevel = victim.getSkillLevel(Skill.DEFENCE);
		int defenceBonus = (victim.getBonus(5) + victim.getBonus(6) + victim.getBonus(7)) / 3;
		
		double effectiveAttack = (attackLevel + attackBonus + 8);
		double effectiveDefense = (defenceLevel + defenceBonus + 8);

		double hitChance = effectiveAttack * (1 + attackBonus / 20);
		double blockChance = effectiveDefense * (1 + defenceBonus / 64);
		System.out.println("hitChance " + hitChance);
		System.out.println("blockChance " + blockChance);
		if(hitChance < blockChance)
			return ((hitChance - 1) / (1.8 * blockChance)) * 100;
		else
			return (1 - (blockChance + 1) / (1.8 * hitChance)) * 100;
	}
	
	public static int getRangedMaxHit(Entity entity) {
		int effectiveStrength = entity.getSkillLevel(Skill.STRENGTH);
		int strengthBonus = entity.getBonus(10);
		//Needs prayer bonus, style bonuses, and void bonuses
		return Math.round(13 + effectiveStrength + (strengthBonus / 8) + ((effectiveStrength * strengthBonus) / 64)) / 10;
	}
	
	
	public static double getRangedAccuracy(Entity attacker, Entity victim) {
		int rangedLevel = attacker.getSkillLevel(Skill.RANGE);
		int rangedBonus = attacker.getBonus(4);
		int defenceLevel = victim.getSkillLevel(Skill.DEFENCE);
		int defenceBonus = victim.getBonus(9);
		
		double effectiveRanged = (rangedLevel + rangedBonus + 8);
		double effectiveDefense = (defenceLevel + defenceBonus + 8);

		double hitChance = effectiveRanged * (1 + rangedBonus / 40);
		double blockChance = effectiveDefense * (1 + defenceBonus / 190);
		System.out.println("hitChance " + hitChance);
		System.out.println("blockChance " + blockChance);
		if(hitChance < blockChance)
			return ((hitChance - 1) / (1.8 * blockChance)) * 100;
		else
			return (1 - (blockChance + 1) / (1.8 * hitChance)) * 100;
	}
	
	public static int getMagicMaxHit(Entity entity) {
		return entity.getMagicMaxHit();
	}
	
	
	public static double getMagicAccuracy(Entity attacker, Entity victim) {
		int attackerMagicLevel = attacker.getSkillLevel(Skill.MAGIC);
		int magicBonus = attacker.getBonus(3);
		int defenceLevel = victim.getSkillLevel(Skill.DEFENCE);
		int defenceBonus = victim.getBonus(8);
		
		double effectiveMagic = (attackerMagicLevel + magicBonus  + 8);
		double defenderMagicLevel = victim.getSkillLevel(Skill.MAGIC) * 0.7;
		double effectiveDefense = ((defenceLevel + defenceBonus + 8) * 0.3) + defenderMagicLevel;
		
		double hitChance = effectiveMagic * (1 + magicBonus / 64);
		double blockChance = effectiveDefense * (1 + defenceBonus / 64);
		System.out.println("hitChance " + hitChance);
		System.out.println("blockChance " + blockChance);
		if(hitChance < blockChance)
			return ((hitChance - 1) / (1.8 * blockChance)) * 100;
		else
			return (1 - (blockChance + 1) / (1.8 * hitChance)) * 100;
	}
	
	public static int calculateHit(Entity attacker) {
		int hit = 0;
		if (attacker instanceof Player) {
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
	
}







