package com.sijobe.spc.util;

import java.util.Random;

/**
 * Provides the standard Minecraft font colour codes
 *
 * @author simo_415
 * @version 1.1
 */
public enum FontColour {
   
   BLACK("\2470"),
   DARK_BLUE("\2471"),
   DARK_GREEN("\2472"),
   DARK_AQUA("\2473"),
   DARK_RED("\2474"),
   PURPLE("\2475"),
   ORANGE("\2476"),
   GREY("\2477"),
   DARK_GREY("\2478"),
   BLUE("\2479"),
   GREEN("\247a"),
   AQUA("\247b"),
   RED("\247c"),
   PINK("\247d"),
   YELLOW("\247e"),
   WHITE("\247f"),
   // Special case - random
   RANDOM("\247k");
   
   /**
    * Holds the random variables
    */
   private Random random;
   
   /**
    * The value of the enum
    */
   private final String value;
   
   /**
    * Initialises the enum using the specified value
    * 
    * @param value - The value of the FontColour
    */
   private FontColour(String value) {
      this.value = value;
      random = new Random();
   }
   
   /**
    * Overrides the default toString method to return the value of the Enum
    * 
    * @see java.lang.Enum#toString()
    */
   @Override
   public String toString() {
      if (value.equalsIgnoreCase("\247k")) {
         return values()[random.nextInt(values().length - 1)].toString();
      }
      return value;
   }
}
