package com.rs2.model.npcs;

import com.rs2.model.Entity.AttackTypes;

public class NpcDefinition {
	
	private int id;
	private String name;
	private int maxHp;
	private int[] combatLevels;
	private int attackAnim;
	private int defenceAnim;
	private boolean isAttackable;
	private AttackStyles attackStyles;
	private AttackTypes attackType = AttackTypes.MELEE;
	private int deathAnimation;
	private int respawnTimer;
	private int hiddenTimer;
	private int hitTimer;
	private int attackSpeed;
	private int size;
	private int maxHit;

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setCombatLevel(int combatId, int combatLevel) {
		this.combatLevels[combatId] = combatLevel;
	}

	public int getCombatLevel(int combatId) {
		return combatLevels[combatId];
	}
	
	public void setMaxHp(int maxHp) {
		this.maxHp = maxHp;
	}

	public int getMaxHp() {
		return maxHp;
	}

	public void setAttackAnim(int attackAnim) {
		this.attackAnim = attackAnim;
	}

	public int getAttackAnim() {
		return attackAnim;
	}

	public void setDefenceAnim(int defenceAnim) {
		this.defenceAnim = defenceAnim;
	}

	public int getDefenceAnim() {
		return defenceAnim;
	}

	public void setAttackable(boolean isAttackable) {
		this.isAttackable = isAttackable;
	}

	public boolean isAttackable() {
		return isAttackable;
	}

	public void setAttackStyles(AttackStyles attackStyles) {
		this.attackStyles = attackStyles;
	}

	public AttackStyles getAttackStyles() {
		return attackStyles;
	}

	public void setDeathAnimation(int deathAnimation) {
		this.deathAnimation = deathAnimation;
	}

	public int getDeathAnimation() {
		return deathAnimation;
	}

	public void setRespawnTimer(int respawnTimer) {
		this.respawnTimer = respawnTimer;
	}

	public int getRespawnTimer() {
		return respawnTimer;
	}

	public void setHiddenTimer(int hiddenTimer) {
		this.hiddenTimer = hiddenTimer;
	}

	public int getHiddenTimer() {
		return hiddenTimer;
	}

	public void setHitTimer(int hitTimer) {
		this.hitTimer = hitTimer;
	}

	public int getHitTimer() {
		return hitTimer;
	}

	public void setAttackSpeed(int attackSpeed) {
		this.attackSpeed = attackSpeed;
	}

	public int getAttackSpeed() {
		return attackSpeed;
	}

	public void setAttackType(AttackTypes attackType) {
		this.attackType = attackType;
	}

	public AttackTypes getAttackType() {
		return attackType;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getSize() {
		return size;
	}

	public void setMaxHit(int maxHit) {
		this.maxHit = maxHit;
	}

	public int getMaxHit() {
		return maxHit;
	}

	public class AttackStyles {

		private int firstAttack;
		private int secondAttack;
		private int thirdAttack;

		public int get(int style) {
			switch(style) {
			case 1:
				return firstAttack;
			case 2:
				return secondAttack;
			case 3:
				return thirdAttack;
			}
			return firstAttack;
		}
	}

}
