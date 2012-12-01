package com.sijobe.spc.core;

import com.sijobe.spc.wrapper.Player;

/**
 * Provides a default set of values that are pretty standard for implementing 
 * the IPlayerSP interface. The class is abstract so that it isn't used by the
 * HookManager and can be easily extended by child classes where they can 
 * define the methods that they need.
 *
 * @author simo_415
 * @version 1.1
 */
public class PlayerSP implements IPlayerSP {

   /**
    * @return By default true is returned
    * @see com.sijobe.spc.core.IHook#isEnabled()
    */
   @Override
   public boolean isEnabled() {
      return true;
   }

   /**
    * @see com.sijobe.spc.core.IHook#init(java.lang.Object[])
    */
   @Override
   public void init(Object... params) {
   }

   /**
    * @see com.sijobe.spc.core.IPlayerSP#onTick(com.sijobe.spc.wrapper.Player)
    */
   @Override
   public void onTick(Player player) {
   }

   /**
    * @see com.sijobe.spc.core.IPlayerSP#movePlayer(float, float, float)
    */
   @Override
   public void movePlayer(Player player, float forward, float strafe, float speed) {
   }
}
