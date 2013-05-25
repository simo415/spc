package com.sijobe.spc.command;

import com.sijobe.spc.util.FontColour;
import com.sijobe.spc.validation.Parameters;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Player;

import java.util.List;

/**
 * Allows player damage to be turned on and off
 *
 * @author simo_415
 * @version 1.0
 */
@Command (
   name = "damage",
   description = "Turns damage on/off",
   example = "enable",
   videoURL = "http://www.youtube.com/watch?v=_nIre9Wxq_0",
   version = "1.0",
   alias = {"god"}
)
public class Damage extends StandardCommand {   
   /**
    * @see com.sijobe.spc.wrapper.CommandBase#execute(net.minecraft.src.ICommandSender, java.util.List)
    */
   @Override
   public void execute(CommandSender sender, List<?> params) {
      Player player = super.getSenderAsPlayer(sender);
      if (params.size() > 0) {
         player.setDamage((Boolean)params.get(0));
      } else {
         player.setDamage(!player.getDamage());
      }
      player.sendChatMessage("Player damage was " + 
               FontColour.AQUA + (player.getDamage() ? "enabled" : "disabled"));
   }

   /**
    * @see com.sijobe.spc.wrapper.CommandBase#getParameters()
    */
   @Override
   public Parameters getParameters() {
      return Parameters.DEFAULT_BOOLEAN;
   }
}
