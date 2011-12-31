package com.rs2.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.util.List;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.rs2.util.Misc;


/**
  * By Mikey` of Rune-Server
  * - This was MOSTLY taken from this tutorial: http://www.mkyong.com/tutorials/java-xml-tutorials/
  */
public class PunishmentManager {
	
	/** 
	  * The path of the punishment file.
	  */
	private static final String PUNISHMENT_FILE = "data/punishments/punishments.xml";
	
	/** 
	  * Checking to see if the player is punished.
	  */
	public static boolean getPunishmentStatus(String username, int macAddress, String ipAddress, Punishments checkPunishmentStatus) {
		SAXBuilder builder = new SAXBuilder();
		File file = new File(PUNISHMENT_FILE);
		try {
			Document document = (Document) builder.build(file);
			Element rootNode = document.getRootElement();
			List list = rootNode.getChildren("player");
			for (int i = 0; i < list.size(); i++) {
				Element node = (Element) list.get(i);
				Element ban = node.getChild("ban");
				Element address = node.getChild("address");
				if (ban.getChildText("addressBanned").equalsIgnoreCase("true") && checkPunishmentStatus == Punishments.BAN) {
					if (address.getChildText("ipAddress").equalsIgnoreCase("" + ipAddress) || 
					address.getChildText("macAddress").equalsIgnoreCase("" + macAddress))
					return true;
				}
				Element mute = node.getChild("mute");
				if (mute.getChildText("addressMuted").equalsIgnoreCase("true") && checkPunishmentStatus == Punishments.MUTE) {
					if (address.getChildText("ipAddress").equalsIgnoreCase("" + ipAddress) || 
					address.getChildText("macAddress").equalsIgnoreCase("" + macAddress))
						return true;
				}
				if (node.getChildText("username").equalsIgnoreCase(username)) {
					if (checkPunishmentStatus == Punishments.BAN) {
						if (ban.getChildText("banned").equalsIgnoreCase("true")) {
							if (punishmentExpired(username, checkPunishmentStatus, ban.getChildText("daysBanApplied"), 
							Integer.parseInt(ban.getChildText("dayOfBan")), Integer.parseInt(ban.getChildText("yearOfBan")))) {
								return false;
							}
							return true;
						}
						return false;
					}
					else if (checkPunishmentStatus == Punishments.MUTE) {
						if (mute.getChildText("muted").equalsIgnoreCase("true")) {
							if (punishmentExpired(username, checkPunishmentStatus, mute.getChildText("daysMuteApplied"), 
							Integer.parseInt(mute.getChildText("dayOfMute")), Integer.parseInt(mute.getChildText("yearOfMute")))) {
								return false;
							}
							return true;
						}
						return false;
					}
				}
			}
			addToPunishmentList(username, macAddress, ipAddress);
		} 
		catch (IOException io) {
			System.out.println(io.getMessage());
		} 
		catch (JDOMException jdomex) {
			System.out.println(jdomex.getMessage());
		}
		return false;
	}
	
	/** 
	  * Applying or removing punishments.
	  */
	public static void appendPunishment(String username, Punishments checkPunishmentStatus, boolean statusUpdate, Object daysApplied) {
		try {
			SAXBuilder builder = new SAXBuilder();
			File xmlFile = new File(PUNISHMENT_FILE);
			Document doc = (Document) builder.build(xmlFile);
			Element rootNode = doc.getRootElement();
			List list = rootNode.getChildren("player");
			for (int i = 0; i < list.size(); i++) {
				Element node = (Element) list.get(i);
				if (node.getChildText("username").equalsIgnoreCase(username)) {
					if (checkPunishmentStatus == Punishments.BAN) {
						Element ban = node.getChild("ban");
						ban.getChild("banned").setText("" + statusUpdate);
						if (!statusUpdate) {
							ban.getChild("daysBanApplied").setText("0");
							ban.getChild("dayOfBan").setText("0");
							ban.getChild("yearOfBan").setText("0");
						}
						else {
							ban.getChild("daysBanApplied").setText("" + daysApplied);
							ban.getChild("dayOfBan").setText("" + Misc.getDayOfYear());
							ban.getChild("yearOfBan").setText("" + Misc.getYear());
						}
					}
					else if (checkPunishmentStatus == Punishments.MUTE) {
						Element mute = node.getChild("mute");
						mute.getChild("muted").setText("" + statusUpdate);
						if (!statusUpdate) {
							mute.getChild("daysMuteApplied").setText("0");
							mute.getChild("dayOfMute").setText("0");
							mute.getChild("yearOfMute").setText("0");
						}
						else {
							mute.getChild("daysMuteApplied").setText("" + daysApplied);
							mute.getChild("dayOfMute").setText("" + Misc.getDayOfYear());
							mute.getChild("yearOfMute").setText("" + Misc.getYear());
						}
					}
					else if (checkPunishmentStatus == Punishments.ADDRESS_BAN) {
						Element addressBan = node.getChild("ban");
						addressBan.getChild("addressBanned").setText("" + statusUpdate);
						if (!statusUpdate) {
							addressBan.getChild("daysBanApplied").setText("0");
							addressBan.getChild("dayOfBan").setText("0");
							addressBan.getChild("yearOfBan").setText("0");
						}
						else {
							addressBan.getChild("daysBanApplied").setText("" + daysApplied);
							addressBan.getChild("dayOfBan").setText("" + Misc.getDayOfYear());
							addressBan.getChild("yearOfBan").setText("" + Misc.getYear());
						}
					}
					else if (checkPunishmentStatus == Punishments.ADDRESS_MUTE) {
						Element addressMute = node.getChild("mute");
						addressMute.getChild("addressMuted").setText("" + statusUpdate);
						if (!statusUpdate) {
							addressMute.getChild("daysMuteApplied").setText("0");
							addressMute.getChild("dayOfMute").setText("0");
							addressMute.getChild("yearOfMute").setText("0");
						}
						else {
							addressMute.getChild("daysMuteApplied").setText("" + daysApplied);
							addressMute.getChild("dayOfMute").setText("" + Misc.getDayOfYear());
							addressMute.getChild("yearOfMute").setText("" + Misc.getYear());
						}
					}
				}
			}
			XMLOutputter xmlOutput = new XMLOutputter();
			xmlOutput.setFormat(Format.getPrettyFormat());
			xmlOutput.output(doc, new FileWriter(xmlFile));
		} 
		catch (IOException io) {
			io.printStackTrace();
		} 
		catch (JDOMException e) {
			e.printStackTrace();
		}
	}
	
	/** 
	  * Checking to see if punishments have expired.
	  */
	private static boolean punishmentExpired(String username, Punishments checkPunishmentStatus, String daysApplied, int dayOf, int yearOf) {
		int currentYear = Misc.getYear();
		int daysPunished;
		if (daysApplied.equalsIgnoreCase("week"))
			daysPunished = 7;
		else if (daysApplied.equalsIgnoreCase("month"))
			daysPunished = 30;
		else if (daysApplied.equalsIgnoreCase("year"))
			daysPunished = 365;
		else if (daysApplied.equalsIgnoreCase("x"))
			return false;
		else
			daysPunished = Integer.parseInt(daysApplied);
		if ((Misc.getDayOfYear() - dayOf) > daysPunished && yearOf == currentYear) {
			appendPunishment(username, checkPunishmentStatus, false, 0);
			return true;
		}
		else if (yearOf != currentYear) {
			if (Misc.getDayOfYear() + (365 - dayOf) > daysPunished) {
				appendPunishment(username, checkPunishmentStatus, false, 0);
				return true;
			}
		}
		return false;
	}
	
	/** 
	  * Adding players to the punishment list (happens on first login)
	  */
	public static void addToPunishmentList(String username, int macAddress, String ipAddress) {
		try {
			SAXBuilder builder = new SAXBuilder();
			File file = new File(PUNISHMENT_FILE);
			Document doc = (Document) builder.build(file);
			Element root = doc.getRootElement();
			
			/** Adding a new child to the root element */
			Element index = new Element("player");
			root.addContent(index);
			/** Adding a new child to the index element */
			Element name = new Element("username");
			name.addContent("" + username);
			index.addContent(name);
			
			/** Adding a new child to the index element */
			Element ban = new Element("ban");
			index.addContent(ban);
			/** Adding a new child to the ban element */
			Element banned = new Element("banned");
			banned.addContent("false");
			ban.addContent(banned);
			/** Adding a new child to the ban element */
			Element addressBanned = new Element("addressBanned");
			addressBanned.addContent("false");
			ban.addContent(addressBanned);
			/** Adding a new child to the ban element */
			Element daysBanApplied = new Element("daysBanApplied");
			daysBanApplied.addContent("0");
			ban.addContent(daysBanApplied);
			/** Adding a new child to the ban element */
			Element dayOfBan = new Element("dayOfBan");
			dayOfBan.addContent("0");
			ban.addContent(dayOfBan);
			/** Adding a new child to the ban element */
			Element yearOfBan = new Element("yearOfBan");
			yearOfBan.addContent("0");
			ban.addContent(yearOfBan);
			
			/** Adding a new child to the index element */
			Element mute = new Element("mute");
			index.addContent(mute);
			/** Adding a new child to the mute element */
			Element muted = new Element("muted");
			muted.addContent("false");
			mute.addContent(muted);
			/** Adding a new child to the ban element */
			Element addressMuted = new Element("addressMuted");
			addressMuted.addContent("false");
			mute.addContent(addressMuted);
			/** Adding a new child to the mute element */
			Element daysMuteApplied = new Element("daysMuteApplied");
			daysMuteApplied.addContent("0");
			mute.addContent(daysMuteApplied);
			/** Adding a new child to the mute element */
			Element dayOfMute = new Element("dayOfMute");
			dayOfMute.addContent("0");
			mute.addContent(dayOfMute);
			/** Adding a new child to the mute element */
			Element yearOfMute = new Element("yearOfMute");
			yearOfMute.addContent("0");
			mute.addContent(yearOfMute);
			
			/** Adding a new child to the index element */
			Element address = new Element("address");
			index.addContent(address);
			/** Adding a new child to the addressBan element */
			Element clientAddress = new Element("macAddress");
			clientAddress.addContent("" + macAddress);
			address.addContent(clientAddress);
			/** Adding a new child to the addressBan element */
			Element userAddress = new Element("ipAddress");
			userAddress.addContent("" + ipAddress);
			address.addContent(userAddress);
			
			XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
			outputter.output(doc, new FileWriter(PUNISHMENT_FILE));
		}
		catch (IOException io) {
			System.out.println(io.getMessage());
		}
		catch (JDOMException e) {
			e.printStackTrace();
		}
	}

	public static enum Punishments {
		BAN, MUTE, ADDRESS_BAN, ADDRESS_MUTE
	}

}