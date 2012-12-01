package com.sijobe.spc.wrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.src.ICommand;
import net.minecraft.src.ServerCommandManager;

/**
 * Provides methods to allow easy access to the server command manager and its
 * methods. 
 *
 * @author simo_415
 * @version 1.0
 */
public class CommandManager {
   
   /**
    * The command manager to run the methods off
    * 
    * @return The command manager reference the server is using
    */   
   public static ServerCommandManager getCommandManager() {
      return (ServerCommandManager)net.minecraft.server.MinecraftServer.getServer().getCommandManager();
   }
   
   /**
    * Runs the specified command (including parameters) as the specified 
    * sender. The sender will get passed through to the commands execute method
    * and will have the action of the command executed on it. 
    * 
    * @param sender - The sender of the command
    * @param command - The command to run
    */
   public static void runCommand(CommandSender sender, String command) {
      getCommandManager().executeCommand(sender.getMinecraftISender(), command);
   }
   
   /**
    * Registers the specified command into the Minecraft command list. If a 
    * command with the same name (or alias) already exists it will be 
    * overwritten.
    * 
    * @param command - The command to register
    */
   public static void registerCommand(CommandBase command) {
      getCommandManager().registerCommand(command);
   }
   
   /**
    * Checks if the specified command name already exists in Minecraft
    * 
    * @param name - The name of the command to check
    * @return True if the command exists 
    */
   public static boolean doesCommandExist(String name) {
      return getCommandManager().getCommands().containsKey(name);
   }
   
   /**
    * Gets the names of the commands in Minecraft
    * 
    * @return A List of command names in Minecraft
    */
   public static List<String> getCommandNames() {
      return new ArrayList<String>(getCommandManager().getCommands().keySet());
   }
   
   /**
    * Returns true if the command is enabled, false otherwise. If the command 
    * isn't a SPC command then true is automatically returned as there is no
    * way to disable a standard Minecraft command
    * 
    * @param name - The name of the command to get the usage for
    * @return True if the command is enabled, false otherwise
    */
   public static boolean isCommandEnabled(String name) {
      try {
         return ((CommandBase)getCommandMap().get(name)).isEnabled();
      } catch (Exception e) {
         return true;
      }
   }
   
   /**
    * Gets the command usage
    * 
    * @param name - The name of the command to get the usage for
    * @param sender - The sender who requested the usage
    * @return A String specifying the command usage
    */
   public static String getCommandUsage(String name, CommandSender sender) {
      try {
         return getCommandMap().get(name).getCommandUsage(sender.getMinecraftISender());
      } catch (Exception e) {
         return null;
      }
   }
   
   /**
    * Gets the command description
    * 
    * @param name - The name of the command to get the description for
    * @return A String specifying the command description
    */
   public static String getCommandDescription(String name) {
      try {
         return ((CommandBase)getCommandMap().get(name)).getDescription();
      } catch (Exception e) {
         return null;
      }
   }
   
   /**
    * Gets the command example
    * 
    * @param name - The name of the command to get the example for
    * @return A String specifying the command example
    */
   public static String getCommandExample(String name) {
      try {
         return ((CommandBase)getCommandMap().get(name)).getExample();
      } catch (Exception e) {
         return null;
      }
   }
   
   /**
    * Gets the command video
    * 
    * @param name - The name of the command to get the video for
    * @return A String specifying the command video
    */
   public static String getCommandVideo(String name) {
      try {
         return ((CommandBase)getCommandMap().get(name)).getVideoURL();
      } catch (Exception e) {
         return null;
      }
   }
   
   /**
    * Checks if the specified player has the permission for the specified 
    * command to run it. True is returned if the player can run the command, 
    * false otherwise. If the command doesn't exist then false is returned.
    * 
    * @param name - The name of the command
    * @param player - The player to check
    * @return True if the player can run it, false otherwise
    */
   public static boolean hasPermission(String name, Player player) {
      try {
         return getCommandMap().get(name).canCommandSenderUseCommand(player.getMinecraftPlayer());
      } catch (Exception e) {
         return false;
      }
   }
   
   /**
    * Gets the command map in the correct format
    * 
    * @return The command map
    */
   private static Map<String,ICommand> getCommandMap() {
      return getCommandManager().getCommands();
   }
}
