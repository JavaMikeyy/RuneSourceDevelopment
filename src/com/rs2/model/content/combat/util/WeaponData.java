package com.rs2.model.content.combat.util;

import com.rs2.model.Entity;
import com.rs2.model.players.Player;
import com.rs2.model.players.ItemManager;

public class WeaponData {
	
	public static int getWeaponSpeed(Player player) {
		int speed = 4;
		if (player.getEquipment().getItemContainer().get(3) == null)
			return speed;
		String weaponName = ItemManager.getItemDefinitions()[
				player.getEquipment().getItemContainer().get(3).getId()].getName();
		Object[][] weaponData = {
			{"dart", 2}, {"knife", 2}, {"shortbow", 4}, {"c'bow", 4}, {"dagger", 4}, {"sword", 4},
			{"scimitar", 4}, {"crossbow", 4}, {"toktz-xil-ul", 4}, {"whip", 4}, {"staff", 4}, {"rapier", 4},
			{"toktz-xil-ak", 4}, {"toktz-xil-ek", 4}, {"longsword", 5}, {"mace", 5}, {"hatchet", 5},
			{"axe", 5}, {"spear", 5}, {"pickaxe", 5}, {"torag", 5}, {"guthan", 5}, {"verac", 5}, {"crystal bow", 5},
			{"seercull", 5}, {"battleaxe", 6}, {"warhammer", 6}, {"barrelchest", 6}, {"ahrim's", 6}, {"toktz-mej-tal", 6},
			{"gravite 2h", 6}, {"longbow", 6}, {"javelin", 6}, {"hand cannon", 6}, {"2h", 7}, {"maul", 7}, 
			{"tzharr-ket-om", 7}, {"dharok's", 7}, {"dark bow", 9}
		};
		for (int i = 0; i < weaponData.length; i++)
			if (weaponName.toLowerCase().contains((String) weaponData[i][0]))
				speed = (Integer) weaponData[i][1];
		return speed;
	}
	
	public static int getAttackAnimation(Player player) {
		int animation = 422;
		if (player.getEquipment().getItemContainer().get(3) == null)
			return animation;
		String weaponName = ItemManager.getItemDefinitions()[
				player.getEquipment().getItemContainer().get(3).getId()].getName();
		Object[][] weaponData = {
			{"scimitar", 451}, {"dagger", 400}, {"dragon dagger", 402}, {"drag dagger", 402}, 
			{"longsword", 451}, {"2h", 407}, {"halberd", 440}, {"whip", 1658}
		};
		for (int i = 0; i < weaponData.length; i++)
			if (weaponName.toLowerCase().contains((String) weaponData[i][0]))
				animation = (Integer) weaponData[i][1];
		return animation;
	}
	
	public static int getBlockAnimation(Player player) {
		int animation = 404;
		if (player.getEquipment().getItemContainer().get(5) == null) {
			if (player.getEquipment().getItemContainer().get(3) == null)
				return animation;
			String weaponName = ItemManager.getItemDefinitions()[
					player.getEquipment().getItemContainer().get(3).getId()].getName();
			Object[][] weaponData = {
				{"whip", 1659}
			};
			for (int i = 0; i < weaponData.length; i++)
				if (weaponName.toLowerCase().contains((String) weaponData[i][0]))
					animation = (Integer) weaponData[i][1];
		}
		else {
			String shieldName = ItemManager.getItemDefinitions()[
					player.getEquipment().getItemContainer().get(5).getId()].getName();
			Object[][] shieldData = {
				{"shield", 403}
			};
			for (int i = 0; i < shieldData.length; i++)
				if (shieldName.toLowerCase().contains((String) shieldData[i][0]))
					animation = (Integer) shieldData[i][1];
		}
		return animation;
	}
	
	public static int getWalkOrRunAnimation(Player player, boolean isRunning) {
		int returnAnim = 0x333;
		Object[][] weaponData = {
			{"whip", 1660, 1661}, {"halberd", 1146, 1210}, {"guthan", 1146, 1210}, {"dharok", 0x67F, 0x680},
			{"ahrim", 1146, 1210}, {"verac", 1830, 1831}, {"wand", 1146, 1210}, {"staff", 1146, 1210},
			{"bow", 819, 824}, {"tzharr-ket-om", 2064, 1664}, {"granite maul", 1663, 1664}, {"karil", 2076, 2077}
		};
		if (player.getEquipment().getItemContainer().get(3) == null)
			if(!isRunning)
				return returnAnim;
			else
				return 0x338;
		if (isRunning)
			returnAnim = 0x338;
		String weaponName = ItemManager.getItemDefinitions()[
				player.getEquipment().getItemContainer().get(3).getId()].getName();
		for (int i = 0; i < weaponData.length; i++)
			if (weaponName.toLowerCase().contains((String) weaponData[i][0])) {
				if (!isRunning)
					returnAnim = (Integer) weaponData[i][1];
				else
					returnAnim = (Integer) weaponData[i][2];
			}
		return returnAnim;
	}
	
	public static int getStandAnimation(Player player) {
		int returnAnim = 0x328;
		Object[][] weaponData = {
			{"whip", 1832}, {"halberd", 809}, {"guthan", 809}, {"wand", 809}, {"staff", 809},
			{"dharok", 0x811}, {"verac", 1832}, {"karil", 2074}, {"2h", 809}, {"bow", 808},
			{"granite maul", 1662}, {"dragon longsword", 809}, {"tzharr-ket-om", 0x811}
		};
		if (player.getEquipment().getItemContainer().get(3) == null)
			return 0x338;
		String weaponName = ItemManager.getItemDefinitions()[
				player.getEquipment().getItemContainer().get(3).getId()].getName();
		for (int i = 0; i < weaponData.length; i++)
			if (weaponName.toLowerCase().contains((String) weaponData[i][0])) {
				returnAnim = (Integer) weaponData[i][1];
			}
		return returnAnim;
	}
	
	public static boolean isTwoHanded(int itemId) {
		String weaponName = ItemManager.getItemDefinitions()[itemId].getName();
		String[] twoHandedData = {
			"maul", "2h"
		};
		for (int i = 0; i < twoHandedData.length; i++)
			if (weaponName.toLowerCase().contains(twoHandedData[i]))
				return true;
		return false;	
	}
	
	public static int getWeaponInterface(Player player) {
		int weaponInterface = 5855;
		if (player.getEquipment().getItemContainer().get(3) == null)
				return weaponInterface;
		String weaponName = ItemManager.getItemDefinitions()[
				player.getEquipment().getItemContainer().get(3).getId()].getName();
		Object[][] weaponData = {
			{"whip", 12290}, {"bow", 1764}, {"stand", 328}, {"wand", 328}, {"dart", 4446}, {"knife", 4446},
			{"javelin", 4446}, {"dagger", 2276}, {"sword", 2276}, {"scimitar", 2276}, {"pickaxe", 5570}, {"axe", 1698}, {"battleaxe", 1698},
			{"halberd", 8460}, {"halberd", 8460}, {"spear", 4679}, {"mace", 3796}, {"warhammer", 425}, {"maul", 425}
		};
		for (int i = 0; i < weaponData.length; i++)
			if (weaponName.toLowerCase().contains((String) weaponData[i][0]))
				weaponInterface = (Integer) weaponData[i][1];
		return weaponInterface;
	}
	
}