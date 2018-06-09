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

import com.gmail.sharpcastle33.potions.CustomPotion;

public class AspectRecipeManager {
	
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
	 * Finds the result of an alchemical reaction given the Aspect totals and the amount of Shaman Sap
	 * @param aspectTotals a Map<Aspect, Integer> obtained by the getAspectTotals method in AspectManager
	 * @param amountShamanSap the integer amount of Shaman Sap (binding agent)
	 * @return CustomPotion
	 */
	public static CustomPotion findResult(Map<Aspect, Integer> aspectTotals, int amountShamanSap) {
		
		/*
		 * 1. Obtain Primary Aspect
		 * 2. Shortlist Recipes that Meet Minimums and Match Primary Aspect
		 * 3. Determine Tolerance Values for Shortlisted Recipes
		 * 4. Remove Shortlisted Recipes with Positive Tolerance - Sap Effect
		 * 5. Shortlist Recipes with Lowest Tolerance Value
		 * 6. Choose Randomly of Remaining
		 */
		
		/* Obtain Primary Aspect */ //=================================================================================================================//
		
		List<Aspect> primaries = new ArrayList<>();
		int frequence = 0;
		
		for(Aspect aspect: aspectTotals.keySet()) {
			if(aspectTotals.get(aspect).intValue() > frequence) {
				primaries.clear();
				frequence = aspectTotals.get(aspect).intValue();
				primaries.add(aspect);
			} else if(aspectTotals.get(aspect).intValue() == frequence) {
				primaries.add(aspect);
			} // if/else
		} // for
		
		if(primaries.size() > 1 || primaries.size() == 0) return CustomPotion.MUNDANE_POT; // return due to multiple primary aspects in ingredients
		
		Aspect primary = primaries.get(0);
		
		/* Shortlist Recipes that Meet Minimums and Match Primary Aspect */ //=========================================================================//
		
		List<AspectRecipe> shortlist = new ArrayList<>();
		
		shortlister:
		for(AspectRecipe recipe: recipes) {
			if(recipe.primaryAspect.equals(primary)) {
				for(Aspect aspect: recipe.aspects.keySet()) {
					if(!aspectTotals.containsKey(aspect) && recipe.aspects.get(aspect).intValue() > aspectTotals.get(aspect)) continue shortlister;
				} // for
				shortlist.add(recipe);
			} // if
		} // for
		
		if(shortlist.size() == 0) return CustomPotion.MUNDANE_POT; // No Minimums Met
		
		/* Determine Tolerance Values for Shortlisted Recipes */ //====================================================================================//
		
		Map<AspectRecipe, Integer> tolerancies = new HashMap<>();
		
		for(AspectRecipe recipe: shortlist) {
			for(Aspect aspect: aspectTotals.keySet()) {
				int tolerance = 0;
				if(recipe.aspects.containsKey(aspect)) {
					tolerance += (aspectTotals.get(aspect).intValue() - recipe.aspects.get(aspect).intValue()) * ADDITIONAL_ASPECT_TOLERANCE;
				} else {
					tolerance += aspectTotals.get(aspect).intValue() * BAD_ASPECT_TOLERANCE;
				} // if/else
				tolerancies.put(recipe, tolerance);
			} // for
		} // for
		
		/* Remove Shortlisted Recipes with Positive Tolerance - Sap Effect */ //=======================================================================//
		
		for(AspectRecipe recipe: tolerancies.keySet()) {
			if(tolerancies.get(recipe).intValue() - (amountShamanSap - recipe.time) * SHAMAN_SAP_VALUE > 0) shortlist.remove(recipe);
		} // for
		
		if(shortlist.size() < 1) return CustomPotion.MUNDANE_POT; // if there are no remaining recipes, fail it
		if(shortlist.size() == 1) return shortlist.get(0).result; // if there is a single remaining recipe, that is the result
		
		/* ShortList Recipes with Lowest Tolerance Value */ //=========================================================================================//
		
		List<AspectRecipe> answers = new ArrayList<>();
		int tolerance = 0;
		
		for(AspectRecipe recipe: shortlist) {
			if(tolerancies.get(recipe).intValue() < tolerance) {
				answers.clear();
				tolerance = tolerancies.get(recipe).intValue();
				answers.add(recipe);
			} else {
				answers.add(recipe);
			} // if/else
		} // for
		
		/* Choose Randomly of Remaining */ //==========================================================================================================//
		
		return answers.get((int) (Math.random() * answers.size())).result;
	} // findResult
	
} // class
