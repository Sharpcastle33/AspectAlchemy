package com.gmail.sharpcastle33.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;
import net.md_5.bungee.api.ChatColor;

public class PotionListener implements Listener {
  
  String ANTIDOTE = ChatColor.YELLOW + "Antidote";
  String STRONG_ANTIDOTE = ChatColor.YELLOW + "Potent Antidote";
  
  String ANTI_BURN = ChatColor.YELLOW + "Vitarun Elixir";
  
  @EventHandler
  public void onDrink(PlayerItemConsumeEvent event) {
    ItemStack i = event.getItem();
    Player p = event.getPlayer();
    
    if(i.hasItemMeta() && i.getItemMeta().hasDisplayName()) {
      ItemMeta meta = i.getItemMeta();
      
      //ANTIDOTE
      if(meta.getDisplayName().equals(ANTIDOTE) || meta.getDisplayName().equals(STRONG_ANTIDOTE)) {
        if(p.hasPotionEffect(PotionEffectType.POISON)) {
          p.removePotionEffect(PotionEffectType.POISON);
        }
      }
      
      //ANTI BURN
      if(meta.getDisplayName().equals(ANTI_BURN)) {
        if(p.getFireTicks() > 0) {
          p.setFireTicks(0);
        }
      }
      
    }
  }

}
