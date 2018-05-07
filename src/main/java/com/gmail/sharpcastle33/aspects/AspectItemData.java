package com.gmail.sharpcastle33.aspects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;

public class AspectItemData {
	public String configName;
	public String displayName;
	public Map<Aspect, Integer> aspects;
	public Material itemMaterial;
	public List<String> itemLore;

	public AspectItemData(String name, String displayName, Map<Aspect, Integer> aspects, Material material,
			List<String> lore) {
		this.configName = name;
		this.displayName = displayName;
		this.aspects = aspects;
		this.itemMaterial = material;
		this.itemLore = lore;
	}

	public AspectItemData() {
		this(null, null, new HashMap<Aspect, Integer>(), null, new ArrayList<String>());
	}
}
