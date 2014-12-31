package com.sijobe.spc.validation;

public abstract class Parameter {

   private String label;
   private boolean optional;
   private boolean variableLength;
   
   public Parameter(String label, boolean optional) {
      this.label = label;
      this.optional = optional;
   }
   
   public Parameter(String label, boolean optional, boolean variableLength) {
      this.label = label;
      this.optional = optional;
      this.variableLength = variableLength;
   }
   
   /**
    * Returns the parameter label. The label is used to display to the user
    * 
    * @return The parameter label
    */
   public String getLabel() {
      return label;
   }
   
   /**
    * Returns whether the parameter is optional
    * 
    * @return Returns the optional value
    */
   public boolean isOptional() {
      return optional;
   }
   
   /**
    * Returns whether the parameter is variable length or not. This parameter 
    * is used while doing parameter validation but is ignored unless it is 
    * the last specified parameter
    * 
    * @return Returns the variable length flag
    */
   public boolean isVariableLength() {
      return variableLength;
   }
   
   /**
    * Validates the specified user input whether it is valid input according to
    * the implementing class. For example ParameterInteger will validate that
    * the provided input is a valid integer between the specified values
    * 
    * @param parameter - The parameter provided by the user to verify
    * @return The validated value is returned when the specified parameter is 
    * valid. The returned value is of the type it was validated as
    */
   public abstract Object validate(String parameter) throws ValidationException;
}
