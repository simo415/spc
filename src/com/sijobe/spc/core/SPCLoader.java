package com.sijobe.spc.core;

import com.sijobe.spc.util.DynamicClassLoader;
import com.sijobe.spc.wrapper.Minecraft;

import java.io.File;
import java.net.URL;

/**
 * Specific implementation designed to initialise SPC to run any custom JARs, 
 * hooks or commands.
 *
 * @author simo_415
 */
public class SPCLoader {

   /**
    * When true the load method has been called and the necessary classes have 
    * been loaded for the mod
    */
   private static boolean IS_LOADED = false;

   /**
    * Loads SPC required JARs and classes
    */
   public static void load() {
      if (!IS_LOADED) {
         IS_LOADED = true;
         loadClasspath();
         populateClassLoader();
      }
   }

   /**
    * Populates the ClassLoader with all classes that are contained within the
    * same package as the current class. This allows Minecraft.jar to have its 
    * classes dynamically loaded into memory.
    */
   private static void populateClassLoader() {
      String classname = SPCLoader.class.getName();
      int depth = classname.split("\\.").length;
      classname = classname.split("\\.")[depth - 1];
      String location = SPCLoader.class.getResource(classname + ".class").toString();
      if (location.startsWith("jar")) {
         try {
            location = location.replaceAll("jar:", "").split("!")[0];
            File root = (new File((new URL(location)).toURI())); 
            DynamicClassLoader.loadClassesFromJAR(root);
         } catch (Exception e) {
            e.printStackTrace();
         }
      } else {
         try {
            File root = (new File((new URL(location)).toURI()));
            for (int i = 0; i < depth; i++) {
               root = root.getParentFile();
            }
            DynamicClassLoader.loadClassesFromDirectory(root);
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
   }

   /**
    * Loads the JARs that are required on the classpath by searching in the 
    * Minecraft bin directory for any JAR files that haven't already been added
    * to the classpath then adding them.
    */
   private static void loadClasspath() {
      File classpath[] = DynamicClassLoader.getClasspath();
      File files[] = (new File(Minecraft.getMinecraftDirectory(),"bin")).listFiles();
      for (File file : files) {
         try {
            if (file.isFile() && file.getName().endsWith(".jar")) {
               boolean found = false;
               // Loops over classpath
               for (File url : classpath) {
                  // Checks if JAR is already on the classpath
                  if (url != null && url.equals(file)) {
                     found = true;
                     break;
                  }
               }
               if (!found) {
                  DynamicClassLoader.addFile(file);
               }
            }
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
   }
}
