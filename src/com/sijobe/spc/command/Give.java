package com.sijobe.spc.command;

import com.sijobe.spc.validation.Parameter;
import com.sijobe.spc.validation.ParameterInteger;
import com.sijobe.spc.validation.ParameterString;
import com.sijobe.spc.validation.Parameters;
import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Item;

import java.util.List;

/**
 * The give command gives the player the item requested
 *
 * @author simo_415
 * @version 1.0
 * @status broken through 1.7.2 update
 */
@Command (
   name = "give",
   description = "Gives player item, if quantity isn’t specified maximum amount of that item",
   example = "wool:5 32",
   videoURL = "http://www.youtube.com/watch?v=tPeyVQI8RMg",
   version = "1.0",
   alias = {"i", "item"}
)
public class Give extends StandardCommand {
   
   /**
    * The parameters of the command
    */
   private static final Parameters PARAMETERS = new Parameters (
      new Parameter[] {
         new ParameterString("<ITEMCODE|ITEMNAME>", false),
         new ParameterInteger("[QUANTITY]", true),
         new ParameterInteger("[DAMAGE]", true)
      }
   );

   /**
    * @throws CommandException When the damage value or item could not be determined
    * @see com.sijobe.spc.wrapper.CommandBase#execute(net.minecraft.src.ICommandSender, java.util.List)
    */
   @Override
   public void execute(CommandSender sender, List<?> params) throws CommandException {
      // Check for colon damage modifier
      int damage = 0;
      String split[] = ((String)params.get(0)).split("(\\^|:)");
      if (split.length == 2) {
         try {
            damage = Integer.parseInt(split[1]);
         } catch (NumberFormatException e) {
            throw new CommandException("Could not parse damage value of " + split[1]);
         }
      }
      
      // Get the item
      Item code = null;
      code = Item.getItem((split[0]));
      if (!Item.isValidItem(code)) {
         throw new CommandException("Cannot find specified item: " + split[0]);
      }
      
      // Get the quantity and damage and give player the item
      int quantity = params.size() > 1 ? (Integer)params.get(1) : 0;
      damage = params.size() > 2 ? (Integer)params.get(2) : damage;
      if (quantity == 0) {
         super.getSenderAsPlayer(sender).givePlayerItemWithDrop(code);
      } else if (damage == 0) {
         super.getSenderAsPlayer(sender).givePlayerItem(code, quantity);
      } else {
         super.getSenderAsPlayer(sender).givePlayerItem(code, quantity, damage);
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
