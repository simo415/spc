package com.sijobe.spc.util;

/**
 * The key listener interface allows callbacks to the implementing classes of 
 * keys that they are listening to.
 *
 * @author simo_415
 * @version 1.0
 * @see KeyboardHandler
 * @see org.lwjgl.input.Keyboard
 */
public interface KeyListener {

   /**
    * Gets called on a registered key press event. See org.lwjgl.input.Keyboard 
    * key fields for more information. 
    * 
    * @param key - The key that was pressed
    * @see KeyboardHandler#addKeyPressedListener
    * @see org.lwjgl.input.Keyboard
    */
   public void keyPressed(int key);
   
   /**
    * Gets called on a registered key release event. See 
    * org.lwjgl.input.Keyboard key fields for more information. 
    * 
    * @param key - The key that was released
    * @see KeyboardHandler#addKeyReleasedListener
    * @see org.lwjgl.input.Keyboard
    */
   public void keyReleased(int key);
}
