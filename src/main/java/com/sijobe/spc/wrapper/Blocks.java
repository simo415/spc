package com.sijobe.spc.wrapper;

public class Blocks
{
	public static Block air;
	public static Block fire;
	public static Block glass;
	public static Block bedrock;
	public static Block end_portal;
	public static Block torch;
	public static Block dragon_egg;
	
	static void init()
	{
		air = (Block) Block.blockRegistry.getObject("minecraft:air");
		fire = (Block) Block.blockRegistry.getObject("minecraft:fire");
		glass = (Block) Block.blockRegistry.getObject("minecraft:glass");
		bedrock = (Block) Block.blockRegistry.getObject("minecraft:bedrock");
		end_portal = (Block) Block.blockRegistry.getObject("minecraft:end_portal");
		torch = (Block) Block.blockRegistry.getObject("minecraft:torch");
		dragon_egg = (Block) Block.blockRegistry.getObject("minecraft:dragon_egg");
	}
}
