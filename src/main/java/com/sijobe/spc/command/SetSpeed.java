package com.sijobe.spc.command;

import com.sijobe.spc.core.IPlayerSP;
import com.sijobe.spc.util.FontColour;
import com.sijobe.spc.util.Settings;
import com.sijobe.spc.validation.Parameter;
import com.sijobe.spc.validation.ParameterString;
import com.sijobe.spc.validation.Parameters;
import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Coordinate;
import com.sijobe.spc.wrapper.Minecraft;
import com.sijobe.spc.wrapper.Player;

import java.util.List;

/**
 * The set speed command allows you to set the speed that the player moves at. 
 * By default the player speed is 1 where player speed 2 is twice as fast, 
 * etc.. 
 *
 * @author simo_415
 * @version 1.1
 * @status broken through 1.7.2 update
 */
@Command (
   name = "setspeed",
   description = "Sets the players speed to the value specified",
   example = "2",
   videoURL = "http://www.youtube.com/watch?v=G48upLnQr-s",
   version = "1.3",
   enabled = true
)
public class SetSpeed extends StandardCommand implements IPlayerSP {
   /**
    * The parameters of the command
    */
   private static final Parameters PARAMETERS = new Parameters (
      new Parameter[] {
         new ParameterString("<SPEED|reset>", false),
      }
   );

   /**
    * The default speed multiplier that the player moves
    */
   private static final double DEFAULT_SPEED = 1;
   
   /**
    * The key that is saved into the config
    */
   private static final String CONFIG_KEY = "speed";

   /**
    * @see com.sijobe.spc.wrapper.CommandBase#execute(com.sijobe.spc.wrapper.CommandSender, java.util.List)
    */
   @Override
   public void execute(CommandSender sender, List<?> params) throws CommandException {
      Settings config = super.loadSettings(super.getSenderAsPlayer(sender));
      if (((String)params.get(0)).equalsIgnoreCase("reset")) {
         config.set(CONFIG_KEY, DEFAULT_SPEED);
      } else {
         try {
            config.set(CONFIG_KEY, Double.parseDouble((String)params.get(0)));
         } catch (Exception e) {
            throw new CommandException("Could not parse " + (String)params.get(0) + " as a speed.");
         }
      }
      config.save();
      sender.sendMessageToPlayer("Player speed set to " + FontColour.AQUA 
               + config.getDouble(CONFIG_KEY, DEFAULT_SPEED) + FontColour.WHITE + "x normal speed");
      }

   /**
    * @see com.sijobe.spc.wrapper.CommandBase#getParameters()
    */
   @Override
   public Parameters getParameters() {
      return PARAMETERS;
   }

   /**
    * @see com.sijobe.spc.core.IHook#init(java.lang.Object[])
    */
   @Override
   public void init(Object... params) {
   }

   /**
    * @see com.sijobe.spc.core.IPlayerMP#onTick(com.sijobe.spc.wrapper.Player)
    */
   @Override
   public void onTick(Player player) {
   }
   
   /**
    * Only enabled in single player since this is a client-side mod
    * 
    * @see com.sijobe.spc.wrapper.CommandBase#isEnabled()
    */
   @Override
   public boolean isEnabled() {
      return Minecraft.isSinglePlayer();
   }

   /**
    * @see com.sijobe.spc.core.IPlayerSP#movePlayer(float, float, float)
    */
   @Override
   public void movePlayer(Player player, float forward, float strafe, float speed) {
      Double pspeed = super.loadSettings(player).getDouble(CONFIG_KEY, DEFAULT_SPEED);
      if (pspeed == null || pspeed == 1) {
         return;
      }
      pspeed--;
      double direction = Math.sqrt((forward * forward) + (strafe * strafe));
      if (direction < 0.01F) {
         return;
      }
      if (direction < 1.0D) {
         direction = 1D;
      }
      direction = speed / direction;
      forward *= direction;
      strafe *= direction;
      double f4 = Math.sin(player.getYaw() * Math.PI / 180.0F);
      double f5 = Math.cos(player.getYaw() * Math.PI / 180.0F);
      Coordinate motion = player.getMotion();
      double motionx = forward * f5 - strafe * f4;
      double motionz = strafe * f5 + forward * f4;
      player.setMotion(new Coordinate(motion.getX() + motionx * pspeed, motion.getY(), motion.getZ() + motionz * pspeed));
   }
}
