package com.gmail.sharpcastle33.util;

import org.bukkit.Location;
import org.bukkit.Particle;
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
	
	public static void alembicSuccessAmbiance(Block b) {
		
		World w = b.getWorld();
		Location chestLoc = b.getLocation();
		Location standLoc = b.getRelative(BlockFace.UP).getLocation();
		Location bellowsLoc = b.getRelative(BlockFace.DOWN).getLocation();
		
		w.playSound(standLoc, Sound.ITEM_BOTTLE_EMPTY, 1, 1);
		w.spawnParticle(Particle.SMOKE_NORMAL, standLoc, 30, 0.5, 0, 0.5, 0.05);
		
		
	}
	
	public static void alembicTickAmbiance(Block b) {
		
		World w = b.getWorld();
		Location chestLoc = b.getLocation();
		Location standLoc = b.getRelative(BlockFace.UP).getLocation();
		Location bellowsLoc = b.getRelative(BlockFace.DOWN).getLocation();
		
		w.playSound(standLoc, Sound.BLOCK_BREWING_STAND_BREW, 1, 1);
		w.spawnParticle(Particle.SMOKE_NORMAL, standLoc, 20, 0.5, 0, 0.5, 0.1);
		
		
	}

}
