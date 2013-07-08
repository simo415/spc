package com.sijobe.spc.util;

import com.sijobe.spc.core.HookManager;
import com.sijobe.spc.core.ICUIEventHandler;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Handles WorldEditCUI and CUI event processing
 *
 * @author q3hardcore
 * @version 1.1
 */
public class WorldEditCUIHelper {

   private static final HookManager HOOK_MANAGER = new HookManager();
   private static boolean hooksLoaded = false;
   
   private static final Class<?> wecuiClass;
   private static final Class<?> cuiEventClass;
   private static final Class<?> eventManagerClass;
   private static final Constructor<?> cuiEventConstructor;
   private static final Method callEventMethod;
   private static final Method getEventManagerMethod;
   
   /**
    * Gets the mod's WorldEditCUI instance and handles the event
    * 
    * @param mod - instance of the WorldEditCUI Mod
    * @param type - type of CUI event
    * @param params - parameters of the event
    */
   public static void handleCUIEvent(Object mod, String type, String[] params) throws Throwable {
      Field modController = mod.getClass().getDeclaredField("controller");
      modController.setAccessible(true);
      Object controller = modController.get(mod);
      Object eventManager = getEventManagerMethod.invoke(controller);
      Object event = cuiEventConstructor.newInstance(controller, type, params);
      callEventMethod.invoke(eventManager, event);
   }

   /**
    * Gets a list of CUI event handlers, load hooks if neccessary
    * 
    * @return list of CUI event handler hooks
    */
   public static List<ICUIEventHandler> getCUIHooks() {
      if(!hooksLoaded) {
         HOOK_MANAGER.loadHooks(ICUIEventHandler.class);
         hooksLoaded = true;
      }
      return HOOK_MANAGER.getHooks(ICUIEventHandler.class);
   }
   
   static {
      wecuiClass = ReflectionHelper.getClass("wecui.WorldEditCUI");
      if(wecuiClass != null) {
         getEventManagerMethod = ReflectionHelper.getMethod(wecuiClass, "getEventManager");
         cuiEventClass = ReflectionHelper.getClass("wecui.event.CUIEvent");
         final Class<?>[] constructorArgs = new Class<?>[]{wecuiClass, String.class, String[].class};
         cuiEventConstructor = ReflectionHelper.getConstructor(cuiEventClass, constructorArgs);
         eventManagerClass = ReflectionHelper.getClass("wecui.fevents.EventManager");
         callEventMethod = ReflectionHelper.getPublicMethodWithParamsLength(eventManagerClass, "callEvent", 1);
      } else {
         getEventManagerMethod = null;
         cuiEventClass = null;
         cuiEventConstructor = null;
         eventManagerClass = null;
         callEventMethod = null;
      }
   }
   
}