package com.sijobe.spc.command;

import com.sijobe.spc.util.FontColour;
import com.sijobe.spc.validation.Parameters;
import com.sijobe.spc.wrapper.CommandBase;
import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Player;

import java.util.List;

/**
 * Command to toggle invinciblity
 *
 * @author q3hardcore
 * @version 1.0
 */
@Command (
   name = "god",
   description = "Enables and disables damage for the player",
   example = "",
   videoURL = "",
   enabled = true
)
public class God extends StandardCommand {

   @Override
   public void execute(CommandSender sender, List<?> params) throws CommandException {
      Player player = CommandBase.getSenderAsPlayer(sender);
      if (params.size() == 0) {
         getCapabilities(player).disableDamage ^= true;
      } else {
         getCapabilities(player).disableDamage = ((Boolean)params.get(0));
      }
      player.getMinecraftPlayer().sendPlayerAbilities();
      player.sendChatMessage("Damage is now " + FontColour.AQUA
               + (getCapabilities(player).disableDamage ? "disabled" : "enabled"));
   }
   
   @Override
   public Parameters getParameters() {
      return Parameters.DEFAULT_BOOLEAN;
   }

   private net.minecraft.src.PlayerCapabilities getCapabilities(Player player) {
      return player.getMinecraftPlayer().capabilities;
   }

}