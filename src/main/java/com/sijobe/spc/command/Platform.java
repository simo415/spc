package com.sijobe.spc.command;

import com.sijobe.spc.wrapper.Block;
import com.sijobe.spc.wrapper.Blocks;
import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Coordinate;
import com.sijobe.spc.wrapper.Player;


import java.util.List;

/**
 * Creates a 1x1 glass platform under the players feet wherever they are on 
 * the map. Note that this doesn't work where the Y coordinate is outside of 
 * the build-able range
 *
 * @author simo_415 
 * @version 1.0
 * @status survived 1.7.2 update
 */
@Command (
   name = "platform",
   description = "Puts a one block glass platform beneath the players position",
   videoURL = "http://www.youtube.com/watch?v=KOoC3MsTanM",
   version = "1.0"
)
public class Platform extends StandardCommand {

   /**
    * The block ID of glass
    */
   public static final Block BLOCK_GLASS = Blocks.glass;
   
   /**
    * @see com.sijobe.spc.wrapper.CommandBase#execute(com.sijobe.spc.wrapper.CommandSender, java.util.List)
    */
   @Override
   public void execute(CommandSender sender, List<?> params) throws CommandException {
      Player player = super.getSenderAsPlayer(sender);
      Coordinate coord = new Coordinate(player.getPosition().getBlockX(), player.getPosition().getBlockY() - 1, player.getPosition().getBlockZ());
      player.getWorld().setBlock(coord, BLOCK_GLASS);
   }
}
