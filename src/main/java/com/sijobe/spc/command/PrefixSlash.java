package com.sijobe.spc.command;

import com.sijobe.spc.util.FontColour;
import com.sijobe.spc.util.Settings;
import com.sijobe.spc.validation.Parameters;
import com.sijobe.spc.wrapper.CommandBase;
import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Player;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Command to toggle requiring '/'
 *
 * @author q3hardcore
 * @version 1.0
 * @status broken through 1.7.2 update -> fixed
 */
@Command (
      name = "prefixslash",
      description = "Enables and disables auto slash-prefixing",
      example = "",
      videoURL = "",
      enabled = true
      )
public class PrefixSlash extends StandardCommand {
   public static Set<String> playersUsing = new HashSet<String>();
   
   @Override
   public boolean isEnabled() {
      return true;
   }
   
   @Override
   public void execute(CommandSender sender, List<?> params) throws CommandException {
      Player player = CommandBase.getSenderAsPlayer(sender);
      Settings config = super.loadSettings(player);
      boolean prefixSlash = config.getBoolean("prefixSlash", true);
      if (params.size() == 0) {
         prefixSlash ^= true;
      } else {
         prefixSlash = ((Boolean)params.get(0));
      }
      if(playersUsing.contains(sender.getSenderName()) && !prefixSlash) {
         playersUsing.remove(sender.getSenderName());
      }
      else if(!playersUsing.contains(sender.getSenderName()) && prefixSlash){
         playersUsing.add(sender.getSenderName());
      }
      config.set("prefixSlash", prefixSlash);
      super.saveSettings(player);
      player.sendChatMessage("Slash prefixing is now " + FontColour.AQUA
            + (prefixSlash ? "enabled" : "disabled"));
   }
   
   @Override
   public Parameters getParameters() {
      return Parameters.DEFAULT_BOOLEAN;
   }
}