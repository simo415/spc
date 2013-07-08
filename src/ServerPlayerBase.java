package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

/**
 * Dummy ServerPlayerBase class
 *
 * @author q3hardcore
 * @version 1.1
 */
public abstract class ServerPlayerBase {
   protected final EntityPlayerMP player;

   public ServerPlayerBase(ServerPlayerAPI serverPlayerAPI) {
      player = serverPlayerAPI.player;
   }

   public void afterLocalConstructing(MinecraftServer mcServer, World world, String var3, ItemInWorldManager manager) {
   }

   public boolean isEntityInsideOpaqueBlock() {
      return true;
   }

   public float getCurrentPlayerStrVsBlock(Block block, boolean bool) {
      return 1.0F;
   }

   public boolean canHarvestBlock(Block block) {
      return true;
   }

   public void beforeOnUpdate() {
   }

   public void afterClonePlayer(EntityPlayer entityPlayer, boolean bool) {
   }

}
