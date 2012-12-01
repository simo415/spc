package com.sijobe.spc.command;

import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandSender;

import net.minecraft.src.EntityPlayer;

import java.util.List;

/**
 * Open enderchest GUI anywhere
 *
 * @author q3hardcore
 * @version 1.4.2
 */
@Command (
   name = "enderchest",
   description = "Opens EnderChest GUI without having EnderChest",
   example = "all",
   version = "1.4.2"
)
public class EnderChest extends StandardCommand {
   
   /**
    * @see com.sijobe.spc.wrapper.CommandBase#execute(com.sijobe.spc.wrapper.CommandSender, java.util.List)
    */
   @Override
   public void execute(CommandSender sender, List<?> params) throws CommandException {
      EntityPlayer player = getSenderAsPlayer(sender).getMinecraftPlayer();
      player.displayGUIChest(player.getInventoryEnderChest());
   }
   
}
