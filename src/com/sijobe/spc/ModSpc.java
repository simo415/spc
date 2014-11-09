package com.sijobe.spc;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import cpw.mods.fml.relauncher.Side;

import com.google.common.eventbus.Subscribe;

import com.sijobe.spc.core.Constants;
import com.sijobe.spc.core.HookManager;
import com.sijobe.spc.core.IHook;
import com.sijobe.spc.util.DynamicClassLoader;
import com.sijobe.spc.wrapper.Block;
import com.sijobe.spc.wrapper.Item;
import com.sijobe.spc.wrapper.Player;
import com.sijobe.spc.core.IPlayerMP;
import com.sijobe.spc.core.IPlayerSP;
import com.sijobe.spc.core.ICUIEventHandler;
import com.sijobe.spc.hooks.InitialiseCommands;

@Mod(useMetadata=true, modid="spc", version="2.0")
public class ModSpc
{
	protected HookManager hookManager;
	protected InitialiseCommands commands;
	
	public ModSpc()
	{
		this.hookManager = new HookManager();
		this.commands = new InitialiseCommands();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		Block.init();
		Item.init();
		this.hookManager.loadHooks(IPlayerMP.class);
		this.hookManager.loadHooks(IPlayerSP.class);
		this.hookManager.loadHooks(ICUIEventHandler.class);
		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this);
	}
	
	@EventHandler
	public void onServerStarting(FMLServerStartingEvent event)
	{
		System.out.println("loading spc commands...");
		this.commands.loadCommands();
	}
	
	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent event)
	{
		if(event.phase == Phase.START && event.side == Side.SERVER)
		{
			if(event.player instanceof EntityPlayerSP)
			{
				Player player = new Player((EntityPlayer) event.player);
				boolean moved = event.player.posX != event.player.prevPosX || event.player.posY != event.player.prevPosY || event.player.posZ != event.player.prevPosZ;
				for(IPlayerSP hook : this.hookManager.getHooks(IPlayerSP.class))
				{
					if(hook.isEnabled())
					{
						hook.onTick(player);
						if(moved)
						{
							hook.movePlayer(player, player.getMovementForward(), player.getMovementStrafe(), event.player.getAIMoveSpeed());
						}
					}
				}
			}
			else if(event.player instanceof EntityPlayerMP)
			{
				Player player = new Player((EntityPlayer) event.player);
				for(IPlayerMP hook : this.hookManager.getHooks(IPlayerMP.class))
				{
					if(hook.isEnabled())
					{
						hook.onTick(player);
					}
				}
			}
		}
	}
}
