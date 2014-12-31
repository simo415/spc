package com.sijobe.spc.command;

import java.util.List;

import com.sijobe.spc.validation.Parameter;
import com.sijobe.spc.validation.ParameterString;
import com.sijobe.spc.validation.Parameters;
import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Player;

/**
 * Changes the dimension that the player is in
 *
 * @author simo_415
 * @version 1.0
 * @status broken through 1.7.2 update
 */
@Command (
   name = "useportal",
   description = "Changes the dimension that the player is in",
   videoURL = "http://www.youtube.com/watch?v=HbhgS5JyPHc",
   version = "1.4.6"
)
public class UsePortal extends StandardCommand {

   private static final int DIMENSION_NETHER = -1;
   private static final int DIMENSION_NORMAL = 0;
   private static final int DIMENSION_END = 1;
   
   private static final Parameters PARAMETERS = new Parameters (
            new Parameter[] {
                     new ParameterString("<normal|nether|end>", false,
                              new String[] {"normal", "nether", "end"}),
                              new ParameterString("", true, true)
            }
            );
   
   @Override
   public void execute(CommandSender sender, List<?> params) throws CommandException {
      Player player = super.getSenderAsPlayer(sender);
      String type = (String)params.get(0);
      if (type.equalsIgnoreCase("normal")) {
          player.changeDimension(DIMENSION_NORMAL);
       } else if (type.equalsIgnoreCase("nether")) {
          player.changeDimension(DIMENSION_NETHER);
       } else if (type.equalsIgnoreCase("end")) {
          player.changeDimension(DIMENSION_END);
       } else {
          throw new CommandException("Unknown dimension specified");
       }
   }
   
   @Override
   public Parameters getParameters() {
      return PARAMETERS;
   }
}
