package com.gmail.sharpcastle33;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class AlembicManager {
	
	private static File configFile;
	private static FileConfiguration config;
	
	public static List<Location> alembics;
	
	public static void init(File file) {
		configFile = file;
		
		try {
			config = YamlConfiguration.loadConfiguration(configFile);
			alembics = loadAlembics();
		} catch (IllegalArgumentException e1) {
			Bukkit.getLogger().severe("Alembics config does not exist! Creating...");
			try {
				configFile.createNewFile();
			} catch(IOException e2) {
				Bukkit.getLogger().severe("Unable to create Alembics config!");
				e2.printStackTrace();
			}
			e1.printStackTrace();
		} // try/catch
	} // init
	
	public static void saveAlembics() {
		config = new YamlConfiguration();
		
		for(Location location: alembics) {
			String world = location.getWorld().getName();
			double x = location.getX();
			double y = location.getY();
			double z = location.getZ();
			
			Map<String, Object> values = new HashMap<>();
			values.put("world", world);
			values.put("x", x);
			values.put("y", y);
			values.put("z", z);
			
			config.createSection(x + " " + y + " " + z, values);
		} // for
		
		try {
			config.save(configFile);
		} catch (IOException e) {
			Bukkit.getLogger().severe("Unable to save Alembics config!");
			e.printStackTrace();
		} // try/catch
	} // saveAlembics
	
	private static List<Location> loadAlembics() {
		List<Location> configAlembics = new ArrayList<>();
		for(String alembicKey : config.getKeys(false)) {
			ConfigurationSection alembicSection = config.getConfigurationSection(alembicKey);
			
			World world = Bukkit.getWorld(alembicSection.getString("world"));
			double x = alembicSection.getDouble("x");
			double y = alembicSection.getDouble("y");
			double z = alembicSection.getDouble("z");
			
			configAlembics.add(new Location(world, x, y, z));
		} // for

		return configAlembics;
	} // loadAlembics
	
	public static void activateAlembic(Location location) {
		if(!alembics.contains(location)) alembics.add(location);
	} // activateAlembic
	
	public static void deactivateAlembic(Location location) {
		if(alembics.contains(location)) alembics.remove(location);
	}
	
} // class
