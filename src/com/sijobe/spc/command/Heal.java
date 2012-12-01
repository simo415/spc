package com.sijobe.spc.command;

import com.sijobe.spc.validation.Parameter;
import com.sijobe.spc.validation.ParameterInteger;
import com.sijobe.spc.validation.Parameters;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Player;

import java.util.List;

/**
 * Command heals the player the specified number of health
 *
 * @author simo_415
 * @version 1.0
 */
@Command (
   name = "heal",
   description = "Heals a player the specified number of points",
   example = "20",
   videoURL = "http://www.youtube.com/watch?v=h3U8K1DK2bo",
   version = "1.0"
)
public class Heal extends StandardCommand {
   /**
    * Specifies the parameters of the command
    */
   private static final Parameters PARAMETERS = new Parameters (
      new Parameter[] {
         new ParameterInteger("<QUANTITY>",false), 
      }
   );

   /**
    * @see com.sijobe.spc.wrapper.CommandBase#execute(net.minecraft.src.ICommandSender, java.util.List)
    */
   @Override
   public void execute(CommandSender sender, List<?> params) {
      Player player = super.getSenderAsPlayer(sender);
      player.heal((Integer)params.get(0));
   }

   /**
    * @see com.sijobe.spc.wrapper.CommandBase#getParameters()
    */
   @Override
   public Parameters getParameters() {
      return PARAMETERS;
   }
}
