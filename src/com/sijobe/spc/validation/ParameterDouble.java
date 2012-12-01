package com.sijobe.spc.validation;

/**
 * Handles parameter validation for the Double type
 *
 * @author simo_415
 * @version 1.1
 * @see Parameters
 */
public class ParameterDouble extends Parameter {
   /**
    * The minimum value
    */
   private double min;
   /**
    * The maximum value
    */
   private double max;
   
   /**
    * Initialises the instance with the specified label and whether the 
    * parameter is optional or not.
    * 
    * @param label - The parameter label
    * @param optional - True if the parameter is optional
    */
   public ParameterDouble(String label, boolean optional) {
      this(label,optional,Double.NEGATIVE_INFINITY,Double.POSITIVE_INFINITY);
   }
   
   /**
    * Initialises the instance with the specified label and whether the 
    * parameter is optional or not. The min and max bounds are used to verify
    * that the parsed value falls in the correct range
    * 
    * @param label - The parameter label
    * @param optional - True if the parameter is optional
    * @param min - The minimum value
    * @param max - The maximum value
    */
   public ParameterDouble(String label, boolean optional, double min, double max) {
      super(label, optional);
      this.min = min;
      this.max = max;
   }

   /**
    * @see com.sijobe.spc.validation.Parameter#validate(java.lang.String)
    */
   @Override
   public Object validate(String parameter) throws ValidationException {
      try {
         double value = Double.parseDouble(parameter);
         if (value > min && value < max) {
            return value;
         } else {
            System.out.println("V1" + value + " " + min + " " + (value > min));
            throw new ValidationException("Parameter " + parameter + 
                     " is out of the expected range (" + min + "-" + max + ")");
         }
      } catch (NumberFormatException n) {
         throw new ValidationException("Invalid text (" + parameter + 
                  ") where double value was expected.");
      } catch (Exception e) { 
         throw new ValidationException();
      }
   }
}
