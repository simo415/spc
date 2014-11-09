package com.sijobe.spc.command;

import com.sijobe.spc.util.FontColour;
import com.sijobe.spc.validation.Parameter;
import com.sijobe.spc.validation.ParameterInteger;
import com.sijobe.spc.validation.Parameters;
import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.World;

import net.minecraft.world.EnumDifficulty;

import java.util.List;

/**
 * Changes the World's current difficulty setting
 *
 * @author simo_415
 * @version 1.0
 * @status broken through 1.7.2 update
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
      int change;
      if (params.size() > 0) {
    	  switch((Integer)params.get(0))
    	  {
    	  	case 0:
    	  		world.setDifficulty(EnumDifficulty.PEACEFUL);
    	  	case 1:
    	  		world.setDifficulty(EnumDifficulty.EASY);
    	  	case 2:
    	  		world.setDifficulty(EnumDifficulty.NORMAL);
    	  	case 3:
    	  		world.setDifficulty(EnumDifficulty.HARD);
    	  }
      } else {
         world.setDifficulty(EnumDifficulty.getDifficultyEnum((world.getDifficulty().getDifficultyId() + 1) % 4));
      }
      String difficulty = "";
      switch (world.getDifficulty()) {
         case PEACEFUL:
            difficulty = FontColour.GREEN + "peaceful";
            break;
         case EASY:
            difficulty = FontColour.YELLOW + "easy";
            break;
         case NORMAL:
            difficulty = FontColour.ORANGE + "normal";
            break;
         case HARD:
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
