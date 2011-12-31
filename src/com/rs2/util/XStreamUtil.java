package com.rs2.util;

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

import java.io.FileNotFoundException;
import java.io.IOException;

import com.rs2.model.content.combat.magic.SpellLoader;
import com.rs2.model.content.combat.ranged.BowLoader;
import com.rs2.model.content.food.FoodLoader;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.players.ItemManager;
import com.rs2.model.players.ShopManager;
import com.rs2.model.players.GlobalObjectHandler;
import com.thoughtworks.xstream.XStream;

/**
 * Class handling all XStream
 * 
 * @author BFMV
 *
 */
public class XStreamUtil {
	
	private static XStreamUtil instance = new XStreamUtil();
	private static XStream xStream = new XStream();
	
	public static XStreamUtil getInstance() {
		return instance;
	}

	public static XStream getxStream() {
		return xStream;
	}

	static {
		xStream.alias("npc", com.rs2.model.npcs.Npc.class);
		xStream.alias("item", com.rs2.model.players.Item.class);
		xStream.alias("itemDef", com.rs2.model.players.ItemManager.ItemDefinition.class);
		xStream.alias("equip", com.rs2.model.players.Equipment.EquipmentDefinition.class);
		xStream.alias("shop", com.rs2.model.players.ShopManager.Shop.class);
		xStream.alias("npcdef", com.rs2.model.npcs.NpcDefinition.class);
		xStream.alias("object", com.rs2.model.players.GlobalObject.class);
		xStream.alias("spelldef", com.rs2.model.content.combat.magic.SpellDefinition.class);
		xStream.alias("bowdef", com.rs2.model.content.combat.ranged.BowDefinition.class);
		xStream.alias("fooddef", com.rs2.model.content.food.FoodLoader.FoodDefinition.class);
	}
	
	public static void loadAllFiles() throws FileNotFoundException, IOException {
		ItemManager.getInstance().loadItemDefinitions();
		ItemManager.getInstance().loadEquipmentDefinitions();
		NpcLoader.loadDefinitions();
		NpcLoader.loadSpawns();
		ShopManager.loadShops();
		GlobalObjectHandler.loadObjects();
		SpellLoader.loadSpellDefinitions();
		BowLoader.loadBowDefinitions();
		FoodLoader.loadFoodDefinitions();
	}

}
