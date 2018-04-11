package com.gmail.sharpcastle33.listeners;

import org.bukkit.Nameable;
import org.bukkit.block.Block;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Chest;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import com.gmail.sharpcastle33.handlers.AlembicHandler;
import net.md_5.bungee.api.ChatColor;

public class AlembicGUI implements Listener{
  
  public static final String IN_PROGRESS = ChatColor.RED + "Inventories of Alembics cannot be modified while they are in progress!";
  
  @EventHandler
  public void alembicGUI(InventoryClickEvent event){
    if(!(event.getWhoClicked() instanceof Player)){
      return;
    }
    
    Player p = (Player) event.getWhoClicked();

    
    
    String invName = event.getInventory().getName();
    
    
   
    if(!(invName.equals(AlembicCreationListener.ALEMBIC_CHEST_NAME)
        || invName.equals(AlembicCreationListener.ALEMBIC_BREWINGSTAND_NAME)
        || invName.equals(AlembicCreationListener.ALEMBIC_FURNACE_NAME))){
      return;
    }
    
    ItemStack clicked = event.getCurrentItem();
    
    if(invName.equals(AlembicCreationListener.ALEMBIC_CHEST_NAME)){
      p.sendMessage("Alchemy: AlembicGUI");
      if(event.getInventory().getItem(17).hasItemMeta() && event.getInventory().getItem(17).getItemMeta().getDisplayName() == ChatColor.RED + "In Progress"){
        event.setCancelled(true);
        p.closeInventory();
        p.sendMessage(IN_PROGRESS);
      }
    }
    
    if(clicked.hasItemMeta()){
      ItemMeta meta = clicked.getItemMeta();
     
      if(meta.getDisplayName().equals(ChatColor.RED + "")){
        event.setCancelled(true);
      }
      
      if(meta.getDisplayName().equals(ChatColor.BLUE + "Information")){
       event.setCancelled(true); 
      }
      
      if(meta.getDisplayName().equals(ChatColor.BLUE + "Alembic Tutorial")){
        event.setCancelled(true);
      }
      
      if(meta.getDisplayName().equals(ChatColor.GREEN + "Start Alchemy")){
        p.sendMessage("unimplemented");
        
        if(!(event.getInventory().getHolder() instanceof Block)){
          p.sendMessage(ChatColor.DARK_RED + " FATAL ERROR, INVENTORY HOLDER NOT INSTANCE OF BLOCK. REPORT TO STAFF");
          return;
        }
        AlembicHandler.startAlchemy((Block) event.getInventory().getHolder(), p.getName());
      }
    }
    
    
  }

}
