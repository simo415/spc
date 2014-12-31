package com.sijobe.spc.proxy;

import java.io.File;

import com.sijobe.spc.wrapper.Player;

import net.minecraft.world.EnumDifficulty;

/**
 * used so that side specific classes don't load on the wrong side
 * 
 * @author aucguy
 * @version 1.0
 */
public abstract class Proxy {
   /**
    * @return file directory for the Minecraft Client
    */
   public abstract File getDataDirectory();
   
   /**
    * @return the player on the client
    */
   public abstract Player getClientPlayer();
   
   /**
    * @return whethor or not minecraft should render the inside of a block while
    *         inside of a block
    */
   public abstract boolean shouldNotRenderInsideOfBlock();
   
   /**
    * sets the difficulty client-side (so it displays up correctly on the pause
    * menu)
    * 
    * @param difficulty - what to set the difficulty to
    */
   public abstract void setDifficulty(EnumDifficulty difficulty);
   
   /**
    * sets the client lighting status
    * 
    * @param isLit - false if the world is lit normally, true if the world is to
    *           be fully lit
    */
   public abstract void setClientLighting(boolean isLit);
   
   /**
    * returns whether or not the client has a gui screen open
    * 
    * @return
    */
   public abstract boolean isClientGuiScreenOpen();
   
   /**
    * sends a chat packet to the server
    * 
    * @param string - the chat to send
    */
   public abstract void sendClientChat(String string);
   
   /**
    * sets the block hit delay for the client. The block hit delay is how long to
    * wait before the player starts the mine the next block
    * 
    * @param delay - the block hit delay
    */
   public abstract void setClientHitDelay(int delay);
}
