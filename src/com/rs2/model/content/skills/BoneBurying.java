package com.rs2.model.content.skills;

import com.rs2.model.World;
import com.rs2.model.tick.Tick;
import com.rs2.model.players.Player;
import com.rs2.model.players.Item;
import com.rs2.model.players.ItemManager;

/**
  * By Mikey` of Rune-Server (MSN: metallic_mike@yahoo.com)
  */
public class BoneBurying {

	private Player player;
	
	public BoneBurying(Player player) {
		this.player = player;
	}
	
	boolean buryingBone = false;
	
	public static final int[] BURY_BONE =
	{
	526, 2859, 528, 3179, 3181, 3179, 3181, 530, 532, 10976, 10977, 3181, 3182, 4812, 3123,
	534, 6812, 536, 4830, 4832, 6729, 4834
	};
	
	public static final double[] BONE_EXPERIENCE =
	{
	4.5, 4.5, 4.5, 5, 5, 5.2, 15, 15, 15, 18, 20, 22.5, 25, 
	30, 50, 72, 84, 96, 125, 140
	};
	
	public void buryBone(int itemId) {
		if (!player.getSkill().skillTickRunning()) {
			for (int i = 0; i < BURY_BONE.length; i++) {
				if (itemId == BURY_BONE[i]) {
					buryingBone = true;
					player.getSkill().addExp(5, BONE_EXPERIENCE[i]);
					player.getInventory().removeItem(new Item(BURY_BONE[i], 1));
					player.getUpdateFlags().sendAnimation(827, 0);
					player.getActionSender().sendMessage("You bury the " + ItemManager.getInstance().getItemName(BURY_BONE[i]) + ".");
					World.submit(new Tick(2) {
						@Override
						public void execute() {
							player.getSkill().setSkillTickRunning(false);
							stop();
							return;
						}
					});
				}
			}
		}
	}
	
}
