package com.gmail.sharpcastle33.listeners;

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

public class AlembicBreakListener implements Listener{
	
	public final static String IN_PROGRESS_MESSAGE = ChatColor.RED + "Alembics cannot be broken while in progress!";

	@EventHandler
	public void alembicBreakEvent(BlockBreakEvent event) {
		Block b = event.getBlock();
		
		if(b.getType() == Material.FURNACE) {
			Furnace furnaceState = (Furnace) b.getState();
			if(furnaceState.getInventory().getName().equals(AlembicCreationListener.ALEMBIC_FURNACE_NAME)) {
				if(b.getRelative(BlockFace.UP) instanceof Chest) {
					Chest chestState = (Chest) b.getRelative(BlockFace.UP).getState();
					if (chestState.getInventory().getItem(17).hasItemMeta() && chestState.getInventory().getItem(17).getItemMeta().getDisplayName().equals(ChatColor.RED + "In Progress")) {
						event.setCancelled(true);
						event.getPlayer().sendMessage(IN_PROGRESS_MESSAGE);
					}
				}
			}
		}
		
		else if(b.getType() == Material.BREWING_STAND) {
			BrewingStand brewingStandState = (BrewingStand) b.getState();
			if(brewingStandState.getInventory().getName().equals(AlembicCreationListener.ALEMBIC_BREWINGSTAND_NAME)) {
				if(b.getRelative(BlockFace.DOWN) instanceof Chest) {
					Chest chestState = (Chest) b.getRelative(BlockFace.DOWN).getState();
					if (chestState.getInventory().getItem(17).hasItemMeta() && chestState.getInventory().getItem(17).getItemMeta().getDisplayName().equals(ChatColor.RED + "In Progress")) {
						event.setCancelled(true);
						event.getPlayer().sendMessage(IN_PROGRESS_MESSAGE);
					}
				}
			}
		}
		
		else if(b.getType() == Material.CHEST) {
			Chest chestState = (Chest) b.getState();
			if(chestState.getInventory().getName().equals(AlembicCreationListener.ALEMBIC_CHEST_NAME)) {
				
				if(event.isCancelled()) { return; }
				
				
				if (chestState.getInventory().getItem(17).hasItemMeta() && chestState.getInventory().getItem(17).getItemMeta().getDisplayName().equals(ChatColor.RED + "In Progress")) {
					event.setCancelled(true);
					event.getPlayer().sendMessage(IN_PROGRESS_MESSAGE);
				}
			}
		}
	}
}
