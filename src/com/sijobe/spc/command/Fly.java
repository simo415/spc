package com.sijobe.spc.command;

import com.sijobe.spc.util.FontColour;
import com.sijobe.spc.validation.Parameters;
import com.sijobe.spc.wrapper.CommandBase;
import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Player;

import java.util.List;

@Command (
   name = "fly",
   description = "Enables and disables the ability to fly for the player",
   example = "",
   videoURL = "http://www.youtube.com/watch?v=4ZOvu3hf7k0",
   enabled = true
)
public class Fly extends StandardCommand {

   @Override
   public void execute(CommandSender sender, List<?> params) throws CommandException {
      Player player = CommandBase.getSenderAsPlayer(sender);
      if (params.size() == 0) {
         player.setAllowFlying(!player.getAllowFlying());
      } else {
         player.setAllowFlying((Boolean)params.get(0));
      }
      player.sendChatMessage("Flying is now " + FontColour.AQUA + (player.getAllowFlying() ? "permitted" : "disabled"));
   }
   
   @Override
   public Parameters getParameters() {
      return Parameters.DEFAULT_BOOLEAN;
   }
}