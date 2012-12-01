package com.sijobe.spc.command;

import com.sijobe.spc.util.FontColour;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Coordinate;
import com.sijobe.spc.wrapper.Player;

import java.util.List;

/**
 * Moves the player to the next available platform above the player. If there 
 * are no available spots then the player is not moved.
 *
 * @author simo_415
 * @version 1.0
 */
@Command (
   name = "ascend",
   description = "Ascends the player to the platform above",
   videoURL = "http://www.youtube.com/watch?v=-94uM70XJlw",
   version = "1.1"
)
public class Ascend extends StandardCommand {

   @Override
   public void execute(CommandSender sender, List<?> params) {
      Player player = getSenderAsPlayer(sender);
      Coordinate c = player.getPosition();
      int y = c.getBlockY() + 1; 
      while (y < 260) {
         if (player.isClear(new Coordinate(c.getBlockX(), y++, c.getBlockZ()))) {
            player.setPosition(new Coordinate(c.getBlockX() + 0.5F, --y, c.getBlockZ() + 0.5F));
            player.sendChatMessage("You ascended " + FontColour.AQUA + (y - c.getBlockY()) + 
                     FontColour.WHITE + " blocks.");
            break;
         }
      }
   }
}
