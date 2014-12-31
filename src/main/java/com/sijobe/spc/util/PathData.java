package com.sijobe.spc.util;

import com.sijobe.spc.wrapper.Block;

public class PathData
{
	public Block block;
	public int meta;
	public int size;
	public int prevx;
	public int prevy;
	public int prevz;

	public PathData(Block block, int meta, int size)
	{
		this.block = block;
		this.meta = meta;
		this.size = size;
		this.prevx = -1;
		this.prevy = -1;
		this.prevz = -1;
	}
}
