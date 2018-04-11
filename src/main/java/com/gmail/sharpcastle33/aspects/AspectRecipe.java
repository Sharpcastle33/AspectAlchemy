package com.gmail.sharpcastle33.aspects;

import java.util.Map;
import org.bukkit.inventory.ItemStack;

public class AspectRecipe {
  
  private Map<Aspect, Integer> aspects;
  private Aspect primaryAspect;
  private int time;
  private ItemStack result;
  
  public AspectRecipe(Map<Aspect, Integer> map, Aspect a, int t, ItemStack result){
    aspects = map;
    primaryAspect = a;
    time = t;
    this.result = result;
  }

}
