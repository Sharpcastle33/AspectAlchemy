package com.gmail.sharpcastle33.handlers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Chest;
import org.bukkit.block.Furnace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.gmail.sharpcastle33.AlembicManager;

public class AlembicTickTask extends BukkitRunnable {

	Integer timeRemaining;
	Location standLocation;
	Location chestLocation;
	Location furnaceLocation;

	/*
	 * Set up tick task by chest block
	 */
	public AlembicTickTask(Chest chest) {

		this.standLocation = ((Block) chest).getRelative(BlockFace.UP).getLocation();
		this.chestLocation = chest.getLocation();
		this.furnaceLocation = ((Block) chest).getRelative(BlockFace.DOWN).getLocation();
		
		Bukkit.getLogger().info("DEBUG: " + standLocation);
	}

	/*
	 * Set up tick task by chest location
	 */
	public AlembicTickTask(Location chestLocation) {

		Block chest = chestLocation.getBlock();
		
		this.standLocation = chest.getRelative(BlockFace.UP).getLocation();
		this.chestLocation = chestLocation;
		this.furnaceLocation = chest.getRelative(BlockFace.DOWN).getLocation();
		Bukkit.getLogger().info("DEBUG: " + standLocation);
		Bukkit.getLogger().info("DEBUG: " + this.chestLocation);
	}

	@Override
	public void run() {
		Bukkit.getLogger().info("Ticked alembic");
		tickAlembic();
	}

	private void tickAlembic() {
		BrewingStand stand = (BrewingStand) standLocation.getBlock().getState();
		Chest chest = (Chest) chestLocation.getBlock().getState();
		Furnace furnace = (Furnace) furnaceLocation.getBlock().getState();

		timeRemaining = getTimeRemaining(chest) - 1;
		updateTimeRemaining(chest);

		if (timeRemaining <= 0) {
			AlembicHandler.evaluateAlembic(chest);
			AlembicHandler.deactivateAlembic(chest);
			this.cancel();
		}
		
		if(!consumeFuel(furnace)) {
			alembicFail(chest);
		}

		ItemStack[] bindingAgent = AlembicHandler.getBindingAgents(chest);
		for(int i=2; i >= 0; i--) {
			if(bindingAgent[i] != null) {
				bindingAgent[i].setAmount(bindingAgent[i].getAmount() - 1);
				break;
			}
		}

	}
	
	
	private boolean consumeFuel(Furnace furnace) {
		return true;
	}
	
	private void alembicFail(Chest chest) {
		
	}

	private void updateTimeRemaining(Chest chest) {
		ItemStack progress = chest.getInventory().getItem(17);
		ItemMeta progressMeta = progress.hasItemMeta() ? progress.getItemMeta() : null;
		if (progressMeta == null) {
			Bukkit.getLogger().severe("Error while ticking alembic: No progress GUI item. (" + chestLocation + ")");
			this.cancel();
		}

		List<String> lore = new ArrayList<>();
		lore.add("Time Remaining: " + timeRemaining + "min");
		
		progressMeta.setLore(lore);
		progress.setItemMeta(progressMeta);
		
	}

	private int getTimeRemaining(Chest chest) {

		int time = 0;

		ItemStack progress = chest.getInventory().getItem(17);
		ItemMeta progressMeta = progress.hasItemMeta() ? progress.getItemMeta() : null;
		if (progressMeta == null) {
			Bukkit.getLogger().severe("Error while ticking alembic: No progress GUI item. (" + chestLocation + ")");
			this.cancel();
		}

		String lore = null;
		for(String loreItem : progressMeta.getLore()) {
			if (loreItem.contains("Time Remaining:")) {
				lore = loreItem;
			}
		}
		if (lore != null) {
			String timeString = lore.split(" ")[2];
			Bukkit.getLogger().info("DEBUG: getTimeRemaining::timeString = " + timeString);
			time = Integer.parseInt(timeString.substring(0, timeString.length()-3));
		}
		Bukkit.getLogger().info("DEBUG: getTimeRemaining = " + time);
		return time;
	}

}
