package com.sijobe.spc.command;

import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Player;

import java.util.List;

import net.minecraft.src.EntityPlayerMP;

/**
 * Command to toggle instant mining
 *
 * @author q3hardcore
 * @version 1.0
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
         EntityPlayerMP playerMP = (EntityPlayerMP)player.getMinecraftPlayer();
         playerMP.setInstantMine(!playerMP.getInstantMine());
         player.sendChatMessage("Instant mining " + (playerMP.getInstantMine()?"enabled.":"disabled."));
      } else {
         throw new CommandException("Non-client command");
      }
   }

}