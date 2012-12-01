package com.sijobe.spc.validation;

import java.util.ArrayList;
import java.util.List;


public class ParameterString extends Parameter {
   private List<String> validValues;
   private String expected;

   public ParameterString(String label, boolean optional) {
      this(label,optional,false,null);
   }
   
   public ParameterString(String label, boolean optional, boolean variableLength) {
      this(label,optional,variableLength,null);
   }
   
   public ParameterString(String label, boolean optional, String validValues[]) {
      super(label, optional, false);
      List<String> values = new ArrayList<String>();
      for (String value : validValues) {
         values.add(value);
      }
      initialise(label, optional, false, values);
   }
   
   public ParameterString(String label, boolean optional, List<String> validValues) {
      this(label,optional,false,validValues);
   }
   
   public ParameterString(String label, boolean optional, boolean variableLength, List<String> validValues) {
      super(label, optional, variableLength);
      initialise(label, optional, variableLength, validValues);
   }
   
   private void initialise(String label, boolean optional, boolean variableLength, List<String> validValues) {
      this.validValues = validValues;
      if (validValues == null) {
         validValues = new ArrayList<String>();
      }
      for (int i = 0; i < validValues.size(); i++) {
         validValues.set(i, validValues.get(i).toLowerCase());
         if (i == 0) {
            expected = validValues.get(i);
         } else {
            expected = expected + ", " + validValues.get(i);
         }
      }
   }

   /**
    * @see com.sijobe.spc.validation.Parameter#validate(java.lang.String)
    */
   @Override
   public Object validate(String parameter) throws ValidationException {
      if (validValues == null) {
         return parameter;
      }
      if (validValues.contains(parameter.toLowerCase())) {
         return parameter;
      }
      throw new ValidationException("Parameter " + parameter + 
               " is an unexpected value (" + expected + ")");
   }

}
