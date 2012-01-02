package com.rs2.model.content.combat.util;

import com.rs2.model.World;
import com.rs2.model.Entity;
import com.rs2.model.tick.Tick;
import com.rs2.model.players.Player;
import com.rs2.model.npcs.Npc;
import com.rs2.model.content.combat.magic.SpellDefinition;

public class HitDelay {
	
	private int hit, delay, endGraphic;
	
	public HitDelay(Player player, Entity attacker, Entity victim, int hit, int delay, int endGraphic) {
		this.hit = hit;
		this.delay = delay;
		this.endGraphic = endGraphic;
		tickHitDelay(player, attacker, victim);
	}
	
	public HitDelay(Npc npc, Entity attacker, Entity victim, int hit, int delay, int endGraphic) {
		this.hit = hit;
		this.delay = delay;
		this.endGraphic = endGraphic;
		tickHitDelay(npc, attacker, victim);
	}
	
	public void tickHitDelay(final Player player, final Entity attacker, final Entity victim) {
		World.submit(new Tick(delay) {
			@Override
			public void execute() {
				int hit = DetermineHit.determineHit(attacker, victim);
				victim.hit(hit, hit == 0 ? 0 : 1);
				player.getCombat().completeDelayedHit(attacker, victim);
				if (endGraphic != 0) {
					if (attacker.getAttackType() == Entity.AttackTypes.MAGIC) {
						if (player.getMagic().getSpellDefinitions()[player.getMagic().getMagicIndex()].getMagicType() == 
								SpellDefinition.MagicTypes.ANCIENT) {
							attacker.getCombatingEntity().getUpdateFlags().sendGraphic(endGraphic, 0);
						}
						else {
							attacker.getCombatingEntity().getUpdateFlags().sendHighGraphic(endGraphic, 0);
						}
					}
				}
				stop();
			}
		});
	}
	
	public void tickHitDelay(final Npc npc, final Entity attacker, final Entity victim) {
		World.submit(new Tick(delay) {
			@Override
			public void execute() {
				int hit = DetermineHit.determineHit(attacker, victim);
				victim.hit(hit, hit == 0 ? 0 : 1);
				npc.getCombat().completeDelayedHit(attacker, victim);
				if (endGraphic > 0)
					attacker.getCombatingEntity().getUpdateFlags().sendGraphic(endGraphic, 0);
				stop();
			}
		});
	}
	
}
