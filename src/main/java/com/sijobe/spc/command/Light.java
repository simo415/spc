package com.sijobe.spc.command;

import com.sijobe.spc.ModSpc;
import com.sijobe.spc.network.Config;
import com.sijobe.spc.network.IClientConfig;
import com.sijobe.spc.network.PacketConfig;
import com.sijobe.spc.util.Settings;
import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Player;

import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;

/**
 * Light command from SinglePlayerCommands 3.2.2,
 * ported to SinglePlayerConsole then back to SPC 4.1
 *
 * @author q3hardcore
 * @version 1.4
 * @status survived 1.7.2 update
 */
@Command (
      name = "light",
      description = "Lights up world",
      version = "1.4"
      )
public class Light extends StandardCommand implements IClientConfig<Boolean> {
   
   public static boolean isLit = false; // is current world lit? client sided
   
   /**
    * @see com.sijobe.spc.wrapper.CommandBase#execute(com.sijobe.spc.wrapper.CommandSender, java.util.List)
    */
   @Override
   public void execute(CommandSender sender, List<?> params) throws CommandException {
      Player player = super.getSenderAsPlayer(sender); // Why super.? Meh, Cannon has it like that
      Settings config = loadSettings(player);
      boolean lit = !config.getBoolean("light", false);
      config.set("light", lit);
      ModSpc.instance.networkHandler.sendTo(new PacketConfig(this.getConfig(), lit), (EntityPlayerMP) player.getMinecraftPlayer());
   }
   
   @Override
   public void init(Object... params) {
   }
   
   @Override
   public void onConfigRecieved(Boolean value) {
      ModSpc.instance.proxy.toggleClientLighting(value);
   }
   
   @Override
   public Config<Boolean> getConfig() {
      return Config.LIGHT;
   }
}
