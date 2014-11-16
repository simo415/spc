package com.sijobe.spc.command;

import com.sijobe.spc.util.FontColour;
import com.sijobe.spc.util.Settings;
import com.sijobe.spc.validation.Parameters;
import com.sijobe.spc.wrapper.CommandBase;
import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Minecraft;
import com.sijobe.spc.wrapper.Player;

import java.util.List;

/**
 * Command to toggle requiring '/'
 *
 * @author q3hardcore
 * @version 1.0
 */
@Command (
   name = "prefixslash",
   description = "Enables and disables auto slash-prefixing",
   example = "",
   videoURL = "",
   enabled = true
)
public class PrefixSlash extends StandardCommand {

	@Override
	public boolean isEnabled() {
		return Minecraft.isSinglePlayer();
	}

   @Override
   public void execute(CommandSender sender, List<?> params) throws CommandException {
      Player player = CommandBase.getSenderAsPlayer(sender);
      Settings config = super.loadSettings(player);
      boolean prefixSlash = config.getBoolean("prefixSlash", true);
      if (params.size() == 0) {
         prefixSlash ^= true;
      } else {
         prefixSlash = ((Boolean)params.get(0));
      }
      config.set("prefixSlash", prefixSlash);
      super.saveSettings(player);
      player.sendChatMessage("Slash prefixing is now " + FontColour.AQUA
               + (prefixSlash ? "enabled" : "disabled"));
   }
   
   @Override
   public Parameters getParameters() {
      return Parameters.DEFAULT_BOOLEAN;
   }

}