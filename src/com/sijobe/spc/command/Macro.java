package com.sijobe.spc.command;

import com.sijobe.spc.core.Constants;
import com.sijobe.spc.validation.Parameter;
import com.sijobe.spc.validation.ParameterString;
import com.sijobe.spc.validation.Parameters;
import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandManager;
import com.sijobe.spc.wrapper.CommandSender;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

/**
 * Macro command allows you to write a file with a list of commands in it then
 * run the file.
 *
 * @author simo_415
 * @version 1.0
 */
@Command (
   name = "macro",
   description = "Macro based commands allow multiple commands to be run",
   example = "test",
   videoURL = "http://www.youtube.com/watch?v=hkQfslQJoQs",
   version = "1.0"
)
public class Macro extends StandardCommand {

   /**
    * The file extension of macro files
    */
   public static final String MACRO_EXTENSION = ".txt";
   /**
    * The directory that the macro files and retrieved from
    */
   public static final File MACRO_DIR = new File(Constants.MOD_DIR, "macros");
   static {
      if (!MACRO_DIR.exists()) {
         MACRO_DIR.mkdirs();
      }
   }

   /**
    * Parameters of the command
    */
   private static final Parameters PARAMETERS = new Parameters (
            new Parameter[] {
                     new ParameterString("<FILE>", false),
                     new ParameterString("{PARAMETERS}", true, true)
            }
   );

   /**
    * @see com.sijobe.spc.wrapper.CommandBase#execute(com.sijobe.spc.wrapper.CommandSender, java.util.List)
    */
   @Override
   public void execute(CommandSender sender, List<?> params) throws CommandException {
      File macro = new File(MACRO_DIR, (String)params.get(0) + MACRO_EXTENSION);
      if (!macro.exists()) {
         throw new CommandException("Specified macro does not exist.");
      }

      try {
         BufferedReader br = new BufferedReader(new FileReader(macro));
         String split[] = null;
         if (params.size() == 1) {
            split = new String[] { (String)params.get(0) };
         } else {
            split = (((String)params.get(0)) + " " + ((String)params.get(1))).split(" ");
         }
         String line = null;
         while ((line = br.readLine()) != null) {
            // Adds arguments to the line
            for (int i = 0; i < split.length; i++) {
               line = line.replaceAll("\\$_" + i, split[i]);
            }

            // Remove all unspecified arguments from line
            line = line.replaceAll("\\$_[0-9]+", "");

            // Executes the line
            CommandManager.runCommand(sender, line);
         }
      } catch (Exception e) {
         throw new CommandException(e);
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
