package com.rs2.model.content.skills;

import com.rs2.util.Misc;
import com.rs2.model.World;
import com.rs2.model.tick.Tick;
import com.rs2.model.Position;
import com.rs2.model.players.Player;
import com.rs2.model.players.Item;
import com.rs2.model.players.ItemManager;
import com.rs2.model.players.WalkToActions;

/**
  * By Mikey` of Rune-Server (MSN: metallic_mike@yahoo.com)
  */

public class Fishing {

	private Player player;
	
	public Fishing(Player player) {
		this.player = player;
	}
	
	public static final int[][] FISHING =
	{//npcId, optionNumber, levelRequired, itemRequired, animation
	//Key: (npcOption1, npcOption2) thisOption
	//(net, bait) net       (net, bait) bait       (lure, bait) lure       (lure, bait) bait                                                                  
	{316, 1, 1, 303, 621}, {316, 2, 5, 307, 623}, {317, 1, 10, 309, 623}, {317, 2, 5, 307, 623},
	//(cage, harpoon) cage  (cage, harpoon) harp.   (net, harpoon) net       (net, harpoon) harp.                                                                                               
	{321, 1, 40, 301, 619}, {321, 2, 35, 311, 618}, {313, 1, 16, 305, 620}, {313, 2, 76, 311, 618},
	//(net) net
	{1174, 1, 62, 303, 621}
	};
	
	public static final int[] BAIT =
	{//The bait ID, 0 for no bait, Same order as FISHING, FISH_FROM_SPOTS
	0, 313, 314, 313, 0, 0, 0, 0, 0
	};
	
	public static final int[][] FISH_FROM_SPOTS =
	{//Same order as FISHING
	{317, 321}, {327, 345, 349}, {335, 331}, {327, 345, 349}, 
	{377}, {359, 371}, {353, 341, 363}, {383}, 
	{7944}
	};
	
	public static final int[][] LEVEL_REQUIRED =
	{//Same order as FISH_FROM_SPOTS
	{1, 15}, {5, 10, 25}, {20, 30}, {5, 10, 25},
	{40}, {35, 50}, {16, 23, 46}, {76}, 
	{62}
	};
	
	public static final int[][] FISH_EXP =
	{//Same order as FISH_FROM_SPOTS
	{10, 40}, {20, 30, 60}, {50, 70}, {20, 30, 60},
	{90}, {80, 100}, {20, 45, 100}, {110}, 
	{120}
	};
	
	public void startFishing(int npcId, int optionNumber) {
		final int level = player.getSkill().getLevelForXP((int) player.getSkill().getExp()[Skill.FISHING]);
		if (!player.getSkill().skillTickRunning()) {
			player.getSkill().setSkillTickRunning(true);
			player.getSkill().setStopSkillTick(false);
			for (int i = 0; i < FISHING.length; i++) {
				if (npcId == FISHING[i][0] && optionNumber == FISHING[i][1]) {
					if (level >= FISHING[i][2]) {
						int hasLure = player.getInventory().getItemContainer().getCount(FISHING[i][3]);
						if (hasLure > 0) {
							final int i2 = i;
							World.submit(new Tick(1) {
								@Override
								public void execute() {
									if (player.getInventory().getItemContainer().freeSlots() < 1) {
										player.getActionSender().sendMessage("You don't have any inventory space.");
										player.getSkill().setStopSkillTick(true);
									}
									if (player.getSkill().stopSkillTick()) {
										player.getSkill().setStopSkillTick(false);
										player.getSkill().setSkillTickRunning(false);
										stop();
										return;
									}
									if (!player.withinDistance())
										return;
									player.getUpdateFlags().sendAnimation(FISHING[i2][4], 0);
									int successChance = calculateSuccess(i2);
									if (successChance > -1) {
										int recievedFish = FISH_FROM_SPOTS[i2][successChance];
										if(BAIT[i2] != 0) {
											int hasBait = player.getInventory().getItemContainer().getCount(BAIT[i2]);
											if (hasBait > 0) {
												player.getInventory().removeItem(new Item(BAIT[i2], 1));
											}
											else {
												player.getActionSender().sendMessage("You don't have any " + ItemManager.getInstance().getItemName(BAIT[i2]) + ".");
												player.getSkill().setStopSkillTick(true);
												player.getUpdateFlags().sendAnimation(-1, 0);
												return;
											}
										}
										if (level < LEVEL_REQUIRED[i2][successChance]) {
											recievedFish = FISH_FROM_SPOTS[i2][0];
										}
										player.getInventory().addItem(new Item(recievedFish, 1));
										player.getSkill().addExp(10, FISH_EXP[i2][successChance]);
										player.getActionSender().sendMessage("You catch " + ItemManager.getInstance().getItemName(recievedFish) + ".");
									}
								}
							});
						}
						else {
							player.getActionSender().sendMessage("You need a " + 
							ItemManager.getInstance().getItemName(FISHING[i][3]) + " to fish here.");
							player.getSkill().setSkillTickRunning(false);
							return;
						}
					}
					else {
						player.getActionSender().sendMessage("You need a Fishing level of " + FISHING[i][2] + " to fish here.");
						player.getSkill().setSkillTickRunning(false);
						return;
					}
				}
			}
		}
	}
	
	public int calculateSuccess(int indexId) {
		int highestIndex = LEVEL_REQUIRED[indexId].length;
		int level = player.getSkill().getLevelForXP((int) player.getSkill().getExp()[Skill.FISHING]);
		int randomAmount = Misc.randomNumber(3);
		double successChance = 0;
		for (int i = highestIndex - 1; i > -1; i--) {
			if (level >= LEVEL_REQUIRED[indexId][i]) {
				successChance = Math.ceil((level * 50 - LEVEL_REQUIRED[indexId][i] * 15) / LEVEL_REQUIRED[indexId][i] / 4 - randomAmount * 4);
				int randomizedSuccessChance = Misc.randomNumber(99);
				if (successChance >= randomizedSuccessChance) {
					return i;
				}
			}
		}
		return -1;
	}

}












