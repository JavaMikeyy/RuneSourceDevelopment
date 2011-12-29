package com.rs2.model.content.combat.util;

import com.rs2.model.tick.Tick;
import com.rs2.model.Entity;
import com.rs2.model.players.Player;

public class Skulling {
	
	public static void skullTick(Player player) {
		if (player.getSkullTimer() > -1)
			player.setSkullTimer((player.getSkullTimer() - 1));
		if (player.getSkullTimer() == 0) {
			player.setSkulled(false);
			for (int i = 0; i < 30; i++)
				player.setEngagedEntity(i, null);
		}
	}
	
	public static void skullEntity(Entity attacker, Entity victim) {
		if (attacker instanceof Player && victim instanceof Player) {
			Player player = (Player) attacker;
			int engagedCount = 0;
			for (int i = 0; i < 30; i++) {
				if (victim.getEngagedEntity(i) != null) {
					if (victim.getEngagedEntity(i) == attacker)
						return;
				}
			}
			for (int i = 0; i < 30; i++) {
				engagedCount ++;
				if (attacker.getEngagedEntity(i) != null) {
					if (attacker.getEngagedEntity(i) == victim)
						return;
				}
			}
			if (engagedCount == 30) {
				for (int i = 0; i < 30; i++)
					attacker.setEngagedEntity(i, null);
				engagedCount = 0;
			}
			attacker.setEngagedEntity((engagedCount == 0 ? engagedCount : engagedCount + 1), victim);
			player.setSkulled(true);
			player.setSkullTimer(600); // 5 Minutes
		}
	}
	
}
