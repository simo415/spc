package com.sijobe.spc.wrapper;

import java.util.HashMap;
import java.util.Map;

public class Potion {
   
   private static Map<String, Integer> POTIONS = retrievePotions();
   
   /**
    * Gets the potion name and ID pairs from Minecraft
    * 
    * @return A Map containing Name > ID pairs
    */
   private static Map<String, Integer> retrievePotions() {
      Map<String, Integer> potions = new HashMap<String, Integer>();
      for (int i = 0; i < net.minecraft.src.Potion.potionTypes.length; i++) {
         if (net.minecraft.src.Potion.potionTypes[i] != null) {
            potions.put(net.minecraft.src.StatCollector.translateToLocal(net.minecraft.src.Potion.potionTypes[i].getName()).replace(' ', '_').toLowerCase(), i);
         }
      }
      return potions;
   }
   
   /**
    * Gets the potions that Minecraft has
    * 
    * @return A Map of potion name > ID pairs
    */
   public static Map<String, Integer> getPotions() {
      return POTIONS;
   }
}
