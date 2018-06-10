package com.gmail.sharpcastle33.util;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Inventory Utility used to create a GUI
 */
public class InventoryUtil {

	/**
	 * Constructs an IronFence type ItemStack with no name
	 * @return ItemStack
	 */
	public static ItemStack constructNullItem() {
		ItemStack stack = new ItemStack(Material.IRON_FENCE);
		// stack.setData(new MaterialData(Material.STAINED_GLASS_PANE, (byte) 15));
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "");
		stack.setItemMeta(meta);
		return stack;
	} // constructNullItem

	/**
	 * Constructs a GUI item with loretext
	 * @param name String name of item
	 * @param desc ArrayList of String loretext
	 * @param mat Material type of item
	 * @return ItemStack
	 */
	public static ItemStack createGuiItem(String name, ArrayList<String> desc, Material mat) {
		ItemStack i = new ItemStack(mat, 1);
		ItemMeta iMeta = i.getItemMeta();
		iMeta.setDisplayName(name);
		iMeta.setLore(desc);
		i.setItemMeta(iMeta);
		return i;
	} // creatGuiItem(String, ArrayList<String>, Material)

	/**
	 * Constructs a GUI item
	 * @param name String name of item
	 * @param mat Material type of item
	 * @return ItemStack
	 */
	public static ItemStack createGuiItem(String name, Material mat) {
		ItemStack i = new ItemStack(mat, 1);
		ItemMeta iMeta = i.getItemMeta();
		iMeta.setDisplayName(name);
		i.setItemMeta(iMeta);
		return i;
	} // createGuiItem(String, Material)
	
} // class
