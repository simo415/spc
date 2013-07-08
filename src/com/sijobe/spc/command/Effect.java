package com.sijobe.spc.command;

import com.sijobe.spc.util.FontColour;
import com.sijobe.spc.validation.Parameter;
import com.sijobe.spc.validation.ParameterString;
import com.sijobe.spc.validation.Parameters;
import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Player;
import com.sijobe.spc.wrapper.Potion;

import java.util.List;
import java.util.Map;

/**
 * Adds effects to the player
 *
 * @author simo_415
 * @version 1.0
 */
@Command (
         name = "effect",
         description = "Configures potion effects on the player",
         videoURL = "http://www.youtube.com/watch?v=mstqLPVBlwE",
         version = "1.4.6"
)
public class Effect extends StandardCommand {

   private static final Parameters PARAMETERS = new Parameters (
            new Parameter[] {
                     new ParameterString("[list|remove <all|TYPE>|add <all|TYPE> [DURATION] [STRENGTH]]", false,
                              new String[] {"list", "add", "remove"}),
                              new ParameterString("", true, true)
            }
   );

   @Override
   public void execute(CommandSender sender, List<?> params) throws CommandException {
      // Lists the potion effects
      if (((String)params.get(0)).equalsIgnoreCase("list")) {
         String potions = "";
         Map<String, Integer> pots = Potion.getPotions();
         for (String name : pots.keySet()) {
            potions += name + " (" + FontColour.AQUA + pots.get(name) + FontColour.WHITE + "), ";
         }
         potions = potions.substring(0, potions.length() - 2);
         sender.sendMessageToPlayer("Potion effects [name (" + FontColour.AQUA + "ID" + FontColour.WHITE + ")]: ");
         sender.sendMessageToPlayer(potions);
      }
      if (params.size() < 2) {
         throw new CommandException("Not enough parameters.");
      }
      // Gets the specified potion
      String args[] = ((String)params.get(1)).split(" ");
      Integer id = null;
      try {
         id = Integer.parseInt(args[0]);
      } catch (NumberFormatException e) {
         id = Potion.getPotions().get(args[0].toLowerCase());
      }
      if (id == null && !(args[0].equalsIgnoreCase("all"))) {
         throw new CommandException("Could not find specified effect.");
      }
      if (id != null && id < 1) {
         throw new CommandException("Invalid effect specified.");
      }
      Player player = super.getSenderAsPlayer(sender);
      // Removes the specified effect
      if (((String)params.get(0)).equalsIgnoreCase("remove")) {
         if (id == null) {
            player.removeAllPotionEffects();
         } else {
            player.removePotionEffect(id);
         }
         // Adds the specified effect
      } else if (((String)params.get(0)).equalsIgnoreCase("add")) {
         int duration = 1;
         int strength = 1;
         if (args.length > 1) {
            try {
               duration = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
               throw new CommandException("Could not parse duration argument.");
            }
         }
         if (args.length > 2) {
            try {
               strength = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
               throw new CommandException("Could not parse strength argument.");
            }
         }
         if (id == null) {
            for (Integer i : Potion.getPotions().values()) {
               player.addPotionEffect(i, duration * 20, strength);
            }
         } else {
            player.addPotionEffect(id, duration * 20, strength);
         }
      } else {
         throw new CommandException("Invalid argument specified.");
      }
   }

   @Override
   public Parameters getParameters() {
      return PARAMETERS;
   }
}
