package com.gmail.sharpcastle33;

import java.io.File;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.sharpcastle33.aspects.AspectManager;
import com.gmail.sharpcastle33.aspects.AspectRecipeManager;
import com.gmail.sharpcastle33.handlers.AlembicHandler;
import com.gmail.sharpcastle33.listeners.AlembicBreakListener;
import com.gmail.sharpcastle33.listeners.AlembicCreationListener;
import com.gmail.sharpcastle33.listeners.AlembicExploitListener;
import com.gmail.sharpcastle33.listeners.AlembicGUI;
import com.gmail.sharpcastle33.listeners.ThaumaturgicalResonatorListener;
import com.gmail.sharpcastle33.potions.PotionManager;

public class AspectAlchemy extends JavaPlugin {

	public static AlembicManager alembicMan;

	Plugin plugin;

	public void onEnable() {

		plugin = this;

		alembicMan = new AlembicManager(plugin);


		AlembicHandler.init(plugin);
		AspectManager.init(new File(this.getDataFolder(), "aspects.yaml"));
		PotionManager.init(new File(this.getDataFolder(), "potions.yaml"));
		AspectRecipeManager.init(new File(this.getDataFolder(), "recipes.yaml"));
		
		getServer().getPluginManager().registerEvents(new AlembicCreationListener(), plugin);
		getServer().getPluginManager().registerEvents(new AlembicBreakListener(), plugin);
		getServer().getPluginManager().registerEvents(new AlembicExploitListener(), plugin);
		getServer().getPluginManager().registerEvents(new AlembicGUI(), plugin);
		
		getServer().getPluginManager().registerEvents(new ThaumaturgicalResonatorListener(), plugin);


	}

	public void onDisable() {
		alembicMan.saveActiveAlembics(plugin);

		saveConfig();
	}
}
