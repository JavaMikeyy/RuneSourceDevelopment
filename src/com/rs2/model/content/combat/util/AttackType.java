package com.rs2.model.content.combat.util;

import com.rs2.model.tick.Tick;
import com.rs2.model.Entity;
import com.rs2.model.players.Player;

public class AttackType {
	
	public static void determineAttackType(Player player) {
		if (player.getMagic().singleMagicAttack != null) {
			player.setAttackType(Entity.AttackTypes.MAGIC);
			return;
		}
		else if (player.getMagic().autoCast != null && player.getMagic().autoCast.autoCasting) {
			player.setAttackType(Entity.AttackTypes.MAGIC);
			return;
		}
		else if (player.getEquipment().getItemContainer().get(3) != null) {
			if (player.getRanged().usingBow(player.getEquipment().getItemContainer().get(3).getId())) {
				player.setAttackType(Entity.AttackTypes.RANGED);
				return;
			}
		}
		player.setAttackType(Entity.AttackTypes.MELEE);
	}
	
}
