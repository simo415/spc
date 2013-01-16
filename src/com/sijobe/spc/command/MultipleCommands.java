package com.sijobe.spc.command;

import com.sijobe.spc.validation.Parameters;
import com.sijobe.spc.wrapper.CommandBase;
import com.sijobe.spc.wrapper.CommandSender;

/**
 * Default class to extend to use Multiple commands per class rather than the 
 * standard single class. The way it works is by creating a separate instance 
 * of the class for every command that is in the class. The commands that are 
 * in the class are specified by the getCommands() method and the current 
 * instances command name is accessed using the getName() method. 
 *
 * TODO: Made annotation compatible
 * TODO: Need to contact mojang about passing the command name through
 *
 * @see MultipleCommands#getCommands()
 * @see MultipleCommands#getName()
 * @author simo_415
 * @version 1.0
 */
public abstract class MultipleCommands extends CommandBase {

   /**
    * The name of the command 
    */
   private final String name;
   
   /**
    * Default constructor initialises the instance using the specified command 
    * name. 
    * 
    * @param name - The command name
    */
   public MultipleCommands(String name) { 
      this.name = name;
   }
   
   /**
    * @see com.sijobe.spc.wrapper.CommandBase#getName()
    */
   @Override
   public final String getName() {
      return name;
   }
   
   /**
    * @see com.sijobe.spc.wrapper.CommandBase#getParameters()
    */
   @Override
   public Parameters getParameters() {
      return Parameters.DEFAULT;
   }
   
   /**
    * @see com.sijobe.spc.wrapper.CommandBase#hasPermission(com.sijobe.spc.wrapper.CommandSender)
    */
   @Override
   public boolean hasPermission(CommandSender sender) {
      return true;
   }
   
   /**
    * Gets an array containing all of the commands that this class contains. 
    * This array is used to initialise instances of the class.
    * 
    * @return An array of command names
    */
   public abstract String[] getCommands();
}
