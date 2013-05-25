package com.sijobe.spc.wrapper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.src.DamageSource;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;

/**
 * Contains methods that manage entities within the game
 *
 * @author simo_415
 */
public class Entity {

   /**
    * Holds the NAME > ID entity list
    */
   private static Map<String,Integer> NAME_TO_ID;
   /**
    * Holds the ID > NAME entity list
    */
   private static Map<Integer,String> ID_TO_NAME;
   /**
    * Holds the NAME > CLASS entity list
    */
   @SuppressWarnings("rawtypes")
   private static Map<String, Class> NAME_TO_CLASS;

   /**
    * Gets the specified entity list from the EntityList class. The Map is 
    * retrieved by the specified generic parameters specifying what type the
    * key is and what type the value is. 
    * 
    * @param <K> - The key type
    * @param <V> - The value type
    * @param key - The class of the key
    * @param value - The class of the value
    * @return The matching entity list, or null if not found
    */
   private static <K,V> Map<K,V> getEntityList(Class<K> key, Class<V> value) {
      Map<K,V> map = null;
      try {
         Field fields[] = EntityList.class.getDeclaredFields();
         for (Field field : fields) {
            field.setAccessible(true);
            Object tvalue = field.get(null);
            if (tvalue instanceof Map) {
               Map<?,?> temp = (Map<?,?>)tvalue;
               Object val = temp.keySet().iterator().next();
               if (val.getClass().isAssignableFrom(key) && temp.get(val).getClass().isAssignableFrom(value)) {
                  map = (Map<K,V>)temp;
                  break;
               }
            }
         }
      } catch (Exception e) {
         e.printStackTrace();
         return null;
      }
      return map;
   }

   /**
    * Gets a list of loaded entities
    * 
    * @return A List of strings specifying the names of the loaded entities
    */
   public static List<String> getLoadedEntities() {
      if (NAME_TO_ID == null) {
         NAME_TO_ID = getEntityList(String.class,Integer.class);
      }
      return new ArrayList<String>(NAME_TO_ID.keySet());
   }

   /**
    * Gets the name > id entity list.
    * 
    * @return The Map containing the name, ID entity pairs
    */
   public static Map<String,Integer> getNameToIdEntityList() {
      if (NAME_TO_ID == null) {
         NAME_TO_ID = getEntityList(String.class,Integer.class);
      }
      return NAME_TO_ID;
   }

   /**
    * Gets the name of the entity using the specified id, null is returned when
    * the id provided was not found.
    * 
    * @param id - The id of the entity to retrieve
    * @return The name of the entity
    */
   public static String getEntityName(int id) {
      if (ID_TO_NAME == null) {
         ID_TO_NAME = getEntityList(Integer.class,String.class);
      }
      return ID_TO_NAME.get(id);
   }

   /**
    * Gets the class associated with the specified name parameter, null is 
    * returned when the class was not found.
    * 
    * @param name - The name of the entity
    * @return The class that is associated with the name
    */
   public static Class<?> getEntityClass(String name) {
      if (NAME_TO_CLASS == null) {
         NAME_TO_CLASS = getEntityList(String.class, Class.class);
      }
      for (String key : NAME_TO_CLASS.keySet()) {
         if (key.equalsIgnoreCase(name)) {
            return NAME_TO_CLASS.get(key);
         }
      }
      return NAME_TO_CLASS.get(name);
   }

   /**
    * Spawns the specified entity at the provided coordinates in the specified 
    * world
    * 
    * @param entity - The entity to spawn
    * @param location - The coordinate to spawn it
    * @param world - The world to spawn it in
    * @return True when the entity was successfully spawned
    */
   public static boolean spawnEntity(String entity, Coordinate location, World world) {
      Class<?> entityClass = getEntityClass(entity);
      try {
         if (entityClass != null) {
            net.minecraft.src.Entity entityInstance = (net.minecraft.src.Entity)entityClass.getConstructor(net.minecraft.src.World.class).newInstance(world.getMinecraftWorld());
            entityInstance.setPosition(location.getX(), location.getY() + 1, location.getZ());
            if(entityInstance instanceof EntityLiving) {
               ((EntityLiving)entityInstance).initCreature();
            }
            world.getMinecraftWorld().spawnEntityInWorld(entityInstance);
            if(entityInstance instanceof EntityLiving) {
               ((EntityLiving)entityInstance).playLivingSound();
            }
            return true;
         }
      } catch (Exception e) { 
         e.printStackTrace();
      }
      return false;
   }
   
   public static List<net.minecraft.src.Entity> killEntities(String entity, Coordinate location, World world, double distance) {
      List<net.minecraft.src.Entity> removedEntities = new ArrayList<net.minecraft.src.Entity>();
      Class<?> entityClass = getEntityClass(entity);
      int count = 0;
      try {
         if (entityClass != null) {
            List<net.minecraft.src.Entity> toremove = new ArrayList<net.minecraft.src.Entity>();
            List<net.minecraft.src.Entity> entities = getLoadedEntities(world);
            for (net.minecraft.src.Entity loaded : entities) {
               if(!(entityClass.isInstance(loaded))) {
                  continue;
               }
               if (location.getDistanceBetweenCoordinates(new Coordinate(loaded.posX,loaded.posY,loaded.posZ)) < distance) {
                  toremove.add(loaded);
               }
            }
            for (net.minecraft.src.Entity remove : toremove) {
               if(remove instanceof EntityPlayer) {
                  continue;
               }
               if(remove.isEntityInvulnerable()) {
                  System.out.println(remove.toString() + " is invulnerable.");
                  continue;
               }
               remove.attackEntityFrom(DamageSource.outOfWorld, 1000);
               removedEntities.add(remove);
               count++;
            }
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
      // System.out.println("SPC: " + count + " entities removed.");
      return removedEntities;
   }
   
   public static List<net.minecraft.src.Entity> findEntities(String entity, Coordinate location, World world, double distance) {
      List<net.minecraft.src.Entity> foundEntities = new ArrayList<net.minecraft.src.Entity>();
      Class<?> entityClass = getEntityClass(entity);
      int count = 0;
      try {
         if (entityClass != null) {
            List<net.minecraft.src.Entity> found = new ArrayList<net.minecraft.src.Entity>();
            List<net.minecraft.src.Entity> entities = getLoadedEntities(world);
            for (net.minecraft.src.Entity loaded : entities) {
               if(!(entityClass.isInstance(loaded))) {
                  continue;
               }
               if (location.getDistanceBetweenCoordinates(new Coordinate(loaded.posX,loaded.posY,loaded.posZ)) < distance) {
                  found.add(loaded);
               }
            }
            for (net.minecraft.src.Entity foundEntity : found) {
               // we don't currently exclude any entities
               foundEntities.add(foundEntity);
               count++;
            }
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
      return foundEntities;
   }
   
   /**
    * Gets the loaded entities in this world
    * 
    * @param world - The world to get the loaded entities from
    * @return A List of loaded entities
    */
   private static List<net.minecraft.src.Entity> getLoadedEntities(World world) {
      return world.getMinecraftWorld().loadedEntityList;
   }
}
