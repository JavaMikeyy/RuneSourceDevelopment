package com.rs2.model.content.combat.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import com.rs2.util.XStreamUtil;

public class Bonuses {
	
	private static BonusDefinition[] bonusDefinitions = new BonusDefinition[11791];
	
	@SuppressWarnings("unchecked")
	public static void loadBonusDefinitions() throws FileNotFoundException, IOException {
		System.out.println("Loading item bonuses...");
		List<Bonuses.BonusDefinition> list = (List<Bonuses.BonusDefinition>) XStreamUtil.getxStream().fromXML(new FileInputStream("./data/content/combat/bonuses.xml"));
		for (Bonuses.BonusDefinition def : list) {
			bonusDefinitions[def.getItemId()] = def;
		}
		System.out.println("Loaded " + list.size() + " bonus definitions.");
	}
	
	public static BonusDefinition[] getBonusDefinitions() {
		return bonusDefinitions;
	}
	
	public class BonusDefinition {
		
		private int itemId;
		private int[] bonuses;
		
		public int getItemId() {
			return itemId;
		}
		
		public int getBonus(int i) {
			return bonuses[i];
		}
	
	}
	
}
