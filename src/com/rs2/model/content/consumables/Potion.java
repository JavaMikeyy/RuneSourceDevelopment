package com.rs2.model.content.consumables;

import com.rs2.model.World;
import com.rs2.model.tick.Tick;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.combat.util.Poison;
import com.rs2.model.players.Player;
import com.rs2.model.players.Item;
import com.rs2.model.players.ItemManager;

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
	public int potionIndex = 0, potionIdIndex = 0;
	private static final int EMPTY_VIAL = 229;
	
	public boolean isPotion(int itemId) {
		for (int i = 0; i < potionCount; i++)
			for (int i2 = 0; i2 < 4; i2++) {
				if (potionDefinitions[i].getPotionIds()[i2] == itemId) {
					potionIndex = i;
					potionIdIndex = i2;
					return true;
				}
			}
		return false;
	}
	
	public void drinkPotion(int itemId, int slot) {
		if (isPotion(itemId) && canDrink && !player.isDead()) {
			canDrink = false;
			int[] potionIds = potionDefinitions[potionIndex].getPotionIds();
			int[] affectedStats = potionDefinitions[potionIndex].getAffectedStats();
			int[] statAddons = potionDefinitions[potionIndex].getStatAddons();
			double[] statModifiers = potionDefinitions[potionIndex].getStatModifiers();
			for (int i = 0; i < affectedStats.length; i++) {
				if (potionDefinitions[potionIndex].getPotionType() == 
						PotionLoader.PotionDefinition.PotionTypes.BOOST) {
					int index = affectedStats[i];
					int currentLevel = player.getSkill().getLevel()[index];
					int actualLevel = player.getSkill().getLevelForXP(player.getSkill().getExp()[index]);
					int levelAfterDrink = actualLevel;
					levelAfterDrink += actualLevel * statModifiers[i];
					levelAfterDrink += statAddons[i];
					if (currentLevel < actualLevel) {
						player.getSkill().getLevel()[index] += levelAfterDrink - actualLevel;
						player.getSkill().refresh(index);
					}
					else if (currentLevel < levelAfterDrink) {
						player.getSkill().getLevel()[index] = levelAfterDrink;
						player.getSkill().refresh(index);
					}
				}
				else if (potionDefinitions[potionIndex].getPotionType() == 
						PotionLoader.PotionDefinition.PotionTypes.RESTORE) {
					int index = affectedStats[i];
					int currentLevel = player.getSkill().getLevel()[index];
					int actualLevel = player.getSkill().getLevelForXP(player.getSkill().getExp()[index]);
					int levelAfterDrink = currentLevel;
					levelAfterDrink += actualLevel * statModifiers[i];
					levelAfterDrink += statAddons[i];
					if (currentLevel > actualLevel)
						continue;
					if (levelAfterDrink <= actualLevel) {
						player.getSkill().getLevel()[index] = levelAfterDrink;
						player.getSkill().refresh(index);
					}
					else {
						player.getSkill().getLevel()[index] = 
								player.getSkill().getLevelForXP(player.getSkill().getExp()[index]);
						player.getSkill().refresh(index);
					}
				}
			}
			doOtherPotionEffects(itemId);
			player.getUpdateFlags().sendAnimation(829, 0);
			if (potionIdIndex < 3) {
				player.getInventory().removeItemSlot(new Item(itemId, 1), slot);
				player.getInventory().addItemToSlot(new Item(
						potionIds[potionIdIndex + 1], 1), slot);
				player.getActionSender().sendMessage("You drink a dose of your " + 
					potionDefinitions[potionIndex].getPotionName() + ".");
			}
			else {
				player.getActionSender().sendMessage("You drink the last dose " +
						"of your " + 
						potionDefinitions[potionIndex].getPotionName() + ".");
				player.getInventory().removeItemSlot(new Item(itemId, 1), slot);
				player.getInventory().addItemToSlot(new Item(EMPTY_VIAL, 1), slot);
			}
			tickAfterDrinking();
		}
	}
	
	private void doOtherPotionEffects(int itemId) {
		switch (itemId) {
			case 2446: //Antipoison
			case 175:
			case 177:
			case 179:
				Poison.appendPoison(player, false, 0);
				player.setPoisonImmunityTimer(300);
				break;
			case 3008: //Energy
			case 3010:
			case 3012:
			case 3014:
				if (player.getEnergy() + 20 < 100)
					player.setEnergy(player.getEnergy() + 20);
				else
					player.setEnergy(100);
				break;
			case 2448: //Super antipoison
			case 181:
			case 183:
			case 185:
				Poison.appendPoison(player, false, 0);
				player.setPoisonImmunityTimer(600);
				break;
			case 3016: //Super energy
			case 3018:
			case 3020:
			case 3022:
				if (player.getEnergy() + 40 < 100)
					player.setEnergy(player.getEnergy() + 40);
				else
					player.setEnergy(100);
				break;
			case 5943: //Antipoison+
			case 5945:
			case 5947:
			case 5949:
				Poison.appendPoison(player, false, 0);
				player.setPoisonImmunityTimer(900);
				break;
			case 2452: //Antifire
			case 2454:
			case 2456:
			case 2458:
				player.setFireImmunityTimer(player.getFireImmunityTimer() + 600);
				break;
			case 5952: //Antipoison++
			case 5954:
			case 5956:
			case 5958:
				Poison.appendPoison(player, false, 0);
				player.setPoisonImmunityTimer(1200);
				break;
			case 6685: //Saradomin brew
			case 6687:
			case 6689:
			case 6691:
				Object[][] skillModifiers = {
					{0, 0.1}, {2, 0.1}, {4, 0.1}, {6, 0.1}
				};
				player.getSkill().getLevel()[Skill.DEFENCE] = (int) (player.getSkill().getLevel()[Skill.DEFENCE] +
						player.getSkill().getLevelForXP(player.getSkill().getExp()[Skill.DEFENCE]) * 0.15);
				for (int i = 0; i < skillModifiers.length; i++) {
					int levelModifier = (int) (player.getSkill().getLevelForXP(player.getSkill().getExp()[(Integer) skillModifiers[i][0]]) * 
							(Double) skillModifiers[i][1]);
					if (player.getSkill().getLevel()[(Integer) skillModifiers[i][0]] - levelModifier <= 1) {
						player.getSkill().getLevel()[(Integer) skillModifiers[i][0]] = 1;
						player.getSkill().refresh((Integer) skillModifiers[i][0]);
						continue;
					}
					player.getSkill().getLevel()[(Integer) skillModifiers[i][0]] = 
							player.getSkill().getLevel()[(Integer) skillModifiers[i][0]] - levelModifier;
					player.getSkill().refresh((Integer) skillModifiers[i][0]);
				}
				int maximumDefence = 
						(int) (player.getSkill().getLevelForXP(player.getSkill().getExp()[Skill.DEFENCE]) * 0.25) +
								player.getSkill().getLevelForXP(player.getSkill().getExp()[Skill.DEFENCE]);
				int newDefenceLevel = player.getSkill().getLevel()[Skill.DEFENCE];
				newDefenceLevel += (int) (player.getSkill().getLevelForXP(player.getSkill().getExp()[Skill.DEFENCE]) * 0.25);
				if (newDefenceLevel > maximumDefence) {
					player.getSkill().getLevel()[Skill.DEFENCE] = maximumDefence;
				}
				else {
					player.getSkill().getLevel()[Skill.DEFENCE] = newDefenceLevel;
				}
				player.getSkill().refresh(1);
				int maximumHitpoints = 
						(int) (player.getSkill().getLevelForXP(player.getSkill().getExp()[Skill.HITPOINTS]) * 0.15) +
								player.getSkill().getLevelForXP(player.getSkill().getExp()[Skill.HITPOINTS]);
				int newHitpointsLevel = player.getSkill().getLevel()[Skill.HITPOINTS];
				newHitpointsLevel += 20;
				newHitpointsLevel += (int) (player.getSkill().getLevelForXP(player.getSkill().getExp()[Skill.HITPOINTS]) * 0.15);
				if (newHitpointsLevel > maximumHitpoints) {
					player.getSkill().getLevel()[Skill.HITPOINTS] = maximumHitpoints;
				}
				else {
					player.getSkill().getLevel()[Skill.HITPOINTS] = newHitpointsLevel;
				}
				player.getSkill().refresh(3);
				break;
			case 2450: //Zamorak brew
			case 189:
			case 191:
			case 193:
				int newLevel, maximumLevel;
				Object[][] statModifiers = {
					{1, 0.1, 2}, {3, 0.1, 20}
				};
				for (int i = 0; i < statModifiers.length; i++) {
					newLevel = player.getSkill().getLevel()[(Integer) statModifiers[i][0]];
					newLevel -= (int) (player.getSkill().getLevelForXP(player.getSkill().getExp()[(Integer) statModifiers[i][0]]) *
							(Double) statModifiers[i][1]);
					newLevel -= (Integer) statModifiers[i][2];
					if (newLevel < 1) {
						player.getSkill().getLevel()[(Integer) statModifiers[i][0]] = 1;
					}
					else {
						player.getSkill().getLevel()[(Integer) statModifiers[i][0]] = newLevel;
					}
					player.getSkill().refresh((Integer) statModifiers[i][0]);
				}
				Object[][] statModifiers2 = {
					{0, 0.2, 2}, {2, 0.12, 2}, {5, 0.1, 0}
				};
				for (int i = 0; i < statModifiers2.length; i++) {
					maximumLevel = (int) (player.getSkill().getLevelForXP(player.getSkill().getExp()[(Integer) statModifiers2[i][0]]) *
							(Double) statModifiers2[i][1]) + (Integer) statModifiers2[i][2] +
							player.getSkill().getLevelForXP(player.getSkill().getExp()[(Integer) statModifiers2[i][0]]);
					newLevel = player.getSkill().getLevel()[(Integer) statModifiers2[i][0]];
					newLevel += (int) (player.getSkill().getLevelForXP(player.getSkill().getExp()[(Integer) statModifiers2[i][0]]) *
							(Double) statModifiers2[i][1]);
					newLevel += (Integer) statModifiers2[i][2];
					if (i == 2 && newLevel > 99) {
						player.getSkill().getLevel()[(Integer) statModifiers2[i][0]] = 99;
					}
					else if (newLevel > maximumLevel) {
						player.getSkill().getLevel()[(Integer) statModifiers2[i][0]] = maximumLevel;
					}
					else {
						player.getSkill().getLevel()[(Integer) statModifiers2[i][0]] = newLevel;
					}
					player.getSkill().refresh((Integer) statModifiers2[i][0]);
				}
				break;
			default:
				break;
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