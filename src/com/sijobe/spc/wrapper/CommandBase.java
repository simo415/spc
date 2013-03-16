package com.sijobe.spc.wrapper;

import com.sijobe.spc.command.Command;
import com.sijobe.spc.core.Constants;
import com.sijobe.spc.util.CommandBlockHelper;
import com.sijobe.spc.util.FontColour;
import com.sijobe.spc.util.Settings;
import com.sijobe.spc.util.SettingsManager;
import com.sijobe.spc.validation.Parameters;
import com.sijobe.spc.validation.ValidationException;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.ICommandSender;

/**
 * Provides a base class for all custom commands to extend. All non-abstract 
 * classes that extend this class should either specify the Command annotation 
 * or overwrite the getName() method. 
 * 
 * TODO: Work out how to handle multiple threads
 *
 * @author simo_415
 * @version 1.3
 */
public abstract class CommandBase extends net.minecraft.src.CommandBase {
   
   /**
    * A map containing all settings managers that have been loaded for worlds
    */
   private static Map<String, SettingsManager> MANAGER = new HashMap<String, SettingsManager>();

   /**
    * The Command annotation of the class that contains command attributes.
    */
   private Command annotation;

   /**
    * Initialises the instance and sets the command annotation (if it exists)
    */
   public CommandBase() {
      annotation = this.getClass().getAnnotation(Command.class);
   }

   /**
    * @see net.minecraft.src.ICommand#getCommandName()
    */
   @Override
   public String getCommandName() {
      return getName();
   }

   /**
    * Returns the name of the command
    * 
    * @return The name of the command
    */
   public String getName() {
      if (annotation != null) {
         return annotation.name();
      }
      assert false : "getName() : Command name is null";
      return null;
   }

   /**
    * Return whether the specified command parameter index is a username parameter.
    */
   public boolean isUsernameIndex(int par1)
   {
      if(getName().equals("gamemode")) {
         return par1 == 1;
      } else if(getName().equals("difficulty")) {
         return false;
      } else if(getName().equals("help")) {
         return false;
      } else if(getName().equals("time")) {
         return false;
      } else if(getName().equals("weather")) {
         return false;
      } else {
         return par1 == 0;
      }
   }

   /**
    * @see net.minecraft.src.ICommand#processCommand(net.minecraft.src.ICommandSender, java.lang.String[])
    */
   @Override
   public void processCommand(final ICommandSender sender, final String[] var2) {
      String cmd = "";
      for(String s : var2) {
         cmd += " " + s;
      }
      // System.out.println("DEBUG: command - " + cmd.trim());

      if(CommandBlockHelper.handleCommand(getName(), sender, var2)) {
         System.out.println("DEBUG: used native command for command block");
         return;
      }

      CommandSender csender = null;
      String[] args = null;

      if(sender.getCommandSenderName().equals("@")) {
         if(var2.length >= 1) {
            System.out.println("DEBUG: using command block cmd");
            if(getName().equals("sudo")) {
               args = var2;
               args[0] = func_82359_c(sender, var2[0]).getCommandSenderName();
            } else {
               EntityPlayerMP player;
               try {
                  player = func_82359_c(sender, var2[0]);
               } catch (net.minecraft.src.PlayerNotFoundException pnfe) {
                  pnfe.printStackTrace();
                  throw pnfe;
               }
               System.out.println("DEBUG: sender - " + player.getCommandSenderName());
               csender = new CommandSender(player);
               args = new String[var2.length - 1];
               for(int i = 1; i < var2.length; i++) {
                  args[i-1] = var2[i];
               }
            }
         } else {
            System.err.println("Skipping cmd: " + getName());
            return;
         }
      }

      if (!isEnabled()) {
         return;
      }

      if(csender == null) {
         csender = new CommandSender(sender);
      }

      if(args == null) {
         args = var2;
      }

      try {
         List<?> params = getParameters().validate(args);
         execute(csender,params);
      } catch (ValidationException v) {
         if (v.getMessage() == null) {
            sender.sendChatToPlayer(FontColour.RED + "Usage: " + getUsage(csender));
         } else {
            sender.sendChatToPlayer(FontColour.RED + "Error: " + v.getMessage());
         }
      } catch (CommandException e) {
         sender.sendChatToPlayer(FontColour.RED + e.getMessage());
      } catch (Exception e) {
         e.printStackTrace();
         sender.sendChatToPlayer(FontColour.RED + e.getMessage());
      }
   }

   /**
    * Runs the current command using the specified parameters and under the 
    * provided sender. The parameters have already been validated using the 
    * parameter validators
    * 
    * @param sender - The sender that ran the command
    * @param params - The parameters provided with the command
    * @throws CommandException - When there is an issue with the command that
    * was run
    */
   public abstract void execute(CommandSender sender, List<?> params) throws CommandException;

   /**
    * @see net.minecraft.src.CommandBase#getCommandUsage(net.minecraft.src.ICommandSender)
    */
   @Override
   public String getCommandUsage(ICommandSender par1ICommandSender) {
      return getUsage(new CommandSender(par1ICommandSender));
   }

   /**
    * Returns a String on the correct syntax for the current command
    * 
    * @param sender - The sender that ran the command
    * @return The syntax of the command
    */
   public String getUsage(CommandSender sender) {
      try {
         return "/" + getName() + " " + getParameters().getUsage();
      } catch (Exception e) {
         return super.getCommandUsage(sender.getMinecraftISender());
      }
   }

   /**
    * @see net.minecraft.src.CommandBase#getCommandAliases()
    */
   @Override
   public List<String> getCommandAliases() {
      return getAliases();
   }

   /**
    * Provides a list of aliases to the current command
    * 
    * @return The list of aliases
    */
   public List<String> getAliases() {
      if (annotation != null) {
         return Arrays.asList(annotation.alias());
      }
      return null;
   }

   /**
    * @see net.minecraft.src.CommandBase#canCommandSenderUseCommand(net.minecraft.src.ICommandSender)
    */
   @Override
   public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender) {
      return hasPermission(new CommandSender(par1ICommandSender));
   }

   /**
    * Checks if the current sender that used the command has the necessary
    * permission to run the command. 
    * 
    * @param sender - The sender that ran the command
    * @return True is returned when the sender can run the command
    */
   public boolean hasPermission(CommandSender sender) {
      return sender.canUseCommand(getName());
   }

   /**
    * @see net.minecraft.src.CommandBase#addTabCompletionOptions(net.minecraft.src.ICommandSender, java.lang.String[])
    */
   @Override
   public List<String> addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr) {
      return getTabCompletionOptions(new CommandSender(par1ICommandSender),par2ArrayOfStr);
   }

   /**
    * Gets a list of available tab completion options based on the current 
    * sender and the current parameters
    * 
    * @return A list of available tab completion options
    */
   public List<String> getTabCompletionOptions(CommandSender sender, String[] param) { // TODO: Use Parameters to get completion options
      return null;
   }

   /**
    * Gets the player entity that is associated with the entity that ran the
    * command
    * 
    * @param sender - The sender of the command
    * @return The associated player object, or null if the sender is not a player
    */
   public static Player getSenderAsPlayer(CommandSender sender) {
      if (sender.isPlayer()) {
         return new Player(getCommandSenderAsPlayer(sender.getMinecraftISender()));
      } 
      return null;
   }

   /**
    * Provides a description of what the command actually does
    * 
    * @return The description of the command
    */
   public String getDescription() {
      if (annotation != null) {
         return annotation.description();
      }
      return null;
   }

   /**
    * Provides an example of how to use the command
    * 
    * @return The example of the command
    */
   public String getExample() {
      if (annotation != null) {
         return annotation.example();
      }
      return null;
   }

   /**
    * Provides a video of what the command actually does
    * 
    * @return A video URL showing what the command does
    */
   public String getVideoURL() {
      if (annotation != null) {
         return annotation.videoURL();
      }
      return null;
   }

   /**
    * Returns a boolean value specifying whether the command is enabled or not.
    * A disabled command will not be able to be run by any user, including 
    * operators.
    * 
    * @return True when the command is enabled, false otherwise
    */
   public boolean isEnabled() {
      if (annotation != null) {
         return annotation.enabled();
      }
      return true;
   }

   /**
    * Returns the parameters of the command. This is used to validate the input
    * parameters before they reach the execute method of the command. This 
    * provides consistent behaviour with less repetitive coding required.
    * 
    * For no validation and unlimited parameters use Parameters.DEFAULT
    * 
    * @return The parameters that are used in this command
    */
   public Parameters getParameters() {
      return Parameters.DEFAULT;
   }
     
   /**
    * Gets the settings for the specified player in the correct world.
    * 
    * @param player - The player to retrieve the settings for
    * @return The settings associated with the user
    */
   public static Settings loadSettings(Player player) {
      if (player == null) {
         return null;
      }
      String directoryName = MinecraftServer.getDirectoryName();
      SettingsManager manager = MANAGER.get(directoryName);
      if (manager == null) {
         File spcPlayers = new File(MinecraftServer.getWorldDirectory(), "spc/players");
         if (!spcPlayers.exists()) {
            spcPlayers.mkdirs();
         }
         manager = new SettingsManager(spcPlayers, Constants.DEFAULT_SETTINGS);
         MANAGER.put(directoryName, manager);
      }
      return manager.load(player.getPlayerName());
   }
   
   /**
    * Saves the settings for the specified player
    * 
    * @param player - The player to save the settings for
    * @return True if the settings get saved successfully, false otherwise
    */
   public static boolean saveSettings(Player player) {
      if (player == null) {
         return false;
      }
      SettingsManager manager = MANAGER.get(MinecraftServer.getDirectoryName());
      if (manager != null) {
         return manager.save(player.getPlayerName());
      }
      return false;
   }
}
