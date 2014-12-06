package com.sijobe.spc.command;

import com.sijobe.spc.ModSpc;
import com.sijobe.spc.network.Config;
import com.sijobe.spc.network.IClientConfig;
import com.sijobe.spc.network.PacketConfig;
import com.sijobe.spc.util.FontColour;
import com.sijobe.spc.util.Settings;
import com.sijobe.spc.validation.Parameters;
import com.sijobe.spc.wrapper.CommandBase;
import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Player;

import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;

/**
 * Command to toggle requiring '/'
 *
 * @author q3hardcore
 * @version 1.0
 * @status broken through 1.7.2 update -> fixed
 */
@Command (
      name = "prefixslash",
      description = "Enables and disables auto slash-prefixing",
      example = "",
      videoURL = "",
      enabled = true
      )
public class PrefixSlash extends StandardCommand implements IClientConfig<Boolean> {
   public static boolean prefixSlash = false;
   
   @Override
   public boolean isEnabled() {
      return true;
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
      ModSpc.instance.networkHandler.sendTo(new PacketConfig(this.getConfig(), prefixSlash), (EntityPlayerMP) player.getMinecraftPlayer());
      player.sendChatMessage("Slash prefixing is now " + FontColour.AQUA
            + (prefixSlash ? "enabled" : "disabled"));
   }
   
   @Override
   public Parameters getParameters() {
      return Parameters.DEFAULT_BOOLEAN;
   }
   
   @Override
   public void init(Object... params) {
   }
   
   @Override
   public void onConfigRecieved(Boolean value) {
      prefixSlash = value;
   }
   
   @Override
   public Config<Boolean> getConfig() {
      return Config.PREFIX_SLASH;
   }
}