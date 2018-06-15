package com.gmail.sharpcastle33.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gmail.sharpcastle33.listeners.AdminToolsListener;

public class DebugGetToolsCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender instanceof Player) {
			Player player = (Player) sender;
			
			if(!player.isOp()) {
				player.sendMessage(DebugGetPotionCommand.INSUFFICIENT_PERMISSIONS);
				return true;
			} // if
			
			ItemStack aspectTool = new ItemStack(Material.STICK);
			ItemStack potionTool = new ItemStack(Material.STICK);
			ItemStack brewingTool = new ItemStack(Material.STICK);
			
			ItemMeta aspectMeta = aspectTool.getItemMeta();
			ItemMeta potionMeta = potionTool.getItemMeta();
			ItemMeta brewingMeta = brewingTool.getItemMeta();
			
			aspectMeta.setDisplayName(AdminToolsListener.ASPECT_ADMIN_TOOL);
			potionMeta.setDisplayName(AdminToolsListener.POTION_ADMIN_TOOL);
			brewingMeta.setDisplayName(AdminToolsListener.INSTANT_ADMIN_TOOL);
			
			aspectTool.setItemMeta(aspectMeta);
			potionTool.setItemMeta(potionMeta);
			brewingTool.setItemMeta(brewingMeta);
			
			player.getInventory().addItem(aspectTool);
			player.getInventory().addItem(potionTool);
			player.getInventory().addItem(brewingTool);
		} // if
		
		return true;
	} // onCommand
	
} // class
