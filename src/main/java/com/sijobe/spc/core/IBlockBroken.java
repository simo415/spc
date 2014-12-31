package com.sijobe.spc.core;

import com.sijobe.spc.wrapper.Block;
import com.sijobe.spc.wrapper.Player;
import com.sijobe.spc.wrapper.World;

public interface IBlockBroken extends IHook {
	public void onBreakBroken(int x, int y, int z, World world, Block block, int metadata, Player player);
}
