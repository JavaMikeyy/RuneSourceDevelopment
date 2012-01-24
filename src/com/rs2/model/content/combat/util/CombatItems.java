package com.rs2.model.content.combat.util;

import com.rs2.model.Entity;
import com.rs2.model.World;
import com.rs2.model.tick.Tick;
import com.rs2.model.players.Player;
import com.rs2.model.players.ItemManager;
import com.rs2.util.Areas;


public class CombatItems {
	//needs ring of wealth
	public static void appendRingOfRecoil(Player player, int hit) {
		if (player.getEquipment().getItemContainer().get(12) == null)
			return;
		if (player.getEquipment().getItemContainer().get(12).getId() == 2550) {
			if ((hit / 4) > 0)
			player.getCombatingEntity().hit(hit / 4, 1);
			if (player.getRingOfRecoilLife() > hit / 4) {
				player.setRingOfRecoilLife(player.getRingOfRecoilLife() - (hit / 4));
			}
			else {
				player.getActionSender().sendMessage("Your Ring of recoil crumbles to dust.");
				player.getEquipment().getItemContainer().set(12, null);
				player.getEquipment().refresh();
				player.setRingOfRecoilLife(400);
			}
		}
	}
	
	public static void appendRingOfLife(final Player player) {
		if (player.getEquipment().getItemContainer().get(12) == null)
			return;
		if (player.getEquipment().getItemContainer().get(12).getId() == 2570) {
			if (player.getSkill().getLevel()[3] <= 
					(player.getSkill().getLevelForXP(player.getSkill().getExp()[3]) / 10) &&
					!player.isDead()) {
				if (Areas.getWildernessLevel(player) < 31) {
					Poison.appendPoison(player, false, 0);
					player.getActionSender().sendMessage("Your Ring of life crumbles to dust.");
					player.getTeleportation().teleport(3086, 3488, 0);
					player.getEquipment().getItemContainer().set(12, null);
					player.getEquipment().refresh();
				}
			}
		}
	}
	
	
	
}