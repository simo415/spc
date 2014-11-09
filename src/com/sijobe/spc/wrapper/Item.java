package com.sijobe.spc.wrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.util.RegistryNamespaced;
import net.minecraft.util.StatCollector;

public class Item {

   /**
    * A list of item names that are loaded in the game
    */
	public static final RegistryNamespaced itemRegistry = new RegistryNamespaced();
	public static final Map<net.minecraft.item.Item, Item> conversionRegistry = new HashMap<net.minecraft.item.Item, Item>();
	public static final RegistryNamespaced realItemRegistry = net.minecraft.item.Item.itemRegistry;
	
	public static void init()
	{
		for (Object i : net.minecraft.item.Item.itemRegistry)
		{
			net.minecraft.item.Item item = (net.minecraft.item.Item) i;
			Item wrapped = new Item(item);
			itemRegistry.putObject(realItemRegistry.getNameForObject(item), wrapped);
			conversionRegistry.put(item, wrapped);
		}
	}
   
   protected net.minecraft.item.Item item;
   
   public Item(net.minecraft.item.Item item)
   {
	   this.item = item;
   }
   
   /*convert a wrapped item into a minecraft item*/
   public net.minecraft.item.Item convert()
   {
	   return this.item;
   }
   
   /**
    * Translates the item code name into the item display name
    * 
    * @param toTranslate - The item name to translate
    * @return The display name of the item
    */
   /*public static String translateItemName(String toTranslate) {
      return StringTranslate.getInstance().translateNamedKey(toTranslate).toString().trim();
   }*/

/**
    * Gets the item id of the specified item denoted by the string name
    * 
    * @param itemName - The name of the item 
    * @return the id of the item. If the item doesn't exist null is returned
    */
   public static Item getItem(String itemName) {
      if(itemRegistry.containsKey(itemName))
      {
    	  return (Item) itemRegistry.getObject(itemName);
      }
      else
      {
    	  return null;
      }
   }
   
   /*returns the converts the item to a minecraft item*/
   public static Item getItem(net.minecraft.item.Item item)
   {
	   return conversionRegistry.get(item);
   }

   /**
    * Returns true if the specified item id denotes a valid item
    * 
    * @param id - The id of the item to check
    * @return true is returned if the id is valid, false otherwise
    */
   public static boolean isValidItem(Item item) {
	   return itemRegistry.containsKey(item);
   }

   /**TODO: remove arguement
    * Gets the maximum stack size of the specified item
    * 
    * @param id - does nothing
    * @return The stack size of the item, or 0 if not valid
    */
   public int getMaxStack(Item id) {
	   return this.item.getItemStackLimit();
   }

   /**
    * Gets a list containing all of the loaded enchantments
    * 
    * @return The enchantment list
    */
   public static List<String> getEnchantments() {
      List<Enchantment> enchantments = Arrays.asList(Enchantment.enchantmentsList);
      List<String> names = new ArrayList<String>();
      for (Enchantment enchantment : enchantments) {
         if (enchantment != null) {
            names.add(StatCollector.translateToLocal(enchantment.getName()).replace(' ', '_'));
         } else {
            names.add(null);
         }
      }
      return names;
   }

   /**
    * Adds the specified enchantment to the currently selected item in the 
    * players inventory
    *  
    * @param player - The player to add the enchantment to
    * @param enchantment - The enchantment to add
    * @param level - The level of the enchantment
    * @return The name of the command added, or null if there was an issue
    */
   public static String addEnchantmentToCurrentItem(Player player, int enchantment, int level) {
      try {
         if (Enchantment.enchantmentsList[enchantment] == null) {
            return null;
         }
         player.getMinecraftPlayer().inventory.mainInventory[player.getMinecraftPlayer().inventory.currentItem].addEnchantment(Enchantment.enchantmentsList[enchantment], level);
         return Enchantment.enchantmentsList[enchantment].getTranslatedName(level);
      } catch (Exception e) {
         return null;
      }
   }
   
   /**
    * Removes all of the enchantments of the currently selected item
    * 
    * @param player - The player to remove the enchantments on
    */
   public static void removeEnchantmentsOnCurrentItem(Player player) {
      player.getMinecraftPlayer().inventory.mainInventory[player.getMinecraftPlayer().inventory.currentItem].stackTagCompound = null;
   }
   
   /**
    * Resets the damage of the item stack in the specified slot of the player.
    * if there is no item in the specified slot then nothing happens.
    * 
    * @param player - The player to change the item
    * @param slot - The slot of the inventory
    */
   public static void resetDamageOnItem(Player player, int slot) {
      if (slot < 0 || slot >= player.getMinecraftPlayer().inventory.mainInventory.length) {
         return;
      }
      ItemStack item = player.getMinecraftPlayer().inventory.mainInventory[slot];
      if (item == null) {
         return;
      }
      if (item.getHasSubtypes() || !item.isItemDamaged()) {
         return;
      }
      item.setItemDamage(0);
   }
}
