package com.gmail.sharpcastle33.aspects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

//import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

public class AspectManager {

	// Map of aspect data per item keyed by name in configuration
	Map<String, AspectData> itemAspects;

	/**
	 * Loads the aspect values for each item in config section "items".
	 * @param config
	 */
	public void loadAspectValues(FileConfiguration config) {
		// Construct new map to load into from configs
		itemAspects = new HashMap<String, AspectData>();

		// Get items section from config
		ConfigurationSection items = config.getConfigurationSection("items");
		if (items != null) {
			
			// Iterate over each item in section
			for (String key : items.getKeys(false)) {
				ConfigurationSection itemSection = items.getConfigurationSection(key);

				// Get item data
				ItemStack itemStack = ((List<ItemStack>) itemSection.getList("package")).get(0);
				ItemMeta meta = itemStack.hasItemMeta() ? itemStack.getItemMeta() : null;
				
				// Get aspect map from aspect section of item
				Map<String, Object> configAspects = itemSection.getConfigurationSection("aspects").getValues(false);

				// Transform the config map
				Map<Aspect, Integer> itemAspectMap = new HashMap<Aspect, Integer>();
				for (String aspect : configAspects.keySet()) {
					itemAspectMap.put(Aspect.valueOf(aspect), (Integer) configAspects.get(aspect));
				}

				// Organize data and put into instance level map of aspect data
				AspectData data = new AspectData(
						key,
						meta.hasDisplayName() ? meta.getDisplayName() : null,
						itemAspectMap,
						itemStack.getType(),
						meta.hasLore() ? meta.getLore() : null
					);
				itemAspects.put(key, data);
			}
		}
	}

	// Get aspect totals for item stack array
	public Map<Aspect, Integer> getAspectTotals(ItemStack[] arr) {

		// Map to populate with aspect data
		Map<Aspect, Integer> ret = new HashMap<Aspect, Integer>();

		// Loop through stacks
		for (ItemStack i : arr) {

			// Get aspects from the stack and add them to the return map
			if (getAspects(i) != null) {
				Map<Aspect, Integer> temp = getAspects(i);
				for (Aspect a : temp.keySet()) {
					ret.put(a, ret.get(a) + temp.get(a));
				}
			}
		}
		return ret;
	}

	
	/**
	 * Get aspect values from an ItemStack.
	 * @param stack
	 * @return Map of aspect values keyed by aspect
	 */
	public Map<Aspect, Integer> getAspects(ItemStack stack) {
		
		// Check if aspects have been loaded
		if (itemAspects == null) {
			return null;
		}

		// Get item meta data from ItemStack
		ItemMeta meta = stack.hasItemMeta() ? stack.getItemMeta() : null;

		// Get data that is used
		Material stackMaterial = stack.getType();
		String stackDisplayName = meta.hasDisplayName() ? meta.getDisplayName() : null;
		List<String> stackLore = meta.hasLore() ? meta.getLore() : null;

		// Iterate through all loaded items
		for (AspectData aspect : itemAspects.values()) {
			
			// Check if display name and material match
			if (aspect.displayName.equals(stackDisplayName) && aspect.itemMaterial == stackMaterial) {
				// Assume that lore match before checking
				boolean itemLoreMatch = true;
				
				// Figure out whether or not lores match
				if (stackLore == null && aspect.itemLore != null) {
					itemLoreMatch = false;
				} else if (stackLore == null && aspect.itemLore == null) {
					itemLoreMatch = true;
				} else {
					// Check if the lore lists have the same entries.
					for (int i = 0; i < stackLore.size(); i++) {
						if (!stackLore.contains(aspect.itemLore.get(i))) {
							itemLoreMatch = false;
						}
					} 
				}
				
				// If the lore matches, return a map with aspects and values
				if (itemLoreMatch) {
					Map<Aspect, Integer> stackAspects = new HashMap<Aspect, Integer>();
					for (Aspect a : aspect.aspects.keySet()) {
						stackAspects.put(a, stack.getAmount() * aspect.aspects.get(a));
					}
					return stackAspects;
				}
			}
		}
		
		// If there was no match return null
		return null;
	}

	/**
	 * Aspect and item data for reading data from config.
	 * @author adamd
	 *
	 */
	// Might want seperate ApsectConfig class, analogous to hidden ores' BlockConfig, ToolConfig, etc.
	private class AspectData {
		String configName;
		String displayName;
		Map<Aspect, Integer> aspects;
		Material itemMaterial;
		List<String> itemLore;

		public AspectData(String name, String displayName, Map<Aspect, Integer> aspects, Material material,
				List<String> lore) {
			this.configName = name;
			this.displayName = displayName;
			this.aspects = aspects;
			this.itemMaterial = material;
			this.itemLore = lore;
		}

		public AspectData() {
			this(null, null, new HashMap<Aspect, Integer>(), null, new ArrayList<String>());
		}
	}
}
