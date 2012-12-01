package com.sijobe.spc.core;

import com.sijobe.spc.wrapper.Player;

/**
 * Provides the hook class that EntityPlayerSP classes use
 *
 * @author simo_415
 * @version 1.1
 */
public interface IPlayerSP extends IHook {
   
   /**
    * Called when the onUpdate() method is called in Minecraft
    * 
    * @param player - the player that the event occurred on
    */
   public void onTick(Player player);
   
   /**
    * Called when the movePlayer() method is called in Minecraft
    * 
    * @param player - the player that the event occurred on
    * @param forward - The forward movement
    * @param strafe - The strafe movement
    * @param speed - The speed modifier
    */
   public void movePlayer(Player player, float forward, float strafe, float speed);
}
