package com.sijobe.spc.command;

import com.sijobe.spc.util.FontColour;
import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Player;

import java.text.DecimalFormat;
import java.util.List;

/**
 * The position command sends the players position to the user
 *
 * @author simo_415
 * @version 1.0
 * @status survived 1.7.2 update
 */
@Command (
   name = "pos",
   description = "Gives the current player position",
   videoURL = "http://www.youtube.com/watch?v=CENdX2XEQgE",
   version = "1.0"
)
public class Position extends StandardCommand {
   /**
    * @see com.sijobe.spc.wrapper.CommandBase#execute(com.sijobe.spc.wrapper.CommandSender, java.util.List)
    */
   @Override
   public void execute(CommandSender sender, List<?> params) throws CommandException {
      Player player = super.getSenderAsPlayer(sender);
      DecimalFormat f = new DecimalFormat("#.##");
      sender.sendMessageToPlayer("Player position: " + 
               FontColour.AQUA + f.format(player.getPosition().getX()) + FontColour.WHITE + ", " + 
               FontColour.GREEN + f.format(player.getPosition().getY()) + FontColour.WHITE + ", " + 
               FontColour.AQUA + f.format(player.getPosition().getZ())
      );
   }
}
