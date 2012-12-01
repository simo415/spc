package com.sijobe.spc.updater;

import java.util.HashMap;
import java.util.Vector;

/**
 * Interface is used in the updater to respond to the update events
 *
 * @author simo_415
 * @version 1.0
 */
public abstract class UpdateCallback {

   /**
    * Called when the update process returns
    * 
    * @param s - The status of the update checks
    */
   public abstract void updateCallback(Vector<HashMap<String,Object>> s); // TODO: FIX FUCKING UPDATE SYSTEM - ITS SHIT.
}
