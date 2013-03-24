package com.sijobe.spc.command;

import com.sijobe.spc.wrapper.CommandBase;
import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Coordinate;
import com.sijobe.spc.wrapper.Player;

import java.util.List;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityItem;
import net.minecraft.src.World;

/**
 * Command to clear item drops
 *
 * @author q3hardcore
 * @version 1.0
 */
@Command (
         name = "cleardrops",
         description = "Clears all item drops within 128 radius of player",
         example = "",
         videoURL = "",
         enabled = true
)
public class ClearDrops extends StandardCommand {

   @Override
   public void execute(CommandSender sender, List<?> params) throws CommandException {
      Player player = CommandBase.getSenderAsPlayer(sender);
      int removedDrops = removeItemDrops(player, 128);
      player.sendChatMessage("Cleared " + removedDrops + " item drop(s).");
   }

   public static int removeItemDrops(Player player, int radius) {
      Coordinate pos = player.getPosition();
      World world = player.getWorld().getMinecraftWorld();
      AxisAlignedBB boundingBox = AxisAlignedBB.getBoundingBox(
               pos.getX() - radius, pos.getY() - radius, pos.getZ() - radius,
               pos.getX() + radius, pos.getY() + radius, pos.getZ() + radius
      );
      List<?> nearbyEntities = world.getEntitiesWithinAABBExcludingEntity(
               player.getMinecraftPlayer(), boundingBox
      );
      int removedDrops = 0;
      for(int entityIndex = 0; entityIndex < nearbyEntities.size(); entityIndex++) {
         Entity entity = (Entity)nearbyEntities.get(entityIndex);
         if(entity instanceof EntityItem) {
            EntityItem entityItem = (EntityItem)entity;
            if(entityItem.age >= 0) {
               player.getWorld().getMinecraftWorld().removeEntity(entityItem);
               removedDrops++;
            }
         }
      }
      return removedDrops;
   }

}