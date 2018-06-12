package com.gmail.sharpcastle33.listeners;

import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gmail.sharpcastle33.aspects.Aspect;
import com.gmail.sharpcastle33.aspects.AspectManager;
import com.gmail.sharpcastle33.potions.CustomPotion;
import com.gmail.sharpcastle33.potions.PotionManager;

import net.md_5.bungee.api.ChatColor;

public class AdminToolsListener implements Listener{

	@EventHandler
	public void resonator(PlayerInteractEvent event) {
		
		final String ASPECT_ADMIN_TOOL = ChatColor.RED + "Secret Admin Aspect Tool";
		final String POTION_ADMIN_TOOL = ChatColor.RED + "Secret Admin Potion Tool";
		final String INSTANT_ADMIN_TOOL = ChatColor.RED + "Secret Admin Brewing Tool";

		/* DEBUG CODE 
		if(event.getHand() == EquipmentSlot.HAND) event.getPlayer().sendMessage(ChatColor.DARK_PURPLE+"YOUR HAND IS A HAND!");
		if(event.getAction() == Action.RIGHT_CLICK_AIR) event.getPlayer().sendMessage(ChatColor.DARK_PURPLE+"YOU RIGHT CLICKED THE AIR!");
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK) event.getPlayer().sendMessage(ChatColor.DARK_PURPLE+"YOU RIGHT CLICKED A BLOCK!");
		/* END DEBUG CODE */
		
		if (event.getHand() == EquipmentSlot.HAND
				&& (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			Player p = event.getPlayer();
			ItemStack main = p.getInventory().getItemInMainHand();
			ItemStack off = p.getInventory().getItemInOffHand();
			
			if(main.hasItemMeta() && off.hasItemMeta()) {
				ItemMeta mainMeta = main.getItemMeta();
				ItemMeta offMeta = off.getItemMeta();
				
				if(mainMeta.hasDisplayName() && mainMeta.getDisplayName().equals(ASPECT_ADMIN_TOOL)) {
					if(offMeta.hasDisplayName()) {
						Map<Aspect, Integer> temp = AspectManager.getAspects(off);
						p.sendMessage(ChatColor.BLUE + "This item has the following aspects:");
						for(Aspect a : temp.keySet()) {
							p.sendMessage(ChatColor.GOLD + a.name() + ": " + temp.get(a));
						}
					}
				}
				
				if(mainMeta.hasDisplayName() && mainMeta.getDisplayName().equals(POTION_ADMIN_TOOL)) {
					p.sendMessage(ChatColor.BLUE + "There are " + PotionManager.potions.size() + " potions loaded.");
					for(CustomPotion pot : PotionManager.potions.keySet()) {
						p.sendMessage(ChatColor.GOLD + pot.name());
					}
				}
				
				if(mainMeta.hasDisplayName() && mainMeta.getDisplayName().equals(INSTANT_ADMIN_TOOL)) {
					
				}
			}
		}	
	 }
}
