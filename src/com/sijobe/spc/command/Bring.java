package com.sijobe.spc.command;

import com.sijobe.spc.validation.Parameter;
import com.sijobe.spc.validation.ParameterInteger;
import com.sijobe.spc.validation.ParameterString;
import com.sijobe.spc.validation.Parameters;
import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Entity;
import com.sijobe.spc.wrapper.Player;
import net.minecraft.src.EntityPlayerMP;

import java.util.List;

/**
 * Allows the user to bring all of a certain entity type using the specified
 * type variable, or items if no parameters are specified
 *
 * @author q3hardcore
 * @version 1.0
 */

@Command (
   name = "bring",
   description = "Brings all of the specified entity type, this will bring items by default.",
   example = "creeper 16",
   videoURL = "http://www.youtube.com/watch?v=ApUL365mLfY",
   version = "1.0"
)
public class Bring extends StandardCommand {

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
      String entityType = "item";
      if(params.size() > 0) {
         entityType = (String)params.get(0);
         if(Entity.getEntityClass(entityType) == null) {
            try {
               radius = Double.parseDouble((String)params.get(0));
               sender.sendMessageToPlayer("Setting radius to: " + radius);
               entityType = "item";
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
      List<net.minecraft.src.Entity> foundEntities = Entity.findEntities(entityType, player.getPosition(), player.getWorld(), radius);
      net.minecraft.src.Vec3 vec3d = player.getMinecraftPlayer().getLook(1.0F);
      double d = 5.0D;
      double offsetY = player.getMinecraftPlayer().posY + player.getMinecraftPlayer().getEyeHeight();
      double d1 = player.getMinecraftPlayer().posX + vec3d.xCoord * d;
      double d2 = offsetY + vec3d.yCoord * d;
      double d3 = player.getMinecraftPlayer().posZ + vec3d.zCoord * d;
      for(net.minecraft.src.Entity entity : foundEntities) {
         if(entity == player.getMinecraftPlayer()) {
            continue; // don't allow the player to bring themselves
         }
         entity.setPosition(d1, d2 + 0.5D, d3);
      }
      sender.sendMessageToPlayer(foundEntities.size() + " entities brought.");
   }

   /**
    * @see com.sijobe.spc.wrapper.CommandBase#getParameters()
    */
   @Override
   public Parameters getParameters() {
      return PARAMETERS;
   }
}
