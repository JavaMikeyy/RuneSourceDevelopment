package com.rs2.model.content.randomevents;

import com.rs2.model.World;
import com.rs2.model.Position;
import com.rs2.model.tick.Tick;
import com.rs2.model.players.Player;
import com.rs2.model.players.Item;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcDefinition;

/**
  * By Mikey` of Rune-Server
  */
public class Genie {

	RandomEvent randomEvent;
	
	Player player;
	
	public Genie(Player player) {
		this.player = player;
	}
	
	private int advancingSkill;
	
	public final int[][] LAMP_INTERFACE_CONFIG =
	{
	{10252, 1, 0}, {10253, 2, 2}, {10254, 3, 4}, {10255, 4, 6}, {11000, 5, 1}, {11001, 6, 3}, {11002, 7, 5}, {11003, 8, 16}, {11004, 9, 15},
	{11005, 10, 17}, {11006, 11, 12}, {11007, 12, 20}, {47002, 20, 18}, {54090, 21, 19}, {11008, 13, 14}, {11009, 14, 13}, {11010, 15, 10},
	{11011, 16, 7}, {11012, 17, 11}, {11013, 18, 8}, {11014, 19, 9}
	};
	
	public void addExpFromLamp() {
		if (player.getInventory().getItemContainer().getCount(2528) > 0) {
			player.getActionSender().sendConfig(261, 0);
			int experience = (Integer) (player.getSkill().getLevelForXP(player.getSkill().getExp()[advancingSkill]) * 10);
			player.getDialogue().sendStatement1("The lamp gives you " + experience + " " + player.getSkill().SKILL_NAME[advancingSkill] + " experience.");
			player.getSkill().addExp(advancingSkill, experience);
			player.getInventory().removeItem(new Item(2528, 1));
			advancingSkill = -1;
		}
		else {
			player.getActionSender().removeInterfaces();
		}
	}
	
	public void clickLampButton(int buttonId) {
		for (int i = 0; i < LAMP_INTERFACE_CONFIG.length; i++) {
			if (buttonId == LAMP_INTERFACE_CONFIG[i][0]) {
				player.getActionSender().sendConfig(261, LAMP_INTERFACE_CONFIG[i][1]);
				advancingSkill = LAMP_INTERFACE_CONFIG[i][2];
			}
		}
		if (buttonId == 11015) {
			addExpFromLamp();
		}
	}
	
	public void sendLampInterface() {
		player.getActionSender().sendInterface(2808);
	}
	
	public void sendRandom() {
		randomEvent = new RandomEvent(player, 409);
		randomEvent.npc.getUpdateFlags().sendGraphic(567, 0);
		randomEvent.npc.getUpdateFlags().sendForceMessage("Hello, " + player.getUsername() + "!");
		randomEvent.npc.getUpdateFlags().faceEntity(player.getIndex() + 32768);
		randomEvent.npc.setFollowingEntity(player);
		World.submit(new Tick(3) {
			int tick = 0;
			@Override
			public void execute() {
				tick ++;
				if (tick == 4) {
					randomEvent.npc.getUpdateFlags().sendForceMessage("Enjoy! Goodbye, friend!");
					randomEvent.npc.getUpdateFlags().sendGraphic(567, 0);
					player.getInventory().addItem(new Item(2528, 1));
				}
				if (tick == 5) {
					randomEvent.npc.setVisible(false);
					randomEvent.npc.setFollowingEntity(null);
					World.unregister(randomEvent.npc);
					this.stop();
					randomEvent = null;
				}
			}
		});
	}
	
}