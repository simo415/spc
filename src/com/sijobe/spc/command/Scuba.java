package com.sijobe.spc.command;

import java.util.List;

import com.sijobe.spc.core.IPlayerMP;
import com.sijobe.spc.util.FontColour;
import com.sijobe.spc.util.Settings;
import com.sijobe.spc.validation.Parameters;
import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Player;

/**
 * Allows the player to breath under water without restrictions
 *
 * @author simo_415
 * @version 1.0
 */
@Command (
   name = "scuba",
   description = "Allows the player to breath underwater",
   videoURL = "http://www.youtube.com/watch?v=1PO6BwUS2uo",
   version = "1.4.6"
)
public class Scuba extends StandardCommand implements IPlayerMP {

   /**
    * The integer value that specifies max player air
    */
   private static final int MAX_AIR = 300;

   @Override
   public void execute(CommandSender sender, List<?> params) throws CommandException {
      Player player = super.getSenderAsPlayer(sender);
      Settings config = super.loadSettings(player);
      boolean value = config.getBoolean("scuba", false);
      if (params.size() > 0) {
         value = !(Boolean)params.get(0);
      }
      if (!value) {
         config.set("scuba", true);
         player.sendChatMessage("Scuba mode is " + FontColour.AQUA + "enabled");
      } else {
         config.set("scuba", false);
         player.sendChatMessage("Scuba mode is " + FontColour.AQUA + "disabled");
      }
      config.save();
   }
   
   /**
    * @see com.sijobe.spc.core.IPlayerMP#onTick(com.sijobe.spc.wrapper.Player)
    */
   @Override
   public void onTick(Player player) {
      if (super.loadSettings(player).getBoolean("scuba", false)) {
         player.setAir(MAX_AIR);
      }
   }

   /**
    * @see com.sijobe.spc.core.IHook#init(java.lang.Object[])
    */
   @Override
   public void init(Object... params) {
   }
   
   @Override
   public Parameters getParameters() {
      return Parameters.DEFAULT_BOOLEAN;
   }
}
