package com.rs2.util;

import com.rs2.model.Position;
import com.rs2.model.Entity;

public class Areas {
	
	public static boolean inWilderness(Position pos) {
		return pos.getX() >= 2944 && pos.getX() < 3392 && pos.getY() >= 3520 && 
			pos.getY() < 4026;
	}
	
	public static boolean inMultiArea(Position pos) {
		return pos.getX() >= 3029 && pos.getX() <= 3374 && pos.getY() >= 3759 && pos.getY() <= 3903
		|| (pos.getX() >= 2250 && pos.getX() <= 2280 && pos.getY() >= 4670 && pos.getY() <= 4720)
		|| (pos.getX() >= 3198 && pos.getX() <= 3380 && pos.getY() >= 3904 && pos.getY() <= 3970)
		|| (pos.getX() >= 3191 && pos.getX() <= 3326 && pos.getY() >= 3510 && pos.getY() <= 3759)
		|| (pos.getX() >= 2987 && pos.getX() <= 3006 && pos.getY() >= 3912 && pos.getY() <= 3937)
		|| (pos.getX() >= 2245 && pos.getX() <= 2295 && pos.getY() >= 4675 && pos.getY() <= 4720)
		|| (pos.getX() >= 2450 && pos.getX() <= 3520 && pos.getY() >= 9450 && pos.getY() <= 9550)
		|| (pos.getX() >= 3006 && pos.getX() <= 3071 && pos.getY() >= 3602 && pos.getY() <= 3710)
		|| (pos.getX() >= 3134 && pos.getX() <= 3192 && pos.getY() >= 3519 && pos.getY() <= 3646);
	}
	
	public static int getWildernessLevel(Entity entity) {
		if (!inWilderness(entity.getPosition())) {
			return 0;
		}
		else {
			return ((entity.getPosition().getY() - 3520) / 8) + 1;
		}
	}
}
