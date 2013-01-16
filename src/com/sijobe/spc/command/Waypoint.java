package com.sijobe.spc.command;

import com.sijobe.spc.validation.Parameter;
import com.sijobe.spc.validation.ParameterString;
import com.sijobe.spc.validation.Parameters;
import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandSender;

import java.util.List;

/**
 * Commands that control waypoints, settings, removing and using them. 
 * Waypoints are saved per player
 *
 * @author simo_415
 * @version 1.0
 */
public class Waypoint extends MultipleCommands {

   /**
    * The parameters of the command
    */
   private static final Parameters PARAMETERS = new Parameters (
      new Parameter[] {
         new ParameterString("<NAME>",false),
      }
   );
   
   public Waypoint(String name) {
      super(name);
   }
   
   @Override
   public boolean isEnabled() {
      return false;
   }

   @Override
   public String[] getCommands() {
      return new String[] {"set", "rem", "goto"};
   }

   @Override
   public void execute(CommandSender sender, List<?> params) throws CommandException {
      if (getName().equalsIgnoreCase("set")) {
         //Settings waypoints = SettingsManager.
      } else if (getName().equalsIgnoreCase("rem")) {
         //Settings waypoints = SettingsManager.
      } else if (getName().equalsIgnoreCase("goto")) {
         //Settings waypoints = SettingsManager.
      } else {
         assert false : "Unexpected call not being handled: " + params.get(0);
      }
   }

   @Override
   public Parameters getParameters() {
      return PARAMETERS;
   }
}
