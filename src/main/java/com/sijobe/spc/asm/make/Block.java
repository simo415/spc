package com.sijobe.spc.asm.make;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class Block
{
	private int blockHitDelay;
	
	public float getPlayerRelativeBlockHardness(EntityPlayer p_149737_1_, World p_149737_2_, int p_149737_3_, int p_149737_4_, int p_149737_5_)
    {
		if(com.sijobe.spc.command.InstantMine.instantMiningEnabled)
		{
			this.blockHitDelay = 5;
			return 1.1F;
		}
		return p_149737_5_;
    }
}
