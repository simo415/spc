package com.sijobe.spc.worldedit;

import com.sk89q.worldedit.BiomeTypes;


public class ServerInterface extends com.sk89q.worldedit.ServerInterface {
   
   /**
    * Initialises the class
    */
   public ServerInterface() {
      super();
   }
   
   @Override
   public BiomeTypes getBiomes() {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public boolean isValidMobType(String arg0) {
      // TODO Auto-generated method stub
      return true;
   }

   @Override
   public void reload() {
      // TODO Auto-generated method stub
      
   }

   @Override
   public int resolveItem(String arg0) {
      // TODO Auto-generated method stub
      return 0;
   }
}
