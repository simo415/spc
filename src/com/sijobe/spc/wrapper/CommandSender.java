package com.sijobe.spc.wrapper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;

/**
 * Provides a wrapper around the ICommandSender interface
 *
 * @author simo_415
 */
public class CommandSender {
   private final ICommandSender sender;
   
   /**
    * Standard constructor that accepts the sender to wrap
    * 
    * @param sender - The sender to wrap
    */
   public CommandSender(ICommandSender sender) {
      this.sender = sender;
   }
   
   /**
    * Creates an instance of a command sender using the player instance
    * 
    * @param player - The player to create a command sender
    */
   public CommandSender(Player player) {
      this (player.getMinecraftPlayer());
   }
   
   /**
    * Gets the senders name, commonly this is the player's username
    * 
    * @return The senders name
    */
   public String getSenderName() {
      return sender.getCommandSenderName();
   }
   
   /**
    * Returns true if the sender can use the specified command
    * 
    * @param command - The command name
    * @return True is returned if the sender can use the specified command
    */
   public boolean canUseCommand(String command) {
      return sender.canCommandSenderUseCommand(4, command);
   }
   
   /**
    * Sends the specified message to the sender. The sender should display this
    * message.
    * 
    * @param message - The message to display
    */
   public void sendMessageToPlayer(String message) {
      sender.addChatMessage(new ChatComponentText(message));
   }
   
   /**
    * Translates the provided key translate with the provided parameters
    * 
    * @param translate - The string to translate
    * @param params - The parameters
    * @return The translated String
    */
   public String translateString(String translate, Object ... params) {
      return StatCollector.translateToLocalFormatted(translate, params);
   }
   
   /**
    * Gets the raw data object that this class is wrapping around
    * 
    * @return The ICommandSender class
    */
   public ICommandSender getMinecraftISender() {
      return sender;
   }
   
   /**
    * Returns true if this command sender object is a player
    * 
    * @return True if the command sender is a player, false otherwise
    */
   public boolean isPlayer() {
      return sender instanceof EntityPlayer;
   }
}
