package com.rs2.model.content.combat.magic;

/**
  * By Mikey` of Rune-Server
  */
public class SpellDefinition {

	private String spellName;
	private int spellId;
	private MagicTypes magicType;
	private int autoCastButton;
	private int magicLevelRequired;
	private double baseExperience;
	private int maxHit;
	private int animationId;
	private int graphicId;
	private int projectileId;
	private int endGraphicId;
	private int runeRequired1;
	private int runeAmount1;
	private int runeRequired2;
	private int runeAmount2;
	private int runeRequired3;
	private int runeAmount3;
	private int runeRequired4;
	private int runeAmount4;
	
	public String getSpellName() {
		return spellName;
	}
	
	public int getSpellId() {
		return spellId;
	}
	
	public int getAutoCastButton() {
		return autoCastButton;
	}
	
	public int getRequiredLevel() {
		return magicLevelRequired;
	}
	
	public double getBaseExperience() {
		return baseExperience;
	}
	
	public int getMaxHit() {
		return maxHit;
	}
	
	public int getAnimationId() {
		return animationId;
	}
	
	public int getGraphicId() {
		return graphicId;
	}
	
	public int getProjectileId() {
		return projectileId;
	}
	
	public int getEndGraphicId() {
		return endGraphicId;
	}
	
	public MagicTypes getMagicType() {
		return magicType;
	}
	
	public void setMagicType(MagicTypes magicType) {
		this.magicType = magicType;
	}
	
	public enum MagicTypes {
		MODERN, ANCIENT, NONE
	}
	
	public int[] getRunesRequired() {
		int[] runesRequired = {
			runeRequired1, runeAmount1, runeRequired2, runeAmount2, runeRequired3, runeAmount3, runeRequired4, runeAmount4
		};
		return runesRequired;
	}
	
}






