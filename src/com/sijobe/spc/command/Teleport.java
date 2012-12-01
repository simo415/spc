package com.sijobe.spc.command;

import com.sijobe.spc.util.FontColour;
import com.sijobe.spc.validation.Parameter;
import com.sijobe.spc.validation.ParameterDouble;
import com.sijobe.spc.validation.Parameters;
import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Coordinate;
import com.sijobe.spc.wrapper.Player;

import java.util.List;

/**
 * Allows you to teleport the player to the specified set of coordinates
 *
 * @author simo_415
 * @version 1.0
 */
@Command (
   name = "teleport",
   description = "Teleports the player to the destination specified",
   example = "0 66 0",
   videoURL = "http://www.youtube.com/watch?v=4omil8aeRRY",
   version = "1.0",
   alias = {"tele","t"}
)
public class Teleport extends StandardCommand {

   /**
    * The parameters of the command
    */
   private static final Parameters PARAMETERS = new Parameters (
      new Parameter[] {
         new ParameterDouble("<X>", false),
         new ParameterDouble("<Y>", false),
         new ParameterDouble("<Z>", false)
      }
   );

   /**
    * @see com.sijobe.spc.wrapper.CommandBase#execute(com.sijobe.spc.wrapper.CommandSender, java.util.List)
    */
   @Override
   public void execute(CommandSender sender, List<?> params) throws CommandException {
      Player player = super.getSenderAsPlayer(sender);
      Coordinate c = player.getPosition();
      player.setPosition(new Coordinate((Double)params.get(0), (Double)params.get(1), (Double)params.get(2)));
      sender.sendMessageToPlayer("Moved from " + 
               FontColour.AQUA + c.getBlockX() + FontColour.WHITE + ", " + 
               FontColour.AQUA + c.getBlockX() + FontColour.WHITE + ", " + 
               FontColour.AQUA + c.getBlockX() + FontColour.WHITE + " to " + 
               FontColour.AQUA + player.getPosition().getBlockX() + FontColour.WHITE + ", " + 
               FontColour.AQUA + player.getPosition().getBlockY() + FontColour.WHITE + ", " + 
               FontColour.AQUA + player.getPosition().getBlockZ()
      );
   }

   /**
    * @see com.sijobe.spc.wrapper.CommandBase#getParameters()
    */
   @Override
   public Parameters getParameters() {
      return PARAMETERS;
   }
}
