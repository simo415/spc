package com.sijobe.spc.command;

import com.sijobe.spc.util.FontColour;
import com.sijobe.spc.validation.Parameter;
import com.sijobe.spc.validation.ParameterString;
import com.sijobe.spc.validation.Parameters;
import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.World;

import java.util.List;

/**
 * Provides methods of changing the current Minecraft time
 *
 * @author simo_415
 */
@Command (
   name = "time",
   description = "Set and get the time within minecraft",
   example = "set 06:00",
   videoURL = "http://www.youtube.com/watch?v=W7T1XcSgJZA",
   version = "1.0"
)
public class Time extends StandardCommand {

   /**
    * The offset of days from the standard time
    */
   private static final int OFFSET_DAY = 0;
   /**
    * The offset of hours from the standard time
    */
   private static final int OFFSET_HOUR = 6;
   /**
    * The offset of minutes from the standard time
    */
   private static final int OFFSET_MINUTE = 0;
   /**
    * The time format that the getCurrentTime() method uses
    */
   public static final String TIME_FORMAT = FontColour.AQUA + "HH:MM" + 
                                            FontColour.WHITE + " on day " + 
                                            FontColour.AQUA + "DD";
   /**
    * The time at which the command <code>/time day</code> sets the time to
    */
   public static final String TIME_DAY = "06:00";
   /**
    * The time at which the command <code>/time night</code> sets the time to
    */
   public static final String TIME_NIGHT = "20:00";

   private static final Parameters PARAMETERS = new Parameters (
      new Parameter[] {
         new ParameterString("[set [TIME]|day|night]", true, new String[] {"set","day","night"}),
         new ParameterString("", true)
      }
   );

   @Override
   public void execute(CommandSender sender, List<?> params) throws CommandException {
      World world = super.getSenderAsPlayer(sender).getWorld();
      if (params.size() == 0) { // Gets the current time
      } else if (((String)params.get(0)).equalsIgnoreCase("day")) {
         setCurrentTime(world,TIME_DAY);
      } else if (((String)params.get(0)).equalsIgnoreCase("night")) {
         setCurrentTime(world,TIME_NIGHT);
      } else if (((String)params.get(0)).equalsIgnoreCase("set")) {
         if (params.size() == 1) {
            throw new CommandException("Must specify what time to set it to.");
         }
         setCurrentTime(world,(String)params.get(1));
      } else {
         throw new CommandException(FontColour.RED + getUsage(sender));
      }
      outputCurrentTime(sender);
   }

   /**
    * Sends the current time to the output
    * 
    * @param sender - The sender to send the output to
    */
   private void outputCurrentTime(CommandSender sender) {
      String time = getCurrentTime(super.getSenderAsPlayer(sender).getWorld());
      String message = "The current time is " + FontColour.AQUA + time;
      sender.sendMessageToPlayer(message);
   }

   /**
    * Gets a String representation of the current time
    * 
    * @return The current time formatted as a String
    */
   public String getCurrentTime(World world) {
      long worldtime = world.getTime();
      int DD = ((int) ((worldtime / 1000) / 24)) + OFFSET_DAY;
      int HH = ((int) ((worldtime / 1000) + OFFSET_HOUR) % 24);
      int MM = ((int) ((((worldtime % 1000) / 1000.0) * 60) + OFFSET_MINUTE) % 60);
      return TIME_FORMAT.replaceAll("DD", DD + "").
                         replaceAll("HH", HH < 10 ? "0" + HH : HH + "").
                         replaceAll("MM", MM < 10 ? "0" + MM : MM + "");
   }

   /**
    * Sets the current time in Minecraft by parsing the specified date String
    * 
    * @param time - The time to set the game to
    * @return True if the time was correctly set
    */
   public void setCurrentTime(World world, String time) throws CommandException {
      int hour = 0;
      int minute = 0;
      try {
         if (time.matches("[0-2][0-9]:[0-5][0-9]")) { // HH:MM
            String parts[] = time.split(":");
            hour = Integer.parseInt(parts[0]);
            if (hour > 23) {
               throw new CommandException("Hour out of range");
            }
            hour -= OFFSET_HOUR;
            minute = Integer.parseInt(parts[1]) - OFFSET_MINUTE;
         } else if (time.matches("[0-9]{0,19}")) {
            long ticks = Long.parseLong(time);
            world.setTime(ticks);
            return;
         } else {
            throw new CommandException("Unknown date format");
         }
         long newTime = ((world.getTime() / 1000) / 24); // Gets the current day
         newTime += 1; // Makes it the next day
         newTime *= 24000; // Converts it to days
         newTime += (hour % 24) * 1000; // Adds the hours on
         newTime += ((minute % 60) / 60.0) * 1000; // Adds the minutes on
         world.setTime(newTime);
      } catch (Exception e) {
         throw new CommandException("Could not parse date.");
      }
   }

   @Override
   public Parameters getParameters() {
      return PARAMETERS;
   }
}
