package com.sijobe.spc.core;

/**
 * Hook class for WorldEdit CUI event handling
 *
 * @author q3hardcore
 */
public interface ICUIEventHandler extends IHook {

   /**
    * Handles a CUI Event
    * 
    * @param type - the type of CUI Event
    * @param type - the parameters of the CUI Event
    */
   public void handleCUIEvent(String type, String[] params);
}
