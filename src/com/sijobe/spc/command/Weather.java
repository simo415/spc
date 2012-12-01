package com.sijobe.spc.command;

import com.sijobe.spc.util.FontColour;
import com.sijobe.spc.validation.Parameter;
import com.sijobe.spc.validation.ParameterBoolean;
import com.sijobe.spc.validation.ParameterString;
import com.sijobe.spc.validation.Parameters;
import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Coordinate;
import com.sijobe.spc.wrapper.Player;
import com.sijobe.spc.wrapper.World;

import java.util.List;

/**
 * Implements the weather command with some basic functionality that entails 
 * turning the rain or thunder on and off and able to send a lightning bolt
 * down to where you're pointing.
 *
 * TODO: Enable/disable weather altogether
 *
 * @author simo_415
 * @version 1.0
 */
@Command (
   name = "weather",
   description = "Commands to toggle weather effects on/off",
   example = "lightning",
   videoURL = "http://www.youtube.com/watch?v=mgGOtzmD31k",
   version = "1.0"
)
public class Weather extends StandardCommand {
   /**
    * The parameters for the command
    */
   private static final Parameters PARAMETERS = new Parameters (
      new Parameter[] {
         new ParameterString("<lightning|[thunder|rain [enable|disable]]>", false, 
            new String[] {"lightning","thunder","rain"}),
         new ParameterBoolean("", true, "enable", "disable")
      }
   );

   /**
    * @see com.sijobe.spc.wrapper.CommandBase#execute(com.sijobe.spc.wrapper.CommandSender, java.util.List)
    */
   @Override
   public void execute(CommandSender sender, List<?> params) throws CommandException {
      String argument = (String)params.get(0);
      Player player = super.getSenderAsPlayer(sender);
      World world = player.getWorld();
      if (argument.equalsIgnoreCase("rain")) {
         // Toggles rain on/off
         boolean rain = !world.isRaining();
         if (params.size() > 1) {
            rain = (Boolean)params.get(1);
         }
         world.setRaining(rain);
         sender.sendMessageToPlayer("Rain was " + FontColour.AQUA + (rain ? "enabled" : "disabled"));
      } else if (argument.equalsIgnoreCase("thunder")) {
         // Toggles thunder storms on/off (changes rain too)
         boolean thunder = !world.isThunder();
         if (params.size() > 1) {
            thunder = (Boolean)params.get(1);
         }
         world.setThunder(thunder);
         world.setRaining(thunder);
         sender.sendMessageToPlayer("Thunder was " + FontColour.AQUA + (thunder ? "enabled" : "disabled"));
      } else if (argument.equalsIgnoreCase("lightning")) {
         Coordinate coordinate = player.trace(128.0);
         if (coordinate == null) {
            return;
         }
         world.useLightning(coordinate);
      } else {
         sender.sendMessageToPlayer("The " + argument + " command is currently unavailable.");
      }
   }

   /**
    * @see com.sijobe.spc.wrapper.CommandBase#getParameters()
    */
   @Override
   public Parameters getParameters() {
      return PARAMETERS;
   }
}
