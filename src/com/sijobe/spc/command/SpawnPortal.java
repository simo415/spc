package com.sijobe.spc.command;

import com.sijobe.spc.validation.Parameter;
import com.sijobe.spc.validation.ParameterString;
import com.sijobe.spc.validation.Parameters;
import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Coordinate;
import com.sijobe.spc.wrapper.Player;
import java.lang.reflect.Method;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.MathHelper;
import net.minecraft.world.Teleporter;

import java.util.List;

/**
 * Command spawns the specified type of portal
 *
 * @author q3hardcore
 * @version 1.0
 * @survived 1.7.2 update but end portal doesn't work
 */
@Command (
   name = "spawnportal",
   description = "Spawns a portal at the players location",
   example = "nether",
   version = "1.0"
)
public class SpawnPortal extends StandardCommand {
   /**
    * Specifies the parameters of the command
    */
   private static final Parameters PARAMETERS = new Parameters (
      new Parameter[] {
         new ParameterString("<end|nether>",false), 
      }
   );

   /**
    * @see com.sijobe.spc.wrapper.CommandBase#execute(net.minecraft.src.ICommandSender, java.util.List)
    */
   @Override
   public void execute(CommandSender sender, List<?> params) throws CommandException {
      Player player = super.getSenderAsPlayer(sender);
      String portalType = (String)params.get(0);
      if (portalType.equalsIgnoreCase("end")) {
         Coordinate coord = player.getPosition();
         int x = MathHelper.floor_double(coord.getX());
         int z = MathHelper.floor_double(coord.getZ());
         EntityDragon entityDragon = new EntityDragon(player.getWorld().getMinecraftWorld()); // EntityDragon
         try {
            Class<?>[] args = new Class<?>[]{Integer.TYPE, Integer.TYPE};
            Method method;
            try {
               method = entityDragon.getClass().getDeclaredMethod("c", args);
            } catch (NoSuchMethodException nsme) {
               method = entityDragon.getClass().getDeclaredMethod("createEnderPortal", args);
            }
            method.setAccessible(true);
            method.invoke(entityDragon, new Object[]{x, z});
         } catch (Throwable t) {
            t.printStackTrace();
            throw new CommandException("End portal generation is currently unsupported.");
         }
      } else if (portalType.equalsIgnoreCase("nether")) {
         EntityPlayerMP playerEntity;
         if(player.getMinecraftPlayer() instanceof EntityPlayerMP) {
            playerEntity = (EntityPlayerMP)player.getMinecraftPlayer();
         } else {
            throw new CommandException("SPC should *NOT* be processing commands client-side!");
         }
         (new Teleporter(playerEntity.getServerForPlayer())).makePortal(playerEntity);
      } else {
         throw new CommandException("Invalid portal type specified.");
      }
   }

   /**
    * @see com.sijobe.spc.wrapper.CommandBase#getParameters()
    */
   @Override
   public Parameters getParameters() {
      return PARAMETERS;
   }
}
