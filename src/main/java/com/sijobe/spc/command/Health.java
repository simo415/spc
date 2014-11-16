package com.sijobe.spc.command;

import com.sijobe.spc.util.FontColour;
import com.sijobe.spc.validation.Parameter;
import com.sijobe.spc.validation.ParameterString;
import com.sijobe.spc.validation.Parameters;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Player;

import java.util.List;

/**
 * The health command allows you to set the players health to pre-determined
 * figures
 *
 * @author simo_415
 * @version 1.0
 * @status survived 1.7.2 update but infinite option doesn't work
 */
@Command (
   name = "health",
   description = "Sets the health of a player to pre-defined figures",
   example = "max",
   videoURL = "http://www.youtube.com/watch?v=yEOuegvCPjY",
   version = "1.0"
)
public class Health extends StandardCommand {
   /**
    * The parameters of the command
    */
   private static final Parameters PARAMETERS = new Parameters (
      new Parameter[] {
         new ParameterString("<min|max|infinite|get>", false, new String[] {"min","max","infinite","get"}),
      }
   );

   /**
    * @see com.sijobe.spc.wrapper.CommandBase#execute(net.minecraft.src.ICommandSender, java.util.List)
    */
   @Override
   public void execute(CommandSender sender, List<?> params) {
      String action = (String)params.get(0);
      Player player = super.getSenderAsPlayer(sender);
      if (action.equalsIgnoreCase("min")) {
         player.setHealth(1);
      } else if (action.equalsIgnoreCase("max")) {
         player.setHealth(20);
      } else if (action.equalsIgnoreCase("infinite")) {
         player.setHealth(Short.MAX_VALUE);
      } 
      player.sendChatMessage("Your health is " + FontColour.AQUA + player.getHealth());
   }

   /**
    * @see com.sijobe.spc.wrapper.CommandBase#getParameters()
    */
   @Override
   public Parameters getParameters() {
      return PARAMETERS;
   }
}
