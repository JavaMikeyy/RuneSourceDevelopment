package com.rs2.model.content.combat.ranged;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import com.rs2.util.XStreamUtil;

public class BowLoader {

	@SuppressWarnings("unchecked")
	public static void loadBowDefinitions() throws FileNotFoundException, IOException {
		System.out.println("Loading bow definitions...");
		List<BowDefinition> list = (List<BowDefinition>) XStreamUtil.getxStream().fromXML(new FileInputStream("./data/content/bowdef.xml"));
		int count = 0;
		for (BowDefinition def : list) {
			Ranged.getBowDefinitions()[count] = def;
			Ranged.bowCount ++;
			count ++;
		}
		System.out.println("Loaded " + list.size() + " bow definitions.");
	}
	
}