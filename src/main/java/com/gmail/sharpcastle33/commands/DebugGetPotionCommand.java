package com.gmail.sharpcastle33.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.gmail.sharpcastle33.potions.CustomPotion;
import com.gmail.sharpcastle33.potions.PotionManager;

public class DebugGetPotionCommand implements CommandExecutor {
	
	public static final String INSUFFICIENT_PERMISSIONS = ChatColor.RED+"[AspectAlchemy] You do not have permission to use this command!";

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender instanceof Player && args.length > 0) {
			Player player = (Player) sender;
			
			if(!player.isOp()) {
				player.sendMessage(INSUFFICIENT_PERMISSIONS);
				return true;
			} // if
			
			if(CustomPotion.valueOf(args[0]) != null) {
				ItemStack potion = PotionManager.getPotion(CustomPotion.valueOf(args[0]));
				player.getInventory().addItem(potion);
				player.sendMessage(ChatColor.YELLOW+"Gave "+potion.getItemMeta().getDisplayName()+ChatColor.YELLOW+" to "+player.getName());
			} // if
		} // if
		
		return true;
	} // onCommand

} // class
