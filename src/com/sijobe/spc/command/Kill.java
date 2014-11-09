package com.sijobe.spc.command;

import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Player;

import net.minecraft.util.DamageSource;

import java.util.List;

/**
 * Kills the player that runs the command
 *
 * @author simo_415
 * @version 1.0
 * @status survived 1.7.2 update
 */
@Command (
   name = "kill",
   description = "Kills the current player",
   videoURL = "http://www.youtube.com/watch?v=iNzdikZvnyI",
   version = "1.0"
)
public class Kill extends StandardCommand {
   /**
    * @see com.sijobe.spc.wrapper.CommandBase#execute(net.minecraft.src.ICommandSender, java.util.List)
    */
   @Override
   public void execute(CommandSender sender, List<?> params) {
      Player player = super.getSenderAsPlayer(sender);
      player.setHealth(0);
      player.getMinecraftPlayer().onDeath(DamageSource.generic);
      sender.sendMessageToPlayer("Ouch. That looks like it hurt.");
   }
}
