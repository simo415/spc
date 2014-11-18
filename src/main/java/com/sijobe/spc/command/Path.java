package com.sijobe.spc.command;

import com.sijobe.spc.core.IPlayerMP;
import com.sijobe.spc.util.PathData;
import com.sijobe.spc.validation.Parameter;
import com.sijobe.spc.validation.ParameterInteger;
import com.sijobe.spc.validation.ParameterString;
import com.sijobe.spc.validation.Parameters;
import com.sijobe.spc.wrapper.Block;
import com.sijobe.spc.wrapper.Blocks;
import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Coordinate;
import com.sijobe.spc.wrapper.Item;
import com.sijobe.spc.wrapper.Player;

import java.util.HashMap;
import java.util.List;

import net.minecraft.util.MathHelper;

/**
 * Creates a path with the specified size.
 *
 * @author q3hardcore
 * @version 1.4
 * @status broken through 1.7.2 update -> fixed
 */
@Command (
   name = "path",
   description = "Creates a path",
   version = "1.4"
)

public class Path extends StandardCommand implements IPlayerMP {

   /**
    * The parameters of the command
    */
   private static final Parameters PARAMETERS = new Parameters (
      new Parameter[] {
         new ParameterString("[BLOCK]",true),
         new ParameterInteger("[RADIUS]",true)
      }
   );

   /**
    * A hashmap containing path settings for each player
    */
   private static HashMap<String, PathData> playerConfig = new HashMap<String, PathData>();

   /**
    * Name of the current world
   */
   private static String worldName = "";
   
   /**
    * @see com.sijobe.spc.wrapper.CommandBase#execute(com.sijobe.spc.wrapper.CommandSender, java.util.List)
    */
   @Override
   public void execute(CommandSender sender, List<?> params) throws CommandException {
      Player player = getSenderAsPlayer(sender);
      String playerName = player.getPlayerName();
      checkWorld(player);
      
      if (params.size() >= 1) {
         String args[] = ((String)params.get(0)).split("(\\^|:)");
         Block block;
         int size = 3;
         int meta = 0;
         
         try {
            block = Block.fromId(Integer.parseInt(args[0]));
         } catch (NumberFormatException e) {
            block = null;
         }
         
         if (block == null) {
            block = (Block) Block.blockRegistry.getObject(args[0]);
         }

         if (args[0].equalsIgnoreCase("air")) {
            block = null;
         }

         if (!player.getWorld().isValidBlockType(block)) { // isValidBlockType should be static
            throw new CommandException("Unknown block: " + args[0]);
         }
         
         if(args.length > 1) {
            try {
               meta = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
               sender.sendMessageToPlayer("Invalid metadata value specified, using default.");
               meta = 0;
            }
            if(meta < 0 || meta > 15) {
               sender.sendMessageToPlayer("Using default metadata value.");
               meta = 0;
            }
         }
         
         if (params.size() > 1) {
            size = (Integer)params.get(1);
            if(size < 1) {
               throw new CommandException("Size must be at least 1.");
            }
            if(size > 50) {
               sender.sendMessageToPlayer("Clamping path size.");
               size = 50;
            }
         }

         if(playerConfig.containsKey(playerName)) {
            PathData plrData = playerConfig.get(playerName);
            if(plrData.block == block && plrData.meta == meta && plrData.size == size) {
               throw new CommandException("Already making specified type of path!");
            }
         }
         
         sender.sendMessageToPlayer("Path mode enabled.");
         playerConfig.put(playerName, new PathData(block, meta, size));
      } else if(playerConfig.containsKey(playerName) && playerConfig.get(playerName).block != null) {
         sender.sendMessageToPlayer("Path mode disabled.");
         playerConfig.get(playerName).block = null;
      } else {
         throw new CommandException("Must specify block.");
      }

   }

   private void checkWorld(Player player) {
      String currentWorldName = player.getWorld().getName();
      if(!worldName.equals(currentWorldName)) {
         System.out.println("SPC: Clearing path settings.");
         playerConfig.clear();
         worldName = currentWorldName;
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
    * @see com.sijobe.spc.core.IHook#init(java.lang.Object[])
    */
   @Override
   public void init(Object... params) {
   }
   
   /**
    * @see com.sijobe.spc.core.IPlayerMP#onTick(com.sijobe.spc.wrapper.Player)
    */
   @Override
   public void onTick(Player player) {
      checkWorld(player);
      String playerName = player.getPlayerName();
      if (playerConfig.containsKey(playerName)) {
         PathData plrData = playerConfig.get(playerName);
         makePath(player, plrData);
      } else {
         return;
      }
   }
   

   /**
    * Check if block is valid, if so makes path
    * 
    * @param player - The player that is making a path
    * @param data - Path data for player
    */
   private void makePath(Player player, PathData data) {
      if (data.block != null) { // block
         Coordinate position = player.getPosition();
         int x = MathHelper.floor_double(position.getX());
         int y = MathHelper.floor_double(position.getY()); // used to take away 1
         int z = MathHelper.floor_double(position.getZ());
         if (x != data.prevx || y != data.prevy || z != data.prevz) { // prevx, prevy, prevz
            int start = data.size * -1 + 1; // size

            for (int i = start; i < data.size; i++) {
               for (int j = -1; j < data.size; j++) {
                  for (int k = start; k < data.size; k++) {
                     if (j == -1) {
                        this.setBlock(player, x + i, y + j, z + k, data.block, data.meta);
                     } else {
                        this.setBlock(player, x + i, y + j, z + k, Blocks.air, 0);
                     }
                  }
               }
            }

            data.prevx = x;
            data.prevy = y;
            data.prevz = z;
         }
      }
   }


   /**
    * Sets block at specified position to specified type
    * This function should be re-written to be parsed a World object
    * Additionally, SPC's World class needs setBlockWithNotify added
    * 
    * @param player - The player whose world we are setting a block in
    * @param i - The X position of the block
    * @param j - The Y position of the block
    * @param k - The Z position of the block
    * @param type - The type (ID) of the block
    */
   private void setBlock(Player player, int i, int j, int k, Block type, int meta) {
      player.getWorld().setBlockDataWithMeta(new Coordinate(i, j, k), type, meta);
   }
}
