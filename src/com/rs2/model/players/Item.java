package com.rs2.model.players;

import com.rs2.model.players.Equipment.EquipmentDefinition;
import com.rs2.model.players.ItemManager.ItemDefinition;


/**
 * Represents a single item.
 * @author Graham Edgecombe
 *
 */
public class Item {
	
	/**
	 * The id.
	 */
	private int id;
	
	/**
	 * The number of items.
	 */
	private int count;
	
	/**
	 * Creates a single item.
	 * @param id The id.
	 */
	public Item(int id) {
		this(id, 1);
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	/**
	 * Creates a stacked item.
	 * @param id The id.
	 * @param count The number of items.
	 * @throws IllegalArgumentException if count is negative.
	 */
	public Item(int id, int count) {
		if(count < 0) {
			throw new IllegalArgumentException("Count cannot be negative.");
		}
		this.id = id;
		this.count = count;
	}
	
	/**
	 * Gets the item id.
	 * @return The item id.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Gets the count.
	 * @return The count.
	 */
	public int getCount() {
		return count;
	}
	
	public ItemDefinition getDefinition() {
		return ItemManager.getInstance().getItemDefinitions()[id];
	}
	
	public EquipmentDefinition getEquipmentDefintion() {
		return ItemManager.getInstance().getEquipmentDefinitions()[id];
	}
	
	@Override
	public String toString() {
		return Item.class.getName() + " [id=" + id + ", count=" + count + "]";
	}

}
