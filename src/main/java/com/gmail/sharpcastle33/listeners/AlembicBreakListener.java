package com.gmail.sharpcastle33.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Chest;
import org.bukkit.block.Furnace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class AlembicBreakListener implements Listener{

	@EventHandler
	public void alembicBreakEvent(BlockBreakEvent event) {
		Block b = event.getBlock();
		
		if(b.getType() == Material.FURNACE) {
			Furnace furnaceState = (Furnace) b.getState();
		}
		
		if(b.getType() == Material.BREWING_STAND) {
			BrewingStand furnaceState = (BrewingStand) b.getState();

		}
		
		if(b.getType() == Material.CHEST) {
			Chest furnaceState = (Chest) b.getState();

		}
	}
}
