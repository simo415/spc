package com.sijobe.spc.command;

import com.sijobe.spc.util.FontColour;
import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandSender;

import java.util.List;


public class Test extends MultipleCommands {

   public Test(String name) {
      super(name);
   }

   @Override
   public void execute(CommandSender sender, List<?> params) throws CommandException {
      for (int i = 0; i < 16; i++) {
         sender.sendMessageToPlayer(FontColour.RANDOM + "Coloured!");
      }
      
      if (getName().equalsIgnoreCase("test")) {
         // DO stuff for test
      }
      System.out.println("This instance is for: " + getName());
   }

   @Override
   public String[] getCommands() {
      return new String[] {"test","test2","test3"};
   }
   
   @Override
   public boolean isEnabled() {
      return false;
   }
}
