package com.sijobe.spc.command;

import com.sijobe.spc.util.FontColour;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Coordinate;
import com.sijobe.spc.wrapper.Player;

import java.util.List;

/**
 * Moves the player down to the next available platform below. If no platforms
 * are available the player won't be moved.
 *
 * @author simo_415
 * @version 1.0
 */
@Command (
   name = "descend",
   description = "Descends the player to the next platform below",
   videoURL = "http://www.youtube.com/watch?v=RYrBDgQJMe4",
   version = "1.1"
)
public class Descend extends StandardCommand {

   /**
    * @see com.sijobe.spc.wrapper.CommandBase#execute(com.sijobe.spc.wrapper.CommandSender, java.util.List)
    */
   @Override
   public void execute(CommandSender sender, List<?> params) {
      Player player = getSenderAsPlayer(sender);
      Coordinate previous = player.getPosition();
      int y = previous.getBlockY() - 1;
      while (y > 0) {
         if (player.isClear(new Coordinate(previous.getBlockX(), y--, previous.getBlockZ()))) {
            player.setPosition(new Coordinate(previous.getBlockX() + 0.5F, ++y, previous.getBlockZ() + 0.5F));
            player.sendChatMessage("You descended " + FontColour.AQUA + (previous.getBlockY() - y) + 
                     FontColour.WHITE + " blocks.");
            break;
         }
      }
   }
}
