package com.sijobe.spc.network;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class PacketConfig implements IMessage {
	Config config;
	Object value;
	
	public PacketConfig() {
	}
	
	public PacketConfig(Config<?> config, Object value) {
		this.config = config;
		this.value = value;
		if(!this.value.getClass().equals(this.config.kind)) {
			throw(new IllegalArgumentException("value must be of type "+this.config.kind.getName()+
					" but instead was of type "+this.value.getClass().getName()));
		}
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		byte id = buf.readByte();
		if(id > Config.configs.length || Config.configs[id] == null) {
			return;
		}
		this.config = Config.configs[id];
		if(this.config.kind.equals(Boolean.class)) {
			this.value = buf.readBoolean();
		}
		else if(this.config.kind.equals(Integer.class)) {
			this.value = buf.readInt();
		}
		else if(this.config.kind.equals(Double.class)) {
			this.value = buf.readDouble();
		}
		else if(this.config.kind.equals(Float.class)) {
			this.value = buf.readFloat();
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeByte(this.config.id);
		if(this.config.kind.equals(Boolean.class)) {
			buf.writeBoolean((Boolean) this.value);
		}
		else if(this.config.kind.equals(Integer.class)) {
			buf.writeInt((Integer) this.value);
		}
		else if(this.config.kind.equals(Double.class)) {
			buf.writeDouble((Double) this.value);
		}
		else if(this.config.kind.equals(Float.class)) {
			buf.writeFloat((Float) this.value);
		}
	}
}
