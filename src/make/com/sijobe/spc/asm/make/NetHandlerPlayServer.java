package com.sijobe.spc.asm.make;

import org.apache.commons.lang3.StringUtils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.ForgeHooks;

public class NetHandlerPlayServer extends net.minecraft.network.NetHandlerPlayServer {
   public NetHandlerPlayServer(MinecraftServer par1MinecraftServer,
         NetworkManager par2iNetworkManager, EntityPlayerMP par3EntityPlayerMP) {
      super(par1MinecraftServer, par2iNetworkManager, par3EntityPlayerMP);
      // TODO Auto-generated constructor stub
   }
   
   public void processChatMessage(C01PacketChatMessage p_147354_1_)
   {
       if (this.playerEntity.func_147096_v() == EntityPlayer.EnumChatVisibility.HIDDEN)
       {
           ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation("chat.cannotSend", new Object[0]);
           chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.RED);
           this.sendPacket(new S02PacketChat(chatcomponenttranslation));
       }
       else
       {
           this.playerEntity.func_143004_u();
           String s = p_147354_1_.func_149439_c();
           s = StringUtils.normalizeSpace(s);

           for (int i = 0; i < s.length(); ++i)
           {
               if (!ChatAllowedCharacters.isAllowedCharacter(s.charAt(i)))
               {
                   this.kickPlayerFromServer("Illegal characters in chat");
                   return;
               }
           }

           if (com.sijobe.spc.asm.SlashPrefixer.isCommand(s, this))
           {
              System.out.println(s);
               //this.handleSlashCommand(s);
           }
           else
           {
               ChatComponentTranslation chatcomponenttranslation1 = new ChatComponentTranslation("chat.type.text", new Object[] {this.playerEntity.func_145748_c_(), s});
               chatcomponenttranslation1 = ForgeHooks.onServerChatEvent(this, s, chatcomponenttranslation1);
               if (chatcomponenttranslation1 == null) return;
               //this.serverController.getConfigurationManager().sendChatMsgImpl(chatcomponenttranslation1, false);
           }

           //this.chatSpamThresholdCount += 20;
           
           /*
           if (this.chatSpamThresholdCount > 200 && !this.serverController.getConfigurationManager().isPlayerOpped(this.playerEntity.getCommandSenderName()))
           {
               this.kickPlayerFromServer("disconnect.spam");
           }
           */
       }
   }
}
