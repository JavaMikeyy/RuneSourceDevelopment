package com.rs2.model.content;

import com.rs2.model.World;
import com.rs2.model.tick.Tick;
import com.rs2.model.players.Player;
import com.rs2.util.clip.Region;
import com.rs2.util.Misc;
import com.rs2.util.Areas;

/**
  * By Mikey` of Rune-Server
  */

public class Teleportation {

	private static final int[][] TELEPORTS =
	{
	//Varrock
	{4140, 3086, 3488, 0}, {4143, 2804, 3434, 0}
	};
	
	private boolean teleportDeclared = false;
	
	private Player player;
	
	public Teleportation(Player player) {
		this.player = player;
	}
	
	public void teleportTick() {
		if (teleportDeclared) {
			if (teleTimer == 0) {
				player.sendTeleport(x, y, height);
				if (player.getMagicBookType() == Player.MagicBookTypes.MODERN) {
					player.getUpdateFlags().sendAnimation(715, 1);
				}
				else if (player.getMagicBookType() == Player.MagicBookTypes.ANCIENT) {
					player.getUpdateFlags().sendGraphic(455, 1);
					player.getUpdateFlags().sendAnimation(715, 1);
				}
				player.getAttributes().put("canTakeDamage", Boolean.TRUE);
				teleportDeclared = false;
			}
			else {
				teleTimer --;
			}
		}
	}
	
	public int x, y, height, teleTimer;
	
	public void teleport(final int x, final int y, final int height) {
		if (!teleportDeclared) {
			teleportDeclared = true;
			if (player.getMagicBookType() == Player.MagicBookTypes.MODERN) {
				player.getUpdateFlags().sendHighGraphic(308, 47);
				player.getUpdateFlags().sendAnimation(714, 0);
			}
			else if (player.getMagicBookType() == Player.MagicBookTypes.ANCIENT) {
				player.getUpdateFlags().sendGraphic(392, 0);
				player.getUpdateFlags().sendAnimation(1979, 0);
			}
			player.getAttributes().put("canTakeDamage", Boolean.FALSE);
			this.x = x;
			this.y = y;
			this.height = height;
			teleTimer = 3;
		}
	}
	
	public void activateTeleportButton(int buttonId) {
		if (Areas.getWildernessLevel(player) < 21) {
			if (!teleportDeclared) {
				for(int i = 0; i < TELEPORTS.length; i++) {
					if (buttonId == TELEPORTS[i][0]) {
						int teleX = TELEPORTS[i][1] + Misc.randomNumber(3);
						int teleY = TELEPORTS[i][2] + Misc.randomNumber(3);
						while (Region.getClipping(teleX, teleY, TELEPORTS[i][3]) != 0) {
							teleX = TELEPORTS[i][1] + Misc.randomNumber(3);
							teleY = TELEPORTS[i][2] + Misc.randomNumber(3);
						}
						teleport(teleX, teleY, TELEPORTS[i][3]);
						return;
					}
				}
			}
		}
	}
	
}
