package com.gmail.sharpcastle33.aspects;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import com.gmail.sharpcastle33.potions.CustomPotion;
import com.gmail.sharpcastle33.potions.PotionManager;

public class AspectRecipeManager {
	
	private static FileConfiguration config;
	public static List<AspectRecipe> recipes;
	
	public static void init(File configFile){
		try {
			config = YamlConfiguration.loadConfiguration(configFile);
			recipes = loadRecipes(config);
			Bukkit.getLogger().info("DEBUGGING: " + recipes.get(0).primaryAspect);
		} catch (IllegalArgumentException e) {
			Bukkit.getLogger().severe("Recipes config does not exist.");
			e.printStackTrace();
		}
	}
	
	private static ArrayList<AspectRecipe> loadRecipes(FileConfiguration config) {
		ArrayList<AspectRecipe> configRecipes = new ArrayList<AspectRecipe>();
		for(String recipeKey : config.getKeys(false)) {
			ConfigurationSection recipeSection = config.getConfigurationSection(recipeKey);
			
			Map<String, Object> configAspectMap = recipeSection.getConfigurationSection("aspects").getValues(false);

			// Transform the config map
			Map<Aspect, Integer> aspectMap = new HashMap<Aspect, Integer>();
			for (String aspect : configAspectMap.keySet()) {
				aspectMap.put(Aspect.valueOf(aspect), (Integer) configAspectMap.get(aspect));
			}
			
			Aspect primAspect = Aspect.valueOf(recipeSection.getString("primary_aspect"));
			int bindingAgent = recipeSection.getInt("binding_agent");
			ItemStack result = PotionManager.getPotion(CustomPotion.valueOf(recipeSection.getString("result")));
			
			configRecipes.add(new AspectRecipe(aspectMap, primAspect, bindingAgent, result));
		}

		return configRecipes;
	}
	
	public AspectRecipeManager() {

	}
}
