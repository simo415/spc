package com.sijobe.spc.command;

import com.sijobe.spc.core.IPlayerMP;
import com.sijobe.spc.validation.Parameter;
import com.sijobe.spc.validation.ParameterInteger;
import com.sijobe.spc.validation.ParameterString;
import com.sijobe.spc.validation.Parameters;
import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Coordinate;
import com.sijobe.spc.wrapper.Item;
import com.sijobe.spc.wrapper.Player;

import java.util.HashMap;
import java.util.List;

import net.minecraft.src.MathHelper;

/**
 * Creates a path with the specified size.
 *
 * @author q3hardcore
 * @version 1.4
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
   private static HashMap<String, int[]> playerConfig = new HashMap<String, int[]>();

   /**
    * @see com.sijobe.spc.wrapper.CommandBase#execute(com.sijobe.spc.wrapper.CommandSender, java.util.List)
    */
   @Override
   public void execute(CommandSender sender, List<?> params) throws CommandException {
      Player player = getSenderAsPlayer(sender);
      String playerName = player.getPlayerName();
      
      if (params.size() >= 1) {
         String args[] = ((String)params.get(0)).split("(\\^|:)");
         int block = -1;
         int size = 3;

         try {
            block = Integer.parseInt(args[0]);
         } catch (NumberFormatException e) {
            block = -1;
         }
         
         if (block == -1) {
            block = Item.getItemId((args[0]).replace('_', ' '));
         }

         if (args[0].equalsIgnoreCase("air")) {
            block = 0;
         }

         if (!player.getWorld().isValidBlockType(block) && block != 0) { // isValidBlockType should be static
            throw new CommandException("Unknown block: " + args[0]);
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

         sender.sendMessageToPlayer("Path mode enabled.");
         playerConfig.put(playerName, new int[]{block, size, -1, -1, -1});
      } else if(playerConfig.containsKey(playerName) && playerConfig.get(playerName)[0] > -1) {
         sender.sendMessageToPlayer("Path mode disabled.");
         playerConfig.get(playerName)[0] = -1;
      } else {
         throw new CommandException("Must specify block.");
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
      String playerName = player.getPlayerName();
      if (playerConfig.containsKey(playerName)) {
         int[] plrData = playerConfig.get(playerName);
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
   private void makePath(Player player, int[] data) {
      if (data[0] >= 0) { // block
         Coordinate position = player.getPosition();
         int x = MathHelper.floor_double(position.getX());
         int y = MathHelper.floor_double(position.getY()); // used to take away 1
         int z = MathHelper.floor_double(position.getZ());
         if (x != data[2] || y != data[3] || z != data[4]) { // prevx, prevy, prevz
            int start = data[1] * -1 + 1; // size

            for (int i = start; i < data[1]; i++) {
               for (int j = -1; j < data[1]; j++) {
                  for (int k = start; k < data[1]; k++) {
                     if (j == -1) {
                        this.setBlock(player, x + i, y + j, z + k, data[0]);
                     } else {
                        this.setBlock(player, x + i, y + j, z + k, 0);
                     }
                  }
               }
            }

            data[2] = x;
            data[3] = y;
            data[4] = z;
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
   private void setBlock(Player player, int i, int j, int k, int type) {
      //player.getWorld().getMinecraftWorld().func_94575_c(i, j, k, type);
      player.getWorld().setBlock(new Coordinate(i, j, k), type);
   }
}
