package com.gmail.sharpcastle33.aspects;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Manages the aspects.yaml file which is used to match custom items to a list
 * of Aspect enums of various quantaties
 */
public class AspectManager {

	static FileConfiguration aspectsConfig;
	static Map<String, AspectItemData> itemAspects;

	/**
	 * Initialization method. Used to load the aspects.yaml file and initialize the
	 * itemAspects Map
	 * 
	 * @param configFile
	 */
	public static void init(File configFile) {
		try {
			aspectsConfig = YamlConfiguration.loadConfiguration(configFile);
			itemAspects = loadAspectValues(aspectsConfig);
		} catch (IllegalArgumentException e) {
			Bukkit.getLogger().severe("Aspects config not loaded");
			e.printStackTrace();
		}
	}

	/**
	 * Gets the Aspect data for all items
	 * 
	 * @return
	 */
	public static Map<String, AspectItemData> getLoadedItemAspects() {
		return itemAspects;
	} // init

	/**
	 * Loads the aspect values for each item in config section "items".
	 * 
	 * @param config
	 */
	private static Map<String, AspectItemData> loadAspectValues(FileConfiguration config) {
		// Construct new map to load into from configs
		Map<String, AspectItemData> itemAspects = new HashMap<String, AspectItemData>();
		
		Bukkit.getLogger().info("Loading Item Aspects...");

		// Iterate over each item in section
		for (String key : config.getKeys(false)) {
			ConfigurationSection itemSection = config.getConfigurationSection(key);

			// Get item data
			String name = itemSection.getString("name");

			// Get aspect map from aspect section of item
			Map<String, Object> configAspects = itemSection.getConfigurationSection("aspects").getValues(false);

			// Transform the config map
			Map<Aspect, Integer> itemAspectMap = new HashMap<Aspect, Integer>();
			for (String aspect : configAspects.keySet()) {
				itemAspectMap.put(Aspect.valueOf(aspect), (Integer) configAspects.get(aspect));
			}

			// Organize data and put into instance level map of aspect data
			AspectItemData data = new AspectItemData(key, name, itemAspectMap);
			itemAspects.put(key, data);
			
			Bukkit.getLogger().info("Loaded Aspects for "+data.displayName);
		} // for
		
		return itemAspects;
	} // loadAspectValues

	/**
	 * Get aspect totals for an array of ItemStack
	 * 
	 * @param arr
	 *            Array of ItemStack
	 * @return Map of Aspect to Integer (frequency of each aspect in the itemstack)
	 */
	public static Map<Aspect, Integer> getAspectTotals(ItemStack[] arr) {

		// Map to populate with aspect data
		Map<Aspect, Integer> ret = new HashMap<Aspect, Integer>();

		// Loop through stacks
		for (ItemStack i : arr) {

			// Get aspects from the stack and add them to the return map
			if (getAspects(i) != null) {
				Map<Aspect, Integer> temp = getAspects(i);
				for (Aspect a : temp.keySet()) {
					if (ret.get(a) != null) {
						ret.put(a, ret.get(a) + temp.get(a));
					} else {
						ret.put(a, temp.get(a)*i.getAmount());
					}
				}
			}
		}
		return ret;
	} // getAspectTotals

	/**
	 * Get aspect values from an ItemStack
	 * 
	 * @param stack
	 * @return Map of aspect values keyed by aspect
	 */
	public static Map<Aspect, Integer> getAspects(ItemStack stack) {
		// Check if aspects have been loaded
		if (itemAspects == null) return null;
		
		if (stack == null) return null;

		if (!stack.hasItemMeta()) return null;
		// Get item meta data from ItemStack
		ItemMeta meta = stack.getItemMeta();
		
		for(AspectItemData aspect: itemAspects.values()) {
			if(meta.getDisplayName().equals(aspect.displayName)) {
				return new HashMap<Aspect, Integer>(aspect.aspects);
			} // if
		} // for

		// If there was no match return null
		return null;
	} // getAspects
}
