package com.sijobe.spc.network;

import java.lang.reflect.Method;

public final class Config<T> {
	static Config[] configs = new Config[256];
	public static final Config BLOCK_REACH = new Config(0, Float.class);
	public static final Config NOCLIP = new Config(1, Boolean.class);
	public static final Config LIGHT = new Config(2, Boolean.class);
	public static final Config INSTANT_MINE = new Config(3, Boolean.class);
	public static final Config PREFIX_SLASH = new Config(4, Boolean.class);
	public static final Config LONGER_LEGS = new Config(5, Float.class);
	public static final Config BIND = new Config(6, Integer.class);
	
	int id;
	Class<T> kind;
	private IClientConfig handler;
	
	private Config(int id, Class<T> kind) {
		this.id = id;
		this.kind = kind;
		configs[this.id] = this;
	}
	
	void callHook(T value) {
		if(this.handler != null) {
			this.handler.onConfigRecieved(value);
		}
	}
	
	public void setHandler(IClientConfig<T> handler) {
		this.handler = handler;
	}
}
