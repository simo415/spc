package com.sijobe.spc.util;

import java.lang.reflect.Field;

/**
 * Helper class for reflection
 *
 * @author q3hardcore
 * @version 1.3
 */
public class ReflectionHelper {

   /**
    * Gets a boolean value from the specified field
    * 
    * @param field - The field to get the value for
    * @param instance - The instance to retrieve the value from
    * @return The value of the field
    */
   public static boolean getBoolean(Field field, Object instance) {
      if(field == null) {
         System.err.println("Null field");
         return false;
      }
      try {
         return field.getBoolean(instance);
      } catch (Exception e) {
         System.err.println(field.getType() + " not assignable from " + Boolean.TYPE);
         return false;
      }
   }

   /**
    * Gets a double value from the specified field
    * 
    * @param field - The field to get the value for
    * @param instance - The instance to retrieve the value from
    * @return The value of the field
    */
   public static double getDouble(Field field, Object instance) {
      if(field == null) {
         System.err.println("Null field");
         return -1.0D;
      }
      try {
         return field.getDouble(instance);
      } catch (Exception e) {
         System.err.println(field.getType() + " not assignable from " + Double.TYPE);
         return -1.0D;
      }
   }

   /**
    * Gets an integer value from the specified field
    * 
    * @param field - The field to get the value for
    * @param instance - The instance to retrieve the value from
    * @return The value of the field
    */
   public static int getInt(Field field, Object instance) {
      if(field == null) {
         System.err.println("Null field");
         return -1;
      }
      try {
         return field.getInt(instance);
      } catch (Exception e) {
         System.err.println(field.getType() + " not assignable from " + Integer.TYPE);
         return -1;
      }
   }

   
   /**
    * Sets the specified field to the specified value in the instance. 
    * 
    * @param field - The field to set
    * @param instance - The instance of the field to set
    * @param value - The value to set the field
    * @return True if setting the field was successful. 
    */
   public static boolean setField(Field field, Object instance, Object value) {
      if(field == null) {
         System.err.println("Null field");
         return false;
      }
      try {
         field.set(instance, value);
      } catch (Exception e) {
         System.err.println(field.getType() + " not assignable from " + value.getClass());
         return false;
      }
      return true;
   }

   /**
    * Gets a field based off the field names provided by return the first one
    * that matches the field name
    * 
    * @param fieldNames - The field names to attempt to match
    * @param clazz - The class to retrieve the field from
    * @return The Field that matched, or null
    */
   public static Field getField(String[] fieldNames, Class<?> clazz) {
      if(clazz == null) {
         System.err.println("No class specified.");
         return null;
      }
      if(fieldNames == null || fieldNames.length < 1) {
         System.err.println("No field name(s) specified.");
         return null;
      }
      Field field = null;
      for(String fieldName : fieldNames) {
         try {
            field = clazz.getDeclaredField(fieldName);
            break;
         } catch (NoSuchFieldException nsfe) {
            continue;
         }
      }
      if(field == null) {
         System.err.println(clazz.getName() + " does not have field " + fieldNames[0]);
         return null; 
      } else {
         try {
            field.setAccessible(true);
         } catch (SecurityException se) {}
         return field;
      }
   }

   /**
    * Gets the field that matches the field names
    * 
    * @param fieldNames - The field names to match
    * @param instance - The instance (class) of the field
    * @return The field if found, or null
    */
   public static Field getField(String[] fieldNames, Object instance) {
      if(instance != null) {
         return getField(fieldNames, instance.getClass());
      } else {
         return null;
      }
   }

}