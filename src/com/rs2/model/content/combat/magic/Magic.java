package com.rs2.model.content.combat.magic;

import com.rs2.model.players.Player;
import com.rs2.model.players.Item;
import com.rs2.model.players.ItemManager;
import com.rs2.model.npcs.Npc;
import com.rs2.model.Entity;

/**
  * By Mikey` of Rune-Server
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
	
	public static boolean attackInitialized = false;
	
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
		appendMagicAttackStatus(entity);
		/*if (entity.isFrozen() && entity.getFrozenTimer() > 0)
			entity.setFrozenTimer(entity.getFrozenTimer() - 1);
		if (entity.isFrozen() && entity.getFrozenTimer() == 0)
			entity.setFrozen(false);*/
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
					singleMagicAttackChange = new Magic(magicId, magicMaxHit, magicIndex, graphicId, animationId, false, spellName);
					attacker.setTarget(victim);
				}
				else if (magicType == MagicTypes.AUTO_CAST) {
					autoCastChange = new Magic(magicId, magicMaxHit, magicIndex, graphicId, animationId, true, spellName);
					setAutoCasting(true, magicIndex);
					attacker.setInstigatingAttack(false);
					attacker.setFollowingEntity(null);
				}
			}
			else {
				player.getActionSender().sendMessage("You need a magic level of " + magicLevelRequired + " to cast " + spellName + ".");
			}
		}
	}
	
	public void appendMagicAttackStatus(Entity entity) {
		if (singleMagicAttackChange != null && !this.attackInitialized) {
			singleMagicAttack = singleMagicAttackChange;
			singleMagicAttackChange = null;
			entity.setInstigatingAttack(true);
		}
		if (autoCastChange != null && !this.attackInitialized) {
			player.getActionSender().sendMessage("Set new autocast.");
			autoCast = autoCastChange;
			autoCastChange = null;
		}
	}
	
	/**
	  * Reseting magic where needed.
	  */
	public void resetMagic(Entity attacker) {
		if (attacker instanceof Player) {
			if (singleMagicAttack != null) {
				singleMagicAttack = null;
				attacker.setInstigatingAttack(false);
				attacker.setFollowingEntity(null);
			}
			this.attackInitialized = false;
			player.getActionSender().sendMessage("Reset magic.");
		}
	}
	
	/**
	  * Checking for and removing runes before casting the spell.
	  */
	public boolean removeRunes(Entity attacker) {
		int magicIndex = getMagicIndex();
		int[] runesRequired = spellDefinitions[magicIndex].getRunesRequired();
		int[] runesToRemove = new int[runesRequired.length];
		for (int i = 0; i < runesRequired.length; i += 2) {
			if (!usingCorrespondingStaff(runesRequired[i])) {
				if (player.getInventory().getItemContainer().getCount(runesRequired[i]) < runesRequired[i + 1]) {
					String runeName = ItemManager.getInstance().getItemName(runesRequired[i]);
					String spellName = spellDefinitions[magicIndex].getSpellName();
					player.getActionSender().sendMessage("You don't have enough " + runeName + "s to cast " + spellName + ".");
					attacker.setInstigatingAttack(false);
					attacker.setFollowingEntity(null);
					return false;
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
		return true;
	}
	
	/**
	  * Applying any extra effects (poisoning, freezing, etc) after the attack.
	  */
	public void applyMagicEffects(Entity attacker, Entity victim) {
		int magicIndex = getMagicIndex();
		switch (magicIndex) {
			case 12891:
				victim.setFrozen(true);
				victim.setFrozenTimer(15);
				break;
		}
	}
	
	/**
	  * Check for a staff that corresponds to the rune required.
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
					setAutoCasting(autoCast.autoCasting, autoCast.magicIndex);
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
		if (singleMagicAttack != null) {
			return singleMagicAttack.magicIndex;
		}
		if (autoCast != null) {
			return autoCast.magicIndex;
		}
		player.getActionSender().sendMessage("Could not find magicIndex, returning 0.");
		return 0;
	}
	
	/**
	  * Used to return the magicIndex of an outside declarded instance of the class
	  */
	public int getNamedMagicIndex() {
		return magicIndex;
	}
	
	public String getSpellName() {
		return spellName;
	}
	
	public boolean isAutoCasting() {
		if (autoCast != null)
			return autoCast.autoCasting;
		return false;
	}
	
	public void setAutoCasting(boolean autoCasting, int magicIndex) {
		if (autoCast != null) {
			autoCast.autoCasting = autoCasting;
			if (autoCast.autoCasting)
				player.getActionSender().updateAutoCastInterface(autoCast.magicIndex);
			if (!autoCast.autoCasting)
				player.getActionSender().resetAutoCastInterface();
		}
		if (autoCastChange != null) {
			autoCastChange.autoCasting = autoCasting;
			if (autoCastChange.autoCasting)
				player.getActionSender().updateAutoCastInterface(autoCastChange.magicIndex);
			if (!autoCastChange.autoCasting) {
				player.getActionSender().resetAutoCastInterface();
			}
		}
	}
	
	public static SpellDefinition[] getSpellDefinitions() {
		return spellDefinitions;
	}
	
}