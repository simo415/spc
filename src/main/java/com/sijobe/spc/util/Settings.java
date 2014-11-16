package com.sijobe.spc.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Handles settings by extending the Properties class and providing useful 
 * methods to retrieve data that is already cast into the correct format.
 *
 * @author simo_415
 * @version 1.1
 */
public class Settings extends Properties {

   /**
    * serialVersionUID used by the serialised interface 
    */
   private static final long serialVersionUID = -7463327430611005961L;
   /**
    * The file that the settings are contained within
    */
   private File settings;

   /**
    * Initialises a default instance of the class with no disk location 
    * specified to load/save to settings from.
    */
   public Settings() {
      super();
   }

   /**
    * Loads the specified file into the instance
    * 
    * @param f - The file to load
    */
   public Settings(File f) {
      this(f, true);
   }

   /**
    * Associates the file with the instance allowing the file to be loaded or 
    * not using the load parameter.
    * 
    * @param f - The file to load
    * @param load - True to load the file, false otherwise
    */
   public Settings(File f, boolean load) {
      super();
      this.settings = f;
      if (load) {
         load(f);
      }
   }

   /**
    * Sets the specified setting to the boolean value
    * 
    * @param key - The key to set
    * @param value - The value to assign
    */
   public void set(String key, boolean value) {
      setProperty(key, new Boolean(value).toString());
   }

   /**
    * Gets the specified setting as a boolean value, if the setting doesn't 
    * exist then the base parameter value is used
    * 
    * @param key - The setting to retrieve 
    * @param base - The default setting to use if the key doesn't exist
    * @return The value of the setting
    */
   public boolean getBoolean(String key, boolean base) {
      String value = getProperty(key);
      try {
         return (value == null || value.trim().equalsIgnoreCase("")) ? base : new Boolean(value);
      } catch (Exception e) {
         return base;
      }
   }

   /**
    * Sets the specified setting to the integer value
    * 
    * @param key - The key to set
    * @param value - The value to assign
    */
   public void set(String key, int value) {
      setProperty(key,new Integer(value).toString());
   }

   /**
    * Gets the specified setting as a integer value, if the setting doesn't 
    * exist then the base parameter value is used
    * 
    * @param key - The setting to retrieve 
    * @param base - The default setting to use if the key doesn't exist
    * @return The value of the setting
    */
   public int getInteger(String key, int base) {
      String value = getProperty(key);
      try {
         return isEmpty(value) ? base : new Integer(value);
      } catch (NumberFormatException e) {
         return base;
      }
   }

   /**
    * Sets the specified setting to the char value
    * 
    * @param key - The key to set
    * @param value - The value to assign
    */
   public void set(String key, char value) {
      setProperty(key,new Character(value).toString());
   }

   /**
    * Gets the specified setting as a char value, if the setting doesn't 
    * exist then the base parameter value is used
    * 
    * @param key - The setting to retrieve 
    * @param base - The default setting to use if the key doesn't exist
    * @return The value of the setting
    */
   public char getCharacter(String key, char base) {
      String value = getProperty(key);
      try {
         return isEmpty(value) ? base : value.charAt(0);
      } catch (NumberFormatException e) {
         return base;
      }
   }

   /**
    * Sets the specified setting to the double value
    * 
    * @param key - The key to set
    * @param value - The value to assign
    */
   public void set(String key, double value) {
      setProperty(key,new Double(value).toString());
   }

   /**
    * Gets the specified setting as a double value, if the setting doesn't 
    * exist then the base parameter value is used
    * 
    * @param key - The setting to retrieve 
    * @param base - The default setting to use if the key doesn't exist
    * @return The value of the setting
    */
   public double getDouble(String key, double base) {
      String value = getProperty(key);
      try {
         return isEmpty(value) ? base : new Double(value);
      } catch (NumberFormatException e) {
         return base;
      }
   }

   /**
    * Sets the specified setting to the float value
    * 
    * @param key - The key to set
    * @param value - The value to assign
    */
   public void set(String key, float value) {
      setProperty(key,new Float(value).toString());
   }

   /**
    * Gets the specified setting as a float value, if the setting doesn't 
    * exist then the base parameter value is used
    * 
    * @param key - The setting to retrieve 
    * @param base - The default setting to use if the key doesn't exist
    * @return The value of the setting
    */
   public float getFloat(String key, float base) {
      String value = getProperty(key);
      try {
         return isEmpty(value) ? base : new Float(value);
      } catch (NumberFormatException e) {
         return base;
      }
   }

   /**
    * Sets the specified setting to the String value
    * 
    * @param key - The key to set
    * @param value - The value to assign
    */
   public void set(String key, String value) {
      setProperty(key,value);
   }

   /**
    * Gets the specified setting as a String value, if the setting doesn't 
    * exist then the base parameter value is used
    * 
    * @param key - The setting to retrieve 
    * @param base - The default setting to use if the key doesn't exist
    * @return The value of the setting
    */
   public String getString(String key, String base) {
      String value = getProperty(key);
      return isEmpty(value) ? base : value;
   }

   /**
    * Saves the settings instance back to file
    * 
    * @return True if the settings were able to be saved
    */
   public boolean save() {
      return this.save("");
   }

   /**
    * Saves the settings instance back to file with the specified header
    * 
    * @param header - The header to set to the properties
    * @return True if the settings were able to be saved
    */
   public boolean save(String header) {
      return this.save(settings,header);
   }

   /**
    * Saves the settings instance back to the specified file, not the one 
    * associated with the instance. The header is also set in the file 
    * 
    * @param file - The file to save the settings to
    * @param header - The header to set to the properties
    * @return True if the settings were able to be saved
    */
   public boolean save(File file, String header) {
      if (file == null || file.isDirectory()) {
         return false;
      }
      FileOutputStream fos = null;
      try {
         if (!file.exists()) {
            file.createNewFile();
         }
         fos = new FileOutputStream(file);
         super.store(fos, header);
         return true;
      } catch (Exception e) {
         return false;
      } finally {
         if (fos != null) {
            try {
               fos.close();
            } catch (IOException e) {}
         }
      }
   }

   /**
    * Loads the settings from disk.
    * 
    * @return True if the settings were able to be loaded
    */
   public boolean load() {
      return this.load(settings);
   }

   /**
    * Loads the settings from disk from the specified file.
    * 
    * @param file - The file to load the settings from
    * @return True if the settings were able to be loaded
    */
   public boolean load(File file) {
      if (file == null || file.isDirectory()) {
         return false;
      }
      try {
         if (!file.exists()) {
            file.createNewFile();
            return true;
         }
         super.load(new FileInputStream(file));
         return true;
      } catch (Exception e) {
         return false;
      }
   }

   /**
    * Gets the file that the settings are saving/loading from
    * 
    * @return The file that the instance is using
    */
   public File getFile() {
      return settings;
   }

   /**
    * Sets the file that the settings are saving/loading from 
    * 
    * @param settings - The path to the file to load/save from
    */
   public void setFile(File settings) {
      this.settings = settings;
   }

   /**
    * Checks if the specified value is empty
    * 
    * @param value - The value to check
    * @return True if the String is empty
    */
   private boolean isEmpty(String value) {
      return (value == null || value.trim().equalsIgnoreCase(""));
   }

   /**
    * @see java.util.Hashtable#clone()
    */
   @Override
   public Object clone() {
      return new Settings(this.settings);
   }
}
