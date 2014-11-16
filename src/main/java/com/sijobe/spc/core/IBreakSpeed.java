package com.sijobe.spc.core;

import com.sijobe.spc.wrapper.Block;
import com.sijobe.spc.wrapper.Player;


/**
 * Allows commands to change break speed
 * Basically a wrapper for forge's BreakSpeed event
 * @author aucguy
 *
 */
public interface IBreakSpeed extends IHook {
	/**called when a player damages a block
	 * 
	 * @param player - the player that the event occurred on
	 * @param Block - the block being damaged
	 * @param metadata - the metadata of the block
	 * @param originalSpeed - the orginal break speed of the block
	 * @param x - the x positon of the block
	 * @param y - the y position of the block
	 * @param z - the z position of the block
	 * @return the new breaking speed
	 * */
	
	public float getBreakSpeed(Player player, Block block, int metadata, float orginalSpeed, int x, int y, int z);
}
