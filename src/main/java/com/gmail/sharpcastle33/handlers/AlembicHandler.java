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
import org.bukkit.scheduler.BukkitTask;

import com.gmail.sharpcastle33.AspectAlchemy;
import com.gmail.sharpcastle33.aspects.Aspect;
import com.gmail.sharpcastle33.aspects.AspectManager;
import com.gmail.sharpcastle33.aspects.AspectRecipeManager;
import com.gmail.sharpcastle33.potions.CustomPotion;
import com.gmail.sharpcastle33.potions.PotionManager;


// TODO: Add logic to restart alembics on server start


public class AlembicHandler {
	
	static final String NOT_ENOUGH_WATER_BOTTLES_MSG = ChatColor.RED + "You must have all three brewing stand slots filled with water bottles to begin an alchemical reaction!";
	static final String NOT_ENOUGH_FUEL_MSG = ChatColor.RED + "You must have some coal in the Alembic Bellows in order to begin an alchemical reaction!";
	static final String SHAMAN_SAP_NAME = ChatColor.YELLOW + "Shaman Sap";

	static final int ALEMBIC_TICK_TIME = 1200; // 1200 MC Ticks in 1 minute
	static Map<String, Integer> shamanSapPoints;
	static Plugin plugin;
	
	
	// Initialization method. Need this to get reference of plugin
	public static void init(Plugin p) {
		plugin = p;
		
		// Restart tasks that for active alembics
		for(Location loc : AspectAlchemy.alembicMan.activeAlembics) {
			BukkitTask tickTask =  new AlembicTickTask(loc).runTaskTimer(plugin, ALEMBIC_TICK_TIME, ALEMBIC_TICK_TIME);
		}
		
		shamanSapPoints = new HashMap<>();
		shamanSapPoints.put(SHAMAN_SAP_NAME, 1);
	}
	
	public static boolean checkWaterBottles(Block b) {
		if(b.getType() == Material.BREWING_STAND) {
            BrewingStand brewingStandState = (BrewingStand) b.getState();	
            ItemStack[] arr = brewingStandState.getInventory().getContents();
            //ItemStack bottle = new ItemStack(Material.POTION, 1, ((byte)0));
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
	}


	public static void startAlchemy(Block b, String name) {
		if(!checkWaterBottles(b.getRelative(0, 1, 0))) {
			Bukkit.getServer().getPlayer(name).sendMessage(NOT_ENOUGH_WATER_BOTTLES_MSG);
			return;
		} // if
		
		if(!(b.getRelative(BlockFace.DOWN).getLocation() instanceof Furnace) || ((Furnace) b).getInventory().getFuel() == null || ((Furnace) b).getInventory().getFuel().getAmount() < 1) {
			Bukkit.getServer().getPlayer(name).sendMessage(NOT_ENOUGH_FUEL_MSG);
			return;
		} // if
		
		updateAlembicInfo((Chest) b.getState(), name);
		
		// Register alembic as active
		AspectAlchemy.alembicMan.activateAlembic(b.getLocation());
		
		// Start alemchemy task
		BukkitTask alebmicTask = new AlembicTickTask(b.getLocation()).runTaskTimer(plugin, ALEMBIC_TICK_TIME, ALEMBIC_TICK_TIME);
		
	}

	public static void updateAlembicInfo(Chest c, String name) {
		
		if (getTotalShamanSapPoints(c) <= 0) {
			Bukkit.getServer().getPlayer(name).sendMessage(ChatColor.RED + "No Shaman Sap! Alechmy not started.");
			return;
		}
		
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
			lore.add("Time Remaining: " + time + "min");
			meta.setLore(lore);
			start.setItemMeta(meta);
		}
		c.getInventory().setItem(17, start);

	}

	public static ItemStack[] getShamanSaps(Chest chest) {
		ItemStack[] ret = new ItemStack[3];
		ret[0] = chest.getInventory().getItem(0);
		ret[1] = chest.getInventory().getItem(9);
		ret[2] = chest.getInventory().getItem(18);
		return ret;
	}

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
	}

	// Remove alembic from active alembic list, revert alembic chest to idle state
	public static void deactivateAlembic(Chest chest) {
		AspectAlchemy.alembicMan.deactivateAlembic(chest.getLocation());
		
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
		
	}
	
	public static void completeAlchemy(Chest chest, BrewingStand stand) {
		//Bukkit.getLogger().info("getting ingredients");
		ItemStack[] ingredients = getIngredients(chest);
		
		//Bukkit.getLogger().info("getting Aspect Totals");
		Map<Aspect, Integer> aspectTotals = AspectManager.getAspectTotals(ingredients);
		
		//Bukkit.getLogger().info("getting Binding Points");
		int amountShamanSap = getTotalShamanSapPoints(chest);
		
		//Bukkit.getLogger().info("Finding Resultant CustomPotion");
		CustomPotion customPot = AspectRecipeManager.findResult(aspectTotals, amountShamanSap);
		
		//Bukkit.getLogger().info("Getting Potion");
		ItemStack result = PotionManager.getPotion(customPot);
		
		//Bukkit.getLogger().info("Setting Contents");
		ItemStack[] results = { result, result, result };
		stand.getInventory().setContents(results);
		
		//Bukkit.getLogger().info("Clearing Ingredients");
		clearIngredients(chest);
	}
	
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
	}
	
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
	
	public static void clearFiftyFifty(Chest chest) {
		int slot = 2;
		for (int counter = 0; counter < 15; counter++) {
			if(chest.getInventory().getItem(slot) != null) chest.getInventory().getItem(slot).setAmount(0);
			if (slot == 6 || slot == 15) {
				slot += 5;
				continue;
			} // if
			slot++;
		} // for
	} // clearFiftyFifty

}
