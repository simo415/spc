package com.sijobe.spc.command;

import com.sijobe.spc.validation.Parameter;
import com.sijobe.spc.validation.ParameterDouble;
import com.sijobe.spc.validation.Parameters;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Player;

import java.util.List;

import net.minecraft.src.EntityTNTPrimed;
import net.minecraft.src.MathHelper;

/**
 * Shoots a piece of cannon in the direction the player is facing
 *
 * @author simo_415
 * @version 1.0
 */
@Command (
   name = "cannon",
   description = "Shoots TNT in the direction you are pointing, at the specified strength",
   example = "10",
   videoURL = "http://www.youtube.com/watch?v=yuBcRP1ILKg",
   version = "1.0"
)
public class Cannon extends StandardCommand {

   private static final Parameters PARAMETERS = new Parameters (
      new Parameter[] {
         new ParameterDouble("[STRENGTH]",true)
      }
   );

   @Override
   public void execute(CommandSender sender, List<?> params) {
      Player player = super.getSenderAsPlayer(sender);
      EntityTNTPrimed tnt = new EntityTNTPrimed(player.getWorld().getMinecraftWorld());
      tnt.setLocationAndAngles(player.getPosition().getX(), player.getPosition().getY() + 1, player.getPosition().getZ(), player.getYaw(), player.getPitch());
      tnt.fuse = 40;
      tnt.motionX = -MathHelper.sin((tnt.rotationYaw / 180F) * 3.141593F) * MathHelper.cos((tnt.rotationPitch / 180F) * 3.141593F);
      tnt.motionZ = MathHelper.cos((tnt.rotationYaw / 180F) * 3.141593F) * MathHelper.cos((tnt.rotationPitch / 180F) * 3.141593F);
      tnt.motionY = -MathHelper.sin((tnt.rotationPitch / 180F) * 3.141593F);

      double multiplier = 1;
      if (params.size() > 0) {
         multiplier = (Double)params.get(0);
      }

      tnt.motionX *= multiplier;
      tnt.motionY *= multiplier;
      tnt.motionZ *= multiplier;

      player.getWorld().getMinecraftWorld().spawnEntityInWorld(tnt);
   }

   @Override
   public Parameters getParameters() {
      return PARAMETERS;
   }
}
