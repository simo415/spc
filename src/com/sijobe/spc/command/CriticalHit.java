package com.sijobe.spc.command;

import com.sijobe.spc.validation.Parameters;
import com.sijobe.spc.wrapper.CommandSender;

import java.util.List;


public class CriticalHit extends StandardCommand {
   
   @Override
   public String getName() {
      return "criticalhit";
   }

   @Override
   public void execute(CommandSender sender, List<?> params) {
      // TODO: Client side
   }

   @Override
   public Parameters getParameters() {
      return Parameters.DEFAULT_BOOLEAN;
   }

   @Override
   public boolean isEnabled() {
      return false;
   }
}
