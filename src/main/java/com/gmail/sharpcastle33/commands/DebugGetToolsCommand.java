package com.gmail.sharpcastle33.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gmail.sharpcastle33.Constants;

import net.md_5.bungee.api.ChatColor;

public class DebugGetToolsCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender instanceof Player) {
			Player player = (Player) sender;
			
			if(!player.isOp()) {
				player.sendMessage(Constants.INSUFFICIENT_PERMISSIONS);
				return true;
			} // if
			
			ItemStack adminTool = new ItemStack(Material.STICK);
			ItemStack aspectTool = new ItemStack(Material.STICK);
			ItemStack potionTool = new ItemStack(Material.STICK);
			ItemStack brewingTool = new ItemStack(Material.STICK);
			ItemStack counterTool = new ItemStack(Material.STICK);
			ItemStack bugfixer = new ItemStack(Material.STICK);
			ItemStack debugger = new ItemStack(Material.STICK);

			ItemStack alembic = new ItemStack(Material.OBSERVER);
			ItemStack resonator = new ItemStack(Constants.RESONATOR_ITEM);
			
			ItemMeta adminMeta = adminTool.getItemMeta();
			ItemMeta aspectMeta = aspectTool.getItemMeta();
			ItemMeta potionMeta = potionTool.getItemMeta();
			ItemMeta brewingMeta = brewingTool.getItemMeta();
			ItemMeta counterMeta = counterTool.getItemMeta();
			ItemMeta alembicMeta = alembic.getItemMeta();
			ItemMeta resonatorMeta = resonator.getItemMeta();
			ItemMeta bugfixerMeta = bugfixer.getItemMeta();
			ItemMeta debuggerMeta = debugger.getItemMeta();
			
			adminMeta.setDisplayName(Constants.DEBUG_ITEM);
			aspectMeta.setDisplayName(Constants.ASPECT_ADMIN_TOOL);
			potionMeta.setDisplayName(Constants.POTION_ADMIN_TOOL);
			brewingMeta.setDisplayName(Constants.INSTANT_ADMIN_TOOL);
			counterMeta.setDisplayName(Constants.COUNTER_ADMIN_TOOL);
			alembicMeta.setDisplayName(Constants.ALEMBIC_ITEM_NAME);
			resonatorMeta.setDisplayName(Constants.RESONATOR_NAME);
			bugfixerMeta.setDisplayName(ChatColor.RED + "Alembic Fixer");
			debuggerMeta.setDisplayName(ChatColor.RED + "Alembic Debugger");
			
			adminTool.setItemMeta(adminMeta);
			aspectTool.setItemMeta(aspectMeta);
			potionTool.setItemMeta(potionMeta);
			brewingTool.setItemMeta(brewingMeta);
			counterTool.setItemMeta(counterMeta);
			alembic.setItemMeta(alembicMeta);
			resonator.setItemMeta(resonatorMeta);
			bugfixer.setItemMeta(bugfixerMeta);
			debugger.setItemMeta(debuggerMeta);
			
			player.getInventory().addItem(adminTool);
			player.getInventory().addItem(aspectTool);
			player.getInventory().addItem(potionTool);
			player.getInventory().addItem(brewingTool);
			player.getInventory().addItem(counterTool);
			player.getInventory().addItem(alembic);
			player.getInventory().addItem(resonator);
			player.getInventory().addItem(bugfixer);
			player.getInventory().addItem(debugger);
			
		} // if
		
		return true;
	} // onCommand
	
} // class
