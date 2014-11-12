package com.sijobe.spc.command;

import com.sijobe.spc.util.ForgeHelper;
import com.sijobe.spc.util.Settings;
import com.sijobe.spc.validation.Parameters;
import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Player;

import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;

/**
 * Command to toggle instant mining
 *
 * @author q3hardcore
 * @version 1.0
 * @status broken through 1.7.2 update
 */
@Command (
   name = "instantmine",
   description = "Allows player to mine blocks instantly",
   example = "",
   videoURL = "",
   enabled = true
)
public class InstantMine extends StandardCommand {
   @Override
   public void execute(CommandSender sender, List<?> params) throws CommandException {
      Player player = super.getSenderAsPlayer(sender);
      if(player.getMinecraftPlayer() instanceof EntityPlayerMP) {
         Settings config = super.loadSettings(player);
         boolean instantMine = config.getBoolean("instantMine", false);
         if (params.size() == 0) {
            instantMine ^= true;
         } else {
            instantMine = ((Boolean)params.get(0));
         }
         config.set("instantMine", instantMine);
         super.saveSettings(player);
         player.sendChatMessage("Instant mining " + (instantMine?"enabled.":"disabled."));
      } else {
         throw new CommandException("Non-client command");
      }
   }

   @Override
   public Parameters getParameters() {
      return Parameters.DEFAULT_BOOLEAN;
   }
   
}