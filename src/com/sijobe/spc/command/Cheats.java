package com.sijobe.spc.command;

import com.sijobe.spc.util.FontColour;
import com.sijobe.spc.validation.Parameters;
import com.sijobe.spc.wrapper.CommandBase;
import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Player;

import java.util.List;

/**
 * Command to toggle native cheats
 *
 * @author q3hardcore
 * @version 1.0
 * @status broken through 1.7.2 update
 */
@Command (
   name = "cheats",
   description = "Enables and disables cheats for current world",
   example = "",
   videoURL = "",
   enabled = true
)
public class Cheats extends StandardCommand {

   @Override
   public void execute(CommandSender sender, List<?> params) throws CommandException {
      Player player = CommandBase.getSenderAsPlayer(sender);
      if (params.size() == 0) {
         player.getWorld().setCheats(!player.getWorld().isCheats());
      } else {
         player.getWorld().setCheats((Boolean)params.get(0));
      }
      player.sendChatMessage("Cheats are " + FontColour.AQUA
               + (player.getWorld().isCheats() ? "enabled" : "disabled"));
   }
   
   @Override
   public Parameters getParameters() {
      return Parameters.DEFAULT_BOOLEAN;
   }

}