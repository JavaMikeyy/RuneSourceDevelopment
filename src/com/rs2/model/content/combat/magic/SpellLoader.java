package com.rs2.model.content.combat.magic;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import com.rs2.util.XStreamUtil;

public class SpellLoader {

	@SuppressWarnings("unchecked")
	public static void loadSpellDefinitions() throws FileNotFoundException, IOException {
		System.out.println("Loading spell definitions...");
		List<SpellDefinition> list = (List<SpellDefinition>) XStreamUtil.getxStream().fromXML(new FileInputStream("./data/content/combat/spelldef.xml"));
		int count = 0;
		for (SpellDefinition def : list) {
			Magic.getSpellDefinitions()[count] = def;
			String spellName = def.getSpellName();
			Magic.spellCount ++;
			count ++;
		}
		System.out.println("Loaded " + list.size() + " spell definitions.");
	}
	
}