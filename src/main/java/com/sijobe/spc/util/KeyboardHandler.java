package com.sijobe.spc.util;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.input.Keyboard;

/**
 * Handles Keyboard events using keyboard listeners. The class allows the 
 * addKeyPressed and addKeyReleased methods to be called with a passed key 
 * value that gets listened to. If that key gets a key released or pressed 
 * event then the appropriate listeners are called.
 *
 * @author simo_415
 * @version 1.0
 * @see KeyListener
 * @see org.lwjgl.input.Keyboard
 */
public class KeyboardHandler extends Thread {

   /**
    * The delay in milliseconds that the keyboard events are checked
    */
   public static final long DELAY = 10;
   /**
    * The singleton instance of the class
    */
   private static KeyboardHandler KEYBOARD = new KeyboardHandler();
   /**
    * The singleton method that returns the instance
    * 
    * @return The instance of the class to use
    */
   public static KeyboardHandler getInstance() {
      return KEYBOARD;
   }

   /**
    * Holds the value of whether the instance is currently listening or not
    */
   private boolean listening;

   /**
    * A List of keys that are currently registered with this listener
    */
   private List<Integer> keys;
   /**
    * A List of keys that have currently been pressed
    */
   private List<Integer> pressed;

   /**
    * A Map containing the registered pressed event listeners
    */
   private Map<Integer, List<KeyListener>> registeredPressed;
   /**
    * A Map containing the registered released event listeners
    */
   private Map<Integer, List<KeyListener>> registeredReleased;

   /**
    * Default private constructor sets up the instance
    */
   private KeyboardHandler() {
      registeredPressed = new ConcurrentHashMap<Integer, List<KeyListener>>();
      registeredReleased = new ConcurrentHashMap<Integer, List<KeyListener>>();
      pressed = new CopyOnWriteArrayList<Integer>();
      keys = new CopyOnWriteArrayList<Integer>();
      listening = false;
   }

   /**
    * Checks for key press and key release events
    * 
    * @see java.lang.Thread#run()
    */
   @Override
   public void run() {
      try {
         while (listening) {
            if (!Keyboard.isCreated()) {
               listening = false;
               break;
            }
            // Check if pressed have been released
            for (Integer press : pressed) {
               if (!Keyboard.isKeyDown(press)) {
                  keyReleased(press);
               }
            }
            // Check if key is pressed
            for (Integer key : keys) {
               if (Keyboard.isKeyDown(key) && !pressed.contains(key)) {
                  keyPressed(key);
               }
            }
            try {
               Thread.sleep(DELAY);
            } catch (Exception e) {
            }
         }
      } catch (Throwable e) {
         e.printStackTrace();
         listening = false;
      }
   }

   /**
    * Calls all of the necessary listeners based off the key pressed
    * 
    * @param key - The key that was pressed
    */
   private void keyPressed(int key) {
      pressed.add(key);
      if (registeredPressed.get(key) == null) {
         return;
      }
      for (KeyListener listener : registeredPressed.get(key)) {
         listener.keyPressed(key);
      }
   }

   /**
    * Calls all of the necessary listeners based off the key released
    * 
    * @param key - The key that was released
    */
   private void keyReleased(int key) {
      pressed.remove((Object)key);
      if (registeredReleased.get(key) == null) {
         return;
      }
      for (KeyListener listener : registeredReleased.get(key)) {
         listener.keyReleased(key);
      }
   }

   /**
    * Stops the instance from listening
    */
   public void stopListening() {
      listening = false;
   }

   /**
    * Adds a key press listener to the specified key. See the Keyboard class 
    * for details on the key codes.
    * 
    * @param key - The key to add the listener to
    * @param listener - The listener to call on event
    * @return True if the listener was successfully added
    * @see org.lwjgl.input.Keyboard
    */
   public boolean addKeyPressedListener(int key, KeyListener listener) {
      return addListener(key, listener, registeredPressed);
   }

   /**
    * Adds a key release listener to the specified key. See the Keyboard class 
    * for details on the key codes.
    * 
    * @param key - The key to add the listener to
    * @param listener - The listener to call on event
    * @return True if the listener was successfully added
    * @see org.lwjgl.input.Keyboard
    */
   public boolean addKeyReleasedListener(int key, KeyListener listener) {
      return addListener(key, listener, registeredReleased);
   }

   /**
    * Generic implementation that adds the specified key and listener to the 
    * provided internal listener map.
    * 
    * @param key - The key to add
    * @param listener - The listener to call
    * @param internal - The map to add to
    * @return True if the listener was added successfully
    */
   private boolean addListener(int key, KeyListener listener, Map<Integer, List<KeyListener>> internal) {
      if (Keyboard.getKeyName(key) == null) {
         return false;
      }
      List<KeyListener> keylist = internal.get(key);
      if (keylist == null) {
         keylist = new CopyOnWriteArrayList<KeyListener>();
         internal.put(key, keylist);
      }
      keylist.add(listener);
      if (keys.indexOf(key) == -1) {
         keys.add(key);
      }
      if (!listening) {
         listening = true;
         start();
      }
      return true;
   }
   
   /**
    * Removes the specified pressed listener and key from being listened to 
    * by this class.
    * 
    * @param key - The key to remove
    * @param listener - The listener to remove
    */
   public void removeKeyPressedListener(int key, KeyListener listener) {
      removeListener(key, listener, registeredPressed);
   }
   
   /**
    * Removes the specified released listener and key from being listened to 
    * by this class.
    * 
    * @param key - The key to remove
    * @param listener - The listener to remove
    */
   public void removeKeyReleasedListener(int key, KeyListener listener) {
      removeListener(key, listener, registeredReleased);
   }   

   /**
    * Removes the specified key and listener instance from the specified Map 
    * listener. 
    * 
    * @param key - The key to remove based on the listener
    * @param listener - The listener to remove 
    * @param internal - The listener Map to remove the key from
    */
   private void removeListener(int key, KeyListener listener, Map<Integer, List<KeyListener>> internal) {
      // Key hasn't been added, ignore
      if (!keys.contains(key)) {
         return;
      }
      // Remove from List
      List<KeyListener> list = internal.get(key);
      if (list != null) {
         list.remove(listener);
      }
      // Check if the key is referenced anywhere
      checkKeyUsage(key);
   }

   /**
    * Checks the specified key whether it is still used by a listener or not. 
    * If the key is not used it is removed from the instance so that it isn't 
    * listened to anymore. 
    * 
    * @param key - The key to check
    */
   private void checkKeyUsage(int key) {
      List<KeyListener> pressed = registeredPressed.get(key);
      boolean pressedEmpty = false;
      if (pressed != null && pressed.size() == 0) {
         registeredPressed.remove(key);
         pressedEmpty = true;
      }
      List<KeyListener> released = registeredReleased.get(key);
      boolean releasedEmpty = false;
      if (released != null && released.size() == 0) {
         registeredReleased.remove(key);
         releasedEmpty = true;
      }
      if (pressedEmpty && releasedEmpty) {
         keys.remove((Object)key);
         pressed.remove((Object)key);
      }
   }

   /**
    * Gets the ID of the specified key string, this ID can be used to register
    * key listeners. If the specified String is an invalid key type then -1 is
    * returned.
    * 
    * @param key - The key to retrieve the ID for
    * @return The ID of the specified key, or -1 if not found
    */
   public static int getKeyCode(String key) {
      if (key == null) {
         return -1;
      }
      return Keyboard.getKeyIndex(key.toUpperCase()) == Keyboard.KEY_NONE ? -1 : Keyboard.getKeyIndex(key.toUpperCase());
   }
}
