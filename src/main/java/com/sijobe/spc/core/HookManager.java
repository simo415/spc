package com.sijobe.spc.core;

import com.sijobe.spc.util.DynamicClassLoader;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class provides methods to manage the Hooks that are available. 
 *
 * @see IHook
 * @author simo_415
 * @version 1.0
 */
public class HookManager {

   /**
    * The master list that contains all of the registered hooks
    */
   private List<IHook> master = new ArrayList<IHook>();
   /**
    * The type separated list that has hooks grouped together by type, this is 
    * used by the getHooks method
    * 
    * @see HookManager#getHooks(Class)
    */
   private Map<Class<? extends IHook>,List<IHook>> typemap = new HashMap<Class<? extends IHook>,List<IHook>>();

   public HookManager() {
      master = new ArrayList<IHook>();
      typemap = new HashMap<Class<? extends IHook>,List<IHook>>();
   }

   /**
    * Registers the specified hook class with the manager. The instance is 
    * initialised in this method using the default constructor. If the 
    * master list contains this hook already then the hook isn't added to the 
    * list since it already exists. If you want to overwrite the old instance 
    * of the hook with a new instance use the remove(Hook) method first. 
    * 
    * @param hook - The hook to add to the manager.
    * @see HookManager#remove(IHook)
    */
   public void register(Class<IHook> hook) {
      if (Modifier.isAbstract(hook.getModifiers())) {
         return;
      }
      try {
         register(hook.newInstance());
      } catch (Exception e) {
         System.err.println("Could not load hook " + hook.getName() + ". " +
            "Check that the class has the default constructor visible.");
         e.printStackTrace();
      }
   }

   /**
    * Registers the specified hook instance with the manager. If the 
    * master list contains this hook already then the hook isn't added to the 
    * list since it already exists. If you want to overwrite the old instance 
    * of the hook with a new instance use the remove(Hook) method first. 
    * 
    * @param hook - The instance of the hook to add to the manager. 
    * @see HookManager#remove(IHook)
    */
   public void register(IHook hook) { // TODO: Should add to TYPES
      if (!master.contains(hook)) {
         master.add(hook);
      } else {
         return;
      }
   }

   /**
    * Removes the specified hook instance from the manager. 
    * 
    * @param hook - The hook to remove from the manager.
    */
   public void remove(IHook hook) {
      if (master.contains(hook)) {
         master.remove(hook);
      }
      for (Class<? extends IHook> types : typemap.keySet()) {
         typemap.get(types).remove(hook);
      }
   }

   /**
    * Removes the specified class type from the manager
    * 
    * @param type - The type of hook to remove
    */
   public void remove(Class<IHook> type) {
      for (IHook hook : master) {
         if (type.isAssignableFrom(hook.getClass())) {
            master.remove(hook);
         }
      }
      for (Class<? extends IHook> types : typemap.keySet()) {
         for (IHook hook : typemap.get(types)) {
            if (type.isAssignableFrom(hook.getClass())) {
               typemap.get(types).remove(hook);
            }
         }
         if (typemap.get(types).size() == 0) {
            typemap.remove(typemap.get(types));
         }
      }
   }

   /**
    * Gets hooks that are registered with this manager of the specified class
    * type. This allows for an easier management of hooking in hook implemented
    * classes.
    * <br><br>
    * Example Usage:
    * <pre>{@code
    * for (Hook hook : HookManager.getHooks(Hook.class) {
    *    if (hook.isEnabled()) {
    *       // Do stuff.
    *    }
    * }
    * </pre>
    * 
    * @param <H> - The class type that must extend Hook
    * @param type - The class type
    * @return A List of registered hooks with matching class types
    */
   @SuppressWarnings("unchecked")
   public <H extends IHook> List<H> getHooks(Class<H> type) {
      if (typemap.containsKey(type)) {
         return (List<H>) typemap.get(type);
      }
      List<H> thisType = new ArrayList<H>();
      for (IHook hook : master) {
         if (type.isAssignableFrom(hook.getClass())) {
            thisType.add((H) hook);
         }
      }
      typemap.put(type, (List<IHook>)thisType);
      return thisType;
   }

   /**
    * Method loads all Hooks that are currently available on the classpath. 
    * 
    * @param <H> - The type of hook to load
    * @param type - The type of class to load
    */
   @SuppressWarnings("unchecked")
   public <H extends IHook> void loadHooks(Class<H> type) {
      SPCLoader.load();
      List<Class<H>> hooks = DynamicClassLoader.getClasses(type);
      if (hooks != null) {
         for (Class<H> hook : hooks) {
            register((Class<IHook>)hook);
         }
      }
   }
}
