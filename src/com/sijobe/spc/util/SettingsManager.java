package com.sijobe.spc.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles settings files from a specified directory, files can be loaded and 
 * saved using a "key". The key is the name of the settings file, minus the 
 * extension (specified by DEFAULT_EXTENSION). 
 *
 * @author simo_415
 * @version 1.0
 */
public class SettingsManager {

   /**
    * The extension that the properties files use
    */
   public static final String DEFAULT_EXTENSION = ".properties";
   /**
    * The cache of loaded settings
    */
   private Map<String,Settings> cache;
   /**
    * The location of where the settings are located
    */
   private File root;
   /**
    * When the settings dont exist prior to load, these are used
    */
   private Settings defaultConfig;

   /**
    * Creates an instance with the specified root directory
    * 
    * @param root - The location where settings files are retrieved
    */
   public SettingsManager(File root) {
      this(root, new Settings());
   }

   /**
    * Creates an instance with the specified root directory and using the 
    * specified configuration file when one doesn't exist prior.
    * 
    * @param root - The location where settings files are retrieved
    * @param defaultConfig - The default configuration to use
    */
   public SettingsManager(File root, Settings defaultConfig) {
      cache = new HashMap<String,Settings>();
      if (root == null || !root.exists() || !root.isDirectory()) {
         throw new RuntimeException("Specified settings location does not exist or is not a directory.");
      }
      this.root = root;
      this.defaultConfig = defaultConfig;
   }

   /**
    * Loads the specified set of settings into memory and returns the settings
    * that were loaded. If the settings didn't exist prior a new instance is
    * created using the specified key and the default configuration.
    * 
    * @param key - The set of settings to load
    * @return The settings that were loaded
    */
   public Settings load(String key) {
      Settings loaded = cache.get(key);
      if (loaded != null) {
         return loaded;
      }
      File location = new File(root, key + DEFAULT_EXTENSION);
      if (location.exists()) {
         loaded = new Settings(location);
      } else {
         loaded = (Settings)defaultConfig.clone();
      }
      cache.put(key, loaded);
      return loaded;
   }

   /**
    * Saves the settings for the specified key back to file. The settings must 
    * already have been loaded by the manager into cache by the load(String) 
    * method. 
    * 
    * @param key - The set of settings to save
    * @return True if the settings were successfully saved, false otherwise
    * @see SettingsManager#load(String)
    */
   public boolean save(String key) {
      Settings save = cache.get(key);
      if (save == null) {
         return false;
      }
      return save.save();
   }
}
