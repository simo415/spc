package com.sijobe.spc.command;

import com.sijobe.spc.ModSpc;
import com.sijobe.spc.core.IBlockBroken;
import com.sijobe.spc.core.IBreakSpeed;
import com.sijobe.spc.network.Config;
import com.sijobe.spc.network.IClientConfig;
import com.sijobe.spc.util.Settings;
import com.sijobe.spc.validation.Parameters;
import com.sijobe.spc.wrapper.Block;
import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Player;
import com.sijobe.spc.wrapper.World;

import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;

/**
 * Command to toggle instant mining
 *
 * @author q3hardcore
 * @version 1.0
 * @status broken through 1.7.2 update -> fixed
 */
@Command (
      name = "instantmine",
      description = "Allows player to mine blocks instantly",
      example = "",
      videoURL = "",
      enabled = true
      )
public class InstantMine extends StandardCommand implements IBreakSpeed, IBlockBroken, IClientConfig<Boolean>{
   public static boolean instantMiningEnabled; //TODO load initial value from config
   
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
         instantMiningEnabled = instantMine;
         player.sendChatMessage("Instant mining " + (instantMine?"enabled.":"disabled."));
      } else {
         throw new CommandException("Non-client command");
      }
   }
   
   @Override
   public Parameters getParameters() {
      return Parameters.DEFAULT_BOOLEAN;
   }
   
   @Override
   public void init(Object... params) {}
   
   @Override
   public float getBreakSpeed(Player player, Block block, int metadata, float orginalSpeed, int x, int y, int z) {
      return instantMiningEnabled ? 50000000.0F : orginalSpeed;
   }
   
   @Override
   public void onBreakBroken(int x, int y, int z, World world, Block block, int metadata, Player player) {
      if(instantMiningEnabled) {
         ModSpc.instance.proxy.setClientHitDelay(5);
      }
   }
   
   @Override
   public void onConfigRecieved(Boolean value) {
      instantMiningEnabled = value;
   }
   
   @Override
   public Config<Boolean> getConfig() {
      return Config.INSTANT_MINE;
   }
}