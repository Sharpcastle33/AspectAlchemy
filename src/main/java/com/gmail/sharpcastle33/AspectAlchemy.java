package com.gmail.sharpcastle33;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import com.gmail.sharpcastle33.aspects.AspectManager;
import com.gmail.sharpcastle33.aspects.AspectRecipeManager;
import com.gmail.sharpcastle33.listeners.AlembicCreationListener;
import com.gmail.sharpcastle33.listeners.AlembicGUI;
import com.gmail.sharpcastle33.potions.PotionManager;

public class AspectAlchemy extends JavaPlugin {
  
  AlembicManager alembicMan;
  PotionManager potionMan;
  AspectManager aspectMan;
  AspectRecipeManager recipeMan;
  Plugin plugin;
  
  public void onEnable(){
    
    plugin = this;

    alembicMan = new AlembicManager(plugin);
    potionMan = new PotionManager();
    aspectMan = new AspectManager();
    recipeMan = new AspectRecipeManager();
    
    potionMan.loadPotions();
    recipeMan.loadRecipes();

    
    getServer().getPluginManager().registerEvents(new AlembicCreationListener(), plugin);
    getServer().getPluginManager().registerEvents(new AlembicGUI(), plugin);

    
  }
  
  public void onDisable(){
    alembicMan.saveActiveAlembics(plugin);

    saveConfig();
  }

}
