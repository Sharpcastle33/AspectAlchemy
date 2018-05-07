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

public class PotionManager {

	static FileConfiguration potionConfig;
	private static Map<CustomPotion, ItemStack> potions;

	public static void init(File configFile) {
		try {
			potionConfig = YamlConfiguration.loadConfiguration(configFile);
			potions = loadPotions(potionConfig);
		} catch (IllegalArgumentException e) {
			Bukkit.getLogger().severe("Potions config does not exist.");
		}
	}

	private static Map<CustomPotion, ItemStack> loadPotions(FileConfiguration config) {
		Map<CustomPotion, ItemStack> configPotions = new HashMap<>();
		for (String potionKey : config.getKeys(false)) {
			configPotions.put(CustomPotion.valueOf(potionKey),
					loadPotionStack(potionConfig.getConfigurationSection(potionKey)));
		}

		return configPotions;
	}

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
	}

	private static ArrayList<PotionEffect> loadPotionEffects(ConfigurationSection effectsSection) {
		ArrayList<PotionEffect> potionEffects = new ArrayList<>();

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
	}

	public static ItemStack getPotion(CustomPotion p) {
		return potions.get(p);
	}

}
