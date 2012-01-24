package com.rs2.model.content.consumables;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import com.rs2.util.XStreamUtil;

/**
  * By Mikey` of Rune-Server
  */
public class FoodLoader {

	@SuppressWarnings("unchecked")
	public static void loadFoodDefinitions() throws FileNotFoundException, IOException {
		System.out.println("Loading food definitions...");
		List<FoodLoader.FoodDefinition> list = (List<FoodLoader.FoodDefinition>) XStreamUtil.getxStream().fromXML(new FileInputStream("./data/content/fooddef.xml"));
		int count = 0;
		for (FoodLoader.FoodDefinition def : list) {
			Food.getFoodDefinitions()[count] = def;
			Food.foodCount ++;
			count ++;
		}
		System.out.println("Loaded " + list.size() + " food definitions.");
	}
	
	public class FoodDefinition {

		private String foodName;
		private int foodId;
		private int healAmount;
	
		public String getFoodName() {
			return foodName;
		}
	
		public int getFoodId() {
			return foodId;
		}
	
		public int getHealAmount() {
			return healAmount;
		}

	}
}






