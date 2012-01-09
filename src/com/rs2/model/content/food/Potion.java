package com.rs2.model.content.food;

import com.rs2.model.World;
import com.rs2.model.tick.Tick;
import com.rs2.model.players.Player;
import com.rs2.model.players.Item;

/**
  * By Mikey` of Rune-Server
  */
public class Potion {

	Player player;
	
	public Potion(Player player) {
		this.player = player;
	}
	
	/**
	  * All the potion definitions.
	  */
	private static PotionLoader.PotionDefinition[] potionDefinitions = new PotionLoader.PotionDefinition[50];

	/**
	  * Potion definition count.
	  */
	public static int potionCount = 0;
	
	private boolean canDrink = true;
	public int potionIndex = 0;
	
	public boolean isPotion(int itemId) {
		for (int i = 0; i < potionCount; i++)
			if (potionDefinitions[i].getPotionId() == itemId) {
				potionIndex = i;
				return true;
			}
		return false;
	}
	
	public void drinkPotion(int itemId, int slot) {
		if (isPotion(itemId) && canDrink) {
			for (int i = 0; i < potionDefinitions[potionIndex].getAffectedStats().length; i++) {
				
			}
			tickAfterDrinking();
		}
	}
	
	private void tickAfterDrinking() {
		World.submit(new Tick(3) {
			@Override
			public void execute() {
				canDrink = true;
				stop();
			}
		});
	}
	
	public static PotionLoader.PotionDefinition[] getPotionDefinitions() {
		return potionDefinitions;
	}

}