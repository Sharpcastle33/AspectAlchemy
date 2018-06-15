package com.gmail.sharpcastle33;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public class Constants {

	/* AspectRecipeManager */
	
	// NOTE: Tolerance must be NONPOSITIVE for an alchemical reaction to succeed
	public static final int ADDITIONAL_ASPECT_TOLERANCE = 1; // number added to tolerance for each additional aspect of a correct type (by the recipe) beyond the base requirement
	public static final int BAD_ASPECT_TOLERANCE = 2; // number added to tolerance for each additional aspect of an incorrect type (by the recipe)
	public static final int SHAMAN_SAP_VALUE = 6; // magnitude of sap effect on tolerance
	
	/* Debug Commands */
	
	public static final String INSUFFICIENT_PERMISSIONS = ChatColor.RED+"[AspectAlchemy] You do not have permission to use this command!";
	
	/* AlembicHandler */
	
	public static final int INGREDIENTS_MINIMUM = 3;
	public static final int ALEMBIC_TICK_TIME = 1200; // 1200 MC Ticks in 1 minute
	
	public static final String SHAMAN_SAP_NAME = ChatColor.YELLOW + "Shaman Sap";
	
	public static final String NOT_ENOUGH_INGREDIENTS_MSG = ChatColor.RED + "You must have at least " + INGREDIENTS_MINIMUM + " different types of ingredients for an alchemical reaction!";
	public static final String NOT_ENOUGH_WATER_BOTTLES_MSG = ChatColor.RED + "You must have all three brewing stand slots filled with water bottles to begin an alchemical reaction!";
	public static final String NOT_ENOUGH_FUEL_MSG = ChatColor.RED + "You must have some coal in the Alembic Bellows in order to begin an alchemical reaction!";
	public static final String NOT_ENOUGH_SAP_MSG = ChatColor.RED + "No " + SHAMAN_SAP_NAME + ChatColor.RED + "! Alechmy not started.";
	
	/* Admin Tools */
	
	public static final String DEBUG_ITEM = ChatColor.RED + "Admin Debug Tool";
	public static final String ASPECT_ADMIN_TOOL = ChatColor.RED + "Secret Admin Aspect Tool";
	public static final String POTION_ADMIN_TOOL = ChatColor.RED + "Secret Admin Potion Tool";
	public static final String INSTANT_ADMIN_TOOL = ChatColor.RED + "Secret Admin Brewing Tool";
	
	/* Alembic Interactions */
	
	public final static String IN_PROGRESS_MESSAGE = ChatColor.RED + "Alembics cannot be broken while in progress!";
	
	public static final String ALEMBIC_ITEM_NAME = ChatColor.YELLOW + "Alembic";
	public static final String ALEMBIC_CONSTRUCTION = ChatColor.RED + "Constructing Alembic...";
	public static final String ALEMBIC_CHEST_NAME = ChatColor.BLUE + "Alembic Chamber";
	public static final String ALEMBIC_FURNACE_NAME = ChatColor.BLUE + "Alembic Bellows";
	public static final String ALEMBIC_BREWINGSTAND_NAME = ChatColor.BLUE + "Alembic Stand";
	
	public static final String IN_PROGRESS_INVENTORY_MESSAGE = ChatColor.RED + "Inventories of Alembics cannot be modified while they are in progress!";
	public static final String ENDER_PEARL_ERROR = ChatColor.RED + "The magics in this item conflict with the energies inside the Alembic.";
	
	/* Thaumaturgical Resonator */
	
	public static final Material RESONATOR_ITEM = Material.WATCH;
	
	public static final String RESONATOR_NAME = ChatColor.YELLOW + "Thaumaturgical Resonator";
	
	public static final String NO_ASPECTS = ChatColor.RED + "This item does not appear to influence the resonator.";
	public static final String SMALL_ASPECTS = ChatColor.YELLOW + "The dial hovers for a moment, then falters. This item contains a small amount of alchemical energy.";
	public static final String MEDIUM_ASPECTS = ChatColor.GOLD + "The dial hovers steadily above the baseline. This item certainly contains alchemical properties.";
	public static final String LARGE_ASPECTS = ChatColor.BLUE + "The resonator thrums with energy, and the dial flails wildly. This item must contain very powerful energies.";
	
	

} // class
