package com.rs2.model.content.combat.ranged;

/**
  * By Mikey` of Rune-Server
  */
public class BowDefinition {

	private String bowName;
	private int bowId;
	private int attackSpeed;
	private int highestArrowId;
	private int animationId;
	
	public String getBowName() {
		return bowName;
	}
	
	public int getBowId() {
		return bowId;
	}
	
	public int getAttackSpeed() {
		return attackSpeed;
	}
	
	public int getAnimationId() {
		return animationId;
	}
	
	public int getHighestArrowId() {
		return highestArrowId;
	}
	
	public boolean arrowAllowed(int arrowId) {
		if (arrowId > highestArrowId)
			return false;
		return true;
	}
	
}






