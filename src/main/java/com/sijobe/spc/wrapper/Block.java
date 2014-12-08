package com.sijobe.spc.wrapper;

import java.util.HashMap;
import java.util.Map;

import com.sijobe.spc.util.RegistryIdCompatible;

import net.minecraft.util.RegistryNamespaced;

/**
 * a wrapper block class
 * 
 * @author aucguy
 * @version 1.0
 */
public class Block {
   /**
    * the wrapped block registry
    */
   public static final RegistryNamespaced blockRegistry = new RegistryIdCompatible();
   
   /**
    * minecraft to wrapped block bindings
    */
   public static final Map<net.minecraft.block.Block, Block> conversionRegistry = new HashMap<net.minecraft.block.Block, Block>();
   
   /**
    * the minecraft block registry
    */
   public static final RegistryNamespaced realBlockRegistry = net.minecraft.block.Block.blockRegistry;
   
   /**
    * initializes the block and conversion registry
    */
   public static void init() {
      /* registers blocks */
      for (Object i : realBlockRegistry) {
         net.minecraft.block.Block block = (net.minecraft.block.Block) i;
         int id = realBlockRegistry.getIDForObject(block);
         Block wrapped = new Block(block);
         blockRegistry.addObject(id, realBlockRegistry.getNameForObject(block), wrapped);
         conversionRegistry.put(block, wrapped);
      }
      Blocks.init();
   }
   
   /**
    * 
    * @param id - the id of the block
    * @return the block with the given id
    */
   public static Block fromId(int id) {
      return (Block) blockRegistry.getObject(realBlockRegistry.getNameForObject(realBlockRegistry.getObjectById(id)));
   }
   
   /**
    * converts a minecraft block to a wrapped block. opposite of Block.convert()
    * 
    * @param block - the minecraft block to convert
    * @return - the wrapped block
    */
   public static Block fromMinecraftBlock(net.minecraft.block.Block block) {
      return conversionRegistry.get(block);
   }
   
   /**
    * the minecraft block associated with this instance
    */
   protected net.minecraft.block.Block block;
   
   /**
    * constructs a wrapped block
    * @param block - the minecraft block
    */
   public Block(net.minecraft.block.Block block) {
      this.block = block;
   }
   
   /**
    * @return the equivant minecraft block
    */
   public net.minecraft.block.Block convert() {
      return this.block;
   }
}
