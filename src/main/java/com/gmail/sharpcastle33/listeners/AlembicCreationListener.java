package com.gmail.sharpcastle33.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Chest;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import com.gmail.sharpcastle33.AlembicManager;
import com.gmail.sharpcastle33.handlers.AlembicHandler;
import com.gmail.sharpcastle33.util.InventoryUtil;

/**
 * Handles Alembic creation logic
 */
public class AlembicCreationListener implements Listener {

	public static final String ALEMBIC_ITEM_NAME = ChatColor.YELLOW + "Alembic";
	public static final String ALEMBIC_CONSTRUCTION = ChatColor.RED + "Constructing Alembic...";
	public static final String ALEMBIC_CHEST_NAME = ChatColor.BLUE + "Alembic Chamber";
	public static final String ALEMBIC_FURNACE_NAME = ChatColor.BLUE + "Alembic Bellows";
	public static final String ALEMBIC_BREWINGSTAND_NAME = ChatColor.BLUE + "Alembic Stand";

	@EventHandler
	public void alembicPlaceEvent(BlockPlaceEvent event) {
		Block b = event.getBlock();
		ItemStack item = event.getItemInHand();
		Player p = event.getPlayer();
		
		// Ensure that no chest is placed next to an alembic
		if(item.getType() == Material.CHEST) {
			for (int x = -1; x <= 1; x++) {
				for (int z = -1; z <= 1; z++) {
					if (z != 0 || x != 0) {
						Block block = b.getRelative(x, 0, z);
						if(AlembicHandler.isAlembic(block)) { 
							event.setCancelled(true);
							return;
						} // if
					} // if
				} // for
			} // for
		} // if

		// Alembic Creation Check
		if (item.getType() == Material.OBSERVER) {
			if (item.hasItemMeta()) {
				ItemMeta meta = item.getItemMeta();
				if (meta.hasDisplayName() && meta.getDisplayName().equals(ALEMBIC_ITEM_NAME)) {
					if (isValidAlembicPosition(b.getLocation())) {
						// Begin alembic placement
						if (event.isCancelled() == false) {
							p.sendMessage(ALEMBIC_CONSTRUCTION);

							World world = b.getWorld();
							Location loc = b.getLocation();

							Block alembicChest = world.getBlockAt(loc.add(new Vector(0, 1, 0)));
							alembicChest.setType(Material.CHEST);

							Block alembicFurnace = b;
							alembicFurnace.setType(Material.FURNACE);

							Block alembicBrewingstand = world.getBlockAt(loc.add(new Vector(0, 1, 0)));
							alembicBrewingstand.setType(Material.BREWING_STAND);

							Furnace furnaceState = (Furnace) world.getBlockAt(loc.add(new Vector(0, -2, 0))).getState();
							furnaceState.update(true);
							furnaceState.setCustomName(ALEMBIC_FURNACE_NAME);
							furnaceState.getInventory().setSmelting(InventoryUtil.constructNullItem());
							furnaceState.update(true);

							BrewingStand brewingStandState = (BrewingStand) alembicBrewingstand.getState();
							brewingStandState.setCustomName(ALEMBIC_BREWINGSTAND_NAME);
							brewingStandState.getInventory().setFuel(InventoryUtil.constructNullItem());
							brewingStandState.getInventory().setIngredient(InventoryUtil.constructNullItem());
							brewingStandState.update(true);

							Chest chestState = (Chest) alembicChest.getState();
							chestState.setCustomName(ALEMBIC_CHEST_NAME);
							// p.sendMessage(chestState.getCustomName() + "_" +
							// chestState.getInventory().getName());
							chestState.update(true);

							ItemStack ni = InventoryUtil.constructNullItem();
							ItemStack info = InventoryUtil.createGuiItem(ChatColor.BLUE + "Information",
									Material.PAPER);
							ItemStack book = InventoryUtil.createGuiItem(ChatColor.BLUE + "Alembic Tutorial",
									Material.BOOK);
							ItemStack start = InventoryUtil.createGuiItem(ChatColor.GREEN + "Start Alchemy",
									Material.PAPER);
							ItemStack[] contents = { null, ni, null, null, null, null, null, ni, info, null, ni, null,
									null, null, null, null, ni, start, null, ni, null, null, null, null, null, ni,
									book };
							chestState.getBlockInventory().setContents(contents);
						}

					} else {
						event.setCancelled(true);
						return;
					}
				}
			}
		}
	} // alembicPlaceEvent

	/**
	 * Determines whether the place in which the Alembic is being placed is a valid
	 * location
	 * 
	 * @param loc
	 *            Location place location
	 * @return false if the location is invalid
	 */
	private boolean isValidAlembicPosition(Location loc) {
		// Ensure there are no chests or alembics immediately next to the location
		for (int x = -1; x <= 1; x++) {
			for (int z = -1; z <= 1; z++) {
				for (int y = 0; y < 3; y++) {
					if (z != 0 || x != 0) {
						Block block = loc.getBlock().getRelative(x, y, z);
						if(block instanceof Chest) return false;
						if(AlembicHandler.isAlembic(block)) return false;
					} // if
				} // for
			} // for
		} // for
		
		// Ensure the two blocks above the location are air
		if (loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY() + 1, loc.getBlockZ())
				.getType() == Material.AIR) {
			if (loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY() + 2, loc.getBlockZ())
					.getType() == Material.AIR) {
				return true;
			} // if
		} // if

		return false;
	} // isValidAlembicPosition

} // class
