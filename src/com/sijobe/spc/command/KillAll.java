package com.sijobe.spc.command;

import com.sijobe.spc.validation.Parameter;
import com.sijobe.spc.validation.ParameterInteger;
import com.sijobe.spc.validation.ParameterString;
import com.sijobe.spc.validation.Parameters;
import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Entity;
import com.sijobe.spc.wrapper.Player;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.List;

/**
 * Allows the user to kill all of a certain entity type using the specified
 * type variable, or all mobs if no parameters are specified
 *
 * @author simo_415
 * @version 1.0
 * @status survived 1.7.2 update
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
      double radius = 128.0D;
      String entityType = "mob";
      if(params.size() > 0) {
         entityType = (String)params.get(0);
         if(Entity.getEntityClass(entityType) == null) {
            try {
               radius = Double.parseDouble((String)params.get(0));
               sender.sendMessageToPlayer("Setting radius to: " + radius);
               entityType = "mob";
            } catch (NumberFormatException nfe) {
               throw new CommandException("Unknown entity specified.");
            }
         }
         if(params.size() > 1) {
            radius = (Integer)params.get(1);
         }
      }
      if(radius <=0 || radius > 256) {
         throw new CommandException("Radius should be between 0 and 256.");
      }
      List<net.minecraft.entity.Entity> removedEntities =
         Entity.killEntities(entityType, player.getPosition(), player.getWorld(), radius);
      sender.sendMessageToPlayer(removedEntities.size() + " entity(s) removed.");
   }

   /**
    * @see com.sijobe.spc.wrapper.CommandBase#getParameters()
    */
   @Override
   public Parameters getParameters() {
      return PARAMETERS;
   }
}
