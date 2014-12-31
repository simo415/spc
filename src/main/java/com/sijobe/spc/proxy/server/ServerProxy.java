package com.sijobe.spc.proxy.server;

import java.io.File;
import java.lang.reflect.Field;

import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.server.dedicated.PropertyManager;

import com.sijobe.spc.proxy.Proxy;
import com.sijobe.spc.util.ReflectionHelper;
import com.sijobe.spc.wrapper.MinecraftServer;
import com.sijobe.spc.wrapper.Player;

public class ServerProxy extends Proxy {
   public static final Field settingsField = ReflectionHelper.getField(DedicatedServer.class, "settings", "l", "field_71340_o");
   
	@Override
	public File getDataDirectory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Player getClientPlayer() {
		return null;
	}

	@Override
	public boolean shouldNotRenderInsideOfBlock() {
		return false;
	}

	@Override
	public void setDifficulty(EnumDifficulty difficulty) {
		DedicatedServer server = (DedicatedServer) MinecraftServer.getMinecraftServer();
		((PropertyManager) ReflectionHelper.getObj(settingsField, server)).setProperty("difficulty", difficulty.getDifficultyId());
	}

	@Override
	public void setClientLighting(boolean isLit) {	
	}
	
	@Override
	public boolean isClientGuiScreenOpen() {
		return false;
	}
	
	@Override
	public void sendClientChat(String string) {
	}

	@Override
	public void setClientHitDelay(int delay) {
	}
}
