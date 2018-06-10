package com.gmail.sharpcastle33.util;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class AmbianceUtil {
	
	public static void alembicStartAmbiance(Block b) {
		
		World w = b.getWorld();
		Location chestLoc = b.getLocation();
		Location standLoc = b.getRelative(BlockFace.UP).getLocation();
		Location bellowsLoc = b.getRelative(BlockFace.DOWN).getLocation();
		
		
	}
	
	public static void alembicTickAmbiance(Block b) {
		
		World w = b.getWorld();
		Location chestLoc = b.getLocation();
		Location standLoc = b.getRelative(BlockFace.UP).getLocation();
		Location bellowsLoc = b.getRelative(BlockFace.DOWN).getLocation();
		
		w.playSound(standLoc, Sound.BLOCK_BREWING_STAND_BREW, 1, 1);
		
		
	}

}
