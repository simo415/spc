package com.sijobe.spc.command;

import com.sijobe.spc.validation.Parameter;
import com.sijobe.spc.validation.ParameterDouble;
import com.sijobe.spc.validation.ParameterInteger;
import com.sijobe.spc.validation.Parameters;
import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Coordinate;
import com.sijobe.spc.wrapper.Player;

import java.util.List;

/**
 * Creates an explosion of the specified size
 *
 * @author simo_415
 * @version 1.0
 * @status survived 1.7.2 update
 */
@Command ( 
   name = "explode",
   description = "Sets off an explosion in your current location.",
   example = "10",
   videoURL = "http://www.youtube.com/watch?v=rWOpPWYjjxY"
)
public class Explode extends StandardCommand {
   
   /**
    * The default size of the explosion if no explosion size is specified
    */
   public static final int DEFAULT_SIZE = 4;
   
   /**
    * The parameters of the command
    */
   private static final Parameters PARAMETERS = new Parameters (
      new Parameter[] {
         new ParameterInteger("[SIZE]",true),
         new ParameterDouble("[X Y Z]",true),
         new ParameterDouble("",true),
         new ParameterDouble("",true)
      }
   );

   /**
    * @see com.sijobe.spc.wrapper.CommandBase#execute(com.sijobe.spc.wrapper.CommandSender, java.util.List)
    */
   @Override
   public void execute(CommandSender sender, List<?> params) throws CommandException {
      Player player = getSenderAsPlayer(sender);
      if (params.size() == 0) {
         player.getWorld().createExplosion(player, player.getPosition(), DEFAULT_SIZE);
      } else if (params.size() == 1) {
         player.getWorld().createExplosion(player, player.getPosition(), (Integer)params.get(0));
      } else if (params.size() > 3) {
         Coordinate location = new Coordinate((Double)params.get(1), (Double)params.get(2), (Double)params.get(3));
         player.getWorld().createExplosion(player, location, (Integer)params.get(0));
      }
      sender.sendMessageToPlayer("Boom!");
   }

   /**
    * @see com.sijobe.spc.wrapper.CommandBase#getParameters()
    */
   @Override
   public Parameters getParameters() {
      return PARAMETERS;
   }
}
