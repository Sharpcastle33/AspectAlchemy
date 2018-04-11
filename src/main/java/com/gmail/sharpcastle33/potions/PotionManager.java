package com.gmail.sharpcastle33.potions;

import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

public class PotionManager {
  
  private Map<CustomPotion, ItemStack> potions;
  
  public PotionManager(){
    
  }
  
  public void loadPotions(){
        
  }
  
  public ItemStack getPotion(CustomPotion p){
      ItemStack ret = potions.get(p);
      return ret;
  }
  
  private static ItemStack createPotionStack(PotionType type) {
    ItemStack stack = new ItemStack(Material.POTION);
    ItemMeta meta = stack.getItemMeta();
    if (meta == null) {
        meta = Bukkit.getItemFactory().getItemMeta(Material.POTION);
    }

    PotionMeta potion = (PotionMeta) meta;
    PotionData data = new PotionData(type);
    potion.setBasePotionData(data);
    stack.setItemMeta(potion);
    return stack;
}

}
