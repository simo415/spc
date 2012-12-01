package com.sijobe.spc.command;

import com.sijobe.spc.util.FontColour;
import com.sijobe.spc.validation.Parameter;
import com.sijobe.spc.validation.ParameterString;
import com.sijobe.spc.validation.Parameters;
import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Player;

import java.util.List;

/**
 * Command sets the game mode of the player
 *
 * @author simo_415
 * @version 1.0
 */
@Command (
         name = "gamemode",
         description = "Changes the gamemode",
         example = "adventure",
         videoURL = "http://www.youtube.com/watch?v=1pAkRcp3KaY",
         version = "1.0",
         alias = {"gm"}
)
public class Gamemode extends StandardCommand {

   /**
    * Parameters of the command
    */
   private static final Parameters PARAMETERS = new Parameters (
      new Parameter[] {
         new ParameterString("<creative|survival|adventure>", false, new String[] {"creative","survival","adventure","0","1","2"})
      }
   );

   /**
    * @see com.sijobe.spc.wrapper.CommandBase#execute(com.sijobe.spc.wrapper.CommandSender, java.util.List)
    */
   @Override
   public void execute(CommandSender sender, List<?> params) throws CommandException {
      Player player = super.getSenderAsPlayer(sender);
      String value = null;
      if (params.size() != 0) {
         value = (String)params.get(0);
         try {
            int integer = Integer.parseInt(value);
            switch (integer) {
               case 0:
                  value = "survival";
                  break;
               case 1:
                  value = "creative";
                  break;
               case 2:
                  value = "adventure";
                  break;
               default:
                  throw new CommandException("Unknown gamemode type.");
            }
         } catch (Exception e) {
         }
      } else {
         ;
      }
      if (!player.setGameType(value)) {
         throw new CommandException("Unknown gamemode type");
      }
      sender.sendMessageToPlayer("Gamemode changed to " + FontColour.AQUA + value);
   }

   /**
    * @see com.sijobe.spc.wrapper.CommandBase#getParameters()
    */
   @Override
   public Parameters getParameters() {
      return PARAMETERS;
   }
}
