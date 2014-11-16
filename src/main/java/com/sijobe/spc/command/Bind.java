package com.sijobe.spc.command;

import com.sijobe.spc.core.Constants;
import com.sijobe.spc.util.FontColour;
import com.sijobe.spc.util.KeyListener;
import com.sijobe.spc.util.KeyboardHandler;
import com.sijobe.spc.util.Settings;
import com.sijobe.spc.validation.Parameter;
import com.sijobe.spc.validation.ParameterInteger;
import com.sijobe.spc.validation.ParameterString;
import com.sijobe.spc.validation.Parameters;
import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandManager;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Minecraft;
import com.sijobe.spc.wrapper.MinecraftServer;
import com.sijobe.spc.wrapper.Player;

import java.io.File;
import java.util.List;

import org.lwjgl.input.Keyboard;

/**
 * Handles the bindings that the player has assigned to keys
 *
 * @author simo_415
 * @version 1.1
 */
public class Bind extends MultipleCommands implements KeyListener {

   /**
    * The bindings that the player uses
    */
   private static Settings BINDINGS = new Settings(new File(Constants.MOD_DIR, "bindings.properties"));

   /**
    * The instance that all key bindings are associated against
    */
   private static Bind INSTANCE = new Bind("");
   
   // Adds all the binding listeners
   static {
      INSTANCE.addKeyListeners();
   }
   
   /**
    * The parameters for the bind command
    */
   private static final Parameters BIND_PARAMS = new Parameters (
            new Parameter[] {
                     new ParameterString("<KEYCODE>", false),
                     new ParameterString("<COMMAND>", false),
                     new ParameterString("{PARAMETERS}", true, true)
            }
   );

   /**
    * The parameters for the bindid command
    */
   private static final Parameters BINDID_PARAMS = new Parameters (
            new Parameter[] {
                     new ParameterInteger("<KEYID>", false),
                     new ParameterString("<COMMAND>", false),
                     new ParameterString("{PARAMETERS}", true, true)
            }
   );

   /**
    * The parameters for the unbind command
    */
   private static final Parameters UNBIND_PARAMS = new Parameters (
            new Parameter[] {
                     new ParameterString("<KEYCODE|all>", false),
            }
   );

   /**
    * Initialises the instance using the specified command name
    * 
    * @param name - The name of the command
    */
   public Bind(String name) {
      super(name);
   }
   
   /**
    * Adds all the binding listeners to the key listener
    */
   private void addKeyListeners() {
      for (Object key : BINDINGS.keySet()) {
         try {
            bind(Integer.parseInt((String)key));
         } catch (Exception e) {
            System.err.println("Invalid key specified in bindings: " + key);
            BINDINGS.remove(key);
         }
      }
   }

   /**
    * The bind commands are only enabled in single player mode since keyboard
    * handling can only currently be done locally as key presses don't get sent
    * to the server. 
    * 
    * @see com.sijobe.spc.wrapper.CommandBase#isEnabled()
    */
   @Override
   public boolean isEnabled() {
      return Minecraft.isSinglePlayer();
   }

   /**
    * @see com.sijobe.spc.command.MultipleCommands#getCommands()
    */
   @Override
   public String[] getCommands() {
      return new String[] {"bind", "unbind", "bindid", "unbindid"};
   }

   /**
    * @see com.sijobe.spc.wrapper.CommandBase#execute(com.sijobe.spc.wrapper.CommandSender, java.util.List)
    */
   @Override
   public void execute(CommandSender sender, List<?> params) throws CommandException {
      if (getName().equalsIgnoreCase("bind") || getName().equalsIgnoreCase("bindid")) {
         int keycode = Keyboard.KEY_NONE;
         if (getName().equalsIgnoreCase("bind")) {
            String key = (String)params.get(0);
            keycode = Keyboard.getKeyIndex(key.toUpperCase());
         } else {
            keycode = (Integer)params.get(0);
         }
         if (keycode == Keyboard.KEY_NONE) {
            throw new CommandException("Unknown keycode " + params.get(0));
         }
         String command = (String)params.get(1);
         String param = "";
         if (params.size() > 2) {
            param = (String)params.get(2);
         }
         if (BINDINGS.get(keycode + "") == null) {
            bind(keycode);
         }
         BINDINGS.set(keycode + "", command + " " + param);
         BINDINGS.save();
         sender.sendMessageToPlayer("Key " + FontColour.AQUA + params.get(0) + 
                  FontColour.WHITE + " was successfully bound.");
      } else if (getName().equalsIgnoreCase("unbind") || getName().equalsIgnoreCase("unbindid")) {
         int keycode = Keyboard.KEY_NONE;
         if (getName().equalsIgnoreCase("unbind")) {
            String key = (String)params.get(0);
            if (key.equalsIgnoreCase("all")) {
               BINDINGS.clear();
               BINDINGS.save();
               sender.sendMessageToPlayer("All bindings removed.");
               return;
            }
            keycode = Keyboard.getKeyIndex(key.toUpperCase());
         } else {
            keycode = (Integer)params.get(0);
         }
         if (keycode == Keyboard.KEY_NONE) {
            throw new CommandException("Unknown keycode " + params.get(0));
         }
         unbind(keycode);
         if (BINDINGS.remove(keycode + "") == null) {
            throw new CommandException("No binding was found for key " + FontColour.AQUA + (String)params.get(0));
         } else {
            BINDINGS.save();
            sender.sendMessageToPlayer("Binding " + FontColour.AQUA + params.get(0) + 
                     FontColour.WHITE + " was successfully removed.");
         }
      } else {
         assert false : "Invalid command " + getName();
      }
   }

   /**
    * Binds a key to a listener
    * 
    * @param key - The key to bind
    */
   private void bind(int key) {
      if (!KeyboardHandler.getInstance().addKeyPressedListener(key, INSTANCE)) {
         System.err.println("Invalid key specified in bindings: " + key);
         BINDINGS.remove(key);
      }
   }
   
   /**
    * Unbinds the key from the listener
    * 
    * @param key - The key to unbind
    */
   private void unbind(int key) {
      KeyboardHandler.getInstance().removeKeyPressedListener(key, INSTANCE);
   }

   @Override
   public Parameters getParameters() {
      if (getName().equalsIgnoreCase("bind")) {
         return BIND_PARAMS;
      } else if (getName().equalsIgnoreCase("unbind")) {
         return UNBIND_PARAMS;
      } else if (getName().equalsIgnoreCase("bindid")) {
         return BINDID_PARAMS;
      } else {
         assert false : "Invalid command name initialsied " + getName();
      return Parameters.DEFAULT;
      }
   }

   /**
    * Handles binding events
    * 
    * @see com.sijobe.spc.util.KeyListener#keyPressed(int)
    */
   @Override
   public void keyPressed(int key) {
      if (Minecraft.isGuiScreenOpen()) {
         return;
      }
      Player player = Minecraft.getPlayer();
      if (player != null) {
         CommandSender sender = new CommandSender(MinecraftServer.getPlayerByUsername(player.getPlayerName()));
         CommandManager.runCommand(sender, BINDINGS.getString(key + "", ""));
      }
   }

   /**
    * @see com.sijobe.spc.util.KeyListener#keyReleased(int)
    */
   @Override
   public void keyReleased(int key) {
      // Not required
   }
}
