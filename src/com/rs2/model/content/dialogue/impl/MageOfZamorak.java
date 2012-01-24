package com.rs2.model.content.dialogue.impl;

import com.rs2.model.World;
import com.rs2.model.tick.Tick;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.BankManager;
import com.rs2.model.players.ShopManager;
import com.rs2.util.Misc;

/**
  * By Mikey` of Rune-Server
  */

public class MageOfZamorak {

	public static void sendDialogue(final Player player, int dialogueId) {
		switch (dialogueId) {
			case 1:
				player.getDialogue().sendNpcChat1("How can I help a fellow Zamorak follower?", player.getDialogue().CONTENT);
				player.getDialogue().setNextDialogue(2);
				break;
			case 2:
				player.getDialogue().sendOption3("Can you teleport me to the abyss?",
						"Can I see your shop?", "I'm not a Zamorak follower!");
				player.getDialogue().setOptionId(5);
				break;
			case 3:
				player.getDialogue().sendPlayerChat1("Can you teleport me to the abyss?", player.getDialogue().CONTENT);
				player.getDialogue().setNextDialogue(4);
				break;
			case 4:
				final Npc npc = World.getNpcs()[player.getNpcClickIndex()];
				player.getActionSender().removeInterfaces();
				World.submit(new Tick(1) {
					@Override
					public void execute() {
						npc.getUpdateFlags().faceEntity(player.getIndex() + 32768);
						npc.getUpdateFlags().sendAnimation(1818, 0);
						player.getUpdateFlags().sendAnimation(1816, 0);
						stop();
					}
				});
				World.submit(new Tick(4) {
				@Override
				public void execute() {
					player.sendTeleport(3040, 4844, 0);
					stop();
				}
			});
				break;
			case 5:
				player.getDialogue().sendPlayerChat1("Can I see your shop?", player.getDialogue().CONTENT);
				player.getDialogue().setNextDialogue(6);
				break;
			case 6:
				ShopManager.openShop(player, 10);
				break;
			case 7:
				player.getDialogue().sendPlayerChat1("I'm not a Zamorak follower!", player.getDialogue().CONTENT);
				player.getDialogue().setNextDialogue(8);
				break;
			case 8:
				player.getDialogue().sendNpcChat1("Then get out of my sight!", player.getDialogue().MAD);
				player.getDialogue().endDialogue();
				break;
			
		}
	}
	
}
