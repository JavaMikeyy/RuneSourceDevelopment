package com.rs2.model.content.food;

import com.rs2.model.World;
import com.rs2.model.tick.Tick;
import com.rs2.model.players.Player;
import com.rs2.model.players.Item;

/**
  * By Mikey` of Rune-Server
  */
public class Food {

	Player player;
	
	public Food(Player player) {
		this.player = player;
	}
	
	/**
	  * All the food definitions.
	  */
	private static FoodLoader.FoodDefinition[] foodDefinitions = new FoodLoader.FoodDefinition[50];

	/**
	  * Food definition count.
	  */
	public static int foodCount = 0;
	private boolean canEat = true;
	public int foodIndex = 0;
	
	public boolean isFood(int itemId) {
		for (int i = 0; i < foodCount; i++)
			if (foodDefinitions[i].getFoodId() == itemId) {
				foodIndex = i;
				return true;
			}
		return false;
	}
	
	public void eatFood(int itemId, int slot) {
		if (isFood(itemId) && canEat) {
			canEat = false;
			String foodName = foodDefinitions[foodIndex].getFoodName();
			int healAmount = foodDefinitions[foodIndex].getHealAmount();
			int hitPoints = player.getSkill().HITPOINTS;
			player.getInventory().removeItemSlot(new Item(itemId, 1), slot);
			player.getUpdateFlags().sendAnimation(829, 0);
			player.getActionSender().sendMessage("You eat the " + foodName + ".");
			if (player.getSkill().getLevel()[hitPoints] + healAmount <= player.getSkill().getLevelForXP(player.getSkill().getExp()[hitPoints])) {
				player.getSkill().getLevel()[hitPoints] += healAmount;
				player.getSkill().refresh(hitPoints);
			}
			player.setAttackTimer(player.getAttackTimer() + 3);
			player.setInstigatingAttack(false);
			player.setFollowingEntity(null);
			tickAfterEating();
		}
	}
	
	private void tickAfterEating() {
		World.submit(new Tick(3) {
			@Override
			public void execute() {
				canEat = true;
				stop();
			}
		});
	}
	
	public static FoodLoader.FoodDefinition[] getFoodDefinitions() {
		return foodDefinitions;
	}

}