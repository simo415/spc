package com.sijobe.spc.validation;

import com.sijobe.spc.wrapper.MinecraftServer;

/**
 * Validates that the provided value is a logged in players name
 *
 * @author simo_415
 * @version 1.0
 */
public class ParameterPlayer extends Parameter {

   /**
    * Initialises the instance using the set parameters
    * 
    * @param label - The name of the parameter
    * @param optional - True if the parameter is optional
    */
   public ParameterPlayer(String label, boolean optional) {
      super(label,optional);
   }

   /**
    * @see com.sijobe.spc.validation.Parameter#validate(java.lang.String)
    */
   @Override
   public Object validate(String parameter) throws ValidationException {
      // Need to loop through rather than use List.contains due to case sensitivity
      for (String player : MinecraftServer.getPlayers()) {
         if (parameter.equalsIgnoreCase(player)) {
            return MinecraftServer.getPlayerByUsername(player);
         }
      }
      throw new ValidationException("Unknown player name \"" + parameter + "\"");
   }
}
