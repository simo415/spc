package com.sijobe.spc.network;

/**
 * used to keep track of client side configuration somethings have to be done client side,
 * like lighting up the world
 * 
 * @author aucguy
 * @version 1.0
 * @param <T>
 */
public final class Config<T> {
   /**
    * the array of configs. used to bind configs to ids
    */
   static Config<?>[] configs = new Config[256];
   public static final Config<Float> BLOCK_REACH = new Config<Float>(0, Float.class);
   public static final Config<Boolean> NOCLIP = new Config<Boolean>(1, Boolean.class);
   public static final Config<Boolean> LIGHT = new Config<Boolean>(2, Boolean.class);
   public static final Config<Boolean> INSTANT_MINE = new Config<Boolean>(3, Boolean.class);
   public static final Config<Float> LONGER_LEGS = new Config<Float>(4, Float.class);
   public static final Config<Integer> BIND = new Config<Integer>(5, Integer.class);
   
   /**
    * the id of the Config. Used to find out the contents of a PacketConfig
    */
   int id;
   
   /**
    * the payload of the packet and its config type. Used as a workaround of Paramatized types
    */
   Class<T> kind;
   
   /**
    * the command that uses the client configurations
    */
   private IClientConfig<T> handler;
   
   /**
    * constructs a Config
    * @param id - the id of this config
    * @param kind - the payload type of this config
    */
   private Config(int id, Class<T> kind) {
      this.id = id;
      this.kind = kind;
      configs[this.id] = this;
   }
   
   /**
    * Sets the client configuration
    * @param value - the value of the config set from the server
    */
   void callHook(T value) {
      if (this.handler != null) {
         this.handler.onConfigRecieved(value);
      }
   }
   
   /**
    * sets the command that uses this config
    * @param handler - the command
    */
   public void setHandler(IClientConfig<T> handler) {
      this.handler = handler;
   }
}
