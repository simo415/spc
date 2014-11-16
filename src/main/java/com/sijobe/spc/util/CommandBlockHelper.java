package com.sijobe.spc.util;

import java.util.HashMap;
import java.util.List;

import net.minecraft.server.MinecraftServer;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandDifficulty;
import net.minecraft.command.CommandEffect;
import net.minecraft.command.CommandEnchant;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandGameMode;
import net.minecraft.command.CommandGive;
import net.minecraft.command.CommandHelp;
import net.minecraft.command.CommandTime;
import net.minecraft.command.CommandWeather;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.StatCollector;

/**
 * Helper to make command blocks execute vanilla commands
 *
 * @author q3hardcore
 * @version 1.1
 */
public class CommandBlockHelper {

   private static final HashMap<String, CommandBase> commandMappings;

   @SuppressWarnings("unchecked")
   public static boolean handleCommand(String cmdName, ICommandSender sender, String[] args) {
      if(sender.getCommandSenderName().equals("@") && commandMappings.containsKey(cmdName)) {
         try {
            commandMappings.get(cmdName).processCommand(sender, args);
         } catch (CommandException ce) {
            // sends command error to opped creative players
            MinecraftServer mcServer = MinecraftServer.getServer();
            List<EntityPlayerMP> players =
               mcServer.getConfigurationManager().playerEntityList;
            for(EntityPlayerMP plr : players) {
               if(plr.canCommandSenderUseCommand(2, "") && plr.capabilities.isCreativeMode) {
                  String cmdErr = StatCollector.translateToLocalFormatted(ce.getMessage(), ce.getErrorOjbects());
                  plr.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + cmdErr));
               }
            }
         }
         return true;
      } else {
         return false;
      }
   }

   static {
      commandMappings = new HashMap<String, CommandBase>();
      commandMappings.put("difficulty", new CommandDifficulty());
      commandMappings.put("effect", new CommandEffect());
      commandMappings.put("enchant", new CommandEnchant());
      commandMappings.put("gamemode", new CommandGameMode());
      commandMappings.put("give", new CommandGive());
      commandMappings.put("help", new CommandHelp());
      // commandMappings.put("kill", new CommandKill());
      commandMappings.put("time", new CommandTime());
      commandMappings.put("weather", new CommandWeather());
   }

}