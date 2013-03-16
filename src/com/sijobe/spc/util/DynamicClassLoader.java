package com.sijobe.spc.util;

import java.io.File;
import java.lang.reflect.Field;
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
   
   /**
    * Flag designates whether the populateClassLoaderWithClasses method has 
    * been called yet or not. 
    * 
    * @see DynamicClassLoader#populateClassLoaderWithClasses()
    */
   private static boolean POPULATED = false;
   
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
   public static <T> List<Class<T>> getClasses(Class<T> type) {
      Vector<Class<T>> found = new Vector<Class<T>>();
      try {
         Field field = ClassLoader.class.getDeclaredField("classes");
         field.setAccessible(true);
         Vector<Class<?>> classes = (Vector<Class<?>>) field.get(getClassLoader());
         synchronized (classes) {
            for (int i = 0; i < classes.size(); i++) {
               Class<?> c = classes.get(i);
               if (type.isAssignableFrom(c)) {
                  found.add((Class<T>) c);
               }
            }
            /*
            for (Class<?> c : classes) {
               if (type.isAssignableFrom(c)) {
                  found.add((Class<T>) c);
               }
            }
            */
         }
      } catch (Exception e) {
         e.printStackTrace();
         return null;
      }
      return found;
   }
   
   /**
    * Method designates whether the populateClassLoaderWithClasses method has 
    * been called yet or not. 
    * 
    * @return True if the populateClassLoaderWithClasses method has been called
    * @see DynamicClassLoader#populateClassLoaderWithClasses()
    */
   @Deprecated
   public static boolean hasBeenPopulated() {
      return POPULATED;
   }

   /**
    * This method searches over everything in the classpath and adds all the 
    * classes that are encountered into memory. As this method can search 
    * through thousands of files it is recommended that it is called sparingly.
    * <br><br>
    * Warning: This method may fail to load classes that don't have the correct
    * dependencies setup on the classpath. To add things to the classpath 
    * refer to the addFile(File) or addURL(URL) methods.
    * 
    * @see DynamicClassLoader#addFile(File)
    * @see DynamicClassLoader#addFile(String)
    * @see DynamicClassLoader#addURL(URL)
    */
   @Deprecated
   public static void populateClassLoaderWithClasses() {
      POPULATED = true;
      File files[] = getClasspath();
      for (File f : files) {
         if (f == null || !f.exists()) {
            continue;
         }
         System.out.println("Loading classes from... " + f.getAbsolutePath());

         if (f.getAbsolutePath().endsWith(".jar")) {
            loadClassesFromJAR(f);
         } else {
            if (f.isDirectory()) {
               loadClassesFromDirectory(f);
            } else {
               try {
                  loadClass(f.getName(),null);
               } catch (Exception e) {
               }
            }
         }
      }
   }

   /**
    * Loads all the classes within the specified directory
    * 
    * @param directory - The directory to load all of the classes from
    * @return A Vector containing all of the loaded classes is returned
    */
   public static List<Class<?>> loadClassesFromDirectory(File directory) {
      return loadClassesFromDirectory(directory,"");
   }

   /**
    * Loads all the classes within the specified directory
    * 
    * @param directory - The directory to load all of the classes from
    * @param parent - The path of the parent directory(s). This parent is used
    * as the package name of the classes that are loaded.
    * @return A Vector containing all of the loaded classes is returned
    */
   public static List<Class<?>> loadClassesFromDirectory(File directory, String parent) {
      Vector<Class<?>> classes = new Vector<Class<?>>();
      try {
         File files[] = directory.listFiles();
         for (File file : files) {
            try {
               if (file.isFile()) {
                  classes.add(loadClass(file.getName(),parent));
               } else {
                  classes.addAll(loadClassesFromDirectory(file,parent + file.getName() + "/"));
               }
            } catch (Exception e) {
            }
         }
      } catch (Exception e) {
         return null;
      }
      return classes;
   }

   /**
    * Loads all of the classes that are contained within the specified JAR 
    * file.
    * 
    * @param jar - The location of the JAR file
    * @return A Vector containing all of the classes that are part of this JAR
    * that were loaded
    */
   public static List<Class<?>> loadClassesFromJAR(File jar) {
      Vector<Class<?>> classes = new Vector<Class<?>>();
      try {
         JarFile jf = new JarFile(jar);
         Enumeration<JarEntry> em = jf.entries();

         while (em.hasMoreElements()) {
            JarEntry je = em.nextElement();
            try {
               classes.add(loadClass(je.getName(), null));
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

      URLClassLoader loader = getClassLoader();
      Class<?> c;
      //System.out.print(clazz + " loading... ");
      try {
         c = loader.loadClass(clazz);
      } catch (Throwable e) {
         //e.printStackTrace();
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
