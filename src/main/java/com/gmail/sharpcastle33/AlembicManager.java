package com.gmail.sharpcastle33;

import java.util.ArrayList;
import org.bukkit.Location;

public class AlembicManager {
  
  public ArrayList<Location> activeAlembics;
  
  public AlembicManager(){
    activeAlembics = loadActiveAlembics();
  }
 
  public ArrayList<Location> loadActiveAlembics(){
    ArrayList<Location> ret = new ArrayList<Location>();
    
    return ret;
  }
  
  public void saveActiveAlembics(){
    
  }
  
  public void activateAlembic(Location loc){
    
  }
  
  public void deactivateAlembic(Location loc){
    
  }
}
