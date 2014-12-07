package com.sijobe.spc.util;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Class that is used to help dynamically configure the system classloader
 * at runtime. This allows classes and jars (and all other files) to be loaded
 * during runtime to provide greater flexibility.
 * 
 * This class is quite a hack in getting things added to the system
 * classpath and may not work where the environment is secured by a security
 * manager. 
 *
 * @author simo_415
 * @version 1.1
 */
public class DynamicClassLoader {
   
   /**
    * The class loader that this class uses
    */
   private static URLClassLoader CLASSLOADER;
   
   /*
    * A vector used to store all SPC classes
   */
   private static Vector<Class<?>> spcClasses = new Vector<Class<?>>();
   
   /**
    * Gets the class loader used by this class (and generally all base classes)
    * 
    * @return The URL class loader used
    */
   public static URLClassLoader getClassLoader() {
      if (CLASSLOADER == null) {
         CLASSLOADER = (URLClassLoader)DynamicClassLoader.class.getClassLoader();
      }
      return CLASSLOADER;
   }

   /**
    * Attempts to add the specified file to the system classpath.
    * 
    * @param file - The path to the file to add to the system classpath
    * @return True is returned when the file was successfully added to the 
    * system classpath, false otherwise. 
    */
   public static boolean addFile(String file) {
      return addFile(new File(file));
   }

   /**
    * Attempts to add the specified file to the system classpath.
    * 
    * @param file - The file to add to the system classpath
    * @return True is returned when the file was successfully added to the 
    * system classpath, false otherwise. 
    */
   public static boolean addFile(File file) {
      try {
         return addURL(file.toURI().toURL());
      } catch (MalformedURLException e) {
         return false;
      }
   }

   /**
    * Attempts to add the specified URL to the system classpath.
    * 
    * @param file - The URL to add to the system classpath
    * @return True is returned when the URL was successfully added to the 
    * system classpath, false otherwise. 
    */
   public static boolean addURL(URL file) {
      URLClassLoader sysloader = getClassLoader();
      Class<URLClassLoader> sysclass = URLClassLoader.class;
      try {
         Method method = sysclass.getDeclaredMethod("addURL", new Class[] { URL.class });
         method.setAccessible(true);
         method.invoke(sysloader, new Object[] { file });
      } catch (Exception e) {
         return false;
      }
      return true;
   }

   /**
    * Returns a Vector containing all of the loaded classes that are castable 
    * to the specified Class type. This would mean that the class itself and
    * any classes that extend the specified class that are currently on the 
    * classpath are returned by the method.
    * 
    * @param <T> - The class type
    * @param type - The type of class to search for
    * @return A Vector that contains all the matching classes. If an exception
    * occurs attempting to retrieve the loaded classes, null will be returned.
    */
   @SuppressWarnings("unchecked")
   public static synchronized <T> List<Class<T>> getClasses(Class<T> type) {
      Vector<Class<T>> found = new Vector<Class<T>>();
      try {
         Vector<Class<?>> classes = getSPCClasses();
         for(Class<?> c : classes) {
            if(c == null) {
               //System.out.println("Skipping null class.");
               continue;
            }
            if(type.isAssignableFrom(c)) {
               found.add((Class<T>) c);
            }
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
      return found;
   }
   /**
    * 
    * 
    * @return A Vector that contains all classes in SPC's package
   */
   private static Vector<Class<?>> getSPCClasses() throws Exception {
      if(spcClasses.size() == 0) {
         String className = DynamicClassLoader.class.getName().replace(".", "/");
         URL location = DynamicClassLoader.class.getResource("/" + className + ".class");
         if(location == null) {
            throw new RuntimeException("SPC: Couldn't find SPC classes!");
         }
         final List<Class<?>> tempSPCClasses;
         if(location.toString().toLowerCase().startsWith("jar")) {
            String jarLocation = location.toString().replaceAll("jar:", "").split("!")[0];
            File root = new File((new URL(jarLocation)).toURI());
            System.out.println("SPC: " + root);
            tempSPCClasses = loadSPCClassesFromJAR(root);
         } else {
            File dynamicClassLoader = new File(location.getFile().replace("%20", " ")); //added replace for paths with spaces
            File parentDir = getSPCParent(dynamicClassLoader);
            if(parentDir == null) {
               System.out.println("SPC: Not loading.");
               tempSPCClasses = new Vector<Class<?>>();
               tempSPCClasses.add(DynamicClassLoader.class);
            } else {
               System.out.println("SPC: " + parentDir);
               tempSPCClasses = loadSPCClassesFromDirectory(parentDir, "com/sijobe/spc/");
            }
         }
         spcClasses.addAll(tempSPCClasses);
         return spcClasses;
      }
      else {
         return spcClasses;
      }
   }
   
   /**
    * Get the parent SPC folder
    * 
    * @parameter file - The base file
    * @return The parent directory
   */
   private static File getSPCParent(File file) {
      File parent = file;
      while((parent = parent.getParentFile()) != null) {
         if(parent.getPath().endsWith("spc") && parent.isDirectory()) {
            return parent;
         }
      }
      return null; // Hopefully, this is impossible
   }
   
   /**
    * Loads all of SPC's classes within the specified directory
    * 
    * @param directory - The directory to load all of the classes from
    * @param parent - The path of the parent directory(s). This parent is used
    * as the package name of the classes that are loaded.
    * @return A Vector containing all of the loaded classes is returned
    */
   public static List<Class<?>> loadSPCClassesFromDirectory(File directory, String parent) {
      if(directory.toString().indexOf("sijobe") == -1) {
         System.out.println("Nope: " + directory);
         return new Vector<Class<?>>();
      }
      Vector<Class<?>> classes = new Vector<Class<?>>();
      try {
         File files[] = directory.listFiles();
         for (File file : files) {
            try {
               if (file.isFile() && file.getPath().endsWith(".class")) { //TODO add class file type check to loadSPCClassesFromJar
                  classes.add(loadClass(file.getName(),parent));
               } else if(file.isDirectory()){
                  classes.addAll(loadSPCClassesFromDirectory(file,parent + file.getName() + "/"));
               }
            } catch (Exception e) {
               e.printStackTrace();
            }
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
      return classes;
   }

   /**
    * Loads all of SPC's classes that are contained within the specified JAR file.
    * 
    * @param jar - The location of the JAR file
    * @return A Vector containing all of the classes that are part of this JAR
    * that were loaded
    */
   public static List<Class<?>> loadSPCClassesFromJAR(File jar) {
      Vector<Class<?>> classes = new Vector<Class<?>>();
      try {
         JarFile jf = new JarFile(jar);
         Enumeration<JarEntry> em = jf.entries();

         while (em.hasMoreElements()) {
            JarEntry je = em.nextElement();
            try {
               String entry = je.getName();
               if(!entry.startsWith("com/sijobe/spc/")) {
                  continue;
               }
               classes.add(loadClass(entry, null));
            } catch (Throwable t) {
            }
         }
      } catch (Exception e) {
         e.printStackTrace();
         return null;
      }
      return classes;
   }

   /**
    * Loads a single class onto the system classpath. As this method is 
    * intended for use by classes dynamically loading content from the file 
    * system the clazz parameter should be the filename on the system - ie: 
    * CLASSNAME.class - the .class is the important part. The pack parameter is
    * the package that the class belongs to.
    * 
    * @param clazz - The name of the class with a .class extension
    * @param pack - The name of the package
    * @return The Class object that was loaded based on the parameters
    * @throws Exception When the specified class is not a .class
    */
   public static Class<?> loadClass(String clazz, String pack) throws Exception {
      if (!clazz.endsWith(".class")) {
         throw new Exception("'" + clazz + "' is not a class.");
      }
      clazz = clazz.split("\\.")[0];
      if (pack != null) {
         pack = pack.endsWith("/") ? pack.substring(0, pack.length() - 1) : pack;
      }
      clazz = pack == null ? clazz : pack + "." + clazz;
      clazz = clazz.replaceAll("/", ".");
      if(clazz.startsWith(".")) {
         clazz = clazz.substring(1);
      }
      
      URLClassLoader loader = getClassLoader();
      Class<?> c;
      //System.out.print(clazz + " loading... ");
      try {
         c = loader.loadClass(clazz);
      } catch (Throwable t) {
         String message = t.getMessage();
         if(message != null && message.startsWith("com/sk89q/worldedit/")) {
            //System.out.println("Couldn't find WorldEdit class: " + message + ".class");
            //System.out.println("Skipping SPC class: " + clazz + ".class");
         } else {
            t.printStackTrace();
         }
         return null;
      }
      //System.out.println("loaded.");      
      return c;
   }
   
   /**
    * Gets all of the currently loaded items on the classpath
    * 
    * @return An array of files that are specified on the classpath
    */
   public static File[] getClasspath() {
      URLClassLoader urlLoader = DynamicClassLoader.getClassLoader();
      URL urls[] = urlLoader.getURLs();
      File classpath[] = new File[urls.length];
      for (int i = 0; i < urls.length; i++) {
         try {
            classpath[i] = new File(urls[i].toURI());
         } catch (Exception e) {
         }
      }
      return classpath;
   }
}
