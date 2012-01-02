package com.rs2.model.content.combat.util;

import com.rs2.model.tick.Tick;
import com.rs2.model.Entity;
import com.rs2.model.players.Player;

public class AttackStyle {
	
	Player player;
	
	private AttackStyles attackStyle = AttackStyles.ACCURATE;
	
	public AttackStyle(Player player) {
		this.player = player;
	}
	
	public void setAttackStyle(AttackStyles attackStyle) {
		this.attackStyle = attackStyle;
	}
	
	public AttackStyles getAttackStyle() {
		return attackStyle;
	}
	
	enum AttackStyles {
		ACCURATE, AGRESSIVE, CONTROLLED, DEFENSIVE
	}
	
}
