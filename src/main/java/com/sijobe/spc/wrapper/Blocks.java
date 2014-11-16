package com.sijobe.spc.wrapper;

public class Blocks
{
	public static Block air;
	public static Block fire;
	public static Block glass;
	
	static void init()
	{
		air = (Block) Block.blockRegistry.getObject("minecraft:air");
		fire = (Block) Block.blockRegistry.getObject("minecraft:fire");
		glass = (Block) Block.blockRegistry.getObject("minecraft:glass");
	}
}
