package com.sijobe.spc.proxy.client;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.command.ICommandSender;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.EnumDifficulty;

import com.sijobe.spc.command.InstantMine;
import com.sijobe.spc.proxy.Proxy;
import com.sijobe.spc.util.AccessHelper;
import com.sijobe.spc.wrapper.Block;
import com.sijobe.spc.wrapper.Player;
import com.sijobe.spc.wrapper.World;

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
	public void toggleClientLighting(boolean isLit) {
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		if(isLit) {
			player.addChatMessage(new ChatComponentText("Lighting world"));
			float[] lightBrightnessTable = player.worldObj.provider.lightBrightnessTable;
			for(int i = 0; i < lightBrightnessTable.length; i++) {
				lightBrightnessTable[i] = 1.0F;
			}
		} else {
			player.addChatMessage(new ChatComponentText("Restoring light levels"));
			try {
				AccessHelper.callMethod(player.worldObj.provider, "generateLightBrightnessTable");
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public boolean isClientGuiScreenOpen() {
		return MinecraftClient.isGuiScreenOpen();
	}
	
	@Override
	public void sendClientChat(String message) {
		NetHandlerPlayClient handler = MinecraftClient.getMinecraft().getNetHandler();
		if(handler != null) {
			Packet packet = new C01PacketChatMessage(message);
			handler.addToSendQueue(packet);
		}
	}

	@Override
	public void setClientHitDelay(int delay) {
		try {
			AccessHelper.setInt(Minecraft.getMinecraft().playerController, "blockHitDelay", delay);
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}