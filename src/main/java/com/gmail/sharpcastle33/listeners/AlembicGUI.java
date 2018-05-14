package com.gmail.sharpcastle33.listeners;

import org.bukkit.Nameable;
import org.bukkit.block.Block;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Chest;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import com.gmail.sharpcastle33.AspectAlchemy;
import com.gmail.sharpcastle33.handlers.AlembicHandler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;

public class AlembicGUI implements Listener{
  
  public static final String IN_PROGRESS = ChatColor.RED + "Inventories of Alembics cannot be modified while they are in progress!";
  
  @EventHandler
  public void alembicGUI(InventoryClickEvent event){
    if(!(event.getWhoClicked() instanceof Player)){
      return;
    }
    
    Player p = (Player) event.getWhoClicked();
    ItemStack clicked = event.getCurrentItem();
    String invName = event.getInventory().getName();
    
    
    if(!(invName.equals(AlembicCreationListener.ALEMBIC_CHEST_NAME)
        || invName.equals(AlembicCreationListener.ALEMBIC_BREWINGSTAND_NAME)
        || invName.equals(AlembicCreationListener.ALEMBIC_FURNACE_NAME))){
      return;
    }
    
    if (clicked == null) {
    	return;
    }
    
    if (!clicked.hasItemMeta()) {
    	return;
    }
    
    ItemMeta clickedMeta = clicked.getItemMeta();
    
    if (!clickedMeta.hasDisplayName()) {
    	return;
    }
    
    if (AspectAlchemy.alembicMan.activeAlembics.contains(event.getClickedInventory().getLocation())) {
    	if (clickedMeta.getDisplayName().equals(ChatColor.BLUE + "Alembic Tutorial")) {
            event.setCancelled(true);
        	p.sendMessage("You've clicked the tutorial button");
        }
    	p.sendMessage(ChatColor.RED + "Alembic cannot be manipulated while active");
    	event.setCancelled(true);
    	return;
    }
    
    if (clickedMeta.getDisplayName().equals(ChatColor.RED + "")) {
    	event.setCancelled(true);
    }
    
    // Implement information thing
    if (clickedMeta.getDisplayName().equals(ChatColor.BLUE + "Information")) {
        event.setCancelled(true);
    	p.sendMessage("Don't do that");
    	ItemStack bind = new ItemStack(Material.PAPER);
    	bind.setItemMeta(Bukkit.getItemFactory().getItemMeta(Material.PAPER));
    	ItemMeta meta = bind.getItemMeta();
    	meta.setDisplayName("Binding Agent");
    	bind.setItemMeta(meta);
    	p.getInventory().addItem(bind);
    }
    
    // Implement tutorial
    if (clickedMeta.getDisplayName().equals(ChatColor.BLUE + "Alembic Tutorial")) {
        event.setCancelled(true);
    	p.sendMessage("You've clicked the tutorial button");
    }
    
    // Implement start alchemy
    if (clickedMeta.getDisplayName().equals(ChatColor.GREEN + "Start Alchemy")) {
        event.setCancelled(true);

    	if (!(event.getInventory().getHolder() instanceof Chest)) {
    		p.sendMessage(ChatColor.RED + "Fatal error. Please report. Inventory not instance of chest.");
    		return;
    	}
    	p.sendMessage("Alchemy started");
    	AlembicHandler.startAlchemy(event.getInventory().getLocation().getBlock(), p.getName());
    }    
  }

}
