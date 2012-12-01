package com.sijobe.spc.command;

import com.sijobe.spc.util.FontColour;
import com.sijobe.spc.validation.Parameter;
import com.sijobe.spc.validation.ParameterInteger;
import com.sijobe.spc.validation.Parameters;
import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.World;

import java.util.List;

/**
 * Changes the World's current difficulty setting
 *
 * @author simo_415
 * @version 1.0
 */
@Command (
   name = "difficulty",
   description = "Sets the difficulty of the game. Valid values 0-3",
   example = "0",
   videoURL = "http://www.youtube.com/watch?v=jkXYM8S41uY",
   version = "1.0",
   alias = {"diff"}
)
public class Difficulty extends StandardCommand {
   /**
    * The parameters of the command
    */
   private static final Parameters PARAMETERS = new Parameters (
      new Parameter[] {
         new ParameterInteger("[DIFFICULTY]", true)
      }
   );

   /**
    * @see com.sijobe.spc.wrapper.CommandBase#execute(com.sijobe.spc.wrapper.CommandSender, java.util.List)
    */
   @Override
   public void execute(CommandSender sender, List<?> params) throws CommandException {
      World world = super.getSenderAsPlayer(sender).getWorld();
      if (params.size() > 0) {
         world.setDifficulty((Integer)params.get(0));
      } else {
         world.setDifficulty((world.getDifficulty() + 1) % 4);
      }
      String difficulty = "";
      switch (world.getDifficulty()) {
         case 0:
            difficulty = FontColour.GREEN + "peaceful";
            break;
         case 1:
            difficulty = FontColour.YELLOW + "easy";
            break;
         case 2:
            difficulty = FontColour.ORANGE + "normal";
            break;
         case 3:
            difficulty = FontColour.RED + "hard";
            break;
      }
      sender.sendMessageToPlayer("Difficulty was set to " + difficulty);
   }

   /**
    * @see com.sijobe.spc.wrapper.CommandBase#getParameters()
    */
   @Override
   public Parameters getParameters() {
      return PARAMETERS;
   }
}
