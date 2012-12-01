package com.sijobe.spc.validation;

import java.util.ArrayList;
import java.util.List;


public class ParameterBoolean extends Parameter {
   private List<String> valid;
   private List<String> invalid;
   
   public ParameterBoolean(String label, boolean optional) {
      this(label,optional,"true","false");
   }
   
   public ParameterBoolean(String label, boolean optional, String valid, String invalid) {
      super(label, optional);
      this.valid = new ArrayList<String>();
      this.valid.add(valid.toLowerCase());
      this.invalid = new ArrayList<String>();
      this.invalid.add(invalid.toLowerCase());
   }
   
   public ParameterBoolean(String label, boolean optional, List<String> valid, List<String> invalid) {
      super(label, optional);
      if (valid == null) {
         valid = new ArrayList<String>();
      }
      for (int i = 0; i < valid.size(); i++) {
         valid.set(i, valid.get(i).toLowerCase());
      }
      this.valid = valid;
      if (invalid == null) {
         invalid = new ArrayList<String>();
      }
      for (int i = 0; i < invalid.size(); i++) {
         invalid.set(i, invalid.get(i).toLowerCase());
      }
      this.invalid = invalid;
   }

   /**
    * @see com.sijobe.spc.validation.Parameter#validate(java.lang.String)
    */
   @Override
   public Boolean validate(String parameter) throws ValidationException {
      if (valid.contains(parameter.toLowerCase())) {
         return true;
      }
      if (invalid.contains(parameter.toLowerCase())) {
         return false;
      }
      throw new ValidationException("Invalid text (" + parameter + 
               ") where boolean value was expected");
   }
}
