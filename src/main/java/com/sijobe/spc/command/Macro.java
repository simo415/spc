package com.sijobe.spc.command;

import com.sijobe.spc.core.Constants;
import com.sijobe.spc.util.Settings;
import com.sijobe.spc.validation.Parameter;
import com.sijobe.spc.validation.ParameterString;
import com.sijobe.spc.validation.Parameters;
import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandManager;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Macro command allows you to write a file with a list of commands in it then
 * run the file.
 * 
 * @author simo_415
 * @version 1.0
 * @status survived 1.7.2 update
 */
@Command(name = "macro", description = "Macro based commands allow multiple commands to be run", example = "test", videoURL = "http://www.youtube.com/watch?v=hkQfslQJoQs", version = "1.0")
public class Macro extends MultipleCommands {
   /**
    * The prefix in a player's settings for player specific macros
    */
   public static final String SETTINGS_PREFIX = "macro-";
   
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
    * Parameters of the macro command
    */
   private static final Parameters MACRO_PARAMS = new Parameters(
         new Parameter[] { new ParameterString("<FILE>", false),
               new ParameterString("{PARAMETERS}", true, true) });
   
   /**
    * Parameters of the setmacro command
    */
   private static final Parameters SETMACRO_PARAMS = new Parameters(
         new Parameter[] { new ParameterString("<NAME>", false),
               new ParameterString("{COMMANDS}", true, true) });
   
   /**
    * Parameters of the remove macro command
    */
   private static final Parameters REMOVEMACRO_PARAMS = new Parameters(
         new Parameter[] { new ParameterString("<NAME|ALL>", false)
         });
   
   public Macro(String name) {
      super(name);
   }
   
   /**
    * @see com.sijobe.spc.wrapper.CommandBase#execute(com.sijobe.spc.wrapper.CommandSender,
    *      java.util.List)
    */
   @Override
   public void execute(CommandSender sender, List<?> params)
         throws CommandException {
      
      if(getName().equalsIgnoreCase("macro")) {
         String macro;
         try {
            macro = getMacro((String) params.get(0), sender);
         } catch (IOException e) {
            throw new CommandException("Unable to opening file.");
         }
         
         String split[] = null;
         if (params.size() == 1) {
            split = new String[] { (String) params.get(0) };
         } else {
            split = (((String) params.get(0)) + " " + ((String) params.get(1)))
                  .split(" ");
         }
         
         String[] lines = macro.replace("\r", "\n").split("\n");
         for (String line : lines) {
            if(line.equals("")) {
               continue;
            }
            // Adds arguments to the line
            for (int i = 0; i < split.length; i++) {
               line = line.replaceAll("\\$_" + i, split[i]);
            }
            
            // Remove all unspecified arguments from line
            line = line.replaceAll("\\$_[0-9]+", "");
            
            // Executes the line
            CommandManager.runCommand(sender, line);
         }
      } else if(getName().equalsIgnoreCase("setmacro")) {
         Player player = getSenderAsPlayer(sender);
         if(player == null) {
            throw new CommandException("sender must be player");
         }
         Settings settings = loadSettings(player);
         settings.set(SETTINGS_PREFIX + (String)params.get(0), ((String) params.get(1)).replace("/", "\n\r"));
      } else if(getName().equalsIgnoreCase("removemacro")) {
         String macro = (String) params.get(0);
         Player player = getSenderAsPlayer(sender);
         if(player == null) {
            throw new CommandException("sender must be player");
         }
         Settings settings = loadSettings(player);
         if(macro.equals("all")) {
            for(Object i : settings.keySet().toArray()) {
               if(i instanceof String && ((String) i).startsWith(SETTINGS_PREFIX)) {
                  settings.remove(i);
               }
            }
            settings.save();
            sender.sendMessageToPlayer("All macros removed.");
            return;
         }
         settings.remove(SETTINGS_PREFIX + macro);
         sender.sendMessageToPlayer("Macro " + macro + " removed.");
      } else {
         assert false : "Invalid command " + getName();
      }
   }
   
   /**
    * gets the macro file for a given macro name
    * 
    * @param name
    *            - the name of the macro (ie. passed through the chat like
    *            '/macro test')
    * @param sender
    *            - the sender who sent the command
    * @return the macro contents (ie the contents of the macro file)
    * @throws CommandException
    * @throws IOException
    * */
   public String getMacro(String name, CommandSender sender)
         throws CommandException, IOException {
      Player player = getSenderAsPlayer(sender);
      if (player != null) {
         Settings settings = loadSettings(player);
         String key = SETTINGS_PREFIX + name;
         if (settings.containsKey(key)) {
            return settings.getProperty(key);
         }
      }
      File file = new File(MACRO_DIR, name + MACRO_EXTENSION);
      FileInputStream reader;
      try {
         reader = new FileInputStream(file);
      } catch (FileNotFoundException error) {
         throw new CommandException("Specified macro does not exist.");
      }
      byte[] data = new byte[(int) file.length()];
      reader.read(data);
      reader.close();
      return new String(data);
   }
   
   /**
    * @see com.sijobe.spc.wrapper.CommandBase#getParameters()
    */
   @Override
   public Parameters getParameters() {
      if (getName().equalsIgnoreCase("macro")) {
         return MACRO_PARAMS;
      } else if (getName().equalsIgnoreCase("setmacro")) {
         return SETMACRO_PARAMS;
      } else if (getName().equalsIgnoreCase("removemacro")) {
         return REMOVEMACRO_PARAMS;
      } else {
         assert false : "Invalid command name initialsied " + getName();
      return Parameters.DEFAULT;
      }
   }
   
   @Override
   public String[] getCommands() {
      return new String[]{"macro", "setmacro", "removemacro"};
   }
}
