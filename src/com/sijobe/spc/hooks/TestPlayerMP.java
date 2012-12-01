package com.sijobe.spc.hooks;

import com.sijobe.spc.core.PlayerMP;
import com.sijobe.spc.wrapper.Player;

public class TestPlayerMP extends PlayerMP {

   private boolean time;
   
   public TestPlayerMP() {
      time = false;
   }
   
   @Override
   public void onTick(Player player) {
      if (!time) {
         System.out.println("TestPlayerMP.onTick() was called");
         time = true;
      }
   }
   
   @Override
   public boolean isEnabled() {
      return false;
   }
}
