package com.sijobe.spc.command;

import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Player;

import java.util.List;

/**
 * The home command teleports the player who ran it to their spawn point
 *
 * @author simo_415
 * @version 1.0
 */
@Command (
   name = "home",
   description = "Teleports the player to their spawn point",
   videoURL = "http://www.youtube.com/watch?v=PEtBSeRqcU8"
)
public class Home extends StandardCommand {

   /**
    * @see com.sijobe.spc.wrapper.CommandBase#execute(com.sijobe.spc.wrapper.CommandSender, java.util.List)
    */
   @Override
   public void execute(CommandSender sender, List<?> params) throws CommandException {
      Player player = getSenderAsPlayer(sender);
      player.setPosition(player.getWorld().getSpawn());
   }
}
