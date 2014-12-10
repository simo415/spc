package com.sijobe.spc.network;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class DummyMessage implements IMessage {

   @Override
   public void fromBytes(ByteBuf buf) {
   }

   @Override
   public void toBytes(ByteBuf buf) {
   }
}
