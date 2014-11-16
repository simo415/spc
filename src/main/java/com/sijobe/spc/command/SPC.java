package com.sijobe.spc.command;

import com.sijobe.spc.core.Constants;
import com.sijobe.spc.util.FontColour;
import com.sijobe.spc.validation.Parameter;
import com.sijobe.spc.validation.ParameterString;
import com.sijobe.spc.validation.Parameters;
import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandSender;

import java.util.List;

/**
 * Contains generic SPC commands that provide standard information
 *
 * @author simo_415
 * @version 1.0
 * @status survived 1.7.2 update
 */
@Command (
   name = "spc",
   description = "Provides generic commands around SPC",
   example = "version",
   version = "1.0"
)
public class SPC extends StandardCommand {

   /**
    * The parameters for the command
    */
   private static final Parameters PARAMETERS = new Parameters (
      new Parameter[] {
         new ParameterString("<version>", false, new String[] {"version"}),
      }
   );

   /**
    * @see com.sijobe.spc.wrapper.CommandBase#execute(com.sijobe.spc.wrapper.CommandSender, java.util.List)
    */
   @Override
   public void execute(CommandSender sender, List<?> params) throws CommandException {
      if (((String)params.get(0)).equalsIgnoreCase("version")) {
         sender.sendMessageToPlayer("The current version is " + FontColour.AQUA + Constants.VERSION);
      } else if (((String)params.get(0)).equalsIgnoreCase("reload")) {
         ;
      } else {
         assert false : "Unknown parameter type " + (String)params.get(0);
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
