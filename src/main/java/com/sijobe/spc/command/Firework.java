package com.sijobe.spc.command;

import java.util.List;

import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Coordinate;
import com.sijobe.spc.wrapper.Entity;
import com.sijobe.spc.wrapper.Player;

/**
 * Creates an endercrystal where the player is pointing //TODO why does it say endercrystal?
 *
 * @author simo_415
 * @version 1.0
 * @status broken through 1.7.2 update (worked before?)
 */
@Command (
   name = "firework",
   description = "Launches a random firework in the direction of the player",
   example = "",
   videoURL = "", // TODO
   version = "1.4.6",
   enabled = false
)
public class Firework extends StandardCommand {

	@Override
	public void execute(CommandSender sender, List<?> params)
			throws CommandException {
		Player player = super.getSenderAsPlayer(sender);
		Coordinate c = player.trace(128D);
		Entity.spawnEntity("FireworksRocketEntity", c, player.getWorld());
	}

}
