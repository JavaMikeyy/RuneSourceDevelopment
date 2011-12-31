package com.rs2.model.content.combat.ranged;

import com.rs2.model.players.Player;
import com.rs2.model.players.Item;
import com.rs2.model.players.ItemManager;
import com.rs2.model.npcs.Npc;
import com.rs2.model.Entity;

/**
  * By Mikey` of Rune-Server
  * - Credit to Xynth of Rune-Server for the projectiles.
  test
  */
public class Ranged {

	private Player player;
	private Npc npc;
	
	public Ranged(Player player) {
		this.player = player;
	}
	
	public Ranged(Npc npc) {
		this.npc = npc;
	}
	
	/**
	  * All the bow definitions.
	  */
	private static BowDefinition[] bowDefinitions = new BowDefinition[50];
	
	/**
	  * I put this in place for loops. It corresponds to the number of indexes in the BowDefinition[] array.
	  */
	public static int bowCount = 0;
	
	public int bowIndex, usingArrow;
	
	private final int[][] ARROW_PROJECTILE_DATA =
	{//Arrow id, Gfx id, Projectile id
	{882, 19, 10}, 
	};
	
	private final int[][] THROWN_PROJECTILE_DATA =
	{//Arrow id, Gfx id, Projectile id
	{863, 220, 213}, 
	};
	
	public boolean usingBow(int weaponId) {
		for (int i = 0; i < bowCount; i++) {
			if (weaponId == bowDefinitions[i].getBowId()) {
				bowIndex = i;
				return true;
			}
		}
		return false;
	}
	
	public boolean checkArrows() {
		Item weapon = player.getEquipment().getItemContainer().get(3);
		Item arrow = player.getEquipment().getItemContainer().get(13);
		if (weapon == null)
			return false;
		if (weapon.getId() == bowDefinitions[bowIndex].getBowId()) {
			if (bowDefinitions[bowIndex].getHighestArrowId() == 0)
				return true;
			if (!bowDefinitions[bowIndex].arrowAllowed(arrow.getId())) {
				String arrowName = ItemManager.getInstance().getItemName(arrow.getId());
				String bowName = ItemManager.getInstance().getItemName(weapon.getId());
				player.getActionSender().sendMessage("You can't use " + arrowName + "s with a " + bowName + "!");
				return false;
			}
			usingArrow = arrow.getId();
			return true;
		}
		return false;
	}
	
	public int[] getArrowProjectileData() {
		Item weapon = player.getEquipment().getItemContainer().get(3);
		for (int i = 0; i < THROWN_PROJECTILE_DATA.length; i++)
			if (weapon.getId() == THROWN_PROJECTILE_DATA[i][0])
				return THROWN_PROJECTILE_DATA[i];
		for (int i = 0; i < ARROW_PROJECTILE_DATA.length; i++)
			if (usingArrow == ARROW_PROJECTILE_DATA[i][0])
				return ARROW_PROJECTILE_DATA[i];
		
		return ARROW_PROJECTILE_DATA[0];
	}
	
	public int getBowIndex() {
		return bowIndex;
	}
	
	public static BowDefinition[] getBowDefinitions() {
		return bowDefinitions;
	}
	
}