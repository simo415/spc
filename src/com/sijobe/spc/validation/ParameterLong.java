package com.sijobe.spc.validation;


public class ParameterLong extends Parameter {
   private long min;
   private long max;
   
   public ParameterLong(String label, boolean optional) {
      this(label,optional,Integer.MIN_VALUE,Integer.MAX_VALUE);
   }
   
   public ParameterLong(String label, boolean optional, long min, long max) {
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
         long value = Long.parseLong(parameter);
         if (value > min && value < max) {
            return value;
         } else {
            throw new ValidationException("Parameter " + parameter + 
                     " is out of the expected range (" + min + "-" + max + ")");
         }
      } catch (NumberFormatException n) {
         throw new ValidationException("Invalid text (" + parameter + 
                  ") where long value was expected.");
      } catch (Exception e) { 
         throw new ValidationException();
      }
   }
}
