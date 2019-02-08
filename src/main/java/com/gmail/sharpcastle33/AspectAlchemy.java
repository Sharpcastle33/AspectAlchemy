package com.gmail.sharpcastle33;

import java.io.File;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import com.gmail.sharpcastle33.aspects.AspectManager;
import com.gmail.sharpcastle33.aspects.AspectRecipeManager;
import com.gmail.sharpcastle33.commands.DebugGetPotionCommand;
import com.gmail.sharpcastle33.commands.DebugGetRecipeCommand;
import com.gmail.sharpcastle33.commands.DebugGetToolsCommand;
import com.gmail.sharpcastle33.commands.DebugListPotsCommand;
import com.gmail.sharpcastle33.handlers.AlembicHandler;
import com.gmail.sharpcastle33.listeners.AdminToolsListener;
import com.gmail.sharpcastle33.listeners.AlembicBreakListener;
import com.gmail.sharpcastle33.listeners.AlembicCreationListener;
import com.gmail.sharpcastle33.listeners.AlembicExploitListener;
import com.gmail.sharpcastle33.listeners.AlembicGUI;
import com.gmail.sharpcastle33.listeners.PotionListener;
import com.gmail.sharpcastle33.listeners.ThaumaturgicalResonatorListener;
import com.gmail.sharpcastle33.potions.PotionManager;

public class AspectAlchemy extends JavaPlugin {

	public static Plugin plugin;

	public void onEnable() {
		plugin = this;

		Constants.load(getConfig(), plugin);																// Loads constants from config
		AlembicManager.init(new File(this.getDataFolder(), "alembics.yaml"));							// Loads active alembics from file
		AlembicHandler.init(plugin);																	// Begins all Alembic Tasks
		AspectManager.init(new File(this.getDataFolder(), "aspects.yaml"));								// Loads item aspects from file
		PotionManager.init(new File(this.getDataFolder(), "potions.yaml"));								// Loads potions from file
		AspectRecipeManager.init(new File(this.getDataFolder(), "recipes.yaml"));						// Loads recipes from file

		getServer().getPluginManager().registerEvents(new AlembicCreationListener(), plugin);			// Detects and handles alembic creation
		getServer().getPluginManager().registerEvents(new AlembicBreakListener(), plugin);				// Detects and handles alembic breakage
		getServer().getPluginManager().registerEvents(new AlembicExploitListener(), plugin);			// Detects and prevents alembic hopper exploit
		getServer().getPluginManager().registerEvents(new AlembicGUI(), plugin);						// Handles alembic GUI
		getServer().getPluginManager().registerEvents(new ThaumaturgicalResonatorListener(), plugin);	// Handles Thaumaturgical Resonator
		getServer().getPluginManager().registerEvents(new AdminToolsListener(), plugin);				// Handles admin tools
	    getServer().getPluginManager().registerEvents(new PotionListener(), plugin);           // Handles custom potion effects

		
		getCommand("getpotion").setExecutor(new DebugGetPotionCommand());								// Handles getpotion command
		getCommand("getrecipe").setExecutor(new DebugGetRecipeCommand());								// Handles getrecipe command
		getCommand("listpots").setExecutor(new DebugListPotsCommand());									// Handles listpots command
		getCommand("gettools").setExecutor(new DebugGetToolsCommand());									// Handles gettools command

	} // onEnable

	public void onDisable() {
		AlembicManager.saveAlembics();
		saveConfig();
	} // onDisable
} // class
