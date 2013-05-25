package com.sijobe.spc.hooks;

import com.sijobe.spc.command.MultipleCommands;
import com.sijobe.spc.core.Constants;
import com.sijobe.spc.core.PlayerMP;
import com.sijobe.spc.core.SPCLoader;
import com.sijobe.spc.util.DynamicClassLoader;
import com.sijobe.spc.util.FontColour;
import com.sijobe.spc.worldedit.WorldEditEvents;
import com.sijobe.spc.wrapper.CommandBase;
import com.sijobe.spc.wrapper.CommandManager;
import com.sijobe.spc.wrapper.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Initialises SPC by adding the commands to the system. The class is 
 * dynamically loaded by the HookManager when hooks of type PlayerMP are 
 * loaded, this is done by the Minecraft class EntityPlayerMP when it is 
 * first referenced using static invokes.
 *
 * @author simo_415
 * @version 1.0
 */
public class InitialiseCommands extends PlayerMP {
   
   /**
    * When Minecraft has properly loaded a welcome message is sent, this value
    * says whether the message has been sent or not
    */
   public static List<String> WELCOME_FLAG = new ArrayList<String>();
   /**
    * Tells the class whether the commands have been loaded 
    */
   private static boolean IS_LOADED = false;
   
   /**
    * The welcome message to send
    */
   private static final String WELCOME_MESSAGE = FontColour.GREY + 
      "Single Player Commands (" + Constants.VERSION + ") - " + FontColour.ORANGE + "http://bit.ly/spcmod";

   /**
    * Pending messages
   */
   private static final List<String> pendingMessages = new ArrayList<String>();
      
   public InitialiseCommands() {
      loadCommands();
   }
   
   @Override
   public void onTick(Player player) {
      // Send the welcome message
      if (IS_LOADED && !WELCOME_FLAG.contains(player.getPlayerName())) {
         sendStartupMessage(player);
         WELCOME_FLAG.add(player.getPlayerName());
      }
      if(pendingMessages.size() > 0) {
         for(String msg : pendingMessages) {
            player.sendChatMessage(msg);
         }
         pendingMessages.clear();
      }
   }
   
   /**
    * Sends the startup message when the mod has loaded
    */
   private void sendStartupMessage(Player player) {
      player.sendChatMessage(WELCOME_MESSAGE);
   }

   /**
    * Loads standard and multiple commands into the game. This method is run 
    * as a thread to keep execution time to a minimum.
    */
   private void loadCommands() {
      (new Thread() {
         @Override
         public void run() {
            // Load all classes on classpath
            SPCLoader.load();
            
            // Load Commands Sets
            loadMultipleCommands();

            try {
               if(com.sijobe.spc.worldedit.WorldEditCommandSet.getCurrentInstance() == null) {
                  Class.forName("com.sk89q.worldedit.WorldEdit");
                  System.out.println("SPCommands: Forcing command re-load.");
                  loadMultipleCommands();
               }
            } catch (Throwable t) {
               WorldEditEvents.disableHandleEvents();
               pendingMessages.add("WorldEdit.jar not found in .minecraft/bin - WE unavailable.");
            }

            // Load Standard Commands
            loadStandardCommands();
            IS_LOADED = true;
         }
      }).start();
   }

   /**
    * Loads the standard commands that have been loaded by the JRE
    */
   private void loadStandardCommands() {
      List<Class<com.sijobe.spc.command.StandardCommand>> commands = DynamicClassLoader.getClasses(com.sijobe.spc.command.StandardCommand.class);
      for (Class<com.sijobe.spc.command.StandardCommand> command : commands) {
         if (Modifier.isAbstract(command.getModifiers())) {
            continue;
         }
         try {
            com.sijobe.spc.command.StandardCommand cmd = command.newInstance();
            if (!cmd.isEnabled()) {
               continue;
            }
            if (doesCommandExist(cmd)) { // TODO: review this
               System.out.println("Overwriting existing command named: " + cmd.getName());
            }
            CommandManager.registerCommand(cmd);
         } catch (Exception e) {
            System.err.println("There was an issue initialising class " + 
                     command.getName() + ". Verify that the class is setup correctly.");
            e.printStackTrace();
         }
      }
   }

   /**
    * Loads the multiple commands that have been loaded by the JRE
    */
   private void loadMultipleCommands() {
      List<Class<com.sijobe.spc.command.MultipleCommands>> sets = DynamicClassLoader.getClasses(com.sijobe.spc.command.MultipleCommands.class);
      for (Class<com.sijobe.spc.command.MultipleCommands> command : sets) {
         if (Modifier.isAbstract(command.getModifiers())) {
            continue;
         }
         try {
            Constructor<com.sijobe.spc.command.MultipleCommands> constructor = command.getConstructor(String.class);
            MultipleCommands instance = constructor.newInstance((String)null);
            String commandNames[] = instance.getCommands();
            if (commandNames == null) {
               continue;
            }
            for (String name : commandNames) {
               com.sijobe.spc.command.MultipleCommands cmd = constructor.newInstance(name);
               if (!cmd.isEnabled()) {
                  continue;
               }
               if (doesCommandExist(cmd)) {
                  System.out.println("Overwriting existing command named: " + cmd.getName());
               }
               CommandManager.registerCommand(cmd);
            }
         } catch (Throwable e) {
            System.err.println("Failed to load " + command.getName());
            // e.printStackTrace();
         }
      }
   }

   /**
    * Checks if the specified command (or alias) name already exist in 
    * Minecraft. If any of them exist then true is returned, if none of them
    * exist then false is returned. 
    * 
    * @param command - The command to check
    * @return True if command already exists, false otherwise.
    */
   public static boolean doesCommandExist(CommandBase command) {
      if (CommandManager.doesCommandExist(command.getCommandName())) {
         return true;
      }
      if (command.getAliases() != null) {
         for (String alias : command.getAliases()) {
            if (CommandManager.doesCommandExist(alias)) {
               return true;
            }
         }
      }
      return false;
   }
}
