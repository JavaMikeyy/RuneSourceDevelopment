package com.rs2.model;

import java.util.HashMap;
import java.util.Map;

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
	private int frozenTimer;
	private int damage;
	private int hitType;
	private int hitDelayTimer = -1;
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
				return player.getRanged().getBowDefinitions()[player.getRanged().getBowIndex()].getAttackSpeed();
			}
			else if (player.getAttackType() == AttackTypes.MELEE) {
				return (weapon == null ? 5 : weapon.getEquipmentDefintion().getHitTimer());
			}
		} else if (this instanceof Npc) {
			Npc npc = (Npc) this;
			return (npc.getDefinition().getHitTimer() > 0 ? npc.getDefinition().getHitTimer() : 5);
		}
		return 0;
	}
	
	public int grabAttackAnimation() {
		int anim = 0;
		if (this instanceof Player) {
			Player player = (Player) this;
			if (player.getAttackType() == AttackTypes.MAGIC) {
				return player.getMagic().getSpellDefinitions()[player.getMagic().getMagicIndex()].getAnimationId();
			}
			if (player.getAttackType() == AttackTypes.RANGED) {
				return player.getRanged().getBowDefinitions()[player.getRanged().getBowIndex()].getAnimationId();
			}
			else {
				Item weapon = player.getEquipment().getItemContainer().get(3);
				return (weapon == null ? 422 : weapon.getEquipmentDefintion().getAttackStyles().get(0));
			}
		} else if (this instanceof Npc) {
			Npc npc = (Npc) this;
			return (npc.getDefinition().getAttackAnim());
		}
		return 0;
	}
	
	public int grabDefenceAnimation() {
		int anim = 0;
		if (this instanceof Player) {
			Player player = (Player) this;
			Item shield = player.getEquipment().getItemContainer().get(5);
			anim = shield == null ? 404 : shield.getEquipmentDefintion().getDefenseAnim();
		} else if (this instanceof Npc) {
			Npc npc = (Npc) this;
			anim = npc.getDefinition().getDefenceAnim();
		}
		return anim;
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
	
	public void setDamage(int damage) {
		this.damage = damage;
	}
	
	public int getDamage() {
		return damage;
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
	
	public void setHitDelayTimer(int hitDelayTimer) {
		this.hitDelayTimer = hitDelayTimer;
	}
	
	public int getHitDelayTimer() {
		return hitDelayTimer;
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
	
	public void setFrozen(boolean isFrozen, int frozenTimer) {
		this.isFrozen = isFrozen;
		this.frozenTimer = frozenTimer;
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
	
	public void setAttackType(AttackTypes attackType) {
		this.attackType = attackType;
	}
	public AttackTypes getAttackType() {
		return attackType;
	}
	
	public boolean isInMultiZone() {
		return false;
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
