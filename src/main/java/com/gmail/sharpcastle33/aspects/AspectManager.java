package com.gmail.sharpcastle33.aspects;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

public class AspectManager {
  
  Map<Material, Map> baseTypes;
  Map<String, Map> itemNames;
  Map<Aspect, Integer> aspects;
  
  public void loadAspectValues(FileConfiguration config){
    
  }

  public Map<Aspect, Integer> getAspectTotals(ItemStack[] arr){
    Map<Aspect, Integer> ret = new HashMap<Aspect, Integer>();
    for(ItemStack i : arr){
      if(getAspects(i) != null){
        Map<Aspect, Integer> temp = getAspects(i);
        for(Aspect a : temp.keySet()){
          ret.put(a, ret.get(a) + temp.get(a));
        }
      }
    }
    return ret;
  }
  
  public Map<Aspect, Integer> getAspects(ItemStack stack){
    return null;   
  }
}
