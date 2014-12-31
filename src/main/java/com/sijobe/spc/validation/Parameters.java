package com.sijobe.spc.validation;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to provide parameter validation and type checking
 *
 * @author simo_415
 * @version 1.0
 */
public class Parameters {
   
   /**
    * Optional String of variable length arguments - ie: any input
    */
   public static final Parameters DEFAULT = new Parameters(
      new Parameter[] {
         new ParameterString("",true,true)
      }
   );
   /**
    * Parameters of the command
    */
   public static final Parameters DEFAULT_BOOLEAN = new Parameters (
      new Parameter[] {
         new ParameterBoolean("[enable|disable]", true, "enable", "disable")
      }
   );
   
   /**
    * Each of the parameter validators to use to validate
    */
   private Parameter params[];

   /**
    * Initialises the class using the specified validation parameters
    * 
    * @param params - The validation parameters to use
    */
   public Parameters(Parameter params[]) {
      this.params = params;
   }

   /**
    * Validates the input against the parameters for this command. The input
    * is validated by each of the validators and which results in a List of
    * type correct Object returned. 
    * 
    * If more parameters are specified than validators and the last validator
    * is not variable length then any extra parameters are ignored.
    * 
    * @param parameters - The parameters to validate
    * @return A list of validated objects that match their validation type
    * @throws ValidationException - when a validation error occurs an expection
    * is thrown detailing the problem
    */
   public List<?> validate(String parameters[]) throws ValidationException {
      List<Object> validated = new ArrayList<Object>();
      for (int i = 0; i < params.length; i++) {
         // Checks for the correct number of arguments
         if (i + 1 > parameters.length) { 
            if (params[i].isOptional()) {
               continue;
            } else {
               throw new ValidationException("Not enough arguments");
            }
         }

         try {
            if (i == params.length - 1 && parameters.length > params.length && params[i].isVariableLength()) { 
               // Last Parameter and variable length - combine parameters and validate
               String variableLength = "";
               for (int j = i; j < parameters.length; j++) {
                  if (j == i) {
                     variableLength = parameters[j];
                  } else {
                     variableLength = variableLength + " " + parameters[j];
                  }
               }
               validated.add(params[i].validate(variableLength));
            } else {
               // Validate the parameter
               validated.add(params[i].validate(parameters[i]));
            }
         } catch (ValidationException v) {
            throw v;
         } catch (Exception e) {
            throw new ValidationException("Problem with the command: " + e);
         }
      }
      return validated;
   }

   /**
    * Gets how to use the command
    * 
    * @return The String representing how to use the command
    */
   public String getUsage() {
      String usage = "";
      for (Parameter param : params) {
         usage += param.getLabel() + " ";
      }
      return usage.trim();
   }
}
