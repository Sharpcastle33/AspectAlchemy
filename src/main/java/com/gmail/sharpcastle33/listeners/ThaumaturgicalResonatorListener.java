package com.gmail.sharpcastle33.listeners;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import com.gmail.sharpcastle33.aspects.Aspect;
import com.gmail.sharpcastle33.aspects.AspectManager;

import net.md_5.bungee.api.ChatColor;

public class ThaumaturgicalResonatorListener {
	
	private final Material RESONATOR_ITEM = Material.COMPASS;
	private final String RESONATOR_NAME = ChatColor.YELLOW + "Thaumaturgical Resonator";
	private final String NO_ASPECTS = ChatColor.RED + "This item does not appear to influence the resonator.";
	private final String SMALL_ASPECTS = ChatColor.YELLOW + "The dial hovers for a moment, then falters. This item contains a small amount of alchemical energy.";
	private final String MEDIUM_ASPECTS = ChatColor.GOLD + "The dial hovers steadily above the baseline. This item certainly contains alchemical properties.";
	private final String LARGE_ASPECTS = ChatColor.BLUE + "The resonator thrums with energy, and the dial flails wildly. This item must contain very powerful energies.";
	
	public void resonator(PlayerInteractEvent event) {
		
		if(event.getHand() == EquipmentSlot.HAND && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)){
		      Player p = event.getPlayer();
		        ItemStack main = p.getInventory().getItemInMainHand();
		        ItemStack off = p.getInventory().getItemInOffHand();
		      
		        //If the player is holding a resonator and an item.
		        if(main.getType() == RESONATOR_ITEM && off != null) {
		        	if(main.hasItemMeta() && main.getItemMeta().hasDisplayName() && main.getItemMeta().getDisplayName().equals(RESONATOR_NAME)) {
		        		Map<Aspect, Integer> aspects = AspectManager.getAspects(main);
		        		if(aspects == null || aspects.size() == 0) {
		        			p.sendMessage(NO_ASPECTS);
		        			return;
		        		}else {
		        			if(     aspects.containsKey(Aspect.PURE_AIR) ||
		        					aspects.containsKey(Aspect.PURE_ARCANE) ||
		        					aspects.containsKey(Aspect.PURE_CORRUPT) ||
		        					aspects.containsKey(Aspect.PURE_DEATH) ||
		        					aspects.containsKey(Aspect.PURE_DIVINE) ||
		        					aspects.containsKey(Aspect.PURE_EARTH) ||
		        					aspects.containsKey(Aspect.PURE_ESSENCE) ||
		        					aspects.containsKey(Aspect.PURE_FIRE) ||
		        					aspects.containsKey(Aspect.PURE_FROST) ||
		        					aspects.containsKey(Aspect.PURE_LIFE) ||
		        					aspects.containsKey(Aspect.PURE_MECHANIC) ||
		        					aspects.containsKey(Aspect.PURE_PSIONIC)||
		        					aspects.containsKey(Aspect.PURE_VOID) ||
		        					aspects.containsKey(Aspect.PURE_WATER))
		        			{
		        				p.sendMessage(LARGE_ASPECTS);
		        				return;
		        			}else 	if(     aspects.containsKey(Aspect.CONCENTRATED_AIR) ||
		        					aspects.containsKey(Aspect.CONCENTRATED_ARCANE) ||
		        					aspects.containsKey(Aspect.CONCENTRATED_CORRUPT) ||
		        					aspects.containsKey(Aspect.CONCENTRATED_DEATH) ||
		        					aspects.containsKey(Aspect.CONCENTRATED_DIVINE) ||
		        					aspects.containsKey(Aspect.CONCENTRATED_EARTH) ||
		        					aspects.containsKey(Aspect.CONCENTRATED_ESSENCE) ||
		        					aspects.containsKey(Aspect.CONCENTRATED_FIRE) ||
		        					aspects.containsKey(Aspect.CONCENTRATED_FROST) ||
		        					aspects.containsKey(Aspect.CONCENTRATED_LIFE) ||
		        					aspects.containsKey(Aspect.CONCENTRATED_MECHANIC) ||
		        					aspects.containsKey(Aspect.CONCENTRATED_PSIONIC)||
		        					aspects.containsKey(Aspect.CONCENTRATED_VOID) ||
		        					aspects.containsKey(Aspect.CONCENTRATED_WATER))
		        			{
		        				p.sendMessage(MEDIUM_ASPECTS);
		        				return;
		        			}else 	if(     aspects.containsKey(Aspect.MUNDANE_AIR) ||
		        					aspects.containsKey(Aspect.MUNDANE_ARCANE) ||
		        					aspects.containsKey(Aspect.MUNDANE_CORRUPT) ||
		        					aspects.containsKey(Aspect.MUNDANE_DEATH) ||
		        					aspects.containsKey(Aspect.MUNDANE_DIVINE) ||
		        					aspects.containsKey(Aspect.MUNDANE_EARTH) ||
		        					aspects.containsKey(Aspect.MUNDANE_ESSENCE) ||
		        					aspects.containsKey(Aspect.MUNDANE_FIRE) ||
		        					aspects.containsKey(Aspect.MUNDANE_FROST) ||
		        					aspects.containsKey(Aspect.MUNDANE_LIFE) ||
		        					aspects.containsKey(Aspect.MUNDANE_MECHANIC) ||
		        					aspects.containsKey(Aspect.MUNDANE_PSIONIC)||
		        					aspects.containsKey(Aspect.MUNDANE_VOID) ||
		        					aspects.containsKey(Aspect.MUNDANE_WATER))
		        			{
		        				p.sendMessage(SMALL_ASPECTS);
		        				return;
		        			}else {
		        				p.sendMessage("SOmething weird happened with Alchemy, PM the developers." );
		        				return;
		        			}
		        			
		        		}
		        	}
		        }
		}
	}

}
	
	
