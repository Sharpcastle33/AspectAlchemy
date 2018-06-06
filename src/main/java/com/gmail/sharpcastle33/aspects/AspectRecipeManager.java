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
	
	public static final int MINIMUM_ASPECTS = 3; // minimum number of aspects required for an alembic reaction to succeed
	
	// NOTE: Tolerance must be NONPOSITIVE for an alchemical reaction to succeed
	public static final int ADDITIONAL_ASPECT_TOLERANCE = 1; // number added to tolerance for each additional aspect of a correct type (by the recipe) beyond the base requirement
	public static final int BAD_ASPECT_TOLERANCE = 2; // number added to tolerance for each additional aspect of an incorrect type (by the recipe)
	public static final int SHAMAN_SAP_VALUE = 2; // magnitude of sap effect on tolerance
	
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
			CustomPotion result = CustomPotion.valueOf(recipeSection.getString("result"));
			
			configRecipes.add(new AspectRecipe(aspectMap, primAspect, bindingAgent, result));
		}

		return configRecipes;
	}
	
	/**
	 * Finds the result of an alchemical reaction given the aspects present and the amount of Shaman Sap
	 * @param aspects the list of all aspects present
	 * @param amountShamanSap the integer amount of Shaman Sap (binding agent)
	 * @return CustomPotion
	 */
	public static CustomPotion findResult(List<Aspect> aspects, int amountShamanSap) {
		HashMap<Aspect, Integer> frequency = new HashMap<>();				// HashMap to determine frequency of each aspect in the recipe
		for(Aspect aspect: aspects) {										// for loop gets all the aspects in the recipe and assigns their frequency
			if(!frequency.containsKey(aspect)) frequency.put(aspect, 0);
			frequency.put(aspect, frequency.get(aspect).intValue() + 1);
		} // for
		
		if(frequency.size() < MINIMUM_ASPECTS) return CustomPotion.MUNDANE_POT; // fail due to too few aspects
		
		int highest = 0;
		for(Aspect aspect: frequency.keySet()) if(frequency.get(aspect).intValue() > highest) highest = frequency.get(aspect).intValue(); // gets the highest frequency in the recipe
		
		int numOfHighest = 0;
		Aspect primary = null;
		for(Aspect aspect: frequency.keySet()) if(frequency.get(aspect).intValue() == highest) { // for loop determines how many aspects have the highest frequency
			numOfHighest++;																		 // and assigns the primary aspect
			primary = aspect;
		} // for
		
		if(numOfHighest > 1) return CustomPotion.MUNDANE_POT;	// there is no determinable primary aspect and the recipe collapses into a mundane pot
		if(numOfHighest == 1) {	// there is a determinable primary aspect
			List<AspectRecipe> validRecipes = new ArrayList<>();
			for(AspectRecipe recipe: recipes) if(recipe.primaryAspect.equals(primary)) { // for all recipes with the same primary recipe
				for(Aspect aspect: recipe.aspects.keySet()) { // if all the aspect minimums are met
					if(!frequency.containsKey(aspect) || frequency.get(aspect).intValue() < recipe.aspects.get(aspect).intValue()) continue;
					validRecipes.add(recipe); // add all valid recipe
				} // for
			} // for
			if(validRecipes.size() == 0) return CustomPotion.MUNDANE_POT; // if there are no valid recipes the reaction collapses into a mundane pot
			
			List<AspectRecipe> confirmedRecipes = new ArrayList<>(); // list of all recipes it could be, and if it contains ANY then the recipe will succeed to one of these
			HashMap<AspectRecipe, Integer> toleranceValues = new HashMap<>(); // hashmap of recipes to tolerance values
			int tolerance = 0; // positive bad, negative good
			for(AspectRecipe recipe: validRecipes) { 				// for each 
				for(Aspect aspect: frequency.keySet()) {
					if(recipe.aspects.containsKey(aspect)) {
						tolerance += (frequency.get(aspect).intValue() - recipe.aspects.get(aspect).intValue()) * ADDITIONAL_ASPECT_TOLERANCE;
					} else {
						tolerance += frequency.get(aspect).intValue() * BAD_ASPECT_TOLERANCE;
					} // if/else
				} // for
				
				toleranceValues.put(recipe, tolerance);
				
				amountShamanSap -= recipe.time;					// account for minimum shaman sap (time requirement int base recipe)
				int sapEffect = amountShamanSap * SHAMAN_SAP_VALUE;	// multiply in sap effect
				
				if(tolerance - sapEffect <= 0) confirmedRecipes.add(recipe); // if tolerance minus sapEffect is less than or equal to zero, we have a winner!
				amountShamanSap += recipe.time;
			} // for
			
			if(confirmedRecipes.size() == 0) return CustomPotion.MUNDANE_POT; // tolerance was positive for all possible 
			
			if(confirmedRecipes.size() == 1) return confirmedRecipes.get(0).result; // if there is only one confirmed recipe, return that
			
			if(confirmedRecipes.size() > 1) { // if there is >1, the one with the lowest tolerance without SapEffect, and if the same then random
				int lowest = 9999999;
				for(AspectRecipe recipe: confirmedRecipes) if(toleranceValues.get(recipe).intValue() < lowest) lowest = toleranceValues.get(recipe).intValue();
				
				List<AspectRecipe> results = new ArrayList<>();
				for(AspectRecipe recipe: confirmedRecipes) if(toleranceValues.get(recipe).intValue() == lowest) results.add(recipe);
				
				if(results.size() == 1) {
					return results.get(0).result;                     // get result, last remaining
				} else {
					int res = (int) (Math.random() * results.size()); // randomly choose
					return results.get(res).result;                   // get result
				} // if/else
			} // if
			
		} // if
		
		return CustomPotion.MUNDANE_POT;
	} // findResult
	
}
