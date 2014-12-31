package com.sijobe.spc.command;

import com.sijobe.spc.validation.Parameter;
import com.sijobe.spc.validation.ParameterPlayer;
import com.sijobe.spc.validation.ParameterString;
import com.sijobe.spc.validation.Parameters;
import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandManager;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Player;

import java.util.List;

/**
 * Command is for LAN mode to allow you to run a command as another player
 *
 * @author simo_415
 * @version 1.0
 * @status survived 1.7.2 update
 */
@Command (
   name = "sudo",
   description = "Allows you to run any command as the specified player",
   example = "simo_415 give wool:35 5",
   videoURL = ""
)
public class Sudo extends StandardCommand {

   /**
    * The parameters of the command
    */
   private static final Parameters PARAMETERS = new Parameters (
      new Parameter[] {
         new ParameterPlayer("<PLAYER_NAME>", false),
         new ParameterString("<COMMAND>", false),
         new ParameterString("{PARAMS}", true, true)
      }
   );
   
   @Override
   public void execute(CommandSender sender, List<?> params) throws CommandException {
      Player su = (Player)params.get(0);
      String command = (String)params.get(1);
      if (params.size() == 3) {
         command += " " + (String)params.get(2);
      }
      CommandManager.runCommand(new CommandSender(su), command);
   }
   
   /**
    * @see com.sijobe.spc.wrapper.CommandBase#getParameters()
    */
   @Override
   public Parameters getParameters() {
      return PARAMETERS;
   }
}
