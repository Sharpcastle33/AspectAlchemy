package com.gmail.sharpcastle33.handlers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Chest;
import org.bukkit.block.Furnace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class AlembicTickTask extends BukkitRunnable {

	Integer timeRemaining;
	Location standLocation;
	Location chestLocation;
	Location furnaceLocation;
	
	int fuelTick;

	/*
	 * Set up tick task by chest block
	 */
	public AlembicTickTask(Chest chest) {
		fuelTick = 4;

		this.standLocation = ((Block) chest).getRelative(BlockFace.UP).getLocation();
		this.chestLocation = chest.getLocation();
		this.furnaceLocation = ((Block) chest).getRelative(BlockFace.DOWN).getLocation();
	}

	/*
	 * Set up tick task by chest location
	 */
	public AlembicTickTask(Location chestLocation) {

		Block chest = chestLocation.getBlock();
		
		this.standLocation = chest.getRelative(BlockFace.UP).getLocation();
		this.chestLocation = chestLocation;
		this.furnaceLocation = chest.getRelative(BlockFace.DOWN).getLocation();
	}

	@Override
	public void run() {
		tickAlembic();
	}
	
	/**
	 * Checks some errors related to block states not being correct
	 */
	private void checkErrors() {
		if(!(standLocation.getBlock().getState() instanceof BrewingStand)) {
			Bukkit.getLogger().severe("Error while ticking alembic: BrewingStand Block is Not a BrewingStand (" + standLocation + ")");
			this.cancel();
		} // if
		if(!(chestLocation.getBlock().getState() instanceof Chest)) {
			Bukkit.getLogger().severe("Error while ticking alembic: Chest Block is Not a Chest (" + chestLocation + ")");
			this.cancel();
		} // if
		if(!(furnaceLocation.getBlock().getState() instanceof Furnace)) {
			Bukkit.getLogger().severe("Error while ticking alembic: Furnace Block is Not a Furnace (" + furnaceLocation + ")");
			this.cancel();
		} // if
	} // checkErrors

	// Tick the alembic
	private void tickAlembic() {
		checkErrors(); // checks some errors related to block states not being correct
		
		// Alembic blocks
		BrewingStand stand = (BrewingStand) standLocation.getBlock().getState();
		Chest chest = (Chest) chestLocation.getBlock().getState();
		Furnace furnace = (Furnace) furnaceLocation.getBlock().getState();

		// Update time remaining
		timeRemaining = getTimeRemaining(chest) - 1;
		updateTimeRemaining(chest);

		// If time is 0 or less evaluate recipe and deactivate alembic
		if (timeRemaining <= 0) {
			this.cancel();
			AlembicHandler.completeAlchemy(chest, stand);
			ItemStack[] shamanSap = AlembicHandler.getShamanSaps(chest);
			for(ItemStack itemStack: shamanSap) if(itemStack != null) itemStack.setAmount(0);
			AlembicHandler.deactivateAlembic(chest);
		}
		
		fuelTick++;
		// Consume fuel and handle failure
		if(fuelTick >= 4 && !consumeFuel(furnace)) {
			alembicFail(chest, stand);
			AlembicHandler.clearFiftyFifty(chest);
			this.cancel();
		}
		
		if(fuelTick >= 4) fuelTick = 0;

	}
	
	
	// Consume fuel method
	private boolean consumeFuel(Furnace furnace) {
		if(furnace.getInventory().getFuel() == null || furnace.getInventory().getFuel().getAmount() == 0) return false;
		if(!furnace.getInventory().getFuel().getType().equals(Material.COAL)) return false;
		furnace.getInventory().getFuel().setAmount(furnace.getInventory().getFuel().getAmount() - 1);
		return true;
	}
	
	// Alembic failure method
	private void alembicFail(Chest chest, BrewingStand stand) {
		ItemStack result = new ItemStack(Material.GLASS_BOTTLE);
		ItemStack[] results = { result, result, result };
		stand.getInventory().setContents(results);
		ItemStack[] shamanSap = AlembicHandler.getShamanSaps(chest);
		for(ItemStack itemStack: shamanSap) if(itemStack != null) itemStack.setAmount(0);
		AlembicHandler.deactivateAlembic(chest);
	}

	// Updates progress GUI item to correct time remaining
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

	// Get remaining time from progress GUI item
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
			time = Integer.parseInt(timeString.substring(0, timeString.length()-3));
		}
		return time;
	}

}
