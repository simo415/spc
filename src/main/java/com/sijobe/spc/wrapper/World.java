package com.sijobe.spc.wrapper;

import java.lang.reflect.Field;
import java.util.Random;

import com.sijobe.spc.ModSpc;
import com.sijobe.spc.util.AccessHelper;


import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenForest;
import net.minecraft.world.gen.feature.WorldGenTaiga1;
import net.minecraft.world.gen.feature.WorldGenTaiga2;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.storage.WorldInfo;

/**
 * Provides methods that interact with the Minecraft world
 *
 * @author simo_415
 * @version 1.2
 */
public class World {
   /**
    * The world instance this class wraps around
    */
   private final net.minecraft.world.World world;
   /**
    * A random instance provides random numbers for methods
    */
   private final Random random;
   
   /**
    * Creates a new instance of this wrapper
    * 
    * @param world - The world instance to wrap around
    */
   public World(net.minecraft.world.World world) {
      this.world = world;
      random = new Random();
   }
   
   /**
    * Gets the ID of the block
    * 
    * @param x - The X coordinate of the block to get
    * @param y - The Y coordinate of the block to get
    * @param z - The Z coordinate of the block to get
    * @return the block at (x, y, z)
    */
   public Block getBlock(int x, int y, int z) {
      return Block.fromMinecraftBlock(this.getMinecraftWorld().getBlock(x, y, z));
   }
   
   /**
    * Gets the ID of the block
    * 
    * @param coord - The coordinate of the block to get
    * @return The ID of the block
    */
   public Block getBlock(Coordinate coord) {
      return this.getBlock(coord.getBlockX(), coord.getBlockY(), coord.getBlockZ());
   }
   
   /**
    * Gets the data of the block
    * 
    * @param coord - The coordinate of the block to use
    * @return The data of the block
    */
   public int getBlockData(Coordinate coord) {
      return world.getBlockMetadata(coord.getBlockX(), coord.getBlockY(), coord.getBlockZ());
   }
   
   /**
    * Returns true if hardcore mode is currently enabled
    * 
    * @return True is returned when hardcore mode is enabled
    */
   public boolean isHardcore() {
      return world.getWorldInfo().isHardcoreModeEnabled();
   }
   
   /**
    * Sets the hardcore mode of the game to the given boolean value
    * 
    * @param hardcore - True turns hardcore mode on, false turns it off
    */
   public void setHardcore(boolean hardcore) {
      changeWorldInfo("hardcore", hardcore);
   }
   
   /**
    * Returns true if cheat mode is currently enabled
    * 
    * @return True is returned when cheat mode is enabled
    */
   public boolean isCheats() {
      return world.getWorldInfo().areCommandsAllowed();
   }
   
   /**
    * Sets the cheat mode of the game to the given boolean value
    * 
    * @param hardcore - True turns cheats mode on, false turns it off
    */
   public void setCheats(boolean cheats) {
      changeWorldInfo("allowCommands", cheats);
      try {
		AccessHelper.setBoolean(MinecraftServer.getMinecraftServer().getConfigurationManager(), "commandsAllowedForAll", cheats);
	} catch (NoSuchFieldException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (SecurityException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IllegalArgumentException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IllegalAccessException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
   }
   
   /**
    * Changes fields in the WorldInfo class 
    * 
    * @param key - The key to modify
    * @param value - The value to change
    */
   private void changeWorldInfo(String key, Object value) {
      NBTTagCompound nbt = world.getWorldInfo().getNBTTagCompound();
      if (value instanceof String) {
         nbt.setString(key, (String) value);
      } else if (value instanceof Boolean) {
         nbt.setBoolean(key, (Boolean) value);
      } else if (value instanceof Integer) {
         nbt.setInteger(key, (Integer) value);
      } else if (value instanceof Long) {
         nbt.setLong(key, (Long) value);
      } 
      WorldInfo info = new WorldInfo(nbt);
      try {
         Field fields[] = net.minecraft.world.World.class.getDeclaredFields();
         for (Field field : fields) {
            field.setAccessible(true);
            if (field.get(this.world) instanceof WorldInfo) {
               field.set(this.world, info);
               break;
            }
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
   
   /**
    * Gets the current difficulty of the world (0-3)
    * 
    * @return The difficulty of the world
    */
   public EnumDifficulty getDifficulty() {
	   return MinecraftServer.getMinecraftServer().func_147135_j();
   }
   
   /**
    * Sets the difficulty of the current world (0-3)
    * 
    * @param difficulty - The difficulty of the world
    */
   public void setDifficulty(EnumDifficulty difficulty) {
	   MinecraftServer.getMinecraftServer().func_147139_a(difficulty);
	   ModSpc.instance.proxy.setDifficulty(difficulty);
   }
   
   /**
    * Sets the block specified by coordinates in the world to the type
    * 
    * @param coord - The coordinate of the block to use
    * @param type - The BlockID to change the specified location to
    * @return True if the data was set correctly
    */
   public boolean setBlock(Coordinate coord, Block type) {
      return world.setBlock(coord.getBlockX(), coord.getBlockY(), coord.getBlockZ(), type.convert(), 0, 2);
   }
   
   /**
    * Sets the blocks data value
    * 
    * @param coord - The coordinate of the block to use
    * @param type - The data type to change to
    * @return True if the data was set correctly
    */
   public boolean setBlockData(Coordinate coord, int type) {
      return world.setBlockMetadataWithNotify(coord.getBlockX(), coord.getBlockY(), coord.getBlockZ(), type, 4);
   }
   
    /**
    * Sets the blocks data value and sets metadata
    * 
    * @param coord - The coordinate of the block to use
    * @param type - The data type to change to
    * @param meta - The metadata to set on the block
    * @return True if the data was set correctly
    */
   public boolean setBlockDataWithMeta(Coordinate coord, Block type, int meta) {
      return world.setBlock(coord.getBlockX(), coord.getBlockY(), coord.getBlockZ(), type.convert(), meta, 2);
      // 2 = setblock, 3 = notify
   }
   
   /**
    * Gets the current worlds time
    * 
    * @return The current worlds time 
    */
   public long getTime() {
      return world.getWorldTime();
   }
   
   /**
    * Sets the current worlds time
    * 
    * @param time - The time to set the world to
    */
   public void setTime(long time) {
      world.setWorldTime(time);
   }
   
   /**
    * Returns true if it is currently raining in the current world
    * 
    * @return True is returned if it is currently raining
    */
   public boolean isRaining() {
      return world.isRaining();
   }
   
   /**
    * Turns rain on/off based on the raining parameter
    * 
    * @param raining - True to turn rain on, false to turn it off
    */
   public void setRaining(boolean raining) {
      world.getWorldInfo().setRaining(raining);
   }
   
   /**
    * Returns true if there is currently a thunder storm in the world
    * 
    * @return True if there is thunder
    */
   public boolean isThunder() {
      return world.isThundering();
   }
   
   /**
    * Sets whether there is thunder
    * 
    * @param thunder - True to turn thunder on, false to turn it off
    */
   public void setThunder(boolean thunder) {
      world.getWorldInfo().setThundering(thunder);
   }
   
   /**
    * Uses a lightning bolt at the specified coordinate
    * 
    * @param coordinate - The coordinate to use the lightning bolt at
    */
   public void useLightning(Coordinate coordinate) {
      EntityLightningBolt lightning = new EntityLightningBolt(world, coordinate.getX(),coordinate.getY(),coordinate.getZ());
      world.addWeatherEffect(lightning);
   }
   
   /**
    * Gets the name of the currently played world
    * 
    * @return The name of the world
    */
   public String getName() {
      return world.getWorldInfo().getWorldName();
   }
   
   /**
    * Gets the block light level at the specified coordinate
    * 
    * @param coordinate - The coordinate to check the blocks light level
    * @return The integer value specifying the block light level
    */
   public int getBlockLightLevel(Coordinate coordinate) {
      return world.getBlockLightValue(coordinate.getBlockX(), coordinate.getBlockY(), coordinate.getBlockZ());
   }
   
   /**
    * Empties a container (specified by the coordinates) of all of its 
    * contents. True is returned if a container was emptied of its contents, if
    * there were any issues or the coordinate wasn't a container false is 
    * returned
    * 
    * @param coordinate - The coordinate to empty
    * @return True if successful
    */
   public boolean emptyContainer(Coordinate coordinate) {
      try {
         IInventory i = (IInventory)world.getTileEntity(coordinate.getBlockX(), coordinate.getBlockY(), coordinate.getBlockZ());
         for (int j = 0; j < i.getSizeInventory(); j++) {
            i.setInventorySlotContents(j, null);
         }
      } catch (Exception e) {
         return false;
      }
      return true;
   }
   
   /**
    * Gets the coordinates to the worlds spawn point
    * 
    * @return The coordinates
    */
   public Coordinate getSpawn() {
      return new Coordinate(world.getSpawnPoint().posX, world.getSpawnPoint().posY, world.getSpawnPoint().posZ);
   }
   
   /**
    * Sets the spawn position using the provided coordinate
    * 
    * @param coordinate - The position to set the spawn to
    */
   public void setSpawn(Coordinate coordinate) {
      world.setSpawnLocation(coordinate.getBlockX(), coordinate.getBlockY(), coordinate.getBlockZ());
   }
   
   /**
    * Generates a big tree at the specified coordinates and returns true if 
    * this was successful, false otherwise.
    * 
    * @param coordinate - The coordinate to generate the big tree at 
    * @return True if the generation was successful, false otherwise
    */
   public boolean generateBigTree(Coordinate coordinate) {
      return (new WorldGenBigTree(true)).generate(world, random, coordinate.getBlockX(), coordinate.getBlockY(), coordinate.getBlockZ());
   }
   
   /**
    * Generates a tree at the specified coordinates and returns true if 
    * this was successful, false otherwise.
    * 
    * @param coordinate - The coordinate to generate the tree at 
    * @return True if the generation was successful, false otherwise
    */
   public boolean generateTree(Coordinate coordinate) {
      return (new WorldGenTrees(true)).generate(world, random, coordinate.getBlockX(), coordinate.getBlockY(), coordinate.getBlockZ());
   }
   
   /**
    * Generates a birch tree at the specified coordinates and returns true if 
    * this was successful, false otherwise.
    * 
    * @param coordinate - The coordinate to generate the birth tree at 
    * @return True if the generation was successful, false otherwise
    */
   public boolean generateBirchTree(Coordinate coordinate) {
	   //second arg for WorldGenForest constructor might need to be true 
      return (new WorldGenForest(true, false)).generate(world, random, coordinate.getBlockX(), coordinate.getBlockY(), coordinate.getBlockZ());
   }
   
   /**
    * Generates a redwood tree at the specified coordinates and returns true 
    * if this was successful, false otherwise.
    * 
    * @param coordinate - The coordinate to generate the redwood tree at 
    * @return True if the generation was successful, false otherwise
    */
   public boolean generateRedwoodTree(Coordinate coordinate) {
      return (new WorldGenTaiga1()).generate(world, random, coordinate.getBlockX(), coordinate.getBlockY(), coordinate.getBlockZ());
   }
   
   /**
    * Generates a tall redwood tree at the specified coordinates and returns 
    * true if this was successful, false otherwise.
    * 
    * @param coordinate - The coordinate to generate the tall redwood tree at 
    * @return True if the generation was successful, false otherwise
    */
   public boolean generateTallRedwoodTree(Coordinate coordinate) {
      return (new WorldGenTaiga2(true)).generate(world, random, coordinate.getBlockX(), coordinate.getBlockY(), coordinate.getBlockZ());
   }
   
   /**
    * Creates an explosion at the specified coordinates of the specified size. 
    * If the player if provided then the specified player is immune to the 
    * explosion. 
    * 
    * @param player - The player that is immune
    * @param coordinate - The coordinate to set the explosion at
    * @param size - The size of the explosion, for reference TNT is 4
    */
   public void createExplosion(Player player, Coordinate coordinate, int size) {
      world.createExplosion(player.getMinecraftPlayer(), coordinate.getX(), coordinate.getY(), coordinate.getZ(), size, true);
   }
   
   /**
    * Checks if the specified block type is valid in Minecraft
    * 
    * @param type - The type of block
    * @return True if it is a valid block type
    */
   public boolean isValidBlockType(Block type) {
	   return Block.blockRegistry.getNameForObject(type) != null;
   }
   
   /**
    * Gets the standard Minecraft World object
    * 
    * @return The Minecraft World object
    */
   public net.minecraft.world.World getMinecraftWorld() {
      return world;
   }
}
