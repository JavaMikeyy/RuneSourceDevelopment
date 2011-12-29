package com.rs2.model.players;

import com.rs2.model.Position;

public class GroundItem {
	
	private String owner;
	private Item item;
	private Position pos;
	private boolean respawn;
	private boolean isGlobal;
	private int time;
	
	public GroundItem(String owner, Item item, Position pos, boolean respawn) {
		this.owner = owner;
		this.item = item;
		this.pos = pos;
		this.respawn = respawn;
	}
	
	public void setRespawn(boolean groundSpawn) {
		this.respawn = groundSpawn;
	}

	public boolean isRespawn() {
		return respawn;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Item getItem() {
		return item;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getOwner() {
		return owner;
	}

	public void setGlobal(boolean isGlobal) {
		this.isGlobal = isGlobal;
	}

	public boolean isGlobal() {
		return isGlobal;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getTime() {
		return time;
	}

	public void setPos(Position pos) {
		this.pos = pos;
	}

	public Position getPos() {
		return pos;
	}

}

