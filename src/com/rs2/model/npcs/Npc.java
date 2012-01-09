package com.rs2.model.npcs;

import com.rs2.util.Misc;
import com.rs2.util.clip.Region;
import com.rs2.model.Entity;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.Following;
import com.rs2.model.content.combat.Combat;

/**
 * A non-player-character.
 * 
 * @author blakeman8192
 */
public class Npc extends Entity {

	private int npcId;
	private NpcDefinition definition;
	private Position minWalk = new Position(0, 0);
	private Position maxWalk = new Position(0, 0);
	private Position spawnPosition;
	private WalkType walkType = WalkType.STAND;
	private int currentX;
	private int currentY;
	private int hp;
	private int primaryDirection = -1;
	private int transformId;
	private int respawnTimer;
	private int hiddenTimer;
	private boolean isVisible = true;
	private boolean transformUpdate;
	private boolean needsRespawn;
	
	private Combat combat = new Combat(this);
	private Following following = new Following(this);
	
	/**
	 * Creates a new Npc.
	 * 
	 * @param npcId
	 *            the NPC ID
	 */
	public Npc(NpcDefinition definition, int npcId) {
		this.setNpcId(npcId);
		getUpdateFlags().setUpdateRequired(true);
		this.definition = definition;
		hp = definition.getMaxHp();
		hiddenTimer = definition.getHiddenTimer();
		respawnTimer = definition.getRespawnTimer();
		initAttributes();
		setAttackType(AttackTypes.MELEE);
	}
	
	@Override
	public void initAttributes() {
		getAttributes().put("doDamage", Boolean.FALSE);
	}

	@Override
	public void process() {
		getFollowing().followTick(this);
		if (getCombatTimer() == 0)
			sendNpcWalk();
		if (needsRespawn) {
			respawnTimer --;
		}
		if (isDead()) {
			hiddenTimer --;
			if (hiddenTimer == 3 && isVisible) {
				getUpdateFlags().sendAnimation(getDefinition().getDeathAnimation(), 0);
			}
			if (hiddenTimer <= 0 && isVisible) {
				needsRespawn = true;
				isVisible = false;
				getUpdateFlags().setUpdateRequired(true);
				System.out.println("" + definition.getName());
				getCombatingEntity().appendSlayerTask(definition.getName(), definition.getMaxHp());
				combat.resetCombat(this);
			}
			if (respawnTimer <= 0 && !isVisible && needsRespawn) {
				getPosition().setAs(spawnPosition);
				isVisible = true;
				setDead(false);
				getUpdateFlags().sendAnimation(65535, 0);
				respawnTimer = definition.getRespawnTimer();
				hiddenTimer = definition.getHiddenTimer();
				hp = definition.getMaxHp();
				needsRespawn = false;
			}
		}
		getCombat().combatTick(this);
	}
	
	@Override
	public void reset() {
		getUpdateFlags().reset();
		transformUpdate = false;
		setPrimaryDirection(-1);
		getUpdateFlags().faceEntity(-1);
	}
	
	@Override
	public void hit(int damage, int hitType) {
		if (damage > hp) {
			damage = hp;
		}
		hp -= damage;
		if (!getUpdateFlags().isHitUpdate()) {
			getUpdateFlags().setDamage(damage);
			getUpdateFlags().setHitType(hitType);
			getUpdateFlags().setHitUpdate(true);
		} else {
			getUpdateFlags().setDamage2(damage);
			getUpdateFlags().setHitType2(hitType);
			getUpdateFlags().setHitUpdate2(true);
		}
		setDamage(damage);
		setHitType(hitType);
		if (hp <= 0) {
			setDead(true);
			getUpdateFlags().sendAnimation(definition.getDeathAnimation(), 65);
		}
	}
	
	/**
	  * Makes walkable npcs walk, then updates it's position.
	  */
	public void sendNpcWalk() {
		if (walkType == WalkType.WALK && Misc.randomNumber(10) == 0) {
			int yModifier = 0, xModifier = 0, direction = 0;
			int[][] coordinateModifiers =
			{{-1, 1}, {0, 1}, {1, 1}, {-1, 0}, 
			{1, 0}, {-1, -1}, {0, -1}, {1, -1}};
			direction = Misc.randomNumber(8);
			xModifier = coordinateModifiers[direction][0];
			yModifier = coordinateModifiers[direction][1];
			if (minWalk.getX() <= (currentX + xModifier) && minWalk.getY() <= (currentY + yModifier) &&
			maxWalk.getX() >= (currentX + xModifier) && maxWalk.getY() >= (currentY + yModifier)) {
				if (!Region.tileClipped(new Position(currentX, currentY), xModifier, yModifier, 0, false)) {
					primaryDirection = direction;
					appendNpcPosition(xModifier, yModifier);
					getUpdateFlags().faceEntity(65535);
				}
			}
		}
	}
	
	/**
	  * Adds to the NPCs position.
	  */
	public void appendNpcPosition(int xModifier, int yModifier) {
		currentX += xModifier;
		currentY += yModifier;
		getPosition().move(xModifier, yModifier);
	}
	
	public void sendTransform(int transformId) {
		NpcDefinition def = World.getDefinitions()[transformId];
		this.transformId = transformId;
		transformUpdate = true;
		setNpcId(transformId);
		getUpdateFlags().setUpdateRequired(true);
		respawnTimer = def.getRespawnTimer();
		hiddenTimer = def.getHiddenTimer();
		hp = def.getMaxHp();
	}

	/**
	 * Sets the NPC ID.
	 * 
	 * @param npcId
	 *            the npcId
	 */
	public void setNpcId(int npcId) {
		this.npcId = npcId;
	}

	/**
	 * Gets the NPC ID.
	 * 
	 * @return the npcId
	 */
	public int getNpcId() {
		return npcId;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setMinWalk(Position minWalk) {
		this.minWalk = minWalk;
	}

	public Position getMinWalk() {
		return minWalk;
	}

	public void setMaxWalk(Position maxWalk) {
		this.maxWalk = maxWalk;
	}

	public Position getMaxWalk() {
		return maxWalk;
	}
	
	public void setWalkType(WalkType walkType) {
		this.walkType = walkType;
	}

	public WalkType getWalkType() {
		return walkType;
	}

	public void setPrimaryDirection(int primaryDirection) {
		this.primaryDirection = primaryDirection;
	}

	public int getPrimaryDirection() {
		return primaryDirection;
	}

	public void setDefinition(NpcDefinition definition) {
		this.definition = definition;
	}

	public NpcDefinition getDefinition() {
		return definition;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getHp() {
		return hp;
	}

	public void setTransformUpdate(boolean transformUpdate) {
		this.transformUpdate = transformUpdate;
	}

	public boolean isTransformUpdate() {
		return transformUpdate;
	}

	public void setTransformId(int transformId) {
		this.transformId = transformId;
	}

	public int getTransformId() {
		return transformId;
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

	public void setNeedsRespawn(boolean needsRespawn) {
		this.needsRespawn = needsRespawn;
	}

	public boolean isNeedsRespawn() {
		return needsRespawn;
	}

	public void setSpawnPosition(Position spawnPosition) {
		this.spawnPosition = spawnPosition;
	}

	public Position getSpawnPosition() {
		return spawnPosition;
	}

	public void setCurrentX(int currentX) {
		this.currentX = currentX;
	}

	public int getCurrentX() {
		return currentX;
	}
	
	public void setCurrentY(int currentY) {
		this.currentY = currentY;
	}

	public int getCurrentY() {
		return currentY;
	}
	
	public Combat getCombat() {
		return combat;
	}
	
	public Following getFollowing() {
		return following;
	}
	
	public enum WalkType {
		STAND, WALK
	}
	
}
