package com.rs2.model.content.combat;

import com.rs2.model.players.Player;
import com.rs2.model.players.ItemManager;
import com.rs2.model.content.combat.magic.SpellDefinition;
import com.rs2.model.content.combat.util.*;
import com.rs2.model.npcs.Npc;
import com.rs2.model.Entity;
import com.rs2.model.World;
import com.rs2.model.tick.Tick;
import com.rs2.util.Misc;
import com.rs2.util.clip.PathFinder;

/**
  * By Mikey` of Rune-Server
  * - Credit to Xynth of Rune-Server for the projectiles.
  test
  */
public class Combat {

	private Player player;
	private Npc npc;
	private HitDelay hit;
	
	public Combat(Player player) {
		this.player = player;
	}
	
	public Combat(Npc npc) {
		this.npc = npc;
	}
	
	/**
	  * The combat tick for entities.
	  * Ticks for all players and npcs (entitys).
	  */
	public void combatTick(Entity entity) {
		if (entity instanceof Player)
			player.getMagic().magicTick(entity);
		if (entity.getCombatingEntity() instanceof Npc && entity.getCombatingEntity().isDead())
			entity.setCombatTimer(0);
		if (entity.getAttackTimer() > 0)
			entity.setAttackTimer(entity.getAttackTimer() - 1);
		if (entity.isInstigatingAttack() && !inCombat(entity, entity.getTarget()))
			if (entity.getAttackTimer() == 0)
				attackEntity(entity, entity.getTarget());
		if (entity.getCombatTimer() > 0)
			entity.setCombatTimer(entity.getCombatTimer() - 1);
	}
	
	/**
	  * Handles the entity attacking another entity.
	  */
	public void attackEntity(Entity attacker, Entity victim) {
		if (victim instanceof Player)
			attacker.getUpdateFlags().faceEntity(victim.getIndex() + 32768);
		else
			attacker.getUpdateFlags().faceEntity(victim.getIndex());
		if (!meetsAttackRequirements(attacker, victim)) {
			return;
		}
		if (!SpecialAttack.specialActivated(attacker)) {
			attacker.getUpdateFlags().sendAnimation(attacker.grabAttackAnimation(), 0);
		}
		if (attacker.getAttackType() == Entity.AttackTypes.MAGIC || attacker.getAttackType() == Entity.AttackTypes.RANGED)
			sendProjectile(attacker, victim);
		else if (attacker instanceof Player) {
			if (SpecialAttack.specialActivated(attacker))
				SpecialAttack.performSpecialAttack(attacker, victim);
			else
				hit = new HitDelay(player, attacker, victim, 1, 0);
		}
		else if (attacker instanceof Npc)
			hit = new HitDelay(npc, attacker, victim, 1, 0);
		appendCombatTimers(attacker, victim);
		attacker.setCombatingEntity(victim);
		victim.setCombatingEntity(attacker);
	}
	
	public void completeDelayedHit(Entity attacker, Entity victim) {
		if (sendDefenceAnimation(attacker)) {
			attacker.getCombatingEntity().getUpdateFlags().sendAnimation(attacker.getCombatingEntity().grabDefenceAnimation(), 0);
		}
		autoRetaliate(attacker.getCombatingEntity());
		if (attacker instanceof Player)
			Skulling.skullEntity(attacker, attacker.getCombatingEntity());
		resetAfterAttack(attacker);
	}
	
	/**
	  * Resets anything needed after the end of combat.
	  */
	public void resetCombat(Entity entity) {
		entity.setCombatingEntity(null);
		entity.setInstigatingAttack(false);
		entity.setFollowingEntity(null);
	}
	
	/**
	  * Resets anything needed after the end of the attack.
	  */
	public void resetAfterAttack(Entity attacker) {
		if (attacker instanceof Player) {
			switch (attacker.getAttackType()) {
			case MAGIC:
				player.getMagic().resetMagic(attacker);
				break;
			}
		}
	}
	
	/**
	  * Updates the Entity's combat timers after an attack.
	  */
	public void appendCombatTimers(Entity attacker, Entity victim) {
		attacker.setAttackTimer(attacker.grabHitTimer());
		attacker.setCombatTimer(15);
		victim.setCombatTimer(15);
	}
	
	/**
	 * Checking if the attacker meets the requirements to attack the victim.
	 */
	public boolean meetsAttackRequirements(Entity attacker, Entity victim) {
		if (victim.isDead() || attacker.isDead() || victim == null) {
			attacker.setInstigatingAttack(false);
			attacker.setFollowingEntity(null);
			return false;
		}
		if (attacker instanceof Player) {
			AttackType.determineAttackType(player);
			switch (attacker.getAttackType()) {
				case MELEE:
					if (PathFinder.clipAllowsAttack(attacker.getPosition(), victim.getPosition()))
						return false;
					break;
				case RANGED:
					player.getRanged().checkArrows();
					break;
				case MAGIC:
					if (!player.getMagic().removeRunes(attacker))
						return false;
					player.getMagic().attackInitialized = true;
					break;
				default:
					break;
			}
		}
		else {
			if (PathFinder.clipAllowsAttack(attacker.getPosition(), victim.getPosition()))
				return false;
		}
		if (!withinRange(attacker, victim))
			return false;
		return true;
	}
	
	/** 
	  * Checks to see if both entitys can start fighting eachother.
	  */
	public boolean inCombat(Entity attacker, Entity victim) {
		if (attacker == null || victim == null)
			return false;
		if (attacker.getCombatTimer() != 0 && attacker.getCombatingEntity() != victim &&
		!victim.isInMultiZone() && !attacker.isInMultiZone()) {
			if (attacker instanceof Player) {
				player.getActionSender().sendMessage("You are already in combat.");
				attacker.setFollowingEntity(null);
				player.getMovementHandler().reset();
			}
			attacker.setInstigatingAttack(false);
			return true;
		}
		if (victim.getCombatingEntity() != null && attacker.getCombatingEntity() != victim &&
		!victim.isInMultiZone() && !attacker.isInMultiZone()) {
			if (attacker instanceof Player) {
				attacker.setFollowingEntity(null);
				player.getMovementHandler().reset();
				attacker.setInstigatingAttack(false);
				attacker.setCombatingEntity(null);
				if (victim instanceof Player)
					player.getActionSender().sendMessage("That player is already in combat.");
				else if (victim instanceof Npc)
					player.getActionSender().sendMessage("That npc is already in combat.");
				return true;
			}
			else {
				attacker.setInstigatingAttack(false);
				attacker.setCombatingEntity(null);
				return true;
			}
		}
		return false;
	}
	
	public boolean sendDefenceAnimation(Entity attacker) {
		return
			(attacker.getCombatingEntity().getAttackTimer() == 0 && !attacker.getCombatingEntity().isInstigatingAttack() 
			|| attacker.getCombatingEntity().getAttackTimer() > 0 && attacker.getCombatingEntity().getAttackTimer() < 
			(attacker.getCombatingEntity().grabHitTimer() - 1)
			&& attacker.getCombatingEntity().isInstigatingAttack());
	}
	
	/**
	  * Determines whether the Entity should retaliate automatically.
	  */
	public void autoRetaliate(Entity victim) {
		if (victim.shouldAutoRetaliate()) {
			victim.setTarget(victim.getCombatingEntity());
			victim.setFollowingEntity(victim.getCombatingEntity());
			victim.setInstigatingAttack(true);
		}
	}
		
	public void sendProjectile(Entity attacker, Entity victim) {
		int attackerX = attacker.getPosition().getX(), attackerY = attacker.getPosition().getY();
		int victimX = victim.getPosition().getX(), victimY = victim.getPosition().getY();
		int offsetX = (attackerY - victimY) * -1;
		int offsetY = (attackerX - victimX) * -1;
		int projectileId = 10;
		if (attacker.getAttackType() == Entity.AttackTypes.MAGIC) {
			int startGraphicId = player.getMagic().getSpellDefinitions()[player.getMagic().getMagicIndex()].getGraphicId();
			projectileId = player.getMagic().getSpellDefinitions()[player.getMagic().getMagicIndex()].getProjectileId();
			hit = new HitDelay(player, attacker, victim, ((((calculateProjectileSpeed(attacker, victim) + 3) / 9) / 3)), 
					player.getMagic().getSpellDefinitions()[player.getMagic().getMagicIndex()].getEndGraphicId());
			if (startGraphicId != 0)
				attacker.getUpdateFlags().sendHighGraphic(startGraphicId, 0);
		}
		if (attacker.getAttackType() == Entity.AttackTypes.RANGED) {
			int projectileData[] = player.getRanged().getArrowProjectileData();
			projectileId = projectileData[2];
			attacker.getUpdateFlags().sendHighGraphic(projectileData[1], 0);
			hit = new HitDelay(player, attacker, victim, ((((calculateProjectileSpeed(attacker, victim) + 3) / 9) / 3)), 
					0);
		}
		if (projectileId != 0) {
			World.sendProjectile(attacker.getPosition(), offsetX, offsetY, projectileId, 43, 31, 
			calculateProjectileSpeed(attacker, victim), victim instanceof Npc ? victim.getIndex() + 1 : victim.getIndex() - 1);
		}
	}
	
	public int calculateProjectileSpeed(Entity attacker, Entity victim) {
		int distance = Misc.getDistance(attacker.getPosition(), victim.getPosition());
		int modifier = ((distance > 1 && distance < 5) ? 68 : 63);
		modifier = (distance == 3 ? 70 : modifier);
		modifier = (distance == 2 ? 73 : modifier);
		int speed = (distance * 3) + modifier;
		return speed;
	}
	
	/** 
	  * Checks if the attacker is within distance required for attacking.
	  */
	public boolean withinRange(Entity attacker, Entity victim) {
		int combatDistance = getDistanceForCombatType(attacker);
		if (attacker instanceof Player) {
			if (player.getPrimaryDirection() != -1 && player.getSecondaryDirection() == -1)
				combatDistance += 1;
			if (player.getPrimaryDirection() != -1 && player.getSecondaryDirection() != -1)
				combatDistance += 2;
		}
		if (attacker instanceof Npc && npc.getPrimaryDirection() > -1) {
			combatDistance += 1;
		}
		return Misc.getDistance(attacker.getPosition(), victim.getPosition()) <= combatDistance;
	}
	
	/** 
	  * Getting the distance needed for combat.
	  */
	public int getDistanceForCombatType(Entity entity) {
		int combatDistance = 0;
		switch (entity.getAttackType()) {
			case MELEE:
				combatDistance += 1;
				break;
			case RANGED:
				combatDistance += 8;
				break;
			case MAGIC:
				combatDistance += 9;
				break;
			default:
				combatDistance += 1;
		}
		return combatDistance;
	}
	
}