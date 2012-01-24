package com.rs2;

public class Constants {
	
	public static final String SERVER_NAME = "Project Endeavor Beta (Open-Source) - Based on RuneSource";
	public static final String[] MESSAGES_ON_LOGIN =
	{
		"Please refer to the advertisement thread for information on playing."
	};
	
	
	public static final int CLIENT_VERSION = 1;
	
	public static final int EXP_RATE = 1;
	public static final int EXP_EVENT_BONUS = 1;
	public static final int START_X = 3086;
	public static final int START_Y = 3488;
	public static final int START_Z = 0;
	public static final int MAX_NPCS = 8192;
	public static final int NPC_WALK_DISTANCE = 6;
	public static final int MAX_ITEMS = 11791;
	public static final int MAX_ITEM_COUNT = Integer.MAX_VALUE;
	public static final int GROUND_START_TIME = 200;
	public static final int SHOW_ALL_GROUND_ITEMS = 100;
	public static final int LOGIN_RESPONSE_OK = 2;
	public static final int LOGIN_RESPONSE_INVALID_CREDENTIALS = 3;
	public static final int LOGIN_RESPONSE_ACCOUNT_DISABLED = 4;
	public static final int LOGIN_RESPONSE_ACCOUNT_ONLINE = 5;
	public static final int LOGIN_RESPONSE_UPDATED = 6;
	public static final int LOGIN_RESPONSE_WORLD_FULL = 7;
	public static final int LOGIN_RESPONSE_LOGIN_SERVER_OFFLINE = 8;
	public static final int LOGIN_RESPONSE_LOGIN_LIMIT_EXCEEDED = 9;
	public static final int LOGIN_RESPONSE_BAD_SESSION_ID = 10;
	public static final int LOGIN_RESPONSE_PLEASE_TRY_AGAIN = 11;
	public static final int LOGIN_RESPONSE_NEED_MEMBERS = 12;
	public static final int LOGIN_RESPONSE_COULD_NOT_COMPLETE_LOGIN = 13;
	public static final int LOGIN_RESPONSE_SERVER_BEING_UPDATED = 14;
	public static final int LOGIN_RESPONSE_LOGIN_ATTEMPTS_EXCEEDED = 16;
	public static final int LOGIN_RESPONSE_MEMBERS_ONLY_AREA = 17;
	public static final int EQUIPMENT_SLOT_HEAD = 0;
	public static final int EQUIPMENT_SLOT_CAPE = 1;
	public static final int EQUIPMENT_SLOT_AMULET = 2;
	public static final int EQUIPMENT_SLOT_WEAPON = 3;
	public static final int EQUIPMENT_SLOT_CHEST = 4;
	public static final int EQUIPMENT_SLOT_SHIELD = 5;
	public static final int EQUIPMENT_SLOT_LEGS = 7;
	public static final int EQUIPMENT_SLOT_HANDS = 9;
	public static final int EQUIPMENT_SLOT_FEET = 10;
	public static final int EQUIPMENT_SLOT_RING = 12;
	public static final int EQUIPMENT_SLOT_ARROWS = 13;
	public static final int APPEARANCE_SLOT_CHEST = 0;
	public static final int APPEARANCE_SLOT_ARMS = 1;
	public static final int APPEARANCE_SLOT_LEGS = 2;
	public static final int APPEARANCE_SLOT_HEAD = 3;
	public static final int APPEARANCE_SLOT_HANDS = 4;
	public static final int APPEARANCE_SLOT_FEET = 5;
	public static final int APPEARANCE_SLOT_BEARD = 6;
	public static final int GENDER_MALE = 0;
	public static final int GENDER_FEMALE = 1;
	public static final int SHOP_UPDATE_TICK = 10;

	/**
	  * Same index order to get there value.
	  */
	public static final String BONUS_NAME[] = { "Stab", "Slash", "Crush", "Magic", "Range",
			"Stab", "Slash", "Crush", "Magic", "Range", "Strength", "Prayer" 
	};
	
	public static final int[] UNTRADEABLE_ITEMS = {6570};
	
	public static final int[] PACKET_LENGTHS = {
		0, 0, 0, 1, -1, 0, 0, 0, 0, 0, // 0
		0, 0, 0, 0, 8, 0, 6, 2, 2, 0, // 10
		0, 2, 0, 6, 0, 12, 0, 0, 0, 0, // 20
		0, 0, 0, 0, 0, 8, 4, 0, 0, 2, // 30
		2, 6, 0, 6, 0, -1, 0, 0, 0, 0, // 40
		0, 0, 0, 12, 0, 0, 0, 0, 8, 0, // 50
		0, 8, 0, 0, 0, 0, 0, 0, 0, 0, // 60
		6, 0, 2, 2, 8, 6, 0, -1, 0, 6, // 70
		0, 0, 0, 0, 0, 1, 4, 6, 0, 0, // 80
		0, 0, 0, 0, 0, 3, 0, 0, -1, 0, // 90
		0, 13, 0, -1, 0, 0, 0, 0, 0, 0,// 100
		0, 0, 0, 0, 0, 0, 0, 6, 0, 0, // 110
		1, 0, 6, 0, 0, 0, -1, 0, 2, 6, // 120
		0, 4, 6, 8, 0, 6, 0, 0, 0, 2, // 130
		0, 0, 0, 0, 0, 6, 0, 0, 0, 0, // 140
		0, 0, 1, 2, 0, 2, 6, 0, 0, 0, // 150
		0, 0, 0, 0, -1, -1, 0, 0, 0, 0,// 160
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 170
		0, 8, 0, 3, 0, 2, 0, 0, 8, 1, // 180
		0, 0, 12, 0, 0, 0, 0, 0, 0, 0, // 190
		2, 0, 0, 0, 0, 0, 0, 0, 4, 0, // 200
		4, 0, 0, 0, 7, 8, 0, 0, 10, 0, // 210
		0, 0, 0, 0, 0, 0, -1, 0, 6, 0, // 220
		1, 0, 0, 0, 6, 0, 6, 8, 1, 0, // 230
		0, 4, 0, 0, 0, 0, -1, 0, -1, 4,// 240
		0, 0, 6, 6, 0, 0, 0 // 250
	};

}
