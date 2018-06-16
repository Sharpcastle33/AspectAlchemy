package com.gmail.sharpcastle33;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

/**
 * Allows for easy change to a variety of constants in AspectAlchemy without 
 * @author KingVictoria
 */
public class Constants {	

	/* AspectRecipeManager */
	
	// NOTE: Tolerance must be NONPOSITIVE for an alchemical reaction to succeed
	public static int ADDITIONAL_ASPECT_TOLERANCE = 1; // number added to tolerance for each additional aspect of a correct type (by the recipe) beyond the base requirement
	public static int BAD_ASPECT_TOLERANCE = 2; // number added to tolerance for each additional aspect of an incorrect type (by the recipe)
	public static int SHAMAN_SAP_VALUE = 6; // magnitude of sap effect on tolerance
	
	
	/* Debug Commands */
	
	public static String INSUFFICIENT_PERMISSIONS = ChatColor.RED + "You do not have permission to use this command!";
	
	
	/* AlembicHandler */
	
	public static int INGREDIENTS_MINIMUM = 3;
	public static int ALEMBIC_TICK_TIME = 1200; // 1200 MC Ticks in 1 minute
	
	public static String SHAMAN_SAP_NAME = ChatColor.YELLOW + "Shaman Sap";
	
	public static String NOT_ENOUGH_INGREDIENTS_MSG = ChatColor.RED + "You must have at least " + INGREDIENTS_MINIMUM + " different types of ingredients for an alchemical reaction!";
	public static String NOT_ENOUGH_WATER_BOTTLES_MSG = ChatColor.RED + "You must have all three brewing stand slots filled with water bottles to begin an alchemical reaction!";
	public static String NOT_ENOUGH_FUEL_MSG = ChatColor.RED + "You must have some coal in the Alembic Bellows in order to begin an alchemical reaction!";
	public static String NOT_ENOUGH_SAP_MSG = ChatColor.RED + "No " + SHAMAN_SAP_NAME + ChatColor.RED + "! Alechmy not started.";
	
	
	/* Admin Tools */
	
	public static String DEBUG_ITEM = ChatColor.RED + "Admin Debug Tool";
	public static String ASPECT_ADMIN_TOOL = ChatColor.RED + "Secret Admin Aspect Tool";
	public static String POTION_ADMIN_TOOL = ChatColor.RED + "Secret Admin Potion Tool";
	public static String INSTANT_ADMIN_TOOL = ChatColor.RED + "Secret Admin Brewing Tool";
	
	
	/* Alembic Interactions */
	
	public static String IN_PROGRESS_MESSAGE = ChatColor.RED + "Alembics cannot be broken while in progress!";
	
	public static String ALEMBIC_ITEM_NAME = ChatColor.YELLOW + "Alembic";
	public static String ALEMBIC_CONSTRUCTION = ChatColor.RED + "Constructing Alembic...";
	public static String ALEMBIC_CHEST_NAME = ChatColor.BLUE + "Alembic Chamber";
	public static String ALEMBIC_FURNACE_NAME = ChatColor.BLUE + "Alembic Bellows";
	public static String ALEMBIC_BREWINGSTAND_NAME = ChatColor.BLUE + "Alembic Stand";
	
	public static String IN_PROGRESS_INVENTORY_MESSAGE = ChatColor.RED + "Inventories of Alembics cannot be modified while they are in progress!";
	public static String ENDER_PEARL_ERROR = ChatColor.RED + "The magics in this item conflict with the energies inside the Alembic.";
	
	
	/* Thaumaturgical Resonator */
	
	public static Material RESONATOR_ITEM = Material.WATCH;
	
	public static String RESONATOR_NAME = ChatColor.YELLOW + "Thaumaturgical Resonator";
	
	public static String NO_ASPECTS = ChatColor.RED + "This item does not appear to influence the resonator.";
	public static String SMALL_ASPECTS = ChatColor.YELLOW + "The dial hovers for a moment, then falters. This item contains a small amount of alchemical energy.";
	public static String MEDIUM_ASPECTS = ChatColor.GOLD + "The dial hovers steadily above the baseline. This item certainly contains alchemical properties.";
	public static String LARGE_ASPECTS = ChatColor.BLUE + "The resonator thrums with energy, and the dial flails wildly. This item must contain very powerful energies.";
	
	/**
	 * Gets the constants set in the config
	 * @param config FileConfiguration
	 * @param plugin AspectAlchemy Plugin
	 */
	public static void load(FileConfiguration config, Plugin plugin) {
		if(!config.isConfigurationSection("item_materials"))	populateItemMaterials(config, plugin);
		loadItemMaterials(config);
		if(!config.isConfigurationSection("constants")) 		populateConstants(config, plugin);
		loadConstants(config);
		if(!config.isConfigurationSection("names"))				populateNames(config, plugin);
		loadNames(config);
		if(!config.isConfigurationSection("messages")) 			populateMessages(config, plugin);
		loadMessages(config);
	} // load
	
	/**
	 * Loads vars from the item_materials config section
	 * @param config FileConfiguration
	 */
	private static void loadItemMaterials(FileConfiguration config) {
		ConfigurationSection itemMaterials = config.getConfigurationSection("item_materials");
		
		RESONATOR_ITEM = Material.valueOf(itemMaterials.getString("resonator_item"));
	} // loadItemMaterials
	
	/**
	 * Loads vars from the constants config section
	 * @param config FileConfiguration
	 */
	private static void loadConstants(FileConfiguration config) {
		ConfigurationSection constants = config.getConfigurationSection("constants");
		
		ADDITIONAL_ASPECT_TOLERANCE = constants.getInt("additional_aspect_tolerance");
		BAD_ASPECT_TOLERANCE = constants.getInt("bad_aspect_tolerance");
		SHAMAN_SAP_VALUE = constants.getInt("shaman_sap_value");
		
		INGREDIENTS_MINIMUM = constants.getInt("ingredients_minimum");
		ALEMBIC_TICK_TIME = constants.getInt("alembic_tick_time");
	} // loadConstants
	
	/**
	 * Loads vars from the names config section
	 * @param config FileConfiguration
	 */
	private static void loadNames(FileConfiguration config) {
		ConfigurationSection names = config.getConfigurationSection("names");
		
		SHAMAN_SAP_NAME = names.getString("shaman_sap_name");
		
		DEBUG_ITEM = names.getString("debug_item");
		ASPECT_ADMIN_TOOL = names.getString("aspect_admin_tool");
		POTION_ADMIN_TOOL = names.getString("potion_admin_tool");
		INSTANT_ADMIN_TOOL = names.getString("instant_admin_tool");
		
		ALEMBIC_ITEM_NAME = names.getString("alembic_item_name");
		ALEMBIC_CHEST_NAME = names.getString("alembic_chest_name");
		ALEMBIC_FURNACE_NAME = names.getString("alembic_furnace_name");
		ALEMBIC_BREWINGSTAND_NAME = names.getString("alembic_brewingstand_name");
		
		RESONATOR_NAME = names.getString("resonator_name");
	} // loadNames
	
	/**
	 * Loads vars from the messages config section
	 * @param config FileConfiguration
	 */
	private static void loadMessages(FileConfiguration config) {
		ConfigurationSection messages = config.getConfigurationSection("messages");
		
		messages.getString("insufficient_permissions", ChatColor.RED + "You do not have permission to use this command!");
		
		messages.getString("not_enough_ingredients_msg");
		messages.getString("not_enough_water_bottles_msg");
		messages.getString("not_enough_fuel_msg");
		messages.getString("not_enough_sap_msg");
		
		messages.getString("in_progress_message");
		messages.getString("alembic_construction");
		messages.getString("in_progress_inventory_message");
		messages.getString("ender_pearl_error");
		
		messages.getString("no_aspects");
		messages.getString("small_aspects");
		messages.getString("medium_aspects");
		messages.getString("large_aspects");
	} // loadMessages
	
	/**
	 * Populates the item_materials config section if it does not exist
	 * @param config FileConfiguration
	 * @param plugin Plugin AspectAlchemy
	 */
	private static void populateItemMaterials(FileConfiguration config, Plugin plugin) {
		HashMap<String, Material> itemMaterials = new HashMap<>();
		
		itemMaterials.put("resonator_item", Material.WATCH);
		
		config.createSection("item_materials", itemMaterials);
		plugin.saveConfig();
	} // populateItemMaterials
	
	/**
	 * Populates the constants config section if it does not exist
	 * @param config FileConfiguration
	 * @param plugin Plugin AspectAlchemy
	 */
	private static void populateConstants(FileConfiguration config, Plugin plugin) {
		HashMap<String, Integer> constants = new HashMap<>();
		
		constants.put("additional_aspect_tolerance", 1);
		constants.put("bad_aspect_tolerance", 2);
		constants.put("shaman_sap_value", 6);
		
		constants.put("ingredients_minimum", 3);
		constants.put("alembic_tick_time", 1200);
		
		config.createSection("constants", constants);
		plugin.saveConfig();
	} // populateConstants
	
	/**
	 * Populates the names config section if it does not exist
	 * @param config FileConfiguration
	 * @param plugin Plugin AspectAlchemy
	 */
	private static void populateNames(FileConfiguration config, Plugin plugin) {
		HashMap<String, String> names = new HashMap<>();
		
		names.put("shaman_sap_name", ChatColor.YELLOW + "Shaman Sap");
		
		names.put("debug_item", ChatColor.RED + "Admin Debug Tool");
		names.put("aspect_admin_tool", ChatColor.RED + "Secret Admin Aspect Tool");
		names.put("potion_admin_tool", ChatColor.RED + "Secret Admin Potion Tool");
		names.put("instant_admin_tool", ChatColor.RED + "Secret Admin Brewing Tool");
		
		names.put("alembic_item_name", ChatColor.YELLOW + "Alembic");
		names.put("alembic_chest_name", ChatColor.BLUE + "Alembic Chamber");
		names.put("alembic_furnace_name", ChatColor.BLUE + "Alembic Bellows");
		names.put("alembic_brewingstand_name", ChatColor.BLUE + "Alembic Stand");
		
		names.put("resonator_name", ChatColor.YELLOW + "Thaumaturgical Resonator");
		
		config.createSection("names", names);
		plugin.saveConfig();
	} // populateNames
	
	/**
	 * Populates the messages config section if it does not exist
	 * @param config FileConfiguration
	 * @param plugin Plugin AspectAlchemy
	 */
	private static void populateMessages(FileConfiguration config, Plugin plugin) {
		HashMap<String, String> messages = new HashMap<>();
		
		messages.put("insufficient_permissions", ChatColor.RED + "You do not have permission to use this command!");
		
		messages.put("not_enough_ingredients_msg", ChatColor.RED + "You must have at least " + INGREDIENTS_MINIMUM + " different types of ingredients for an alchemical reaction!");
		messages.put("not_enough_water_bottles_msg", ChatColor.RED + "You must have all three brewing stand slots filled with water bottles to begin an alchemical reaction!");
		messages.put("not_enough_fuel_msg", ChatColor.RED + "You must have some coal in the Alembic Bellows in order to begin an alchemical reaction!");
		messages.put("not_enough_sap_msg", ChatColor.RED + "No " + SHAMAN_SAP_NAME + ChatColor.RED + "! Alechmy not started.");
		
		messages.put("in_progress_message", ChatColor.RED + "Alembics cannot be broken while in progress!");
		messages.put("alembic_construction", ChatColor.RED + "Constructing Alembic...");
		messages.put("in_progress_inventory_message", ChatColor.RED + "Inventories of Alembics cannot be modified while they are in progress!");
		messages.put("ender_pearl_error", ChatColor.RED + "The magics in this item conflict with the energies inside the Alembic.");
		
		messages.put("no_aspects", ChatColor.RED + "This item does not appear to influence the resonator.");
		messages.put("small_aspects", ChatColor.YELLOW + "The dial hovers for a moment, then falters. This item contains a small amount of alchemical energy.");
		messages.put("medium_aspects", ChatColor.GOLD + "The dial hovers steadily above the baseline. This item certainly contains alchemical properties.");
		messages.put("large_aspects", ChatColor.BLUE + "The resonator thrums with energy, and the dial flails wildly. This item must contain very powerful energies.");
		
		config.createSection("messages", messages);
		plugin.saveConfig();
	} // populateMessages

} // class
