package com.sijobe.spc.command;

import com.sijobe.spc.util.FontColour;
import com.sijobe.spc.validation.Parameter;
import com.sijobe.spc.validation.ParameterInteger;
import com.sijobe.spc.validation.ParameterString;
import com.sijobe.spc.validation.Parameters;
import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Item;

import java.util.List;

/**
 * The enchant command adds the specified enchantment to the currently selected
 * item in the players inventory
 *
 * @author simo_415
 * @version 1.0
 * @status survived 1.7.2 update
 */
@Command (
   name = "enchant",
   description = "Enchants the currently selected item",
   example = "add protection 10",
   videoURL = "http://www.youtube.com/watch?v=zd9mPAU5TG8",
   version = "1.0"
)
public class Enchant extends StandardCommand {

   /**
    * The parameters of the command
    */
   private static final Parameters PARAMETERS = new Parameters (
      new Parameter[] {
         new ParameterString("<list|remove|add <TYPE> [LEVEL]>", false, new String[] {"list","add","remove"}),
         new ParameterString("", true),
         new ParameterInteger("", true)
      }
   );

   /**
    * @see com.sijobe.spc.wrapper.CommandBase#execute(com.sijobe.spc.wrapper.CommandSender, java.util.List)
    */
   @Override
   public void execute(CommandSender sender, List<?> params) throws CommandException {
      String argument = (String)params.get(0);
      if (argument.equalsIgnoreCase("list")) {
         String list = "";
         for (String i : Item.getEnchantments()) {
            if (i != null) {
               list += i + " (" + getEnchantmentId(i) + "), ";
            }
         }
         list = list.substring(0, list.length() - 2);
         sender.sendMessageToPlayer(FontColour.GREEN + "Enchantments [name (id)]:");
         sender.sendMessageToPlayer(list);
      } else if (argument.equalsIgnoreCase("add")) {
         if (params.size() == 1) {
            throw new CommandException(getUsage(sender));
         }
         int id = -1;
         try {
            id = Integer.parseInt((String)params.get(1));
         } catch (Exception e) {
            id = getEnchantmentId((String)params.get(1));
         }
         if (id < 0) {
            throw new CommandException("Invalid enchantment specified.");
         }
         int level = 1;
         if (params.size() == 3) {
            level = (Integer)params.get(2);
         }
         String name = null;
         if ((name = Item.addEnchantmentToCurrentItem(getSenderAsPlayer(sender), id, level)) == null) {
            throw new CommandException("Unable to add the specified enchantment with ID " + id);
         }
         getSenderAsPlayer(sender).sendChatMessage("The " + FontColour.AQUA + 
                  name + FontColour.WHITE + " command was successfully added.");
      } else {
         Item.removeEnchantmentsOnCurrentItem(getSenderAsPlayer(sender));
         getSenderAsPlayer(sender).sendChatMessage("Enchantment(s) for the current item were removed");
      }
   }
   
   /**
    * Gets the ID of the enchantment specified
    * 
    * @param enchantment - The enchantment name
    * @return The ID of the enchantment, or -1 if not found
    */
   public int getEnchantmentId(String enchantment) {
      return Item.getEnchantments().indexOf(enchantment);
   }

   /**
    * @see com.sijobe.spc.wrapper.CommandBase#getParameters()
    */
   @Override
   public Parameters getParameters() {
      return PARAMETERS;
   }
}
