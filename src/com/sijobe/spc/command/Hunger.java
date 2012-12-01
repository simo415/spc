package com.sijobe.spc.command;

import com.sijobe.spc.util.FontColour;
import com.sijobe.spc.validation.Parameter;
import com.sijobe.spc.validation.ParameterString;
import com.sijobe.spc.validation.Parameters;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Player;

import java.util.List;

/**
 * Provides an implementation of the hunger command. Command allows the player 
 * to heal themselves to predetermined figures
 *
 * @author simo_415
 * @version 1.0
 */
@Command (
   name = "hunger",
   description = "Sets the players hunger level to pre-defined figures",
   example = "infinite",
   videoURL = "http://www.youtube.com/watch?v=Ymc7JDgdtXU",
   version = "1.0"
)
public class Hunger extends StandardCommand {
   /**
    * Parameters of the command
    */
   private static final Parameters PARAMETERS = new Parameters ( // TODO: Enable/disable hunger
      new Parameter[] {
         new ParameterString("<empty|full|infinite>", false, 
                  new String[] {"empty","full","infinite"})
      }
   );
 
   /**
    * @see com.sijobe.spc.wrapper.CommandBase#execute(net.minecraft.src.ICommandSender, java.util.List)
    */
   @Override
   public void execute(CommandSender sender, List<?> params) {
      String action = (String)params.get(0);
      Player player = super.getSenderAsPlayer(sender);
      if (action.equalsIgnoreCase("empty")) {
         player.setHunger(0);
      } else if (action.equalsIgnoreCase("full")) {
         player.setHunger(20);
      } else if (action.equalsIgnoreCase("infinite")) {
         player.setHunger(Short.MAX_VALUE);
      } else if (action.equalsIgnoreCase("enable")) {
         player.sendChatMessage(FontColour.ORANGE + "Cannot enable/disable hunger");
         return;
      } else if (action.equalsIgnoreCase("disable")) {
         player.sendChatMessage(FontColour.ORANGE + "Cannot enable/disable hunger");
         return;
      }
      player.sendChatMessage("Your hunger level is set at " + FontColour.AQUA + player.getHunger());
   }

   /**
    * @see com.sijobe.spc.wrapper.CommandBase#getParameters()
    */
   @Override
   public Parameters getParameters() {
      return PARAMETERS;
   }
}
