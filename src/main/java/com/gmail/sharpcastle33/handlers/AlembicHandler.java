package com.gmail.sharpcastle33.handlers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Chest;
import org.bukkit.block.Furnace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import com.gmail.sharpcastle33.AlembicManager;
import com.gmail.sharpcastle33.aspects.Aspect;
import com.gmail.sharpcastle33.aspects.AspectManager;
import com.gmail.sharpcastle33.aspects.AspectRecipeManager;
import com.gmail.sharpcastle33.potions.CustomPotion;
import com.gmail.sharpcastle33.potions.PotionManager;

public class AlembicHandler {
	static final int INGREDIENTS_MINIMUM = 3;
	static final int ALEMBIC_TICK_TIME = 1200; // 1200 MC Ticks in 1 minute
	
	static final String SHAMAN_SAP_NAME = ChatColor.YELLOW + "Shaman Sap";
	
	static final String NOT_ENOUGH_INGREDIENTS_MSG = ChatColor.RED + "You must have at least "+INGREDIENTS_MINIMUM+" different types of ingredients for an alchemical reaction!";
	static final String NOT_ENOUGH_WATER_BOTTLES_MSG = ChatColor.RED + "You must have all three brewing stand slots filled with water bottles to begin an alchemical reaction!";
	static final String NOT_ENOUGH_FUEL_MSG = ChatColor.RED + "You must have some coal in the Alembic Bellows in order to begin an alchemical reaction!";
	static final String NOT_ENOUGH_SAP_MSG = ChatColor.RED + "No "+SHAMAN_SAP_NAME+ChatColor.RED+"! Alechmy not started.";

	static Map<String, Integer> shamanSapPoints;
	static Plugin plugin;
	
	
	// Initialization method. Need this to get reference of plugin
	/**
	 * Initialization method. Gets a reference of plugin and initializes active Alembic AlembicTickTasks
	 * @param p AspectAlchemy Plugin
	 */
	public static void init(Plugin p) {
		plugin = p;
		
		// Restart tasks that for active alembics
		for(Location loc : AlembicManager.alembics) {
			new AlembicTickTask(loc).runTaskTimer(plugin, ALEMBIC_TICK_TIME, ALEMBIC_TICK_TIME);
		} // for
		
		shamanSapPoints = new HashMap<>();
		shamanSapPoints.put(SHAMAN_SAP_NAME, 1);
	} // init
	
	/**
	 * Ensures that there are enough water bottles, fuel, and ingredients to begin alchemcy, then begins Alchemy
	 * @param b Alembic Chest (as a Block)
	 * @param name String name of Player
	 */
	public static void startAlchemy(Block b, String name) {
		if(!runChecks(b, name)) return; // runs all checks necessary before starting Alchemy
		
		updateAlembicInfo((Chest) b.getState(), name);
		
		// Register alembic as active
		AlembicManager.activateAlembic(b.getLocation());
		
		// Start alchemy task
		new AlembicTickTask(b.getLocation()).runTaskTimer(plugin, ALEMBIC_TICK_TIME, ALEMBIC_TICK_TIME);
		
	} // startAlchemy

	/**
	 * Runs all of the beginning checks before starting Alchemy
	 * @param b Alembic Chest Block
	 * @param name String name of Player
	 * @return false if any othe the checks fail
	 */
	private static boolean runChecks(Block b, String name) {
		if(!checkWaterBottles(b.getRelative(0, 1, 0))) {
			Bukkit.getServer().getPlayer(name).sendMessage(NOT_ENOUGH_WATER_BOTTLES_MSG);
			return false;
		} // if
		
		if(!checkFuel(b)) {
			Bukkit.getServer().getPlayer(name).sendMessage(NOT_ENOUGH_FUEL_MSG);
			return false;
		} // if
		
		if(!minimumIngredientsCheck((Chest) b.getState())) {
			Bukkit.getServer().getPlayer(name).sendMessage(NOT_ENOUGH_INGREDIENTS_MSG);
			return false;
		} // if
		
		if(getTotalShamanSapPoints((Chest) b.getState()) <= 0) {
			Bukkit.getServer().getPlayer(name).sendMessage(NOT_ENOUGH_SAP_MSG);
			return false;
		} // if
		
		return true;
	} // runChecks
	
	/**
	 * Checks to ensure that the minimum number of water bottles are present
	 * @param b Alembic Stand
	 * @return false if there are not enough water bottles (all three slots must be filled)
	 */
	private static boolean checkWaterBottles(Block b) {
		if(b.getType() == Material.BREWING_STAND) {
            BrewingStand brewingStandState = (BrewingStand) b.getState();	
            ItemStack[] arr = brewingStandState.getInventory().getContents();
            for(int i = 0; i< 3; i++) {
            	if(arr[i] != null && arr[i].getType() == Material.POTION) {
            		
            	}else {
            		return false;
            	}
            }
            return true;
		}
		Bukkit.getServer().getLogger().warning("Called checkWaterBottles on something that isn't a brewingstand!");
		return false;
	} // checkWaterBottles
	
	/**
	 * Checks the fuel to insure there is at least one coal available for an alchemical reaction
	 * @param b Alembic Chest Block
	 * @return false if there is no furnace or no fuel present
	 */
	private static boolean checkFuel(Block b) {
		if(!(b.getRelative(BlockFace.DOWN).getState() instanceof Furnace) 
				|| ((Furnace) b.getRelative(BlockFace.DOWN).getState()).getInventory().getFuel() == null 
				|| ((Furnace) b.getRelative(BlockFace.DOWN).getState()).getInventory().getFuel().getAmount() < 1)
			return false;
		return true;
	} // checkFuel
	
	/**
	 * Checks to ensure that the minimum amount of ingredients are present (only counts unique ingredients with aspects)
	 * @param chest Alembic Chest
	 * @return false if there are fewer than INGREDIENTS_MINIMUM different ingredients present
	 */
	private static boolean minimumIngredientsCheck(Chest chest) {
		List<String> uniques = new ArrayList<>();
		int slot = 2;
		for (int counter = 0; counter < 15; counter++) {
			if(chest.getInventory().getItem(slot) != null) if(!uniques.contains(chest.getInventory().getItem(slot).getItemMeta().getDisplayName())) {
				if(AspectManager.getAspects(chest.getInventory().getItem(slot)) != null)
					uniques.add(chest.getInventory().getItem(slot).getItemMeta().getDisplayName());
			}
			if(uniques.size() >= INGREDIENTS_MINIMUM) return true;
			if (slot == 6 || slot == 15) {
				slot += 5;
				continue;
			} // if
			slot++;
		} // for
		
		return false;
	} // minimumIngredientsCheck

	/**
	 * Gets the Alembic GUI into the in-progress display
	 * @param c Alembic Chest
	 * @param name String name of Player (player than began the process)
	 */
	private static void updateAlembicInfo(Chest c, String name) {
		// Update "Information" GUI item to reflect time started and who started
		ItemStack info = c.getBlockInventory().getItem(8);
		if (info.hasItemMeta()) {
			ItemMeta meta = info.getItemMeta();
			Date now = new Date();
			SimpleDateFormat time = new SimpleDateFormat("HH:mm");
			String lore[] = { ChatColor.BLUE + "Started by: " + ChatColor.GOLD + name,
					ChatColor.BLUE + "Began at: " + ChatColor.RED + time.format(now) };
			meta.setLore(Arrays.asList(lore));
			info.setItemMeta(meta);
		}
		c.getInventory().setItem(8, info);

		// Update "Start" GUI item to show process is in prorgess and time left
		ItemStack start = c.getBlockInventory().getItem(17);
		if (start.hasItemMeta()) {
			ItemMeta meta = start.getItemMeta();
			meta.setDisplayName(ChatColor.RED + "In Progress");
			int time = getTotalShamanSapPoints(c) * ALEMBIC_TICK_TIME / 20 / 60;
			List<String> lore = new ArrayList<>();
			lore.add(ChatColor.RED+"Time Remaining: " + time + "min");
			meta.setLore(lore);
			start.setItemMeta(meta);
		}
		c.getInventory().setItem(17, start);

	} // updateAlembicInfo

	/**
	 * Gets an array of ItemStack for all the ShamanSaps in an Alembic
	 * @param chest Alembic Chest
	 * @return array of ItemStack of ShamanSaps
	 */
	public static ItemStack[] getShamanSaps(Chest chest) {
		ItemStack[] ret = new ItemStack[3];
		ret[0] = chest.getInventory().getItem(0);
		ret[1] = chest.getInventory().getItem(9);
		ret[2] = chest.getInventory().getItem(18);
		return ret;
	} // getShamanSaps

	/**
	 * Gets the total number of ShamanSapPoints in an Alembic Chest
	 * @param chest Alembic Chest
	 * @return int number of ShamanSaps in the Alembic
	 */
	public static int getTotalShamanSapPoints(Chest chest) {
		int sapPoints = 0;
		
		ItemStack[] shamanSaps = getShamanSaps(chest);
		for (ItemStack sap : shamanSaps) {
			if (sap != null && sap.hasItemMeta()) {
				ItemMeta sapMeta = sap.getItemMeta();
				if (sapMeta.hasDisplayName()) {
					sapPoints += sap.getAmount() * shamanSapPoints.getOrDefault(sapMeta.getDisplayName(), 0);
				}
			}
		}

		return sapPoints;
	} // getTotalShamanSapPoints

	/**
	 * Removes an Alembic from the active list and reverts it to its idle state
	 * @param chest Alembic Chest
	 */
	public static void deactivateAlembic(Chest chest) {
		AlembicManager.deactivateAlembic(chest.getLocation());
		
		ItemStack info = chest.getInventory().getItem(8);
		ItemMeta infoMeta = info.hasItemMeta() ? info.getItemMeta() : Bukkit.getItemFactory().getItemMeta(info.getType());
		ItemStack start = chest.getInventory().getItem(17);
		ItemMeta startMeta = start.hasItemMeta() ? start.getItemMeta() : Bukkit.getItemFactory().getItemMeta(start.getType());
		
		startMeta.setDisplayName(ChatColor.GREEN + "Start Alchemy");
		startMeta.setLore(null);
		
		infoMeta.setDisplayName(ChatColor.BLUE + "Information");
		infoMeta.setLore(null);
		
		start.setItemMeta(startMeta);
		info.setItemMeta(infoMeta);
		
	} // deactivateAlembic
	
	/**
	 * Run at the end of Alchemy before disabling the Alembic, determines the result of the alchemical reaction and outputs it to the Alembic Stand
	 * @param chest Alembic Chest
	 * @param stand Alembic Stand
	 */
	public static void completeAlchemy(Chest chest, BrewingStand stand) {
		ItemStack[] ingredients = getIngredients(chest);
		
		Map<Aspect, Integer> aspectTotals = AspectManager.getAspectTotals(ingredients);
		
		int amountShamanSap = getTotalShamanSapPoints(chest);
		
		CustomPotion customPot = AspectRecipeManager.findResult(aspectTotals, amountShamanSap);
		
		ItemStack result = PotionManager.getPotion(customPot);
		
		ItemStack[] results = { result, result, result };
		stand.getInventory().setContents(results);
		
		clearIngredients(chest);
	} // completeAlchemy
	
	/**
	 * Gets all the ingredients from the Alembic as an array of ItemStack
	 * @param chest Alembic Chest
	 * @return array of ItemStack
	 */
	public static ItemStack[] getIngredients(Chest chest) {
		ItemStack[] ret = new ItemStack[15];

		int slot = 2;

		for (int counter = 0; counter < 15; counter++) {
			ret[counter] = chest.getInventory().getItem(slot);

			if (slot == 6 || slot == 15) {
				slot += 5;
				continue;
			}
			slot++;
		}
		return ret;
	} // getIngredients
	
	/**
	 * Clears all of the ingredients from the Alembic
	 * @param chest Alembic Chest
	 */
	public static void clearIngredients(Chest chest) {
		int slot = 2;
		for (int counter = 0; counter < 15; counter++) {
			if(chest.getInventory().getItem(slot) != null) chest.getInventory().getItem(slot).setAmount(0);
			if (slot == 6 || slot == 15) {
				slot += 5;
				continue;
			} // if
			slot++;
		} // for
	} // clearIngredients
	
	/**
	 * Clears a random number of the ingredients from the Alembic
	 * @param chest Alembic Chest
	 */
	public static void clearRand(Chest chest) {
		int slot = 2;
		for (int counter = 0; counter < 15; counter++) {
			if(chest.getInventory().getItem(slot) != null) chest.getInventory().getItem(slot).setAmount((int) (Math.random() * chest.getInventory().getItem(slot).getAmount()));
			if (slot == 6 || slot == 15) {
				slot += 5;
				continue;
			} // if
			slot++;
		} // for
	} // clearRand

} // class
