package com.gmail.sharpcastle33.util;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryUtil {
  
  public static ItemStack constructNullItem(){
    ItemStack stack = new ItemStack(Material.IRON_FENCE);
    //stack.setData(new MaterialData(Material.STAINED_GLASS_PANE, (byte) 15));
    ItemMeta meta = stack.getItemMeta();
    meta.setDisplayName(ChatColor.RED + "");
    stack.setItemMeta(meta);
    return stack;  
  }
  
  public static ItemStack createGuiItem(String name, ArrayList<String> desc, Material mat) {
    ItemStack i = new ItemStack(mat, 1);
    ItemMeta iMeta = i.getItemMeta();
    iMeta.setDisplayName(name);
    iMeta.setLore(desc);
    i.setItemMeta(iMeta);
    return i;
}

  public static ItemStack createGuiItem(String name, Material mat) {
    ItemStack i = new ItemStack(mat, 1);
    ItemMeta iMeta = i.getItemMeta();
    iMeta.setDisplayName(name);
    i.setItemMeta(iMeta);
    return i;
}
}
