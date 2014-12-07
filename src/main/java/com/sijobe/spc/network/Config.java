package com.sijobe.spc.network;

public final class Config<T> {
	static Config<?>[] configs = new Config[256];
	public static final Config<Float> BLOCK_REACH = new Config<Float>(0, Float.class);
	public static final Config<Boolean> NOCLIP = new Config<Boolean>(1, Boolean.class);
	public static final Config<Boolean> LIGHT = new Config<Boolean>(2, Boolean.class);
	public static final Config<Boolean> INSTANT_MINE = new Config<Boolean>(3, Boolean.class);
	public static final Config<Float> LONGER_LEGS = new Config<Float>(4, Float.class);
	public static final Config<Integer> BIND = new Config<Integer>(5, Integer.class);
	
	int id;
	Class<T> kind;
	private IClientConfig<T> handler;
	
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
