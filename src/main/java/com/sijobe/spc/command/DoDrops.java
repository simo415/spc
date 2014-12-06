package com.sijobe.spc.command;

import com.sijobe.spc.core.IPlayerMP;
import com.sijobe.spc.validation.Parameters;
import com.sijobe.spc.wrapper.CommandBase;
import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Coordinate;
import com.sijobe.spc.wrapper.Player;

import java.util.List;

/**
 * Command to toggle drops
 *
 * @author q3hardcore
 * @version 1.0
 * @status survived 1.7.2 update
 */
@Command (
         name = "dodrops",
         description = "Toggles mob and block drops",
         example = "",
         videoURL = "",
         enabled = true
)
public class DoDrops extends StandardCommand implements IPlayerMP {

   private static boolean removeDrops = false;

   @Override
   public boolean isEnabled() {
      return true;
   }

   @Override
   public void execute(CommandSender sender, List<?> params) throws CommandException {
      Player player = CommandBase.getSenderAsPlayer(sender);
      if (params.size() == 0) {
         removeDrops ^= true;
      } else {
         removeDrops = ((Boolean)params.get(0));
      }
      player.sendChatMessage("Do mob and block drops? " + !removeDrops);
   }

   @Override
   public Parameters getParameters() {
      return Parameters.DEFAULT_BOOLEAN;
   }
   
   /**
    * @see com.sijobe.spc.core.IHook#init(java.lang.Object[])
    */
   @Override
   public void init(Object... params) {
   }
   
   /**
    * @see com.sijobe.spc.core.IPlayerMP#onTick(com.sijobe.spc.wrapper.Player)
    */
   @Override
   public void onTick(Player player) {
      if(removeDrops) {
         ClearDrops.removeItemDrops(player, 128);
      }
   }
   
}