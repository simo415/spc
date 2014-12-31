package com.sijobe.spc.validation;


public class ParameterInteger extends Parameter {
   private int min;
   private int max;
   
   public ParameterInteger(String label, boolean optional) {
      this(label,optional,Integer.MIN_VALUE,Integer.MAX_VALUE);
   }
   
   public ParameterInteger(String label, boolean optional, int min, int max) {
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
         int value = Integer.parseInt(parameter);
         if (value > min && value < max) {
            return value;
         } else {
            throw new ValidationException("Parameter " + parameter + 
                     " is out of the expected range (" + min + "-" + max + ")");
         }
      } catch (NumberFormatException n) {
         throw new ValidationException("Invalid text (" + parameter + 
                  ") where integer value was expected.");
      } catch (Exception e) {
         throw new ValidationException();
      }
   }
}
