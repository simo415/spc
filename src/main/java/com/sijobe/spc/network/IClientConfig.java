package com.sijobe.spc.network;

import com.sijobe.spc.core.IHook;

public interface IClientConfig<T> extends IHook {
	public void onConfigRecieved(T value);
	Config<T> getConfig();
}
