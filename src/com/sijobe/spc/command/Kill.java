package com.sijobe.spc.command;

import com.sijobe.spc.wrapper.CommandSender;

import java.util.List;

/**
 * Kills the player that runs the command
 *
 * @author simo_415
 * @version 1.0
 */
@Command (
   name = "kill",
   description = "Kills the current player",
   videoURL = "http://www.youtube.com/watch?v=iNzdikZvnyI",
   version = "1.0"
)
public class Kill extends StandardCommand {
   /**
    * @see com.sijobe.spc.wrapper.CommandBase#execute(net.minecraft.src.ICommandSender, java.util.List)
    */
   @Override
   public void execute(CommandSender sender, List<?> params) {
      super.getSenderAsPlayer(sender).setHealth(0);
   }
}
