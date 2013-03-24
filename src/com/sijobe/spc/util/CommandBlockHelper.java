package com.sijobe.spc.util;

import java.util.HashMap;
import net.minecraft.src.*;

/**
 * Helper to make command blocks execute vanilla commands
 *
 * @author q3hardcore
 * @version 1.0
 */
public class CommandBlockHelper {

   private static final HashMap<String, CommandBase> commandMappings;

        public static boolean handleCommand(String cmdName, ICommandSender sender, String[] args) {
           if(sender.getCommandSenderName().equals("@") && commandMappings.containsKey(cmdName)) {
              commandMappings.get(cmdName).processCommand(sender, args);
              return true;
           } else {
              return false;
           }
      }

   static {
      commandMappings = new HashMap<String, CommandBase>();
      commandMappings.put("difficulty", new CommandDifficulty());
      commandMappings.put("enchant", new CommandEnchant());
      commandMappings.put("gamemode", new CommandGameMode());
      commandMappings.put("give", new CommandGive());
      commandMappings.put("help", new CommandHelp());
      // commandMappings.put("kill", new CommandKill());
      commandMappings.put("time", new CommandTime());
      commandMappings.put("weather", new CommandWeather());
   }

}