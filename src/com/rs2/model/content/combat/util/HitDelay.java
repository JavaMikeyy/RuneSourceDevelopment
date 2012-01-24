package com.rs2.model.content.combat.util;

import com.rs2.model.World;
import com.rs2.model.Entity;
import com.rs2.model.tick.Tick;
import com.rs2.model.players.Player;
import com.rs2.model.npcs.Npc;
import com.rs2.model.content.combat.magic.SpellDefinition;

public class HitDelay {
	
	private int delay, endGraphic;
	
	public HitDelay(Player player, Entity attacker, Entity victim, int delay, int endGraphic) {
		this.delay = delay;
		this.endGraphic = endGraphic;
		tickHitDelay(player, attacker, victim);
	}
	
	public HitDelay(Npc npc, Entity attacker, Entity victim, int delay, int endGraphic) {
		this.delay = delay;
		this.endGraphic = endGraphic;
		tickHitDelay(npc, attacker, victim);
	}
	
	public void tickHitDelay(final Player player, final Entity attacker, final Entity victim) {
		World.submit(new Tick(delay) {
			@Override
			public void execute() {
				int hit = DetermineHit.determineHit(attacker, victim);
				if (hit == 0 && attacker.getAttackType() == Entity.AttackTypes.MAGIC) {
					victim.getUpdateFlags().sendHighGraphic(629, 0);
					player.getCombat().completeDelayedHit(attacker, victim);
					stop();
					return;
				}
				else {
					hit = attacker.applyPrayerToHit(attacker, victim, hit);
					victim.hit(hit, hit == 0 ? 0 : 1);
					attacker.applyPrayerEffects(victim, hit);
					Poison.applyPoisonFromWeapons(attacker, victim);
				}
				if (attacker.getAttackType() == Entity.AttackTypes.MAGIC) {
					MagicSpellEffects.applyMagicEffects(attacker, attacker.getCombatingEntity(), hit);
				}
				if (endGraphic != 0) {
					if (attacker.getAttackType() == Entity.AttackTypes.MAGIC) {
						if (player.getMagic().getSpellDefinitions()[player.getMagic().getMagicIndex()].getMagicType() == 
								SpellDefinition.MagicTypes.ANCIENT) {
							attacker.getCombatingEntity().getUpdateFlags().sendGraphic(endGraphic, 0);
						}
						else if (player.getMagic().getSpellDefinitions()[player.getMagic().getMagicIndex()].getMagicType() == 
								SpellDefinition.MagicTypes.MODERN) {
							attacker.getCombatingEntity().getUpdateFlags().sendHighGraphic(endGraphic, 0);
						}
					}
				}
				stop();
				player.getCombat().completeDelayedHit(attacker, victim);
			}
		});
	}
	
	public void tickHitDelay(final Npc npc, final Entity attacker, final Entity victim) {
		World.submit(new Tick(delay) {
			@Override
			public void execute() {
				int hit = DetermineHit.determineHit(attacker, victim);
				hit = attacker.applyPrayerToHit(attacker, victim, hit);
				victim.hit(hit, hit == 0 ? 0 : 1);
				Poison.applyPoisonFromWeapons(attacker, victim);
				npc.getCombat().completeDelayedHit(attacker, victim);
				if (endGraphic > 0)
					attacker.getCombatingEntity().getUpdateFlags().sendGraphic(endGraphic, 0);
				stop();
			}
		});
	}
	
}
