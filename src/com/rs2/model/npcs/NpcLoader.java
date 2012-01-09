package com.rs2.model.npcs;

/*
 * This file is part of RuneSource.
 *
 * RuneSource is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RuneSource is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RuneSource.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.util.XStreamUtil;
import com.rs2.Constants;

/**
 * Having anything to do with any type of npc data loading.
 * @author BFMV
 */
public class NpcLoader {
	
	@SuppressWarnings("unchecked")
	public static void loadSpawns() throws FileNotFoundException {
		System.out.println("Loading npc spawns...");
		List<Npc> list = (List<Npc>) XStreamUtil.getxStream().fromXML(new FileInputStream("./data/npcs/npcspawns.xml"));
		for(Npc spawn : list) {
			NpcDefinition def = World.getDefinitions()[spawn.getNpcId()];
			Npc npc = new Npc(def, spawn.getNpcId());
			npc.setPosition(spawn.getPosition());
			npc.setSpawnPosition(spawn.getPosition());
			npc.setMinWalk(new Position(spawn.getPosition().getX() - Constants.NPC_WALK_DISTANCE, spawn.getPosition().getY() - 
					Constants.NPC_WALK_DISTANCE));
			npc.setMaxWalk(new Position(spawn.getPosition().getX() + Constants.NPC_WALK_DISTANCE, spawn.getPosition().getY() + 
					Constants.NPC_WALK_DISTANCE));
			npc.setWalkType(spawn.getWalkType());
			npc.setCurrentX(spawn.getPosition().getX());
			npc.setCurrentY(spawn.getPosition().getY());
			World.register(npc);
		}
		System.out.println("Loaded " + list.size() + " npc spawns");
	}
	
	@SuppressWarnings("unchecked")
	public static void loadDefinitions() throws FileNotFoundException {
		System.out.println("Loading npc definitions...");
		List<NpcDefinition> list = (List<NpcDefinition>) XStreamUtil.getxStream().fromXML(new FileInputStream("./data/npcs/npcdef.xml"));
		for(NpcDefinition def : list) {
			World.getDefinitions()[def.getId()] = def;
		}
		System.out.println("Loaded " + list.size() + " npc definitions.");
	}

}
