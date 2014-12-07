package com.sijobe.spc.command;

import com.sijobe.spc.ModSpc;
import com.sijobe.spc.network.Config;
import com.sijobe.spc.network.IClientConfig;
import com.sijobe.spc.network.PacketConfig;
import com.sijobe.spc.util.FontColour;
import com.sijobe.spc.util.KeyListener;
import com.sijobe.spc.util.KeyboardHandler;
import com.sijobe.spc.util.Settings;
import com.sijobe.spc.validation.Parameter;
import com.sijobe.spc.validation.ParameterInteger;
import com.sijobe.spc.validation.ParameterString;
import com.sijobe.spc.validation.Parameters;
import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Player;

import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.entity.player.EntityPlayerMP;

/**
 * Handles the bindings that the player has assigned to keys
 *
 * @author simo_415
 * @version 1.1
 */
public class Bind extends MultipleCommands implements KeyListener, IClientConfig<Integer> {
   public static final String SETTINGS_PREFIX = "keybinding-";
   public static final String KEYPRESS_COMMAND = "/keypress ";
   
   /**
    * The instance that all key bindings are associated against
    */
   private static Bind INSTANCE = new Bind("");
   
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
    * The parameters for the unbind command
    */
   private static final Parameters UNBINDID_PARAMS = new Parameters (
         new Parameter[] {
               new ParameterInteger("<KEYID>", false),
         }
         );
   
   public Bind() {
      super("");
   }
   
   /**
    * Initialises the instance using the specified command name
    * 
    * @param name - The name of the command
    */
   public Bind(String name) {
      super(name);
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
      Player player = getSenderAsPlayer(sender);
      Settings settings = loadSettings(player);
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
         
         ModSpc.instance.networkHandler.sendTo(new PacketConfig(this.getConfig(), keycode), (EntityPlayerMP) player.getMinecraftPlayer());
         settings.set(SETTINGS_PREFIX + keycode, command + " " + param);
         settings.save();
         sender.sendMessageToPlayer("Key " + FontColour.AQUA + params.get(0) + 
               FontColour.WHITE + " was successfully bound.");
      } else if (getName().equalsIgnoreCase("unbind") || getName().equalsIgnoreCase("unbindid")) {
         int keycode = Keyboard.KEY_NONE;
         if (getName().equalsIgnoreCase("unbind")) {
            String key = (String)params.get(0);
            if (key.equalsIgnoreCase("all")) {
               for(Object i : settings.entrySet()) {
                  if(i instanceof String && ((String) i).startsWith(SETTINGS_PREFIX)) {
                     settings.remove(i);
                  }
               }
               settings.save();
               sender.sendMessageToPlayer("All bindings removed.");
               ModSpc.instance.networkHandler.sendTo(new PacketConfig(this.getConfig(), 0x02000000), (EntityPlayerMP) player.getMinecraftPlayer());
               return;
            }
            keycode = Keyboard.getKeyIndex(key.toUpperCase());
         } else {
            keycode = (Integer)params.get(0);
         }
         if (keycode == Keyboard.KEY_NONE) {
            throw new CommandException("Unknown keycode " + params.get(0));
         }
         
         
         if (settings.remove(SETTINGS_PREFIX + keycode) == null) {
            throw new CommandException("No binding was found for key " + FontColour.AQUA + (String)params.get(0));
         } else {
            settings.save();
            ModSpc.instance.networkHandler.sendTo(new PacketConfig(this.getConfig(), 0x01000000 | keycode), (EntityPlayerMP) player.getMinecraftPlayer());
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
      } else if (getName().equalsIgnoreCase("unbindid")) {
         return UNBINDID_PARAMS;
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
      if (ModSpc.instance.proxy.isClientGuiScreenOpen()) {
         return;
      }
      
      ModSpc.instance.proxy.sendClientChat(KEYPRESS_COMMAND + key);
   }
   
   /**
    * @see com.sijobe.spc.util.KeyListener#keyReleased(int)
    */
   @Override
   public void keyReleased(int key) {
      // Not required
   }
   
   @Override
   public void init(Object... params) {
   }
   
   
   /**
    * @see com.sijobe.spc.network.IClientConfig#onConfigRecieved(java.lang.Object)
    * @param value the first byte determines the what action is required
    * 	if it is zero, bind the key specified in the last byte
    * 	if it is one, unbind the key specified in the last byte
    * 	if it is two, unbind all keys
    */
   @Override
   public void onConfigRecieved(Integer value) {
      int action = (value & 0xFF000000) >> 24;
      int keycode = value & 0x000000FF;
      if(action == 0) {
         this.bind(keycode);
      }
      else if(action == 1) {
         this.unbind(keycode);
      }
      else if(action == 2) {
         KeyboardHandler.removeAllKeys(INSTANCE);
      }
   }
   
   @Override
   public Config<Integer> getConfig() {
      return Config.BIND;
   }
}
