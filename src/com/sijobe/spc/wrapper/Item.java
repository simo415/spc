package com.sijobe.spc.wrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.src.Enchantment;
import net.minecraft.src.ItemStack;
import net.minecraft.src.StatCollector;
import net.minecraft.src.StringTranslate;

public class Item {

   /**
    * A list of item names that are loaded in the game
    */
   private static final List<String> ITEM_NAMES;
   static {
      ITEM_NAMES = new ArrayList<String>();
      for (net.minecraft.src.Item item : net.minecraft.src.Item.itemsList) {
         if (item != null) {
            ITEM_NAMES.add(translateItemName(item.getItemName()).toLowerCase());
         } else {
            ITEM_NAMES.add(null);
         }
      }
   }

   /**
    * Translates the item code name into the item display name
    * 
    * @param toTranslate - The item name to translate
    * @return The display name of the item
    */
   public static String translateItemName(String toTranslate) {
      return StringTranslate.getInstance().translateNamedKey(toTranslate).toString().trim();
   }

   /**
    * Gets the item id of the specified item denoted by the string name
    * 
    * @param itemName - The name of the item 
    * @return the id of the item. If the item doesn't exist -1 is returned
    */
   public static int getItemId(String itemName) {
      return ITEM_NAMES.indexOf(itemName.toLowerCase());
   }

   /**
    * Returns true if the specified item id denotes a valid item
    * 
    * @param id - The id of the item to check
    * @return true is returned if the id is valid, false otherwise
    */
   public static boolean isValidItem(int id) {
      if (id < 0 || id > ITEM_NAMES.size()) {
         return false;
      }
      return ITEM_NAMES.get(id) != null;
   }

   /**
    * Gets the maximum stack size of the specified item
    * 
    * @param id - The id of the item 
    * @return The stack size of the item, or 0 if not valid
    */
   public static int getMaxStack(int id) {
      if (isValidItem(id)) {
         return net.minecraft.src.Item.itemsList[id].getItemStackLimit();
      }
      return 0;
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
      item.setItemDamage(0);
   }
}
