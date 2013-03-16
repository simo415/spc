package com.sijobe.spc.command;

import com.sijobe.spc.util.FontColour;
import com.sijobe.spc.util.Settings;
import com.sijobe.spc.validation.Parameter;
import com.sijobe.spc.validation.ParameterString;
import com.sijobe.spc.validation.Parameters;
import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Coordinate;
import com.sijobe.spc.wrapper.Player;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Commands that control waypoints, settings, removing and using them. 
 * Waypoints are saved per player
 *
 * @author simo_415
 * @version 1.0
 */
public class Waypoint extends MultipleCommands {

   public static final String PREFIX = "waypoint-";
   
   /**
    * The parameters of the command
    */
   private static final Parameters PARAMETERS = new Parameters (
      new Parameter[] {
         new ParameterString("<NAME>",false),
      }
   );
   
   public Waypoint(String name) {
      super(name);
   }
   
   @Override
   public boolean isEnabled() {
      return true;
   }

   @Override
   public String[] getCommands() {
      return new String[] {"set", "rem", "goto"};
   }

   @Override
   public void execute(CommandSender sender, List<?> params) throws CommandException {
      Player player = super.getSenderAsPlayer(sender);
      if (getName().equalsIgnoreCase("set")) {
         setWaypoint((String)params.get(0), player);
         player.sendChatMessage("Waypoint was set at " + FontColour.AQUA + getLocationAsString(player.getPosition()));
      } else if (getName().equalsIgnoreCase("rem")) {
         if (removeWaypoint((String)params.get(0), player)) {
            player.sendChatMessage("Waypoint was removed.");
         } else {
            throw new CommandException("Waypoint does not exist.");
         }
      } else if (getName().equalsIgnoreCase("goto")) {
         if (gotoWaypoint((String)params.get(0), player)) {
            player.sendChatMessage("Whoosh");
         } else {
            throw new CommandException("Waypoint does not exist.");
         }
      } else {
         assert false : "Unexpected call not being handled: " + params.get(0);
      }
      super.loadSettings(player).save();
   }
   
   private String getLocationAsString(Coordinate c) {
      if (c == null) {
         return "N/A";
      }
      DecimalFormat twodd = new DecimalFormat("#.##");
      return twodd.format(c.getX()) + "," + twodd.format(c.getY()) + "," + twodd.format(c.getZ());
   }
   
   /**
    * Sets a waypoint at the location the player is currently standing in.
    * 
    * @param name - The name of the waypoint
    * @param player - The player to add the waypoint for
    */
   public void setWaypoint(String name, Player player) {
      if (name == null || player == null) {
         return;
      }
      Settings config = super.loadSettings(player);
      Coordinate position = player.getPosition();
      String value = position.getX() + "," + 
                     position.getY() + "," + 
                     position.getZ() + "," + 
                     player.getYaw() + "," + 
                     player.getPitch();
      config.set((PREFIX + name).toLowerCase(), value);
   }
   
   /**
    * Removes the specified waypoint from the player. 
    * 
    * @param name - The name of the waypoint
    * @param player - The player to remove the waypoint from
    * @return True if the waypoint existed and was removed
    */
   public boolean removeWaypoint(String name, Player player) {
      if (name == null || player == null) {
         return false;
      }
      Settings config = super.loadSettings(player);
      return config.remove((PREFIX + name).toLowerCase()) != null;
   }
   
   public boolean gotoWaypoint(String name, Player player) {
      if (name == null || player == null) {
         return false;
      }
      Settings config = super.loadSettings(player);
      String waypoint = config.getString((PREFIX + name).toLowerCase(), null);
      if (waypoint == null) {
         return false;
      }
      String parts[] = waypoint.split(",");
      if (parts.length != 5) {
         return false;
      }
      double x = Double.parseDouble(parts[0]);
      double y = Double.parseDouble(parts[1]);
      double z = Double.parseDouble(parts[2]);
      float yaw = Float.parseFloat(parts[3]);
      float pitch = Float.parseFloat(parts[4]);
      player.setPosition(new Coordinate(x, y, z));
      player.setYaw(yaw);
      player.setPitch(pitch);
      return true;
   }

   @Override
   public Parameters getParameters() {
      return PARAMETERS;
   }
}
