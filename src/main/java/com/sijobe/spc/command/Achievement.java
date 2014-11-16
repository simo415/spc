package com.sijobe.spc.command;

import com.sijobe.spc.util.FontColour;
import com.sijobe.spc.validation.Parameter;
import com.sijobe.spc.validation.ParameterString;
import com.sijobe.spc.validation.Parameters;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Minecraft;
import com.sijobe.spc.wrapper.Player;
import com.sijobe.spc.wrapper.Stats;

import java.util.List;

/**
 * Gives achievements to the player
 *
 * TODO: Achievement isn't triggered correctly for some reason
 *
 * @author simo_415
 * @version 1.0
 */
@Command (
   name = "achievement",
   description = "Allows you to list or unlock all achievements",
   videoURL = "http://www.youtube.com/watch?v=aLS0OmVt0ac",
   version = "1.4.6",
   enabled = false
)
public class Achievement extends StandardCommand {
   
   /**
    * Parameters of the command
    */
   private static final Parameters PARAMETERS = new Parameters (
      new Parameter[] {
         new ParameterString("<list|unlock [NAME]>", false, new String[] {"list", "unlock"}),
         new ParameterString("", true)
      }
   );

   @Override
   public void execute(CommandSender sender, List<?> params) {
      if (((String)params.get(0)).equalsIgnoreCase("list")) {
         String achievements = "";
         for (String name : Stats.getAchievementNames()) {
            achievements += name + ", ";
         }
         achievements = achievements.substring(0, achievements.length() - 2);
         sender.sendMessageToPlayer(achievements);
      } else if (((String)params.get(0)).equalsIgnoreCase("unlock")) {
         Player player = Minecraft.getPlayer();
         if (params.size() == 2) {
            if (player.addAchievement((String)params.get(1))) {
               player.sendChatMessage("The " + FontColour.AQUA + (String)params.get(1) 
                        + FontColour.WHITE + " achievement was unlocked.");
            }
         } else {
            for (String name : Stats.getAchievementNames()) {
               player.addAchievement(name);
            }
         }
      } else {
         assert false : "Unknown achievement argument provided.";
      }
      
   }

   @Override
   public Parameters getParameters() {
      return PARAMETERS;
   }
   
   @Override
   public boolean isEnabled() {
      return Minecraft.isSinglePlayer();
   }
}
