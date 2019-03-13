package com.gmail.sharpcastle33.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import com.gmail.sharpcastle33.AspectAlchemy;
import com.gmail.sharpcastle33.Constants;
import com.gmail.sharpcastle33.aspects.Aspect;
import com.gmail.sharpcastle33.aspects.AspectManager;
import com.gmail.sharpcastle33.aspects.AspectRecipe;
import com.gmail.sharpcastle33.aspects.AspectRecipeManager;
import com.gmail.sharpcastle33.handlers.AlembicHandler;
import com.gmail.sharpcastle33.handlers.AlembicTickTask;
import com.gmail.sharpcastle33.potions.CustomPotion;
import com.gmail.sharpcastle33.potions.PotionManager;

import net.md_5.bungee.api.ChatColor;

public class AdminToolsListener implements Listener {
	
	@EventHandler
	public void resonator(PlayerInteractEvent event) {

		if (event.getHand() == EquipmentSlot.HAND
				&& (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			Player p = event.getPlayer();
			ItemStack main = p.getInventory().getItemInMainHand();
			ItemStack off = p.getInventory().getItemInOffHand();

			if (p.getGameMode() == GameMode.CREATIVE && main.hasItemMeta()) {
				ItemMeta mainMeta = main.getItemMeta();
				ItemMeta offMeta = null;
				if(off.hasItemMeta()) offMeta = off.getItemMeta();
				
				if(mainMeta.hasDisplayName() && mainMeta.getDisplayName().equals(ChatColor.RED + "Alembic Debugger")) {
					 if (AlembicHandler.isAlembic(event.getClickedBlock())) {
						 	Chest c  = (Chest) event.getClickedBlock().getState();
							p.sendMessage(ChatColor.BLUE + "Alembic Aspect Values: ");
							p.sendMessage(ChatColor.GOLD + "Shaman Saps: " + AlembicHandler.getTotalShamanSapPoints(c));
					
							
							int saps = AlembicHandler.getTotalShamanSapPoints(c);
							ItemStack[] ingredients = AlembicHandler.getIngredients(c);

							Map<Aspect, Integer> aspectTotals = AspectManager.getAspectTotals(ingredients);
							
							for(Aspect a : aspectTotals.keySet()) {
								p.sendMessage(ChatColor.GOLD + a.name() + ": " + ChatColor.BLUE + aspectTotals.get(a));
							}
							
							p.sendMessage(ChatColor.BLACK + "-------------------------------");
							
								List<Aspect> primaries = new ArrayList<>();
								int frequence = 0;
	
								for (Aspect aspect : aspectTotals.keySet()) {
									if (aspectTotals.get(aspect).intValue() > frequence) {
										primaries.clear();
										frequence = aspectTotals.get(aspect).intValue();
										primaries.add(aspect);
									} else if (aspectTotals.get(aspect).intValue() == frequence) {
										primaries.add(aspect);
									} // if/else
								} // for
								
							p.sendMessage(ChatColor.BLUE + "Found " + primaries.size() + " primary aspect(s):" + ChatColor.GOLD + primaries.toString());
							
								Aspect primary = primaries.get(0);

								List<AspectRecipe> shortlist = new ArrayList<>();

								shortlister: for (AspectRecipe recipe : AspectRecipeManager.recipes) {
									if (recipe.primaryAspect.equals(primary)) {
										for (Aspect aspect : recipe.aspects.keySet()) {
											Bukkit.getServer().getLogger().info("Recipe contains: " + aspect.name() + " Solution contains?: " + aspectTotals.containsKey(aspect));
											if (!aspectTotals.containsKey(aspect)
													|| recipe.aspects.get(aspect).intValue() > aspectTotals.get(aspect).intValue())
												continue shortlister;
										} // for
										shortlist.add(recipe);
									} // if
								} // for
								
								ArrayList<String> formattedShortlist = new ArrayList<String>();
								for(AspectRecipe r : shortlist) {
									formattedShortlist.add(r.result.name());
								}
								
							p.sendMessage(ChatColor.BLUE + "Found " + shortlist.size() + " potential recipes " + ChatColor.GOLD + formattedShortlist.toString());
							
								Map<AspectRecipe, Integer> tolerancies = new HashMap<>();
	
								for (AspectRecipe recipe : shortlist) {
									for (Aspect aspect : aspectTotals.keySet()) {
										int tolerance = 0;
										if (recipe.aspects.containsKey(aspect)) {
											tolerance += (aspectTotals.get(aspect).intValue() - recipe.aspects.get(aspect).intValue())
													* Constants.ADDITIONAL_ASPECT_TOLERANCE;
										} else {
											tolerance += aspectTotals.get(aspect).intValue() * Constants.BAD_ASPECT_TOLERANCE;
										} // if/else
										tolerancies.put(recipe, tolerance);
									} // for
								} // for
	
								/* Remove Shortlisted Recipes with Positive Tolerance - Sap Effect */ // =======================================================================//
	
								for (AspectRecipe recipe : tolerancies.keySet()) {
									if (tolerancies.get(recipe).intValue() - (saps - recipe.time) * Constants.SHAMAN_SAP_VALUE > 0)
										shortlist.remove(recipe);
								} // for
								formattedShortlist.clear();
								for(AspectRecipe r : shortlist) {
									formattedShortlist.add(r.result.name());
								}
								
								
							p.sendMessage(ChatColor.BLUE + "Found " + shortlist.size() + " recipes within tolerance bounds of " + Constants.SHAMAN_SAP_VALUE + " per sap.");
							p.sendMessage(ChatColor.GOLD + formattedShortlist.toString());
							p.sendMessage(ChatColor.BLACK + "-------------------------------");

								List<AspectRecipe> answers = new ArrayList<>();
								int tolerance = 0;
	
								for (AspectRecipe recipe : shortlist) {
									if (tolerancies.get(recipe).intValue() < tolerance) {
										answers.clear();
										tolerance = tolerancies.get(recipe).intValue();
										answers.add(recipe);
									} else {
										answers.add(recipe);
									} // if/else
								} // for
								
								
								formattedShortlist.clear();
								for(AspectRecipe r : answers) {
									formattedShortlist.add(r.result.name());
								}
								
							p.sendMessage(ChatColor.BLUE + "Answers decided: " + answers.size() + ": " + answers.toString());
								
								if(answers.size() == 1) {
									p.sendMessage(ChatColor.BLUE + "Final answer: " + ChatColor.GOLD + answers.get(0).result.name());
								}else {
									int highest = 0;
									AspectRecipe best = null;
									
									for(AspectRecipe rec : answers) {
										if(AspectRecipeManager.aspectCost(rec) > highest) {
											best = rec;
											highest = AspectRecipeManager.aspectCost(rec);
											
										}
									}
									
									if(best == null) {
										p.sendMessage(ChatColor.BLUE + "Final Answer: " + ChatColor.GOLD + "NULL");
									}else {
										p.sendMessage(ChatColor.BLUE + "Final Answer: " + ChatColor.GOLD + best.result.name());

									}
									//return best.result;
									p.sendMessage(ChatColor.BLACK + "-------------------------------");

								}
					 
					 }else {
						 p.sendMessage(ChatColor.RED + "You must click an Alembic Chamber to use this tool");
					 }
					

				}

				
				
				
				
				
				
				
				
				
				//Alembic Fixer
				if(mainMeta.hasDisplayName() && mainMeta.getDisplayName().equals(ChatColor.RED + "Alembic Fixer")) {
		         
				  if (AlembicHandler.isAlembic(event.getClickedBlock())) {

                    Chest chest = (Chest) event.getClickedBlock().getState();

                    ItemStack progress = chest.getInventory().getItem(17);
                    ItemMeta progressMeta = progress.hasItemMeta() ? progress.getItemMeta() : null;
                    if (progressMeta != null && progressMeta.getDisplayName().equals(ChatColor.GREEN + "Start Alchemy")) {
                       
                    } else {
                      new AlembicTickTask(event.getClickedBlock().getLocation()).runTaskTimer(AspectAlchemy.plugin, Constants.ALEMBIC_TICK_TIME, Constants.ALEMBIC_TICK_TIME);

                    } // if/else
				  
				  }
				}
				
				if (mainMeta.hasDisplayName() && mainMeta.getDisplayName().equals(Constants.COUNTER_ADMIN_TOOL)) {
					Inventory inv = p.getInventory();
					
					Map<Aspect, Integer> ret = new HashMap<>();

					for(int i = 0; i < 9; i++) {
						ItemStack item = inv.getItem(i);
						if(item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
							if(AspectManager.getAspects(item) != null) {
								Map<Aspect, Integer> temp = AspectManager.getAspects(item);
								for(Aspect a : temp.keySet()) {
									if(ret.containsKey(a)) {
										ret.put(a, ret.get(a) + temp.get(a)*item.getAmount());
									}else {
										ret.put(a, temp.get(a)*item.getAmount());
									}
								}
							}
						}
					}
					
					p.sendMessage(ChatColor.BLUE + "Your hotbar contains the following aspects");
					
					for(Aspect a : ret.keySet()) {
						p.sendMessage(ChatColor.GOLD + a.name() + " " + ret.get(a));
					}
				}

				if (mainMeta.hasDisplayName() && mainMeta.getDisplayName().equals(Constants.ASPECT_ADMIN_TOOL) && off.hasItemMeta()) {
					if (offMeta.hasDisplayName()) {
						Map<Aspect, Integer> temp = AspectManager.getAspects(off);
						p.sendMessage(ChatColor.BLUE + "This item has the following aspects:");
						for (Aspect a : temp.keySet()) {
							p.sendMessage(ChatColor.GOLD + a.name() + ": " + temp.get(a));
						}
					}
				}

				if (mainMeta.hasDisplayName() && mainMeta.getDisplayName().equals(Constants.POTION_ADMIN_TOOL)) {
					p.sendMessage(ChatColor.BLUE + "There are " + PotionManager.potions.size() + " potions loaded.");
					for (CustomPotion pot : PotionManager.potions.keySet()) {
						p.sendMessage(ChatColor.GOLD + pot.name());
					}
				}

				if (mainMeta.hasDisplayName() && mainMeta.getDisplayName().equals(Constants.INSTANT_ADMIN_TOOL)) {
					if (AlembicHandler.isAlembic(event.getClickedBlock())) {

						Chest chest = (Chest) event.getClickedBlock().getState();

						ItemStack progress = chest.getInventory().getItem(17);
						ItemMeta progressMeta = progress.hasItemMeta() ? progress.getItemMeta() : null;
						if (progressMeta != null && progressMeta.getDisplayName().equals(ChatColor.GREEN + "Start Alchemy")) {
							AlembicHandler.completeAlchemy((Chest) event.getClickedBlock().getState(),
									(BrewingStand) event.getClickedBlock().getRelative(BlockFace.UP).getState());
						} else {
							List<String> lore = new ArrayList<>();
							lore.add(ChatColor.RED + "Time Remaining: " + 1 + "min");

							progressMeta.setLore(lore);
							progress.setItemMeta(progressMeta);
						} // if/else
					}
				}
			}
		}
	}
}
