package com.sijobe.spc.command;

import com.sijobe.spc.overwrite.ONetServerHandler;
import com.sijobe.spc.util.FontColour;
import com.sijobe.spc.validation.Parameters;
import com.sijobe.spc.wrapper.CommandBase;
import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Minecraft;
import com.sijobe.spc.wrapper.Player;

import java.util.List;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.NetServerHandler;

/**
 * Command for disabling clipping
 *
 * @author q3hardcore
 * @version 1.2
 */
@Command (
         name = "noclip",
         description = "Enables and disables the ability to clip for the player",
         example = "",
         videoURL = "http://www.youtube.com/watch?v=4ZOvu3hf7k0", // video for fly command
         enabled = true
)
public class Noclip extends StandardCommand {

   @Override
   public void execute(CommandSender sender, List<?> params) throws CommandException {
      Player player = CommandBase.getSenderAsPlayer(sender);

      if(!player.getMinecraftPlayer().username.equals(MinecraftServer.getServer().getServerOwner())) {
         throw new CommandException("Must be server host");
      }
      if(!player.getMinecraftPlayer().capabilities.isFlying && !player.getMinecraftPlayer().noClip) {
         throw new CommandException("Must be flying");
      }

      if (params.size() == 0) {
         player.getMinecraftPlayer().noClip ^= true;
      } else {
         player.getMinecraftPlayer().noClip = (Boolean)params.get(0);
      }

      // replace server handler
      if(player.getMinecraftPlayer() instanceof EntityPlayerMP) {

         EntityPlayerMP playerEntity = (EntityPlayerMP)player.getMinecraftPlayer();
         NetServerHandler handler = playerEntity.playerNetServerHandler;

         if(playerEntity.noClip) {
            if(!(handler instanceof ONetServerHandler)) {
               playerEntity.playerNetServerHandler = new ONetServerHandler(MinecraftServer.getServer(),
                        handler.netManager, handler.playerEntity, handler);
            }
         } else {
            if(handler instanceof ONetServerHandler) {
               NetServerHandler oldInstance = ((ONetServerHandler)handler).getOldInstance();
               if(oldInstance != null) {
                  handler.netManager.setNetHandler(oldInstance);
                  playerEntity.playerNetServerHandler = oldInstance;
               }
            }
         }
      } else {
         player.getMinecraftPlayer().noClip = false;
         throw new CommandException("Noclip unavailable");
      }

      Minecraft.getMinecraft().thePlayer.noClip = player.getMinecraftPlayer().noClip;
      player.sendChatMessage("Noclip is now " + FontColour.AQUA + (!player.getMinecraftPlayer().noClip ? "disabled" : "enabled"));
   }

   @Override
   public Parameters getParameters() {
      return Parameters.DEFAULT_BOOLEAN;
   }
}