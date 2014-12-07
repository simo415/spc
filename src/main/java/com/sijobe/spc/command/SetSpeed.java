package com.sijobe.spc.command;

import com.sijobe.spc.util.AccessHelper;
import com.sijobe.spc.util.FontColour;
import com.sijobe.spc.util.Settings;
import com.sijobe.spc.validation.Parameter;
import com.sijobe.spc.validation.ParameterString;
import com.sijobe.spc.validation.Parameters;
import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandSender;

import java.util.List;

import net.minecraft.entity.player.PlayerCapabilities;

/**
 * The set speed command allows you to set the speed that the player moves at. 
 * By default the player speed is 1 where player speed 2 is twice as fast, 
 * etc.. 
 *
 * @author simo_415
 * @version 1.1
 * @status broken through 1.7.2 update -> fixed
 */
@Command (
		name = "setspeed",
		description = "Sets the players speed to the value specified",
		example = "2",
		videoURL = "http://www.youtube.com/watch?v=G48upLnQr-s",
		version = "1.3",
		enabled = true
		)
public class SetSpeed extends StandardCommand {	
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
				float speed = Float.parseFloat((String)params.get(0));
				config.set(CONFIG_KEY, speed);
				try {
					PlayerCapabilities capabilities = super.getSenderAsPlayer(sender).getMinecraftPlayer().capabilities;
					AccessHelper.setFloat(capabilities, "walkSpeed", speed/10);
					AccessHelper.setFloat(capabilities, "flySpeed", speed/20);
					super.getSenderAsPlayer(sender).getMinecraftPlayer().sendPlayerAbilities();
				} catch (NoSuchFieldException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (NumberFormatException e) {
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
}
