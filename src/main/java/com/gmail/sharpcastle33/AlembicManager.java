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
import com.gmail.sharpcastle33.handlers.AlembicTickTask;

/**
 * Manages the alembics.yaml file which is used to save the locations of active
 * alembics through restarts and reloads
 * 
 * @author KingVictoria
 */
public class AlembicManager {

	private static File configFile;
	private static FileConfiguration config;

	public static List<Location> alembics;

	/**
	 * Loads from config and creates the alembics List
	 * 
	 * @param file
	 *            config File
	 * @author KingVictoria
	 */
	public static void init(File file) {
		configFile = file;

		try {
			config = YamlConfiguration.loadConfiguration(configFile);
			alembics = loadAlembics();
		} catch (IllegalArgumentException e1) {
			Bukkit.getLogger().severe("Alembics config does not exist! Creating...");
			try {
				configFile.createNewFile();
				alembics = new ArrayList<>();
			} catch (IOException e2) {
				Bukkit.getLogger().severe("Unable to create Alembics config!");
				e2.printStackTrace();
			}
			e1.printStackTrace();
		} // try/catch
	} // init

	/**
	 * Saves the alembics List to the config
	 * 
	 * @author KingVictoria
	 */
	public static void saveAlembics() {
		config = new YamlConfiguration();

		for (Location location : alembics) {
			String world = location.getWorld().getName();
			double x = location.getX();
			double y = location.getY();
			double z = location.getZ();

			Map<String, Object> values = new HashMap<>();
			values.put("world", world);
			values.put("x", x);
			values.put("y", y);
			values.put("z", z);

			config.createSection(world + "_" + ((int) x) + "_" + ((int) y) + "_" + ((int) z), values);
		} // for

		try {
			config.save(configFile);
		} catch (IOException e) {
			Bukkit.getLogger().severe("Unable to save Alembics config!");
			e.printStackTrace();
		} // try/catch
	} // saveAlembics

	/**
	 * Loads the alembics from config
	 * 
	 * @return alembics List (of locations)
	 * @author KingVictoria
	 */
	private static List<Location> loadAlembics() {
		List<Location> configAlembics = new ArrayList<>();
		for (String alembicKey : config.getKeys(false)) {
			ConfigurationSection alembicSection = config.getConfigurationSection(alembicKey);

			World world = Bukkit.getWorld(alembicSection.getString("world"));
			double x = alembicSection.getDouble("x");
			double y = alembicSection.getDouble("y");
			double z = alembicSection.getDouble("z");

			configAlembics.add(new Location(world, x, y, z));
			
		    new AlembicTickTask(new Location(world, x, y, z)).runTaskTimer(AspectAlchemy.plugin, Constants.ALEMBIC_TICK_TIME, Constants.ALEMBIC_TICK_TIME);

		} // for

		return configAlembics;
	} // loadAlembics

	/**
	 * Activates an alembic (saves its location while the alembic is active)
	 * 
	 * @param location
	 *            Location of Alembic
	 * @author KingVictoria
	 */
	public static void activateAlembic(Location location) {
		if (!alembics.contains(location))
			alembics.add(location);
	} // activateAlembic

	/**
	 * Deactivates and alembic (dereferences its location since the alembic reaction
	 * is complete)
	 * 
	 * @param location
	 *            Location of Alembic
	 * @author KingVictoria
	 */
	public static void deactivateAlembic(Location location) {
		if (alembics.contains(location))
			alembics.remove(location);
	} // deactivateAlembic

} // class
