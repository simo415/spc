package net.minecraft.src;

/**
 * Dummy ServerPlayerAPI class
 *
 * @author q3hardcore
 * @version 1.0
 */
public class ServerPlayerAPI {

   protected final EntityPlayerMP player;
   
   public static void register(String modID, Class<?> clazz) {
      throw new RuntimeException("SPC installed incorrectly");
   }
   
   private ServerPlayerAPI(EntityPlayerMP player) {
      this.player = player;
   }
   
   public static ServerPlayerAPI create(EntityPlayerMP player) {
      return new ServerPlayerAPI(player);
   }
   
}
