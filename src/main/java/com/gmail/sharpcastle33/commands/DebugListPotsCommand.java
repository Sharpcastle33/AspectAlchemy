package com.gmail.sharpcastle33.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.sharpcastle33.potions.CustomPotion;
import com.gmail.sharpcastle33.potions.PotionManager;

public class DebugListPotsCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender instanceof Player) {
			Player player = (Player) sender;
			
			if(!player.isOp()) {
				player.sendMessage(DebugGetPotionCommand.INSUFFICIENT_PERMISSIONS);
				return true;
			} // if
			
			player.sendMessage(ChatColor.DARK_PURPLE+"LOADED POTIONS:");
			for(CustomPotion potion: PotionManager.potions.keySet()) player.sendMessage(ChatColor.YELLOW +""+ potion);
		} // if
		
		return true;
	} // onCommand
	
} // class