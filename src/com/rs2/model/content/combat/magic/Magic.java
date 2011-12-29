package com.rs2.model.content.combat.magic;

import com.rs2.model.players.Player;
import com.rs2.model.players.Item;
import com.rs2.model.players.ItemManager;
import com.rs2.model.npcs.Npc;
import com.rs2.model.Entity;

/**
  * By Mikey` of Rune-Server (MSN: metallic_mike@yahoo.com)
  */
public class Magic {

	private Player player;
	private Npc npc;
	
	public Magic(Player player) {
		this.player = player;
	}
	
	public Magic(Npc npc) {
		this.npc = npc;
	}
	
	/**
	  * All the spell definitions.
	  */
	private static SpellDefinition[] spellDefinitions = new SpellDefinition[50];
	
	/**
	  * I put this in place for loops. It corresponds to the number of indexes in the SpellDefinition[] array.
	  */
	public static int spellCount = 0;
	
	/**
	  * Staff ids, and the runes they can substitute for.
	  */
	public static final int[][] STAFFS =
	{//runeId, staffId
	{556, 1381}, {0, 4675}
	};
	
	private int magicId, magicMaxHit, magicIndex, graphicId, animationId;
	public boolean autoCasting;
	private String spellName;
	
	/**
	  * The type of magic you are casting.
	  */
	public enum MagicTypes {
		AUTO_CAST, SINGLE_ATTACK
	}
	
	/**
	  * This instance is used to declare single magic attacks, while the main class is used for autocasting.
	  */
	public Magic singleMagicAttack;
	
	/**
	  * This instance is used to declare single magic attacks, while the main class is used for autocasting.
	  */
	public Magic singleMagicAttackChange;
	
	/**
	  * This instance is used to store data for the autocast change, so that variables change only after the attack finishes.
	  */
	public Magic autoCast;
	
	/**
	  * This instance is used to store data for the autocast change, so that variables change only after the attack finishes.
	  */
	public Magic autoCastChange;
	
	/**
	  * Declaring a new instance of the magic class.
	  */
	public Magic(int magicId, int magicMaxHit, int magicIndex, int graphicId, int animationId, boolean autoCasting, String spellName) {
		this.magicId = magicId;
		this.magicMaxHit = magicMaxHit;
		this.magicIndex = magicIndex;
		this.graphicId = graphicId;
		this.animationId = animationId;
		this.autoCasting = autoCasting;
		this.spellName = spellName;
	}
	
	public void magicTick(Entity entity) {
		if (autoCastChange != null) {
			if (entity.getHitDelayTimer() == -1) {
				autoCast = autoCastChange;
				autoCastChange = null;
			}	
		}
		if (singleMagicAttackChange != null) {
			if (entity.getHitDelayTimer() == -1) {
				singleMagicAttack = singleMagicAttackChange;
				singleMagicAttackChange = null;
			}
		}
		if (!entity.isInstigatingAttack())
			resetMagic(entity);
	}
	
	/**
	  * Declaring a single magic attack, or setting your autocast settings.
	  */
	public void calculateAttackWithMagic(Entity attacker, Entity victim, int magicId, MagicTypes magicType) {
		if (attacker instanceof Player) {
			int magicIndex = -1;
			for (int i = 0; i < spellCount; i++) {
				if (magicId == spellDefinitions[i].getSpellId())
					magicIndex = i;
			}
			if (magicIndex == -1)
				return;
			int magicLevel = player.getSkill().getLevelForXP((int) player.getSkill().getExp()[player.getSkill().MAGIC]);
			String spellName = spellDefinitions[magicIndex].getSpellName();
			int magicLevelRequired = spellDefinitions[magicIndex].getRequiredLevel();
			int magicMaxHit = spellDefinitions[magicIndex].getMaxHit();
			int graphicId = spellDefinitions[magicIndex].getGraphicId();
			int[] runesRequired = spellDefinitions[magicIndex].getRunesRequired();
			if (magicLevel >= magicLevelRequired) {
				for (int i = 0; i < runesRequired.length; i += 2) {
					if (runesRequired[i] != 0) {
						if (player.getInventory().getItemContainer().getCount(runesRequired[i]) < runesRequired[i + 1]) {
							if (!usingCorrespondingStaff(runesRequired[i])) {
								String runeName = ItemManager.getInstance().getItemName(runesRequired[i]);
								player.getActionSender().sendMessage("You don't have enough " + runeName + "s to cast " + spellName + ".");
								attacker.setInstigatingAttack(false);
								attacker.setFollowingEntity(null);
								return;
							}
						}
					}
				}
				if (magicType == MagicTypes.SINGLE_ATTACK) {
					attacker.setAttackType(Entity.AttackTypes.MAGIC);
					attacker.setInstigatingAttack(true);
					attacker.setTarget(victim);
					singleMagicAttackChange = new Magic(magicId, magicMaxHit, magicIndex, graphicId, animationId, false, spellName);
				}
				else if (magicType == MagicTypes.AUTO_CAST) {
					autoCastChange = new Magic(magicId, magicMaxHit, magicIndex, graphicId, animationId, true, spellName);
					player.getActionSender().sendConfig(108, 1);
					player.getActionSender().sendString((String) "@yel@" + spellDefinitions[autoCastChange.magicIndex].getSpellName(), 18585);
					attacker.setAttackType(Entity.AttackTypes.MAGIC);
				}
			}
			else {
				player.getActionSender().sendMessage("You need a magic level of " + magicLevelRequired + " to cast " + spellName + ".");
			}
		}
	}
	
	/**
	  * Reseting magic where needed.
	  */
	public void resetMagic(Entity attacker) {
		if (attacker instanceof Player) {
			/*if (autoCast != null) {
				if (!autoCast.autoCasting && attacker.getAttackType() == Entity.AttackTypes.MAGIC
				|| autoCast.autoCasting && !hasStaff()) {
					player.getCombat().resetCombatType(attacker);
				}
			}*/
			if (singleMagicAttack != null) {
				singleMagicAttack = null;
			}
		}
	}
	
	public void resetAutoCast() {
		if (autoCast != null) {
			autoCast = null;
		}
	}
	
	/**
	  * Checking for and removing runes before casting the spell.
	  */
	public void removeRunes(Entity attacker) {
		int magicIndex = -1;
		if (autoCast != null)
			magicIndex = autoCast.magicIndex;
		if (singleMagicAttack != null)
			magicIndex = singleMagicAttack.magicIndex;
		int[] runesRequired = spellDefinitions[magicIndex].getRunesRequired();
		int[] runesToRemove = new int[runesRequired.length];
		for (int i = 0; i < runesRequired.length; i += 2) {
			if (!usingCorrespondingStaff(runesRequired[i])) {
				if (player.getInventory().getItemContainer().getCount(runesRequired[i]) < runesRequired[i + 1]) {
					String runeName = ItemManager.getInstance().getItemName(runesRequired[i]);
					String spellName = getSpellName();
					player.getActionSender().sendMessage("You don't have enough " + runeName + "s to cast " + spellName + ".");
					player.getCombat().resetCombatType(attacker);
					return;
				}
			}
			if (!usingCorrespondingStaff(runesRequired[i])) {
					runesToRemove[i] = runesRequired[i];
					runesToRemove[i + 1] = runesRequired[i + 1];
			}
		}
		for (int i = 0; i < runesToRemove.length; i += 2)
			if (runesToRemove[i] != 0)
				player.getInventory().removeItem(new Item(runesToRemove[i], runesToRemove[i + 1]));
	}
	
	/**
	  * Applying any extra effects (poisoning, freezing, etc) after the attack.
	  */
	public void applyMagicEffects(Entity attacker, Entity victim) {
		int magicIndex = -1;
		if (autoCast != null)
			magicIndex = autoCast.magicIndex;
		if (singleMagicAttack != null)
			magicIndex = singleMagicAttack.magicIndex;
		switch (magicIndex) {
			case 0:
				break;
		}
	}
	
	/**
	  * Check for a staff that corresponds to the rune.
	  */
	public boolean usingCorrespondingStaff(int runeId) {
		Item weapon = player.getEquipment().getItemContainer().get(3);
		if (weapon == null)
			return false;
		for (int i = 0; i < STAFFS.length; i++) {
			if (STAFFS[i][0] == runeId) {
				if (weapon.getId() == STAFFS[i][1]) {
					return true;
				}
				return false;
			}
		}
		return false;
	}
	
	/**
	  * Checking if the player has a staff equipped.
	  */
	public boolean hasStaff() {
		Item weapon = player.getEquipment().getItemContainer().get(3);
		if (weapon == null)
			return false;
		for (int i = 0; i < STAFFS.length; i++) {
			if (weapon.getId() == STAFFS[i][1]) {
				return true;
			}
		}
		return false;
	}
	
	/**
	  * The buttons associated with anything relating to this class.
	  */
	public void clickingToAutoCast(int buttonId) {
		for (int i = 0; i < spellCount; i++) {
			if (buttonId == spellDefinitions[i].getAutoCastButton()) {
				calculateAttackWithMagic(player, null, spellDefinitions[i].getSpellId(), MagicTypes.AUTO_CAST);
				player.getActionSender().sendSidebarInterface(0, 328);
			}
		}
		switch (buttonId) {
			case 1093:
				if (autoCast != null) {
					autoCast.autoCasting = !autoCast.autoCasting;
					player.getActionSender().sendConfig(108, autoCast.autoCasting ? 1 : 0);
					if (autoCast.autoCasting)
						player.setAttackType(Entity.AttackTypes.MAGIC);
				}
				else {
					player.getActionSender().sendMessage("You haven't selected a spell to autocast!");
				}
				break;
			case 1097:
				Item weapon = player.getEquipment().getItemContainer().get(3);
				if (weapon.getId() == 4675) {
					if (player.getMagicBookType() == Player.MagicBookTypes.ANCIENT)
						player.getActionSender().sendSidebarInterface(0, 1689);
					else
						player.getActionSender().sendMessage("You can't autocast ancient magic while using the modern spell book!");
				}
				else {
					if (player.getMagicBookType() == Player.MagicBookTypes.MODERN)
						player.getActionSender().sendSidebarInterface(0, 1829);
					else
						player.getActionSender().sendMessage("You can't autocast modern magic while using the ancient spell book!");
				}
				break;
			case 7212:
				player.getActionSender().sendSidebarInterface(0, 328);
				break;
		}
	}
	
	public int getMagicId() {
		return magicId;
	}
	
	public int getMagicMaxHit() {
		return magicMaxHit;
	}
	
	public int getMagicIndex() {
		if (singleMagicAttack != null)
			return singleMagicAttack.magicIndex;
		if (autoCast != null)
			return autoCast.magicIndex;
		return 0;
	}
	
	public String getSpellName() {
		return spellName;
	}
	
	public boolean isAutoCasting() {
		System.out.println("" + autoCast);
		if (autoCast != null)
			return autoCast.autoCasting;
		return false;
	}
	
	public void setAutoCasting(boolean autoCasting) {
		if (autoCast != null) {
			autoCast.autoCasting = autoCasting;
			player.getActionSender().sendConfig(108, autoCast.autoCasting ? 1 : 0);
			if (!autoCast.autoCasting) {
				player.getActionSender().sendString("Choose spell", 18585);
				resetAutoCast();
			}
		}
	}
	
	public static SpellDefinition[] getSpellDefinitions() {
		return spellDefinitions;
	}
	
}