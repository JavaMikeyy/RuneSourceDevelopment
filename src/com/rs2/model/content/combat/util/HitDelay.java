package com.rs2.model.content.combat.util;

import com.rs2.model.World;
import com.rs2.model.Entity;
import com.rs2.model.tick.Tick;
import com.rs2.model.players.Player;
import com.rs2.model.npcs.Npc;

public class HitDelay {
	
	private int hit, delay;
	
	public HitDelay(Player player, Entity attacker, Entity victim, int hit, int delay) {
		this.hit = hit;
		this.delay = delay;
		tickHitDelay(player, attacker, victim);
	}
	
	public HitDelay(Npc npc, Entity attacker, Entity victim, int hit, int delay) {
		this.hit = hit;
		this.delay = delay;
		tickHitDelay(npc, attacker, victim);
	}
	
	public void tickHitDelay(final Player player, final Entity attacker, final Entity victim) {
		World.submit(new Tick(delay) {
			@Override
			public void execute() {
				victim.hit(hit, 1);
				player.getCombat().completeDelayedHit(attacker, victim);
				stop();
			}
		});
	}
	
	public void tickHitDelay(final Npc npc, final Entity attacker, final Entity victim) {
		World.submit(new Tick(delay) {
			@Override
			public void execute() {
				victim.hit(hit, 1);
				npc.getCombat().completeDelayedHit(attacker, victim);
				stop();
			}
		});
	}
	
}
