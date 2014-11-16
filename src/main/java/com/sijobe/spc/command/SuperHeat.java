package com.sijobe.spc.command;

import com.sijobe.spc.validation.Parameter;
import com.sijobe.spc.validation.ParameterString;
import com.sijobe.spc.validation.Parameters;
import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Item;
import com.sijobe.spc.wrapper.Player;

import java.util.List;
import java.util.Map;

import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.ItemStack;

/**
 * Cooks player's current item or all items
 *
 * @author q3hardcore
 * @version 1.0
 * @status broken through 1.7.2 update -> fixed
 */
@Command (
         name = "superheat",
         description = "Cooks items where possible",
         example = "all",
         videoURL = ""
)
public class SuperHeat extends StandardCommand {

   /**
    * The parameters of the command
    */
   private static final Parameters PARAMETERS = new Parameters (
            new Parameter[] {
                     new ParameterString("[all]", true, new String[] {"all"})
            }
   );

   @Override
   public void execute(CommandSender sender, List<?> params) throws CommandException {
      @SuppressWarnings("unchecked")
      Map<Integer, ItemStack> smelt = FurnaceRecipes.smelting().getSmeltingList();

      Player player = getSenderAsPlayer(sender);
      boolean all = false;

      if (params.size() != 0) {
         all = true;
      }

      ItemStack[] mainInventory = player.getMinecraftPlayer().inventory.mainInventory;

      int length = all ? mainInventory.length : 1;
      int start = all ? 0 : player.getCurrentSlot(); // currentItem

      for (int i = start; i < start + length; i++) {
         ItemStack oldStack = mainInventory[i];
         if (oldStack == null) {
            continue;
         }
         
         net.minecraft.item.Item key = oldStack.getItem();
         ItemStack newStack = FurnaceRecipes.smelting().getSmeltingResult(oldStack);
         if (newStack != null) {
            int amt = oldStack.stackSize;
            int damage = newStack.getItemDamage();
            ItemStack item = new ItemStack(newStack.getItem(), amt, damage);
            mainInventory[i] = item;
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
