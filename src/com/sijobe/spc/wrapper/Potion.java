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
      for (int i = 0; i < net.minecraft.potion.Potion.potionTypes.length; i++) {
         if (net.minecraft.potion.Potion.potionTypes[i] != null) {
            potions.put(net.minecraft.util.StatCollector.translateToLocal(net.minecraft.potion.Potion.potionTypes[i].getName()).replace(' ', '_').toLowerCase(), i);
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
