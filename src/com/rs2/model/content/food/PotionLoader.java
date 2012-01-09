package com.rs2.model.content.food;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import com.rs2.util.XStreamUtil;

/**
  * By Mikey` of Rune-Server
  */
public class PotionLoader {

	@SuppressWarnings("unchecked")
	public static void loadPotionDefinitions() throws FileNotFoundException, IOException {
		System.out.println("Loading potion definitions...");
		List<PotionLoader.PotionDefinition> list = (List<PotionLoader.PotionDefinition>) XStreamUtil.getxStream().fromXML(new FileInputStream("./data/content/potiondef.xml"));
		int count = 0;
		for (PotionLoader.PotionDefinition def : list) {
			Potion.getPotionDefinitions()[count] = def;
			Potion.potionCount ++;
			count ++;
		}
		System.out.println("Loaded " + list.size() + " potion definitions.");
	}
	
	public class PotionDefinition {

		private String potionName;
		private int potionId;
		private int affectedStats[];
		
		public String getPotionName() {
			return potionName;
		}
	
		public int getPotionId() {
			return potionId;
		}
		
		public int[] getAffectedStats() {
			return affectedStats;
		}
	
	}
}






