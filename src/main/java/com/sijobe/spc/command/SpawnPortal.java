package com.sijobe.spc.command;

import com.sijobe.spc.validation.Parameter;
import com.sijobe.spc.validation.ParameterString;
import com.sijobe.spc.validation.Parameters;
import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Coordinate;
import com.sijobe.spc.wrapper.Player;
import com.sijobe.spc.wrapper.World;
import com.sijobe.spc.wrapper.Blocks;

import java.lang.reflect.Method;

import net.minecraft.block.BlockEndPortal;
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
         int y = MathHelper.floor_double(coord.getY()+1);
         int z = MathHelper.floor_double(coord.getZ());
         this.createEnderPortal(x, z, y, new World(sender.getMinecraftISender().getEntityWorld()));
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
   
   /** 
    * taken from EntityDragon.createEnderPortal
    * Creates the ender portal leading back to the normal world after defeating the enderdragon.
    */
   public void createEnderPortal(int x, int z, int level, World worldObj)
   {
       //byte level = 64; //used to be this in minecraft method
       BlockEndPortal.field_149948_a = true; //so dragon eggs don't despawn
       byte radius = 4;

       for (int coordy = level - 1; coordy <= level + 32; ++coordy)
       {
           for (int coordx = x - radius; coordx <= x + radius; ++coordx)
           {
               for (int coordz = z - radius; coordz <= z + radius; ++coordz)
               {
                   double offsetx = (double)(coordx - x);
                   double offsetz = (double)(coordz - z);
                   double dist = offsetx * offsetx + offsetz * offsetz;
                   
                   //sqrt isn't used for distance calculations because
                   //sqrt(x1*x1+y2*y2) < sqrt(x2*x2+y2*y2) will have the same result as
                   //x1*x1+y2*y2 < x2*x2+y2*y2
                   //this takes advantage of the multiplication property of equality
                   
                   if (dist <= ((double)radius - 0.5D) * ((double)radius - 0.5D)) //must be within distance
                   {
                       if (coordy < level) //lower level
                       {
                    	   //-1 so one block smaller radius
                           if (dist <= ((double)(radius - 1) - 0.5D) * ((double)(radius - 1) - 0.5D)) //right under end portal
                           {
                               worldObj.setBlock(new Coordinate(coordx, coordy, coordz), Blocks.bedrock);
                           }
                       }
                       else if (coordy > level) //above frame
                       {
                           worldObj.setBlock(new Coordinate(coordx, coordy, coordz), Blocks.air);
                       }
                       else if (dist > ((double)(radius - 1) - 0.5D) * ((double)(radius - 1) - 0.5D)) //forms the end portal ring
                       {
                           worldObj.setBlock(new Coordinate(coordx, coordy, coordz), Blocks.bedrock);
                       }
                       else //the actual end portal (only occurs on the level)
                       {
                           worldObj.setBlock(new Coordinate(coordx, coordy, coordz), Blocks.end_portal);
                       }
                   }
               }
           }
       }

       worldObj.setBlock(new Coordinate(x, level + 0, z), Blocks.bedrock); //the column
       worldObj.setBlock(new Coordinate(x, level + 1, z), Blocks.bedrock);
       worldObj.setBlock(new Coordinate(x, level + 2, z), Blocks.bedrock);
       worldObj.setBlock(new Coordinate(x - 1, level + 2, z), Blocks.torch); //torches on the clumn
       worldObj.setBlock(new Coordinate(x + 1, level + 2, z), Blocks.torch);
       worldObj.setBlock(new Coordinate(x, level + 2, z - 1), Blocks.torch);
       worldObj.setBlock(new Coordinate(x, level + 2, z + 1), Blocks.torch);
       worldObj.setBlock(new Coordinate(x, level + 3, z), Blocks.bedrock); //another block for the column
       worldObj.setBlock(new Coordinate(x, level + 4, z), Blocks.dragon_egg); //tops it off
       BlockEndPortal.field_149948_a = false; //reset this field
   }
}
