package com.sijobe.spc.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;

/**
 * handles Config Packets from the server
 * 
 * @author aucguy
 * @version 1.0
 */
public class ConfigMessageHandler implements IMessageHandler<PacketConfig, IMessage> {
	@Override
	public IMessage onMessage(PacketConfig message, MessageContext ctx) {
		if(ctx.side != Side.CLIENT) {
			System.out.println("Config packet handled on server side. Abandon Ship!");
			return null;
		}
		
		if(message.value != null) {
			message.config.callHook(message.value);
		}
		return null;
	}
}
