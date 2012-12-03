package com.sijobe.spc.command;

import com.sijobe.spc.util.FontColour;
import com.sijobe.spc.validation.Parameter;
import com.sijobe.spc.validation.ParameterString;
import com.sijobe.spc.validation.ParameterInteger;
import com.sijobe.spc.validation.Parameters;
import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Coordinate;
import com.sijobe.spc.wrapper.Player;

import java.util.List;

/**
 * Allows you to move the player to the specified set of distance and direction
 *
 * @author simo_415
 * @version 1.0
 */
@Command (
         name = "moveplayer",
         description = "move the player to the distance and direction specified",
         example = "10 north",
         videoURL = "http://www.youtube.com/watch?v=4omil8aeRRY",
         version = "1.4.5"
)
public class MovePlayer extends StandardCommand {

   /**
    * The parameters of the command
    */
   private static final Parameters PARAMETERS = new Parameters (
            new Parameter[] {
                     new ParameterInteger("[DISTINCT]", false),
                     new ParameterString("[DIRECTION(N/E/S/W/U/D)]", false)
            }
   );

   /**
    * @see com.sijobe.spc.wrapper.CommandBase#execute(com.sijobe.spc.wrapper.CommandSender, java.util.List)
    */
   @Override
   public void execute(CommandSender sender, List<?> params) throws CommandException {
      Player player = super.getSenderAsPlayer(sender);
      Coordinate c = player.getPosition();
      int distance = 0;
      try {
         distance = Integer.parseInt((String)params.get(0));
      } catch (NumberFormatException e) {
         throw new CommandException("Could not parse disance specified as integer: " + params.get(0));
      }
      
      if (((String)params.get(1)).toUpperCase().startsWith("N")) {
         player.setPosition(new Coordinate(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ() - distance));
      } else if (((String)params.get(1)).toUpperCase().startsWith("E")) {
         player.setPosition(new Coordinate(player.getPosition().getX() + distance, player.getPosition().getY(), player.getPosition().getZ()));
      } else if (((String)params.get(1)).toUpperCase().startsWith("S")) {
         player.setPosition(new Coordinate(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ() + distance));
      } else if (((String)params.get(1)).toUpperCase().startsWith("W")) {
         player.setPosition(new Coordinate(player.getPosition().getX() - distance, player.getPosition().getY(), player.getPosition().getZ()));
      } else if (((String)params.get(1)).toUpperCase().startsWith("U")) {
         player.setPosition(new Coordinate(player.getPosition().getX(), player.getPosition().getY() + distance, player.getPosition().getZ()));
      } else if (((String)params.get(1)).toUpperCase().startsWith("D")) {
         player.setPosition(new Coordinate(player.getPosition().getX(), player.getPosition().getY() - distance, player.getPosition().getZ() - distance));
      } else {
         throw new CommandException("Invalid direction specified.");
      }

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