package com.sijobe.spc.wrapper;

import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.EnumGameType;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.PotionEffect;
import net.minecraft.src.Vec3;

/**
 * Provides a wrapper around the Minecraft player classes.
 * 
 * @author simo_415
 * @version 1.0
 */
public class Player {

   /**
    * The object that the instance is wrapped around
    */
   private final EntityPlayer player;

   public Player(EntityPlayer player) {
      this.player = player;
   }

   /**
    * Sets the player position and if on server sends the update to the client
    * 
    * @param c - The coordinate that the player is being moved to
    */
   public void setPosition(Coordinate c) {
      if (player instanceof EntityPlayerMP) {
         ((EntityPlayerMP) player).setPositionAndUpdate(c.getX(), c.getY(), c.getZ());
      } else {
         player.setPosition(c.getX(), c.getY(), c.getZ());
      }
   }

   /**
    * Gets the current position that the player is currently standing
    * 
    * @return The position that the player is standing
    */
   public Coordinate getPosition() {
      return new Coordinate(player.posX, player.posY, player.posZ);
   }

   /**
    * Gets the player rotation yaw
    * 
    * @return Rotation Yaw
    */
   public float getYaw() {
      return player.rotationYaw;
   }

   /**
    * Sets the rotation yaw of the player
    * 
    * @param yaw - The yaw value to set to the player
    */
   public void setYaw(float yaw) {
      player.rotationYaw = yaw;
   }

   /**
    * Gets the player rotation pitch
    * 
    * @return Rotation pitch
    */
   public float getPitch() {
      return player.rotationPitch;
   }

   /**
    * Sets the rotation pitch of the player
    * 
    * @param pitch - The pitch value to set to the player
    */
   public void setPitch(float pitch) {
      player.rotationPitch = pitch;
   }

   /**
    * Gets the world that the player is currently in
    * 
    * @return The world that the player is currently in
    */
   public World getWorld() {
      if (player instanceof EntityPlayerMP) {
         return new World(((EntityPlayerMP) player).theItemInWorldManager.theWorld); // why?
      } else {
         return new World(player.worldObj);
      }
   }

   /**
    * Sends a chat message to the user
    * 
    * @param message - The message to send to the user
    */
   public void sendChatMessage(String message) {
      player.addChatMessage(message);
   }

   /**
    * Gives the player a maximum stack of the specified item
    * 
    * @param id - The item id
    */
   public void givePlayerItem(int id) {
      givePlayerItem(id, Item.getMaxStack(id));
   }

   /**
    * Gives the player a maximum stack of the specified item
    * 
    * @param id - The item id
    * @param quantity - The quantity of the item
    */
   public void givePlayerItem(int id, int quantity) {
      givePlayerItem(id, quantity, 0);
   }

   /**
    * Gives the player a maximum stack of the specified item
    * 
    * @param id - The item id
    * @param quantity - The quantity of the item
    * @param damage - The "damage" (metadata) value of the item
    */
   public void givePlayerItem(int id, int quantity, int damage) {
      ItemStack itemStack = new ItemStack(id, quantity, damage);
      if (!player.inventory.addItemStackToInventory(itemStack)) {
         player.dropPlayerItem(itemStack);
      }
   }

   /**
    * Gets the players current health
    * 
    * @return The value of the players health
    */
   public int getHealth() {
      return player.getHealth();
   }

   /**
    * Sets the players health to the specified value
    * 
    * @param health - The health amount
    */
   public void setHealth(int health) {
      player.setEntityHealth(health);
   }

   /**
    * Heals the player the specified quantity. Use a negative number to remove
    * the specified amount from the player.
    * 
    * @param quantity - The ammount to heal the player
    */
   public void heal(int quantity) {
      setHealth(getHealth() + quantity);
   }

   /**
    * Gets the players current hunger level
    * 
    * @return The hunger level
    */
   public int getHunger() {
      return player.getFoodStats().getFoodLevel();
   }

   /**
    * Sets the players hunger level
    * 
    * @param food - The hunger level to set to
    */
   public void setHunger(int food) {
      player.getFoodStats().setFoodLevel(food);
   }

   /**
    * Gets the boolean value representing whether player damage is on or off.
    * 
    * @return True when damage is on, false when damage is off
    */
   public boolean getDamage() {
      return !player.capabilities.disableDamage;
   }

   /**
    * Sets whether player damage is on or off
    * 
    * @param damage - when true it turns player damage on, false turns it off
    */
   public void setDamage(boolean damage) {
      player.capabilities.disableDamage = !damage;
   }

   /**
    * Sets the item contained within the specified inventory slot
    * 
    * @param slot - The slot to set
    * @param id - The item id
    * @param quantity - The item quantity
    * @param damage - The item "damage" value
    * @return True if the slot was correctly set, false otherwise
    */
   public boolean setInventorySlot(int slot, int id, int quantity, int damage) {
      if (slot < 0 || slot >= player.inventory.mainInventory.length) {
         return false;
      } else if (!Item.isValidItem(id)) {
         if (id == 0) {
            player.inventory.mainInventory[slot] = null;
            return true;
         }
         return false;
      }
      player.inventory.mainInventory[slot] = new ItemStack(id, quantity, damage);
      return true;
   }

   /**
    * Performs a trace on the players view to the specified maximum distance.
    * The trace returns the coordinate where it hits a block or null if nothing
    * was found.
    * 
    * @param distance - The maximum distance to check
    * @return A coordinate object containing the block that was inline with
    *         the players view
    */
   public Coordinate trace(double distance) {
      MovingObjectPosition m = rayTrace(distance, 1.0F);
      if (m == null) {
         return null;
      }
      return new Coordinate(m.blockX, m.blockY, m.blockZ);
   }

   public MovingObjectPosition rayTrace(double distance, float partialTickTime) {
      Vec3 positionVec = getPositionVec(partialTickTime);
      Vec3 lookVec = player.getLook(partialTickTime);
      Vec3 hitVec = positionVec.addVector(lookVec.xCoord * distance, lookVec.yCoord * distance, lookVec.zCoord * distance);
      return player.worldObj.rayTraceBlocks(positionVec, hitVec);
   }

   /**
    * interpolated position vector
    */
   public Vec3 getPositionVec(float partialTickTime) {
      double offsetY = player.posY + player.getEyeHeight();
      if (partialTickTime == 1.0F) {
         return getWorld().getMinecraftWorld().getWorldVec3Pool().getVecFromPool(player.posX, offsetY, player.posZ);
      } else {
         double var2 = player.prevPosX + (player.posX - player.prevPosX) * partialTickTime;
         double var4 = player.prevPosY + (offsetY - (player.prevPosY + player.getEyeHeight())) * partialTickTime;
         double var6 = player.prevPosZ + (player.posZ - player.prevPosZ) * partialTickTime;
         return getWorld().getMinecraftWorld().getWorldVec3Pool().getVecFromPool(var2, var4, var6);
      }
   }

   /*public String getGameType() { // TODO
   }*/

   /**
    * Sets the gametype that the player uses
    * 
    * @param gametype - The player game type
    * @return True if the specified gametype was found
    */
   public boolean setGameType(String gametype) {
      EnumGameType chosen = null;
      if ((chosen = EnumGameType.getByName(gametype)) == null) {
         return false;
      }
      player.setGameType(chosen);
      return true;
   }

   /**
    * Gets the players name
    * 
    * @return The players name
    */
   public String getPlayerName() {
      return player.username;
   }

   /**
    * Gets the item ID of the current held item
    * 
    * @return The ID of the currently held item
    */
   public int getCurrentItem() {
      try {
         return player.inventory.mainInventory[getCurrentSlot()].itemID;
      } catch (NullPointerException e) {
         return 0;
      }
   }

   /**
    * Gets the slot number of the current item
    * 
    * @return A value 0-8 based on the slot currently selected
    */
   public int getCurrentSlot() {
      return player.inventory.currentItem;
   }

   /**
    * Sets the URL that the skin is retrieved from
    * 
    * @param URL - The URL the skin is taken from
    */
   public void setSkin(String URL) {
      player.skinUrl = URL;
   }

   /**
    * Sets the player's current motion
    * 
    * @param motion - The motion to set to the player
    */
   public void setMotion(Coordinate motion) {
      player.motionX = motion.getX();
      player.motionY = motion.getY();
      player.motionZ = motion.getZ();
   }

   /**
    * Gets the player's current motion
    * 
    * @return The motion of the player
    */
   public Coordinate getMotion() {
      return new Coordinate(player.motionX, player.motionY, player.motionZ);
   }

   /**
    * Checks if the specified position is able to contain a player. This
    * involves two squares at the player's height that are empty and one block
    * beneath the player's feet.
    * 
    * @param player - The player object
    * @param x - The X coordinate
    * @param y - The Y coordinate
    * @param z - The Z coordinate
    * @return True if the player can stand in the specified location
    */
   public boolean isClear(Coordinate location) {
      return getWorld().getBlockId(location.getBlockX(), location.getBlockY(), location.getBlockZ()) == 0
      && getWorld().getBlockId(location.getBlockX(), location.getBlockY() + 1, location.getBlockZ()) == 0
      && !(getWorld().getBlockId(location.getBlockX(), location.getBlockY() - 1, location.getBlockZ()) == 0);
   }

   /**
    * Checks if the specified position is able to contain a player. This
    * involves two squares at the player's height that are empty and one block
    * below the player being empty.
    * 
    * @param player - The player object
    * @param x - The X coordinate
    * @param y - The Y coordinate
    * @param z - The Z coordinate
    * @return True if the player can fall in the specified location
    */
   public boolean isClearBelow(Coordinate location) {
      return getWorld().getBlockId(location.getBlockX(), location.getBlockY(), location.getBlockZ()) == 0
      && getWorld().getBlockId(location.getBlockX(), location.getBlockY() + 1, location.getBlockZ()) == 0
      && getWorld().getBlockId(location.getBlockX(), location.getBlockY() - 1, location.getBlockZ()) == 0;
   }
   
   /**
    * Gets the movement forward. This is a value between -1 and 1. -1 is when
    * the player is moving backward, 1 is that player moving forward. The value
    * of the float is the percentage of speed the player is moving.
    * 
    * @return The movement forward percentage
    */
   public float getMovementForward() {
      if (player instanceof EntityPlayerMP) {
         return ((EntityPlayerMP) player).getMoveForwardField();
      } else if (player instanceof EntityClientPlayerMP) {
         return ((EntityClientPlayerMP) player).getMovementForward();
      } else {
         return 0F;
      }
   }

   /**
    * Gets the movement strafe. This is a value between -1 and 1. -1 is when
    * the player is moving right, 1 is that player moving left. The value
    * of the float is the percentage of speed the player is moving.
    * 
    * @return The movement strafe percentage
    */
   public float getMovementStrafe() {
      if (player instanceof EntityPlayerMP) {
         return ((EntityPlayerMP) player).getMoveStrafingField();
      } else if (player instanceof EntityClientPlayerMP) {
         return ((EntityClientPlayerMP) player).getMovementStrafe();
      } else {
         return 0F;
      }
   }

   /**
    * Sets the player's step height.
    * 
    * @param height - The height of the player's step
    */
   public void setStepHeight(float height) {
      player.stepHeight = height;
   }

   /**
    * Gets the step height of the player
    * 
    * @return Height (in blocks) of the player step height
    */
   public float getStepHeight() {
      return player.stepHeight;
   }

   /**
    * Gets the internal Minecraft player object
    * 
    * @return The Minecraft player object
    */
   public EntityPlayer getMinecraftPlayer() {
      return player;
   }

   /**
    * Removes the specified potion from the player
    * 
    * @param potion - The potion ID to remove
    */
   public void removePotionEffect(int potion) {
      player.removePotionEffect(potion);
   }

   /**
    * Removes all potion effects
    */
   public void removeAllPotionEffects() {
      player.clearActivePotions();
   }

   /**
    * Adds a potion effect to the player
    * 
    * @param id - The ID of the potion
    * @param duration - The duration the potion should run for
    * @param strength - The strength of effect to add
    */
   public void addPotionEffect(int id, int duration, int strength) {
      player.addPotionEffect(new PotionEffect(id, duration, strength));
   }

   /**
    * Changes the dimension that the player is in
    * 
    * @param dimension - The dimension to move the player to
    */
   public void changeDimension(int dimension) {
      player.travelToDimension(dimension);
   }

   /**
    * Sets the quantity of air that the player has
    * 
    * @param air - The quantity of air that the player has
    */
   public void setAir(int air) {
      player.setAir(air);
   }

   /**
    * Gives the specified achievement to the player. Note that this will only
    * work in client mode, server cannot give achievements to the player.
    * 
    * @param name - The name of the achievement
    * @return If the achievement was able to be added to the player
    */
   public boolean addAchievement(String name) {
      if (player instanceof EntityClientPlayerMP) {
         if (Stats.doesAchievementExist(name)) {
            player.triggerAchievement(Stats.getAchievementByName(name));
            return true;
         }
      }
      return false;
   }

   /**
    * Returns true if flying is permitted for the user, false otherwise
    * 
    * @return True if flying is permitted
    */
   public boolean getAllowFlying() {
      return player.capabilities.allowFlying;
   }

   /**
    * Allows and disables Minecraft flying mode
    * 
    * @param allow - True to allow flying, false to disable it
    */
   public void setAllowFlying(boolean allow) {
      player.capabilities.allowFlying = allow;
      player.capabilities.isFlying = allow;
      player.sendPlayerAbilities();
   }

   /**
    * Returns true if the player is currently in creative mode, false if in
    * survival or adventure mode.
    * 
    * @return True is in creative mode, false otherwise.
    */
   public boolean isCreativeMode() {
      return player.capabilities.isCreativeMode;
   }
}
