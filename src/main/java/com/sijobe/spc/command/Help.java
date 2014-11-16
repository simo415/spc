package com.sijobe.spc.command;

import com.sijobe.spc.util.FontColour;
import com.sijobe.spc.validation.Parameter;
import com.sijobe.spc.validation.ParameterString;
import com.sijobe.spc.validation.Parameters;
import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandManager;
import com.sijobe.spc.wrapper.CommandSender;

import java.util.Collections;
import java.util.List;

/**
 * Help command provides help for all of the loaded commands
 *
 * @author simo_415
 * @version 1.1
 * @status survived 1.7.2 update
 */
@Command (
   name = "help",
   description = "Brings up the help message",
   example = "give",
   videoURL = "http://www.youtube.com/watch?v=mxU4p_vvjGw",
   version = "1.1",
   alias = {"?"}
)
public class Help extends StandardCommand {
   
   public static final String MESSAGE_HEADING = FontColour.GREEN + "===Help===================";
   public static final String MESSAGE_FOOTER = FontColour.GREEN + "==========================";
   public static final String MESSAGE_USAGE = FontColour.GREEN + "Usage: " + FontColour.WHITE;
   public static final String MESSAGE_DESCRIPTION = FontColour.GREEN + "Description: " + FontColour.WHITE;
   public static final String MESSAGE_EXAMPLE = FontColour.GREEN + "Example: " + FontColour.WHITE;
   public static final String MESSAGE_VIDEO = FontColour.GREEN + "Video: " + FontColour.WHITE;
   
   /**
    * The parameters of the command
    */
   private static final Parameters PARAMETERS = new Parameters (
      new Parameter[] {
         new ParameterString("[COMMAND_NAME]", true)
      }
   );

   /**
    * @see com.sijobe.spc.wrapper.CommandBase#execute(net.minecraft.src.ICommandSender, java.util.List)
    */
   @Override
   public void execute(CommandSender sender, List<?> params) throws CommandException {
      if (params.size() == 0) { //--General Help
         List<String> names = CommandManager.getCommandNames();
         Collections.sort(names);
         String commands = "";
         for (String name : names) {
            if (CommandManager.isCommandEnabled(name) && CommandManager.hasPermission(name, getSenderAsPlayer(sender))) {
               if (name.startsWith("/")) {
                  commands += "/" + name + ", ";
               } else {
                  commands += name + ", ";
               }
            }
         }
         if (commands.length() > 0) {
            commands = commands.substring(0, commands.length() - 2);
         }
         sender.sendMessageToPlayer(MESSAGE_HEADING);
         sender.sendMessageToPlayer(commands);
         sender.sendMessageToPlayer("Use " + 
                  FontColour.AQUA + "/help COMMAND_NAME" + 
                  FontColour.WHITE + " for more information.");
         sender.sendMessageToPlayer(MESSAGE_FOOTER);
      } else { //-------------------Specific Help
         String command = (String)params.get(0);
         if (!CommandManager.doesCommandExist(command) || !CommandManager.isCommandEnabled(command)) {
            throw new CommandException("Specified command name " + command + 
                     " does not exist or is not enabled.");
         }
         String description = CommandManager.getCommandDescription(command);
         String example = CommandManager.getCommandExample(command);
         String usage = CommandManager.getCommandUsage(command, sender);
         String video = CommandManager.getCommandVideo(command);
         
         sender.sendMessageToPlayer(MESSAGE_HEADING);
         sender.sendMessageToPlayer("Help for the " + FontColour.AQUA + 
                  command + FontColour.WHITE + " command.");
         if (description != null) {
            sender.sendMessageToPlayer(MESSAGE_DESCRIPTION + description);
         }
         if (usage != null) {
            sender.sendMessageToPlayer(MESSAGE_USAGE + usage);
         }
         if (example != null) {
            sender.sendMessageToPlayer(MESSAGE_EXAMPLE + "/" + command + " " + example);
         }
         if (video != null) {
            sender.sendMessageToPlayer(MESSAGE_VIDEO + video);
         }
         sender.sendMessageToPlayer(MESSAGE_FOOTER);
      }
   }

   /**
    * @see com.sijobe.spc.wrapper.CommandBase#getParameters()
    */
   @Override
   public Parameters getParameters() {
      return PARAMETERS;
   }
}
