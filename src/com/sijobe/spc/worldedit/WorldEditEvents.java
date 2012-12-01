package com.sijobe.spc.worldedit;

import com.sijobe.spc.core.PlayerSP;
import com.sijobe.spc.wrapper.Coordinate;
import com.sijobe.spc.wrapper.Minecraft;
import com.sijobe.spc.wrapper.Player;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.input.Mouse;

/**
 * Handles the client-side events that occur such as mouse events for 
 * WorldEdit. When the events are detected they are passed to the 
 * WorldEditCommandSet class.
 *
 * @author simo_415
 * @version 1.0
 */
public class WorldEditEvents extends PlayerSP {

   private Map<String,Coordinate> left;
   private Map<String,Coordinate> right;

   /**
    * The max range that the mouse methods trace the blocks for
    */
   private static final int TRACE_RANGE = 128;

   public WorldEditEvents() {
      left = new HashMap<String,Coordinate>();
      right = new HashMap<String,Coordinate>();
   }

   @Override
   public void onTick(Player player) {
      if (Minecraft.isGuiScreenOpen()) { 
         return;
      }
      try {
         // Check left button down
         checkLeftButton(player);

         // Check right button down
         checkRightButton(player);
      } catch (Throwable e) {
         //e.printStackTrace();
      }
   }

   /**
    * Checks if the left mouse button was pressed, if so then it will call the 
    * appropriate WorldEdit method for arm swinging or block hitting if they 
    * occur. 
    * 
    * @param player - The player that triggered the mouse button
    */
   private void checkLeftButton(Player player) {
      if (Mouse.isButtonDown(0)) {
         Coordinate hit = null;
         if ((hit = player.trace(TRACE_RANGE)) != null) {
            WorldEditCommandSet.getCurrentInstance().handleArmSwing(player);
            if (!hit.equals(left.get(player.getPlayerName()))) {
               left.put(player.getPlayerName(), hit);
               WorldEditCommandSet.getCurrentInstance().handleBlockLeftClick(player, hit);
            }
         }
      }
   }

   /**
    * Checks if the right mouse button was pressed, if so then it will call the
    * appropriate WorldEdit method for arm swinging or block hitting if they 
    * occur. 
    * 
    * @param player - The player that triggered the mouse button
    */
   private void checkRightButton(Player player) {
      if (Mouse.isButtonDown(1)) {
         Coordinate hit = null;
         if ((hit = player.trace(TRACE_RANGE)) != null) {
            WorldEditCommandSet.getCurrentInstance().handleRightClick(player);
            if (!hit.equals(right.get(player.getPlayerName()))) {
               right.put(player.getPlayerName(), hit);
               WorldEditCommandSet.getCurrentInstance().handleBlockRightClick(player, hit);
            }
         }
      }
   }
}
