package com.sijobe.spc.proxy.server;

import java.io.File;

import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.server.dedicated.PropertyManager;

import com.sijobe.spc.proxy.Proxy;
import com.sijobe.spc.util.AccessHelper;
import com.sijobe.spc.wrapper.Block;
import com.sijobe.spc.wrapper.MinecraftServer;
import com.sijobe.spc.wrapper.Player;
import com.sijobe.spc.wrapper.World;

public class ServerProxy extends Proxy {

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
		try {
			((PropertyManager) AccessHelper.getObj(server, "settings")).setProperty("difficulty", difficulty.getDifficultyId());
		} catch (NoSuchFieldException e) {
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

	@Override
	public void toggleClientLighting(boolean isLit) {	
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
