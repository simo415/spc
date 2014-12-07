package com.sijobe.spc;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

import com.sijobe.spc.core.HookManager;
import com.sijobe.spc.core.IBlockBroken;
import com.sijobe.spc.core.IBreakSpeed;
import com.sijobe.spc.wrapper.Block;
import com.sijobe.spc.wrapper.Item;
import com.sijobe.spc.wrapper.Player;
import com.sijobe.spc.wrapper.World;
import com.sijobe.spc.core.IPlayerMP;
import com.sijobe.spc.core.ICUIEventHandler;
import com.sijobe.spc.hooks.InitialiseCommands;
import com.sijobe.spc.network.ConfigMessageHandler;
import com.sijobe.spc.network.IClientConfig;
import com.sijobe.spc.network.PacketConfig;
import com.sijobe.spc.proxy.Proxy;

//TODO make prefixslash work in multiplayer

@Mod(useMetadata=true, modid="spc", version="5.0")
public class ModSpc
{
	@Instance
	public static ModSpc instance;
	
	protected HookManager hookManager;
	protected InitialiseCommands commands;
	public SimpleNetworkWrapper networkHandler;

	public Side side;
	public Proxy proxy;
	
	public ModSpc()
	{
		this.hookManager = new HookManager();
		this.commands = new InitialiseCommands();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) throws InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		this.side = event.getSide();
		if(this.side == Side.CLIENT) {
			this.proxy = (Proxy) Class.forName("com.sijobe.spc.proxy.client.ClientProxy").newInstance(); //using reflection to prevent client class loading server side 
		}
		else {
			this.proxy = (Proxy) Class.forName("com.sijobe.spc.proxy.server.ServerProxy").newInstance(); //using reflection to prevent client class loading server side 
		}
		
		Block.init();
		Item.init();
		this.hookManager.loadHooks(IPlayerMP.class);
		this.hookManager.loadHooks(ICUIEventHandler.class);
		this.hookManager.loadHooks(IBreakSpeed.class);
		this.hookManager.loadHooks(IBlockBroken.class);
		this.hookManager.loadHooks(IClientConfig.class);
		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this);
		this.networkHandler = new SimpleNetworkWrapper("spc.network");
		this.networkHandler.registerMessage(ConfigMessageHandler.class, PacketConfig.class, 0, Side.CLIENT);
		this.loadClientSettingHooks();
	}
	
	@EventHandler
	public void onServerStarting(FMLServerStartingEvent event)
	{
		System.out.println("loading spc commands...");
		this.commands.loadCommands();
	}
	
	//TODO make void handleHook<T>()
	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent event)
	{
		if(event.phase == Phase.START && event.side == Side.SERVER)
		{
			if(event.player instanceof EntityPlayerMP)
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
	
	@SubscribeEvent
	public void onBreakSpeed(BreakSpeed event)
	{
		for(IBreakSpeed hook : this.hookManager.getHooks(IBreakSpeed.class))
		{
			if(hook.isEnabled())
			{
				//TODO pass same player instance instead of creating a new one each time
				event.newSpeed = hook.getBreakSpeed(new Player(event.entityPlayer), Block.fromMinecraftBlock(event.block), event.metadata, event.originalSpeed, event.x, event.y, event.z);
			}
		}
	}
	
	@SubscribeEvent
	public void onBlockBreak(BreakEvent event)
	{
		for(IBlockBroken hook : this.hookManager.getHooks(IBlockBroken.class))
		{
			if(hook.isEnabled())
			{
				hook.onBreakBroken(event.x, event.y, event.z, new World(event.world), Block.fromMinecraftBlock(event.block), event.blockMetadata, new Player(event.getPlayer()));
			}
		}
	}
	
	public void loadClientSettingHooks()
	{
		for(IClientConfig hook : this.hookManager.getHooks(IClientConfig.class))
		{
			if(hook.isEnabled())
			{
				hook.getConfig().setHandler((IClientConfig) hook);
			}
		}
	}
}
