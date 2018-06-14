package com.gmail.sharpcastle33.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.sharpcastle33.aspects.Aspect;
import com.gmail.sharpcastle33.aspects.AspectRecipe;
import com.gmail.sharpcastle33.aspects.AspectRecipeManager;
import com.gmail.sharpcastle33.potions.CustomPotion;

import net.md_5.bungee.api.ChatColor;

public class DebugGetRecipeCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender instanceof Player && args.length > 0) {
			Player player = (Player) sender;
			
			if(!player.isOp()) {
				player.sendMessage(DebugGetPotionCommand.INSUFFICIENT_PERMISSIONS);
			} // if
			
			if(CustomPotion.valueOf(args[0]) != null) {
				for(AspectRecipe recipe: AspectRecipeManager.recipes) {
					if(recipe.result.equals(CustomPotion.valueOf(args[0]))) {
						player.sendMessage(ChatColor.YELLOW+"RECIPE FOR "+recipe.result);
						for(Aspect aspect: recipe.aspects.keySet()) {
							player.sendMessage(ChatColor.YELLOW+""+recipe.aspects.get(aspect).intValue()+" "+aspect);
						} // for
						return true;
					} // if
				} // for
				
				player.sendMessage(ChatColor.RED+"SEVERE! VALID CUSTOMPOTION "+ ChatColor.DARK_PURPLE + CustomPotion.valueOf(args[0]) + ChatColor.RED +" HAS NO RECIPE!");
			} else {
				player.sendMessage(ChatColor.RED+"That is not a valid CustomPotion!");
			} // if
		} // if
		
		
		return true;
	} // onCommand

} // class
