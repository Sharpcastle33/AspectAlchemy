package com.gmail.sharpcastle33.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Chest;
import org.bukkit.block.Furnace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import com.gmail.sharpcastle33.Constants;
import com.gmail.sharpcastle33.handlers.AlembicHandler;
import com.gmail.sharpcastle33.util.InventoryUtil;

/**
 * Handles Alembic breaking mechanics
 * 
 * @author Sharpcastle33
 */
public class AlembicBreakListener implements Listener {

	@EventHandler
	public void alembicBreakEvent(BlockBreakEvent event) {
		Block b = event.getBlock();

		if (b.getType() == Material.FURNACE) {
			Furnace furnaceState = (Furnace) b.getState();
			if (furnaceState.getInventory().getName().equals(Constants.ALEMBIC_FURNACE_NAME)) {

				if (event.isCancelled()) {
					return;
				}

				if (b.getRelative(BlockFace.UP).getState() instanceof Chest) {
					Chest chestState = (Chest) b.getRelative(BlockFace.UP).getState();
					if (chestState.getInventory().getItem(17).hasItemMeta() && chestState.getInventory().getItem(17)
							.getItemMeta().getDisplayName().equals(ChatColor.RED + "In Progress")) {
						event.setCancelled(true);
						event.getPlayer().sendMessage(Constants.IN_PROGRESS_MESSAGE);
					} else {
						if(b.getRelative(BlockFace.UP)!= null && b.getRelative(BlockFace.UP).getType() == Material.CHEST){
							Block b2 = b.getRelative(BlockFace.UP);
							Chest chest = (Chest) b2.getState();
							if(chest.getInventory().getName().equals(Constants.ALEMBIC_CHEST_NAME)) {
								breakAlembicChest(b.getRelative(BlockFace.UP));
								breakAlembicStand(b.getRelative(BlockFace.UP).getRelative(BlockFace.UP));
								breakAlembicBellows(b);
								b.getWorld().dropItemNaturally(b.getLocation(),
										InventoryUtil.createGuiItem(ChatColor.YELLOW + "Alembic", Material.OBSERVER));
								event.setCancelled(true);
							}
						}
						
					}
				}
			}
		}

		else if (b.getType() == Material.BREWING_STAND) {
			BrewingStand brewingStandState = (BrewingStand) b.getState();
			if (brewingStandState.getInventory().getName().equals(Constants.ALEMBIC_BREWINGSTAND_NAME)) {

				if (event.isCancelled()) {
					return;
				}

				if (b.getRelative(BlockFace.DOWN).getState() instanceof Chest) {
					Chest chestState = (Chest) b.getRelative(BlockFace.DOWN).getState();
					if (chestState.getInventory().getItem(17).hasItemMeta() && chestState.getInventory().getItem(17)
							.getItemMeta().getDisplayName().equals(ChatColor.RED + "In Progress")) {
						event.setCancelled(true);
						event.getPlayer().sendMessage(Constants.IN_PROGRESS_MESSAGE);
					} else if(chestState.getInventory().getName().equals(Constants.ALEMBIC_CHEST_NAME)){
						
						breakAlembicChest(b.getRelative(BlockFace.DOWN));
						breakAlembicStand(b);
						breakAlembicBellows(b.getRelative(BlockFace.DOWN).getRelative(BlockFace.DOWN));
						b.getWorld().dropItemNaturally(b.getLocation(),
								InventoryUtil.createGuiItem(ChatColor.YELLOW + "Alembic", Material.OBSERVER));
						event.setCancelled(true);
					}
				}
			}
		}

		else if (b.getType() == Material.CHEST) {
			Chest chestState = (Chest) b.getState();
			if (chestState.getInventory().getName().equals(Constants.ALEMBIC_CHEST_NAME)) {

				if (event.isCancelled()) {
					return;
				}

				if (chestState.getInventory().getItem(17).hasItemMeta() && chestState.getInventory().getItem(17)
						.getItemMeta().getDisplayName().equals(ChatColor.RED + "In Progress")) {
					event.setCancelled(true);
					event.getPlayer().sendMessage(Constants.IN_PROGRESS_MESSAGE);
				} else {
					
					if(b.getRelative(BlockFace.DOWN) != null && b.getRelative(BlockFace.DOWN).getType() == Material.FURNACE){
						Furnace furnaceState = (Furnace) b.getRelative(BlockFace.DOWN).getState();
						if (furnaceState.getInventory().getName().equals(Constants.ALEMBIC_FURNACE_NAME)) {
							breakAlembicChest(b);
							breakAlembicStand(b.getRelative(BlockFace.UP));
							breakAlembicBellows(b.getRelative(BlockFace.DOWN));
							b.getWorld().dropItemNaturally(b.getLocation(),
									InventoryUtil.createGuiItem(ChatColor.YELLOW + "Alembic", Material.OBSERVER));
							event.setCancelled(true);
						}
					}
					
				}
			}
		}
	}

	/**
	 * Breaks the Alembic Chest
	 * 
	 * @param b
	 *            Alembic Chest Block
	 */
	public void breakAlembicChest(Block b) {
		if (b.getState() instanceof Chest) {
			Chest chestState = (Chest) b.getState();

			for (ItemStack i : AlembicHandler.getIngredients(chestState)) {
				if (i != null)
					b.getLocation().getWorld().dropItemNaturally(b.getLocation(), i);
			}

			for (ItemStack i : AlembicHandler.getShamanSaps(chestState)) {
				if (i != null)
					b.getLocation().getWorld().dropItemNaturally(b.getLocation(), i);
			}

			chestState.getBlockInventory().clear();
			// chestState.update(true);
			chestState.getBlock().setType(Material.AIR);
		} else {
			Bukkit.getServer().getLogger().warning(
					"breakAlembicChest was called, but the block is not a chest!" + b.getLocation().toString());
		}
	} // breakAlembicChest

	/**
	 * Breaks the Alembic BrewingStand
	 * 
	 * @param b
	 *            Alembic BrewingStand Block
	 */
	public void breakAlembicStand(Block b) {
		if (b.getState() instanceof BrewingStand) {
			BrewingStand bs = (BrewingStand) b.getState();

			for (ItemStack i : bs.getInventory().getContents()) {
				if (i != null)
					b.getLocation().getWorld().dropItemNaturally(b.getLocation(), i);
			}

			bs.getInventory().clear();

			b.setType(Material.AIR);
		}
	} // breakAlembicStand

	/**
	 * Breaks the Alembic Bellows
	 * 
	 * @param b
	 *            Alembic Bellows (Furnace) Block
	 */
	public void breakAlembicBellows(Block b) {
		if (b.getState() instanceof Furnace) {
			Furnace fur = (Furnace) b.getState();

			for (ItemStack i : fur.getInventory().getContents()) {
				if (i != null)
					b.getLocation().getWorld().dropItemNaturally(b.getLocation(), i);
			}

			fur.getInventory().clear();

			b.setType(Material.AIR);
		}

	} // breakAlembicBellows

} // class
