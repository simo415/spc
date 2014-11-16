package com.sijobe.spc.validation;


public class ValidationException extends Exception {

   private static final long serialVersionUID = 4644287908267502300L;
   
   public ValidationException() {
      super();
   }
   
   public ValidationException(String message) {
      super(message);
   }
   
   public ValidationException(Throwable t) {
      super(t.getMessage());
   }
}
