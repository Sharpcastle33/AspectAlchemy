package com.gmail.sharpcastle33.potions;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.gmail.sharpcastle33.util.ColorUtil;
import com.gmail.sharpcastle33.util.PotionUtil;
import com.gmail.sharpcastle33.util.PotionUtil.PotionVariant;

/**
 * Manages the potions.yaml which holds the details and effects of all
 * CustomPotions, and allows for matching CustomPotion enums with their
 * ItemStack counterparts
 * 
 * @author adamdusty
 */
public class PotionManager {

	static FileConfiguration potionConfig;
	private static Map<CustomPotion, ItemStack> potions;

	/**
	 * Initialization method. Loads the potions.yaml and initialized the potions Map
	 * 
	 * @param configFile
	 *            File representing potions.yaml
	 */
	public static void init(File configFile) {
		try {
			potionConfig = YamlConfiguration.loadConfiguration(configFile);
			potions = loadPotions(potionConfig);
		} catch (IllegalArgumentException e) {
			Bukkit.getLogger().severe("Potions config does not exist.");
			e.printStackTrace();
		}
	} // init

	/**
	 * Called by initialization method, loads the potions from the configuration
	 * into a Map
	 * 
	 * @param config
	 *            FileConfiguration loaded potions.yaml configuration
	 * @return Map of CustomPotion to ItemStack
	 */
	private static Map<CustomPotion, ItemStack> loadPotions(FileConfiguration config) {
		Map<CustomPotion, ItemStack> configPotions = new HashMap<CustomPotion, ItemStack>();
		for (String potionKey : config.getKeys(false)) {
			configPotions.put(CustomPotion.valueOf(potionKey),
					loadPotionStack(potionConfig.getConfigurationSection(potionKey)));
		}

		return configPotions;
	} // loadPotions

	/**
	 * Called by loadPotions method, loads the details of a given subsection of the
	 * potion config
	 * 
	 * @param potionSection
	 *            ConfigurationSection section representing the potion
	 * @return ItemStack represented by the section
	 */
	private static ItemStack loadPotionStack(ConfigurationSection potionSection) {
		Color color = null;
		PotionVariant variant = null;

		if (potionSection.getString("variant") != null) {
			variant = PotionVariant.valueOf(potionSection.getString("variant"));
		}

		if (potionSection.getString("color") != null) {
			color = ColorUtil.colors.get(potionSection.getString("color").toUpperCase());
		}

		ItemStack potion = PotionUtil.createPotion(potionSection.getString("display_name"),
				loadPotionEffects(potionSection.getConfigurationSection("effects")), variant, color);
		return potion;
	} // loadPotionStack

	/**
	 * Called by loadPotionStack method, loads the specific effects of the potion
	 * 
	 * @param effectsSection
	 *            ConfigurationSection subsection representing the effects of the
	 *            potion
	 * @return ArrayList of PotionEffect
	 */
	private static ArrayList<PotionEffect> loadPotionEffects(ConfigurationSection effectsSection) {
		ArrayList<PotionEffect> potionEffects = new ArrayList<PotionEffect>();

		for (String effectKey : effectsSection.getKeys(false)) {
			ConfigurationSection effect = effectsSection.getConfigurationSection(effectKey);

			PotionEffectType type;
			int duration;
			int amplifier;
			boolean ambient;
			boolean particles;
			Color particleColor;

			type = PotionEffectType.getByName(effect.getString("type"));
			duration = effect.getInt("duration", 0);
			amplifier = effect.getInt("amplifier", 0);
			ambient = effect.getBoolean("ambient", false);
			particles = effect.getBoolean("particles", false);
			particleColor = particles ? ColorUtil.colors.get(effect.getString("color").toUpperCase()) : null;

			potionEffects.add(new PotionEffect(type, duration, amplifier, ambient, particles, particleColor));
		}

		return potionEffects;
	} // loadPotionEffects

	/**
	 * Gets the ItemStack correspondent to a given CustomPotion enum
	 * 
	 * @param p
	 *            CustomPotion desired
	 * @return ItemStack potion
	 */
	public static ItemStack getPotion(CustomPotion p) {
		return potions.get(p);
	} // getPotion

} // class
