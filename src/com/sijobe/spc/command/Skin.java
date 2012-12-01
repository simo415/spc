package com.sijobe.spc.command;

import com.sijobe.spc.validation.Parameter;
import com.sijobe.spc.validation.ParameterString;
import com.sijobe.spc.validation.Parameters;
import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandSender;

import java.util.List;

/**
 * Provides the skin command that changes the players skin to another player 
 * name's skin
 *
 * @author simo_415
 * @version 1.0
 */
@Command (
   name = "skin",
   description = "Changes the players skin to the one specified",
   example = "simo_415",
   videoURL = "http://www.youtube.com/watch?v=t46QHrttza0",
   version = "1.0",
   enabled = false
)
public class Skin extends StandardCommand {
   /**
    * The URL to download the skin from
    */
   public static final String URL_PREFIX = "http://s3.amazonaws.com/MinecraftSkins/";
   /**
    * The extension of the image, currently ".png"
    */
   public static final String URL_POSTFIX = ".png";

   /**
    * The parameters of the command
    */
   private static final Parameters PARAMETERS = new Parameters (
      new Parameter[] {
         new ParameterString("<PLAYER_NAME>", false),
      }
   );

   /**
    * @see com.sijobe.spc.wrapper.CommandBase#execute(com.sijobe.spc.wrapper.CommandSender, java.util.List)
    */
   @Override
   public void execute(CommandSender sender, List<?> params) throws CommandException {
      super.getSenderAsPlayer(sender).setSkin(URL_PREFIX + (String)params.get(0) + URL_POSTFIX);
   }

   /**
    * @see com.sijobe.spc.wrapper.CommandBase#getParameters()
    */
   @Override
   public Parameters getParameters() {
      return PARAMETERS;
   }
}
