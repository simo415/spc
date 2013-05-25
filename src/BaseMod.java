package net.minecraft.src;

/**
 * Dummy BaseMod class
 * DO NOT INCLUDE THIS WHEN PACKAGING SPC
 *
 * @author q3hardcore
 * @version 1.0
 */
public abstract class BaseMod {

   public String getName() {
      return getClass().getSimpleName();
   }
   
   public abstract String getVersion();
   
   public abstract void load();
   
   public String getPriorities() {
      throw new RuntimeException("BaseMod should not be packaged with SPC");
   }
}