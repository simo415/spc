package com.sijobe.spc.command;

import com.sijobe.spc.ModSpc;
import com.sijobe.spc.network.Config;
import com.sijobe.spc.network.IClientConfig;
import com.sijobe.spc.network.PacketConfig;
import com.sijobe.spc.util.ForgeHelper;
import com.sijobe.spc.validation.Parameter;
import com.sijobe.spc.validation.Parameters;
import com.sijobe.spc.validation.ParameterDouble;
import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Player;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.List;

/**
 * Changes block reach distance
 *
 * @author q3hardcore
 * @version 1.0
 * @status broken through 1.7.2 update -> fixed
 */
@Command (
      name = "blockreach",
      description = "Changes block reach distance",
      alias = {"reach"}
      )
public class BlockReach extends StandardCommand implements IClientConfig<Float>{
   
   /**
    * The parameters of the command
    */
   private static final Parameters PARAMETERS = new Parameters (
         new Parameter[] {
               new ParameterDouble("[DISTANCE]", true),
         }
         );
   
   public static float reachDistance = 4.5F; //refered to through ASM modifications and is client sided
   
   /**
    * @see com.sijobe.spc.wrapper.CommandBase#execute(com.sijobe.spc.wrapper.CommandSender, java.util.List)
    */
   @Override
   public void execute(CommandSender sender, List<?> params) throws CommandException {
      Player player = getSenderAsPlayer(sender);
      if(!(player.getMinecraftPlayer() instanceof EntityPlayerMP)) {
         throw new CommandException("Command must be executed by non-client player.");
      }
      EntityPlayerMP playerEntity = (EntityPlayerMP)player.getMinecraftPlayer();
      if(params.size() == 0) {
         sender.sendMessageToPlayer("Current block reach distance: " +
               ForgeHelper.getBlockReachDistance(playerEntity.theItemInWorldManager));
      } else {
         double newReach = (Double)params.get(0);
         if(newReach < 4.5D || newReach > 255.0D) {
            throw new CommandException("Reach distance must be between 4.5 and 255.");
         }
         playerEntity.theItemInWorldManager.setBlockReachDistance(newReach);
         sender.sendMessageToPlayer("Set block reach distance to: " + newReach);
         ModSpc.instance.networkHandler.sendTo(new PacketConfig(this.getConfig(), (float) newReach), playerEntity);
      }
   }
   
   /**
    * @see com.sijobe.spc.wrapper.CommandBase#getParameters()
    */
   @Override
   public Parameters getParameters() {
      return PARAMETERS;
   }
   
   @Override
   public void init(Object... params) {
   }
   
   @Override
   public void onConfigRecieved(Float value) {
      reachDistance = value;
   }
   
   @Override
   public Config getConfig() {
      return Config.BLOCK_REACH;
   }
}
