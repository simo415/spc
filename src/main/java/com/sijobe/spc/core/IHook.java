package com.sijobe.spc.core;

/**
 * Generic hook interface, use with the HookManager class
 *
 * @see HookManager
 * @author simo_415
 */
public interface IHook {
   /**
    * Returns true if the hook is enabled, false otherwise
    * 
    * @return True if the hook is enabled
    */
   public boolean isEnabled();
   
   /**
    * Allows the instance to be setup using the specified parameters. Calling
    * this method is like creating a new instance of the class.
    * 
    * @param params - The parameters to assign to the instance
    */
   public void init(Object... params);
}
