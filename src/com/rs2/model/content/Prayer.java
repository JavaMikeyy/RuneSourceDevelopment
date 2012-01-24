package com.rs2.model.content;

import com.rs2.model.Entity;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.content.skills.Skill;
import com.rs2.util.Misc;

public class Prayer {

	private Player player;

	public Prayer(Player player) {
		this.player = player;
	}

	public static final int THICK_SKIN = 0, BURST_OF_STRENGTH = 1, CLARITY_OF_THOUGHT = 2, SHARP_EYE = 3, MYSTIC_WILL = 4,
			ROCK_SKIN = 5, SUPERHUMAN_STRENGTH = 6, IMPROVED_REFLEXES = 7, RAPID_RESTORE = 8, RAPID_HEAL = 9,
			PROTECT_ITEM = 10, HAWK_EYE = 11, MYSTIC_LORE = 12, STEEL_SKIN = 13, ULTIMATE_STRENGTH = 14,
			INCREDIBLE_REFLEXES = 15, PROTECT_FROM_MAGIC = 16, PROTECT_FROM_RANGED = 17, PROTECT_FROM_MELEE = 18,
			EAGLE_EYE = 19, MYSTIC_MIGHT = 20, RETRIBUTION = 21, REDEMPTION = 22, SMITE = 23, CHIVALRY = 24, PIETY = 25;

	private final Object[][] PRAYER_DATA = {
			//id, configId, name, levelRequired, drainAmount, drainRate
			{THICK_SKIN, 83, "Thick Skin", 1, 1.0}, 
			{BURST_OF_STRENGTH, 84, "Burst of Strength", 4, 1.0}, 
			{CLARITY_OF_THOUGHT, 85, "Clarity of Thought", 7, 1.0},
			{SHARP_EYE, 601, "Sharp Eye", 8, 1.0},
			{MYSTIC_WILL, 602, "Mystic Will", 0, 1.0},
			{ROCK_SKIN, 86, "Rock Skin", 10, 2.0},
			{SUPERHUMAN_STRENGTH, 87, "Superhuman Strength", 13, 2.0},
			{IMPROVED_REFLEXES, 88, "Improved Reflexes", 16, 2.0},
			{RAPID_RESTORE, 89, "Rapid Restore", 19, 0.4},
			{RAPID_HEAL, 90, "Rapid Heal", 22, 0.6},
			{PROTECT_ITEM, 91, "Protect Item", 25, 0.6},
			{HAWK_EYE, 603, "Hawk Eye", 26, 1.5},
			{MYSTIC_LORE, 604, "Mystic Lore", 27, 2.0},
			{STEEL_SKIN, 92, "Steel Skin", 28, 4.0},
			{ULTIMATE_STRENGTH, 93, "Ultimate Strength", 31, 4.0},
			{INCREDIBLE_REFLEXES, 94, "Incredible Reflexes", 34, 4.0},
			{PROTECT_FROM_MAGIC, 95, "Protect from Magic", 37, 4.0},
			{PROTECT_FROM_RANGED, 96, "Protect from Range", 40, 4.0},
			{PROTECT_FROM_MELEE, 97, "Protect from Melee", 43, 4.0},
			{EAGLE_EYE, 605, "Eagle Eye", 44, 4.0},
			{MYSTIC_MIGHT, 606, "Mystic Might", 45, 4.0},
			{RETRIBUTION, 98, "Retribution", 46, 1.0},
			{REDEMPTION, 99, "Redemption", 49, 2.0},
			{SMITE, 100, "Smite", 52, 6.0},
			{CHIVALRY, 607, "Chivalry", 60, 8.0},
			{PIETY, 608, "Piety", 70, 8.0}
	};
	
	private int drainTimer = 0;
	private double amountToDrain = 0.0;
	
	public void prayerTick() {
		for (int i = 0; i < player.getIsUsingPrayer().length; i++) {
			if (player.getIsUsingPrayer()[i]) {
				amountToDrain += ((Double) PRAYER_DATA[i][4] / 12)/* *
						(1 + (0.035 * player.getBonus(11)))*/;
			}
		}
		if (drainTimer < 12) {
			drainTimer ++;
		}
		if (drainTimer == 12 || amountToDrain >= player.getSkill().getLevel()[Skill.PRAYER]) {
			if (amountToDrain > 0)
				drainPrayer((int) amountToDrain);
			amountToDrain = 0.0;
			drainTimer = 0;
		}
	}

	public int getPrayerData(int index, int dataSlot) {
		int data = 0;
		for (Object[] i : PRAYER_DATA) {
			if (i[0] == (Integer) index) {
				data = (Integer) i[dataSlot];
			}
		}
		return data;
	}

	public void activatePrayers(int id) {
		if (player.isDead()) {
			return;
		}
		int config = 0;
		String name = null;
		int level = 0;
		for (Object[] data : PRAYER_DATA) {
			if (data[0] == (Integer) id) {
				config = (Integer) data[1];
				name = (String) data[2];
				level = (Integer) data[3];
			}
		}
		if (player.getSkill().getLevelForXP(player.getSkill().getExp()[5]) < level) {
			player.getActionSender().sendMessage("You need a prayer level of at least " + level + " to use " + name + ".");
			player.getActionSender().sendConfig(config, 0);
			return;
		}
		if (player.getSkill().getLevel()[5] <= 0) {
			resetAll();
			return;
		}
		int headIcon = -1;
		boolean hasHeadIcon = false;
		switch (id) {
		case PROTECT_FROM_MAGIC:
			headIcon = 2;
			hasHeadIcon = true;
			break;
		case PROTECT_FROM_RANGED:
			headIcon = 1;
			hasHeadIcon = true;
			break;
		case PROTECT_FROM_MELEE:
			headIcon = 0;
			hasHeadIcon = true;
			break;
		case RETRIBUTION:
			headIcon = 3;
			hasHeadIcon = true;
			break;
		case REDEMPTION:
			headIcon = 5;
			hasHeadIcon = true;
			break;
		case SMITE:
			headIcon = 4;
			hasHeadIcon = true;
			break;
		}
		if (hasHeadIcon) {
			player.setPrayerIcon(!player.getIsUsingPrayer()[id] ? headIcon : -1);
		}
		player.getIsUsingPrayer()[id] = !player.getIsUsingPrayer()[id];
		player.getActionSender().sendConfig(config, player.getIsUsingPrayer()[id] ? 1 : 0);
		switchPrayers(id);
		player.setPrayerDrainTimer(player.getIsUsingPrayer()[id] ? 0 : 1);
		player.setAppearanceUpdateRequired(true);
	}

	private void switchPrayers(int id) {
		int[] turnOff = new int[0];
		switch (id) {
		case THICK_SKIN:
			turnOff = new int[] {ROCK_SKIN, STEEL_SKIN, CHIVALRY, PIETY};
			break;
		case ROCK_SKIN:
			turnOff = new int[] {THICK_SKIN, STEEL_SKIN, CHIVALRY, PIETY};
			break;
		case STEEL_SKIN:
			turnOff = new int[] {THICK_SKIN, ROCK_SKIN, CHIVALRY, PIETY};
			break;
		case CLARITY_OF_THOUGHT:
			turnOff = new int[] {IMPROVED_REFLEXES, INCREDIBLE_REFLEXES, CHIVALRY, PIETY};
			break;
		case IMPROVED_REFLEXES:
			turnOff = new int[] {CLARITY_OF_THOUGHT, INCREDIBLE_REFLEXES, CHIVALRY, PIETY};
			break;
		case INCREDIBLE_REFLEXES:
			turnOff = new int[] {IMPROVED_REFLEXES, CLARITY_OF_THOUGHT, CHIVALRY, PIETY};
			break;
		case BURST_OF_STRENGTH:
			turnOff = new int[] {SUPERHUMAN_STRENGTH, ULTIMATE_STRENGTH, SHARP_EYE, MYSTIC_WILL, HAWK_EYE, 
					MYSTIC_LORE, EAGLE_EYE, MYSTIC_MIGHT, CHIVALRY, PIETY};
			break;
		case SUPERHUMAN_STRENGTH:
			turnOff = new int[] {BURST_OF_STRENGTH, ULTIMATE_STRENGTH, SHARP_EYE, MYSTIC_WILL, HAWK_EYE, 
					MYSTIC_LORE, EAGLE_EYE, MYSTIC_MIGHT, CHIVALRY, PIETY};
			break;
		case ULTIMATE_STRENGTH:
			turnOff = new int[] {SUPERHUMAN_STRENGTH, BURST_OF_STRENGTH, SHARP_EYE, MYSTIC_WILL, HAWK_EYE, 
					MYSTIC_LORE, EAGLE_EYE, MYSTIC_MIGHT, CHIVALRY, PIETY};
			break;
		case SHARP_EYE:
			turnOff = new int[] {MYSTIC_WILL, HAWK_EYE, MYSTIC_LORE, EAGLE_EYE, MYSTIC_MIGHT,
					BURST_OF_STRENGTH, SUPERHUMAN_STRENGTH, ULTIMATE_STRENGTH,
					CLARITY_OF_THOUGHT, IMPROVED_REFLEXES, INCREDIBLE_REFLEXES, CHIVALRY, PIETY};
			break;
		case HAWK_EYE:
			turnOff = new int[] {MYSTIC_WILL, SHARP_EYE, MYSTIC_LORE, EAGLE_EYE, MYSTIC_MIGHT, 
					BURST_OF_STRENGTH, SUPERHUMAN_STRENGTH, ULTIMATE_STRENGTH,
					CLARITY_OF_THOUGHT, IMPROVED_REFLEXES, INCREDIBLE_REFLEXES, CHIVALRY, PIETY};
			break;
		case EAGLE_EYE:
			turnOff = new int[] {MYSTIC_WILL, HAWK_EYE, MYSTIC_LORE, SHARP_EYE, MYSTIC_MIGHT,
					BURST_OF_STRENGTH, SUPERHUMAN_STRENGTH, ULTIMATE_STRENGTH,
					CLARITY_OF_THOUGHT, IMPROVED_REFLEXES, INCREDIBLE_REFLEXES, CHIVALRY, PIETY};
			break;
		case MYSTIC_WILL:
			turnOff = new int[] {SHARP_EYE, HAWK_EYE, MYSTIC_LORE, EAGLE_EYE, MYSTIC_MIGHT,
					BURST_OF_STRENGTH, SUPERHUMAN_STRENGTH, ULTIMATE_STRENGTH,
					CLARITY_OF_THOUGHT, IMPROVED_REFLEXES, INCREDIBLE_REFLEXES, CHIVALRY, PIETY};
			break;
		case MYSTIC_LORE:
			turnOff = new int[] {MYSTIC_WILL, HAWK_EYE, SHARP_EYE, EAGLE_EYE, MYSTIC_MIGHT,
					BURST_OF_STRENGTH, SUPERHUMAN_STRENGTH, ULTIMATE_STRENGTH,
					CLARITY_OF_THOUGHT, IMPROVED_REFLEXES, INCREDIBLE_REFLEXES, CHIVALRY, PIETY};
			break;
		case MYSTIC_MIGHT:
			turnOff = new int[] {MYSTIC_WILL, HAWK_EYE, MYSTIC_LORE, EAGLE_EYE, SHARP_EYE,
					BURST_OF_STRENGTH, SUPERHUMAN_STRENGTH, ULTIMATE_STRENGTH,
					CLARITY_OF_THOUGHT, IMPROVED_REFLEXES, INCREDIBLE_REFLEXES, CHIVALRY, PIETY};
			break;
		case PROTECT_FROM_MAGIC:
			turnOff = new int[] {REDEMPTION, SMITE, RETRIBUTION, PROTECT_FROM_RANGED, PROTECT_FROM_MELEE};
			break;
		case PROTECT_FROM_RANGED:
			turnOff = new int[] {REDEMPTION, SMITE, RETRIBUTION, PROTECT_FROM_MAGIC, PROTECT_FROM_MELEE};
			break;
		case PROTECT_FROM_MELEE:
			turnOff = new int[] {REDEMPTION, SMITE, RETRIBUTION, PROTECT_FROM_RANGED, PROTECT_FROM_MAGIC};
			break;
		case RETRIBUTION:
			turnOff = new int[] {REDEMPTION, SMITE, PROTECT_FROM_MELEE, PROTECT_FROM_RANGED, PROTECT_FROM_MAGIC};
			break;
		case REDEMPTION:
			turnOff = new int[] {RETRIBUTION, SMITE, PROTECT_FROM_MELEE, PROTECT_FROM_RANGED, PROTECT_FROM_MAGIC};
			break;
		case SMITE:
			turnOff = new int[] {REDEMPTION, RETRIBUTION, PROTECT_FROM_MELEE, PROTECT_FROM_RANGED, PROTECT_FROM_MAGIC};
			break;
		case CHIVALRY:
			turnOff = new int[] {SHARP_EYE, MYSTIC_WILL, HAWK_EYE, MYSTIC_LORE, EAGLE_EYE, MYSTIC_MIGHT,
					BURST_OF_STRENGTH, SUPERHUMAN_STRENGTH, ULTIMATE_STRENGTH,
					CLARITY_OF_THOUGHT, IMPROVED_REFLEXES, INCREDIBLE_REFLEXES, PIETY, THICK_SKIN,
					ROCK_SKIN, STEEL_SKIN};
			break;
		case PIETY:
			turnOff = new int[] {SHARP_EYE, MYSTIC_WILL, HAWK_EYE, MYSTIC_LORE, EAGLE_EYE, MYSTIC_MIGHT,
					BURST_OF_STRENGTH, SUPERHUMAN_STRENGTH, ULTIMATE_STRENGTH,
					CLARITY_OF_THOUGHT, IMPROVED_REFLEXES, INCREDIBLE_REFLEXES, CHIVALRY, THICK_SKIN,
					ROCK_SKIN, STEEL_SKIN};
			break;
		}
		for (int i : turnOff) {
			if (i != id) {
				player.getIsUsingPrayer()[i] = false;
				player.getActionSender().sendConfig(getPrayerData(i, 1), 0);
			}
		}
	}

	public void drainPrayer(int drainAmount) {
		player.getSkill().getLevel()[Skill.PRAYER] -= drainAmount;
		if (player.getSkill().getLevel()[Skill.PRAYER] <= 0) {
			player.getSkill().getLevel()[Skill.PRAYER] = 0;
			player.getSkill().refresh(Skill.PRAYER);
			resetAll();
			player.getActionSender().sendMessage("You have ran out of prayer points;" +
					" you must recharge at an altar.");
			return;
		}
		player.getSkill().refresh(Skill.PRAYER);
	}
	
	public void applySmiteEffect(Player victim, int hit) {
		if (player.getIsUsingPrayer()[SMITE]) {
			if ((victim.getSkill().getLevel()[Skill.PRAYER] -= hit / 4) < 0)
				victim.getSkill().getLevel()[Skill.PRAYER] = 0;
			else
				victim.getSkill().getLevel()[Skill.PRAYER] -= hit / 4;
			victim.getSkill().refresh(Skill.PRAYER);
		}
	}
	
	public void applyRedemptionPrayer(Player player) {
		if (player.getIsUsingPrayer()[REDEMPTION]) {
			if (player.getSkill().getLevel()[Skill.HITPOINTS] <=
					(int) (player.getSkill().getLevelForXP(player.getSkill().getExp()[Skill.HITPOINTS]) * 0.1)) {
				player.getSkill().getLevel()[Skill.HITPOINTS] += 
						(int) (player.getSkill().getLevelForXP(player.getSkill().getExp()[Skill.PRAYER]) * 0.25);
				player.getSkill().getLevel()[Skill.PRAYER] = 0;
				player.getUpdateFlags().sendGraphic(436, 0);
				player.getSkill().refresh(Skill.PRAYER);
				player.getSkill().refresh(Skill.HITPOINTS);
			}
		}
	}
	
	public void applyRetributionPrayer(Player player) {
		if (player.isDead()) {
			//Check for multi-area
			if (player.getCombatingEntity() != null) {
				if (Misc.getDistance(player.getPosition(), player.getCombatingEntity().getPosition()) < 4) {
					player.getCombatingEntity().hit(
							(int) (player.getSkill().getLevelForXP(player.getSkill().getExp()[5]) * 0.25), 1);
				}
				if (player.getCombatingEntity() instanceof Player) {
					Player otherPlayer = (Player) player.getCombatingEntity();
					otherPlayer.getSkill().refresh(Skill.HITPOINTS);
				}
			}
			player.getUpdateFlags().sendGraphic(437, 0);
		}
	}
	
	public int prayerHitModifiers(Entity attacker, Entity victim, int hit) {
		if (victim instanceof Player) {
			Player victimPlayer = (Player) victim;
			if (attacker instanceof Player) {
				Player attackingPlayer = (Player) attacker;
				if (victimPlayer.getIsUsingPrayer()[PROTECT_FROM_MELEE] &&
						attackingPlayer.getAttackType() == Entity.AttackTypes.MELEE) {
					hit = hit / 4;
				}
				else if (victimPlayer.getIsUsingPrayer()[PROTECT_FROM_RANGED] &&
						attackingPlayer.getAttackType() == Entity.AttackTypes.RANGED) {
					hit = hit / 4;
				}
				else if (victimPlayer.getIsUsingPrayer()[PROTECT_FROM_MAGIC] &&
						attackingPlayer.getAttackType() == Entity.AttackTypes.MAGIC) {
					hit = hit / 4;
				}
			}
			else if (attacker instanceof Npc) {
				Npc attackingNpc = (Npc) attacker;
				if (victimPlayer.getIsUsingPrayer()[PROTECT_FROM_MELEE] &&
						attackingNpc.getAttackType() == Entity.AttackTypes.MELEE) {
					hit = 0;
				}
				else if (victimPlayer.getIsUsingPrayer()[PROTECT_FROM_RANGED] &&
						attackingNpc.getAttackType() == Entity.AttackTypes.RANGED) {
					hit = 0;
				}
				else if (victimPlayer.getIsUsingPrayer()[PROTECT_FROM_MAGIC] &&
						attackingNpc.getAttackType() == Entity.AttackTypes.MAGIC) {
					hit = 0;
				}
			}
		}
		return hit;
	}
	
	public void resetAll() {
		for (int i = 0; i < 26; i ++) {
			player.getIsUsingPrayer()[i] = false;
			player.getActionSender().sendConfig(getPrayerData(i, 1), 0);
		}
		player.setPrayerIcon(-1);
		player.setAppearanceUpdateRequired(true);
	}

	public void setPrayers(int buttonId) {
		switch(buttonId) {
		case 21233:
			activatePrayers(THICK_SKIN);
			break;
		case 21234:
			activatePrayers(BURST_OF_STRENGTH);
			break;
		case 21235:
			activatePrayers(CLARITY_OF_THOUGHT);
			break;
		case 70080:
			activatePrayers(SHARP_EYE);
			break;
		case 70082:
			activatePrayers(MYSTIC_WILL);
			break;
		case 21236:
			activatePrayers(ROCK_SKIN);
			break;
		case 21237:
			activatePrayers(SUPERHUMAN_STRENGTH);
			break;
		case 21238:
			activatePrayers(IMPROVED_REFLEXES);
			break;
		case 21239:
			activatePrayers(RAPID_RESTORE);
			break;
		case 21240:
			activatePrayers(RAPID_HEAL);
			break;
		case 21241:
			activatePrayers(PROTECT_ITEM);
			break;
		case 70084:
			activatePrayers(HAWK_EYE);
			break;
		case 70086:
			activatePrayers(MYSTIC_LORE);
			break;
		case 21242:
			activatePrayers(STEEL_SKIN);
			break;
		case 21243:
			activatePrayers(ULTIMATE_STRENGTH);
			break;
		case 21244:
			activatePrayers(INCREDIBLE_REFLEXES);
			break;
		case 21245:
			activatePrayers(PROTECT_FROM_MAGIC);
			break;
		case 21246:
			activatePrayers(PROTECT_FROM_RANGED);
			break;
		case 21247:
			activatePrayers(PROTECT_FROM_MELEE);
			break;
		case 70088:
			activatePrayers(EAGLE_EYE);
			break;
		case 70090:
			activatePrayers(MYSTIC_MIGHT);
			break;
		case 2171:
			activatePrayers(RETRIBUTION);
			break;
		case 2172:
			activatePrayers(REDEMPTION);
			applyRedemptionPrayer(player);
			break;
		case 2173:
			activatePrayers(SMITE);
			break;
		case 70092:
			activatePrayers(CHIVALRY);
			break;
		case 70094:
			activatePrayers(PIETY);
			break;
		}
	}

}
