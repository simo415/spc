package com.sijobe.spc.network;

import com.sijobe.spc.core.IHook;

/**
 * used to signify that a command uses client-sided configurations
 * 
 * @author aucguy
 * 
 * @param <T> - the payload and configuration type
 */
public interface IClientConfig<T> extends IHook {
   public void onConfigRecieved(T value);
   
   Config<T> getConfig();
}
