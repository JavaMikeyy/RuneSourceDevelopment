package com.rs2.model.content.combat.util;

import com.rs2.model.Entity;
import com.rs2.model.players.Player;
import com.rs2.model.players.ItemManager;

public class FreezeEntity {
	
	public static void freezeTick(Entity entity) {
		if (entity.isFrozen()) {
			if (entity.getFrozenTimer() == 0) {
				freezeEntity(entity, false, 0);
			}
			else {
				entity.setFrozenTimer(entity.getFrozenTimer() - 1);
			}
		}
		if (entity.getFreezeImmunityTimer() > 0) {
			entity.setFreezeImmunityTimer(entity.getFreezeImmunityTimer() - 1);
		}
	}
	
	public static void freezeEntity(Entity entity, boolean settingEntityFrozen, int freezeTimer) {
		if (entity.getFreezeImmunityTimer() > 0 && settingEntityFrozen) {
			return;
		}
		if (settingEntityFrozen) {
			entity.setFrozenTimer(freezeTimer);
			entity.setFrozen(true);
		}
		else if (!settingEntityFrozen) {
			entity.setFreezeImmunityTimer(7);
			entity.setFrozen(false);
		}
	}
	
}



