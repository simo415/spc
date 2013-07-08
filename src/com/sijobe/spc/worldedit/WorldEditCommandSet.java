package com.sijobe.spc.worldedit;

import com.sijobe.spc.command.MultipleCommands;
import com.sijobe.spc.core.IHook;
import com.sijobe.spc.util.WorldEditCUIHelper;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Coordinate;
import com.sijobe.spc.wrapper.MinecraftServer;
import com.sijobe.spc.wrapper.Player;

import java.util.List;

/**
 * The class that provides connection to WorldEdit. 
 *
 * @author simo_415
 * @version 1.2
 */
public class WorldEditCommandSet extends MultipleCommands {

   /*static {
      DynamicClassLoader.addFile(new File(Minecraft.getMinecraftDirectory(),"bin/WorldEdit.jar"));
   }*/

   /**
    * The WorldEdit object that the class communicates to WorldEdit with
    */
   public static com.sk89q.worldedit.WorldEdit WORLDEDIT;
   /**
    * The server interface that WorldEdit is using
    */
   public static ServerInterface SERVER; 
   /**
    * The configuration that WorldEdit is using
    */
   public static PropertiesConfiguration CONFIGURATION;

   /**
    * Stores the last instance of this class that was created
    */
   private static WorldEditCommandSet INSTANCE;

   /**
    * Used to store the last time (in ms) that a left-click was sent
    */
   private long leftClick;
   /**
    * Used to store the last time (in ms) that a right-click was sent
    */
   private long rightClick;
   
   private long leftBlock;
   private long rightBlock;

   /**
    * Provides a delay between sending mouse updates to WorldEdit
    */
   private static final int CLICK_DELAY = 200;

   public WorldEditCommandSet(String name) {
      super(name);

      CONFIGURATION = new PropertiesConfiguration();
      CONFIGURATION.load();

      SERVER = new ServerInterface();

      WORLDEDIT = new com.sk89q.worldedit.WorldEdit(SERVER, CONFIGURATION);

      leftClick = rightClick = leftBlock = rightBlock = System.currentTimeMillis();

      INSTANCE = this;
   }

   /**
    * Gets the currently constructed instance of the class. This allows reuse 
    * of the same class by multiple implementing classes without having to 
    * create new instances and deal with that.
    * 
    * @return The current instance of this class
    */
   public static WorldEditCommandSet getCurrentInstance() {
      return INSTANCE;
   }

   @Override
   public String[] getCommands() {
      return WORLDEDIT.getCommands().keySet().toArray(new String[]{});
   }

   @Override
   public String getDescription() {
      return WORLDEDIT.getCommands().get(getName());
   }

   @Override
   public void execute(CommandSender sender, List<?> params) {
      String command[] = null;
      if (params.size() == 1) {
         String split[] = ((String)params.get(0)).split(" ");
         command = new String[split.length + 1];
         for (int i = 1; i < command.length; i++) {
            command[i] = split[i - 1];
         }
         command[0] = "/" + getName();
      } else {
         command = new String[] { "/" + getName() };
      }
      handleCommand(getSenderAsPlayer(sender), command);
   }

   /**
    * Handles a command that is sent to a WorldEdit command
    * 
    * @param player - The player that executed the command
    * @param command - The arguments (command) that the player sent
    */
   public void handleCommand(Player player, String command[]) {
      LocalPlayer localPlayer = getLocalPlayer(player);
      WORLDEDIT.handleCommand(localPlayer, command);
   }

   private static LocalPlayer getLocalPlayer(Player player) {
      LocalPlayer localPlayer = new LocalPlayer(player, SERVER);
      if(player.getPlayerName().equals(MinecraftServer.getMinecraftServer().getServerOwner())) {
         if(hasCUIHooks()) {
            WORLDEDIT.getSession(localPlayer).setCUISupport(true);
            WORLDEDIT.getSession(localPlayer).dispatchCUISetup(localPlayer);
         }
      }
      return localPlayer;
   }
   
   private static boolean hasCUIHooks() {
      int cuiHooks = 0;
      for(IHook hook : WorldEditCUIHelper.getCUIHooks()) {
         if(hook.isEnabled()) {
            cuiHooks++;
         }
      }
      return (cuiHooks > 0);
   }
   
   /**
    * Handles a player arm swing event
    * 
    * @param player - The player the event occurred on
    */
   public void handleArmSwing(Player player) {
      if (leftClick + CLICK_DELAY < System.currentTimeMillis()) {
         LocalPlayer localPlayer = getLocalPlayer(MinecraftServer.getPlayerByUsername(player.getPlayerName()));
         WORLDEDIT.handleArmSwing(localPlayer);
         leftClick = System.currentTimeMillis();
      }
   }

   /**
    * Handles a player right-click event
    * 
    * @param player - The player the event occurred on
    */
   public void handleRightClick(Player player) {
      if (rightClick + CLICK_DELAY < System.currentTimeMillis()) {
         LocalPlayer localPlayer = getLocalPlayer(MinecraftServer.getPlayerByUsername(player.getPlayerName()));
         WORLDEDIT.handleRightClick(localPlayer);
         rightClick = System.currentTimeMillis();
      }
   }

   /**
    * Handles a player left clicking a block
    * 
    * @param player - The player the event occurred on
    * @param block - The block that the player clicked
    */
   public void handleBlockLeftClick(Player player, Coordinate block) {
      if (leftBlock + CLICK_DELAY < System.currentTimeMillis()) {
         LocalPlayer localPlayer = getLocalPlayer(MinecraftServer.getPlayerByUsername(player.getPlayerName()));
         com.sk89q.worldedit.WorldVector vector = new com.sk89q.worldedit.WorldVector(localPlayer.getWorld(), block.getBlockX(), block.getBlockY(), block.getBlockZ());
         WORLDEDIT.handleBlockLeftClick(localPlayer, vector);
         leftBlock = System.currentTimeMillis();
      }
   }

   /**
    * Handles a player right clicking a block
    * 
    * @param player - The player the event occurred on
    * @param block - The block that the player clicked
    */
   public void handleBlockRightClick(Player player, Coordinate block) {
      if (rightBlock + CLICK_DELAY < System.currentTimeMillis()) {
         LocalPlayer localPlayer = getLocalPlayer(MinecraftServer.getPlayerByUsername(player.getPlayerName()));
         com.sk89q.worldedit.WorldVector vector = new com.sk89q.worldedit.WorldVector(localPlayer.getWorld(), block.getBlockX(), block.getBlockY(), block.getBlockZ());
         WORLDEDIT.handleBlockRightClick(localPlayer, vector);
         rightBlock = System.currentTimeMillis();
      }
   }
}
