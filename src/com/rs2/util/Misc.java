package com.rs2.util;
/*
 * This file is part of RuneSource.
 *
 * RuneSource is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RuneSource is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RuneSource.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.Locale;

import com.rs2.model.Position;

/**
 * A collection of miscellaneous utility methods and constants.
 * 
 * @author blakeman8192
 */
public class Misc {
	
	private static char xlateTable[] = { ' ', 'e', 't', 'a', 'o', 'i', 'h', 'n', 's', 
		'r', 'd', 'l', 'u', 'm', 'w', 'c', 'y', 'f', 'g', 'p', 'b', 'v', 'k', 'x', 'j', 
		'q', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ' ', '!', '?', '.', 
		',', ':', ';', '(', ')', '-', '&', '*', '\\', '\'', '@', '#', '+', '=', '\243', 
		'$', '%', '"', '[', ']' 
	};
	
	private static char decodeBuf[] = new char[4096];

	public static int hexToInt(byte[] data) {
		int value = 0;
		int n = 1000;
		for (int i = 0; i < data.length; i++) {
			int num = (data[i] & 0xFF) * n;
			value += (int) num;
			if (n > 1) {
				n = n / 1000;
			}
		}
		return value;
	}

	public static String textUnpack(byte packedData[], int size) {
		int idx = 0, highNibble = -1;
		for (int i = 0; i < size * 2; i++) {
			int val = packedData[i / 2] >> (4 - 4 * (i % 2)) & 0xf;
			if (highNibble == -1) {
				if (val < 13)
					decodeBuf[idx++] = xlateTable[val];
				else
					highNibble = val;
			} else {
				decodeBuf[idx++] = xlateTable[((highNibble << 4) + val) - 195];
				highNibble = -1;
			}
		}

		return new String(decodeBuf, 0, idx);
	}

	/**
	 * Returns the delta coordinates. Note that the returned Position is not an
	 * actual position, instead it's values represent the delta values between
	 * the two arguments.
	 * 
	 * @param a
	 *            the first position
	 * @param b
	 *            the second position
	 * @return the delta coordinates contained within a position
	 */
	public static Position delta(Position a, Position b) {
		return new Position(b.getX() - a.getX(), b.getY() - a.getY());
	}
	
	public static int getDistance(Position a, Position b) {
		int deltaX = b.getX() - a.getX();
		int deltaY = b.getY() - a.getY();
		return ((int) Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2)));
	}

	/**
	 * Calculates the direction between the two coordinates.
	 * 
	 * @param dx
	 *            the first coordinate
	 * @param dy
	 *            the second coordinate
	 * @return the direction
	 */
	public static int direction(int dx, int dy) {
		if (dx < 0) {
			if (dy < 0) {
				return 5;
			} else if (dy > 0) {
				return 0;
			} else {
				return 3;
			}
		} else if (dx > 0) {
			if (dy < 0) {
				return 7;
			} else if (dy > 0) {
				return 2;
			} else {
				return 4;
			}
		} else {
			if (dy < 0) {
				return 6;
			} else if (dy > 0) {
				return 1;
			} else {
				return -1;
			}
		}
	}
	
	public static int direction(int srcX, int srcY, int destX, int destY) {
		int dx = destX - srcX, dy = destY - srcY;
		// a lot of cases that have to be considered here ... is there a more
		// sophisticated (and quick!) way?
		if (dx < 0) {
			if (dy < 0) {
				if (dx < dy)
					return 11;
				else if (dx > dy)
					return 9;
				else
					return 10; // dx == dy
			} else if (dy > 0) {
				if (-dx < dy)
					return 15;
				else if (-dx > dy)
					return 13;
				else
					return 14; // -dx == dy
			} else { // dy == 0
				return 12;
			}
		} else if (dx > 0) {
			if (dy < 0) {
				if (dx < -dy)
					return 7;
				else if (dx > -dy)
					return 5;
				else
					return 6; // dx == -dy
			} else if (dy > 0) {
				if (dx < dy)
					return 1;
				else if (dx > dy)
					return 3;
				else
					return 2; // dx == dy
			} else { // dy == 0
				return 4;
			}
		} else { // dx == 0
			if (dy < 0) {
				return 8;
			} else if (dy > 0) {
				return 0;
			} else { // dy == 0
				return -1; // src and dest are the same
			}
		}
	}

	public static String formatNumber(double number) {
		NumberFormat format = NumberFormat.getIntegerInstance(Locale.US);
		return format.format(number);
	}

	/**
	 * A simple logging utility that prefixes all messages with a timestamp.
	 * 
	 * @author blakeman8192
	 */
	public static class TimestampLogger extends PrintStream {

		private BufferedWriter writer;
		private DateFormat df = new SimpleDateFormat();

		/**
		 * The OutputStream to log to.
		 * 
		 * @param out
		 */
		public TimestampLogger(OutputStream out, String file) throws IOException {
			super(out);
			writer = new BufferedWriter(new FileWriter(file, true));
		}
		
		public TimestampLogger(OutputStream out) {
			super(out);
		}

		@Override
		public void println(String msg) {
			msg = "[" + df.format(new Date()) + "]: " + msg;
			super.println(msg);
			log(msg);
		}

		/**
		 * Logs the message to the log file.
		 * 
		 * @param msg
		 *            the message
		 */
		private void log(String msg) {
			try {
				if (writer == null)
					return;
				writer.write(msg);
				writer.newLine();
				writer.flush();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}

	/**
	 * A simple timing utility.
	 * 
	 * @author blakeman8192
	 */
	public static class Stopwatch {

		/** The cached time. */
		private long time = System.currentTimeMillis();

		/**
		 * Resets this stopwatch.
		 */
		public void reset() {
			time = System.currentTimeMillis();
		}

		/**
		 * Returns the amount of time elapsed (in milliseconds) since this
		 * object was initialized, or since the last call to the "reset()"
		 * method.
		 * 
		 * @return the elapsed time (in milliseconds)
		 */
		public long elapsed() {
			return System.currentTimeMillis() - time;
		}
	}
	
	public static String intToString(int intToChange) {
		if (intToChange == 1)
			return "first";
		else if (intToChange == 2)
			return "second";
		else if (intToChange == 3)
			return "third";
		else if (intToChange == 4)
			return "fourth";
		return "first";
	}
	
	/**
	  *Generates a random number between 0 and the range
	  */
	public static int randomNumber(int range) {
		return (int)(java.lang.Math.random() * (range));
	}
	
	public static int getDayOfYear() {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
		int days = 0;
		int[] daysOfTheMonth = {
				31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31
		};
		if ((year % 4 == 0) && (year % 100 != 0) || (year % 400 == 0)) {
			daysOfTheMonth[1] = 29;
		}
		days += c.get(Calendar.DAY_OF_MONTH);
		for (int i = 0; i < daysOfTheMonth.length; i++) {
			if (i < month)
				days += daysOfTheMonth[i];
		}
		return days;
	}
	
	public static int getYear() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.YEAR);
	}

}
