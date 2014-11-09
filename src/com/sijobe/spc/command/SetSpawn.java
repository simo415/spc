package com.sijobe.spc.command;

import com.sijobe.spc.util.FontColour;
import com.sijobe.spc.validation.Parameter;
import com.sijobe.spc.validation.ParameterInteger;
import com.sijobe.spc.validation.Parameters;
import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Coordinate;
import com.sijobe.spc.wrapper.Player;

import java.util.List;

/**
 * Sets the players spawn based on the player position or the arguments 
 * provided
 *
 * @author simo_415
 * @version 1.0
 * @status survived 1.7.2 update
 */
@Command ( 
   name = "setspawn",
   description = "Sets the spawn point of the world the player is in",
   videoURL = "http://www.youtube.com/watch?v=2ZTAiIPpL3U"
)
public class SetSpawn extends StandardCommand {
   
   /**
    * The parameters of the command
    */
   private static final Parameters PARAMETERS = new Parameters (
      new Parameter[] {
         new ParameterInteger("[X Y Z]",true),
         new ParameterInteger("",true),
         new ParameterInteger("",true),
      }
   );

   /**
    * @see com.sijobe.spc.wrapper.CommandBase#execute(com.sijobe.spc.wrapper.CommandSender, java.util.List)
    */
   @Override
   public void execute(CommandSender sender, List<?> params) throws CommandException {
      if (params.size() != 0 && params.size() < 3) {
         throw new CommandException("Usage: " + getUsage(sender));
      }
      Player player = getSenderAsPlayer(sender);
      if (params.size() == 0) {
         player.getWorld().setSpawn(player.getPosition());
      } else {
         player.getWorld().setSpawn(new Coordinate((Integer)params.get(0),(Integer)params.get(1),(Integer)params.get(2)));
      }
      Coordinate spawn = player.getWorld().getSpawn();
      player.sendChatMessage("Spawn point set to " 
               + FontColour.AQUA + spawn.getBlockX() + FontColour.WHITE + ","
               + FontColour.AQUA + spawn.getBlockY() + FontColour.WHITE + ","
               + FontColour.AQUA + spawn.getBlockZ()
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
