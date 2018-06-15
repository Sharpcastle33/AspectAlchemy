package com.gmail.sharpcastle33.listeners;

import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import com.gmail.sharpcastle33.Constants;
import com.gmail.sharpcastle33.aspects.Aspect;
import com.gmail.sharpcastle33.aspects.AspectItemData;
import com.gmail.sharpcastle33.aspects.AspectManager;
import com.gmail.sharpcastle33.aspects.AspectRecipeManager;

/**
 * Handles the use of the ThaumaturgicalResonator which is used to ascertain the
 * purity of alchemical properties in an item
 * 
 * @author Sharpcastle33
 */
public class ThaumaturgicalResonatorListener implements Listener {

	@EventHandler
	public void resonator(PlayerInteractEvent event) {

		if (event.getHand() == EquipmentSlot.HAND
				&& (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			Player p = event.getPlayer();
			ItemStack main = p.getInventory().getItemInMainHand();
			ItemStack off = p.getInventory().getItemInOffHand();

			// If the player is holding a resonator and an item.
			if (main.getType() == Constants.RESONATOR_ITEM && off != null) {
				if (main.hasItemMeta() && main.getItemMeta().hasDisplayName()
						&& main.getItemMeta().getDisplayName().equals(Constants.RESONATOR_NAME)) {

					if (off.hasItemMeta() && off.getItemMeta().hasDisplayName()
							&& off.getItemMeta().getDisplayName().equals(Constants.DEBUG_ITEM)) {
						Map<String, AspectItemData> itemAspects = AspectManager.getLoadedItemAspects();
						p.sendMessage("There are " + itemAspects.size() + " itemaspect entries loaded");
						for (String s : itemAspects.keySet()) {
							p.sendMessage(s);
						}

						if (AspectRecipeManager.recipes == null) {
							p.sendMessage("AspectRecipes List is null");
						} else {
							p.sendMessage(AspectRecipeManager.recipes.size() + "= amt of recipes");
						}

					}

					Map<Aspect, Integer> aspects = AspectManager.getAspects(off);
					if (aspects == null || aspects.size() == 0) {
						p.sendMessage(Constants.NO_ASPECTS);
						return;
					} else {
						if (aspects.containsKey(Aspect.PURE_AIR) || aspects.containsKey(Aspect.PURE_ARCANE)
								|| aspects.containsKey(Aspect.PURE_CORRUPT) || aspects.containsKey(Aspect.PURE_DEATH)
								|| aspects.containsKey(Aspect.PURE_DIVINE) || aspects.containsKey(Aspect.PURE_EARTH)
								|| aspects.containsKey(Aspect.PURE_ESSENCE) || aspects.containsKey(Aspect.PURE_FIRE)
								|| aspects.containsKey(Aspect.PURE_FROST) || aspects.containsKey(Aspect.PURE_LIFE)
								|| aspects.containsKey(Aspect.PURE_MECHANIC) || aspects.containsKey(Aspect.PURE_PSIONIC)
								|| aspects.containsKey(Aspect.PURE_VOID) || aspects.containsKey(Aspect.PURE_WATER)) {
							p.sendMessage(Constants.LARGE_ASPECTS);
							return;
						} else if (aspects.containsKey(Aspect.CONCENTRATED_AIR)
								|| aspects.containsKey(Aspect.CONCENTRATED_ARCANE)
								|| aspects.containsKey(Aspect.CONCENTRATED_CORRUPT)
								|| aspects.containsKey(Aspect.CONCENTRATED_DEATH)
								|| aspects.containsKey(Aspect.CONCENTRATED_DIVINE)
								|| aspects.containsKey(Aspect.CONCENTRATED_EARTH)
								|| aspects.containsKey(Aspect.CONCENTRATED_ESSENCE)
								|| aspects.containsKey(Aspect.CONCENTRATED_FIRE)
								|| aspects.containsKey(Aspect.CONCENTRATED_FROST)
								|| aspects.containsKey(Aspect.CONCENTRATED_LIFE)
								|| aspects.containsKey(Aspect.CONCENTRATED_MECHANIC)
								|| aspects.containsKey(Aspect.CONCENTRATED_PSIONIC)
								|| aspects.containsKey(Aspect.CONCENTRATED_VOID)
								|| aspects.containsKey(Aspect.CONCENTRATED_WATER)) {
							p.sendMessage(Constants.MEDIUM_ASPECTS);
							return;
						} else if (aspects.containsKey(Aspect.MUNDANE_AIR) || aspects.containsKey(Aspect.MUNDANE_ARCANE)
								|| aspects.containsKey(Aspect.MUNDANE_CORRUPT)
								|| aspects.containsKey(Aspect.MUNDANE_DEATH)
								|| aspects.containsKey(Aspect.MUNDANE_DIVINE)
								|| aspects.containsKey(Aspect.MUNDANE_EARTH)
								|| aspects.containsKey(Aspect.MUNDANE_ESSENCE)
								|| aspects.containsKey(Aspect.MUNDANE_FIRE) || aspects.containsKey(Aspect.MUNDANE_FROST)
								|| aspects.containsKey(Aspect.MUNDANE_LIFE)
								|| aspects.containsKey(Aspect.MUNDANE_MECHANIC)
								|| aspects.containsKey(Aspect.MUNDANE_PSIONIC)
								|| aspects.containsKey(Aspect.MUNDANE_VOID)
								|| aspects.containsKey(Aspect.MUNDANE_WATER)) {
							p.sendMessage(Constants.SMALL_ASPECTS);
							return;
						} else {
							p.sendMessage("SOmething weird happened with Alchemy, PM the developers.");
							return;
						}

					}
				}
			}
		}
	} // resonator

} // class
