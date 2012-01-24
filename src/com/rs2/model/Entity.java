package com.rs2.model;

import java.util.HashMap;
import java.util.Map;

import com.rs2.model.content.combat.util.SpecialAttack;
import com.rs2.model.content.combat.util.WeaponData;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Item;
import com.rs2.model.players.Player;
import com.rs2.net.StreamBuffer;

public abstract class Entity {
	
	private int index;
	
	private Entity interactingEntity;
	private Entity combatingEntity;
	private Entity followingEntity;
	private Entity[] engagedEntity = new Entity[30];
	private Entity target;
	
	private boolean isDead;
	private boolean instigatingAttack;
	private boolean isFrozen;
	private boolean isPoisoned;
	
	private int frozenTimer;
	private int freezeImmunityTimer;
	private int poisonDamage;
	private int poisonedTimer;
	private int poisonHitTimer;
	private int poisonImmunityTimer;
	private int fireImmunityTimer;
	private int hitType;
	private int combatTimer;
	private int attackTimer;
	
	private Map<String, Object> attributes = new HashMap<String, Object>();
	
	private Position position;
	private UpdateFlags updateFlags = new UpdateFlags();
	private AttackTypes attackType = AttackTypes.MELEE;
	
	public abstract void reset();
	public abstract void initAttributes();
	public abstract void process();
	public abstract void hit(int damage, int hitType);
	
	public int grabHitTimer() {
		int hitTimer = 0;
		if (this instanceof Player) {
			Player player = (Player) this;
			Item weapon = player.getEquipment().getItemContainer().get(3);
			if (player.getAttackType() == AttackTypes.MAGIC) {
				return 5;
			}
			else if (player.getAttackType() == AttackTypes.RANGED) {
				return WeaponData.getWeaponSpeed(player);
				//return player.getRanged().getBowDefinitions()[player.getRanged().getBowIndex()].getAttackSpeed();
			}
			else if (player.getAttackType() == AttackTypes.MELEE) {
				return WeaponData.getWeaponSpeed(player);
				//return (weapon == null ? 5 : weapon.getEquipmentDefintion().getHitTimer());
			}
		} else if (this instanceof Npc) {
			Npc npc = (Npc) this;
			return (npc.getDefinition().getHitTimer() > 0 ? npc.getDefinition().getHitTimer() : 5);
		}
		return 5;
	}
	
	public int grabAttackAnimation() {
		int anim = 0;
		if (this instanceof Player) {
			Player player = (Player) this;
			if (player.getAttackType() == AttackTypes.MAGIC) {
				return player.getMagic().getSpellDefinitions()[player.getMagic().getMagicIndex()].getAnimationId();
			}
			if (player.getAttackType() == AttackTypes.RANGED) {
				int animation = player.getRanged().getBowDefinitions()[player.getRanged().getBowIndex()].getAnimationId();
				if (player.isSpecialAttackActive())
					animation = SpecialAttack.getSpecialAttackAnimation(player, animation);
				return animation;
			}
			else {
				//Item weapon = player.getEquipment().getItemContainer().get(3);
				//int animation = (weapon == null ? 422 : weapon.getEquipmentDefintion().getAttackStyles().get(0));
				int animation = WeaponData.getAttackAnimation(player);
				if (player.isSpecialAttackActive())
					animation = SpecialAttack.getSpecialAttackAnimation(player, animation);
				return animation;
			}
		} else if (this instanceof Npc) {
			Npc npc = (Npc) this;
			return (npc.getDefinition().getAttackAnim());
		}
		return 0;
	}
	
	public int grabDefenceAnimation() {
		if (this instanceof Player) {
			Player player = (Player) this;
			return WeaponData.getBlockAnimation(player);
			//Item shield = player.getEquipment().getItemContainer().get(5);
			//anim = shield == null ? 404 : shield.getEquipmentDefintion().getDefenseAnim();
		} else if (this instanceof Npc) {
			Npc npc = (Npc) this;
			return npc.getDefinition().getDefenceAnim();
		}
		return 404;
	}
	
	public boolean shouldAutoRetaliate() {
		if (this instanceof Player) {
			Player player = (Player) this;
			return player.shouldAutoRetaliate() && player.getPrimaryDirection() == -1;
		}
		else {
			return true;
		}
	}
	
	public int getSkillLevel(int skillId) {
		if (this instanceof Player) {
			Player player = (Player) this;
			return player.getSkill().getLevel()[skillId];
		}
		else {
			Npc npc = (Npc) this;
			return npc.getDefinition().getCombatLevel(skillId);
		}
	}
	
	public int getBonus(int bonusId) {
		if (this instanceof Player) {
			Player player = (Player) this;
			return player.getBonuses().get(bonusId);
		}
		else {
			return 0;
		}
	}
	
	/**
	  * Only use through the attacker (attacker.applyPrayerEffects()).
	  */
	public void applyPrayerEffects(Entity victim, int hit) {
		if (victim instanceof Player) {
			Player player = (Player) this;
			Player otherPlayer = (Player) victim;
			player.getPrayer().applySmiteEffect(otherPlayer, hit);
		}
	}
	
	public int applyPrayerToHit(Entity attacker, Entity victim, int hit) {
		if (victim instanceof Player) {
			Player player = (Player) victim;
			return player.getPrayer().prayerHitModifiers(attacker, victim, hit);
		}
		return hit;
	}
	
	public int getNpcMaxHit() {
		if (this instanceof Npc) {
			Npc npc = (Npc) this;
			return npc.getDefinition().getMaxHit();
		}
		return 0;
	}
	
	public int getMagicMaxHit() {
		if (this instanceof Player) {
			Player player = (Player) this;
			return player.getMagic().getSpellDefinitions()[player.getMagic().getMagicIndex()].getMaxHit();
		}
		return 0;
	}
	
	public void appendSlayerTask(String npcName, int npcHp) {
		if (this instanceof Player) {
			Player player = (Player) this;
			if (npcName.equalsIgnoreCase((String) player.getSlayerTask()[0])) {
				player.killedSlayerNpc();
				player.getSkill().addExp(18, (npcHp / 10) == 0 ? 1 : (npcHp / 10));
			}
		}
	}
	
	public void setIndex(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	public void setInteractingEntity(Entity interactingEntity) {
		this.interactingEntity = interactingEntity;
	}

	public Entity getInteractingEntity() {
		return interactingEntity;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public Position getPosition() {
		return position;
	}

	public void setUpdateFlags(UpdateFlags updateFlags) {
		this.updateFlags = updateFlags;
	}

	public UpdateFlags getUpdateFlags() {
		return updateFlags;
	}

	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}

	public boolean isDead() {
		return isDead;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}
	
	public void setHitType(int hitType) {
		this.hitType = hitType;
	}
	
	public int getHitType() {
		return hitType;
	}
	
	public void setCombatTimer(int combatTimer) {
		this.combatTimer = combatTimer;
	}
	
	public int getCombatTimer() {
		return combatTimer;
	}
	
	public void setAttackTimer(int attackTimer) {
		this.attackTimer = attackTimer;
	}
	
	public int getAttackTimer() {
		return attackTimer;
	}
	
	public boolean isInstigatingAttack() {
		return instigatingAttack;
	}
	
	public void setInstigatingAttack(boolean instigatingAttack) {
		this.instigatingAttack = instigatingAttack;
	}
	
	public void setCombatingEntity(Entity combatingEntity) {
		this.combatingEntity = combatingEntity;
	}

	public Entity getCombatingEntity() {
		return combatingEntity;
	}
	
	public void setTarget(Entity target) {
		this.target = target;
	}

	public Entity getTarget() {
		return target;
	}
	
	public void setFrozen(boolean isFrozen) {
		this.isFrozen = isFrozen;
	}

	public boolean isFrozen() {
		return isFrozen;
	}
	
	public void setFrozenTimer(int frozenTimer) {
		this.frozenTimer = frozenTimer;
	}

	public int getFrozenTimer() {
		return frozenTimer;
	}
	
	public void setFreezeImmunityTimer(int freezeImmunityTimer) {
		this.freezeImmunityTimer = freezeImmunityTimer;
	}

	public int getFreezeImmunityTimer() {
		return freezeImmunityTimer;
	}
	
	public void setPoisoned(boolean isPoisoned) {
		this.isPoisoned = isPoisoned;
	}

	public boolean isPoisoned() {
		return isPoisoned;
	}
	
	public void setPoisonedTimer(int poisonedTimer) {
		this.poisonedTimer = poisonedTimer;
	}

	public int getPoisonedTimer() {
		return poisonedTimer;
	}
	
	public void setPoisonHitTimer(int poisonHitTimer) {
		this.poisonHitTimer = poisonHitTimer;
	}

	public int getPoisonHitTimer() {
		return poisonHitTimer;
	}
	
	public void setPoisonDamage(int poisonDamage) {
		this.poisonDamage = poisonDamage;
	}

	public int getPoisonDamage() {
		return poisonDamage;
	}
	
	public void setPoisonImmunityTimer(int poisonImmunityTimer) {
		this.poisonImmunityTimer = poisonImmunityTimer;
	}

	public int getPoisonImmunityTimer() {
		return poisonImmunityTimer;
	}
	
	public void setFireImmunityTimer(int fireImmunityTimer) {
		this.fireImmunityTimer = fireImmunityTimer;
	}

	public int getFireImmunityTimer() {
		return fireImmunityTimer;
	}
	
	public void setAttackType(AttackTypes attackType) {
		this.attackType = attackType;
	}
	public AttackTypes getAttackType() {
		return attackType;
	}
	
	public boolean isInMultiZone() {
		return true;
	}
	
	public void setFollowingEntity(Entity followingEntity) {
		this.followingEntity = followingEntity;
	}

	public Entity getFollowingEntity() {
		return followingEntity;
	}
	
	public void setEngagedEntity(int i, Entity engagedEntity) {
		this.engagedEntity[i] = engagedEntity;
	}

	public Entity getEngagedEntity(int i) {
		return engagedEntity[i];
	}
	
	public enum AttackTypes {
		MELEE, RANGED, MAGIC
	}

}
