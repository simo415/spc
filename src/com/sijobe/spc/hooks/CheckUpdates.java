package com.sijobe.spc.hooks;

import com.sijobe.spc.core.Constants;
import com.sijobe.spc.core.PlayerMP;
import com.sijobe.spc.updater.CheckVersion;
import com.sijobe.spc.updater.ModVersion;
import com.sijobe.spc.updater.UpdateCallback;
import com.sijobe.spc.util.FontColour;
import com.sijobe.spc.wrapper.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

/**
 * Loaded on player load and checks for updates of the mod.
 * 
 * TODO: Fix updater so it isn't shit
 * TODO: Check once, alert users afterward
 *
 * @author simo_415
 */
public class CheckUpdates extends PlayerMP {

   /**
    * The players names who have already been alerted
    */
   private static List<String> ALERTED = new ArrayList<String>();
   
   /**
    * @see com.sijobe.spc.core.IPlayerMP#onTick(com.sijobe.spc.wrapper.Player)
    */
   @Override
   public void onTick(Player player) {
      if (!ALERTED.contains(player.getPlayerName())) {
         ALERTED.add(player.getPlayerName());
         checkForUpdates(player);
      }
   }
   
   /**
    * Checks for any updates to the mod and alerts the user
    * 
    * @param player - The player that asked for an update check
    */
   private void checkForUpdates(final Player player) {
      (new CheckVersion(
         new ModVersion[]{Constants.SPC_VERSION},
         "",
         new UpdateCallback() { 
            @Override 
            public void updateCallback(Vector<HashMap<String,Object>> s) {
               processUpdate(s, player);
            }
         })
      ).start();
   }
   
   /**
    * When an update is detected it alerts the player
    * 
    * @param s 
    * @param player - The player to send the alert to
    */
   private void processUpdate(Vector<HashMap<String,Object>> s, Player player) {
      if (s != null && s.size() != 0) {
         for (HashMap<String,Object> t : s) {
            if (t.get("message") == null || ((String)t.get("message")).equalsIgnoreCase("")) {
               player.sendChatMessage(FontColour.AQUA + "" + t.get("name") + " V" + t.get("version") + " now out! " + t.get("website"));
            } else {
               player.sendChatMessage((String)t.get("message"));
            }
         }
      }
   }
}
