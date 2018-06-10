package com.gmail.sharpcastle33.util;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;

/**
 * Utility for creating customized potions
 */
public class PotionUtil {

	/**
	 * Variants of potions enum
	 */
	public static enum PotionVariant {
		SPLASH, LINGERING, POTION
	}

	/**
	 * Creates potion item stack.
	 * 
	 * @param name
	 *            Display name of potion item
	 * @param effects
	 *            ArrayList of potion effects
	 * @param variant
	 *            Type of potion. (PotionUtil.PotionVariant.SPLASH | LINGERING |
	 *            POTION)
	 * @param color
	 *            Color of potion item
	 * @return
	 */
	public static ItemStack createPotion(String name, ArrayList<PotionEffect> effects, PotionVariant variant,
			Color color) {

		// Create appropriate potion item stack
		ItemStack potionStack;
		switch (variant) {
		case SPLASH:
			potionStack = new ItemStack(Material.SPLASH_POTION);
			break;
		case LINGERING:
			potionStack = new ItemStack(Material.LINGERING_POTION);
			break;
		default:
			potionStack = new ItemStack(Material.POTION);
			break;
		}

		// Get potion meta data and set accordingly
		PotionMeta meta = potionStack.hasItemMeta() ? (PotionMeta) potionStack.getItemMeta()
				: (PotionMeta) Bukkit.getItemFactory().getItemMeta(Material.POTION);

		meta.setDisplayName(name);
		meta.setColor(color != null ? color : Color.BLUE);

		for (PotionEffect effect : effects) {
			meta.addCustomEffect(effect, false);
		}

		potionStack.setItemMeta(meta);

		return potionStack;
	} // createPotion(String, ArrayList<PotionEffect>, PotionVariant, Color)

	/**
	 * Creates potion ItemStack with default color of BLUE
	 * 
	 * @param name
	 *            Display name of potion item
	 * @param effects
	 *            ArrayList of potion effects
	 * @param variant
	 *            Type of potion. (PotionUtil.PotionVariant.SPLASH | LINGERING |
	 *            POTION)
	 * @return
	 */
	public static ItemStack createPotion(String name, ArrayList<PotionEffect> effects, PotionVariant variant) {

		// Create appropriate potion item stack
		ItemStack potionStack;
		switch (variant) {
		case SPLASH:
			potionStack = new ItemStack(Material.SPLASH_POTION);
			break;
		case LINGERING:
			potionStack = new ItemStack(Material.LINGERING_POTION);
			break;
		default:
			potionStack = new ItemStack(Material.POTION);
			break;
		}

		// Get potion meta data and set accordingly
		PotionMeta meta = potionStack.hasItemMeta() ? (PotionMeta) potionStack.getItemMeta()
				: (PotionMeta) Bukkit.getItemFactory().getItemMeta(Material.POTION);

		meta.setDisplayName(name);
		meta.setColor(Color.BLUE);

		for (PotionEffect effect : effects) {
			meta.addCustomEffect(effect, false);
		}

		potionStack.setItemMeta(meta);

		return potionStack;
	} // createPotion(String, ArrayList<PotionEffect>, PotionVariant)

	/**
	 * Creates potion ItemStack with default of regular potion variant
	 * 
	 * @param name
	 *            Display name of potion item
	 * @param effects
	 *            ArrayList of potion effects
	 * @param color
	 *            Color of potion item
	 * @return
	 */
	public static ItemStack createPotion(String name, ArrayList<PotionEffect> effects, Color color) {

		// Create appropriate potion item stack
		ItemStack potionStack = new ItemStack(Material.POTION);

		// Get potion meta data and set accordingly
		PotionMeta meta = potionStack.hasItemMeta() ? (PotionMeta) potionStack.getItemMeta()
				: (PotionMeta) Bukkit.getItemFactory().getItemMeta(Material.POTION);

		meta.setDisplayName(name);
		meta.setColor(color);

		for (PotionEffect effect : effects) {
			meta.addCustomEffect(effect, false);
		}

		potionStack.setItemMeta(meta);

		return potionStack;
	} // createPotion(String, ArrayList<PotionEffect>, Color)

	/**
	 * Creates potion ItemStack with default of regular potion variant and BLUE
	 * color
	 * 
	 * @param name
	 *            Display name of potion item
	 * @param effects
	 *            ArrayList of potion effects
	 * @return
	 */
	public static ItemStack createPotion(String name, ArrayList<PotionEffect> effects) {

		// Create appropriate potion item stack
		ItemStack potionStack = new ItemStack(Material.POTION);

		// Get potion meta data and set accordingly
		PotionMeta meta = potionStack.hasItemMeta() ? (PotionMeta) potionStack.getItemMeta()
				: (PotionMeta) Bukkit.getItemFactory().getItemMeta(Material.POTION);

		meta.setDisplayName(name);
		meta.setColor(Color.BLUE);

		for (PotionEffect effect : effects) {
			meta.addCustomEffect(effect, false);
		}

		potionStack.setItemMeta(meta);

		return potionStack;
	} // createPotion(String, ArrayList<PotionEffect>)
} // class
