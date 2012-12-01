package com.sijobe.spc.command;

import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Coordinate;
import com.sijobe.spc.wrapper.Player;

import java.util.List;


/**
 * Ignites the top of the block that the player is pointing at
 *
 * @author simo_415
 * @version 1.0
 */
@Command (
   name = "ignite",
   description = "Sets the top of the block the player is pointing at on fire",
   videoURL = "Currently unavailable",
   version = "1.0"
)
public class Ignite extends StandardCommand {

   /**
    * The block ID of fire
    */
   public static final int BLOCK_FIRE = 51;
   
   /**
    * @see com.sijobe.spc.wrapper.CommandBase#execute(com.sijobe.spc.wrapper.CommandSender, java.util.List)
    */
   @Override
   public void execute(CommandSender sender, List<?> params) throws CommandException {
      Player player = getSenderAsPlayer(sender);
      Coordinate block = player.trace(128);
      if (block == null) {
         throw new CommandException("No block within range.");
      }
      Coordinate fire = new Coordinate(block.getBlockX(), block.getBlockY() + 1, block.getBlockZ());
      if (player.getWorld().getBlockId(fire) == 0) {
         player.getWorld().setBlock(fire, BLOCK_FIRE);
      }
   }
}
