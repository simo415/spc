package com.sijobe.spc.wrapper;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import com.sijobe.spc.ModSpc;
import com.sijobe.spc.core.Constants;
import com.sijobe.spc.util.ReflectionHelper;

import cpw.mods.fml.relauncher.Side;

/**
 * Wrapper around the MinecraftServer class providing.
 *
 * @author simo_415
 */
public class MinecraftServer {
   
   /**
    * Gets the instance of the Minecraft server that is running
    * 
    * @return The Minecraft server instance
    */
   public static net.minecraft.server.MinecraftServer getMinecraftServer() {
      return net.minecraft.server.MinecraftServer.getServer();
   }
   
   /**
    * Gets the version of Minecraft that the server currently is
    * 
    * @return The server version
    */
   public static String getVersion() {
      return getMinecraftServer().getMinecraftVersion();
   }
   
   /**
    * Returns a List of all currently logged in users
    * 
    * @return A List of all the usernames of logged in users
    */
   public static List<String> getPlayers() {
      List<String> players = new ArrayList<String>();
      for (String username : getMinecraftServer().getAllUsernames()) {
         players.add(username.toLowerCase());
      }
      return players;
   }
   
   /**
    * Checks if the specified user is currently logged in, if so true is 
    * returned, otherwise false
    * 
    * @param username - The username to check
    * @return True if the user is logged in
    */
   public static boolean isLoggedIn(String username) {
      return getPlayers().indexOf(username.toLowerCase()) != -1;
   }
   
   /**
    * Gets the player object that matches the provided username or null if the 
    * username cannot be found
    * 
    * @param username - The username to search for
    * @return The Player object that wraps the Minecraft instance
    */
   public static Player getPlayerByUsername(String username) {
      if (isLoggedIn(username)) {
         return new Player(getMinecraftServer().getConfigurationManager().getPlayerForUsername(username));
      }
      return null;
   }
   
   /**
    * Runs the specified command (with parameters) on the server. Note that 
    * this sends through the command as a server instance rather than with a 
    * player. If you need to send through a command using a sender please see
    * CommandManager.runCommand
    * 
    * @param command - The command to run on the server
    * @return A String of the output received 
    * @see CommandManager#runCommand(CommandSender, String)
    */
   public static String runCommand(String command) {
      return getMinecraftServer().handleRConCommand(command);
   }
   
   /**
    * Gets the directory name of the loaded world
    * 
    * @return The directory name of the world
    */
   public static String getDirectoryName_() {
      return getMinecraftServer().getFolderName();
   }
   
   /**
    * Gets the directory that the currently loaded world is located at
    * 
    * @return The location where the world is located
    */
   public static File getWorldDirectory() {
      return new File(Constants.SAVES_DIR, getWorldFolder());
   }
   
   public static String getWorldFolder() {
      return getMinecraftServer().getFolderName();
   }
   
   public static String getDirectoryName() {
      if(ModSpc.instance.side == Side.SERVER) {
         String dir = null;
         try {
            dir = ((File) ReflectionHelper.getDataDirectory.invoke(getMinecraftServer())).getAbsolutePath();
         } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
         if(dir == null) {
            return dir;
         }
         if(dir.endsWith(".")) {
            dir = dir.substring(0, dir.length()-1);
         }
         return dir;
      }
      else if(ModSpc.instance.side == Side.CLIENT){
         return ModSpc.instance.proxy.getDataDirectory().getAbsolutePath();
      }
      else {
         return null;
      }
   }
}