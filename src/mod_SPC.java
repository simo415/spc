package net.minecraft.src;

import com.sijobe.spc.core.Constants;

/**
 * Used for registering SinglePlayerCommands with PlayerAPI
 *
 * @author q3hardcore
 * @version 1.2
 */
public class mod_SPC extends BaseMod {
   
   @Override
   public String getName() {
      return Constants.NAME;
   }
   
   @Override
   public String getVersion() {
      return Constants.VERSION;
   }
   
   @Override
   public void load() {
      try {
         EntityClientPlayerMP.class.getDeclaredField("HOOK_MANAGER");
      } catch (NoSuchFieldException nsfe) {
         throw new RuntimeException("SPC installed incorrectly");
      }
      try {
         EntityPlayerMP.class.getDeclaredField("spcServerPlayerBase");
         System.out.println("SPC: PlayerAPI not detected.");
      } catch (NoSuchFieldException nsfe) {
         ServerPlayerAPI.register("SinglePlayerCommands", SPCServerPlayerBase.class);;
      }
   }
}
