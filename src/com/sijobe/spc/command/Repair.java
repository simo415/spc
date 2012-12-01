package com.sijobe.spc.command;

import com.sijobe.spc.validation.Parameter;
import com.sijobe.spc.validation.ParameterString;
import com.sijobe.spc.validation.Parameters;
import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Item;
import com.sijobe.spc.wrapper.Player;

import java.util.List;

/**
 * Provides a method to repair the players current item or all items
 *
 * @author simo_415
 * @version 1.0
 */
@Command (
   name = "repair",
   description = "Repairs the currently selected item to full health",
   example = "all",
   videoURL = "http://www.youtube.com/watch?v=tPMu6096hu8"
)
public class Repair extends StandardCommand {

   /**
    * The size of the player inventory
    */
   public static final int INVENTORY_SIZE = 36;
   
   /**
    * Parameters of the command
    */
   private static final Parameters PARAMETERS = new Parameters ( 
      new Parameter[] {
         new ParameterString("[all]", true, new String[] {"all"})
      }
   );
   
   /**
    * @see com.sijobe.spc.wrapper.CommandBase#execute(com.sijobe.spc.wrapper.CommandSender, java.util.List)
    */
   @Override
   public void execute(CommandSender sender, List<?> params) throws CommandException {
      Player player = getSenderAsPlayer(sender);
      if (params.size() == 0) {
         Item.resetDamageOnItem(player, player.getCurrentSlot());
      } else {
         for (int i = 0; i < INVENTORY_SIZE; i++) {
            Item.resetDamageOnItem(player, i);
         }
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
