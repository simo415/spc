package com.sijobe.spc.core;

import com.sijobe.spc.wrapper.Player;

/**
 * Provides the hook class that EntityPlayerMP classes use
 *
 * @author simo_415
 * @version 1.0
 */
public interface IPlayerMP extends IHook {
   
   /**
    * Called when the onUpdate() method is called in Minecraft
    * 
    * @param player - the player that the event occurred on
    */
   public void onTick(Player player);
}
