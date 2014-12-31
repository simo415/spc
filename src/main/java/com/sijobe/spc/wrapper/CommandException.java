package com.sijobe.spc.wrapper;


public class CommandException extends Exception {
   
   /**
    * GUID
    */
   private static final long serialVersionUID = -2082390336460702125L;

   public CommandException() {
      super();
   }
   
   public CommandException(String message) {
      super(message);
   }
   
   public CommandException(Throwable t) {
      super(t);
   }
}
