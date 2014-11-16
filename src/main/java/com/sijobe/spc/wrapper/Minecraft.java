package com.sijobe.spc.wrapper;

import java.io.File;

/**
 * Provides a wrapper around the Minecraft class
 *
 * @author simo_415
 * @version 1.0
 */
public class Minecraft {

   /**
    * Gets the directory that Minecraft is installed in
    * 
    * @return The Minecraft installation directory
    */
   public static File getMinecraftDirectory() {
      return getMinecraft().mcDataDir;
   }

   /**
    * Gets the internal Minecraft reference
    * 
    * @return The Minecraft instance
    */
   public static net.minecraft.client.Minecraft getMinecraft() {
      return net.minecraft.client.Minecraft.getMinecraft();
   }

   /**
    * Returns true if the game is being played in Single player, false if the
    * game is in LAN or server mode.
    * 
    * @return True when in single player, false otherwise.
    */
   public static boolean isSinglePlayer() {
      return getMinecraft().isSingleplayer() && !getMinecraft().getIntegratedServer().getPublic();
   }

   /**
    * Returns true if the a GUI screen is currently open
    * 
    * @return True if there is a GUI screen open
    */
   public static boolean isGuiScreenOpen() {
      return getMinecraft().currentScreen != null;
   }

   /**
    * If the game is being player in single player this will return the client 
    * instance, otherwise null
    * 
    * @return The player instance, otherwise null
    */
   public static Player getPlayer() {
      if (isSinglePlayer()) {
         return new Player(getMinecraft().thePlayer);
      } 
      return null;
   }
}
