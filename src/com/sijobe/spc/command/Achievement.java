package com.sijobe.spc.command;

import com.sijobe.spc.validation.Parameter;
import com.sijobe.spc.validation.ParameterString;
import com.sijobe.spc.validation.Parameters;
import com.sijobe.spc.wrapper.CommandSender;

import java.util.List;

public class Achievement extends StandardCommand {
   
   /**
    * Parameters of the command
    */
   private static final Parameters PARAMETERS = new Parameters (
      new Parameter[] {
         new ParameterString("<list|unlock>", false, new String[] {"list", "unlock"})
      }
   );
   
   @Override
   public String getName() {
      return "achievement";
   }

   @Override
   public void execute(CommandSender sender, List<?> params) {
      // TODO: Client side
   }

   @Override
   public Parameters getParameters() {
      return PARAMETERS;
   }
   
   @Override
   public boolean isEnabled() {
      return false;
   }

}
