package com.sijobe.spc.command;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;

import com.sijobe.spc.util.Settings;
import com.sijobe.spc.validation.Parameter;
import com.sijobe.spc.validation.ParameterInteger;
import com.sijobe.spc.validation.ParameterString;
import com.sijobe.spc.validation.Parameters;
import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandManager;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.MinecraftServer;

/**
 * Executes the command bound to the given key.
 *
 * @author aucguy
 * @version 1.0
 */
@Command (
		name = "keypress",
		description = "Executes the command bound to the given key. Internally used command",
		videoURL = "none, as I said, this is an internally used command.",
		version = "1.0"
		)
public class Keypress extends StandardCommand {
	private static final Parameters PARAMETERS = new Parameters (
			new Parameter[] {
					new ParameterInteger("[keycode]",true)
			}
	);

	@Override
	public void execute(CommandSender sender, List<?> params) throws CommandException {
		Settings settings = this.loadSettings(getSenderAsPlayer(sender));
		String command = settings.getProperty(Bind.SETTINGS_PREFIX+params.get(0));
		CommandManager.runCommand(sender, command);
	}
}
