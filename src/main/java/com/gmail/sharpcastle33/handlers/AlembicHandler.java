package com.gmail.sharpcastle33.handlers;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import net.md_5.bungee.api.ChatColor;

public class AlembicHandler {
  
  public static void startAlchemy(Block b, String name){
    updateAlembicInfo((Chest) b.getState(), name);
  }
  
  public static void updateAlembicInfo(Chest c, String name){
    ItemStack info = c.getBlockInventory().getItem(8);
    if(info.hasItemMeta()){
      ItemMeta meta = info.getItemMeta();
      Date now = new Date();
      SimpleDateFormat time = new SimpleDateFormat("HH:mm");
      String lore[] = {ChatColor.BLUE + "Started by: " + ChatColor.GOLD + name, ChatColor.BLUE + "Began at: " + ChatColor.RED + time.format(now)};
      meta.setLore(Arrays.asList(lore));
    }
    c.getInventory().setItem(8, info);
  }

  public static ItemStack[] getBindingAgents(Chest chest){
    ItemStack[] ret = new ItemStack[3];
    ret[0] = chest.getInventory().getItem(0);
    ret[1] = chest.getInventory().getItem(9);
    ret[2] = chest.getInventory().getItem(18);
    return ret;
  }
  
  public static ItemStack[] getIngredients(Chest chest){
    ItemStack[] ret = new ItemStack[15];
    
    int slot = 2;
    
    for(int counter = 0; counter < 16; counter++){
      ret[counter] = chest.getInventory().getItem(slot);
      
      if(slot == 6 || slot == 15){
        slot+=4;
        continue;
      }
      slot++;
    }
    return ret;
  }
}
