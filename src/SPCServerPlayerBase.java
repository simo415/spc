package net.minecraft.src;

import com.sijobe.spc.core.HookManager;
import com.sijobe.spc.core.IPlayerMP;
import com.sijobe.spc.overwrite.ONetServerHandler;
import com.sijobe.spc.util.Settings;
import com.sijobe.spc.wrapper.CommandBase;
import com.sijobe.spc.wrapper.Player;
import java.util.List;
import net.minecraft.server.MinecraftServer;

/**
 * Interface between PlayerAPI and SPC
 *
 * @author q3hardcore
 * @version 1.0
 */
public class SPCServerPlayerBase extends ServerPlayerBase {

   private Player spcPlayer;
   private static HookManager HOOK_MANAGER = new HookManager();
   private static String SERVER_ID;

   public SPCServerPlayerBase(ServerPlayerAPI api) {
      super(api);
   }
  
   private List<IPlayerMP> getPlayerHooks() {
      if (!MinecraftServer.getServer().toString().equals(SERVER_ID)) {
         HOOK_MANAGER = new HookManager();
         HOOK_MANAGER.loadHooks(IPlayerMP.class);
         SERVER_ID = MinecraftServer.getServer().toString();
      }
      return HOOK_MANAGER.getHooks(IPlayerMP.class);
   }

   private boolean getInstantMine() {
      Settings settings = CommandBase.loadSettings(spcPlayer);
      if(settings == null) {
         return false;
      } else {
         return settings.getBoolean("instantMine", false);
      }
   }
   
   @Override
   public void afterLocalConstructing(MinecraftServer mcServer, World world, String var3, ItemInWorldManager manager) {
      spcPlayer = new Player(player);
   }
   
   /**
    * Based upon Zombe's noclip implementation
   */
   @Override
   public boolean isEntityInsideOpaqueBlock() {
      if(player.noClip) {
         return false;
      } else {
         return !player.sleeping && player.superIsEntityInsideOpaqueBlock();
      }
    }

   @Override
   public float getCurrentPlayerStrVsBlock(Block block, boolean bool) {
      if(getInstantMine()) {
         return 1000000.0F; // 1 million
      } else {
         return player.superGetCurrentPlayerStrVsBlock(block, bool);
      }
   }

   @Override
   public boolean canHarvestBlock(Block block) {
      if(getInstantMine()) {
         return true;
      } else {
         return player.superCanHarvestBlock(block);
      }
   }
    
   @Override
   public void beforeOnUpdate() {
      for (IPlayerMP hook : getPlayerHooks()) {
         if (hook.isEnabled()) {
            hook.onTick(spcPlayer);
         }
      }
   }
  
   @Override
   public void afterClonePlayer(EntityPlayer entityPlayer, boolean bool) {
      if(player.playerNetServerHandler instanceof ONetServerHandler) {
         NetServerHandler oldInstance = ((ONetServerHandler)player.playerNetServerHandler).getOldInstance();
         if(oldInstance != null) {
            System.out.println("SPC: Restoring playerNetServerHandler.");
            player.playerNetServerHandler = new NetServerHandler(MinecraftServer.getServer(), oldInstance.netManager, player);
         }
      }
   }
  
 }