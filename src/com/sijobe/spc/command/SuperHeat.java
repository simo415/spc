package com.sijobe.spc.command;

import com.sijobe.spc.validation.Parameter;
import com.sijobe.spc.validation.ParameterString;
import com.sijobe.spc.validation.Parameters;
import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Player;

import java.util.List;
import java.util.Map;

import net.minecraft.src.FurnaceRecipes;
import net.minecraft.src.ItemStack;

/**
 * Cooks player's current item or all items
 *
 * @author q3hardcore
 * @version 1.0
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
         int key = oldStack.itemID;
         if (smelt.containsKey(key) && smelt.get(key) != null) {
            int amt = oldStack.stackSize;
            ItemStack temp = smelt.get(key);
            int id = temp.itemID;
            int damage = temp.getItemDamage();
            ItemStack item = new ItemStack(id, amt, damage);
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
