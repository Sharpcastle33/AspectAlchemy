package com.gmail.sharpcastle33.aspects;

import java.util.Map;

import com.gmail.sharpcastle33.potions.CustomPotion;

/**
 * Holds the recipe for a given CustomPotion
 */
public class AspectRecipe {

	public Map<Aspect, Integer> aspects;
	public Aspect primaryAspect;
	public int time;
	public CustomPotion result;

	public AspectRecipe(Map<Aspect, Integer> aspectMap, Aspect primaryAspect, int time, CustomPotion result) {
		this.aspects = aspectMap;
		this.primaryAspect = primaryAspect;
		this.time = time;
		this.result = result;
	}

}
