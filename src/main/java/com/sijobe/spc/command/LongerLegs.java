package com.sijobe.spc.command;

import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;

import com.sijobe.spc.ModSpc;
import com.sijobe.spc.network.Config;
import com.sijobe.spc.network.IClientConfig;
import com.sijobe.spc.network.PacketConfig;
import com.sijobe.spc.util.FontColour;
import com.sijobe.spc.wrapper.CommandException;
import com.sijobe.spc.wrapper.CommandSender;
import com.sijobe.spc.wrapper.Player;

/**
 * Changes the player step height to allow the player to step up blocks that
 * are one high. The default step height is 0.5 blocks. This command currently
 * only functions in single player since player step height is not 
 * synchronised. 
 *
 * @author simo_415
 * @version 1.0
 * @status survived 1.7.2 update
 */
@Command (
   name = "longerlegs",
   description = "Changes the player leg height step height",
   example = "",
   videoURL = "http://www.youtube.com/watch?v=1Qy2jhvCkDk",
   version = "1.4.6"
)
public class LongerLegs extends StandardCommand implements IClientConfig<Float> {

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public void execute(CommandSender sender, List<?> params)
			throws CommandException {
		Player player = getSenderAsPlayer(sender);
		if (player.getStepHeight() != 0.5F) {
			player.setStepHeight(0.5F);
			sender.sendMessageToPlayer("Longer legs is "+FontColour.AQUA+"disabled");
		} else {
			player.setStepHeight(1.0F);
			sender.sendMessageToPlayer("Longer legs is "+FontColour.AQUA+"enabled");
		}
		ModSpc.instance.networkHandler.sendTo(new PacketConfig(this.getConfig(), player.getStepHeight()), (EntityPlayerMP) player.getMinecraftPlayer());
	}

	@Override
	public void init(Object... params) {
	}

	@Override
	public void onConfigRecieved(Float value) {
		System.out.println("foo");
		ModSpc.instance.proxy.getClientPlayer().setStepHeight(value);
	}

	@Override
	public Config<Float> getConfig() {
		return Config.LONGER_LEGS;
	}
}