package com.sijobe.spc.command;

import com.sijobe.spc.wrapper.CommandBase;
import com.sijobe.spc.wrapper.CommandSender;

/**
 * Provides an abstract class for a standard (single) command to extend
 *
 * @author simo_415
 * @version 1.0
 */
public abstract class StandardCommand extends CommandBase {

   /**
    * @see com.sijobe.spc.wrapper.CommandBase#hasPermission(com.sijobe.spc.wrapper.CommandSender)
    */
   @Override
   public boolean hasPermission(CommandSender sender) {
      return true;
   }
}
