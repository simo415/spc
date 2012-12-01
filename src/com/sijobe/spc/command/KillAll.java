package com.sijobe.spc.command;

import com.sijobe.spc.validation.Parameter;
import com.sijobe.spc.validation.ParameterInteger;
import com.sijobe.spc.validation.ParameterString;
import com.sijobe.spc.validation.Parameters;
import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Entity;
import com.sijobe.spc.wrapper.Player;

import java.util.List;

/**
 * Allows the user to kill all of a certain entity type using the specified
 * type variable, or all mobs if no parameters are specified
 *
 * @author simo_415
 * @version 1.0
 */

@Command (
   name = "killall",
   description = "Kills all of the specified entity type, this will destroy ALL entities (paintings, minecarts, etc) unless specified otherwise.",
   example = "creeper 16",
   videoURL = "http://www.youtube.com/watch?v=rVz5lAhZ54w",
   version = "1.0"
)
public class KillAll extends StandardCommand {

   /**
    * The parameters of the command
    */
   private static final Parameters PARAMETERS = new Parameters (
      new Parameter[] {
         new ParameterString("[ENTITY_TYPE]",true),
         new ParameterInteger("[DISTANCE]",true),
      }
   );
   
   /**
    * @see com.sijobe.spc.wrapper.CommandBase#execute(com.sijobe.spc.wrapper.CommandSender, java.util.List)
    */
   @Override
   public void execute(CommandSender sender, List<?> params) throws CommandException {
      Player player = getSenderAsPlayer(sender);
      Entity.killEntities("mob", player.getPosition(), player.getWorld(), 128);
   }

   /**
    * @see com.sijobe.spc.wrapper.CommandBase#getParameters()
    */
   @Override
   public Parameters getParameters() {
      return PARAMETERS;
   }
}
