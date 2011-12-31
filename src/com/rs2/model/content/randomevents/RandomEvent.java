package com.rs2.model.content.randomevents;

import com.rs2.model.World;
import com.rs2.model.Position;
import com.rs2.model.tick.Tick;
import com.rs2.model.players.Player;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcDefinition;

/**
  * By Mikey` of Rune-Server
  */
public class RandomEvent {
	
	Npc npc;
	
	public RandomEvent(Player player, int npcId) {
		NpcDefinition def = World.getDefinitions()[npcId];
		final Npc npc = new Npc(def, npcId);
		npc.setPosition(new Position(player.getPosition().getX() - 1, player.getPosition().getY()));
		npc.setSpawnPosition(new Position(player.getPosition().getX() - 1, player.getPosition().getY()));
		npc.setMinWalk(new Position(0, 0));
		npc.setMaxWalk(new Position(0, 0));
		npc.setCurrentX(player.getPosition().getX() - 1);
		npc.setCurrentY(player.getPosition().getX() - 1);
		World.register(npc);
		this.npc = npc;
	}
	
}












