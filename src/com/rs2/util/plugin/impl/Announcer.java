package com.rs2.util.plugin.impl;

import com.rs2.model.World;
import com.rs2.model.players.Player;
import com.rs2.util.plugin.GlobalPlugin;

public class Announcer extends GlobalPlugin {
	
	private static final String MESSAGE = "Support open-source development.";
	private static final String MESSAGE2 = "Donate to metallic_mike@yahoo.com (via paypal).";
	private int timer = 0;
	
	@Override
	public void onTick() {
		if (timer >= 0) {
			timer--;
			return;
		}
		for (Player p : World.getPlayers()) {
			if (p != null) {
				log("Announcing...");
				p.getActionSender().sendMessage(MESSAGE);
				p.getActionSender().sendMessage(MESSAGE2);
				timer = 50;
			}
		}
	}

	@Override
	public String getName() {
		return "Announcer";
	}

	@Override
	public String getAuthor() {
		return "Tommo";
	}

	@Override
	public double getVersion() {
		return 1.0;
	}

}
