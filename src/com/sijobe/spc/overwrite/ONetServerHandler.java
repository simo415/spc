package com.sijobe.spc.overwrite;

import com.sijobe.spc.command.Noclip;
import com.sijobe.spc.util.ForgeHelper;
import com.sijobe.spc.util.Mappings;
import com.sijobe.spc.util.ReflectionHelper;

import java.lang.reflect.Field;
import java.util.HashMap;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.INetworkManager;
import net.minecraft.src.NetServerHandler;
import net.minecraft.src.Packet10Flying;
import net.minecraft.src.WorldServer;

/**
 * Custom server handler to allow noclip
 * Note: use of reflection is neccessary,
 * otherwise player cannot interact with world
 *
 * @author q3hardcore
 * @version 1.4
 */
public final class ONetServerHandler extends NetServerHandler {

   private final NetServerHandler oldInstance;
   private final MinecraftServer mcServer;
   private int ticksForFloatKick;
   private double lastPosX;
   private double lastPosY;
   private double lastPosZ;
   private boolean hasMoved = true;

   private static final HashMap<String, String[]> fieldMappings;   
   
   // mappings valid as of: Minecraft 1.5.2 (no changes)
   static {
        fieldMappings = new Mappings();
        fieldMappings.put("lastPosX", new String[]{"n", "field_72579_o"});
        fieldMappings.put("lastPosY", new String[]{"o", "field_72589_p"});
        fieldMappings.put("lastPosZ", new String[]{"p", "field_72588_q"});
        fieldMappings.put("hasMoved", new String[]{"q", "field_72587_r"});
    }
   
   public ONetServerHandler(MinecraftServer par1, INetworkManager par2, EntityPlayerMP par3, NetServerHandler instance) {
      super(par1, par2, par3);
      
      if(instance instanceof ONetServerHandler) {
         throw new RuntimeException("SPC: Critical error encountered!");
      }
      
      oldInstance = instance;
      mcServer = par1;
      connectionClosed = oldInstance.connectionClosed;
   }
   
   public NetServerHandler getOldInstance() {
      return oldInstance;
   }

   private void updateHasMoved() {
      Field field = ReflectionHelper.getField(oldInstance, fieldMappings.get("hasMoved"));
      this.hasMoved = ReflectionHelper.getBoolean(field, oldInstance);
   }
   
   private void updatePosition() {
      Field field;
      field = ReflectionHelper.getField(oldInstance, fieldMappings.get("lastPosX"));
      this.lastPosX = ReflectionHelper.getDouble(field, oldInstance);
      field = ReflectionHelper.getField(oldInstance, fieldMappings.get("lastPosY"));
      this.lastPosY = ReflectionHelper.getDouble(field, oldInstance);
      field = ReflectionHelper.getField(oldInstance, fieldMappings.get("lastPosZ"));
      this.lastPosZ = ReflectionHelper.getDouble(field, oldInstance);
   }
   
   private void setHasMoved(boolean bool) {
      Field field = ReflectionHelper.getField(oldInstance, fieldMappings.get("hasMoved"));
      ReflectionHelper.setField(field, oldInstance, bool);
   }
   
   private void setPosition(double x, double y, double z) {
      Field field;
      field = ReflectionHelper.getField(oldInstance, fieldMappings.get("lastPosX"));
      ReflectionHelper.setField(field, oldInstance, x);
      field = ReflectionHelper.getField(oldInstance, fieldMappings.get("lastPosY"));
      ReflectionHelper.setField(field, oldInstance, y);
      field = ReflectionHelper.getField(oldInstance, fieldMappings.get("lastPosZ"));
      ReflectionHelper.setField(field, oldInstance, z);
   }
   
   @Override
   public void handleFlying(Packet10Flying par1Packet10Flying)
   {
      // we use Forge's handleFlying method
      if(!ForgeHelper.HAS_FORGE) {
         updateHasMoved();
         updatePosition();
      }
   
      Noclip.checkSafe(this.playerEntity);
   
      // Forge has Noclip support
      if(ForgeHelper.HAS_FORGE) {
         super.handleFlying(par1Packet10Flying);
         return;
      }
   
      WorldServer var2 = this.mcServer.worldServerForDimension(oldInstance.playerEntity.dimension);

      if (!oldInstance.playerEntity.playerConqueredTheEnd)
      {
         double var3;

         if (!hasMoved)
         {
            var3 = par1Packet10Flying.yPosition - lastPosY;

            if (par1Packet10Flying.xPosition == lastPosX && var3 * var3 < 0.01D && par1Packet10Flying.zPosition == lastPosZ)
            {
               setHasMoved(true);
               hasMoved = true;
            }
         }

         if (hasMoved)
         {
            double var5;
            double var7;
            double var9;
            double var13;

            if (oldInstance.playerEntity.ridingEntity != null)
            {
               float var34 = oldInstance.playerEntity.rotationYaw;
               float var4 = oldInstance.playerEntity.rotationPitch;
               oldInstance.playerEntity.ridingEntity.updateRiderPosition();
               var5 = oldInstance.playerEntity.posX;
               var7 = oldInstance.playerEntity.posY;
               var9 = oldInstance.playerEntity.posZ;
               double var35 = 0.0D;
               var13 = 0.0D;

               if (par1Packet10Flying.rotating)
               {
                  var34 = par1Packet10Flying.yaw;
                  var4 = par1Packet10Flying.pitch;
               }

               if (par1Packet10Flying.moving && par1Packet10Flying.yPosition == -999.0D && par1Packet10Flying.stance == -999.0D)
               {
                  if (Math.abs(par1Packet10Flying.xPosition) > 1.0D || Math.abs(par1Packet10Flying.zPosition) > 1.0D)
                  {
                     System.err.println(oldInstance.playerEntity.getUsername() + " was caught trying to crash the server with an invalid position.");
                     this.kickPlayerFromServer("Nope!");
                     return;
                  }

                  var35 = par1Packet10Flying.xPosition;
                  var13 = par1Packet10Flying.zPosition;
               }

               this.playerEntity.onGround = par1Packet10Flying.onGround;
               this.playerEntity.onUpdateEntity();
               this.playerEntity.moveEntity(var35, 0.0D, var13);
               this.playerEntity.setPositionAndRotation(var5, var7, var9, var34, var4);
               this.playerEntity.motionX = var35;
               this.playerEntity.motionZ = var13;

               /*TODO if (this.playerEntity.ridingEntity != null)
               {
                  var2.uncheckedUpdateEntity(this.playerEntity.ridingEntity, true);
               }*/

               if (this.playerEntity.ridingEntity != null)
               {
                  this.playerEntity.ridingEntity.updateRiderPosition();
               }
               
               this.mcServer.getConfigurationManager().serverUpdateMountedMovingPlayer(this.playerEntity);
               lastPosX = this.playerEntity.posX;
               lastPosY = this.playerEntity.posY;
               lastPosZ = this.playerEntity.posZ;
               setPosition(lastPosX, lastPosY, lastPosZ);
               var2.updateEntity(this.playerEntity);
               return;
            }

            if (this.playerEntity.isPlayerSleeping())
            {
               this.playerEntity.onUpdateEntity();
               this.playerEntity.setPositionAndRotation(lastPosX, lastPosY, lastPosZ, this.playerEntity.rotationYaw, this.playerEntity.rotationPitch);
               var2.updateEntity(this.playerEntity);
               return;
            }

            var3 = this.playerEntity.posY;
            lastPosX = this.playerEntity.posX;
            lastPosY = this.playerEntity.posY;
            lastPosZ = this.playerEntity.posZ;
            setPosition(lastPosX, lastPosY, lastPosZ);
            var5 = this.playerEntity.posX;
            var7 = this.playerEntity.posY;
            var9 = this.playerEntity.posZ;
            float var11 = this.playerEntity.rotationYaw;
            float var12 = this.playerEntity.rotationPitch;

            if (par1Packet10Flying.moving && par1Packet10Flying.yPosition == -999.0D && par1Packet10Flying.stance == -999.0D)
            {
               par1Packet10Flying.moving = false;
            }

            if (par1Packet10Flying.moving)
            {
               var5 = par1Packet10Flying.xPosition;
               var7 = par1Packet10Flying.yPosition;
               var9 = par1Packet10Flying.zPosition;
               var13 = par1Packet10Flying.stance - par1Packet10Flying.yPosition;

               if (!this.playerEntity.isPlayerSleeping() && (var13 > 1.65D || var13 < 0.1D))
               {
                  this.kickPlayerFromServer("Illegal stance");
                  this.mcServer.getLogAgent().logWarning(this.playerEntity.getUsername() + " had an illegal stance: " + var13);
                  return;
               }

               if (Math.abs(par1Packet10Flying.xPosition) > 3.2E7D || Math.abs(par1Packet10Flying.zPosition) > 3.2E7D)
               {
                  this.kickPlayerFromServer("Illegal position");
                  return;
               }
            }

            if (par1Packet10Flying.rotating)
            {
               var11 = par1Packet10Flying.yaw;
               var12 = par1Packet10Flying.pitch;
            }

            this.playerEntity.onUpdateEntity();
            this.playerEntity.ySize = 0.0F;
            this.playerEntity.setPositionAndRotation(lastPosX, lastPosY, lastPosZ, var11, var12);

            if (!hasMoved)
            {
               return;
            }

            var13 = var5 - this.playerEntity.posX;
            double var15 = var7 - this.playerEntity.posY;
            double var17 = var9 - this.playerEntity.posZ;
            double var19 = Math.min(Math.abs(var13), Math.abs(this.playerEntity.motionX));
            double var21 = Math.min(Math.abs(var15), Math.abs(this.playerEntity.motionY));
            double var23 = Math.min(Math.abs(var17), Math.abs(this.playerEntity.motionZ));
            double var25 = var19 * var19 + var21 * var21 + var23 * var23;

            if (var25 > 100.0D && (!this.mcServer.isSinglePlayer() || !this.mcServer.getServerOwner().equals(this.playerEntity.getUsername())))
            {
               this.mcServer.getLogAgent().logWarning(this.playerEntity.getUsername() + " moved too quickly! " + var13 + "," + var15 + "," + var17 + " (" + var19 + ", " + var21 + ", " + var23 + ")");
               this.setPlayerLocation(lastPosX, lastPosY, lastPosZ, this.playerEntity.rotationYaw, this.playerEntity.rotationPitch);
               return;
            }

            float var27 = 0.0625F;
            boolean var28 = var2.getCollidingBoundingBoxes(this.playerEntity, this.playerEntity.boundingBox.copy().contract(var27, var27, var27)).isEmpty();

            if (this.playerEntity.onGround && !par1Packet10Flying.onGround && var15 > 0.0D)
            {
               this.playerEntity.addExhaustion(0.2F);
            }
            
            this.playerEntity.moveEntity(var13, var15, var17);
            this.playerEntity.onGround = par1Packet10Flying.onGround;
            this.playerEntity.addMovementStat(var13, var15, var17);
            double var29 = var15;
            var13 = var5 - this.playerEntity.posX;
            var15 = var7 - this.playerEntity.posY;

            if (var15 > -0.5D || var15 < 0.5D)
            {
               var15 = 0.0D;
            }

            var17 = var9 - this.playerEntity.posZ;
            var25 = var13 * var13 + var15 * var15 + var17 * var17;
            boolean var31 = false;

            if (var25 > 0.0625D && !this.playerEntity.isPlayerSleeping() && !this.playerEntity.theItemInWorldManager.isCreative())
            {
               var31 = true;
               this.mcServer.getLogAgent().logWarning(this.playerEntity.getUsername() + " moved wrongly!");
            }
            
            this.playerEntity.setPositionAndRotation(var5, var7, var9, var11, var12);
            boolean var32 = var2.getCollidingBoundingBoxes(this.playerEntity, this.playerEntity.boundingBox.copy().contract(var27, var27, var27)).isEmpty();

            if(this.playerEntity.noClip) {
               // do nothing
            }
            else if (var28 && (var31 || !var32) && !this.playerEntity.isPlayerSleeping())
            {
               this.setPlayerLocation(lastPosX, lastPosY, lastPosZ, var11, var12);
               return;
            }

            AxisAlignedBB var33 = this.playerEntity.boundingBox.copy().expand(var27, var27, var27).addCoord(0.0D, -0.55D, 0.0D);

            if (!this.mcServer.isFlightAllowed() && !this.playerEntity.theItemInWorldManager.isCreative() && !var2.checkBlockCollision(var33))
            {
               if (var29 >= -0.03125D)
               {
                  ticksForFloatKick++;
                  
                  if (ticksForFloatKick > 80)
                  {
                     this.mcServer.getLogAgent().logWarning(this.playerEntity.getUsername() + " was kicked for floating too long!");
                     this.kickPlayerFromServer("Flying is not enabled on this server");
                     return;
                  }
               }
            }
            else
            {
               ticksForFloatKick = 0;
            }
            
            this.playerEntity.onGround = par1Packet10Flying.onGround;
            this.mcServer.getConfigurationManager().serverUpdateMountedMovingPlayer(this.playerEntity);
            this.playerEntity.updateFlyingState(this.playerEntity.posY - var3, par1Packet10Flying.onGround);
         }
      }
   }
}
