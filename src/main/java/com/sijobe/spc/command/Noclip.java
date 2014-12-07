package com.sijobe.spc.command;

import com.sijobe.spc.ModSpc;
import com.sijobe.spc.core.IPlayerMP;
import com.sijobe.spc.network.Config;
import com.sijobe.spc.network.IClientConfig;
import com.sijobe.spc.network.PacketConfig;
import com.sijobe.spc.util.FontColour;
import com.sijobe.spc.validation.Parameters;
import com.sijobe.spc.wrapper.CommandBase;
import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Coordinate;
import com.sijobe.spc.wrapper.Player;

import java.util.List;

import net.minecraft.util.ChatComponentText;
import net.minecraft.entity.player.EntityPlayerMP;

/**
 * Command for disabling clipping
 *
 * @author q3hardcore
 * @version 1.2
 * @status survived 1.7.2 update but can't see anything while inside blocks -> fixed
 */
@Command (
      name = "noclip",
      description = "Enables and disables the ability to clip for the player",
      example = "",
      videoURL = "http://www.youtube.com/watch?v=4ZOvu3hf7k0", // video for fly command
      enabled = true
      )
public class Noclip extends StandardCommand implements IPlayerMP, IClientConfig<Boolean> {
   
   @Override
   public void execute(CommandSender sender, List<?> params) throws CommandException {
      Player player = CommandBase.getSenderAsPlayer(sender);
      
      /*if(!player.getUsername().equals(MinecraftServer.getServer().getServerOwner())) {
         throw new CommandException("Must be server host");
      }*/
      if(!player.getMinecraftPlayer().capabilities.isFlying && !player.getMinecraftPlayer().noClip) {
         throw new CommandException("Must be flying");
      }
      
      if (params.size() == 0) {
         player.getMinecraftPlayer().noClip ^= true;
      } else {
         player.getMinecraftPlayer().noClip = (Boolean)params.get(0);
      }
      
      if(player.getMinecraftPlayer().noClip == false) {
         ascendPlayer(player);
      }
      ModSpc.instance.networkHandler.sendTo(new PacketConfig(this.getConfig(), player.getMinecraftPlayer().noClip), (EntityPlayerMP) player.getMinecraftPlayer());
      player.sendChatMessage("Noclip is now " + FontColour.AQUA + (!player.getMinecraftPlayer().noClip ? "disabled" : "enabled"));
   }
   
   @Override
   public Parameters getParameters() {
      return Parameters.DEFAULT_BOOLEAN;
   }
   
   public static void checkSafe(EntityPlayerMP player) {
      if(player.noClip && !player.capabilities.isFlying) {
         player.noClip = false;
         ModSpc.instance.networkHandler.sendTo(new PacketConfig(Config.NOCLIP, false), player);
         player.addChatMessage(new ChatComponentText("Noclip auto-disabled. (Player not flying)"));
         ascendPlayer(new Player(player));
      }
   }
   
   private static boolean ascendPlayer(Player player) {
      Coordinate playerPos = player.getPosition();
      if(player.isClearBelow(playerPos) && playerPos.getY() > 0) {
         return false;
      }
      double y = playerPos.getY() - 1; // in case player was standing on ground
      while (y < 260) {
         if(player.isClear(new Coordinate(playerPos.getX(), y++, playerPos.getZ()))) {
            final double newY;
            if(playerPos.getY() > 0) {
               newY = y - 1;
            } else {
               newY = y;
            }
            Coordinate newPos = new Coordinate(playerPos.getX() + 0.5F, newY, playerPos.getZ() + 0.5F);
            player.setPosition(newPos);
            break;
         }
      }
      return true;
   }
   
   @Override
   public void init(Object... params) {
   }
   
   @Override
   public void onConfigRecieved(Boolean value) {
      ModSpc.instance.proxy.getClientPlayer().getMinecraftPlayer().noClip = value;
   }
   
   @Override
   public Config<Boolean> getConfig() {
      return Config.NOCLIP;
   }

   @Override
   public void onTick(Player player) {
      checkSafe((EntityPlayerMP) player.getMinecraftPlayer());
   }
}