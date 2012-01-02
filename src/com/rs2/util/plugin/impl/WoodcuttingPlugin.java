package com.rs2.util.plugin.impl;

import com.rs2.model.players.Item;
import com.rs2.model.players.Player;
import com.rs2.util.plugin.AbstractPlugin;
import com.rs2.net.StreamBuffer;
import com.rs2.net.packet.Packet;

/**
 * A simple test plugin.
 * @author Tommo
 *
 */
public class WoodcuttingPlugin extends AbstractPlugin {
	
	private boolean canTick = false;
	
	private int timer = 0;
	
	private static final int PACKET_CLICK_OBJECT = 132;
	private static final int WOODCUTTING_ANIM_ID = 875;
	private static final int WOODCUTTING_SKILL_ID = 8;
	
	@Override
	public void onCreate() {
	}

	@Override
	public String getName() {
		return "Woodcutting";
	}

	@Override
	public String getAuthor() {
		return "Tommo";
	}

	@Override
	public double getVersion() {
		return 1.0;
	}
	
	@Override
	public boolean canTick() {
		return canTick;
	}

	@Override
	public void onDestroy() {
		System.out.println("Thanks for using woodcutting plugin by tommo.");
	}
	
	@Override
	public void onTick() {
		if (timer >= 0) {
			timer--;
		}
	}
	
	@Override
	public void reset() {
		canTick = false;
	}
	
	@Override
	public boolean onPacketArrival(Player player, Packet packet) {
		if (packet.getOpcode() == PACKET_CLICK_OBJECT) {
			if (player.getClickId() == 1278) {
				canTick = true;
				return false;
			}
		}
		return true;
	}
	
	@Override
	public void onPlayerTick(Player player) {
		if (canTick) {
			if (timer > 0) {
				player.getUpdateFlags().sendAnimation(WOODCUTTING_ANIM_ID, 0); 
			}
			else {
				player.getUpdateFlags().sendAnimation(WOODCUTTING_ANIM_ID, 0); 
				player.getActionSender().sendMessage("You cut the tree.");
				player.getInventory().addItem(new Item(1511));
				player.getSkill().addExp(WOODCUTTING_SKILL_ID, 10000);
				timer = 5;
			}
		}
	}

}







