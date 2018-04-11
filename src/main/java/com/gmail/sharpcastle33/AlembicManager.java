package com.gmail.sharpcastle33;

import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

public class AlembicManager {
  
  public ArrayList<Location> activeAlembics;
  
  public AlembicManager(){
    activeAlembics = loadActiveAlembics();
  }
 
  public ArrayList<Location> loadActiveAlembics(Plugin plugin){
    ArrayList<Location> ret = new ArrayList<Location>();

    ArrayList<HashMap<String, String>> serializedAlembics = plugin.getConfig().getList("active_alembics");

    for(HashMap<String, String> map: serializedAlembics) {
	double x = (double) map.get("x");
	double y = (double) map.get("y");
	double z = (double) map.get("z");
	String world = (String) map.get("world");
	ret.add(new Location(plugin.getServer().getWorld(world), x, y, z));
    } // for
    
    return ret;
  }
  
  public void saveActiveAlembics(Plugin plugin){
      ArrayList<HashMap<String, String>> serializedAlembics = new ArrayList<>();
      for(Location loc: activeAlembics) {
	  HashMap<String, Object> map = new HashMap<>();
	  map.put("x", loc.getX());
	  map.put("y", loc.getY());
	  map.put("z", loc.getZ());
	  map.put("world", loc.getWorld().getName());
      } // for

      plugin.getConfig().set("active_alembics", serializedAlembics);
  } // saveActiveAlembics
  
  public void activateAlembic(Location loc){
    
  }
  
  public void deactivateAlembic(Location loc){
    
  }
}
