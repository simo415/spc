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

/**
 * the main mod class
 * 
 * @author aucguy
 * @version 1.0
 */
@Mod(useMetadata = true, modid = "spc", version = "5.0")
public class ModSpc { 
   /**
    * the spc mod instance
    */
   @Instance
   public static ModSpc instance;
   
   /**
    * the hook manager. Used for hooking things
    */
   protected HookManager hookManager;
   
   /**
    * the initialize commands instance. Used for initializing commands when the
    * server is launched
    */
   protected InitialiseCommands commands;
   
   /**
    * the network handler instance. Used to set client sided settings (like
    * light levels for the light command)
    */
   public SimpleNetworkWrapper networkHandler;
   
   /**
    * the side this mod is running on
    */
   public Side side;
   
   /**
    * the proxy instance. Used since sometimes it is neccessary to access the
    * Minecraft instance. However, this is client-sided. Even though it may not
    * be used on the server, loading any class that uses references the
    * Minecraft instance will load net.minecraft.client.Minecraft. If this
    * happens on the server, the server will crash.
    */
   public Proxy proxy;
   
   /**
    * construct me a mod!
    */
   public ModSpc() {
      this.hookManager = new HookManager();
      this.commands = new InitialiseCommands();
   }
   
   /**
    * used to initialize everything
    * 
    * @param event - the initialization event
    * @throws InstantiationException - because of reflection
    * @throws IllegalAccessException - because of reflection
    * @throws ClassNotFoundException - because of reflection
    */
   @EventHandler
   public void init(FMLInitializationEvent event)
         throws InstantiationException, IllegalAccessException,
         ClassNotFoundException {
      this.side = event.getSide();
      if (this.side == Side.CLIENT) {
         // if reflection wasn't used then when this class was loaded the
         // ClientProxy class would be loaded
         // and since ClientProxy referrs to net.minecraft.client.Minecraft,
         // that would be loaded
         // loading net.minecraft.client.Minecraft on a server crashes it.
         this.proxy = (Proxy) Class.forName(
               "com.sijobe.spc.proxy.client.ClientProxy").newInstance(); // using
                                                                         // reflection
                                                                         // to
                                                                         // prevent
                                                                         // client
                                                                         // class
                                                                         // loading
                                                                         // server
                                                                         // side
      } else {
         this.proxy = (Proxy) Class.forName(
               "com.sijobe.spc.proxy.server.ServerProxy").newInstance(); // using
                                                                         // reflection
                                                                         // to
                                                                         // prevent
                                                                         // client
                                                                         // class
                                                                         // loading
                                                                         // server
                                                                         // side
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
      this.networkHandler.registerMessage(ConfigMessageHandler.class,
            PacketConfig.class, 0, Side.CLIENT);
      this.loadClientSettingHooks();
   }
   
   /**
    * initialize the spc commands
    * 
    * @param event - the ServerStarting event
    */
   @EventHandler
   public void onServerStarting(FMLServerStartingEvent event) {
      System.out.println("loading spc commands...");
      this.commands.loadCommands();
   }
   
   // TODO make void handleHook<T>()
   /**
    * callss all of the player tick hooks
    * 
    * @param event - the PlayerTick event
    */
   @SubscribeEvent
   public void onPlayerTick(PlayerTickEvent event) {
      if (event.phase == Phase.START && event.side == Side.SERVER) {
         if (event.player instanceof EntityPlayerMP) {
            Player player = new Player((EntityPlayer) event.player);
            for (IPlayerMP hook : this.hookManager.getHooks(IPlayerMP.class)) {
               if (hook.isEnabled()) {
                  hook.onTick(player);
               }
            }
         }
      }
   }
   
   /**
    * calls of of the break speed hooks. Used to modify the time it takes to
    * break something
    * 
    * @param event - the breakSpeed event
    */
   @SubscribeEvent
   public void onBreakSpeed(BreakSpeed event) {
      for (IBreakSpeed hook : this.hookManager.getHooks(IBreakSpeed.class)) {
         if (hook.isEnabled()) {
            // TODO pass same player instance instead of creating a new one each
            // time
            event.newSpeed = hook.getBreakSpeed(new Player(event.entityPlayer),
                  Block.fromMinecraftBlock(event.block), event.metadata,
                  event.originalSpeed, event.x, event.y, event.z);
         }
      }
   }
   
   /**
    * calls of the the block broken hooks
    * 
    * @param event - the break event
    */
   @SubscribeEvent
   public void onBlockBreak(BreakEvent event) {
      for (IBlockBroken hook : this.hookManager.getHooks(IBlockBroken.class)) {
         if (hook.isEnabled()) {
            hook.onBreakBroken(event.x, event.y, event.z,
                  new World(event.world),
                  Block.fromMinecraftBlock(event.block), event.blockMetadata,
                  new Player(event.getPlayer()));
         }
      }
   }
   
   /**
    * loads all the client configuration hooks
    */
   public void loadClientSettingHooks() {
      for (IClientConfig hook : this.hookManager.getHooks(IClientConfig.class)) {
         if (hook.isEnabled()) {
            hook.getConfig().setHandler((IClientConfig) hook);
         }
      }
   }
}
