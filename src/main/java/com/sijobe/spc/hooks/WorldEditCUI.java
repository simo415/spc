package com.sijobe.spc.hooks;

import com.sijobe.spc.core.ICUIEventHandler;
import com.sijobe.spc.util.ReflectionHelper;
import com.sijobe.spc.util.WorldEditCUIHelper;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class WorldEditCUI implements ICUIEventHandler {

   private static final boolean hasLiteLoader;
   private static final boolean hasWorldEditCUILL;
   private static final boolean hasModLoader;
   private static final boolean hasWorldEditCUIML;
   private static boolean loadedWorldEditCUI = false;
   private static Object WorldEditCUIMod = null;
   private static boolean handleEvents = true;
   
   private static final Class<?> llClass;
   private static final Class<?> mlClass;
   
   /**
    * @return By default true is returned
    * @see com.sijobe.spc.core.IHook#isEnabled()
    */
   @Override
   public boolean isEnabled() {
      return handleEvents;
   }

   /**
    * @see com.sijobe.spc.core.IHook#init(java.lang.Object[])
    */
   @Override
   public void init(Object... params) {
   }
   
   /**
    * @see com.sijobe.spc.core.ICUIEventHandler#handleCUIEvent(java.lang.String, java.lang.String[])
   */
	@Override
	public void handleCUIEvent(String type, String[] params) {   
		if(!hasWorldEditCUILL && !hasWorldEditCUIML) {
         // neither version of mod was installed
         handleEvents = false;
			return;
		}

      if(!hasModLoader && !hasLiteLoader) {
         // mod was installed but no LiteLoader or ModLoader
         handleEvents = false;
         return;
      }
      
		if(!loadedWorldEditCUI) {
         if(!setWorldEditCUIMod()) {
            System.out.println("SPC/WECUI: WorldEditCUI incorrectly loaded.");
            handleEvents = false;
            return;
         }
		}

		try {
         WorldEditCUIHelper.handleCUIEvent(WorldEditCUIMod, type, params);		
		} catch (Throwable t) {
			System.out.println("SPC/WECUI: WorldEditCUI not initialized.");
         handleEvents = false;
			t.printStackTrace();
		}
	}

   @SuppressWarnings("unchecked")
   private Object getWorldEditCUIMod() {
      if(hasLiteLoader) {
         try {
            Field field = llClass.getDeclaredField("mods");
            field.setAccessible(true);
            Method method = llClass.getMethod("getInstance");
            Object instance = method.invoke(null);
            List<?> liteMods = (List<?>)field.get(instance);
            Class<?> lmClass = Class.forName("com.mumfrey.liteloader.LiteMod");
            Method getName = lmClass.getMethod("getName");
            for(Object liteMod : liteMods) {
               String name = getName.invoke(liteMod).toString();
               if(name.startsWith("WorldEditCUI by yetanotherx")) {
                  loadedWorldEditCUI = true;
                  return liteMod;
               }
            }
         } catch (Throwable t) {
            t.printStackTrace();
         }
      }
      
      if(WorldEditCUIMod == null && hasModLoader) {
         try {
            Method method = mlClass.getMethod("getLoadedMods");
            List<Object> mods = (List<Object>)method.invoke(null);
            for(Object mod : mods) {
            	if(mod.getClass().getName() == "BaseMod")
            	{
            		Method meth = mod.getClass().getMethod("getName");
            		if(meth.invoke(mod).equals("mod_WorldEditCUI"))
            		{
            			loadedWorldEditCUI = true;
           				return mod;
            		}
            	}
            }
         } catch (Throwable t) {
            t.printStackTrace();
         }
      }
      
      loadedWorldEditCUI = false;
      return null;
   }
   
	private boolean setWorldEditCUIMod() {
      WorldEditCUIMod = getWorldEditCUIMod();
      return (WorldEditCUIMod != null);
	}
   
   static {
      mlClass = ReflectionHelper.getClass("net.minecraft.src.ModLoader", "ModLoader");
      if(mlClass != null) {
         hasModLoader = true;
         System.out.println("SPC/WECUI: ModLoader is installed.");
      } else {
         hasModLoader = false;
         System.out.println("SPC/WECUI: ModLoader not installed.");
      }
      
      Class<?> modClass = ReflectionHelper.getClass("net.minecraft.src.mod_WorldEditCUI", "mod_WorldEditCUI");
      if(modClass != null) {
         hasWorldEditCUIML = true;
         System.out.println("SPC/WECUI: WorldEditCUI ML is installed.");
      } else {
         hasWorldEditCUIML = false;
         System.out.println("SPC/WECUI: WorldEditCUI ML not installed.");
      }
      
      llClass = ReflectionHelper.getClass("com.mumfrey.liteloader.core.LiteLoader");
      if(llClass != null) {
         hasLiteLoader = true;
			System.out.println("SPC/WECUI: LiteLoader is installed.");
		} else {
			hasLiteLoader = false;
			System.out.println("SPC/WECUI: LiteLoader not installed.");
		}

      Class<?> liteModClass = ReflectionHelper.getClass("wecui.LiteModWorldEditCUI");
      if(liteModClass != null) {
			hasWorldEditCUILL = true;
			System.out.println("SPC/WECUI: WorldEditCUI LiteMod is installed.");
		} else {
			hasWorldEditCUILL = false;
			System.out.println("SPC/WECUI: WorldEditCUI LiteMod not installed.");
		}
   }
   
}