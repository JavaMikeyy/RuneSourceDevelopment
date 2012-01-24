package com.rs2.model.content.combat.util;

import com.rs2.model.Entity;
import com.rs2.model.players.Player;
import com.rs2.util.Misc;

public class MagicSpellEffects {
	
	/**
	  * Applying any extra effects (poisoning, freezing, etc) after the attack.
	  */
	public static void applyMagicEffects(Entity attacker, Entity victim, int hit) {
		if (attacker instanceof Player) {
			Player player = (Player) attacker;
			int spellId = player.getMagic().getSpellDefinitions()[player.getMagic().getMagicIndex()].getSpellId();
			switch (spellId) {
				case 12891: //Ice barrage
					FreezeEntity.freezeEntity(victim, true, 34);
					break;
				case 12871: //Ice blitz
					FreezeEntity.freezeEntity(victim, true, 25);
					break;
				case 12881: //Ice burst
					FreezeEntity.freezeEntity(victim, true, 17);
					break;
				case 12861: //Ice burst
					FreezeEntity.freezeEntity(victim, true, 9);
					break;
				case 12929: //Blood barrage
				case 12911: //Blood blitz
				case 12918: //Blood burst
				case 12901: //Blood rush
					int finalHitpoints = player.getSkill().getLevel()[3] + (hit / 4);
					if (finalHitpoints <= player.getSkill().getLevelForXP(player.getSkill().getExp()[3])) {
						player.getSkill().getLevel()[3] = finalHitpoints;
					}
					else {
						player.getSkill().getLevel()[3] = 
								player.getSkill().getLevelForXP(player.getSkill().getExp()[3]);
					}
					player.getSkill().refresh(3);
					break;
				case 13023: //Shadow barrage
					if (victim instanceof Player) {
						Player otherPlayer = (Player) victim;
						int finalAttack = (otherPlayer.getSkill().getLevelForXP(otherPlayer.getSkill().getExp()[0]) - 
								(int) (otherPlayer.getSkill().getLevelForXP(otherPlayer.getSkill().getExp()[0]) * 0.15));
						if (otherPlayer.getSkill().getLevel()[0] > finalAttack) {
							otherPlayer.getSkill().getLevel()[0] = finalAttack;
						}
						otherPlayer.getSkill().refresh(0);
					}
					break;
				case 12999: //Shadow bitz
				case 13011: //Shadow burst
				case 12987: //Shadow rush
					if (victim instanceof Player) {
						Player otherPlayer = (Player) victim;
						int finalAttack = (otherPlayer.getSkill().getLevelForXP(otherPlayer.getSkill().getExp()[0]) - 
								(int) (otherPlayer.getSkill().getLevelForXP(otherPlayer.getSkill().getExp()[0]) * 0.1));
						if (otherPlayer.getSkill().getLevel()[0] > finalAttack) {
							otherPlayer.getSkill().getLevel()[0] = finalAttack;
						}
						otherPlayer.getSkill().refresh(0);
					}
					break;
				case 12975: //Smoke barrage
				case 12951: //Smoke blitz
					if (Misc.randomNumber(10) == 0)
						Poison.appendPoison(victim, true, 4);
					break;
				case 12963: //Smoke burst
				case 12939: //Smoke rush	
					if (Misc.randomNumber(10) == 0)
						Poison.appendPoison(victim, true, 2);
					break;
			}
		}
	}
	
}