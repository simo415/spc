package com.sijobe.spc.proxy.client;

import java.io.File;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.EnumDifficulty;

import com.sijobe.spc.proxy.Proxy;
import com.sijobe.spc.util.ReflectionHelper;
import com.sijobe.spc.wrapper.Player;

public class ClientProxy extends Proxy {
   @Override
   public File getDataDirectory() {
      return MinecraftClient.getMinecraftDirectory();
   }
   
   @Override
   public Player getClientPlayer() {
      return new Player(Minecraft.getMinecraft().thePlayer);
   }
   
   @Override
   public boolean shouldNotRenderInsideOfBlock() {
      return Minecraft.getMinecraft().thePlayer.noClip;
   }
   
   @Override
   public void setDifficulty(EnumDifficulty difficulty) {
      Minecraft.getMinecraft().gameSettings.difficulty = difficulty;
   }
   
   @Override
   public void setClientLighting(boolean isLit) {
      EntityPlayer player = Minecraft.getMinecraft().thePlayer;
      if (isLit) {
         player.addChatMessage(new ChatComponentText("Lighting world"));
         float[] lightBrightnessTable = player.worldObj.provider.lightBrightnessTable;
         for (int i = 0; i < lightBrightnessTable.length; i++) {
            lightBrightnessTable[i] = 1.0F;
         }
      } else {
         player.addChatMessage(new ChatComponentText("Restoring light levels"));
         /*copied from WorldProvider.generateLightBrightnessTable*/
         float f = 0.0F;
         
         for (int i = 0; i <= 15; ++i) {
            float f1 = 1.0F - (float) i / 15.0F;
            player.worldObj.provider.lightBrightnessTable[i] = (1.0F - f1) / (f1 * 3.0F + 1.0F)
                  * (1.0F - f) + f;
         }
      }
   }
   
   @Override
   public boolean isClientGuiScreenOpen() {
      return MinecraftClient.isGuiScreenOpen();
   }
   
   @Override
   public void sendClientChat(String message) {
      NetHandlerPlayClient handler = MinecraftClient.getMinecraft()
            .getNetHandler();
      if (handler != null) {
         Packet packet = new C01PacketChatMessage(message);
         handler.addToSendQueue(packet);
      }
   }
   
   @Override
   public void setClientHitDelay(int delay) {
      ReflectionHelper.setField(ReflectionHelper.blockHitDelay, Minecraft
            .getMinecraft().playerController, delay);
   }
}