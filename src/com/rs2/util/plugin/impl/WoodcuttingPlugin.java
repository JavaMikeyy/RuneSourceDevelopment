package com.rs2.util.plugin.impl;

import com.rs2.model.players.Item;
import com.rs2.util.plugin.LocalPlugin;
import com.rs2.net.packet.Packet;

/**
 * A simple test plugin.
 * @author Tommo
 *
 */
public class WoodcuttingPlugin extends LocalPlugin {
	
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
	public boolean onPacketArrival(Packet packet) {
		if (packet.getOpcode() == PACKET_CLICK_OBJECT) {
			/*if (timer > 0) {
				getPlayer().getUpdateFlags().sendAnimation(WOODCUTTING_ANIM_ID, 0); 
			} else {
				getPlayer().getUpdateFlags().sendAnimation(WOODCUTTING_ANIM_ID, 0); 
				getPlayer().getActionSender().sendMessage("You cut the tree.");
				getPlayer().getInventory().addItem(new Item(1511));
				getPlayer().getSkill().addExp(WOODCUTTING_SKILL_ID, 10000);
				timer = 5;
			}*/
		}
		return true;
	}

}







