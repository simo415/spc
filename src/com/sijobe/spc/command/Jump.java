package com.sijobe.spc.command;

import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Coordinate;
import com.sijobe.spc.wrapper.Player;

import java.util.List;

/**
 * Sets the players spawn based on the player position or the arguments  //TODO make doc correct
 * provided
 *
 * @author simo_415
 * @version 1.0
 * @status survived 1.7.2 update
 */
@Command ( 
   name = "jump",
   description = "Moves the player to where the player is pointing",
   videoURL = "http://www.youtube.com/watch?v=Kc6lm3XxSUA",
   version = "1.0"
)
public class Jump extends StandardCommand {

   /**
    * @see com.sijobe.spc.wrapper.CommandBase#execute(com.sijobe.spc.wrapper.CommandSender, java.util.List)
    */
   @Override
   public void execute(CommandSender sender, List<?> params) throws CommandException {
      Player player = getSenderAsPlayer(sender);
      Coordinate hit = player.trace(128);
      if (hit == null) {
         throw new CommandException("No block in sight.");
      }
      int y = hit.getBlockY() + 1; 
      while (y < 260) {
         if (player.isClear(new Coordinate(hit.getBlockX(), y++, hit.getBlockZ()))) {
            player.setPosition(new Coordinate(hit.getBlockX() + 0.5F, --y, hit.getBlockZ() + 0.5F));
            break;
         }
      }
   }
}
