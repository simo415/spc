package com.sijobe.spc.worldedit;

import com.sijobe.spc.util.FontColour;
import com.sijobe.spc.wrapper.Coordinate;
import com.sijobe.spc.wrapper.Player;
import com.sk89q.worldedit.ServerInterface;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldVector;
import com.sk89q.worldedit.bags.BlockBag;


public class LocalPlayer extends com.sk89q.worldedit.LocalPlayer  {

   private Player player;
   
   protected LocalPlayer(Player player, ServerInterface server) {
      super(server);
      this.player = player;
   }

   @Override
   public String[] getGroups() {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public BlockBag getInventoryBlockBag() {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public int getItemInHand() {
      return player.getCurrentItem();
   }

   @Override
   public String getName() {
      return player.getPlayerName();
   }

   @Override
   public double getPitch() {
      return player.getPitch();
   }

   @Override
   public WorldVector getPosition() {
      Coordinate c = player.getPosition();
      return new WorldVector(getWorld(), c.getX(), c.getY(), c.getZ());
   }

   @Override
   public com.sk89q.worldedit.LocalWorld getWorld() {
      return new LocalWorld(player.getWorld());
   }

   @Override
   public double getYaw() {
      return player.getYaw();
   }

   @Override
   public void giveItem(int type, int quantity) {
      player.givePlayerItem(type, quantity);
   }

   @Override
   public boolean hasPermission(String arg0) {
      // TODO Check permissions
      return true;
   }

   @Override
   public void print(String message) {
      player.sendChatMessage(message);
   }

   @Override
   public void printDebug(String message) {
      System.out.println("WORLDEDIT-DEBUG: " + message);
   }

   @Override
   public void printError(String message) {
      player.sendChatMessage(FontColour.RED + message);
   }

   @Override
   public void printRaw(String message) { // TODO: Fix
      player.sendChatMessage(message);
   }

   @Override
   public void setPosition(Vector pos, float pitch, float yaw) {
      player.setPosition(new Coordinate(pos.getX(),pos.getY(),pos.getZ()));
      player.setPitch(pitch);
      player.setYaw(yaw);
   }
   
   @Override
   public boolean equals(Object object) {
      if (object != null && object instanceof LocalPlayer) {
         return ((LocalPlayer)object).getName().equals(getName());
      }
      return false;
   }
}
