package com.sijobe.spc.command;

import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Minecraft;
import com.sijobe.spc.wrapper.Player;
import com.sijobe.spc.wrapper.World;

import java.util.List;

/**
 * Light command from SinglePlayerCommands 3.2.2,
 * ported to SinglePlayerConsole then back to SPC 4.1
 *
 * @author q3hardcore
 * @version 1.4
 */
@Command (
   name = "light",
   description = "Lights up world",
   version = "1.4"
)
public class Light extends StandardCommand {

   public static boolean isLit = false; // is current world lit?
   public static int litWorld = 0; // hashCode for currently lit world
   
   /**
    * @see com.sijobe.spc.wrapper.CommandBase#execute(com.sijobe.spc.wrapper.CommandSender, java.util.List)
    */
   @Override
   public void execute(CommandSender sender, List<?> params) throws CommandException {
      Player player = super.getSenderAsPlayer(sender); // Why super.? Meh, Cannon has it like that
      Player clientPlayer = Minecraft.getPlayer();
      
      if(clientPlayer == null) {
         throw new CommandException("No client player!");
      }
      
      World clientWorld = clientPlayer.getWorld();
      
      if(player.getWorld().getMinecraftWorld().hashCode() != litWorld) {
         isLit = false;
      }
      
      // Note: provider is worldProvider
      if(!isLit) {
         sender.sendMessageToPlayer("Lighting world");
         float[] lightBrightnessTable = clientWorld.getMinecraftWorld().provider.lightBrightnessTable;
         for(int i = 0; i < lightBrightnessTable.length; i++) {
            lightBrightnessTable[i] = 1.0F;
         }
         litWorld = player.getWorld().getMinecraftWorld().hashCode(); // we go by the serverside hashcode
      } else {
         sender.sendMessageToPlayer("Restoring defaults");
         // clientWorld.getMinecraftWorld().provider.generateLightBrightnessTable();
         clientWorld.getMinecraftWorld().provider.registerWorld(clientWorld.getMinecraftWorld());
      }
      isLit ^= true; // toggle isLit
   }

   /**
    * Only enabled in single player since this is a client-side mod
    * 
    * @see com.sijobe.spc.wrapper.CommandBase#isEnabled()
    */   
   @Override
   public boolean isEnabled() {
      return Minecraft.isSinglePlayer();
   }

}
