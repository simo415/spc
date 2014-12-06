package com.sijobe.spc.proxy;

import java.io.File;

import com.sijobe.spc.core.IBlockBroken;
import com.sijobe.spc.wrapper.Player;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.EnumDifficulty;

public abstract class Proxy{
	public abstract File getDataDirectory();
	public abstract Player getClientPlayer();
	public abstract boolean shouldNotRenderInsideOfBlock();
	public abstract void setDifficulty(EnumDifficulty difficulty);
	public abstract void toggleClientLighting(boolean isLit);
	public abstract boolean isClientGuiScreenOpen();
	public abstract void sendClientChat(String string);
	public abstract void setClientHitDelay(int delay);
}
