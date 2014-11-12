package com.sijobe.spc.command;

import com.sijobe.spc.util.FontColour;
import com.sijobe.spc.validation.Parameters;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Player;
import com.sijobe.spc.wrapper.World;

import java.util.List;

/**
 * Toggles hardcore mode on/off
 *
 * @author simo_415
 * @version 1.0
 * @status survived 1.7.2 update
 */
@Command (
   name = "hardcore",
   description = "Configures the world to be in hardcore mode or not",
   example = "enable",
   videoURL = "http://www.youtube.com/watch?v=1_woyXQ3io4",
   version = "1.0"
)
public class Hardcore extends StandardCommand {
   /**
    * @see com.sijobe.spc.wrapper.CommandBase#execute(net.minecraft.src.ICommandSender, java.util.List)
    */
   @Override
   public void execute(CommandSender sender, List<?> params) {
      Player player = super.getSenderAsPlayer(sender);
      World world = player.getWorld();
      if (params.size() > 0) {
         world.setHardcore((Boolean)params.get(0));
      } else {
         world.setHardcore(!world.isHardcore());
      }
      player.sendChatMessage("Hardcore mode was " + 
               FontColour.AQUA + (world.isHardcore() ? "enabled" : "disabled"));
   }

   /**
    * @see com.sijobe.spc.wrapper.CommandBase#getParameters()
    */
   @Override
   public Parameters getParameters() {
      return Parameters.DEFAULT_BOOLEAN;
   }
}
