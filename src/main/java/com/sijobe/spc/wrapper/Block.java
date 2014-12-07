package com.sijobe.spc.wrapper;

import java.util.HashMap;
import java.util.Map;

import com.sijobe.spc.util.RegistryIdCompatible;

import net.minecraft.util.RegistryNamespaced;

public class Block
{
	public static final RegistryNamespaced blockRegistry = new RegistryIdCompatible();
	public static final Map<net.minecraft.block.Block, Block> conversionRegistry = new HashMap<net.minecraft.block.Block, Block>();
	public static final RegistryNamespaced realBlockRegistry = net.minecraft.block.Block.blockRegistry;
	
	public static void init()
	{
		/*registers blocks*/
		for(Object i : realBlockRegistry)
		{
			net.minecraft.block.Block block = (net.minecraft.block.Block) i;
			int id = realBlockRegistry.getIDForObject(block);
			Block wrapped = new Block(block);
			blockRegistry.addObject(id, realBlockRegistry.getNameForObject(block), wrapped);
			conversionRegistry.put(block, wrapped);
		}
		Blocks.init();
	}
	
	/*returns the block with the given id*/
	public static Block fromId(int id)
	{	
		return (Block) blockRegistry.getObject(realBlockRegistry.getNameForObject(realBlockRegistry.getObjectById(id)));
	}
	
	/*converts a minecraft block to a wrapped block. opposite of Block.convert()*/
	public static Block fromMinecraftBlock(net.minecraft.block.Block block)
	{
		return conversionRegistry.get(block);
	}
	
	protected net.minecraft.block.Block block;
	
	public Block(net.minecraft.block.Block block)
	{
		this.block = block;
	}
	
	/*returns the equivant minecraft block*/
	public net.minecraft.block.Block convert()
	{
		return this.block;
	}
}
