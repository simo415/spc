package com.sijobe.spc.command;

import java.util.List;

import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Coordinate;
import com.sijobe.spc.wrapper.Entity;
import com.sijobe.spc.wrapper.Player;

/**
 * Creates an endercrystal where the player is pointing
 *
 * @author simo_415
 * @version 1.0
 */
@Command (
   name = "endercrystal",
   description = "Creates an Ender Crystal where the player is pointing",
   example = "",
   videoURL = "", // TODO
   version = "1.4.6"
)
public class EnderCrystal extends StandardCommand {

	@Override
	public void execute(CommandSender sender, List<?> params)
			throws CommandException {
		Player player = super.getSenderAsPlayer(sender);
		Coordinate c = player.trace(128D);
		if (c == null) {
			throw new CommandException("No block within range.");
		}
		
		Entity.spawnEntity("EnderCrystal", c, player.getWorld());
	}
}
