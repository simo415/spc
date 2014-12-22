package com.sijobe.spc.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;

/**
 * Helper class for reflection
 * 
 * @author q3hardcore
 * @version 1.5
 */
public class ReflectionHelper {
   /**
    * a bunch of stuff that is accessed through reflection
    */
   public static final Field walkSpeed = getField(PlayerCapabilities.class, "walkSpeed", "g", "field_75097_g");
   public static final Field flySpeed = getField(PlayerCapabilities.class, "flySpeed", "f", "field_75096_f");
   public static final Field commandsAllowedForAll = getField(ServerConfigurationManager.class, "commandsAllowedForAll", "o", "field_72407_n");
   public static final Method getDataDirectory = getMethod(MinecraftServer.class, new String[] {
         "getDataDirectory", "r", "func_71238_n" });
   
   /**
    * The only blacklisted class
    */
   private static final String blacklistedClass = "java.lang.Object";
   
   /**
    * Gets the highest non-blacklisted superclass for a given class
    * 
    * @param clazz - The class to find the 'base class' of
    * @return The base class
    */
   public static Class<?> getBaseClass(Class<?> clazz) {
      if (clazz == null) {
         System.err.println("No class specified.");
         return null;
      }
      Class<?> baseClass;
      while ((baseClass = clazz.getSuperclass()) != null
            && !baseClass.getName().equals(blacklistedClass)) {
         clazz = baseClass;
      }
      return clazz;
   }
   
   /**
    * Finds the first method with that matches the specified name and parameters
    * length
    * 
    * @param clazz - The class to search
    * @param name - The method name
    * @param length - The parameter length
    * @return The first matching method, or null
    */
   public static Method getPublicMethodWithParamsLength(Class<?> clazz, String name, int length) {
      if (clazz == null) {
         System.err.println("No class specified.");
         return null;
      }
      try {
         Method[] methods = clazz.getMethods();
         for (Method method : methods) {
            boolean correctName = method.getName().equals(name);
            if (correctName && method.getParameterTypes().length == length) {
               return method;
            }
         }
      } catch (Throwable t) {
         t.printStackTrace();
      }
      System.err.println(clazz.getName() + " does not have method " + name);
      return null;
   }
   
   /**
    * Finds the first class that matches one of the names specified
    * 
    * @param classes - A list of all class names
    * @return The first class found, or null
    */
   public static Class<?> getClass(String... classes) {
      if (classes.length == 0) {
         System.err.println("No classes specified.");
         return null;
      }
      for (String name : classes) {
         try {
            return Class.forName(name);
         } catch (Throwable t) {
         }
      }
      
      return null;
   }
   
   /**
    * Gets a boolean value from the specified field
    * 
    * @param field - The field to get the value for
    * @param instance - The instance to retrieve the value from
    * @return The value of the field
    */
   public static boolean getBoolean(Field field, Object instance) {
      if (field == null) {
         System.err.println("Null field");
         return false;
      }
      try {
         return field.getBoolean(instance);
      } catch (Exception e) {
         System.err.println(field.getType() + " not assignable from "
               + Boolean.TYPE);
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
      if (field == null) {
         System.err.println("Null field");
         return -1.0D;
      }
      try {
         return field.getDouble(instance);
      } catch (Exception e) {
         System.err.println(field.getType() + " not assignable from "
               + Double.TYPE);
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
      if (field == null) {
         System.err.println("Null field");
         return -1;
      }
      try {
         return field.getInt(instance);
      } catch (Exception e) {
         System.err.println(field.getType() + " not assignable from "
               + Integer.TYPE);
         return -1;
      }
   }
   
   /**
    * Gets an object value from the specified field
    * 
    * @param field - The field to get the value for
    * @param instance - The instance to retrieve the value from
    * @return The value of the field
    */
   public static Object getObj(Field field, Object instance) {
      if (field == null) {
         System.err.println("Null field");
         return null;
      }
      try {
         return field.get(instance);
      } catch (Exception e) {
         System.err.println(field.getType() + " not assignable from "
               + Integer.TYPE);
         return null;
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
      if (field == null) {
         System.err.println("Null field");
         return false;
      }
      try {
         field.set(instance, value);
      } catch (Exception e) {
         System.err.println(field.getType() + " not assignable from "
               + value.getClass());
         return false;
      }
      return true;
   }
   
   /**
    * Attempts to get a constructor from the specified class
    * 
    * @param class - The class to retrieve constructor from
    * @param params - The parameters for the constructor
    * @return The constructor if found, otherwise null
    */
   public static Constructor<?> getConstructor(Class<?> clazz, Class<?>... params) {
      if (clazz == null) {
         System.err.println("No class specified.");
         return null;
      }
      Constructor<?> constructor = null;
      try {
         constructor = clazz.getDeclaredConstructor(params);
      } catch (NoSuchMethodException nsme) {
      }
      if (constructor == null) {
         System.err.println(clazz.getName()
               + "does not have specified constructor");
         return null;
      } else {
         try {
            constructor.setAccessible(true);
         } catch (SecurityException se) {
         }
         return constructor;
      }
   }
   
   /**
    * Attempts to get a method from the specified class
    * 
    * @param clazz - The class to retrieve the method from
    * @param methodName - Name for the method
    * @param params - A list of the methods parameters
    * @return The Method that matched, or null
    */
   public static Method getMethod(Class<?> clazz, String methodName, Class<?>... params) {
      return getMethod(clazz, new String[] { methodName }, params);
   }
   
   /**
    * Attempts to get a method from the specified class
    * 
    * @param clazz - The class to retrieve the method from
    * @param methodNames - Names for the method
    * @param params - A list of the methods parameters
    * @return The Method that matched, or null
    */
   public static Method getMethod(Class<?> clazz, String[] methodNames, Class<?>... params) {
      if (clazz == null) {
         System.err.println("No class specified.");
         return null;
      }
      if (methodNames == null || methodNames.length < 1) {
         System.err.println("No methodNames specified.");
         return null;
      }
      Method method = null;
      for (String methodName : methodNames) {
         try {
            method = clazz.getDeclaredMethod(methodName, params);
            break;
         } catch (NoSuchMethodException nsfe) {
            continue;
         } catch (NoClassDefFoundError ncdfe) {
            continue;
         }
      }
      if (method == null) {
         System.err.println(clazz.getName() + " does not have method "
               + methodNames[0]);
         return null;
      } else {
         try {
            method.setAccessible(true);
         } catch (SecurityException se) {
         }
         return method;
      }
   }
   
   /**
    * Gets a field based off the field names provided by return the first one
    * that matches the field name
    * 
    * @param fieldNames - The field names to attempt to match
    * @param clazz - The class to retrieve the field from
    * @return The Field that matched, or null
    */
   public static Field getField(Class<?> clazz, String... fieldNames) {
      if (clazz == null) {
         System.err.println("No class specified.");
         return null;
      }
      if (fieldNames == null || fieldNames.length < 1) {
         System.err.println("No field name(s) specified.");
         return null;
      }
      Field field = null;
      for (String fieldName : fieldNames) {
         try {
            field = clazz.getDeclaredField(fieldName);
            break;
         } catch (NoSuchFieldException nsfe) {
            continue;
         }
      }
      if (field == null) {
         System.err.println(clazz.getName() + " does not have field "
               + fieldNames[0]);
         return null;
      } else {
         try {
            field.setAccessible(true);
         } catch (SecurityException se) {
         }
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
   public static Field getField(Object instance, String... fieldNames) {
      if (instance != null) {
         return getField(instance.getClass(), fieldNames);
      } else {
         return null;
      }
   }
   
}