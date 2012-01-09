package com.rs2.util.clip;

import java.io.*;
import java.util.zip.*;

import com.rs2.model.Position;


public class PathFinder {

    /**
	  * Determines the NPCs simple follow path
	  * - This will only block the NPC from moving when an object blocks it, not search for a path.
	  */
	public static int checkDirection(Position position, int direction,  boolean alternateRoute) {
		boolean blocked = false;
		int newDirection = direction;
		/*switch (direction) {
			case 0:
				blocked = Region.blockedNorthWest(position.getX(), position.getY(), 0, false);
				if (blocked && !alternateRoute)
					newDirection = 3;
				else if (blocked && alternateRoute)
					newDirection = 1;
				break;
			case 1:
				blocked = Region.blockedNorth(position.getX(), position.getY(), 0, false);
				break;
			case 2:
				blocked = Region.blockedNorthEast(position.getX(), position.getY(), 0, false);
				if (blocked && !alternateRoute)
					newDirection = 4;
				else if (blocked && alternateRoute)
					newDirection = 1;
				break;
			case 3:
				blocked = Region.blockedWest(position.getX(), position.getY(), 0, false);
				break;
			case 4:
				blocked = Region.blockedEast(position.getX(), position.getY(), 0, false);
				break;
			case 5:
				blocked = Region.blockedSouthWest(position.getX(), position.getY(), 0, false);
				if (blocked && !alternateRoute)
					newDirection = 3;
				else if (blocked && alternateRoute)
					newDirection = 6;
				break;
			case 6:
				blocked = Region.blockedSouth(position.getX(), position.getY(), 0, false);
				break;
			case 7:
				blocked = Region.blockedSouthEast(position.getX(), position.getY(), 0, false);
				if (blocked && !alternateRoute)
					newDirection = 4;
				else if (blocked && alternateRoute)
					newDirection = 6;
				break;
		}*/
		return newDirection;
	}
	
	public static boolean clipAllowsAttack(Position attackerPosition, Position defenderPosition) {
		int xModifier = defenderPosition.getX() - attackerPosition.getX();
		int yModifier = defenderPosition.getY() - attackerPosition.getY();
		return ((Region.tileClipped(attackerPosition, xModifier, 
					yModifier, attackerPosition.getZ(), false)));
	}
}


