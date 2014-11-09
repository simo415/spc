package com.sijobe.spc.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.world.WorldProvider;

/**
 * Helper for compatiblity with Minecraft Forge
 *
 * @author q3hardcore
 * @version 1.0
 */
public final class ForgeHelper {

   public static final boolean HAS_FORGE = getHasForge();

   private final static Class<?> forgeClass;
   private final static Field eventBusField;
   private final static Class<?> eventClass;
   private final static Class<?> eventBusClass;
   private final static Method post;
   private final static Field capturedDropsField;
   private final static Field captureDropsField;
   private final static Class<?> playerDropsEventClass;
   private final static Constructor<?> playerDropsEventConstructor;
   private final static Class<?> watchClass;
   private final static Constructor<?> watchConstructor;
   private final static Method getBlockReachDistance;
   private final static Method setBlockReachDistance;
   private final static Class<?> forgeHooksClass;
   private final static Method onLivingDeath;
   
   private static boolean getHasForge() {
      try {
         Class.forName("net.minecraftforge.common.MinecraftForge");
         return true;
      } catch (Throwable t) {
         return false;
      }
   }
   
   public static ChunkCoordinates getRandomizedSpawnPoint(WorldProvider provider) {
      ChunkCoordinates spawnPoint = new ChunkCoordinates(0, 64, 0);
      try {
			spawnPoint = (ChunkCoordinates)provider.getClass().getMethod("getRandomizedSpawnPoint").invoke(provider);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return spawnPoint;
	}
   
	public static void clearDrops(EntityPlayerMP entityPlayerMP) {
		setCaptureDrops(entityPlayerMP, true);
		getCapturedDrops(entityPlayerMP).clear();
	}
   
   @SuppressWarnings("unchecked")
	public static void captureDrops(EntityPlayerMP entityPlayerMP, DamageSource damageSource, int recentlyHit) {
		setCaptureDrops(entityPlayerMP, false);
		Object event = createPlayerDropsEvent(entityPlayerMP, damageSource, getCapturedDrops(entityPlayerMP), recentlyHit > 0);
		if(!postEvent(event)) {
			for(EntityItem entityItem : getCapturedDrops(entityPlayerMP)) {
				entityPlayerMP.worldObj.spawnEntityInWorld(entityItem); 
			}
		}
	}
   
   private static boolean postEvent(Object event) {
      if(event == null) {
         System.err.println("SPC/Forge: No event to post.");
         return false;
      }
      try {
         Object returnVal = post.invoke(eventBusField.get(null), new Object[]{event});
         return Boolean.parseBoolean(returnVal.toString());
      } catch (Throwable t) {
         t.printStackTrace();
         return false;
      }
   }
   
   public static void watchChunk(ChunkCoordIntPair chunkCoord, EntityPlayerMP player) {
      try {
         Object event = watchConstructor.newInstance(new Object[]{chunkCoord, player});
         postEvent(event);
      } catch (Throwable t) {
         t.printStackTrace();
      }
   }
   
   private static Object createPlayerDropsEvent(EntityPlayer player, DamageSource damageSource,
         ArrayList<EntityItem> drops, boolean wasRecentlyHit) {
      Object playerDropsEvent = null;
      try {
         playerDropsEvent = playerDropsEventConstructor.newInstance(new Object[]{player, damageSource, drops, wasRecentlyHit});
      } catch (Throwable t) {
         t.printStackTrace();
      }
      return playerDropsEvent;
   }
   
   private static void setCaptureDrops(Entity entity, boolean val) {
		try {
			captureDropsField.set(entity, val);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private static ArrayList<EntityItem> getCapturedDrops(Entity entity) {
		ArrayList<EntityItem> capturedDrops = new ArrayList<EntityItem>();
		try {
			Object returnVal = capturedDropsField.get(entity);
			if(returnVal instanceof ArrayList) {
				capturedDrops = (ArrayList<EntityItem>)returnVal;
			} else {
            System.err.println("SPC/Forge: Couldn't determine captured drops.");
         }
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return capturedDrops;
	}
   
	public static boolean onLivingDeath(EntityPlayerMP entityPlayerMP, DamageSource damageSource) {
      try {
         Object returnVal = onLivingDeath.invoke(null, entityPlayerMP, damageSource);
         return Boolean.parseBoolean(returnVal.toString());
      } catch (Throwable t) {
         t.printStackTrace();
         return false;
      }
	}
   
   public static double getBlockReachDistance(ItemInWorldManager manager) {
      try {
         Object returnVal = getBlockReachDistance.invoke(manager);
         return Double.parseDouble(returnVal.toString());
      } catch (Throwable t) {
         t.printStackTrace();
         return 5.0D;
      }
   }
   
   public static void setBlockReachDistance(ItemInWorldManager manager, double distance) {
      try {
         setBlockReachDistance.invoke(manager, distance);
      } catch (Throwable t) {
         t.printStackTrace();
      }
   }
   
   static {
      if(HAS_FORGE) {
         System.out.println("SPC: Detected Forge.");
         forgeClass = ReflectionHelper.getClass("net.minecraftforge.common.MinecraftForge");
         eventBusField = ReflectionHelper.getField(forgeClass, "EVENT_BUS");
         eventClass = ReflectionHelper.getClass("net.minecraftforge.event.Event");
         eventBusClass = ReflectionHelper.getClass("net.minecraftforge.event.EventBus");
         post = ReflectionHelper.getMethod(eventBusClass, "post", eventClass);
         capturedDropsField = ReflectionHelper.getField(Entity.class, "capturedDrops");
         captureDropsField = ReflectionHelper.getField(Entity.class, "captureDrops");
         playerDropsEventClass = ReflectionHelper.getClass("net.minecraftforge.event.entity.player.PlayerDropsEvent");
         final Class[] params = new Class[]{ EntityPlayer.class, DamageSource.class, ArrayList.class, Boolean.TYPE };
         playerDropsEventConstructor = ReflectionHelper.getConstructor(playerDropsEventClass, params);
         watchClass = ReflectionHelper.getClass("net.minecraftforge.event.world.ChunkWatchEvent$Watch");
         watchConstructor = ReflectionHelper.getConstructor(watchClass, new Class[]{ChunkCoordIntPair.class, EntityPlayerMP.class});
         getBlockReachDistance = ReflectionHelper.getMethod(ItemInWorldManager.class, "getBlockReachDistance");
         setBlockReachDistance = ReflectionHelper.getMethod(ItemInWorldManager.class, "setBlockReachDistance", Double.TYPE);
         forgeHooksClass = ReflectionHelper.getClass("net.minecraftforge.common.ForgeHooks");
         onLivingDeath = ReflectionHelper.getMethod(forgeHooksClass, "onLivingDeath", EntityLiving.class, DamageSource.class);
      } else {
         System.out.println("SPC: Forge not detected.");
         forgeClass = null; eventBusField = null;
         eventClass = null; eventBusClass = null;
         post = null;
         capturedDropsField = null; captureDropsField = null;
         playerDropsEventClass = null; playerDropsEventConstructor = null;
         watchClass = null; watchConstructor = null;
         getBlockReachDistance = null; setBlockReachDistance = null;
         forgeHooksClass = null; onLivingDeath = null;
      }
   }
}