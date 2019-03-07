package com.gmail.sharpcastle33.handlers;

import java.util.ArrayList;
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
import org.bukkit.scheduler.BukkitRunnable;
import com.gmail.sharpcastle33.aspects.Aspect;
import com.gmail.sharpcastle33.aspects.AspectManager;
import com.gmail.sharpcastle33.aspects.AspectRecipeManager;
import com.gmail.sharpcastle33.potions.CustomPotion;
import com.gmail.sharpcastle33.potions.PotionManager;
import com.gmail.sharpcastle33.util.AmbianceUtil;

/**
 * Handles Alembic Ticks (each minute this checks various things, performs
 * various actions, and decreases the time remaining)
 */
public class AlembicTickTask extends BukkitRunnable {

	Integer timeRemaining;
	Location standLocation;
	Location chestLocation;
	Location furnaceLocation;

	int fuelTick;

	/**
	 * Set up tick task by chest block
	 * 
	 * @param chest
	 *            Alembic Chest
	 */
	public AlembicTickTask(Chest chest) {
		fuelTick = 4;

		this.standLocation = ((Block) chest).getRelative(BlockFace.UP).getLocation();
		this.chestLocation = chest.getLocation();
		this.furnaceLocation = ((Block) chest).getRelative(BlockFace.DOWN).getLocation();
	} // AlembicTickTask(Chest)

	/**
	 * Set up tick task by chest location chestLocation Location of Alembic Chest
	 */
	public AlembicTickTask(Location chestLocation) {
		fuelTick = 4;
		
		if(chestLocation == null) {
			Bukkit.getServer().getLogger().info(ChatColor.RED + "[AspectAlchemy]: " + "Caught null pointer: Attempted to run an Alembic Tick Task at however, the location was null.");
			return;
		}
		
		try {
			if(chestLocation.getBlock() == null) {
				Bukkit.getServer().getLogger().info(ChatColor.RED + "[AspectAlchemy]: " + "Caught null pointer: Attempted to run an Alembic Tick Task at x:" + chestLocation.getBlockX() + "z:" + chestLocation.getBlockZ() + " however, the chest block was null.");
				return;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

		Block chest = chestLocation.getBlock();
		


		this.standLocation = chest.getRelative(BlockFace.UP).getLocation();
		this.chestLocation = chestLocation;
		this.furnaceLocation = chest.getRelative(BlockFace.DOWN).getLocation();
	} // AlembicTickTask(Location)

	@Override
	public void run() {
		tickAlembic();
	} // run

	/**
	 * Checks some errors related to block states not being correct
	 * 
	 * @author KingVictoria
	 */
	private void checkErrors() {
		if (!(standLocation.getBlock().getState() instanceof BrewingStand)) {
			Bukkit.getLogger().severe(
					"Error while ticking alembic: BrewingStand Block is Not a BrewingStand (" + standLocation + ")");
			this.cancel();
		} // if
		if (!(chestLocation.getBlock().getState() instanceof Chest)) {
			Bukkit.getLogger()
					.severe("Error while ticking alembic: Chest Block is Not a Chest (" + chestLocation + ")");
			this.cancel();
		} // if
		if (!(furnaceLocation.getBlock().getState() instanceof Furnace)) {
			Bukkit.getLogger()
					.severe("Error while ticking alembic: Furnace Block is Not a Furnace (" + furnaceLocation + ")");
			this.cancel();
		} // if
	} // checkErrors

	/**
	 * Tick the alembic
	 */
	private void tickAlembic() {
		checkErrors(); // checks some errors related to block states not being correct

		// Alembic blocks
		BrewingStand stand = (BrewingStand) standLocation.getBlock().getState();
		Chest chest = (Chest) chestLocation.getBlock().getState();
		Furnace furnace = (Furnace) furnaceLocation.getBlock().getState();

		// Update time remaining
		timeRemaining = getTimeRemaining(chest) - 1;
		updateTimeRemaining(chest);
		
        ItemStack[] shamanSap = AlembicHandler.getShamanSaps(chest);
        int sapAmt = AlembicHandler.getTotalShamanSapPoints(chest);    
        
		
        // If time is halfway check the recipe and if it is destined to fail, fail it.
        if(timeRemaining == sapAmt/2) {
          ItemStack[] ingredients = AlembicHandler.getIngredients(chest);

          Map<Aspect, Integer> aspectTotals = AspectManager.getAspectTotals(ingredients);

          int amountShamanSap = AlembicHandler.getTotalShamanSapPoints(chest);

          CustomPotion customPot = AspectRecipeManager.findResult(aspectTotals, amountShamanSap);

          ItemStack result = PotionManager.getPotion(customPot);
          
          //If recipe will fail
          if(result == null || result.getType() == Material.AIR || customPot == CustomPotion.MUNDANE_POT) {
            AlembicHandler.completeAlchemy(chest, stand);
            for (ItemStack itemStack : shamanSap)
                if (itemStack != null)
                    itemStack.setAmount(0);
            AlembicHandler.deactivateAlembic(chest);
            AmbianceUtil.alembicFailureAmbiance(chest.getBlock());
          }
        }
		// If time is 0 or less evaluate recipe and deactivate alembic
		if (timeRemaining <= 1) {
			this.cancel();
			AlembicHandler.completeAlchemy(chest, stand);
			for (ItemStack itemStack : shamanSap)
				if (itemStack != null)
					itemStack.setAmount(0);
			AlembicHandler.deactivateAlembic(chest);
			AmbianceUtil.alembicSuccessAmbiance(chest.getBlock());
		}else {
			AmbianceUtil.alembicTickAmbiance(chest.getBlock());
		}

		fuelTick++;
		// Consume fuel and handle failure
		if (fuelTick >= 4 && !consumeFuel(furnace)) {
			alembicFail(chest, stand);
			AlembicHandler.clearRand(chest);
			this.cancel();
		}

		if (fuelTick >= 4)
			fuelTick = 0;

	} // tickAlembic

	/**
	 * Consume fuel method
	 * 
	 * @param furnace
	 *            Furnace Alembic bellows
	 * @return false if there is no more fuel
	 * @author KingVictoria
	 */
	private boolean consumeFuel(Furnace furnace) {
		if (furnace.getInventory().getFuel() == null || furnace.getInventory().getFuel().getAmount() == 0)
			return false;
		if (!furnace.getInventory().getFuel().getType().equals(Material.COAL))
			return false;
		furnace.getInventory().getFuel().setAmount(furnace.getInventory().getFuel().getAmount() - 1);
		return true;
	} // consumeFuel

	/**
	 * Alembic failure method
	 * 
	 * @param chest
	 *            Alembic Chest
	 * @param stand
	 *            Alembic BrewingStand
	 * @author KingVictoria
	 */
	private void alembicFail(Chest chest, BrewingStand stand) {
		ItemStack result = new ItemStack(Material.GLASS_BOTTLE);
		ItemStack[] results = { result, result, result };
		stand.getInventory().setContents(results);
		ItemStack[] shamanSap = AlembicHandler.getShamanSaps(chest);
		for (ItemStack itemStack : shamanSap)
			if (itemStack != null)
				itemStack.setAmount(0);
		AlembicHandler.deactivateAlembic(chest);
	} // alembicFail

	/**
	 * Updates progress GUI item to correct time remaining
	 * 
	 * @param chest
	 *            Alembic Chest
	 */
	private void updateTimeRemaining(Chest chest) {
		ItemStack progress = chest.getInventory().getItem(17);
		ItemMeta progressMeta = progress.hasItemMeta() ? progress.getItemMeta() : null;
		if (progressMeta == null) {
			Bukkit.getLogger().severe("Error while ticking alembic: No progress GUI item. (" + chestLocation + ")");
			this.cancel();
		}

		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.RED + "Time Remaining: " + timeRemaining + "min");

		progressMeta.setLore(lore);
		progress.setItemMeta(progressMeta);

	} // updateTimeRemaining

	/**
	 * Get remaining time from progress GUI item
	 * 
	 * @param chest
	 *            Alembic Chest
	 * @return int time remaining (in minutes)
	 */
	private int getTimeRemaining(Chest chest) {

		int time = 0;

		ItemStack progress = chest.getInventory().getItem(17);
		ItemMeta progressMeta = progress.hasItemMeta() ? progress.getItemMeta() : null;
		if (progressMeta == null) {
			Bukkit.getLogger().severe("Error while ticking alembic: No progress GUI item. (" + chestLocation + ")");
			this.cancel();
		}

		String lore = null;
		for (String loreItem : progressMeta.getLore()) {
			if (loreItem.contains(ChatColor.RED + "Time Remaining:")) {
				lore = loreItem;
			}
		}
		if (lore != null) {
			String timeString = lore.split(" ")[2];
			time = Integer.parseInt(timeString.substring(0, timeString.length() - 3));
		}
		return time;
	} // getTimeRemaining

} // class
