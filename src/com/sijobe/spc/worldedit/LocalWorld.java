package com.sijobe.spc.worldedit;

import com.sijobe.spc.wrapper.Coordinate;
import com.sijobe.spc.wrapper.World;
import com.sk89q.worldedit.BiomeType;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.EntityType;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.Vector2D;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.BaseItemStack;
import com.sk89q.worldedit.regions.Region;

// q3 code ;)
import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.Chunk;

/**
 * Implements the WorldEdit local world class that provides necessary methods
 * required to edit the world.
 *
 * TODO: 
 * java.util.ConcurrentModificationException
   at java.util.AbstractList$Itr.checkForComodification(AbstractList.java:372)
   at java.util.AbstractList$Itr.next(AbstractList.java:343)
   at net.minecraft.src.PlayerManager.func_72693_b(PlayerManager.java:49)
   at net.minecraft.src.WorldServer.tick(WorldServer.java:123)
   at net.minecraft.server.MinecraftServer.updateTimeLightAndEntities(MinecraftServer.java:613)
   at net.minecraft.server.MinecraftServer.tick(MinecraftServer.java:555)
   at net.minecraft.src.IntegratedServer.tick(IntegratedServer.java:121)
   at net.minecraft.server.MinecraftServer.run(MinecraftServer.java:464)
   at net.minecraft.src.ThreadServerApplication.run(ThreadServerApplication.java:17)
 *
 * @author simo_415
 * @version 1.1
 */
public class LocalWorld extends com.sk89q.worldedit.LocalWorld {

   /**
    * The world instance that the editing is carried out on
    */
   private World world;

   /**
    * Default constructor allows the world to be specified
    * 
    * @param world - The world that the editing should be done on
    */
   public LocalWorld(World world) {
      this.world = world;
   }

   @Override
   public boolean clearContainerBlockContents(Vector pos) {
      return world.emptyContainer(getCoordinate(pos));
   }

   @Override
   public boolean copyFromWorld(Vector pos, BaseBlock type) {
      // TODO Auto-generated method stub
      return false;
   }

   @Override
   public boolean copyToWorld(Vector pos, BaseBlock type) {
      // TODO Auto-generated method stub
      return false;
   }

   @Override
   public void dropItem(Vector pos, BaseItemStack item) {
      // TODO Auto-generated method stub

   }

   @Override
   public boolean equals(Object world) {
      if (world instanceof LocalWorld) {
         return ((LocalWorld)world).getName().equals(getName());
      }
      return false;
   }

   @Override
   public BiomeType getBiome(Vector2D arg0) {
      BiomeGenBase biome = this.world.getMinecraftWorld().getBiomeGenForCoords(arg0.getBlockX(), arg0.getBlockZ()); // world.getWorldChunkManager().getBiomeGenAt
      try {
         return MinecraftBiomeType.valueOf(biome.biomeName.toUpperCase(java.util.Locale.ENGLISH)); // name
      } catch(IllegalArgumentException e) {
         // e.printStackTrace();
         return BiomeType.UNKNOWN;
      } 
   }

   /**
    * TODO: Implements a custom block tile entity if required
    * @see https://github.com/sk89q/worldedit/blob/master/src/main/java/com/sk89q/worldedit/bukkit/NmsBlock.java
    * @see https://github.com/sk89q/worldedit/blob/master/src/main/java/com/sk89q/worldedit/bukkit/BukkitWorld.java#L962
    * 
    * @see com.sk89q.worldedit.LocalWorld#getBlock(com.sk89q.worldedit.Vector)
    */
   @Override
   public BaseBlock getBlock(Vector pos) {
      try {
         return new BaseBlock(getBlockType(pos), getBlockData(pos));
      } catch (Exception e) {
         return null;
      }
   }
   
   @Override
   public boolean isValidBlockType(int type) {
      if (type == 0) {
         return true;
      }
      return world.isValidBlockType(type);
   }

   @Override
   public int getBlockData(Vector pos) {
      return world.getBlockData(getCoordinate(pos));
   }

   @Override
   public int getBlockLightLevel(Vector pos) {
      return world.getBlockLightLevel(getCoordinate(pos));
   }

   @Override
   public int getBlockType(Vector pos) {
      return world.getBlockId(getCoordinate(pos));
   }

   @Override
   public String getName() {
      return world.getName();
   }

   @Override
   public int hashCode() {
      return world.getName().hashCode();
   }

   @Override
   public boolean regenerate(Region arg0, EditSession arg1) {
      // TODO Auto-generated method stub
      return false;
   }

   @Override
   public int removeEntities(EntityType entity, Vector pos, int area) {
      // TODO Auto-generated method stub
      return 1;
   }

   @Override
   public void setBiome(Vector2D arg0, BiomeType arg1) {
      if(arg1 instanceof MinecraftBiomeType) {
         int biomeId = ((MinecraftBiomeType) arg1).getBiomeID();
         
         int x = arg0.getBlockX();
         int z = arg0.getBlockZ();
         
         if(this.world.getMinecraftWorld().blockExists(x, 0, z)) { // blockExists
            Chunk chunk = this.world.getMinecraftWorld().getChunkFromBlockCoords(x, z); // Chunk, getChunkFromBlockCoords
            if(chunk != null) {
               byte[] biomevals = chunk.getBiomeArray(); // getBiomeArray
               biomevals[((z & 0xF) << 4 | x & 0xF)] = (byte)biomeId;
               // work around for biome data not being updated on client
               com.sijobe.spc.wrapper.Minecraft.getMinecraft().thePlayer.worldObj.getChunkFromBlockCoords(x, z).setBiomeArray(biomevals);
            } else {
               System.err.println("Can't set biome for null chunk.");
            }
         } else {
            System.err.println("Not setting biome.");
         }
      }
   }

   @Override
   @Deprecated
   public void setBlockData(Vector pos, int block) {
      world.setBlockData(getCoordinate(pos), block);
   }

   @Override
   @Deprecated
   public void setBlockDataFast(Vector pos, int block) {
      world.setBlockData(getCoordinate(pos), block);
   }

   @Override
   @Deprecated
   public boolean setBlockType(Vector pos, int block) {
      return world.setBlock(getCoordinate(pos), block);
   }

   @Override
   @Deprecated
   public boolean generateBigTree(EditSession session, Vector pos) {
      return world.generateBigTree(getCoordinate(pos));
   }

   @Override
   @Deprecated
   public boolean generateTree(EditSession session, Vector pos) {
      return world.generateTree(getCoordinate(pos));
   }

   @Override
   @Deprecated
   public boolean generateTallRedwoodTree(EditSession session, Vector pos) {
      return world.generateTallRedwoodTree(getCoordinate(pos));
   }

   @Override
   @Deprecated
   public boolean generateRedwoodTree(EditSession session, Vector pos) {
      return world.generateRedwoodTree(getCoordinate(pos));
   }

   @Override
   @Deprecated
   public boolean generateBirchTree(EditSession session, Vector pos) {
      return world.generateBirchTree(getCoordinate(pos));
   }

   @Override
   @Deprecated
   public int killMobs(Vector pos, int radius) {
      return 1;
   }

   /**
    * Gets the coordinate object that the specified Vector object represents
    * 
    * @param pos - The position of the coordinate
    * @return The matching coordinate object
    */
   private Coordinate getCoordinate(Vector pos) {
      return new Coordinate(pos.getX(), pos.getY(), pos.getZ());
   }
}
