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
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import com.gmail.sharpcastle33.AspectAlchemy;


// TODO: Add logic to restart alembics on server start


public class AlembicHandler {

	static final int ALEMBIC_TICK_TIME = 1200; // 1200 MC Ticks in 1 minute
	static Map<String, Integer> bindingAgentPoints;
	static Plugin plugin;
	
	
	// Initialization method. Need this to get reference of plugin
	public static void init(Plugin p) {
		plugin = p;
		
		// Restart tasks that for active alembics
		for(Location loc : AspectAlchemy.alembicMan.activeAlembics) {
			BukkitTask tickTask =  new AlembicTickTask(loc).runTaskTimer(plugin, ALEMBIC_TICK_TIME, ALEMBIC_TICK_TIME);
		}
		
		bindingAgentPoints = new HashMap<>();
		bindingAgentPoints.put("Binding Agent", 1);
	}
	
	public static boolean checkWaterBottles(Block b) {
		if(b.getType() == Material.BREWING_STAND) {
            BrewingStand brewingStandState = (BrewingStand) b.getState();	
            ItemStack[] arr = brewingStandState.getInventory().getContents();
            ItemStack bottle = new ItemStack(Material.POTION, 1, ((byte)0));
            for(int i = 0; i< 3; i++) {
            	if(arr[i] != null && arr[i].isSimilar(bottle)) {
            		
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
		updateAlembicInfo((Chest) b.getState(), name);
		
		// Register alembic as active
		AspectAlchemy.alembicMan.activateAlembic(b.getLocation());
		
		// Start alemchemy task
		BukkitTask alebmicTask = new AlembicTickTask(b.getLocation()).runTaskTimer(plugin, ALEMBIC_TICK_TIME, ALEMBIC_TICK_TIME);
		
	}

	public static void updateAlembicInfo(Chest c, String name) {
		
		if (getTotalBindingPoints(c) <= 0) {
			Bukkit.getServer().getPlayer(name).sendMessage(ChatColor.RED + "No binding agent! Alechmy not started.");
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
			int time = getTotalBindingPoints(c) * ALEMBIC_TICK_TIME / 20 / 60;
			List<String> lore = new ArrayList<>();
			lore.add("Time Remaining: " + time + "min");
			meta.setLore(lore);
			start.setItemMeta(meta);
		}
		c.getInventory().setItem(17, start);

	}

	public static ItemStack[] getBindingAgents(Chest chest) {
		ItemStack[] ret = new ItemStack[3];
		ret[0] = chest.getInventory().getItem(0);
		ret[1] = chest.getInventory().getItem(9);
		ret[2] = chest.getInventory().getItem(18);
		return ret;
	}

	public static int getTotalBindingPoints(Chest chest) {
		int bindingPoints = 0;
		
		ItemStack[] bindingAgents = getBindingAgents(chest);
		for (ItemStack agent : bindingAgents) {
			if (agent != null && agent.hasItemMeta()) {
				ItemMeta bindingMeta = agent.getItemMeta();
				if (bindingMeta.hasDisplayName()) {
					bindingPoints += agent.getAmount() * bindingAgentPoints.getOrDefault(bindingMeta.getDisplayName(), 0);
				}
			}
		}

		return bindingPoints;
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
	
	public static List<ItemStack> evaluateAlembic(Chest chest) {
		
		List<ItemStack> results = new ArrayList<>();
		
		
		return results;
	}
	
	public static ItemStack[] getIngredients(Chest chest) {
		ItemStack[] ret = new ItemStack[15];

		int slot = 2;

		for (int counter = 0; counter < 16; counter++) {
			ret[counter] = chest.getInventory().getItem(slot);

			if (slot == 6 || slot == 15) {
				slot += 4;
				continue;
			}
			slot++;
		}
		return ret;
	}

}
